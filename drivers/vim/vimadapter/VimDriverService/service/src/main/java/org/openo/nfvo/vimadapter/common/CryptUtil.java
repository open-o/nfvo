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

package org.openo.nfvo.vimadapter.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openo.baseservice.encrypt.cbb.CipherCreator;
import org.openo.baseservice.encrypt.cbb.inf.AbstractCipher;

public final class CryptUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CryptUtil.class);
    

    private CryptUtil() {

    }

    public static String enCrypt(String plain) {
        LOG.debug("enCrypt pwd", plain);
        AbstractCipher abstractCipher = CipherCreator.instance().create();
        return abstractCipher.encrypt(plain);        
    }

    public static String deCrypt(String pwd) {
        AbstractCipher abstractCipher = CipherCreator.instance().create();
        return abstractCipher.decrypt(pwd);  
    }
}
