# Copyright 2017 CMCC Corporation.
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

#from functest.pub.config.config import SCRIPT_SOURCE_HOST, SSH_PORT, SCRIPT_SOURCE_PATH, SCRIPT_SOURCE_USER, SCRIPT_SOURCE_PWD
#from functest.pub.config.config import LOCATION_WEB_SERVER, SCRIPT_EXTENDED_NAME, PATH_SEPARATOR
#from functest.pub.config.config import SCRIPT_DEST_HOST, SCRIPT_DEST_PATH, SCRIPT_DEST_USER, SCRIPT_DEST_PWD


import requests, json
import datetime
from functest.pub.database.models import ScriptUploadingStatus
from functest.pub.utils import restcall
from functest.pub.config.config import MSB_SERVICE_IP, MSB_SERVICE_PORT

catalog_uri = '/openoapi/vnf-functest/v1/uploadpkg/'
set_env_uri = '/openoapi/vnf-functest/v1/setenv/'
base_url = "http://%s:%s/" % (MSB_SERVICE_IP, MSB_SERVICE_PORT)


def setenv():
    headers = {
        'Content-Type': 'application/json'
    }
    params = {
        'remoteIP': 'ip_address',
        'UserName': 'user_name',
        'password': 'password',
        'path': 'upload_path'
    }
    data = json.dumps(params)
    r = requests.post(set_env_uri, data, header=headers)
    functest_env_id = r.json
    return functest_env_id


def upload_script(package_id, functest_env_id):

    data = json.dumps({'functest_env_id': functest_env_id, 'package_id': package_id})
    # call script upload rest api
    # r = requests.post(catalog_uri, data)

    resource = catalog_uri + package_id + "?functest_env_id=" + functest_env_id
    ret = restcall.call_req(base_url, "", "", 0, resource, 'POST', '')
    upload_id = ret[1]
    return upload_id


def record_status(package_id):
    current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    ScriptUploadingStatus.objects.create(package_id=package_id,update_time=current_time)


def update_script(package_id, file_name):

    return


def delete_script(package):

    return

