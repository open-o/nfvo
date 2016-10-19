/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
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
package org.openo.nfvo.monitor.umc.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public class APIHttpClient {
	private static final Logger logger = LoggerFactory.getLogger(APIHttpClient.class);

	public static String doGet(String url, String queryString, String charset, String token) {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		GetMethod getMethod = new GetMethod(url);
		if (!Global.isEmpty(queryString)) {
			getMethod.setQueryString(queryString);
		}
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);

		if (!Global.isEmpty(token)) {
			getMethod.addRequestHeader("X-Auth-Token", token);
		}
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		String response = "";
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("request error: " + getMethod.getStatusLine());
			}else{
				byte[] responseBody = getMethod.getResponseBody(); 
				response = new String(responseBody, charset);
				logger.debug("----------response:"+ response);
			}

		} catch (HttpException e) {
			logger.error("Exception",e);
		} catch (IOException e) {
			logger.error("Exception",e);
		} finally {
			getMethod.releaseConnection();
		}
		return response;
	}

	@SuppressWarnings({ "resource" })
	public static String doPost2Str(String url, JSONObject json, String token) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String response = null;
		try {
			if (null != json) {
				StringEntity s = new StringEntity(json.toString());
				s.setContentEncoding("UTF-8");
				s.setContentType("application/json"); // set contentType
				post.setEntity(s);
			}
			if (!Global.isEmpty(token)) {
				post.addHeader("X-Auth-Token", token);
			}
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK
					|| res.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
				String result = EntityUtils.toString(res.getEntity());
				if (!Global.isEmpty(result)) {
					response = result;
				} else {
					response = null;
				}
			}
		} catch (Exception e) {
			logger.error("Exception",e);
		}
		return response;
	}

	@SuppressWarnings({ "resource" })
	public static JSONObject doPost(String url, JSONObject json, String token) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			if (null != json) {
				StringEntity s = new StringEntity(json.toString());
				s.setContentEncoding("UTF-8");
				s.setContentType("application/json");
				post.setEntity(s);
			}
			if (!Global.isEmpty(token)) {
				post.addHeader("X-Auth-Token", token);
			}
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK
					|| res.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
				String result = EntityUtils.toString(res.getEntity());
				logger.info("post result is :{}",result);
				if (!Global.isEmpty(result)) {
					response = JSONObject.fromObject(result);
				} else {
					response = null;
				}
			}
		} catch (Exception e) {
			logger.error("Exception",e);
		}
		return response;
	}

	public static void doDelete(String urls, String token) {
		URL url = null;
		try {
			url = new URL(urls);
		} catch (MalformedURLException exception) {
			exception.printStackTrace();
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			if (!Global.isEmpty(token)) {
				httpURLConnection.setRequestProperty("X-Auth-Token", token);
			}
			httpURLConnection.setRequestMethod("DELETE");
			logger.info("#####====" + httpURLConnection.getResponseCode());
		} catch (IOException e) {
			logger.error("Exception",e);
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}

	public static String doPut(String uri, String jsonObj, String token) {
		String resStr = null;
		HttpClient htpClient = new HttpClient();
		PutMethod putMethod = new PutMethod(uri);
		putMethod.addRequestHeader("Content-Type", "application/json");
		putMethod.addRequestHeader("X-Auth-Token", token);
		putMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		putMethod.setRequestBody(jsonObj);
		try {
			int statusCode = htpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			byte[] responseBody = putMethod.getResponseBody();
			resStr = new String(responseBody, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			putMethod.releaseConnection();
		}
		return resStr;
	}
	
	public static String doPut(String uri, JSONObject jsonObj, String token) {
		String resStr = null;
		String requestBody = jsonObj.toString();
		HttpClient htpClient = new HttpClient();
		PutMethod putMethod = new PutMethod(uri);
		putMethod.addRequestHeader("Content-Type", "application/json");
		putMethod.addRequestHeader("X-Auth-Token", token);
		putMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		putMethod.setRequestBody(requestBody);
		try {
			int statusCode = htpClient.executeMethod(putMethod);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			byte[] responseBody = putMethod.getResponseBody();
			resStr = new String(responseBody, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			putMethod.releaseConnection();
		}
		return resStr;
	}
}