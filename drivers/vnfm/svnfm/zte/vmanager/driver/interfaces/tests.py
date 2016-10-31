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
from django.test import Client
from django.test import TestCase
from rest_framework import status
from driver.pub.utils import restcall


class InterfacesTest(TestCase):
    def setUp(self):
        self.client = Client()

    def tearDown(self):
        pass

#     @mock.patch.object(restcall, 'call_req')
#     def test_instantiate_vnf_001(self, mock_call_req):
#         """
#         Initate_VNF
#         """
#         r1 = [0, json.JSONEncoder().encode({
#             "vnfmId": "19ecbb3a-3242-4fa3-9926-8dfb7ddc29ee",
#             "name": "g_vnfm",
#             "type": "vnfm",
#             "vimId": "",
#             "vendor": "ZTE",
#             "version": "v1.0",
#             "description": "vnfm",
#             "certificateUrl": "",
#             "url": "http://10.74.44.11",
#             "userName": "admin",
#             "password": "admin",
#             "createTime": "2016-07-06 15:33:18"})]
#
#         r2 = [0, json.JSONEncoder().encode(
#             {
#                 "vnfPackageId": "1111111",
#                 "csarId": "78ede6f3-66cc-46ab-b748-38a6c010d272",
#                 "csarUri": "/files/catalog/NSAR/ZTE/NanocellGW/v1.0/NanocellGateway.csar"})]
#
#         r3 = [0, json.JSONEncoder().encode(
#             {
#                 "csarId": "78ede6f3-66cc-46ab-b748-38a6c010d272",
#                 "packageInfo": {"downloadUri": "vbras_packageInfo_downloadUri",
#                                 "name": "vbras_packageInfo_name",
#                                 }, })]
#
#         r4 = [0, json.JSONEncoder().encode(
#             {
#                 "VNFInstanceID": "1",
#                 "JobId": "1"})]
#         mock_call_req.side_effect = [r1, r2, r3, r4]
#
#         req_data = {
#             "vnfInstanceName": "vbras",
#             "vnfPackageId": "vnf_package_id",
#             "vnfDescriptorId": "vbras_vnfd",
#             "vnfInstanceDescription": "vbras_vnfInstanceDescription", }
#         response = self.client.post("/openoapi/ztevnfm/v1/ztevnfmid/vnfs", data=req_data)
#
#         self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code)
#         expect_resp_data = {"jobid": "1", "vnfInstanceId": "1"}
#         self.assertEqual(expect_resp_data, response.data)
#
#     @mock.patch.object(restcall, 'call_req')
#     def test_terminate_vnf__002(self, mock_call_req):
#         """
#         Terminate_VNF
#         """
#         r1 = [0, json.JSONEncoder().encode({
#             "vnfmId": "19ecbb3a-3242-4fa3-9926-8dfb7ddc29ee",
#             "name": "g_vnfm",
#             "type": "vnfm",
#             "vimId": "",
#             "vendor": "ZTE",
#             "version": "v1.0",
#             "description": "vnfm",
#             "certificateUrl": "",
#             "url": "http://10.74.44.11",
#             "userName": "admin",
#             "password": "admin",
#             "createTime": "2016-07-06 15:33:18"})]
#
#         r2 = [0, json.JSONEncoder().encode(
#             {
#                 "vnfInstanceId": "1",
#                 "JobId": "1"})]
#         mock_call_req.side_effect = [r1, r2]
#
#         response = self.client.post("/openoapi/ztevnfm/v1/ztevnfmid/vnfs/vbras_innstance_id/terminate")
#
#         self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code)
#         expect_resp_data = {"jobid": "1", "vnfInstanceId": "1"}
#         self.assertEqual(expect_resp_data, response.data)
#
#     @mock.patch.object(restcall, 'call_req')
#     def test_query_vnf_003(self, mock_call_req):
#         """
#         Query_VNF
#         """
#         r1 = [0, json.JSONEncoder().encode({
#             "vnfmId": "19ecbb3a-3242-4fa3-9926-8dfb7ddc29ee",
#             "name": "g_vnfm",
#             "type": "vnfm",
#             "vimId": "",
#             "vendor": "ZTE",
#             "version": "v1.0",
#             "description": "vnfm",
#             "certificateUrl": "",
#             "url": "http://10.74.44.11",
#             "userName": "admin",
#             "password": "admin",
#             "createTime": "2016-07-06 15:33:18"})]
#
#         r2 = [0, json.JSONEncoder().encode(
#             {
#                 "vnfinstancestatus": "1", })]
#         mock_call_req.side_effect = [r1, r2]
#
#         response = self.client.get("/openoapi/ztevnfm/v1/ztevnfmid/vnfs/vbras_innstance_id")
#
#         self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code)
#
#         expect_resp_data = {
#             "vnfInfo":
#                 {
#                     "vnfStatus": "1", }}
#         self.assertEqual(expect_resp_data, response.data)
#
#     @mock.patch.object(restcall, 'call_req')
#     def test_operation_status_004(self, mock_call_req):
#         """
#         Operation_status
#         """
#         r1 = [0, json.JSONEncoder().encode({
#             "vnfmId": "19ecbb3a-3242-4fa3-9926-8dfb7ddc29ee",
#             "name": "g_vnfm",
#             "type": "vnfm",
#             "vimId": "",
#             "vendor": "ZTE",
#             "version": "v1.0",
#             "description": "vnfm",
#             "certificateUrl": "",
#             "url": "http://10.74.44.11",
#             "userName": "admin",
#             "password": "admin",
#             "createTime": "2016-07-06 15:33:18"})]
#         r2 = [0, json.JSONEncoder().encode("""{
#     "jobId": "1003003",
#     "responseDescriptor": {
#         "errorCode": null,
#         "progress": "40",
#         "responseHistoryList": [
#             {
#                 "errorCode": null,
#                 "progress": "40",
#                 "responseId": "1",
#                 "status": "proccessing",
#                 "statusDescription": "OMC VMs are decommissioned in VIM"
#             },
#             {
#                 "errorCode": null,
#                 "progress": "41",
#                 "responseId": "2",
#                 "status": "proccessing",
#                 "statusDescription": "OMC VMs are decommissioned in VIM"
#             }
#         ],
#         "responseId": "42",
#         "status": "proccessing",
#         "statusDescription": "OMC VMs are decommissioned in VIM"
#     }
# }""")]
#         mock_call_req.side_effect = [r1, r2]
#         response = self.client.get("/openoapi/{vnfmtype}/v1/{vnfmid}/jobs/{jobid}?responseId={responseId}".format(
#             vnfmtype='vnfm',
#             vnfmid=1,
#             jobid=1003003,
#             responseId=1))
#
#         self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code)
#
#         expect_resp_data = {
#             "jobId": "1003003",
#             "responseDescriptor": {
#                 "errorCode": None,
#                 "progress": "40",
#                 "responseHistoryList": [
#                     {
#                         "errorCode": None,
#                         "progress": "40",
#                         "status": "proccessing",
#                         "statusDescription": "OMC VMs are decommissioned in VIM",
#                         "responseId": "1"},
#                     {
#                         "errorCode": None,
#                         "progress": "41",
#                         "status": "proccessing",
#                         "statusDescription": "OMC VMs are decommissioned in VIM",
#                         "responseId": "2"}],
#                 "responseId": "42",
#                 "status": "proccessing",
#                 "statusDescription": "OMC VMs are decommissioned in VIM"}}
#         self.assertDictEqual(expect_resp_data, json.JSONDecoder().decode(response.data))
#
#     @mock.patch.object(restcall, 'call_req')
#     def test_grantvnf_005(self, mock_call_req):
#         """
#         Grant_VNF
#         """
#         # r1 = [0, json.JSONEncoder().encode({
#         #     "vnfmId": "19ecbb3a-3242-4fa3-9926-8dfb7ddc29ee",
#         #     "name": "g_vnfm",
#         #     "type": "vnfm",
#         #     "vimId": "",
#         #     "vendor": "ZTE",
#         #     "version": "v1.0",
#         #     "description": "vnfm",
#         #     "certificateUrl": "",
#         #     "url": "http://10.74.44.11",
#         #     "userName": "admin",
#         #     "password": "admin",
#         #     "createTime": "2016-07-06 15:33:18"
#         # })]
#
#         r2 = [0, json.JSONEncoder().encode(
#             {"vim":
#                 {
#                     "vimInfoId": "111111",
#                     "vimId": "12345678",
#                     "interfaceInfo": {
#                         "vimType": "vnf",
#                         "apiVersion": "v1",
#                         "protocolType": "None"},
#                     "accessInfo": {
#                         "tenant": "tenant1",
#                         "username": "admin",
#                         "password": "password"},
#                     "interfaceEndpoint": "http://127.0.0.1/api/v1"},
#                 "zone": "",
#                 "addResource": {
#                     "resourceDefinitionId": "xxxxx",
#                     "vimId": "12345678",
#                     "zoneId": "000"},
#                 "removeResource": "",
#                 "vimAssets": {
#                     "computeResourceFlavour": {
#                         "vimId": "12345678",
#                         "vduId": "sdfasdf",
#                         "vimFlavourId": "12"},
#                     "softwareImage": {
#                         "vimId": "12345678",
#                         "imageName": "AAA",
#                         "vimImageId": ""}},
#                 "additionalParam": ""
#              })]
#
#         req_data = {
#             "vnfmid": "13232222",
#             "nfvoid": "03212234",
#             "vimid": "12345678",
#             "exvimidlist ":
#                 [
#                     "exvimid"],
#             "tenant": " tenant1",
#             "vnfistanceid": "1234",
#             "operationright": "0",
#             "vmlist": [
#                 {
#                     "vmflavor": "SMP",
#                     "vmnumber": "3"},
#                 {
#                     "vmflavor": "CMP",
#                     "vmnumber": "3"}
#                       ]}
#
#         # mock_call_req.side_effect = [r1, r2]
#         mock_call_req.return_value = r2
#         response = self.client.put("/v1/resource/grant", data=json.dumps(req_data), content_type='application/json')
#
#         self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code)
#
#         expect_resp_data = {
#             "vimid": "12345678",
#             "tenant": "tenant1"}
#         self.assertDictEqual(expect_resp_data, response.data)
#
#     @mock.patch.object(restcall, 'call_req')
#     def test_notify_006(self, mock_call_req):
#         """
#         Notification
#         """
#         r1 = [0, json.JSONEncoder().encode({
#             "vnfmId": "19ecbb3a-3242-4fa3-9926-8dfb7ddc29ee",
#             "name": "g_vnfm",
#             "type": "vnfm",
#             "vimId": "",
#             "vendor": "ZTE",
#             "version": "v1.0",
#             "description": "vnfm",
#             "certificateUrl": "",
#             "url": "http://10.74.44.11",
#             "userName": "admin",
#             "password": "admin",
#             "createTime": "2016-07-06 15:33:18"})]
#
#         r2 = [0, json.JSONEncoder().encode(
#             {"vim":
#                 {
#                     "vimInfoId": "111111",
#                     "vimId": "12345678",
#                     "interfaceInfo": {
#                         "vimType": "vnf",
#                         "apiVersion": "v1",
#                         "protocolType": "None"},
#                     "accessInfo": {
#                         "tenant": "tenant1",
#                         "username": "admin",
#                         "password": "password"},
#                     "interfaceEndpoint": "http://127.0.0.1/api/v1"},
#                 "zone": "",
#                 "addResource": {
#                     "resourceDefinitionId": "xxxxx",
#                     "vimId": "12345678",
#                     "zoneId": "000"},
#                 "removeResource": "",
#                 "vimAssets": {
#                     "computeResourceFlavour": {
#                         "vimId": "12345678",
#                         "vduId": "sdfasdf",
#                         "vimFlavourId": "12"},
#                     "softwareImage": {
#                         "vimId": "12345678",
#                         "imageName": "AAA",
#                         "vimImageId": ""}},
#                 "additionalParam": ""
#              })]
#
#         req_data = {
#             "nfvoid": "1",
#             "vnfmid": "876543211",
#             "vimid": "6543211", "timestamp": "1234567890",
#             "vnfinstanceid": "1",
#             "eventtype": "0",
#             "vmlist":
#                 [
#                     {
#                         "vmflavor": "SMP",
#                         "vmnumber": "3",
#                         "vmidlist ":
#                             [
#                                 "vmuuid"]},
#                     {
#                         "vmflavor": "CMP",
#                         "vmnumber": "3",
#                         "vmidlist ":
#                             [
#                                 "vmuuid"]}]}
#         mock_call_req.side_effect = [r1, r2]
#         response = self.client.post("/v1/vnfs/lifecyclechangesnotification", data=req_data)
#
#         self.assertEqual(status.HTTP_202_ACCEPTED, response.status_code)
#
#         expect_resp_data = None
#         self.assertEqual(expect_resp_data, response.data)
