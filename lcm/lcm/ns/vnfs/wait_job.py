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
import time
import datetime
import logging

import math

from lcm.pub.exceptions import NSLCMException
from lcm.pub.utils.jobutil import JobUtil, JOB_MODEL_STATUS
from lcm.pub.utils.restcall import req_by_msb

logger = logging.getLogger(__name__)


def query_job(job_id, response_id=0):
    retry_time = 3
    rsp = ''
    uri = '/openoapi/%s/v1/jobs/%s&responseId=%s' % ('ztevmanagerdriver', job_id, str(response_id))
    while retry_time > 0:
        rsp = req_by_msb(uri, "GET")
        if str(rsp[2]) == '404':
            return False, ''
        if rsp[0] != 0:
            logger.warning('retry_time=%s, detail message:%s' % (str(retry_time), rsp[1]))
            retry_time -= 1
        else:
            break
    if retry_time <= 0:
        logger.error(rsp[1])
        raise NSLCMException(msgid='Failed to query job from VNFM!')
    return True, json.JSONDecoder().decode(rsp[1])


def calc_progress(vnfm_progress, target_range=None):
    target_range = [0, 100] if not target_range else target_range
    progress = int(vnfm_progress)
    if progress > 100:
        return progress
    floor_progress = int(math.floor(float(target_range[1] - target_range[0]) / 100 * progress))
    target_range = floor_progress + target_range[0]
    return target_range


def default_callback(vnfo_job_id, vnfm_job_id, job_status, jobs, progress_range, **kwargs):
    for job in jobs:
        progress = calc_progress(job['progress'], progress_range)
        JobUtil.add_job_status(vnfo_job_id, progress, job.get('statusdescription', ''), job.get('errorcode', ''))
    latest_progress = calc_progress(job_status['progress'], progress_range)
    JobUtil.add_job_status(vnfo_job_id, latest_progress, job_status.get('statusdescription', ''),
                           job_status.get('errorcode', ''))
    if job_status['status'] in (JOB_MODEL_STATUS.ERROR, JOB_MODEL_STATUS.FINISHED):
        return True, job_status['status']
    return False, JOB_MODEL_STATUS.PROCESSING


def wait_job_finish(vnfo_job_id, vnfm_job_id, progress_range=None, timeout=600, job_callback=default_callback, **kwargs):
    progress_range = [0, 100] if not progress_range else progress_range
    response_id = 0
    query_interval = 2
    start_time = end_time = datetime.datetime.now()
    while (end_time - start_time).seconds < timeout:
        query_status, result = query_job(vnfm_job_id, response_id)
        time.sleep(query_interval)
        end_time = datetime.datetime.now()
        if not query_status:
            continue
        job_status = result['responsedescriptor']
        response_id_new = job_status['responseid']
        if response_id_new == response_id:
            continue
        response_id = response_id_new
        jobs = job_status.get('responsehistorylist', [])
        jobs.reverse()
        is_end, status = job_callback(vnfo_job_id, vnfm_job_id, job_status, jobs, progress_range, **kwargs)
        if is_end:
            return status
    return JOB_MODEL_STATUS.TIMEOUT
