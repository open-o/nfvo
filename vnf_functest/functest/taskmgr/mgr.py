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
import json


from rest_framework import status
# from rest_framework.decorators import api_view
from rest_framework.response import Response

from functest.pub.utils.utils import ignore_case_get, fun_name, generate_taskid
from functest.pub.database.models import TaskMgrModel
from functest.scriptmgr.mgr import setenv, upload_script, update_script

logger = logging.getLogger(__name__)


class TaskManager(object):

    @classmethod
    def start_onboarding_test(cls, request, *args, **kwargs):
        logger.info("Enter %s, data is %s", fun_name(), request.data)
        packageID = ignore_case_get(request.data, 'PackageID')
        try:
            if TaskMgrModel.objects.filter(packageid=packageID):
                raise Exception("The Package(%s) is already the onboarding package." % packageID)
            taskID = generate_taskid()
            envID = setenv()
            uploadID = upload_script()
            TaskMgrModel(
                packageid=packageID,
                taskid=generate_taskid(),
                envid=envID,
                uploadid=uploadID,
                status='CREATED'
                ).save()
        except Exception as e:
            return Response(data={'errorCode': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(data={"taskID": taskID}, status=status.HTTP_201_CREATED)


    @classmethod
    def query_test_status(cls, request, *args, **kwargs):
        pass

    @classmethod
    def collect_task_result(cls, request, *args, **kwargs):
        pass