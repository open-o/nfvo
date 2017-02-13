# Copyright 2017 ZTE Corporation.
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
import threading
import traceback

from lcm.pub.exceptions import NSLCMException
from lcm.pub.utils.jobutil import JobUtil
from lcm.pub.utils.values import ignore_case_get

JOB_ERROR = 255
SCALE_TYPE = ("SCALE_NS", "SCALE_VNF")
logger = logging.getLogger(__name__)


class NSManualScaleService(threading.Thread):
    def __init__(self, ns_instance_id, request_data, job_id):
        super(NSManualScaleService, self).__init__()
        self.ns_instance_id = ns_instance_id
        self.request_data = request_data
        self.job_id = job_id
        self.scale_type = ''
        self.scale_vnf_data = ''
        self.vnf_scale_jobs = []

    def run(self):
        try:
            self.do_biz()
        except NSLCMException as e:
            JobUtil.add_job_status(self.job_id, JOB_ERROR, e.message)
        except:
            logger.error(traceback.format_exc())
            JobUtil.add_job_status(self.job_id, JOB_ERROR, 'ns scale fail')

    def do_biz(self):
        self.get_and_check_params()
        self.do_vnfs_scale()

    def get_and_check_params(self):
        self.scale_type = ignore_case_get(self.request_data, 'scaleType')
        if not self.scale_type or self.scale_type != SCALE_TYPE[1]:
            logger.error('scaleType parameter does not exist or value incorrect')
            raise NSLCMException('scaleType parameter does not exist or value incorrect')
        self.scale_vnf_data = ignore_case_get(self.request_data, 'scaleVnfData')
        if not self.scale_vnf_data:
            logger.error('scaleVnfData parameter does not exist or value incorrect')
            raise NSLCMException('scaleVnfData parameter does not exist or value incorrect')

    def do_vnfs_scale(self):
        for vnf_data in self.scale_vnf_data:
            vnf_scale_params = self.prepare_vnf_scale_params(vnf_data)
            vnf_scale_job_id = self.do_vnf_scale(vnf_scale_params)
            self.vnf_scale_jobs.append(vnf_scale_job_id)

    def prepare_vnf_scale_params(self, vnf_data):
        vnf_instance_id = ignore_case_get(vnf_data, 'vnfInstanceId')
        scale_by_step_data = ignore_case_get(vnf_data, 'scaleByStepData')
        result = {
            "vnfInstanceId": vnf_instance_id,
            "scaleByStepData": scale_by_step_data,
            "nsInstanceId": self.ns_instance_id
        }
        return result

    def do_vnf_scale(self, vnf_scale_params):
        # do_biz
        print vnf_scale_params
        return ""
