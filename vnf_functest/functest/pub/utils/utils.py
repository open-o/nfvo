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

# get value from key-value pair

import inspect
import uuid

def ignore_case_get(args, key, def_val=""):
    if not key:
        return def_val
    if key in args:
        return args[key]
    for old_key in args:
        if old_key.upper() == key.upper():
            return args[old_key]
    return def_val


def set_opt_val(param, key, val):
    if val or val is False:
        param[key] = val


def fun_name():
    return inspect.stack()[1][3]


def generate_taskid():
    return str(uuid.uuid1())