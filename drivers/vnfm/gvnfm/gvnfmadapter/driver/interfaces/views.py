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

import inspect
import json
import logging
from rest_framework.decorators import api_view
from rest_framework.response import Response
from driver.pub.utils import restcall
from driver.pub.utils.restcall import req_by_msb

logger = logging.getLogger(__name__)

def fun_name():
    return "=================%s==================" % inspect.stack()[1][3]

def ignorcase_get(args, key):
    if not key:
        return ""
    if not args:
        return ""
    if key in args:
        return args[key]
    for old_key in args:
        if old_key.upper() == key.upper():
            return args[old_key]
    return ""

# Query VNFM by VNFMID
def vnfm_get(vnfmid):
    ret = req_by_msb("openoapi/extsys/v1/vnfms/%s" % vnfmid, "GET")
    return ret

# ==================================================
create_vnf_url = "v1/vnfs"

@api_view(http_method_names=['POST'])
def instantiate_vnf(request, *args, **kwargs):
    try:
        logger.debug("[%s] request.data=%s", fun_name(), request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, "url"),
            user=ignorcase_get(vnfm_info, "userName"),
            passwd=ignorcase_get(vnfm_info, "password"),
            auth_type=restcall.rest_no_auth,
            resource=create_vnf_url,
            method='post',
            content=json.JSONEncoder().encode(request.data))
        logger.debug("[%s] call_req ret=%s", fun_name(), ret)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        resp_data = json.JSONDecoder().decode(ret[1])
        logger.info("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when instantiating VNF")
        raise e
    return Response(data=resp_data, status=ret[2])


# ==================================================
vnf_delete_url = "v1/vnfs/%s"

@api_view(http_method_names=['POST'])
def terminate_vnf(request, *args, **kwargs):
    try:
        logger.debug("[%s] request.data=%s", fun_name(), request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        data = {}
        logger.debug("[%s]req_data=%s", fun_name(), data)
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, "url"),
            user=ignorcase_get(vnfm_info, "userName"),
            passwd=ignorcase_get(vnfm_info, "password"),
            auth_type=restcall.rest_no_auth,
            resource=vnf_delete_url % (ignorcase_get(kwargs, "vnfInstanceID")),
            method='delete',
            content=json.JSONEncoder().encode(data))
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        resp_data = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when terminating VNF")
        raise e
    return Response(data=resp_data, status=ret[2])


# ==================================================
vnf_detail_url = "v1/vnfs/%s"

@api_view(http_method_names=['GET'])
def query_vnf(request, *args, **kwargs):
    try:
        logger.debug("[%s] request.data=%s", fun_name(), request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        data = {}
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, "url"),
            user=ignorcase_get(vnfm_info, "userName"),
            passwd=ignorcase_get(vnfm_info, "password"),
            auth_type=restcall.rest_no_auth,
            resource=vnf_detail_url % (ignorcase_get(kwargs, "vnfInstanceID")),
            method='get',
            content=json.JSONEncoder().encode(data))
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        resp = json.JSONDecoder().decode(ret[1])
        vnf_status = ignorcase_get(resp, "vnfinstancestatus")
        resp_data = {"vnfInfo": {"vnfStatus": vnf_status}}
        logger.debug("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when querying VNF information.")
        raise e
    return Response(data=resp_data, status=ret[2])

# ==================================================
operation_status_url = '/v1/jobs/{jobId}?NFVOID={nfvoId}&VNFMID={vnfmId}&ResponseID={responseId}'

@api_view(http_method_names=['GET'])
def operation_status(request, *args, **kwargs):
    data = {}
    try:
        logger.debug("[%s] request.data=%s", fun_name(), request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, 'url'),
            user=ignorcase_get(vnfm_info, 'userName'),
            passwd=ignorcase_get(vnfm_info, 'password'),
            auth_type=restcall.rest_no_auth,
            resource=operation_status_url.format(jobId=ignorcase_get(kwargs, 'jobid'), nfvoId=1,
                                                 vnfmId=ignorcase_get(kwargs, 'vnfmid'),
                                                 responseId=ignorcase_get(request.GET, 'responseId')),
            method='get',
            content=json.JSONEncoder().encode(data))

        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        resp_data = json.JSONDecoder().decode(ret[1])
        logger.info("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when getting operation status information.")
        raise e
    return Response(data=resp_data, status=ret[2])


# ==================================================
grant_vnf_url = 'openoapi/nslcm/v1/ns/grantvnf'

@api_view(http_method_names=['PUT'])
def grantvnf(request, *args, **kwargs):
    logger.info("=====grantvnf=====")
    try:
        resp_data = {}
        logger.info("req_data = %s", request.data)
        ret = req_by_msb(grant_vnf_url, "POST", content=json.JSONEncoder().encode(request.data))
        logger.info("ret = %s", ret)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
        resp = json.JSONDecoder().decode(ret[1])
        resp_data['vimid'] = ignorcase_get(resp['vim'], 'vimid')
        resp_data['tenant'] = ignorcase_get(ignorcase_get(resp['vim'], 'accessinfo'), 'tenant')
        logger.info("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred in Grant VNF.")
        raise e
    return Response(data=resp_data, status=ret[2])


# ==================================================
notify_url = 'openoapi/nslcm/v1/ns/{vnfmid}/vnfs/{vnfInstanceId}/Notify'

@api_view(http_method_names=['POST'])
def notify(request, *args, **kwargs):
    try:
        logger.info("[%s]req_data = %s", fun_name(), request.data)
        ret = req_by_msb(notify_url.format(vnfmid=ignorcase_get(request.data, 'VNFMID'),
                                           vnfInstanceId=ignorcase_get(request.data, 'vnfinstanceid')),
                         "POST", content=json.JSONEncoder().encode(request.data))
        logger.info("[%s]data = %s", fun_name(), ret)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=ret[2])
    except Exception as e:
        logger.error("Error occurred in LCM notification.")
        raise e
    return Response(data=None, status=ret[2])