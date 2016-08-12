# Copyright (C) 2015 ZTE, Inc. and others. All rights reserved. (ZTE)
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

from rest_framework import status


class NSLCMException(Exception):
    def __init__(self, view=None, *args, **kwargs):
        self.msg_id = kwargs.get("msgid", "Unknown error [%s]")
        self.msg_args = kwargs.get("args", None)
        self.status = kwargs.get("status", status.HTTP_500_INTERNAL_SERVER_ERROR)
        self.message = self.msg_id % self.msg_args


