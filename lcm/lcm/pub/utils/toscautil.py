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

def safe_get(key_val, key):
    return key_val[key] if key in key_val else ""


def find_node_name(node_id, node_list):
    for node in node_list:
        if node['id'] == node_id:
            return node['template_name']
    raise Exception('can not find node(%s).' % node_id)


def find_node_type(node_id, node_list):
    for node in node_list:
        if node['id'] == node_id:
            return node['type_name']
    raise Exception('can not find node(%s).' % node_id)


def find_related_node(node_id, src_json_model, requirement_name):
    related_nodes = []
    for model_tpl in src_json_model["node_templates"]:
        for rt in safe_get(model_tpl, 'requirement_templates'):
            if safe_get(rt, 'name') == requirement_name and \
                safe_get(rt, 'target_node_template_name') == node_id:
                related_nodes.append(model_tpl['name'])
    return related_nodes


def convert_props(src_node, dest_node):
    if 'properties' in src_node and src_node['properties']:
        for prop_name, prop_info in src_node['properties'].items():
            if 'value' in prop_info:
                dest_node['properties'][prop_name] = prop_info['value']   


def convert_metadata(src_json):
    return src_json['metadata'] if 'metadata' in src_json else {}


def convert_inputs(src_json):
    inputs = {}
    if 'inputs' in src_json:
        src_inputs = src_json['inputs']
        for param_name, param_info in src_inputs.items():
            input_param = {}
            if 'type_name' in param_info:
                input_param['type'] = param_info['type_name']
            if 'description' in param_info:
                input_param['description'] = param_info['description']
            if 'value' in param_info:
                input_param['value'] = param_info['value']
            inputs[param_name] = input_param
    return inputs


def convert_vnf_node(src_node, src_json_model):
    vnf_node = {'type': src_node['type_name'], 'vnf_id': src_node['template_name'],
        'description': '', 'properties': {}, 'dependencies': [], 'networks': []}
    convert_props(src_node, vnf_node)
    for model_tpl in src_json_model['node_templates']:
        if model_tpl['name'] != vnf_node['vnf_id']:
            continue
        vnf_node['dependencies'] = [{
            'key_name': requirement['name'],
            'vl_id': requirement['target_node_template_name']} for \
            requirement in model_tpl['requirement_templates'] if \
            safe_get(requirement, 'target_capability_name') == 'virtual_linkable']
        vnf_node['networks'] = [requirement['target_node_template_name'] for \
            requirement in model_tpl['requirement_templates'] if \
            safe_get(requirement, 'name') == 'dependency']
    return vnf_node


def convert_pnf_node(src_node, src_json_model):
    pnf_node = {'pnf_id': src_node['template_name'], 'description': '', 'properties': {}}
    convert_props(src_node, pnf_node)
    pnf_node['cps'] = find_related_node(src_node['id'], src_json_model, 'virtualbinding')
    return pnf_node


def convert_vl_node(src_node, src_node_list):
    vl_node = {'vl_id': src_node['template_name'], 'description': '', 'properties': {}}
    convert_props(src_node, vl_node)
    vl_node['route_id'] = ''
    for relation in safe_get(src_node, 'relationships'):
        if safe_get(relation, 'type_name').endswith('.VirtualLinksTo'):
            vl_node['route_id'] = find_node_name(relation['target_node_id'], src_node_list)
            break
    vl_node['route_external'] = (src_node['type_name'].find('.RouteExternalVL') > 0)
    return vl_node


def convert_cp_node(src_node, src_node_list, model_type='NSD'):
    cp_node = {'cp_id': src_node['template_name'], 'description': '', 'properties': {}}
    convert_props(src_node, cp_node)
    src_relationships = src_node['relationships']
    for relation in src_relationships:
        if safe_get(relation, 'name') == 'virtualLink':
            cp_node['vl_id'] = find_node_name(relation['target_node_id'], src_node_list)
        elif safe_get(relation, 'name') == 'virtualbinding':
            node_key = 'pnf_id' if model_type == 'NSD' else 'vdu_id'
            cp_node[node_key] = find_node_name(relation['target_node_id'], src_node_list)
    return cp_node


def convert_router_node(src_node, src_node_list):
    router_node = {'router_id': src_node['template_name'], 'description': '', 'properties': {}}
    convert_props(src_node, router_node)
    for relation in src_node['relationships']:
        if safe_get(relation, 'name') != 'external_virtual_link':
            continue
        router_node['external_vl_id'] = find_node_name(relation['target_node_id'], src_node_list)
        router_node['external_ip_addresses'] = []
        if 'properties' not in relation:
            continue
        for prop_name, prop_info in relation['properties'].items():
            if prop_name == 'router_ip_address':
                router_node['external_ip_addresses'].append(prop_info['value'])
        break
    return router_node


def convert_fp_node(src_node, src_node_list):
    fp_node = {'fp_id': src_node['template_name'], 'description': '', 
        'properties': {}, 'forwarder_list': []}
    convert_props(src_node, fp_node)
    for relation in safe_get(src_node, 'relationships'):
        if safe_get(relation, 'name') != 'forwarder':
            continue
        forwarder_point = {'type': 'vnf'}
        target_node_type = find_node_type(relation['target_node_id'], src_node_list).upper()
        if target_node_type.find('.CP.') >= 0 or target_node_type.endswith('.CP'):
            forwarder_point['type'] = 'cp'
        forwarder_point['node_name'] = find_node_name(relation['target_node_id'], src_node_list)
        forwarder_point['capability'] = ''
        fp_node['forwarder_list'].append(forwarder_point)
    return fp_node


def convert_vnffg_group(src_group, src_group_list, src_node_list):
    vnffg = {'vnffg_id': src_group['template_name'], 'description': '', 
        'properties': {}, 'members': []}
    convert_props(src_group, vnffg)
    for member_node_id in src_group['member_node_ids']:
        vnffg['members'].append(find_node_name(member_node_id, src_node_list))
    return vnffg


def convert_imagefile_node(src_node, src_node_list):
    image_node = {'image_file_id': src_node['template_name'], 'description': '', 
        'properties': {}}
    convert_props(src_node, image_node)
    return image_node


def convert_localstorage_node(src_node, src_node_list):
    localstorage_node = {'local_storage_id': src_node['template_name'], 'description': '', 
        'properties': {}}
    convert_props(src_node, localstorage_node)
    return localstorage_node


def convert_vdu_node(src_node, src_node_list, src_json_model):
    vdu_node = {'vdu_id': src_node['template_name'], 'description': '', 'properties': {},
        'image_file': '', 'local_storages': [], 'dependencies': [], 'nfv_compute': {},
        'vls': [], 'artifacts': []}
    convert_props(src_node, vdu_node)

    for relation in src_node['relationships']:
        r_id, r_name = safe_get(relation, 'target_node_id'), safe_get(relation, 'name')
        if r_name == 'guest_os':
            vdu_node['image_file'] = find_node_name(r_id, src_node_list)
        elif r_name == 'local_storage':
            vdu_node['local_storages'].append(find_node_name(r_id, src_node_list))
        elif r_name.endswith('.AttachesTo'):
            nt = find_node_type(r_id, src_node_list)
            if nt.endswith('.BlockStorage.Local') or nt.endswith('.LocalStorage'):
                vdu_node['local_storages'].append(find_node_name(r_id, src_node_list))

    for capability in src_node['capabilities']:
        if capability['name'] != 'nfv_compute':
            continue
        for prop_name, prop_info in capability['properties'].items():
            if 'value' in prop_info:
                vdu_node['nfv_compute'][prop_name] = prop_info['value']

    vdu_node['cps'] = find_related_node(src_node['id'], src_json_model, 'virtualbinding')

    for cp_node in vdu_node['cps']:
        for src_cp_node in src_node_list:
            if src_cp_node['template_name'] != cp_node:
                continue
            for relation in safe_get(src_cp_node, 'relationships'):
                if relation['name'] != 'virtualLink':
                    continue
                vl_node_name = find_node_name(relation['target_node_id'], src_node_list)
                if vl_node_name not in vdu_node['vls']:
                    vdu_node['vls'].append(vl_node_name)

    for item in safe_get(src_node, 'artifacts'):
        artifact = {'artifact_name': item['name'], 'type': item['type_name'], 
            'file': item['source_path']}
        vdu_node['artifacts'].append(artifact)

    return vdu_node


def convert_exposed_node(src_json, src_nodes, exposed):
    for item in safe_get(safe_get(src_json, 'substitution'), 'requirements'):
        exposed['external_cps'].append({'key_name': item['mapped_name'],
            "cp_id": find_node_name(item['node_id'], src_nodes)})
    for item in safe_get(safe_get(src_json, 'substitution'), 'capabilities'):
        exposed['forward_cps'].append({'key_name': item['mapped_name'],
            "cp_id": find_node_name(item['node_id'], src_nodes)})


def convert_vnffgs(src_json_inst, src_nodes):
    vnffgs = []
    src_groups = safe_get(src_json_inst, 'groups')
    for group in src_groups:
        type_name = group['type_name'].upper()
        if type_name.find('.VNFFG.') >= 0 or type_name.endswith('.VNFFG'):
            vnffgs.append(convert_vnffg_group(group, src_groups, src_nodes))
    return vnffgs


def convert_common(src_json, target_json):
    if isinstance(src_json, (unicode, str)):
        src_json_dict = json.loads(src_json)
    else:
        src_json_dict = src_json
    src_json_inst = src_json_dict["instance"]
    src_json_model = src_json_dict["model"]

    target_json['metadata'] = convert_metadata(src_json_inst)
    target_json['inputs'] = convert_inputs(src_json_inst)
    target_json['vls'] = []
    target_json['cps'] = []
    target_json['routers'] = []

    return src_json_inst, src_json_model


def convert_nsd_model(src_json):
    target_json = {'vnfs': [], 'pnfs': [], 'fps': []}
    src_json_inst, src_json_model = convert_common(src_json, target_json)
   
    src_nodes = src_json_inst['nodes']
    for node in src_nodes:
        type_name = node['type_name']
        if type_name.find('.VNF.') > 0 or type_name.endswith('.VNF'):
            target_json['vnfs'].append(convert_vnf_node(node, src_json_model))
        elif type_name.find('.PNF.') > 0 or type_name.endswith('.PNF'):
            target_json['pnfs'].append(convert_pnf_node(node, src_json_model))
        elif type_name.find('.VL.') > 0 or type_name.endswith('.VL') \
                or node['type_name'].find('.RouteExternalVL') > 0:
            target_json['vls'].append(convert_vl_node(node, src_nodes))
        elif type_name.find('.CP.') > 0 or type_name.endswith('.CP'):
            target_json['cps'].append(convert_cp_node(node, src_nodes))
        elif type_name.find('.FP.') > 0 or type_name.endswith('.FP'):
            target_json['fps'].append(convert_fp_node(node, src_nodes))
        elif type_name.endswith('.Router'):
            target_json['routers'].append(convert_router_node(node, src_nodes))

    target_json['vnffgs'] = convert_vnffgs(src_json_inst, src_nodes)

    target_json['ns_exposed'] = {'external_cps': [], 'forward_cps': []}
    convert_exposed_node(src_json_inst, src_nodes, target_json['ns_exposed'])
    return json.dumps(target_json)


def convert_vnfd_model(src_json):
    target_json = {'image_files': [], 'local_storages': [], 'vdus': []}
    src_json_inst, src_json_model = convert_common(src_json, target_json)

    src_nodes = src_json_inst['nodes']
    for node in src_nodes:
        type_name = node['type_name']
        if type_name.endswith('.ImageFile'):
            target_json['image_files'].append(convert_imagefile_node(node, src_nodes))
        elif type_name.endswith('.BlockStorage.Local') or type_name.endswith('.LocalStorage'):
            target_json['local_storages'].append(convert_localstorage_node(node, src_nodes))
        elif type_name.find('.VDU.') > 0 or type_name.endswith('.VDU'):
            target_json['vdus'].append(convert_vdu_node(node, src_nodes, src_json_model))
        elif type_name.find('.VL.') > 0 or type_name.endswith('.VL') \
                or node['type_name'].find('.RouteExternalVL') > 0:
            target_json['vls'].append(convert_vl_node(node, src_nodes))
        elif type_name.find('.CP.') > 0 or type_name.endswith('.CP'):
            target_json['cps'].append(convert_cp_node(node, src_nodes, 'VNFD'))
        elif type_name.endswith('.Router'):
            target_json['routers'].append(convert_router_node(node, src_nodes))
    
    target_json['vnf_exposed'] = {'external_cps': [], 'forward_cps': []}
    convert_exposed_node(src_json_inst, src_nodes, target_json['vnf_exposed'])
    return json.dumps(target_json)






