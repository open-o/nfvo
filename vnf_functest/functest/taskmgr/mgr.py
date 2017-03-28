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
import os

from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view

from functest.pub.utils import restcall
from functest.pub.utils.utils import ignore_case_get, fun_name, generate_taskid
from functest.pub.database.models import TaskMgrTaskTbl, TaskMgrCaseTbl
from functest.scriptmgr.mgr import setenv, upload_script, update_script

logger = logging.getLogger(__name__)


json_file = os.path.join(os.path.dirname(__file__), 'taskmgr.json')
with open(json_file) as f:
    json_data = json.JSONDecoder().decode(f.read())

@api_view(http_method_names=['POST'])
def start_onboarding_tes(request, *args, **kwargs):
    logger.info("Enter %s, data is %s", fun_name(), request.data)
    packageID = ignore_case_get(request.data, 'PackageID')
    try:
        ret = TaskMgrTaskTbl.objects.filter(packageid=packageID)
        if ret:
            raise Exception("The Package(%s) is already the onboarding package." % packageID)
        taskID = generate_taskid()
        envID = setenv()
        uploadID = upload_script(packageID, envID)
        TaskMgrTaskTbl(
            packageid=packageID,
            taskid=generate_taskid(),
            envid=envID,
            uploadid=uploadID,
            operationid=uploadID,
            status='CREATED'
            ).save()
    except Exception as e:
        return Response(data={'errorCode': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    # execute_test_script(taskID)
    return Response(data={"taskID": taskID}, status=status.HTTP_201_CREATED)


@api_view(http_method_names=['GET'])
def query_test_status(request, *args, **kwargs):
    logger.info("Enter %s, data is %s", fun_name(), request.data)
    taskID = ignore_case_get(request.data, 'taskID')
    try:
        record = TaskMgrTaskTbl.objects.filter(taskid=taskID)
        if not record:
            err_msg = "the Task(%s) does not exist..!" % taskID
            return Response(data={'error': err_msg}, status=status.HTTP_404_NOT_FOUND)
        else:
            query_status_data = json_data['/status/']['get']
            operID = record[0].operationid
        ret = restcall.call_req(
            base_url=json_data['basePath'],
            user="",
            passwd="",
            auth_type=0,
            resource=query_status_data['resource'] + operID,
            method=query_status_data['method'],
            content=json.dumps(query_status_data['parameters'])
        )
        if ret[0] != 0:
            raise Exception("Failed to query status for Task(%s), %s" % (taskID, ret[1]))
        #TODO assume ret[1] that is dic.
        resp_status = ignore_case_get(ret[1], 'operResult')
        TaskMgrTaskTbl(
            status=resp_status
        ).save()
        data={"taskID": taskID, "status": resp_status}
    except Exception as e:
        return Response(data={'errorCode': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(data, status=status.HTTP_202_ACCEPTED)


@api_view(http_method_names=['GET'])
def collect_task_result(request, *args, **kwargs):
    logger.info("Enter %s, data is %s", fun_name(), request.data)
    taskID = ignore_case_get(request.data, 'taskID')
    try:
        record = TaskMgrTaskTbl.objects.filter(taskid=taskID)
        if not record:
            err_msg = "the Task(%s) does not exist..!" % taskID
            return Response(data={'error': err_msg}, status=status.HTTP_404_NOT_FOUND)
        else:
            download_ret_data = json_data['/download/']['get']
            funcID = record[0].functionid
        ret = restcall.call_req(
            base_url=json_data['basePath'],
            user="",
            passwd="",
            auth_type=0,
            resource=download_ret_data['resource'] + funcID,
            method=download_ret_data['method'],
            content=""
        )
        if ret[0] != 0:
            raise Exception("Failed to download results for Task(%s), %s" % (taskID, ret[1]))
        # TODO assume ret[1] that is dic.
        testid = ignore_case_get(ret[1], 'test_id')
        testresult = ignore_case_get(ret[1], 'testresult')
        testdes = ignore_case_get(ret[1], 'testdes')
        TaskMgrCaseTbl(
            taskid=record[0].taskid,
            testid=testid,
            testresult=testresult,
            testdes=testdes,
        ).save()
        resp_data = {"taskID": taskID, "testid": testid, "testresult": testresult, "testdes": testdes}
    except Exception as e:
        return Response(data={'errorCode': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(resp_data, status=status.HTTP_202_ACCEPTED)


def execute_test_script(taskID):
    logger.info("Enter %s, data is %s", fun_name(), taskID)
    record = TaskMgrTaskTbl.objects.filter(taskid=taskID)
    if not record:
        raise Exception("the Task(%s) does not exist..!" % taskID)
    else:
        execute_data = json_data['/execute']['post']
        execute_data['parameters']['upload_id']=record[0].uploadid
        execute_data['parameters']['functest_env_id']=record[0].envid
        execute_data['parameters']['frameworktype']='ROBOT'
    ret = restcall.call_req(
        base_url=json_data['basePath'],
        user="",
        passwd="",
        auth_type=0,
        resource=execute_data['resource'],
        method=execute_data['method'],
        content=json.dumps(execute_data['parameters'])
    )
    if ret[0] != 0:
        raise Exception("Failed to download results for Task(%s), %s" % (taskID, ret[1]))
    # TODO assume ret[1] that is dic.
    funcid = ignore_case_get(ret[1], 'functest_id')
    TaskMgrCaseTbl(
        functionid=funcid,
        status='RUNNING'
    ).save()