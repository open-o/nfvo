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
import json
import logging

from lcm.pub.exceptions import NSLCMException
from lcm.pub.utils.restcall import req_by_msb
from lcm.pub.utils.values import ignore_case_get

logger = logging.getLogger(__name__)


def send_nf_init_request(vnfm_inst_id, vnf_inst_name, nf_package_id, vnfd_id, inputs):
    uri = '/openoapi/%s/v1/%s/vnfs' % ('zte-vnfm', vnfm_inst_id)
    req_param = json.JSONEncoder().encode(
        {'vnfInstanceName': vnf_inst_name, 'vnfPackageId': nf_package_id, 'vnfDescriptorId': vnfd_id,
         'additionalParam': inputs})
    ret = req_by_msb(uri, "POST", req_param)
    if ret[0] != 0:
        logger.error("Send NF instance request to VNFM failed. Status code is %s, detail is %s.", ret[2], ret[1])
        raise NSLCMException('Send NF instance request to VNFM failed.')
    resp_body = json.JSONDecoder().decode(ret[1])
    vnfm_job_id = ignore_case_get(resp_body, 'jobId')
    vnfm_nf_inst_id = ignore_case_get(resp_body, 'vnfInstanceId')
    return vnfm_job_id, vnfm_nf_inst_id