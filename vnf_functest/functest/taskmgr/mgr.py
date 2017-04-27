# Copyright 2017 ZTE, CMCC Corporation.
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
from functest.scriptmgr.mgr import setenv, upload_script

from functest.pub.config.config import ROBOT_RUN_USER, ROBOT_RUN_PWD, BASE_URL


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
        uploadID = upload_script(envID)
        TaskMgrTaskTbl(
            packageid=packageID,
            taskid=taskID,
            envid=envID,
            uploadid=uploadID,
            operationid=uploadID,
            functionid=u'',
            status='CREATED',
            operfinished='False',
            operresult='FAILURE',
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

        base_url = BASE_URL + json_data['basePath'] + '/functest' + query_status_data['resource'] + operID

        ret = restcall.call_req(
            base_url=base_url,
            user="",
            passwd="",
            auth_type=0,
            resource='',
            method=query_status_data['method'],
            content=''
        )
        if ret[0] != 0:
            raise Exception("Failed to query status for Task(%s), %s" % (taskID, ret[1]))
        #TODO: Assume ret[1] that is dic.
        result = json.loads(ret[1])
        operfinished =result['operFinished']
        operresult = result['oResultCode']
        operresultmessage = result['operResultMessage']
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

        base_url = BASE_URL + json_data['basePath'] + '/functest' + download_ret_data['resource'] + funcID
        ret = restcall.call_req(
            base_url=base_url,
            user="",
            passwd="",
            auth_type=0,
            resource='',
            method=download_ret_data['method'],
            content=''
        )
        if ret[0] != 0:
            raise Exception("Failed to download results for Task(%s), %s" % (taskID, ret[1]))

        result = json.loads(ret[1])

        for case_ret in result:
            testid = ignore_case_get(case_ret, 'name')
            testdes = ignore_case_get(case_ret, 'description')
            testresult = ignore_case_get(case_ret, 'status')
            TaskMgrCaseTbl(
                taskid=taskID,
                functionid=funcID,
                testid=testid,
                testresult=testresult,
                testdes=testdes,
            ).save()
        resp_data = {"taskID": taskID, "funcTestResults": result}
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
    base_url = BASE_URL + json_data['basePath'] + '/functest' \
               + "/" + execute_data['parameters']['upload_id'] \
               + "/" + execute_data['parameters']['functest_env_id'] \
               + "/" + execute_data['parameters']['frameworktype']
    ret = restcall.call_req(
        base_url=base_url,
        user=ROBOT_RUN_USER,
        passwd=ROBOT_RUN_PWD,
        auth_type=0,
        resource='',
        method=execute_data['method'],
        content=''
    )
    if ret[0] != 0:
        logger.info("The Task(%s) failure, %s" % (taskID, ret[1]))
        return False
    # TODO: Assume ret[1] that is dic.
    funcid = ret[1][1:-1]
    record[0].functionid = funcid
    record[0].status = 'RUNNING'
    record[0].save()
    return True
