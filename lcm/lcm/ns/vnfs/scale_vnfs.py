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

from lcm.pub.database.models import NfInstModel
from lcm.pub.exceptions import NSLCMException
from lcm.pub.utils.jobutil import JobUtil, JOB_TYPE
from lcm.pub.utils.values import ignore_case_get

JOB_ERROR = 255
SCALE_TYPE = ("SCALE_OUT", "SCALE_IN")
logger = logging.getLogger(__name__)


class NFManualScaleService(threading.Thread):
    def __init__(self, vnf_instance_id, data):
        super(NFManualScaleService, self).__init__()
        self.vnf_instance_id = vnf_instance_id
        self.data = data
        self.job_id = JobUtil.create_job("NF", JOB_TYPE.MANUAL_SCALE_VNF, vnf_instance_id)
        self.scale_vnf_data = ''

    def run(self):
        try:
            self.do_biz()
        except NSLCMException as e:
            JobUtil.add_job_status(self.job_id, JOB_ERROR, e.message)
        except:
            logger.error(traceback.format_exc())
            JobUtil.add_job_status(self.job_id, JOB_ERROR, 'nf scale fail')

    def do_biz(self):
        pass

    def get_and_check_params(self):
        nf_info = NfInstModel.objects.filter(nfinstid=self.vnf_instance_id)
        if not nf_info:
            logger.error('NF instance[id=%s] does not exist' % self.vnf_instance_id)
            raise NSLCMException('NF instance[id=%s] does not exist' % self.vnf_instance_id)
        self.scale_vnf_data = ignore_case_get(self.data, 'scaleVnfData')
        if not self.scale_vnf_data:
            logger.error('scaleVnfData parameter does not exist or value incorrect')
            raise NSLCMException('scaleVnfData parameter does not exist or value incorrect')
            # for vnf_data in self.scale_vnf_data:
