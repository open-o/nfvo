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

package org.openo.nfvo.vnfmadapter.service.adapter.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.impl.SystemEnvVariablesFactory;
import org.openo.nfvo.vnfmadapter.common.DownloadCsarManager;
import org.openo.nfvo.vnfmadapter.common.VnfmException;
import org.openo.nfvo.vnfmadapter.common.servicetoken.VNFRestfulUtil;
import org.openo.nfvo.vnfmadapter.service.adapter.inf.IResourceManager;
import org.openo.nfvo.vnfmadapter.service.constant.Constant;
import org.openo.nfvo.vnfmadapter.service.constant.UrlConstant;
import org.openo.nfvo.vnfmadapter.service.csm.connect.ConnectMgrVnfm;
import org.openo.nfvo.vnfmadapter.service.csm.connect.HttpRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Resource Manager adapter class.
 * .</br>
 *
 * @author
 * @version     NFVO 0.5  Sep 13, 2016
 */
public class AdapterResourceManager implements IResourceManager {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterResourceManager.class);

    @Override
    public JSONObject uploadVNFPackage(JSONObject vnfpkg, Map<String, String> paramsMap) {
        JSONObject resultObj = new JSONObject();
        try {
            // if upper layer do not provide vnfpackage info,then get the
            // vnfpackage info from JSON file.
            if (vnfpkg == null || vnfpkg.isEmpty()) {
                String vnfPkgInfo = readVfnPkgInfoFromJson();
                vnfpkg = JSONObject.fromObject(vnfPkgInfo); //NOSONAR
            }
        } catch (IOException e) {
            LOG.error("function=uploadVNFPackage", e);
        }

        // check if parameters are null.
        if (paramsMap == null || paramsMap.isEmpty()) {
            resultObj.put("reason", "csarid and vnfmid are null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        String csarid = paramsMap.get("csarid");
        String vnfmid = paramsMap.get("vnfmid");
        if(null == csarid || "".equals(csarid)) {
            resultObj.put("reason", "csarid is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }
        if (null == vnfmid || "".equals(vnfmid)) {
            resultObj.put("reason", "vnfmid is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        // obtain CSAR package info
        JSONObject csarobj = getVnfmCsarInfo(csarid);
        String downloadUri = "";
        if (Integer.valueOf(csarobj.get("retCode").toString()) == Constant.HTTP_OK) {
            downloadUri = csarobj.getString("downloadUri");
        } else {
            resultObj.put("reason", csarobj.get("reason").toString());
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        String csarfilepath = vnfpkg.getString("vnfd_file_path");

        // download csar package and save in location.
        JSONObject downloadObject = downloadCsar(downloadUri, csarfilepath);

        if (Integer.valueOf(downloadObject.get("retCode").toString()) != Constant.REST_SUCCESS) {
            resultObj.put("reason", downloadObject.get("reason").toString());
            resultObj.put("retCode", downloadObject.get("retCode").toString());
            return resultObj;
        }

        Map<String, String> vnfmMap = new HashMap<>();
        vnfmMap.put("url", String.format(UrlConstant.REST_VNFMINFO_GET, vnfmid));
        vnfmMap.put("methodType", Constant.GET);

        // get VNFM connection info
        JSONObject vnfmObject = getVnfmConnInfo(vnfmMap);
        if (Integer.valueOf(vnfmObject.get("retCode").toString()) != Constant.HTTP_OK) {
            resultObj.put("reason", vnfmObject.get("reason").toString());
            resultObj.put("retCode", vnfmObject.get("retCode").toString());
            return resultObj;
        }

        String vnfmUrl = vnfmObject.getString("url");
        String userName = vnfmObject.getString("userName");
        String password = vnfmObject.getString("password");

        // build VNFM connection and get token
        ConnectMgrVnfm mgrVcmm = new ConnectMgrVnfm();

        JSONObject connObject = new JSONObject();
        connObject.put("url", vnfmUrl);
        connObject.put("userName", userName);
        connObject.put("password", password);
        if (Constant.HTTP_OK != mgrVcmm.connect(vnfmObject)) {
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "connect fail.");
            return resultObj;
        }

        String connToken = mgrVcmm.getRoaRand();

        // get vim_id
        JSONObject cloudObject = getAllCloud(vnfmUrl);
        String vimId = "";

        if (!cloudObject.isEmpty() && cloudObject.get(Constant.RETCODE).equals(HttpStatus.SC_OK)) {
            vimId = cloudObject.getString("dn");
        } else {
            return cloudObject;
        }

        // upload VNF package
        vnfpkg.put("vim_id", vimId);

        JSONObject uploadPkgObject = upload(vnfpkg, vnfmUrl, connToken);

        String vnfdid = "";
        if (!uploadPkgObject.isEmpty() && uploadPkgObject.get(Constant.RETCODE).equals(HttpStatus.SC_CREATED)) {
            vnfdid = uploadPkgObject.getString("id");
        } else {
            return uploadPkgObject;
        }

        // get vnfd version
        String vnfdVersion = "";

        JSONObject vnfdVerObject = getVnfdVersion(vnfmUrl, String.format(UrlConstant.URL_VNFDINFO_GET, vnfdid));

        if (!vnfdVerObject.isEmpty() && vnfdVerObject.get(Constant.RETCODE).equals(HttpStatus.SC_OK)) {
            vnfdVersion = vnfdVerObject.getString("vnfd_version");
        } else {
            return vnfdVerObject;
        }

        // get vnfd plan info
        String planName = "";
        String planId = "";

        JSONObject vnfdPlanInfo = getVNFDPlanInfo(vnfmUrl, vnfdid);

        if (!vnfdPlanInfo.isEmpty() && vnfdPlanInfo.get(Constant.RETCODE).equals(HttpStatus.SC_OK)) {
            planName = vnfdPlanInfo.getString("plan_name");
            planId = vnfdPlanInfo.getString("plan_id");
        } else {
            return vnfdPlanInfo;
        }

        //return values
        resultObj.put("retCode", Constant.HTTP_OK);
        resultObj.put("vnfdId", vnfdid);
        resultObj.put("vnfdVersion", vnfdVersion);
        resultObj.put("planName", planName);
        resultObj.put("planId", planId);

        return resultObj;
    }

    private JSONObject sendRequest(Map paramsMap) {
        JSONObject resultObj = new JSONObject();
        RestfulResponse rsp = VNFRestfulUtil.getRemoteResponse(paramsMap, "");
        if (null == rsp) {
            LOG.error("function=sendRequest,  RestfulResponse is null");
            resultObj.put("reason", "RestfulResponse is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }
        String resultCreate = rsp.getResponseContent();

        if (rsp.getStatus() == Constant.HTTP_OK) {
            LOG.warn("function=sendRequest, msg= status={}, result={}.", rsp.getStatus(), resultCreate);
            resultObj = JSONObject.fromObject(resultCreate);
            resultObj.put("retCode", Constant.HTTP_OK);
            return resultObj;
        } else {
            LOG.error("function=sendRequest, msg=ESR return fail,status={}, result={}.", rsp.getStatus(),
                    resultCreate);
            resultObj.put("reason", "ESR return fail.");
        }
        resultObj.put("retCode", Constant.REST_FAIL);
        return resultObj;
    }

    @Override
    public JSONObject getVnfmCsarInfo(String csarid) {
        JSONObject resultObj = new JSONObject();

        if(null == csarid || "".equals(csarid)) {
            resultObj.put("reason", "csarid is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        Map paramsMap = new HashMap();

        paramsMap.put("url", String.format(UrlConstant.REST_CSARINFO_GET, csarid));
        paramsMap.put("methodType", Constant.GET);

        return this.sendRequest(paramsMap);
    }

    @Override
    public JSONObject downloadCsar(String url, String filePath) {
        JSONObject resultObj = new JSONObject();

        if(url == null || "".equals(url)) {
            resultObj.put("reason", "url is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }
        if(filePath == null || "".equals(filePath)) {
            resultObj.put("reason", "downloadUrl filePath is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        String status = DownloadCsarManager.download(url, filePath);

        if (Constant.DOWNLOADCSAR_SUCCESS.equals(status)) {
            resultObj.put("reason", "download csar file successfully.");
            resultObj.put("retCode", Constant.REST_SUCCESS);
        } else {
            resultObj.put("reason", "download csar file failed.");
            resultObj.put("retCode", Constant.REST_FAIL);
        }
        return resultObj;
    }

    @Override
    public JSONObject getAllCloud(String url) {
        JSONObject resultObj = new JSONObject();
        JSONArray resArray = new JSONArray();

        // get vim_id
        HttpMethod httpMethodCloud = null;
        try {
            httpMethodCloud = new HttpRequests.Builder(Constant.ANONYMOUS)
                    .setUrl(url.trim() + ":" + UrlConstant.PORT_COMMON, UrlConstant.URL_ALLCLOUD_GET).setParams("")
                    .get().execute();

            int statusCode = httpMethodCloud.getStatusCode();

            String result = httpMethodCloud.getResponseBodyAsString();

            if (statusCode == HttpStatus.SC_OK) {
                resArray = JSONArray.fromObject(result);
                resultObj = resArray.getJSONObject(0);
                resultObj.put(Constant.RETCODE, statusCode);
           } else {
                LOG.error("uploadVNFPackage get allcloud failed, code:" + statusCode + " re:" + result);
                resultObj.put(Constant.RETCODE, statusCode);
                resultObj.put("reason", "get allcloud failed. code:" + statusCode + " re:" + result);
                return resultObj;
            }
        } catch (JSONException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get allcloud JSONException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get allcloud failed and JSONException." + e.getMessage());
            return resultObj;
        } catch (VnfmException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get allcloud VnfmException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get allcloud failed and VnfmException." + e.getMessage());
            return resultObj;
        } catch (IOException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get allcloud IOException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get allcloud failed and IOException." + e.getMessage());
            return resultObj;
        }
        return resultObj;
    }

    /**
     * Upload vnfpackage<br>
     *
     * @param vnfpackage
     * @param vnfmurl
     * @param conntoken
     * @return
     * @since  NFVO 0.5
     */
    public JSONObject upload(JSONObject vnfpackage, String vnfmurl, String conntoken) {
        JSONObject resultObj = new JSONObject();
        HttpMethod httpMethodVnf = null;

        try {
            httpMethodVnf = new HttpRequests.Builder(Constant.ANONYMOUS)
                    .setUrl(vnfmurl.trim() + ":" + UrlConstant.PORT_UPLOADVNFPKG, UrlConstant.URL_VNFPACKAGE_POST)
                    .setParams(vnfpackage.toString()).addHeader(Constant.HEADER_AUTH_TOKEN, conntoken).post().execute();

            int statusCodeUp = httpMethodVnf.getStatusCode();

            String resultUp = httpMethodVnf.getResponseBodyAsString();

            if (statusCodeUp == HttpStatus.SC_CREATED) {
                resultObj = JSONObject.fromObject(resultUp);
                resultObj.put(Constant.RETCODE, statusCodeUp);
            } else {
                LOG.error("uploadVNFPackage upload VNF package failed, code:" + statusCodeUp + " re:" + resultUp);
                resultObj.put(Constant.RETCODE, statusCodeUp);
                resultObj.put("data", "upload VNF package failed, code:" + statusCodeUp + " re:" + resultUp);
                return resultObj;
            }
        } catch (JSONException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage upload VNF package JSONException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "upload VNF package failed and JSONException." + e.getMessage());
            return resultObj;
        } catch (VnfmException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage upload VNF package VnfmException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "upload VNF package failed and VnfmException." + e.getMessage());
            return resultObj;
        } catch (IOException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage upload VNF package IOException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "upload VNF package failed and IOException." + e.getMessage());
            return resultObj;
        }
        return resultObj;
    }

    /**
     * Find vnfd version.<br>
     *
     * @param prefixUrl
     * @param serviceUrl
     * @return
     * @since  NFVO 0.5
     */
    public JSONObject getVnfdVersion(String prefixUrl, String serviceUrl) {
        JSONObject resultObj = new JSONObject();
        HttpMethod httpMethodVnfd = null;
       try {
            httpMethodVnfd = new HttpRequests.Builder(Constant.ANONYMOUS)
                    .setUrl(prefixUrl.trim() + ":" + UrlConstant.PORT_COMMON, serviceUrl).setParams("").get()
                    .execute();

            int statusCodeVnfd = httpMethodVnfd.getStatusCode();

            String resultVnfd = httpMethodVnfd.getResponseBodyAsString();

            if (statusCodeVnfd == HttpStatus.SC_OK) {
                resultObj = JSONObject.fromObject(resultVnfd);
                resultObj.put(Constant.RETCODE, statusCodeVnfd);
            } else {
                LOG.error("uploadVNFPackage vnfd version failed, code:" + statusCodeVnfd + " re:" + resultVnfd);
                resultObj.put(Constant.RETCODE, statusCodeVnfd);
                resultObj.put("data", "get vnfd version failed, code:" + statusCodeVnfd + " re:" + resultVnfd);
                return resultObj;
            }
        } catch (JSONException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get vnfd version JSONException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get vnfd version failed and JSONException." + e.getMessage());
            return resultObj;
        } catch (VnfmException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get vnfd version VnfmException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get vnfd version failed and VnfmException." + e.getMessage());
            return resultObj;
        } catch (IOException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get vnfd version IOException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get vnfd version failed and IOException." + e.getMessage());
            return resultObj;
        }
        return resultObj;
    }

    /**
     *Find VNFM connection information.<br>
     *
     * @param paramsMap
     * @return
     * @since  NFVO 0.5
     */
    public JSONObject getVnfmConnInfo(Map<String, String> paramsMap) {
        return this.sendRequest(paramsMap);
    }

    @Override
    public JSONObject getVNFDPlanInfo(String url, String vnfdid) {
        JSONObject resultObj = new JSONObject();
        JSONArray resArray = new JSONArray();

        HttpMethod httpMethodPlan = null;
        try {
            httpMethodPlan = new HttpRequests.Builder(Constant.ANONYMOUS)
                    .setUrl(url.trim() + ":" + UrlConstant.PORT_COMMON, String.format(UrlConstant.URL_VNFDPLANINFO_GET,vnfdid)).setParams("")
                    .get().execute();

            int statusCode = httpMethodPlan.getStatusCode();

            String result = httpMethodPlan.getResponseBodyAsString();

            if (statusCode == HttpStatus.SC_OK) {
                resArray = JSONArray.fromObject(result);
                resultObj = resArray.getJSONObject(0);
                resultObj.put(Constant.RETCODE, statusCode);
           } else {
                LOG.error("uploadVNFPackage get VNFDPlanInfo failed, code:" + statusCode + " re:" + result);
                resultObj.put(Constant.RETCODE, statusCode);
                resultObj.put("reason", "get VNFDPlanInfo failed. code:" + statusCode + " re:" + result);
                return resultObj;
            }
        } catch (JSONException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get VNFDPlanInfo JSONException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get VNFDPlanInfo failed and JSONException." + e.getMessage());
            return resultObj;
        } catch (VnfmException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get VNFDPlanInfo VnfmException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get VNFDPlanInfo failed and VnfmException." + e.getMessage());
            return resultObj;
        } catch (IOException e) {
            LOG.error("function=uploadVNFPackage, msg=uploadVNFPackage get VNFDPlanInfo IOException e={}.", e);
            resultObj.put(Constant.RETCODE, Constant.HTTP_INNERERROR);
            resultObj.put("reason", "get VNFDPlanInfo failed and IOException." + e.getMessage());
            return resultObj;
        }
        return resultObj;
    }

    /**
     * Get VNF package information.<br>
     *
     * @return
     * @throws IOException
     * @since  NFVO 0.5
     */
    public static String readVfnPkgInfoFromJson() throws IOException {
        InputStream ins = null;
        BufferedInputStream bins = null;
        String fileContent = "";

        String fileName = SystemEnvVariablesFactory.getInstance().getAppRoot() + System.getProperty("file.separator")
                + "etc" + System.getProperty("file.separator") + "vnfpkginfo" + System.getProperty("file.separator")
                + Constant.VNFPKGINFO;

        try {
            ins = new FileInputStream(fileName);
            bins = new BufferedInputStream(ins);

            byte[] contentByte = new byte[ins.available()];
            int num = bins.read(contentByte);

            if (num > 0) {
                fileContent = new String(contentByte);
            }
        } catch (FileNotFoundException e) {
            LOG.error(fileName + "is not found!", e);
        } finally {
            if (ins != null) {
                ins.close();
            }
            if (bins != null) {
                bins.close();
            }
        }

        return fileContent;
    }

}
