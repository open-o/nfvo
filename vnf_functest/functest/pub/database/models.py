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
from django.db import models


class TaskMgrTaskTbl(models.Model):
    class Meta:
        db_table = 'TASK_RECORD'

    packageid = models.CharField(db_column='PACKAGEID', primary_key=True, max_length=200)
    taskid = models.CharField(db_column='TASKID', max_length=200)
    envid = models.CharField(db_column='ENVID', max_length=200)
    uploadid = models.CharField(db_column='UPLOADID', max_length=200)
    operationid = models.CharField(db_column='OPERID', max_length=200)
    functionid = models.CharField(db_column='FUNCID', max_length=200)
    status = models.CharField(db_column='STATUS', max_length=200)


class TaskMgrCaseTbl(models.Model):
    class Meta:
        db_table = 'CASE_RECORD'

    taskid = models.CharField(db_column='TASKID', max_length=200)
    testid = models.CharField(db_column='CASEID', primary_key=True, max_length=200)
    testresult = models.CharField(db_column='CASERET', max_length=200)
    testdes = models.CharField(db_column='CASEDES', max_length=200)


class ScriptUploadingStatus (models.Model):
    class Meta:
        db_table = 'script_upload_status'
    package_id = models.CharField(db_column='package_id',primary_key=True, max_length=200)
    update_time = models.DateTimeField(db_column='update_time')

    def __str__(self):
        return self.package_id


