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
import os
import logging
import traceback

from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView

from lcm.ns.ns_create import CreateNSService
from lcm.ns.ns_get import GetNSInfoService
from lcm.ns.ns_instant import InstantNSService
from lcm.ns.ns_terminate import TerminateNsService, DeleteNsService
from lcm.pub.utils.jobutil import JobUtil, JOB_TYPE
from lcm.pub.utils.values import ignore_case_get
from lcm.pub.database.models import NSInstModel

logger = logging.getLogger(__name__)


class CreateNSView(APIView):
    def get(self, request):
        logger.debug("CreateNSView::get")
        ret = GetNSInfoService().get_ns_info()
        if not ret:
            return Response(status=status.HTTP_404_NOT_FOUND)
        logger.debug("CreateNSView::get::ret=%s", ret)
        return Response(data=ret, status=status.HTTP_200_OK)

    def post(self, request):
        logger.debug("Enter CreateNS: %s", request.data)
        nsd_id = ignore_case_get(request.data, 'nsdId')
        ns_name = ignore_case_get(request.data, 'nsName')
        description = ignore_case_get(request.data, 'description')
        try:
            ns_inst_id = CreateNSService(nsd_id, ns_name, description).do_biz()
        except Exception as e:
            logger.error("Exception in CreateNS: %s", e.message)
            Response(data={'error': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        logger.debug("CreateNSView::post::ret={'nsInstanceId':%s}", ns_inst_id)
        return Response(data={'nsInstanceId': ns_inst_id}, status=status.HTTP_201_CREATED)


class NSInstView(APIView):
    def do_get(self, request, ns_instance_id):
        logger.debug("Enter NSInstView::do_get")
        ret = GetNSInfoService(ns_instance_id).get_ns_info()
        if not ret:
            logger.debug("Leave NSInstView::do_get::status=404")
            return Response(status=status.HTTP_404_NOT_FOUND)
        logger.debug("Leave NSInstView::do_get::ret=%s, status=200", ret)
        return Response(data=ret, status=status.HTTP_200_OK)

    def post(self, request, ns_instance_id):
        ack = InstantNSService(ns_instance_id, request.data).do_biz()
        logger.debug("Leave NSInstView::post::ack=%s", ack)
        return Response(data=ack['data'], status=ack['status'])


class TerminateNSView(APIView):
    def post(self, request, ns_instance_id):
        logger.debug("Enter TerminateNSView::post %s", request.data)
        termination_type = ignore_case_get(request.data, 'terminationType')
        graceful_termination_timeout = ignore_case_get(request.data, 'gracefulTerminationTimeout')
        job_id = JobUtil.create_job("VNF", JOB_TYPE.TERMINATE_VNF, ns_instance_id)
        try:
            TerminateNsService(ns_instance_id, termination_type, graceful_termination_timeout, job_id).do_biz()
        except Exception as e:
            logger.error("Exception in CreateNS: %s", e.message)
            Response(data={'error': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        ret = {'jobID': job_id}
        logger.debug("Leave TerminateNSView::post ret=%s", ret)
        return Response(data=ret, status=status.HTTP_202_ACCEPTED)


class DeleteNSView(APIView):
    def delete(self, request, ns_instance_id):
        logger.debug("Enter DeleteNSView::delete %s", request.data)
        ret = DeleteNsService(ns_instance_id).do_biz()
        logger.debug("Leave DeleteNSView::delete ret= %s", ret)
        if ret == 'true':
            return Response(data={}, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(data={}, status=status.HTTP_409_CONFLICT)


class SwaggerJsonView(APIView):
    def get(self, request):
        json_file = os.path.join(os.path.dirname(__file__), 'swagger.json')
        f = open(json_file)
        json_data = json.JSONDecoder().decode(f.read())
        f.close()
        return Response(json_data)


class NSInstPostDealView(APIView):
    def post(self, request, ns_instance_id):
        logger.debug("Enter NSInstPostDealView::post %s", request.data)
        ns_status = 'ACTIVE' if ignore_case_get(request.data, 'status') == 'true' else 'FAILED'
        try:
            NSInstModel.objects.filter(id=ns_instance_id).update(status=ns_status)
        except:
            logger.error(traceback.format_exc())
            return Response(data={'error': 'Failed to update status of NS(%s)' % ns_instance_id},
                            status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(data={'success': 'Update status of NS(%s) to %s' % (ns_instance_id, ns_status)},
                        status=status.HTTP_202_ACCEPTED)
