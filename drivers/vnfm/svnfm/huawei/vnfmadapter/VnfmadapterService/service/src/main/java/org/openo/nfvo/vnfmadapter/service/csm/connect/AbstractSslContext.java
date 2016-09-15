/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.nfvo.vnfmadapter.service.csm.connect;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SSL context
 * .</br>
 *
 * @author
 * @version     NFVO 0.5  Sep 14, 2016
 */
public class AbstractSslContext {

    protected AbstractSslContext(){
        //constructor
    }

    private static SSLContext getSSLContext() throws NoSuchAlgorithmException {
        return SSLContext.getInstance("TLSv1.2");
    }

    protected static SSLContext getAnonymousSSLContext() throws GeneralSecurityException {
        SSLContext sslContext = getSSLContext();
        sslContext.init(null, new TrustManager[] {new TrustAnyTrustManager()}, new SecureRandom());
        return sslContext;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            //NOSONAR
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            //NOSONAR
        }
    }
}
