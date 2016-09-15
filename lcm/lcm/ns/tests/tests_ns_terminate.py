# Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
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
import uuid

import mock
from django.test import TestCase, Client
from rest_framework import status

from lcm.pub.database.models import NfInstModel, JobModel, NfPackageModel, NSInstModel
from lcm.pub.utils import restcall
from lcm.pub.utils.jobutil import JOB_MODEL_STATUS
from lcm.ns.ns_terminate import TerminateNsService
from lcm.pub.utils.jobutil import JobUtil, JOB_TYPE


class TestTerminateNsViews(TestCase):
    def setUp(self):
        self.client = Client()
        self.ns_inst_id = '1'
        self.nf_inst_id = '1'
        self.vnffg_id = str(uuid.uuid4())
        self.vim_id = str(uuid.uuid4())
        self.job_id = str(uuid.uuid4())
        self.nf_uuid = '1-1-1'
        self.tenant = "tenantname"
        NSInstModel(id=self.ns_inst_id, name="ns_name",status='null').save()
        NfInstModel.objects.create(nfinstid=self.nf_inst_id, nf_name='name_1', vnf_id='1',
                                   vnfm_inst_id='1', ns_inst_id='1-1-1,2-2-2',
                                   max_cpu='14', max_ram='12296', max_hd='101', max_shd="20", max_net=10,
                                   status='null', mnfinstid=self.nf_uuid, package_id='pkg1',
                                   vnfd_model='{"metadata": {"vnfdId": "1","vnfdName": "PGW001","vnfProvider": "zte","vnfdVersion": "V00001","vnfVersion": "V5.10.20","productType": "CN","vnfType": "PGW","description": "PGW VNFD description","isShared":true,"vnfExtendType":"driver"}}')

    def tearDown(self):
        NSInstModel.objects.all().delete()
        NfInstModel.objects.all().delete()

    @mock.patch.object(TerminateNsService, 'do_biz')
    def test_terminate_vnf_url(self, mock_run):
        req_data = {
            "terminationType": "forceful",
            "gracefulTerminationTimeout": "600"
        }
        response = self.client.post("/openoapi/nslcm/v1/ns/%s/terminate" % self.ns_inst_id, data=req_data)
        self.failUnlessEqual(status.HTTP_202_ACCEPTED, response.status_code)

        response = self.client.delete("/openoapi/nslcm/v1/ns/%s" % self.ns_inst_id)
        self.failUnlessEqual(status.HTTP_202_ACCEPTED, response.status_code)

        #context = json.loads(response.content)
        #self.assertTrue(NfInstModel.objects.filter(nfinstid=context['vnfInstId']).exists())

    @mock.patch.object(restcall, "call_req")
    def test_terminate_vnf(self, mock_call_req):
        job_id = JobUtil.create_job("VNF", JOB_TYPE.TERMINATE_VNF, self.nf_inst_id)

        mock_vals = {
        "/openoapi/nslcm/v1/ns/vls/1":
            [0, json.JSONEncoder().encode({"jobId": self.job_id}), '200'],
        "/openoapi/nslcm/v1/ns/sfcs/1":
            [0, json.JSONEncoder().encode({"jobId": self.job_id}), '200'],
        "/openoapi/nslcm/v1/ns/vnfs/1":
            [0, json.JSONEncoder().encode({}), '200'],
        "/openoapi/zte-vnfm/v1/jobs/" + self.job_id + "&responseId=0":
            [0, json.JSONEncoder().encode({"jobid": self.job_id,
                                           "responsedescriptor": {"progress": "100",
                                                                  "status": JOB_MODEL_STATUS.FINISHED,
                                                                  "responseid": "3",
                                                                  "statusdescription": "creating",
                                                                  "errorcode": "0",
                                                                  "responsehistorylist": [
                                                                      {"progress": "0",
                                                                       "status": JOB_MODEL_STATUS.PROCESSING,
                                                                       "responseid": "2",
                                                                       "statusdescription": "creating",
                                                                       "errorcode": "0"}]}}), '200'],
        }

        req_data = {
            "terminationType": "forceful",
            "gracefulTerminationTimeout": "600"
        }

        def side_effect(*args):
            return mock_vals[args[4]]
        mock_call_req.side_effect = side_effect

        TerminateNsService(self.nf_inst_id, "forceful", "600", job_id).do_biz()
        nsinst = NSInstModel.objects.get(id=self.ns_inst_id)
        if nsinst:
            self.assertTrue(1, 0)
        else:
            self.assertTrue(1, 1)