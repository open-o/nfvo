#!/usr/bin/env python
# -*- coding: utf-8 -*-
import logging

from rest_framework.views import APIView
from rest_framework.response import Response

logger = logging.getLogger(__name__)


class NsPackageView(APIView):
    def do_post(self, request, format=None):
        pass
