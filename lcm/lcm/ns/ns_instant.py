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
import traceback

from rest_framework import status

from lcm.pub.database.models import NSInstModel
from lcm.pub.msapi.catalog import get_process_id, get_download_url_from_catalog
from lcm.pub.msapi.catalog import query_rawdata_from_catalog, get_servicetemplate_id
from lcm.pub.msapi.wso2bpel import workflow_run
from lcm.pub.utils.jobutil import JobUtil
from lcm.pub.utils import toscautil

logger = logging.getLogger(__name__)


class InstantNSService(object):
    def __init__(self, ns_inst_id, plan_content):
        self.ns_inst_id = ns_inst_id
        self.req_data = plan_content

    def do_biz(self):
        try:
            job_id = JobUtil.create_job("NS", "NS_INST", self.ns_inst_id)
            logger.debug('ns-instant(%s) workflow starting...' % self.ns_inst_id)
            logger.debug('req_data=%s' % self.req_data)
            ns_inst = NSInstModel.objects.get(id=self.ns_inst_id)

            input_parameters = []
            for key, val in self.req_data['additionalParamForNs'].items():
                input_parameters.append({"key": key, "value": val})

            src_plan = query_rawdata_from_catalog(ns_inst.nspackage_id, input_parameters)
            dst_plan = toscautil.convert_nsd_model(src_plan["rawData"])
            logger.debug('tosca plan dest:%s' % dst_plan)

            params_json = json.JSONEncoder().encode(self.req_data["additionalParamForNs"])
            plan_input = {'jobId': job_id, 
                'nsInstanceId': self.req_data["nsInstanceId"],
                'object_context': dst_plan,
                'object_additionalParamForNs': params_json,
                'object_additionalParamForVnf': params_json}
            plan_input.update(**self.get_model_count(dst_plan))


            servicetemplate_id = get_servicetemplate_id(ns_inst.nsd_id)
            process_id = get_process_id('init', servicetemplate_id)
            data = {"processId": process_id, "params": {"planInput": plan_input}}
            logger.debug('ns-instant(%s) workflow data:%s' % (self.ns_inst_id, data))

            ret = workflow_run(data)
            logger.info("ns-instant(%s) workflow result:%s" % (self.ns_inst_id, ret))
            if ret.get('status') == 1:
                return dict(data={'jobId': job_id}, status=status.HTTP_200_OK)
            return dict(data={'error': ret['message']}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        except Exception as e:
            logger.error(traceback.format_exc())
            logger.error("ns-instant(%s) workflow error:%s" % (self.ns_inst_id, e.message))
            return dict(data={'error': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    @staticmethod
    def get_model_count(context):
        data = json.JSONDecoder().decode(context)
        vls = len(data.get('vls', []))
        sfcs = len(data.get('fps', []))
        vnfs = len(data.get('vnfs', []))
        return {'vlCount': str(vls), 'sfcCount': str(sfcs), 'vnfCount': str(vnfs)}
