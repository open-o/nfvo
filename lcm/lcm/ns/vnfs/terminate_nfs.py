# Copyright 2016 ZTE Corporation.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
import logging
import traceback
import json

from threading import Thread

from lcm.ns.vnfs.wait_job import wait_job_finish
from lcm.pub.database.models import NfInstModel
from lcm.ns.vnfs.const import VNF_STATUS, NFVO_VNF_INST_TIMEOUT_SECOND
from lcm.pub.utils.restcall import req_by_msb
from lcm.pub.utils.values import ignore_case_get

from lcm.pub.utils.jobutil import JOB_MODEL_STATUS, JobUtil
from lcm.pub.exceptions import NSLCMException
from lcm.pub.msapi.vnfmdriver import send_nf_terminate_request


logger = logging.getLogger(__name__)


class TerminateVnfs(Thread):
    def __init__(self, data, vnf_inst_id, job_id):
        self.vnf_inst_id = vnf_inst_id
        self.job_id = job_id
        self.vnfm_inst_id = ''
        self.vnf_uuid = ''
        self.vnfm_job_id = ''
        self.terminationType = data['terminationType']
        self.gracefulTerminationTimeout = data['gracefulTerminationTimeout']
        self.initdata()

    def run(self):
        try:
            self.check_nf_valid()
            self.send_nf_terminate_to_vnfmDriver()
            self.wait_vnfm_job_finish()
            self.send_terminate_vnf_to_resMgr()
            self.delete_data_from_db()
        except NSLCMException as e:
            self.exception(e.message)
        except Exception:
            logger.error(traceback.format_exc())
            self.exception('unexpected exception')

    def set_vnf_status(self, vnf_inst_info):
        vnf_status = vnf_inst_info.status
        if (vnf_status == VNF_STATUS.TERMINATING):
            logger.info('[VNF terminate] VNF is dealing by other application,try again later.')
            raise NSLCMException(msgid='[VNF terminate] VNF is dealing by other application,try again later.')
        else:
            vnf_inst_info.status = VNF_STATUS.TERMINATING
            vnf_inst_info.save()

    def check_vnf_is_exist(self):
        vnf_inst = NfInstModel.objects.filter(nfinstid=self.vnf_inst_id)
        if not vnf_inst.exists():
            logger.warning('[VNF terminate] Vnf terminate [%s] is not exist.' % self.vnf_inst_id)
            return None
        return vnf_inst[0]

    def add_progress(self, progress, status_decs, error_code=""):
        JobUtil.add_job_status(self.job_id, progress, status_decs, error_code)

    def initdata(self):
        vnf_inst_info = self.check_vnf_is_exist()
        if not vnf_inst_info:
            self.add_progress(100, "TERM_VNF_NOT_EXIST_SUCCESS", "finished")
        self.add_progress(2, "GET_VNF_INST_SUCCESS")
        self.vnfm_inst_id = vnf_inst_info.vnfm_inst_id
        self.vnf_uuid = vnf_inst_info.mnfinstid
        if not self.vnf_uuid:
            self.add_progress(100, "TERM_VNF_NOT_EXIST_SUCCESS", "finished")

    def check_nf_valid(self):
        vnf_inst = NfInstModel.objects.filter(nfinstid=self.vnf_inst_id)
        if not vnf_inst.exists():
            logger.warning('[VNF terminate] Vnf instance [%s] is not exist.' % self.vnf_inst_id)
            raise NSLCMException(msgid='[VNF terminate] Vnf instance is not exist.')
        if not vnf_inst:
            self.add_progress(100, "TERM_VNF_NOT_EXIST_SUCCESS", "finished")
            raise NSLCMException(msgid='[VNF terminate] Vnf instance is not exist.')
        self.set_vnf_status(vnf_inst[0])

    def exception(self, error_msg):
        logger.error('VNF Terminate failed, detail message: %s' % error_msg)
        NfInstModel.objects.filter(nfinstid=self.vnf_inst_id).update(status=VNF_STATUS.FAILED)
        JobUtil.add_job_status(self.job_id, 255, 'VNF Terminate failed, detail message: %s' % error_msg, 0)

    def send_nf_terminate_to_vnfmDriver(self):
        req_param = json.JSONEncoder().encode({
            'terminationType': self.terminationType, 
            'gracefulTerminationTimeout': self.gracefulTerminationTimeout})
        rsp = send_nf_terminate_request(self.vnfm_inst_id, self.vnf_inst_id, req_param)
        self.vnfm_job_id = ignore_case_get(rsp, 'jobId')

    def send_terminate_vnf_to_resMgr(self):
        uri = '/openoapi/resmgr/v1/vnf'
        req_param = {}
        ret = req_by_msb(uri, "DELETE", json.dumps(req_param))
        if ret[0] > 0:
            logger.error('Send terminate VNF request to resmgr failed.')
            raise NSLCMException('Send terminate VNF request to resmgr failed.')

    def wait_vnfm_job_finish(self):
        ret = wait_job_finish(vnfm_id=self.vnfm_inst_id, vnfo_job_id=self.job_id, 
            vnfm_job_id=self.vnfm_job_id, progress_range=[10, 90],
            timeout=NFVO_VNF_INST_TIMEOUT_SECOND)

        if ret != JOB_MODEL_STATUS.FINISHED:
            logger.error('VNF terminate failed on VNFM side.')
            raise NSLCMException(msgid='VNF terminate failed on VNFM side.')

    def delete_data_from_db(self):
        NfInstModel.objects.filter(nfinstid=self.vnf_inst_id).delete()
        JobUtil.add_job_status(self.job_id, 100, 'vnf terminate success', 0)
