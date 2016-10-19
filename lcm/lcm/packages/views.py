#!/usr/bin/env python
# -*- coding: utf-8 -*-
import logging
import uuid

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from lcm.pub.utils.values import ignore_case_get
from lcm.pub.utils.syscomm import fun_name
from lcm.packages import ns_package, nf_package

logger = logging.getLogger(__name__)


@api_view(http_method_names=['POST'])
def ns_on_boarding(request, *args, **kwargs):
    csar_id = ignore_case_get(request.data, "csarId")
    logger.info("Enter %s, method is %s, csar_id is %s", fun_name(), request.method, csar_id)
    ret = ns_package.ns_on_boarding(csar_id)
    logger.info("Leave %s, Return value is %s", fun_name(), str(ret))
    if ret[0] != 0:
        return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(data=ret[1], status=status.HTTP_202_ACCEPTED)


@api_view(http_method_names=['GET', 'DELETE'])
def ns_access_csar(request, *args, **kwargs):
    csar_id = ignore_case_get(kwargs, "csarId")
    logger.info("Enter %s, method is %s, csar_id is %s", fun_name(), request.method, csar_id)
    ret, normal_status = None, None
    if request.method == 'GET':
        ret = ns_package.ns_get_csar(csar_id)
        normal_status = status.HTTP_200_OK
    else:
        ret = ns_package.ns_delete_csar(csar_id)
        normal_status = status.HTTP_202_ACCEPTED
    logger.info("Leave %s, Return value is %s", fun_name(), str(ret))
    if ret[0] != 0:
        return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(data=ret[1], status=normal_status)


@api_view(http_method_names=['DELETE'])
def ns_delete_pending_csar(request, *args, **kwargs):
    csar_id = ignore_case_get(kwargs, "csarId")
    logger.info("Enter %s, method is %s, csar_id is %s", fun_name(), request.method, csar_id)
    ret = ns_package.ns_delete_pending_csar(csar_id)
    logger.info("Leave %s, Return value is %s", fun_name(), str(ret))
    if ret[0] != 0:
        return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    return Response(data=ret[1], status=status.HTTP_202_ACCEPTED)


#################################################################################################################
@api_view(http_method_names=['POST'])
def nf_on_boarding(request, *args, **kwargs):
    logger.info("Enter %s%s, method is %s", fun_name(), args, request.method)
    csar_id = ignore_case_get(kwargs, "csarId")
    vim_ids = ignore_case_get(kwargs, "vimIds")
    lab_vim_id = ignore_case_get(kwargs, "labVimId")
    job_id = str(uuid.uuid4())
    nf_package.NfOnBoardingThread(csar_id, vim_ids, lab_vim_id, job_id).start()
    ret = {"jobId": job_id}
    logger.info("Leave %s, Return value is %s", fun_name(), str(ret))
    return Response(data=ret, status=status.HTTP_202_ACCEPTED)


@api_view(http_method_names=['GET', 'DELETE'])
def nf_access_csar(request, *args, **kwargs):
    logger.info("Enter %s%s, method is %s", fun_name(), args, request.method)
    csar_id = ignore_case_get(kwargs, "csarId")
    if request.method == 'GET':
        ret = nf_package.NfPackage().get_csar(csar_id)
        logger.info("Leave %s, Return value is %s", fun_name(), str(ret))
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(data=ret[1], status=status.HTTP_200_OK)
    # NF package deleting
    job_id = str(uuid.uuid4())
    nf_package.NfPkgDeleteThread(csar_id, job_id).start()
    ret = {"jobId": job_id}
    logger.info("Leave %s, Return value is %s", fun_name(), str(ret))
    return Response(data=ret, status=status.HTTP_202_ACCEPTED)


@api_view(http_method_names=['DELETE'])
def nf_delete_pending_csar(request, *args, **kwargs):
    logger.info("Enter %s%s, method is %s", fun_name(), args, request.method)
    csar_id = ignore_case_get(kwargs, "csarId")
    job_id = str(uuid.uuid4())
    nf_package.NfPkgDeletePendingThread(csar_id, job_id).start()
    ret = {"jobId": job_id}
    logger.info("Leave %s, Return value is %s", fun_name(), str(ret))
    return Response(data=ret, status=status.HTTP_202_ACCEPTED)
