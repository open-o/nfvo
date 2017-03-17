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
from functest.pub.database.models import TaskMgrModel


class VnfRegTest(unittest.TestCase):


    def setUp(self):
        self.client = Client()
        TaskMgrModel.objects.filter().delete()
        self.taskInst1 = {
            "packageid": 1,
            "taskid": 1,
            "status": "CREATED"
        }

    def tearDown(self):
        pass

    def test_start_onboarding_test(self):
        response = self.client.post("/openoapi/vnf-functest/v1/taskmanager/testtasks", self.taskInst1, format='json')
        self.assertEqual(status.HTTP_201_CREATED, response.status_code, response.content)
        tasks = TaskMgrModel.objects.filter()
        self.assertEqual(1, len(tasks))
        taskInstActual = {
            "packageid": tasks[0].packageid,
            "taskid": tasks[0].taskid,
            "status": tasks[0].status,
        }
        self.assertEqual(self.taskInst1, taskInstActual)




