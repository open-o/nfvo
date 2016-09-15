# Copyright 2016 [ZTE] and others.
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
import json

from lcm.pub.utils.restcall import req_by_msb
from lcm.pub.msapi import resmgr

from lcm.pub.exceptions import NSLCMException


logger = logging.getLogger(__name__)



class GrantVnfs(object):
    def __init__(self, data, job_id):
        self.job_id = job_id
        self.vnfm_inst_id = ''
        self.vnf_uuid = ''
        self.vnfm_job_id = ''
        self.data = data

    def send_grant_vnf_to_resMgr(self):
        req_param = self.data
        resmgr.grant_vnf(req_param)



