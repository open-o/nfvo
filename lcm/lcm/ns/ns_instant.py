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
from lcm.pub.msapi.tosca import tosca_plan
from lcm.pub.msapi.wso2bpel import workflow_run
from lcm.pub.utils.jobutil import JobUtil
from lcm.pub.utils import toscautil

logger = logging.getLogger(__name__)


class InstantNSService(object):
    def __init__(self, ns_inst_id, plan_content):
        self.ns_inst_id = ns_inst_id
        self.plan_input = plan_content
        self.yaml_relative_path = 'abc.yaml'  # TODO

    def do_biz(self):
        try:
            job_id = JobUtil.create_job("NS", "NS_INST", self.ns_inst_id)
            logger.debug('ns-instant(%s) workflow starting...' % self.ns_inst_id)
            ns_inst = NSInstModel.objects.get(id=self.ns_inst_id)
            url, _ = get_download_url_from_catalog(ns_inst.nspackage_id, self.yaml_relative_path)
            logger.debug('ns-instant(%s) package-id:%s, yaml-path:%s, catalog-url:%s' %
                         (self.ns_inst_id, ns_inst.nspackage_id, self.yaml_relative_path, url))
            src_plan = tosca_plan(url, self.plan_input['additionalParamForNs'])
            dst_plan = toscautil.convert_nsd_model(src_plan)
            logger.debug('ns-instant(%s) tosca plan src:%s, dest:%s' % (self.ns_inst_id, src_plan, dst_plan))

            self.plan_input.update({'jobId': job_id})
            self.plan_input.update(**self.get_model_count(dst_plan))
            process_id = get_process_id('LCM', ns_inst.nsd_id)
            data = {"processId": process_id, "params": {"planInput": self.plan_input}}
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
