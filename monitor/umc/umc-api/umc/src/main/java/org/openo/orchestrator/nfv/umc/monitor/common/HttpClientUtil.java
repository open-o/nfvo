/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.monitor.common;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @ClassName: HttpClientUtil
 * @Description: TODO(HttpClient Wrapper Class,realize the rest interface to access )
 * @author tanghua10186366
 * @date 2015.12.25
 *
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * @Title httpPost
     * @Description TODO(realize the rest interface to access by httpPost)
     * @param url
     * @throws Exception
     * @return int (return StatusCode,If zero error said)
     */
    public static int httpPost(String url) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            CloseableHttpResponse res = httpClient.execute(httpPost);
            try {
                return res.getStatusLine().getStatusCode();
            } finally {
                res.close();
            }
        } catch (ParseException e) {
            LOGGER.error("HttpClient throw ParseException:" + url, e);
        } catch (IOException e) {
            LOGGER.error("HttpClient throw IOException:" + url, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                LOGGER.error("HttpClient Close throw IOException", e);
            }
        }

        return 0;

    }


    /**
     * @Title httpDelete
     * @Description TODO(realize the rest interface to access by httpDelete)
     * @param url
     * @return
     * @throws Exception
     * @return int (return StatusCode,If zero error said)
     */
    public static int httpDelete(String url) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        try {
            CloseableHttpResponse res = httpClient.execute(httpDelete);
            try {
                return res.getStatusLine().getStatusCode();
            } finally {
                res.close();
            }
        } catch (ParseException e) {
            LOGGER.error("HttpClient throw ParseException:" + url, e);
        } catch (IOException e) {
            LOGGER.error("HttpClient throw IOException:" + url, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                LOGGER.error("HttpClient Close throw IOException", e);
            }
        }

        return 0;

    }



}
