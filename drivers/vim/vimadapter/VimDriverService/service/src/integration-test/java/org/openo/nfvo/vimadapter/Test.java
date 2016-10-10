/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.nfvo.vimadapter;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.vimadapter.mocoserver.VimAdapterSuccessServer;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Aug 11, 2016
 */
public class Test {

    public static void main(String[] args) {
        VimAdapterSuccessServer vimAdapterServer = new VimAdapterSuccessServer();
        try {
            vimAdapterServer.start();
            Thread.sleep(30000);
        } catch(ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
