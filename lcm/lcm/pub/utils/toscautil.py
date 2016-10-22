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


def find_node_name(node_id, node_list):
    for node in node_list:
        if node['id'] == node_id:
            return node['template_name']
    raise Exception('can not find node by node_id:%s when convert nsd or vnfd model.'
                    % node_id)


def find_node_type(node_id, node_list):
    for node in node_list:
        if node['id'] == node_id:
            return node['type_name']
    raise Exception('can not find node by node_id:%s when convert nsd or vnfd model.'
                    % node_id)


def find_related_node(node_id, source_node_list, relation_type_name):
    related_nodes = []
    for node in source_node_list:
        if 'relationships' in node.keys():
            for relation in node['relationships']:
                if relation_type_name is not None:
                    if relation['type_name'] == relation_type_name \
                            and relation['target_node_id'] == node_id:
                        related_nodes.append(node['template_name'])
    return related_nodes


def convert_metadata(source_json_dict):
    source_meta_data = {}
    if 'metadata' in source_json_dict.keys():
        source_meta_data = source_json_dict['metadata']
    return source_meta_data


def convert_inputs(source_json_dict):
    inputs = {}
    if 'inputs' in source_json_dict.keys():
        source_inputs = source_json_dict['inputs']
        for param_name, param_info in source_inputs.items():
            input_param = {}
            if 'type_name' in param_info.keys():
                input_param['type'] = param_info['type_name']
            if 'description' in param_info.keys():
                input_param['description'] = param_info['description']
            if 'value' in param_info.keys():
                input_param['value'] = param_info['value']
            inputs[param_name] = input_param
    return inputs


def convert_vnf_node(source_node, source_node_list):
    vnf_node = {'type': source_node['type_name'], 'vnf_id': source_node['template_name'], 'description': '',
                'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                vnf_node['properties'][prop_name] = prop_info['value']
    vnf_node['dependencies'] = []
    vnf_node['networks'] = []
    source_relationships = source_node['relationships'] if 'relationships' in source_node else []
    for relation in source_relationships:
        if relation['type_name'].endswith('.VirtualLinksTo'):
            related_network_info = {'key_name': '',
                                    'vl_id': find_node_name(relation['target_node_id'], source_node_list)}
            # related_network_info['key_name'] = relation['relationship_name']
            # aria parser not support relationship_name
            vnf_node['networks'].append(related_network_info)
        elif relation['type_name'].endswith('.DependsOn'):
            vnf_node['dependencies'].append(find_node_name(relation['target_node_id'], source_node_list))
    return vnf_node


def convert_pnf_node(source_node, source_node_list):
    pnf_node = {'pnf_id': source_node['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                pnf_node['properties'][prop_name] = prop_info['value']
    pnf_node['cps'] = find_related_node(source_node['id'], source_node_list, 'tosca.relationships.nfv.VirtualBindsTo')
    return pnf_node


def convert_vl_node(source_node, source_node_list):
    vl_node = {'vl_id': source_node['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                vl_node['properties'][prop_name] = prop_info['value']
    vl_node['route_id'] = ''
    if 'relationships' in source_node.keys():
        source_relationships = source_node['relationships']
        for relation in source_relationships:
            if relation['type_name'].endswith('.VirtualLinksTo'):
                vl_node['route_id'] = find_node_name(relation['target_node_id'], source_node_list)
            break
    vl_node['route_external'] = False
    if source_node['type_name'].find('.RouteExternalVL') > 0:
        vl_node['route_external'] = True
    return vl_node


def convert_cp_node(source_node, source_node_list, model_type='NSD'):
    cp_node = {'cp_id': source_node['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                cp_node['properties'][prop_name] = prop_info['value']
    source_relationships = source_node['relationships']
    for relation in source_relationships:
        if relation['type_name'].endswith('.VirtualLinksTo') > 0:
            cp_node['vl_id'] = find_node_name(relation['target_node_id'], source_node_list)
        elif relation['type_name'].endswith('.VirtualBindsTo') > 0:
            if model_type == 'NSD':
                cp_node['pnf_id'] = find_node_name(relation['target_node_id'], source_node_list)
            else:
                cp_node['vdu_id'] = find_node_name(relation['target_node_id'], source_node_list)
    return cp_node


def convert_router_node(source_node, source_node_list):
    router_node = {'router_id': source_node['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                router_node['properties'][prop_name] = prop_info['value']
    source_relationships = source_node['relationships']
    for relation in source_relationships:
        if relation['type_name'].endswith('.ExternalVirtualLinksTo'):
            router_node['external_vl_id'] = find_node_name(relation['target_node_id'], source_node_list)
            router_node['external_ip_addresses'] = []
            if 'properties' in relation.keys():
                for prop_name, prop_info in relation['properties'].items():
                    if prop_name == 'router_ip_address':
                        router_node['external_ip_addresses'] = prop_info['value']
                        break
    return router_node


def convert_fp_node(source_node, source_node_list):
    fp_node = {'fp_id': source_node['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                fp_node['properties'][prop_name] = prop_info['value']
    fp_node['forwarder_list'] = []
    if 'relationships' in source_node.keys():
        source_relationships = source_node['relationships']
        for relation in source_relationships:
            if relation['type_name'].endswith('.ForwardsTo'):
                forwarder_point = {'type': 'vnf'}
                target_node_type = find_node_type(relation['target_node_id'], source_node_list).upper()
                if target_node_type.find('.CP.') >= 0 or target_node_type.endswith('.CP'):
                    forwarder_point['type'] = 'cp'
                forwarder_point['node_name'] = find_node_name(relation['target_node_id'], source_node_list)
                # forwarder_point['capability'] = relation['target_capability_name']
                # aria parser not support capability_name
                forwarder_point['capability'] = ''
    return fp_node


def convert_vnffg_group(source_group, source_group_list, source_node_list):
    vnffg = {'vnffg_id': source_group['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_group['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                vnffg['properties'][prop_name] = prop_info['value']
    vnffg['members'] = []
    source_member_nodes = source_group['member_node_ids']
    for member_node_id in source_member_nodes:
        vnffg['members'].append(find_node_name(member_node_id, source_node_list))
    return vnffg


def convert_imagefile_node(source_node, source_node_list):
    image_node = {'image_file_id': source_node['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                image_node['properties'][prop_name] = prop_info['value']
    return image_node


def convert_localstorage_node(source_node, source_node_list):
    localstorage_node = {'local_storage_id': source_node['template_name'], 'description': '', 'properties': {}}
    source_node_properties = source_node['properties']
    if source_node_properties is not None:
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                localstorage_node['properties'][prop_name] = prop_info['value']
    return localstorage_node


def convert_vdu_node(source_node, source_node_list):
    vdu_node = {'vdu_id': source_node['template_name'], 'description': '', 'properties': {}}
    if 'properties' in source_node.keys():
        source_node_properties = source_node['properties']
        for prop_name, prop_info in source_node_properties.items():
            if 'value' in prop_info.keys():
                vdu_node['properties'][prop_name] = prop_info['value']
    vdu_node['image_file'] = ''
    vdu_node['local_storages'] = []
    vdu_node['dependencies'] = []
    source_relationships = source_node['relationships']
    for relation in source_relationships:
        # if relation['type_name'] == 'guest_os':
        if relation['type_name'].endswith('.DependsOn'):
            vdu_node['image_file'] = find_node_name(relation['target_node_id'], source_node_list)
        # elif relation['type_name'] == 'local_storage':
        elif relation['type_name'].endswith('.LocalAttachesTo'):
            vdu_node['local_storages'].append(find_node_name(relation['target_node_id'], source_node_list))
        # elif relation['type_name'] == 'block_storage':
        elif relation['type_name'].endswith('.AttachesTo'):
            target_node_type = find_node_type(relation['target_node_id'], source_node_list).upper()
            if target_node_type.endswith('.BlockStorage.Local') \
                    or target_node_type.endswith('.LocalStorage'):
                vdu_node['local_storages'].append(find_node_name(relation['target_node_id'], source_node_list))
                # elif relation['template_name'] == 'dependency':
                #    vdu_node['dependencies'].append(find_node_name(relation['target_node_id'],source_node_list))
    vdu_node['nfv_compute'] = {}
    source_capabilities = source_node['capabilities']
    for capability in source_capabilities:
        if capability['name'] == 'nfv_compute':
            for prop_name, prop_info in capability['properties'].items():
                if 'value' in prop_info.keys():
                    vdu_node['nfv_compute'][prop_name] = prop_info['value']
    vdu_node['cps'] = find_related_node(source_node['id'], source_node_list, 'tosca.relationships.nfv.VirtualBindsTo')
    vdu_node['vls'] = []
    for cp_node in vdu_node['cps']:
        for source_cp_node in source_node_list:
            if source_cp_node['template_name'] == cp_node:
                relationships = source_cp_node['relationships']
                for relation in relationships:
                    if relation['type_name'].find('.VirtualLinksTo') > 0:
                        vl_node_name = find_node_name(relation['target_node_id'], source_node_list)
                        if vl_node_name not in vdu_node['vls']:
                            vdu_node['vls'].append(vl_node_name)
    vdu_node['artifacts'] = []
    if 'artifacts' in source_node.keys():
        source_artifacts = source_node['artifacts']
        for item in source_artifacts:
            artifact = {'artifact_name': item['name'], 'type': item['type_name'], 'file': item['source_path']}
            vdu_node['artifacts'].append(artifact)
    return vdu_node


def convert_nsd_model(source_json=None):
    target_json = {}
    if source_json is None:
        return target_json
    if type(source_json) is str:
        source_json_dict = json.loads(source_json)
    else:
        source_json_dict = source_json
    # convert metadata
    target_json['metadata'] = convert_metadata(source_json_dict)
    # convert inputs
    target_json['inputs'] = convert_inputs(source_json_dict)
    # convert nodes
    target_json['vnfs'] = []
    target_json['pnfs'] = []
    target_json['vls'] = []
    target_json['cps'] = []
    target_json['fps'] = []
    target_json['routers'] = []
    source_nodes = source_json_dict['nodes']
    if source_nodes is not None:
        for node in source_nodes:
            type_name = node['type_name']
            if type_name.find('.VNF.') > 0 or type_name.endswith('.VNF'):
                target_json['vnfs'].append(convert_vnf_node(node, source_nodes))
            elif type_name.find('.PNF.') > 0 or type_name.endswith('.PNF'):
                target_json['pnfs'].append(convert_pnf_node(node, source_nodes))
            elif type_name.find('.VL.') > 0 or type_name.endswith('.VL') \
                    or node['type_name'].find('.RouteExternalVL') > 0:
                target_json['vls'].append(convert_vl_node(node, source_nodes))
            elif type_name.find('.CP.') > 0 or type_name.endswith('.CP'):
                target_json['cps'].append(convert_cp_node(node, source_nodes))
            elif type_name.find('.FP.') > 0 or type_name.endswith('.FP'):
                target_json['fps'].append(convert_fp_node(node, source_nodes))
            elif type_name.endswith('.Router'):
                target_json['routers'].append(convert_router_node(node, source_nodes))
    # convert groups
    target_json['vnffgs'] = []
    if 'groups' in source_json_dict.keys():
        source_groups = source_json_dict['groups']
        if source_groups is not None:
            for group in source_groups:
                type_name = group['type_name'].upper()
                if type_name.find('.VNFFG.') >= 0 or type_name.endswith('.VNFFG'):
                    target_json['vnffgs'].append(convert_vnffg_group(group, source_groups, source_nodes))
    # convert substitution_mappings
    target_json['ns_exposed'] = {}
    external_cps = []
    forward_cps = []
    if 'substitution' in source_json_dict.keys():
        source_substitution = source_json_dict['substitution']
        if 'requirements' in source_substitution.keys():
            for item in source_substitution['requirements']:
                external_cps.append({'key_name': item['mapped_name'],
                                     "cp_id": find_node_name(item['node_id'], source_nodes)})
        if 'capabilities' in source_substitution.keys():
            for item in source_substitution['capabilities']:
                forward_cps.append({'key_name': item['mapped_name'],
                                    "cp_id": find_node_name(item['node_id'], source_nodes)})
    target_json['ns_exposed']['external_cps'] = external_cps
    target_json['ns_exposed']['forward_cps'] = forward_cps
    return json.dumps(target_json)


def convert_vnfd_model(source_json=None):
    target_json = {}
    if source_json is None:
        return target_json
    source_json_dict = json.loads(source_json)
    # convert metadata
    target_json['metadata'] = convert_metadata(source_json_dict)
    # convert inputs
    target_json['inputs'] = convert_inputs(source_json_dict)
    # convert nodes
    target_json['image_files'] = []
    target_json['local_storages'] = []
    target_json['vdus'] = []
    target_json['vls'] = []
    target_json['cps'] = []
    target_json['routers'] = []
    source_nodes = source_json_dict['nodes']
    if source_nodes is not None:
        for node in source_nodes:
            type_name = node['type_name']
            if type_name.endswith('.ImageFile'):
                target_json['image_files'].append(convert_imagefile_node(node, source_nodes))
            elif type_name.endswith('.BlockStorage.Local') or type_name.endswith('.LocalStorage'):
                target_json['local_storages'].append(convert_localstorage_node(node, source_nodes))
            elif type_name.find('.VDU.') > 0 or type_name.endswith('.VDU'):
                target_json['vdus'].append(convert_vdu_node(node, source_nodes))
            elif type_name.find('.VL.') > 0 or type_name.endswith('.VL') \
                    or node['type_name'].find('.RouteExternalVL') > 0:
                target_json['vls'].append(convert_vl_node(node, source_nodes))
            elif type_name.find('.CP.') > 0 or type_name.endswith('.CP'):
                target_json['cps'].append(convert_cp_node(node, source_nodes, 'VNFD'))
            elif type_name.endswith('.Router'):
                target_json['routers'].append(convert_router_node(node, source_nodes))
    # convert substitution_mappings
    target_json['vnf_exposed'] = {}
    external_cps = []
    forward_cps = []
    if 'substitution' in source_json_dict.keys():
        source_substitution = source_json_dict['substitution']
        if 'requirements' in source_substitution.keys():
            for item in source_substitution['requirements']:
                external_cps.append({'key_name': item['mapped_name'],
                                     "cp_id": find_node_name(item['node_id'], source_nodes)})
        if 'capabilities' in source_substitution.keys():
            for item in source_substitution['capabilities']:
                forward_cps.append({'key_name': item['mapped_name'],
                                    "cp_id": find_node_name(item['node_id'], source_nodes)})
    target_json['vnf_exposed']['external_cps'] = external_cps
    target_json['vnf_exposed']['forward_cps'] = forward_cps
    return json.dumps(target_json)

if __name__ == '__main__': 
    src_json = json.JSONEncoder().encode(
        {
            "metadata": {
                "invariant_id": "VBRAS_NS_NO_SFC",
                "name": "VBRAS_NS",
                "version": "1.0",
                "vendor": "ZTE",
                "id": "VBRAS_NS_ZTE_1.0",
                "description": "VBRAS_ZTE_NS"
            },
            "nodes": [
                {
                    "id": "VBras_e4m2s6k126txi8yu65ggr0p54",
                    "type_name": "tosca.nodes.nfv.ext.zte.VNF.VBras",
                    "template_name": "VBras",
                    "properties": {
                        "plugin_info": {
                            "type_name": "string",
                            "value": "vbrasplugin_1.0"
                        },
                        "vendor": {
                            "type_name": "string",
                            "value": "zte"
                        },
                        "is_shared": {
                            "type_name": "string",
                            "value": "False"
                        },
                        "name": {
                            "type_name": "string",
                            "value": "vbras"
                        },
                        "vnf_extend_type": {
                            "type_name": "string",
                            "value": "driver"
                        },
                        "id": {
                            "type_name": "string",
                            "value": "zte_vbras_1.0"
                        },
                        "version": {
                            "type_name": "string",
                            "value": "1.0"
                        },
                        "nsh_aware": {
                            "type_name": "string",
                            "value": "True"
                        },
                        "cross_dc": {
                            "type_name": "string",
                            "value": "False"
                        },
                        "vnf_type": {
                            "type_name": "string",
                            "value": "vbras"
                        },
                        "externalDataNetworkName": {
                            "type_name": "string",
                            "value": "vlan_4004_tunnel_net"
                        },
                        "vnfd_version": {
                            "type_name": "string",
                            "value": "1.0.0"
                        },
                        "request_reclassification": {
                            "type_name": "string",
                            "value": "False"
                        }
                    },
                    "interfaces": [
                        {
                            "name": "Standard",
                            "type_name": "tosca.interfaces.node.lifecycle.Standard"
                        }
                    ],
                    "capabilities": [
                        {
                            "name": "feature",
                            "type_name": "tosca.capabilities.Node"
                        },
                        {
                            "name": "forwarder",
                            "type_name": "tosca.capabilities.nfv.Forwarder"
                        }
                    ]
                },
                {
                    "id": "ext_mnet_net_4b6snzsooyg2wvtr0r3n48dd9",
                    "type_name": "tosca.nodes.nfv.ext.zte.VL",
                    "template_name": "ext_mnet_net",
                    "properties": {
                        "name": {
                            "type_name": "string",
                            "value": "vlan_4004_tunnel_net"
                        },
                        "mtu": {
                            "type_name": "integer",
                            "value": 1500
                        },
                        "location_info": {
                            "type_name": "tosca.datatypes.nfv.ext.LocationInfo",
                            "value": {
                                "vimid": 2,
                                "tenant": "admin",
                                "availability_zone": "nova"
                            }
                        },
                        "ip_version": {
                            "type_name": "integer",
                            "value": 4
                        },
                        "dhcp_enabled": {
                            "type_name": "boolean",
                            "value": True
                        },
                        "network_name": {
                            "type_name": "string",
                            "value": "vlan_4004_tunnel_net"
                        },
                        "network_type": {
                            "type_name": "string",
                            "value": "vlan"
                        }
                    },
                    "interfaces": [
                        {
                            "name": "Standard",
                            "type_name": "tosca.interfaces.node.lifecycle.Standard"
                        }
                    ],
                    "capabilities": [
                        {
                            "name": "feature",
                            "type_name": "tosca.capabilities.Node"
                        },
                        {
                            "name": "virtual_linkable",
                            "type_name": "tosca.capabilities.nfv.VirtualLinkable"
                        }
                    ]
                }
            ],
            "substitution": {
                "node_type_name": "tosca.nodes.nfv.NS.VBRAS_NS"
            },
            "inputs": {
                "externalManageNetworkName": {
                    "type_name": "string",
                    "value": "vlan_4004_tunnel_net"
                }
            }
        }
    )
    print convert_nsd_model(src_json)



