/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eclipsesource.jaxrs.consumer.internal;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class TLSUtil {

  public static void initializeUntrustedContext() {
    X509TrustManager tm = new X509TrustManager() {

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      @Override
      public void checkServerTrusted( X509Certificate[] paramArrayOfX509Certificate,
                                      String paramString ) throws CertificateException
      {
        // no content
      }

      @Override
      public void checkClientTrusted( X509Certificate[] paramArrayOfX509Certificate,
                                      String paramString ) throws CertificateException
      {
        // no content
      }
    };
    SSLContext ctx;
    try {
      ctx = SSLContext.getInstance( "TLS" );
      ctx.init( null, new TrustManager[] { tm }, null );
      SSLContext.setDefault( ctx );
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
    HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier() {

      @Override
      public boolean verify( String hostname, SSLSession session ) {
        return true;
      }
    } );
  }
}
