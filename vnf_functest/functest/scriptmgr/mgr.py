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

from functest.pub.config.config import UPLOAD_HOST, UPLOAD_PATH, UPLOAD_USER, UPLOAD_PWD, CATALOG_URI

import requests
import json
from functest.pub.config.config import BASE_URL, SET_ENV_URL, UPLOAD_URL


def setenv():
    headers = {
        'Content-Type': 'application/json'
    }
    params = {
        'remoteIp': UPLOAD_HOST,
        'userName': UPLOAD_USER,
        'password': UPLOAD_PWD,
        'path': UPLOAD_PATH
    }
    data = json.dumps(params)
    r = requests.post(BASE_URL + SET_ENV_URL, data)
    if r.status_code == 200:
        return r.content[1:-1]
    else:
        return ""


def upload_script(functest_env_id):
    download_url = CATALOG_URI
    resource = BASE_URL + UPLOAD_URL + functest_env_id
    headers = {
        'content-type': 'application/json',
        'URL': download_url
    }

    r = requests.post(resource, data=None, headers=headers)
    if r.status_code == 200:
        return r.content[1:-1]
    else:
        return ""


def update_script(package_id, file_name):

    return


def delete_script(package):

    return

