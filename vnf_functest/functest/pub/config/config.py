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
import os

# [MSB]
MSB_SERVICE_IP = '127.0.0.1'
MSB_SERVICE_PORT = '10080'

# SSH PORT
SSH_PORT = 22

PATH_SEPARATOR = "/"


# SCRIPT FILE EXTENDED NAME
SCRIPT_EXTENDED_NAME = ""

# [VNF PACKAGES TEST SCRIPT SOURCE LOCATION]
SCRIPT_SOURCE_HOST = ""
SCRIPT_SOURCE_PATH = ""
SCRIPT_SOURCE_USER = "" # not sure if it's necessary
SCRIPT_SOURCE_PWD = "" # not sure if it's necessary

# TEMP LOCATION ON WEB SERVER
LOCATION_WEB_SERVER = ""

# [VNF PACKAGES TEST SCRIPT DESTINATION LOCATION]
SCRIPT_DEST_HOST = ""
SCRIPT_DEST_PATH = ""
SCRIPT_DEST_USER = "" # not sure if it's necessary
SCRIPT_DEST_PWD = ""


# [REDIS]
REDIS_HOST = '127.0.0.1'
REDIS_PORT = '6379'
REDIS_PASSWD = ''

# [mysql]
# DB_IP = "127.0.0.1"
# DB_PORT = 3306
# DB_NAME = "nfvo"
# DB_USER = "root"
# DB_PASSWD = "password"

DB_IP = "127.0.0.1"
DB_PORT = 3306
DB_NAME = "functest"
DB_USER = "zte"
DB_PASSWD = "ztemysql1"

# [register]
REG_TO_MSB_WHEN_START = True
REG_TO_MSB_REG_URL = "/openoapi/vnf-functest/v1/scripts"
REG_TO_MSB_REG_PARAM = {
    "serviceName": "functest",
    "version": "v1",
    "url": "/openoapi/vnf-functest/v1",
    "protocol": "REST",
    "visualRange": "1",
    "nodes": [{
        "ip": "127.0.0.1",
        "port": "8403",
        "ttl": 0
    }]
}
