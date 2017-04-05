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
def start_onboarding_test(request, *args, **kwargs):
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
            taskid=taskID,
            envid=envID,
            uploadid=uploadID,
            #TODO: No interactions' response take 'operationid' from HUAWEI API
            #TODO: So, taskmgr temporarily takes the 'uploadid' value to set the 'operationid' value..
            operationid=uploadID,
            functionid=u'',
            status='CREATED',
            operfinished='False',
            operresult='FALIURE',
            operresultmessage=u''
            ).save()
        task_exe_ret = execute_test_script(taskID)
        if not task_exe_ret:
            raise ValueError , 'TaskID [{0}] failure..!!'.format(taskID)
    except Exception as e:
        return Response(data={'errorCode': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(data={"taskID": taskID}, status=status.HTTP_201_CREATED)


@api_view(http_method_names=['GET'])
def query_test_status(request, *args, **kwargs):
    logger.info("Enter %s, data is %s", fun_name(), kwargs)
    taskID = ignore_case_get(kwargs, "taskID")
    try:
        record = TaskMgrTaskTbl.objects.filter(taskid=taskID)
        if not record:
            err_msg = "the Task(%s) does not exist..!" % taskID
            return Response(data={'error': err_msg}, status=status.HTTP_404_NOT_FOUND)
        else:
            query_status_data = json_data['paths']['/status/']['get']
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
        #TODO: Assume ret[1] that is dic.
        operfinished = ignore_case_get(ret[1], 'operFinished')
        operresult = ignore_case_get(ret[1], 'operResult')
        operresultmessage = ignore_case_get(ret[1], 'operResultMessage')
        record[0].operfinished = operfinished
        record[0].operresult = operresult
        record[0].operresultmessage = operresultmessage
        record[0].save()
        data={"taskID": taskID, "status": operresult}
    except Exception as e:
        return Response(data={'errorCode': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(data, status=status.HTTP_202_ACCEPTED)


@api_view(http_method_names=['GET'])
def collect_task_result(request, *args, **kwargs):
    logger.info("Enter %s, data is %s", fun_name(), kwargs)
    taskID = ignore_case_get(kwargs, 'taskID')
    try:
        record = TaskMgrTaskTbl.objects.filter(taskid=taskID)
        if not record:
            err_msg = "the Task(%s) does not exist..!" % taskID
            return Response(data={'error': err_msg}, status=status.HTTP_404_NOT_FOUND)
        else:
            download_ret_data = json_data['paths']['/download/']['get']
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
        # TODO: according to definition of api ret[1] shall be list.
        for case_ret in ret[1]:
            testid = ignore_case_get(case_ret, 'test_id')
            testresult = ignore_case_get(case_ret, 'test_description')
            testdes = ignore_case_get(case_ret, 'test_result')
            TaskMgrCaseTbl(
                taskid=taskID,
                functionid=funcID,
                testid=testid,
                testresult=testresult,
                testdes=testdes,
            ).save()
        resp_data = {"taskID": taskID, "funcTestResults": ret[1]}
    except Exception as e:
        return Response(data={'errorCode': e.message}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(resp_data, status=status.HTTP_202_ACCEPTED)


def execute_test_script(taskID):
    logger.info("Enter %s, data is %s", fun_name(), taskID)
    record = TaskMgrTaskTbl.objects.filter(taskid=taskID)
    if not record:
        raise Exception("the Task(%s) does not exist..!" % taskID)
    else:
        execute_data = json_data['paths']['/execute']['post']
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
        logger.info("The Task(%s) failure, %s" % (taskID, ret[1]))
        return False
    # TODO: Assume ret[1] that is dic.
    funcid = ignore_case_get(ret[1], 'functest_id')
    record[0].functionid = funcid
    record[0].status = 'RUNNING'
    record[0].save()
    return True
