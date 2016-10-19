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
from lcm.pub.database.models import NSDModel, NSInstModel, NfPackageModel


class TestNsPackage(TestCase):
    def setUp(self):
        self.client = Client()
        NSDModel.objects.filter().delete()
        NSInstModel.objects.filter().delete()
        NfPackageModel.objects.filter().delete()

    def tearDown(self):
        pass

    @mock.patch.object(restcall, 'call_req')
    def test_ns_pkg_on_boarding_when_on_boarded(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({"onBoardState": "onBoarded"}), '200']
        resp = self.client.post("/openoapi/nslcm/v1/nspackage", {"csarId": "1"}, format='json')
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("failed", resp.data["status"])
        self.assertEqual("CSAR(1) already onBoarded.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_ns_pkg_on_boarding_when_nsd_already_exists(self, mock_call_req):
        mock_vals = {
            "/openoapi/catalog/v1/csars/2":
                [0, json.JSONEncoder().encode({"onBoardState": "non-onBoarded"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode({"rawData": {"metadata": {"id": "2"}}}), '200']}

        def side_effect(*args):
            return mock_vals[args[4]]
        mock_call_req.side_effect = side_effect

        NSDModel(id="1", nsd_id="2").save()
        resp = self.client.post("/openoapi/nslcm/v1/nspackage", {"csarId": "2"}, format='json')
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("failed", resp.data["status"])
        self.assertEqual("NSD(2) already exists.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_ns_pkg_on_boarding_when_vnf_pkg_not_on_boarded(self, mock_call_req):
        mock_vals = {
            "/openoapi/catalog/v1/csars/3":
                [0, json.JSONEncoder().encode({"onBoardState": "non-onBoarded"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode(
                    {"rawData": {"metadata": {"id": "2"}},
                     "vnfs": [{"properties": {"id": "3"}}]}), '200']}

        def side_effect(*args):
            return mock_vals[args[4]]
        mock_call_req.side_effect = side_effect

        resp = self.client.post("/openoapi/nslcm/v1/nspackage", {"csarId": "3"}, format='json')
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("failed", resp.data["status"])
        self.assertEqual("VNF package(3) is not onBoarded.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_ns_pkg_on_boarding_when_vnf_pkg_not_on_boarded_on_catalog(self, mock_call_req):
        mock_vals = {
            "/openoapi/catalog/v1/csars/4":
                [0, json.JSONEncoder().encode({"onBoardState": "non-onBoarded"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode(
                    {"rawData": {"metadata": {"id": "2"}},
                     "vnfs": [{"properties": {"id": "5"}}]}), '200'],
            "/openoapi/catalog/v1/csars/5":
                [0, json.JSONEncoder().encode({"onBoardState": "non-onBoarded"}), '200'], }

        def side_effect(*args):
            return mock_vals[args[4]]
        mock_call_req.side_effect = side_effect

        NfPackageModel(uuid="1", nfpackageid="5").save()
        resp = self.client.post("/openoapi/nslcm/v1/nspackage", {"csarId": "4"}, format='json')
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("failed", resp.data["status"])
        self.assertEqual("VNF package(5) is not onBoarded on catalog.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_ns_pkg_on_boarding_when_on_board_success(self, mock_call_req):
        mock_vals = {
            "/openoapi/catalog/v1/csars/5":
                [0, json.JSONEncoder().encode({
                    "onBoardState": "non-onBoarded",
                    "createTime": "2016-05-15 12:30:34",
                    "modifyTime": "2016-05-15 12:30:34"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode(
                    {"rawData": {"metadata": {
                        "id": "2",
                        "name": "3",
                        "vendor": "4",
                        "description": "5",
                        "version": "6"}},
                        "vnfs": [{
                            "properties": {"id": "6"}}]}), '200'],
            "/openoapi/catalog/v1/csars/6":
                [0, json.JSONEncoder().encode({"onBoardState": "onBoarded"}), '200'],
            "/openoapi/catalog/v1/csars/5?onBoardState=onBoarded":
                [0, "OK", '200']}

        def side_effect(*args):
            return mock_vals[args[4]]

        mock_call_req.side_effect = side_effect

        NfPackageModel(uuid="1", nfpackageid="6").save()
        resp = self.client.post("/openoapi/nslcm/v1/nspackage", {"csarId": "5"}, format='json')
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("success", resp.data["status"])
        self.assertEqual("CSAR(5) onBoarded successfully.", resp.data["statusDescription"])
        nsds = NSDModel.objects.filter(id="5")
        self.assertEqual(1, len(nsds))
        self.assertEqual("2", nsds[0].nsd_id)

    ###############################################################################################################
    @mock.patch.object(restcall, 'call_req')
    def test_delete_csar_when_id_not_exist(self, mock_call_req):
        mock_call_req.return_value = [0, "", '204']
        resp = self.client.delete("/openoapi/nslcm/v1/nspackage/6")
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("success", resp.data["status"])
        self.assertEqual("Delete CSAR(6) successfully.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_delete_csar_when_ref_by_ns_inst(self, mock_call_req):
        mock_call_req.return_value = [0, "OK", '200']

        NSDModel(id="7", nsd_id="2").save()
        NSInstModel(id="1", nspackage_id="7").save()

        resp = self.client.delete("/openoapi/nslcm/v1/nspackage/7")
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("success", resp.data["status"])
        self.assertEqual("Set deletionPending to True of CSAR(7) successfully.",
                         resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_delete_csar_when_delete_success(self, mock_call_req):
        mock_call_req.return_value = [0, "OK", '204']

        NSDModel(id="8", nsd_id="2").save()

        resp = self.client.delete("/openoapi/nslcm/v1/nspackage/8")
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("success", resp.data["status"])
        self.assertEqual("Delete CSAR(8) successfully.", resp.data["statusDescription"])

    ###############################################################################################################
    def test_delete_pending_csar_when_id_not_exist(self):
        resp = self.client.delete("/openoapi/nslcm/v1/nspackage/9/deletionpending")
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("success", resp.data["status"])
        self.assertEqual("Delete pending CSAR(9) successfully.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_delete_pending_csar_when_pending_is_false(self, mock_call_req):
        mock_call_req.return_value = [0, '{"deletionPending": "false"}', '200']
        NSDModel(id="10", nsd_id="2").save()
        resp = self.client.delete("/openoapi/nslcm/v1/nspackage/10/deletionpending")
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("failed", resp.data["status"])
        self.assertEqual("CSAR(10) need not to be deleted.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_delete_pending_csar_when_refed_by_ns(self, mock_call_req):
        mock_call_req.return_value = [0, '{"deletionPending": "true"}', '200']
        NSDModel(id="11", nsd_id="2").save()
        NSInstModel(id="1", nspackage_id="11").save()
        resp = self.client.delete("/openoapi/nslcm/v1/nspackage/11/deletionpending")
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("failed", resp.data["status"])
        self.assertEqual("CSAR(11) is in using, cannot be deleted.", resp.data["statusDescription"])

    @mock.patch.object(restcall, 'call_req')
    def test_delete_pending_csar_when_delete_success(self, mock_call_req):
        mock_call_req.side_effect = [
            [0, '{"deletionPending": "true"}', '200'],
            [0, "OK", '204']]
        NSDModel(id="12", nsd_id="2").save()
        resp = self.client.delete("/openoapi/nslcm/v1/nspackage/12/deletionpending")
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)
        self.assertEqual("success", resp.data["status"])
        self.assertEqual("Delete CSAR(12) successfully.", resp.data["statusDescription"])

    ###############################################################################################################
    @mock.patch.object(restcall, 'call_req')
    def test_get_csar_successfully(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({
            "name": "1",
            "provider": "2",
            "version": "3",
            "operationalState": "4",
            "usageState": "5",
            "onBoardState": "6",
            "processState": "7",
            "deletionPending": "8",
            "downloadUri": "9",
            "createTime": "10",
            "modifyTime": "11",
            "format": "12",
            "size": "13"
            }), '200']

        NSDModel(id="13", nsd_id="2", vendor="3", version="4").save()
        NSInstModel(id="1", nspackage_id="13", name="11").save()
        NSInstModel(id="2", nspackage_id="13", name="22").save()

        resp = self.client.get("/openoapi/nslcm/v1/nspackage/13")
        self.assertEqual(resp.status_code, status.HTTP_200_OK)
        expect_data = {"nsInstanceInfo": [{"nsInstanceId": "1", "nsInstanceName": "11"},
                                          {"nsInstanceId": "2", "nsInstanceName": "22"}], "csarId": "13",
                       "packageInfo": {"nsdProvider": "3", "usageState": "5",
                                       "onBoardState": "6", "name": "1", "format": "12",
                                       "modifyTime": "11", "nsdId": "2", "nsdVersion": "4",
                                       "deletionPending": "8", "version": "3", "downloadUri": "9",
                                       "processState": "7", "provider": "2", "operationalState": "4",
                                       "createTime": "10", "size": "13"}}
        self.assertEqual(expect_data, resp.data)
