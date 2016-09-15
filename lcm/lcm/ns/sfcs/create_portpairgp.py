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


import json

from lcm.ns.const import OWNER_TYPE
from lcm.ns.sfcs.utils import get_fp_model_by_fp_inst_id
from lcm.pub.database.models import FPInstModel, NfInstModel, CPInstModel, PortInstModel
from lcm.pub.msapi import extsys
from lcm.pub.msapi import sdncdriver


class CreatePortPairGroup(object):
    def __init__(self, data):
        self.fp_inst_id = data["fpinstid"]
        self.ns_model_data = data["ns_model_data"]
        self.ns_inst_id = data["ns_inst_id"]
        self.fp_model = get_fp_model_by_fp_inst_id(self.ns_model_data,
                                                   self.fp_inst_id)
        self.port_pair_groups = []

    def do_biz(self):
        self.init_port_pair_group(self.fp_model)
        self.create_port_pair_and_groups()

    def create_port_pair_and_groups(self):
        for port_pair_group_info in self.port_pair_groups:
            port_pairs_info = port_pair_group_info["portpair"]
            port_pair_ids = []
            for port_pair_info in port_pairs_info:
                port_pair_ids.append(self.create_port_pair(port_pair_info))
            self.create_port_pair_and_group(port_pair_ids)

    def create_port_pair_and_group(self, port_pair_ids):
        # url = const.port_pair_group_url
        port_pair_group = {
            "sdnControllerId": self.sdncontrollerid,
            "url": extsys.get_sdn_controller_by_id(self.sdncontrollerid)["url"],
            "portPairs": port_pair_ids
        }
        # req_param = json.JSONEncoder.encode(port_pair_group)
        # ret = req_by_msb("OPENAPI_CREATE_PORTPAIRGROUP", port_pair_group)
        # if (ret[0] > 0):
        #     sfc_inst_failed_handle(self.fp_inst_id, "create port pair request to Driver failed.")
        #     raise NSLCMException('create port pair request to Driver failed.')
        # ret_body = json.loads(ret[1])
        # port_pair_group_id = ignorcase_get(ret_body, "id")
        port_pair_group_id = sdncdriver.create_port_pair_group(port_pair_group)

        port_pair_group_info = {
            "groupid": port_pair_group_id,
            "portpair": port_pair_ids
        }

        port_pair_group_in_db_info = FPInstModel.objects.filter(fpinstid=self.fp_inst_id).get().portpairgroups
        port_pair_group_db = []
        if port_pair_group_in_db_info:
            port_pair_group_db = json.loads(port_pair_group_in_db_info)
        port_pair_group_db.append(port_pair_group_info)
        FPInstModel.objects.filter(fpinstid=self.fp_inst_id).update(
            portpairgroups=json.JSONEncoder().encode(port_pair_group_db))

    def create_port_pair(self, port_pair_info):
        # url = const.port_pair_url
        port_pair_info.update({
            "sdnControllerId": self.sdncontrollerid,
            "url": extsys.get_sdn_controller_by_id(self.sdncontrollerid)["url"]
        })
        return sdncdriver.create_port_pair(port_pair_info)
        # req_param = json.JSONEncoder.encode(port_pair_info)
        # ret = req_by_msb(url,"POST",port_pair_info)
        # ret = req_by_msb("OPENAPI_CREATE_PORTPAIR", port_pair_info)
        # if (ret[0] != 0):
        #     sfc_inst_failed_handle(self.fp_inst_id, "create port pair request to Driver failed.")
        #     raise NSLCMException('create port pair request to Driver failed.')
        # ret_body = json.loads(ret[1])
        # return ret_body["id"]

    def init_port_pair_group(self, fp_model):
        forwarder_list = fp_model["forwarder_list"]
        self.sdncontrollerid = FPInstModel.objects.filter(fpinstid=self.fp_inst_id).get().sdncontrollerid
        index = 0
        while index < len(forwarder_list):
            if (forwarder_list[index]["type"] == "cp"):
                index += self.generate_port_pair_group_cp(index, forwarder_list)
            else:
                self.generate_port_pair_group_vnf(index, forwarder_list)
            index += 1

    def generate_port_pair_group_cp(self, index, forwarder_list):
        cur_forwarder = forwarder_list[index]["node_name"]
        cur_cp_model_info = self.get_cp_model_info_by_cpid(cur_forwarder)
        if (index < len(forwarder_list) - 1 and forwarder_list[index + 1]["type"] == "cp"):
            next_forward = forwarder_list[index + 1]["node_name"]
            next_cp_model_info = self.get_cp_model_info_by_cpid(next_forward)
            if (cur_cp_model_info["pnf_id"] == next_cp_model_info["pnf_id"]):  # same port pair group
                self.generate_port_pair_group_type_cp(cur_cp_model_info, next_cp_model_info)
                return 1
            else:
                self.generate_port_pair_group_type_cp(cur_cp_model_info, cur_cp_model_info)
        else:
            self.generate_port_pair_group_type_cp(cur_cp_model_info, cur_cp_model_info)
        return 0

    def generate_port_pair_group_vnf(self, index, forwarder_list):
        cur_vnf_id = forwarder_list[index]["node_name"]
        cur_forwarder = forwarder_list[index]["capability"]
        self.vnf_model_in_ns_info = self.get_vnf_model_info_by_vnf_id(cur_vnf_id)
        vnf_inst_database_info = NfInstModel.objects.filter(vnf_id=self.vnf_model_in_ns_info["vnf_id"],
                                                            ns_inst_id=self.ns_inst_id).get()
        self.vnf_inst_database_info = vnf_inst_database_info
        vnfd_model_info = json.loads(vnf_inst_database_info.vnfd_model)
        self.vnfd_model_info = vnfd_model_info
        cpd_id = self.get_cpdid_info_forwarder(vnfd_model_info, cur_forwarder)
        self.cp_model_in_vnf = self.get_cp_from_vnfd_model(cpd_id)
        cp_inst_infos = CPInstModel.objects.filter(cpdid=cpd_id,
                                                   ownertype=OWNER_TYPE.VNF,
                                                   ownerid=vnf_inst_database_info.nfinstid)
        port_pairs = []
        if (index < len(forwarder_list) - 1 and forwarder_list[index + 1]["type"] == "vnf"):
            next_vnf_id = forwarder_list[index + 1]["node_name"]
            if (cur_vnf_id != next_vnf_id):
                for cp_inst_info in cp_inst_infos:
                    port_pairs.append(self.generate_port_pair_group(cp_inst_info, cp_inst_info))
            else:
                next_forwarder = forwarder_list[index]["capability"]
                next_forwarder_cpd_id = self.get_cpdid_info_forwarder(vnfd_model_info, next_forwarder)
                next_cp_inst_infos = CPInstModel.objects.filter(cpdid=next_forwarder_cpd_id,
                                                                ownertype=OWNER_TYPE.VNF,
                                                                ownerid=vnf_inst_database_info.nfinstid)

                port_pairs.add(self.generate_port_pair_group(cp_inst_infos[0], next_cp_inst_infos[0]))


        else:
            for cp_inst_info in cp_inst_infos:
                port_pairs.append(self.generate_port_pair_group(cp_inst_info, cp_inst_info))
        self.port_pair_groups.append(
            {
                "portpair": port_pairs
            }
        )

    def generate_port_pair_group_type_cp(self, ingress_cp_model_info, egress_cp_model_info):
        sfType = ""
        requestReclassification = False
        nshAware = True
        sfParam = {}
        if ingress_cp_model_info["pnf_id"] == "":
            pass
        else:
            pnf_model_info = self.get_pnf_model_info_by_cpid(ingress_cp_model_info["pnf_id"])
            sfType = pnf_model_info["properties"]["pnf_type"]
            nshAware = pnf_model_info["properties"]["nsh_aware"]
            requestReclassification = pnf_model_info["properties"]["request_reclassification"]
            sfParam = pnf_model_info["properties"]
        port_pair_info = {
            "sfType": sfType,
            "nshAware": nshAware,
            "requestReclassification": requestReclassification,
            "ingress": {
                "encapsulation": ingress_cp_model_info["properties"]["sfc_encapsulation"],
                "ip": ingress_cp_model_info["properties"]["ip_address"],
                "mac": ingress_cp_model_info["properties"]["mac_address"]
            },
            "egress": {
                "encapsulation": egress_cp_model_info["properties"]["sfc_encapsulation"],
                "ip": egress_cp_model_info["properties"]["ip_address"],
                "mac": egress_cp_model_info["properties"]["mac_address"]
            },
            "sfParam": sfParam
        }
        self.port_pair_groups.append(
            {
                "portpair": [port_pair_info]
            }
        )

    def generate_port_pair_group(self, ingress_cp_inst_info, egress_cp_inst_info):
        if (ingress_cp_inst_info.relatedport != ""):
            ingress_port = ingress_cp_inst_info.relatedport
        else:
            ingress_port = CPInstModel.objects.filter(cpinstanceid=ingress_cp_inst_info.relatedcp,
                                                      ownertype="vnf",
                                                      ownerid=self.vnf_inst_database_info.nfinstid).get().relatedport
        if (egress_cp_inst_info.relatedport != ""):
            egress_port = egress_cp_inst_info.relatedport
        else:
            egress_port = CPInstModel.objects.filter(cpinstanceid=egress_cp_inst_info.relatedcp,
                                                     ownertype="vnf",
                                                     ownerid=self.vnf_inst_database_info.nfinstid).get().relatedport
        ingress_port_inst_info = PortInstModel.objects.filter(portid=ingress_port).get()
        egress_port_inst_info = PortInstModel.objects.filter(portid=egress_port).get()

        port_pair_info = {
            "sfType": self.vnf_model_in_ns_info["properties"]["vnf_type"],
            "nshAware": self.vnf_model_in_ns_info["properties"]["nsh_aware"],
            "requestReclassification": self.vnf_model_in_ns_info["properties"]["request_reclassification"],
            "ingress": {
                "encapsulation": ingress_port_inst_info.sfcencapsulation,
                "ip": ingress_port_inst_info.ipaddress,
                "mac": ingress_port_inst_info.macaddress
            },
            "egress": {
                "encapsulation": egress_port_inst_info.sfcencapsulation,
                "ip": egress_port_inst_info.ipaddress,
                "mac": egress_port_inst_info.macaddress
            },
            "sfParam": self.vnf_model_in_ns_info["properties"]
        }
        return port_pair_info

    def get_cp_from_vnfd_model(self, cpdid):
        for cp_model in self.vnfd_model_info["cps"]:
            if cp_model["cp_id"] == cpdid:
                return cp_model

    def get_pnf_model_info_by_cpid(self, pnfid):
        for pnf_model_info in self.ns_model_data["pnfs"]:
            if (pnf_model_info["pnf_id"] == pnfid):
                return pnf_model_info

    def get_cp_model_info_by_cpid(self, cpid):
        for cp_model_info in self.ns_model_data["cps"]:
            if (cp_model_info["cp_id"] == cpid):
                return cp_model_info

    def get_vnf_model_info_by_vnf_id(self, vnfid):
        for vnf_model_info in self.ns_model_data["vnfs"]:
            if (vnf_model_info["vnf_id"] == vnfid):
                return vnf_model_info

    def get_cpdid_info_forwarder(self, vnf_model, forwarder):
        for forwarder_info in vnf_model["vnf_exposed"]["forward_cps"]:
            if (forwarder_info["key_name"] == forwarder):
                return forwarder_info["cp_id"]
