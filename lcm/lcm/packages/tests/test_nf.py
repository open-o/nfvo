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
import mock
from rest_framework import status
from django.test import TestCase
from django.test import Client

from lcm.pub.utils import restcall
from lcm.pub.database.models import NfPackageModel, VnfPackageFileModel, NfInstModel
from lcm.pub.database.models import JobStatusModel, JobModel
from lcm.packages.nf_package import NfOnBoardingThread


class TestNfPackage(TestCase):
    def setUp(self):
        self.client = Client()
        NfPackageModel.objects.filter().delete()
        VnfPackageFileModel.objects.filter().delete()
        NfInstModel.objects.filter().delete()
        JobModel.objects.filter().delete()
        JobStatusModel.objects.filter().delete()

    def tearDown(self):
        pass
        
    def assert_job_result(self, job_id, job_detail):
        pass

    @mock.patch.object(NfOnBoardingThread, 'run')
    def test_nf_pkg_on_boarding_normal(self, mock_run):
        resp = self.client.post("/openoapi/nslcm/v1/vnfpackage", {
            "csarId": "1",
            "vimIds": ["1"]
            }, format='json')
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        
