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
import time

from rest_framework import status

from lcm.pub.database.models import DefPkgMappingModel, ServiceBaseInfoModel, InputParamMappingModel
from lcm.pub.database.models import NSInstModel, NfPackageModel
from lcm.pub.msapi.catalog import get_process_id, get_download_url_from_catalog
from lcm.pub.msapi.catalog import query_rawdata_from_catalog, get_servicetemplate_id, get_servicetemplate
from lcm.pub.msapi.wso2bpel import workflow_run
from lcm.pub.msapi.extsys import select_vnfm
from lcm.pub.utils.jobutil import JobUtil
from lcm.pub.utils import toscautil
from lcm.pub.utils.values import ignore_case_get

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

            vim_id = self.req_data['additionalParamForNs']['location']

            src_plan = query_rawdata_from_catalog(ns_inst.nspackage_id, input_parameters)
            dst_plan = toscautil.convert_nsd_model(src_plan["rawData"])
            logger.debug('tosca plan dest:%s' % dst_plan)
            NSInstModel.objects.filter(id=self.ns_inst_id).update(nsd_model=dst_plan)

            params_json = json.JSONEncoder().encode(self.req_data["additionalParamForNs"])
            # start
            params_vnf = []
            plan_dict = json.JSONDecoder().decode(dst_plan)
            for vnf in plan_dict["vnfs"]:
                vnfd = NfPackageModel.objects.get(vnfdid=vnf['properties']['id'])
                vnfd_model = json.JSONDecoder().decode(vnfd.vnfdmodel)
                vnfm_type = vnfd_model["metadata"].get("vnfmType", "ztevmanagerdriver")
                vnfm_info = select_vnfm(vnfm_type=vnfm_type, vim_id=vim_id)               
                params_vnf.append({
                    "vnfProfileId": vnf["vnf_id"],
                    "additionalParam": {
                        "vnfmInstanceId": vnfm_info["vnfmId"],
                        "vnfmType": vnfm_type,
                        "inputs": params_json
                    }
                })
            # end
            vnf_params_json = json.JSONEncoder().encode(params_vnf)
            plan_input = {'jobId': job_id, 
                'nsInstanceId': self.req_data["nsInstanceId"],
                'object_context': dst_plan,
                'object_additionalParamForNs': params_json,
                'object_additionalParamForVnf': vnf_params_json}
            plan_input.update(**self.get_model_count(dst_plan))
            plan_input["sdncontrollerid"] = ignore_case_get(
                self.req_data['additionalParamForNs'], "sdncontroller")

            ServiceBaseInfoModel(service_id=self.ns_inst_id,
                                 service_name=ns_inst.name,
                                 service_type='NFVO',
                                 description=ns_inst.description,
                                 active_status='--',
                                 status=ns_inst.status,
                                 creator='--',
                                 create_time=int(time.time())).save()

            service_tpl = get_servicetemplate(ns_inst.nsd_id)
            DefPkgMappingModel(service_id=self.ns_inst_id,
                               service_def_id=service_tpl['csarId'],
                               template_name=service_tpl['templateName'],
                               template_id=service_tpl['serviceTemplateId']).save()

            for key, val in self.req_data['additionalParamForNs'].items():
                InputParamMappingModel(service_id=self.ns_inst_id,
                    input_key=key, input_value=val).save()

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
