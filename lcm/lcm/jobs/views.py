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
from rest_framework.response import Response
from rest_framework.views import APIView

from lcm.jobs.job_get import GetJobInfoService
from lcm.pub.utils.jobutil import JobUtil
from lcm.pub.utils.values import ignore_case_get


class JobView(APIView):
    def get(self, request, job_id):
        response_id = ignore_case_get(request.META, 'responseId')
        ret = GetJobInfoService(job_id, response_id).do_biz()
        return Response(data=ret)

    def post(self, request, job_id):
        try:
            jobs = JobUtil.query_job_status(job_id)
            if len(jobs) > 0 and jobs[-1:].errcode == '255':
                return Response(data={'result': 'ok'})
            progress = request.data.get('progress')
            desc = request.data.get('desc')
            errcode = '0' if request.data.get('errcode') == 'active' else '255'
            JobUtil.add_job_status(job_id, progress, desc, error_code=errcode)
            return Response(data={'result': 'ok'})
        except Exception as e:
            return Response(data={'result': 'error', 'msg': e.message})
