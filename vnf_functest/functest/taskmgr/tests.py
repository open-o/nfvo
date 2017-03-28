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
    'ret_start_onboarding_test': {
        "packageID": u'1',
        "taskid": u'1',
        "envid": u'1',
        "uploadid": u'1',
        "status": "CREATED",
    },

    'ret_query_test_status': {
        "operFinished": u'True',
	    "operResult":   u'SUCCESS',
	    "operResultMessage": u'Query Operation'
    },
}


class TaskMgrTest(unittest.TestCase):

    def setUp(self):
        self.client = Client()
        TaskMgrTaskTbl.objects.filter().delete()
        self.startTaskInst1 = {
            "PackageID": u'1',
        }
        self.queryStatusInst1 = {
            "TaskID": u'1',
        }

    def tearDown(self):
        pass

    @mock.patch("functest.scriptmgr.mgr.upload_script")
    @mock.patch("functest.scriptmgr.mgr.setenv")
    @mock.patch("functest.pub.utils.utils.generate_taskid")
    def test_start_onboarding_test(self, mock_upload_script, mock_setenv, mock_generate_taskid):
        mock_upload_script.return_value = "1"
        mock_setenv.return_value = "1"
        mock_generate_taskid.return_value = "1"
        response = self.client.post("/openoapi/vnf-functest/v1/taskmanager/testtasks", self.startTaskInst1, format='json')
        self.assertEqual(status.HTTP_201_CREATED, response.status_code, response.content)
        record = TaskMgrTaskTbl.objects.filter(packageid=self.startTaskInst1["PackageID"])
        self.assertEqual(1, len(record))
        taskInstActual = {
            "packageID": record[0].packageid,
            "taskid": record[0].taskid,
            "envid": record[0].envid,
            "uploadid": record[0].uploadid,
            "status": record[0].status,
        }
        self.assertEqual(expect_ret['ret_start_onboarding_test'], taskInstActual)