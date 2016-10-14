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
from lcm.pub.utils import fileutil
from lcm.pub.nfvi.vim.vimadaptor import VimAdaptor
from lcm.pub.database.models import NfPackageModel, VnfPackageFileModel, NfInstModel
from lcm.pub.database.models import JobStatusModel, JobModel
from lcm.packages.nf_package import NfOnBoardingThread, NfPkgDeletePendingThread
from lcm.packages.nf_package import NfPkgDeleteThread
from lcm.packages import nf_package


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

    def assert_job_result(self, job_id, job_progress, job_detail):
        jobs = JobStatusModel.objects.filter(
            jobid=job_id,
            progress=job_progress,
            descp=job_detail)
        self.assertEqual(1, len(jobs))

    @mock.patch.object(NfOnBoardingThread, 'run')
    def test_nf_pkg_on_boarding_normal(self, mock_run):
        resp = self.client.post("/openoapi/nslcm/v1/vnfpackage", {
            "csarId": "1",
            "vimIds": ["1"]
            }, format='json')
        self.assertEqual(resp.status_code, status.HTTP_202_ACCEPTED)

    @mock.patch.object(restcall, 'call_req')
    def test_nf_pkg_on_boarding_when_on_boarded(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({"onBoardState": "onBoarded"}), '200']
        NfOnBoardingThread(csar_id="1",
                           vim_ids=["1"],
                           lab_vim_id="",
                           job_id="2").run()
        self.assert_job_result("2", 255, "CSAR(1) already onBoarded.")

    @mock.patch.object(restcall, 'call_req')
    def test_nf_pkg_on_boarding_when_on_boarding(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({
            "onBoardState": "non-onBoarded",
            "processState": "onBoarding"
            }), '200']
        NfOnBoardingThread(csar_id="2",
                           vim_ids=["1"],
                           lab_vim_id="",
                           job_id="3").run()
        self.assert_job_result("3", 255, "CSAR(2) is onBoarding now.")

    @mock.patch.object(restcall, 'call_req')
    def test_nf_on_boarding_when_nfd_already_exists(self, mock_call_req):
        mock_vals = {
            "/openoapi/catalog/v1/csars/2":
                [0, json.JSONEncoder().encode({
                    "onBoardState": "onBoardFailed", "processState": "deleteFailed"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode({
                    "rawData": {"metadata": {"id": "999"}}}), '200']}

        def side_effect(*args):
            return mock_vals[args[4]]

        mock_call_req.side_effect = side_effect
        NfPackageModel(vnfdid="999").save()
        NfOnBoardingThread(csar_id="2", vim_ids=["1"], lab_vim_id="", job_id="4").run()
        self.assert_job_result("4", 255, "NFD(999) already exists.")

    @mock.patch.object(restcall, 'call_req')
    @mock.patch.object(fileutil, 'download_file_from_http')
    @mock.patch.object(VimAdaptor, '__init__')
    @mock.patch.object(VimAdaptor, 'create_image')
    @mock.patch.object(VimAdaptor, 'get_image')
    def test_nf_on_boarding_when_successfully(self, mock_get_image, mock_create_image,
                                              mock__init__, mock_download_file_from_http, mock_call_req):
        mock_download_file_from_http.return_value = "/root/package"
        mock_vals = {
            "/openoapi/catalog/v1/csars/2":
                [0, json.JSONEncoder().encode({
                    "onBoardState": "onBoardFailed", "processState": "deleteFailed"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode({
                    "rawData": {"metadata": {"id": "999", "vendor": "1", "vnfd_version": "2", "version": "3"}},
                    "image_files": [
                        {"properties":
                            {
                                "name": "4",
                                "file_url": "5",
                                "disk_format": "6",
                                "description": "7"}}]}), '200'],
            "/openoapi/catalog/v1/csars/2/files?relativePath=5":
                [0, json.JSONEncoder().encode({
                    "csar_file_info": [{"downloadUri": "8"}, {"localPath": "9"}]}), '200'],
            "/openoapi/extsys/v1/vims":
                [0, json.JSONEncoder().encode([{
                    "vimId": "1", "url": "/root/package", "userName": "tom",
                    "password": "tom", "tenant": "10"}]), '200'],
            "/openoapi/catalog/v1/csars/2?onBoardState=onBoarded": [0, '{}', 200],
            "/openoapi/catalog/v1/csars/2?processState=normal": [0, '{}', 200]}
        mock_create_image.return_value = [0, {"id": "30", "name": "jerry", "res_type": 0}]
        mock__init__.return_value = None
        mock_get_image.return_value = [0, {"id": "30", "name": "jerry", "size": "60", "status": "active"}]

        def side_effect(*args):
            return mock_vals[args[4]]
        mock_call_req.side_effect = side_effect

        NfOnBoardingThread(csar_id="2", vim_ids=["1"], lab_vim_id="", job_id="4").run()
        self.assert_job_result("4", 100, "CSAR(2) onBoarding successfully.")

    @mock.patch.object(restcall, 'call_req')
    @mock.patch.object(fileutil, 'download_file_from_http')
    @mock.patch.object(VimAdaptor, '__init__')
    @mock.patch.object(VimAdaptor, 'create_image')
    @mock.patch.object(VimAdaptor, 'get_image')
    def test_nf_on_boarding_when_timeout(self, mock_get_image, mock_create_image,
                                         mock__init__, mock_download_file_from_http, mock_call_req):
        nf_package.MAX_RETRY_TIMES = 2
        nf_package.SLEEP_INTERVAL_SECONDS = 1
        mock_download_file_from_http.return_value = "/root/package"
        mock_vals = {
            "/openoapi/catalog/v1/csars/2":
            [0, json.JSONEncoder().encode({"onBoardState": "onBoardFailed",
                                           "processState": "deleteFailed"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode({
                    "rawData": {
                        "metadata": {
                            "id": "999", "vendor": "1", "vnfd_version": "2", "version": "3"}},
                 "image_files": [{
                     "properties":
                         {
                             "name": "4", "file_url": "5",
                             "disk_format": "6",
                             "description": "7"}}]}), '200'],
            "/openoapi/catalog/v1/csars/2/files?relativePath=5":
                [0, json.JSONEncoder().encode({
                    "csar_file_info": [{"downloadUri": "8"}, {"localPath": "9"}]}), '200'],
            "/openoapi/extsys/v1/vims":
                [0, json.JSONEncoder().encode([{
                    "vimId": "1", "url": "/root/package", "userName": "tom",
                    "password": "tom", "tenant": "10"}]), 200]}
        mock_create_image.return_value = [0, {"id": "30", "name": "jerry", "res_type": 0}]
        mock__init__.return_value = None
        mock_get_image.return_value = [0, {"id": "30", "name": "jerry", "size": "60", "status": "0"}]

        def side_effect(*args):
            return mock_vals[args[4]]

        mock_call_req.side_effect = side_effect
        NfOnBoardingThread(csar_id="2", vim_ids=["1"], lab_vim_id="", job_id="4").run()
        self.assert_job_result("4", 255, "Failed to create image:timeout(2 seconds.)")

    @mock.patch.object(restcall, 'call_req')
    @mock.patch.object(fileutil, 'download_file_from_http')
    @mock.patch.object(VimAdaptor, '__init__')
    @mock.patch.object(VimAdaptor, 'create_image')
    def test_nf_on_boarding_when_failed_to_create_image(self, mock_create_image,
                                                        mock__init__, mock_download_file_from_http, mock_call_req):
        mock_download_file_from_http.return_value = "/root/package"
        mock_vals = {
            "/openoapi/catalog/v1/csars/2":
                [0, json.JSONEncoder().encode({
                    "onBoardState": "onBoardFailed", "processState": "deleteFailed"}), '200'],
            "/openoapi/catalog/v1/servicetemplates/queryingrawdata":
                [0, json.JSONEncoder().encode(
                    {"rawData": {"metadata": {"id": "999", "vendor": "1",
                                              "vnfd_version": "2", "version": "3"}},
                     "image_files": [{"properties": {"name": "4", "file_url": "5",
                                                     "disk_format": "6", "description": "7"}}]}), '200'],
            "/openoapi/catalog/v1/csars/2/files?relativePath=5":
                [0, json.JSONEncoder().encode({
                    "csar_file_info": [{"downloadUri": "8"}, {"localPath": "9"}]}), '200'],
            "/openoapi/extsys/v1/vims":
                [0, json.JSONEncoder().encode([{
                    "vimId": "1", "url": "/root/package", "userName": "tom",
                    "password": "tom", "tenant": "10"}]), '200']}
        mock_create_image.return_value = [1, 'Unsupported image format.']
        mock__init__.return_value = None

        def side_effect(*args):
            return mock_vals[args[4]]
        mock_call_req.side_effect = side_effect
        NfOnBoardingThread(csar_id="2", vim_ids=["1"], lab_vim_id="", job_id="4").run()
        self.assert_job_result("4", 255, "Failed to create image:Unsupported image format.")

    #########################################################################
    @mock.patch.object(restcall, 'call_req')
    def test_get_csar_successfully(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({
            "name": "1", "provider": "2", "version": "3", "operationalState": "4",
            "usageState": "5", "onBoardState": "6", "processState": "7",
            "deletionPending": "8", "downloadUri": "9", "createTime": "10",
            "modifyTime": "11", "format": "12", "size": "13"
            }), '200']
        NfPackageModel(uuid="1", vnfdid="001", vendor="vendor",
                       vnfdversion="1.2.0", vnfversion="1.1.0", nfpackageid="13").save()
        VnfPackageFileModel(id="1", filename="filename", imageid="00001",
                            vimid="1", vimuser="001", tenant="12", status="1", vnfpid="13").save()
        NfInstModel(nfinstid="1", mnfinstid="001", nf_name="name", package_id="13").save()
        resp = self.client.get("/openoapi/nslcm/v1/vnfpackage/13")
        self.assertEqual(resp.status_code, status.HTTP_200_OK)
        expect_data = {
            "csarId": '13',
            "packageInfo": {
                "vnfdId": "001",
                "vnfdProvider": "vendor",
                "vnfdVersion": "1.2.0",
                "vnfVersion": "1.1.0",
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
                "size": "13"},
            "imageInfo": [{
                "index": "0",
                "fileName": "filename",
                "imageId": "00001",
                "vimId": "1",
                "vimUser": "001",
                "tenant": "12",
                "status": "1"}],
            "vnfInstanceInfo": [{
                "vnfInstanceId": "1",
                "vnfInstanceName": "name"}]}
        self.assertEqual(expect_data, resp.data)

    #########################################################################
    @mock.patch.object(restcall, 'call_req')
    def test_delete_pending_csar_when_successfully(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({
            "processState": "deleting"}), "200"]
        NfPkgDeletePendingThread(csar_id="1", job_id='2').run()
        self.assert_job_result("2", 100, "Delete pending CSAR(1) successfully.")

    @mock.patch.object(restcall, 'call_req')
    def test_delete_pending_csar_when_deleting(self, mock_call_req):
        NfPackageModel(uuid="01", nfpackageid="1").save()
        mock_call_req.return_value = [0, json.JSONEncoder().encode({
            "processState": "deleting"}), "200"]
        NfPkgDeletePendingThread(csar_id="1", job_id='2').run()
        self.assert_job_result("2", 100, "CSAR(1) is deleting now.")

    @mock.patch.object(restcall, 'call_req')
    def test_delete_pending_csar_when_not_deletion_pending(self, mock_call_req):
        NfPackageModel(uuid="01", nfpackageid="1").save()
        mock_call_req.return_value = [0, json.JSONEncoder().encode({
            "deletionPending": "false"}), "200"]
        NfPkgDeletePendingThread(csar_id="1", job_id='2').run()
        self.assert_job_result("2", 100, "CSAR(1) need not to be deleted.")

    @mock.patch.object(restcall, 'call_req')
    def test_delete_pending_csar_when_in_using(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({
            "processState": "normal"}), "200"]
        NfPackageModel(uuid="01", nfpackageid="1").save()
        NfInstModel(nfinstid="01", package_id="1").save()
        NfPkgDeletePendingThread(csar_id="1", job_id='2').run()
        self.assert_job_result("2", 100, "CSAR(1) is in using, cannot be deleted.")

    @mock.patch.object(VimAdaptor, '__init__')
    @mock.patch.object(VimAdaptor, 'delete_image')
    @mock.patch.object(restcall, 'call_req')
    def test_delete_csarr_when_exception(self, mock_call_req, mock_delete_image, mock_init_):
        mock_vals = {
            ("/openoapi/catalog/v1/csars/1", "DELETE"):
                [1, "{}", "400"],
            ("/openoapi/catalog/v1/csars/1?processState=deleting", "PUT"):
                [0, "{}", "200"],
            ("/openoapi/catalog/v1/csars/1?processState=deleteFailed", "PUT"):
                [0, "{}", "200"],
            ("/openoapi/catalog/v1/csars/1", "GET"):
                [0, json.JSONEncoder().encode({"processState": "normal"}), "200"],
            ("/openoapi/extsys/v1/vims", "GET"):
                [0, json.JSONEncoder().encode([{"vimId": "002",
                                                "url": "url_test",
                                                "userName": "test01",
                                                "password": "123456",
                                                "tenant": "test"}]), "200"]}
        mock_delete_image.return_value = [0, "", '200']

        def side_effect(*args):
            return mock_vals[(args[4], args[5])]

        mock_call_req.side_effect = side_effect
        mock_init_.return_value = None
        VnfPackageFileModel(vnfpid="1", imageid="001", vimid="002").save()
        NfPackageModel(uuid="01", nfpackageid="1").save()
        NfPkgDeletePendingThread(csar_id="1", job_id='2').run()
        self.assert_job_result("2", 255, "Failed to delete CSAR(1) from catalog.")

    @mock.patch.object(VimAdaptor, '__init__')
    @mock.patch.object(VimAdaptor, 'delete_image')
    @mock.patch.object(restcall, 'call_req')
    def test_delete_csar_when_successfully(self, mock_call_req, mock_delete_image, mock_init_):
        mock_vals = {
            ("/openoapi/catalog/v1/csars/1", "DELETE"):
                [0, json.JSONEncoder().encode({"successfully": "successfully"}), "200"],
            ("/openoapi/catalog/v1/csars/1?processState=deleting", "PUT"):
                [0, json.JSONEncoder().encode({"successfully": "successfully"}), "200"],
            ("/openoapi/catalog/v1/csars/1?processState=deleteFailed", "PUT"):
                [0, json.JSONEncoder().encode({"successfully": "successfully"}), "200"],
            ("/openoapi/catalog/v1/csars/1", "GET"):
                [0, json.JSONEncoder().encode({"notProcessState": "notProcessState"}), "200"],
            ("/openoapi/extsys/v1/vims", "GET"):
                [0, json.JSONEncoder().encode([{
                    "vimId": "002",
                    "url": "url_test",
                    "userName": "test01",
                    "password": "123456",
                    "tenant": "test"}]), "200"]}
        mock_delete_image.return_value = [0, json.JSONEncoder().encode({"test": "test"}), '200']

        def side_effect(*args):
            return mock_vals[(args[4], args[5])]

        mock_call_req.side_effect = side_effect
        mock_init_.return_value = None
        VnfPackageFileModel(vnfpid="1", imageid="001", vimid="002").save()
        NfPackageModel(uuid="01", nfpackageid="1").save()
        NfPkgDeletePendingThread(csar_id="1", job_id='2').run()
        self.assert_job_result("2", 100, "Delete CSAR(1) successfully.")

    #########################################################################
    @mock.patch.object(restcall, 'call_req')
    def test_delete_nf_pkg_when_deleting(self, mock_call_req):
        mock_call_req.return_value = [0, json.JSONEncoder().encode({"processState": "deleting"}), '200']
        NfPkgDeleteThread(csar_id="1", job_id="2").run()
        self.assert_job_result("2", 100, "CSAR(1) is deleting now.")
