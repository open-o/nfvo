# Copyright 2017 ZTE Corporation.
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

from django.conf.urls import url, patterns
from functest.taskmgr import mgr
from rest_framework.urlpatterns import format_suffix_patterns


urlpatterns = [
    url(r'^openoapi/vnf-functest/v1/taskmanager/testtasks$', mgr.start_onboarding_test, name='start_onboarding_test'),
    url(r'^openoapi/vnf-functest/v1/taskmanager/testtasks/(?P<taskID>[0-9a-zA-Z\-\_]+)/$', mgr.query_test_status, name='query_test_status'),
    url(r'^openoapi/vnf-functest/v1/taskmanager/testtasks/(?P<taskID>[0-9a-zA-Z\-\_]+)/result/$', mgr.collect_task_result, name='collect_task_result'),
]

# what is the role for this coding??
urlpatterns = format_suffix_patterns(urlpatterns)

