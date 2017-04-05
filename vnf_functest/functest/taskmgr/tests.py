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

import unittest
import json
import mock
from django.test import Client
from rest_framework import status
from functest.pub.database.models import TaskMgrTaskTbl, TaskMgrCaseTbl

expect_ret = {
    'ret_start_onboarding_test_exe_succeed': {
        "packageID": u'1',
        "taskid": u'1',
        "envid": u'2',
        "uploadid": u'3',
        "operationid": u'3',
        "status": "CREATED",
    },

    'ret_exe_success': {
        "functionid": u'1',
        "status": "RUNNING"
    },

    'ret_query_test_status': {
        "operFinished": u'True',
	    "operResult":   u'SUCCESS',
	    "operResultMessage": u'Query Operation'
    },

    'ret_collect_task_result': [
        {
            'test_id': u'1',
            'test_description': u'test_id = 1',
            'test_result': u'PASS'
        },
        {
            'test_id': u'2',
            'test_description': u'test_id = 2',
            'test_result': u'PASS'
        }
    ]
}


class TaskMgrTestNoVirtualDB(unittest.TestCase):

    def setUp(self):
        self.client = Client()
        TaskMgrTaskTbl.objects.filter().delete()
        self.packageIdInst1 = {
            "PackageID": u'1',
        }
        self.taskIdInst1 = {
            "TaskID": u'10000',
        }

    def tearDown(self):
        pass


    @mock.patch("functest.taskmgr.mgr.execute_test_script")
    @mock.patch("functest.scriptmgr.mgr.upload_script")
    @mock.patch("functest.scriptmgr.mgr.setenv")
    @mock.patch("functest.pub.utils.utils.generate_taskid")
    def test_start_onboarding_test_exe_success(self, mock_generate_taskid, mock_setenv, mock_upload_script, mock_execute_test_script):
        mock_generate_taskid.return_value = "1"
        mock_setenv.return_value = "2"
        mock_upload_script.return_value = "3"
        mock_execute_test_script.return_value = True
        response = self.client.post("/openoapi/vnf-functest/v1/taskmanager/testtasks", self.packageIdInst1, format='json')
        self.assertEqual(status.HTTP_201_CREATED, response.status_code, response.content)
        record = TaskMgrTaskTbl.objects.filter(packageid=self.packageIdInst1["PackageID"])
        self.assertEqual(1, len(record))
        taskInstActual = {
            "packageID": record[0].packageid,
            "taskid": record[0].taskid,
            "envid": record[0].envid,
            "uploadid": record[0].uploadid,
            "operationid": record[0].operationid,
            "status": record[0].status,
        }
        self.assertEqual(expect_ret['ret_start_onboarding_test_exe_succeed'], taskInstActual)


    @mock.patch("functest.taskmgr.mgr.execute_test_script")
    @mock.patch("functest.scriptmgr.mgr.upload_script")
    @mock.patch("functest.scriptmgr.mgr.setenv")
    @mock.patch("functest.pub.utils.utils.generate_taskid")
    def test_start_onboarding_test_exe_fail(self, mock_generate_taskid, mock_setenv, mock_upload_script, mock_execute_test_script):
        mock_generate_taskid.return_value = "1"
        mock_setenv.return_value = "2"
        mock_upload_script.return_value = "3"
        mock_execute_test_script.return_value = False
        response = self.client.post("/openoapi/vnf-functest/v1/taskmanager/testtasks", self.packageIdInst1, format='json')
        record = TaskMgrTaskTbl.objects.filter(packageid=self.packageIdInst1["PackageID"])
        self.assertEqual(1, len(record))
        self.assertEqual(status.HTTP_500_INTERNAL_SERVER_ERROR, response.status_code, response.content)


class TaskMgrTestWithVirtualDB(unittest.TestCase):

    def setUp(self):
        from functest.taskmgr.mgr import execute_test_script
        self.execute_test_script = execute_test_script
        self.client = Client()
        TaskMgrTaskTbl.objects.filter().delete()
        self.taskIdInst1 = {
            "TaskID": u'10000',
        }

    def tearDown(self):
        pass

    def _construct_database_data(self, task_status='CREATED'):
        TaskMgrTaskTbl(
            packageid=u'1',
            taskid=u'10000',
            envid=u'1',
            uploadid=u'1',
            operationid=u'1',
            functionid=u'1',
            status=task_status
        ).save()

    @mock.patch("functest.pub.utils.restcall.call_req")
    def test_execute_test_script_return_true(self, mock_call_req):
        mock_call_req.return_value = [0, {'functest_id':u'1'}, '200']
        self._construct_database_data()
        actual_ret = self.execute_test_script(self.taskIdInst1['TaskID'])
        self.assertTrue(actual_ret)
        record = TaskMgrTaskTbl.objects.filter(taskid=self.taskIdInst1['TaskID'])
        self.assertEqual(1, len(record))
        exeInstActual = {
            "functionid": record[0].functionid,
            "status": record[0].status
        }
        self.assertEqual(expect_ret['ret_exe_success'], exeInstActual)


    @mock.patch("functest.pub.utils.restcall.call_req")
    def test_query_test_status(self, mock_call_req):
        resp_info_dic = {'operFinished': u'True',
                         'operResult': u'SUCCESS',
                         'operResultMessage': u''
                         }
        self._construct_database_data()
        mock_call_req.return_value = [0, resp_info_dic, '202']
        response = self.client.get("/openoapi/vnf-functest/v1/taskmanager/testtasks/{0}/".format(self.taskIdInst1['TaskID']))
        record = TaskMgrTaskTbl.objects.filter(taskid=self.taskIdInst1['TaskID'])
        self.assertEqual(1, len(record))
        taskInstActual = {
            "operFinished": record[0].operfinished,
            "operResult": record[0].operresult,
            "operResultMessage": record[0].operresultmessage,
        }
        self.assertEqual(resp_info_dic, taskInstActual)
        self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code, response.content)


    @mock.patch("functest.pub.utils.restcall.call_req")
    def test_collect_task_result(self, mock_call_req):
        self._construct_database_data()
        mock_call_req.return_value = [0, expect_ret['ret_collect_task_result'], '202']
        response = self.client.get("/openoapi/vnf-functest/v1/taskmanager/testtasks/{0}/result/".format(self.taskIdInst1['TaskID']))
        self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code, response.content)
        record = TaskMgrCaseTbl.objects.filter(taskid=self.taskIdInst1['TaskID'])
        self.assertEqual(2, len(record))
        taskInstActual = []
        for re in record._result_cache:
            record_info = {
                "test_id": re.testid,
                "test_description": re.testresult,
                "test_result": re.testdes,
                }
            taskInstActual.append(record_info)
        self.assertEqual(taskInstActual, expect_ret['ret_collect_task_result'])
