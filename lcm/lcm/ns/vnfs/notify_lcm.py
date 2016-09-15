# Copyright 2016 [ZTE] and others.
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

import logging
import json
import traceback

from lcm.pub.utils.restcall import req_by_msb
from rest_framework import status
from rest_framework.response import Response
from lcm.pub.exceptions import NSLCMException
from lcm.pub.database.models import VNFCInstModel,VLInstModel,NfInstModel


logger = logging.getLogger(__name__)



class NotifyLcm(object):
    def __init__(self, vnfmid, vnfInstanceId, data):

        self.vnf_instid = ''
        self.vnfmid = vnfmid
        self.m_vnfInstanceId = vnfInstanceId
        self.status = data['status']
        self.operation = data['operation']
        self.lcm_jobid = data['jobId']
        self.vnfdmodule = data['vnfdmodule']
        self.affectedVnfc = data['affectedVnfc']
        self.affectedVl = data['affectedVl']
        self.affectedVirtualStorage = data['affectedVirtualStorage']

    def do_biz(self):
        try:
            self.vnf_instid = self.get_vnfinstid(self.m_vnfInstanceId,self.vnfmid)
            self.update_Vnfc()
            self.update_Vl()
            self.update_Storage()
            self.update_vnf_by_vnfdmodule()
        except NSLCMException as e:
            self.exception(e.message)
        except Exception:
            logger.error(traceback.format_exc())
            self.exception('unexpected exception')

    def get_vnfinstid(self,mnfinstid,vnfm_inst_id):
        nfinst = NfInstModel.objects.filter(mnfinstid=mnfinstid,vnfm_inst_id=vnfm_inst_id).first()
        if nfinst:
            return nfinst.nfinstid
        else:
            self.exception('vnfinstid not exist')

    def exception(self, error_msg):
        logger.error('Notify Lcm failed, detail message: %s' % error_msg)
        return Response(data={'error': '%s' % error_msg}, status=status.HTTP_409_CONFLICT)

    def update_Vnfc(self):
        for vnfc in self.affectedVnfc:
            vnfcInstanceId = vnfc['vnfcInstanceId']
            vduId = vnfc['vduId']
            changeType = vnfc['changeType']
            vmResource = vnfc['vmResource']
            vimId = vmResource['vimId']
            resourceType = vmResource['resourceType']
            resourceId = vmResource['resourceId']
            resourceName = vmResource['resourceName']

            if resourceType != 'vm':
                self.exception('affectedVnfc struct error: resourceType not euqal vm')

            if changeType == 'added':
                VNFCInstModel(vnfcinstanceid=vnfcInstanceId,vduid=vduId,nfinstid=self.vnf_instid,vmid=resourceId).save()
            elif changeType == 'removed':
                VNFCInstModel.objects.filter(vnfcinstanceid=vnfcInstanceId).delete()
            elif changeType == 'modified':
                VNFCInstModel.objects.filter(vnfcinstanceid=vnfcInstanceId).update(vduid=vduId,nfinstid=self.vnf_instid,vmid=resourceId)
            else:
                self.exception('affectedVnfc struct error: changeType not in {added,removed,modified}')


    def update_Vl(self):
        for vl in self.affectedVl:
            vlInstanceId = vl['vlInstanceId']
            vldid = vl['vldid']
            changeType = vl['changeType']
            networkResource = vl['networkResource']
            vimId = networkResource['vimId']
            resourceType = networkResource['resourceType']
            resourceId = networkResource['resourceId']
            resourceName = networkResource['resourceName']

            if resourceType != 'network':
                self.exception('affectedVl struct error: resourceType not euqal network')

            ownerId = self.vnf_instid
            ownerId = self.get_vnfinstid(self.vnf_instid,self.vnfmid)

            if changeType == 'added':
                VLInstModel(vlInstanceId=vlInstanceId,vldId=vldid,ownerType=0,ownerId=ownerId,relatedNetworkId=resourceId,vlType=0).save()
            elif changeType == 'removed':
                VLInstModel.objects.filter(vlInstanceId=vlInstanceId).delete()
            elif changeType == 'modified':
                VLInstModel.objects.filter(vlInstanceId=vlInstanceId).update(vldId=vldid,ownerType=0,ownerId=ownerId,relatedNetworkId=resourceId,vlType=0)
            else:
                self.exception('affectedVl struct error: changeType not in {added,removed,modified}')


    def update_Storage(self):
        pass

    def update_vnf_by_vnfdmodule(self):
        NfInstModel.objects.filter(nfinstid=self.vnf_instid).update(vnfd_model=self.vnfdmodule)


