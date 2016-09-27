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


from threading import Thread

from lcm.ns.sfcs.create_flowcla import CreateFlowClassifier
from lcm.ns.sfcs.create_port_chain import CreatePortChain
from lcm.ns.sfcs.create_portpairgp import CreatePortPairGroup
from lcm.ns.sfcs.sfc_instance import SfcInstance
from lcm.pub.utils.jobutil import JobUtil


class CreateSfcWorker(Thread):
    def __init__(self, data):
        super(CreateSfcWorker, self).__init__()
        self.ns_inst_id = data["nsinstid"]
        self.ns_model_data = data["ns_model_data"]
        self.fp_id = data["fpindex"]
        self.sdnControllerId = data["sdncontrollerid"]
        self.data = data

    def init_data(self):
        self.job_id = JobUtil.create_job("SFC", "sfc_init", self.ns_inst_id + "_" + self.fp_id)
        return self.job_id

    def run(self):
        rsp = SfcInstance(self.data).do_biz()
        self.data.append({"fpinstid": rsp.data["fpinstid"]})
        CreateFlowClassifier(self.data).do_biz()
        CreatePortPairGroup(self.data).do_biz()
        CreatePortChain(self.data).do_biz()
