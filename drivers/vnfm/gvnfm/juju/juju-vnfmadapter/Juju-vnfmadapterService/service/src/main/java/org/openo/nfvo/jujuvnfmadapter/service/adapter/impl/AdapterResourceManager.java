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

package org.openo.nfvo.jujuvnfmadapter.service.adapter.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.openo.nfvo.jujuvnfmadapter.common.servicetoken.JujuVnfmRestfulUtil;
import org.openo.nfvo.jujuvnfmadapter.service.adapter.inf.IResourceManager;
import org.openo.nfvo.jujuvnfmadapter.service.constant.Constant;
import org.openo.nfvo.jujuvnfmadapter.service.constant.UrlConstant;
import org.openo.nfvo.jujuvnfmadapter.common.DownloadCsarManager;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.impl.SystemEnvVariablesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * 
 * Adapter resource manager class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     NFVO 0.5  Sep 12, 2016
 */
public class AdapterResourceManager implements IResourceManager {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterResourceManager.class);


    @Override
    public JSONObject getJujuVnfmInfo(Map<String, String> paramsMap) {
        JSONObject resultObj = new JSONObject();
        //verify url,reserve
        
        RestfulResponse rsp = JujuVnfmRestfulUtil.getRemoteResponse(paramsMap,"");
        if(null == rsp) {
            LOG.error("function=getJujuVnfmInfo,  RestfulResponse is null");
            resultObj.put(Constant.REASON, "RestfulResponse is null.");
            resultObj.put(Constant.RETURN_CODE, Constant.ERROR_STATUS_CODE);
            return resultObj;
        }
        String resultCreate = rsp.getResponseContent();

        if(rsp.getStatus() == Constant.HTTP_OK) {
            LOG.warn("function=getJujuVnfmInfo, msg= status={}, result={}.", rsp.getStatus(), resultCreate);
            resultObj = JSONObject.fromObject(resultCreate);
            resultObj.put(Constant.RETURN_CODE, Constant.HTTP_OK);
            return resultObj;
        } else {
            LOG.error("function=getJujuVnfmInfo, msg=ESR return fail,status={}, result={}.", rsp.getStatus(),
                    resultCreate);
            resultObj.put(Constant.REASON, "ESR return fail.");
        }
        resultObj.put(Constant.RETURN_CODE, Constant.ERROR_STATUS_CODE);
        return resultObj;
    }

    @Override
    public JSONObject getVnfdInfo(String csarId) {
        JSONObject resultObj = new JSONObject();
        JSONObject csarObj = new JSONObject();
        JSONObject csarPkgInfoObj = new JSONObject();
        
        if(null == csarId || "".equals(csarId)) {
            resultObj.put("reason", "csarId is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        Map paramsMap = new HashMap();

        paramsMap.put("url", String.format(UrlConstant.REST_CSARINFO_GET, csarId));
        paramsMap.put("methodType", Constant.GET);
        
        RestfulResponse rsp = JujuVnfmRestfulUtil.getRemoteResponse(paramsMap,"");
        if(null == rsp) {
            LOG.error("function=getVnfdInfo,  RestfulResponse is null");
            resultObj.put(Constant.REASON, "RestfulResponse is null.");
            resultObj.put(Constant.RETURN_CODE, Constant.ERROR_STATUS_CODE);
            return resultObj;
        }
        String resultCreate = rsp.getResponseContent();

        if(rsp.getStatus() != Constant.HTTP_OK) {
            LOG.error("function=getVnfdInfo, msg=catalog return fail,status={}, result={}.", rsp.getStatus(),
                    resultCreate);
            resultObj.put(Constant.REASON, "catalog return fail.");
            resultObj.put(Constant.RETURN_CODE, rsp.getStatus());
            return resultObj;
        } 
        csarObj = JSONObject.fromObject(resultCreate);
        String downloadUri = csarObj.getString("downloadUri");
        String csarPkgInfo;
		try {
			csarPkgInfo = readCsarPkgInfo();
	        csarPkgInfoObj = JSONObject.fromObject(csarPkgInfo); //NOSONAR
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        String csarfilepath = csarPkgInfoObj.getString("csar_file_path");
        String csarfilename = csarPkgInfoObj.getString("csar_file_name");

        // download csar package and save in location.
        JSONObject downloadObject = downloadCsar(downloadUri, csarfilepath+ System.getProperty("file.separator") +csarfilename);

        if (Integer.valueOf(downloadObject.get("retCode").toString()) != Constant.REST_SUCCESS) {
        	LOG.error("download CSAR fail.", downloadObject.get("retCode"));
            resultObj.put("reason", downloadObject.get("reason").toString());
            resultObj.put("retCode", downloadObject.get("retCode").toString());
            return resultObj;
        }
    	LOG.info("download CSAR successful.", downloadObject.get("retCode"));

        // unzip csar package to location.
        JSONObject unzipObject = unzipCSAR(csarfilepath+ System.getProperty("file.separator") +csarfilename, csarfilepath);

        if (Integer.valueOf(unzipObject.get("retCode").toString()) != Constant.REST_SUCCESS) {
        	LOG.error("unzip CSAR fail.", unzipObject.get("retCode"));
            resultObj.put("reason", unzipObject.get("reason").toString());
            resultObj.put("retCode", unzipObject.get("retCode").toString());
            return resultObj;
        }
    	LOG.info("unzip CSAR successful.", unzipObject.get("retCode"));

        resultObj.put(Constant.RETURN_CODE, Constant.HTTP_OK);
        resultObj.put("csarFilePath", csarfilepath);

        return resultObj;
    }
    
    /**
     * Get csar package information.<br>
     *
     * @return
     * @throws IOException
     * @since  NFVO 0.5
     */
    public static String readCsarPkgInfo() throws IOException {
        InputStream ins = null;
        BufferedInputStream bins = null;
        String fileContent = "";

        String fileName = SystemEnvVariablesFactory.getInstance().getAppRoot() + System.getProperty("file.separator")
                + "etc" + System.getProperty("file.separator") + "csarInfo" + System.getProperty("file.separator")
                + Constant.CSARINFO;

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
    
    /**
     * download CSAR.<br>
     * @return
     * @throws IOException
     * @since  NFVO 0.5
     */    
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
    
    /*
     * unzip CSAR packge
     * @param fileName filePath
     * @return     
     */
    public JSONObject unzipCSAR(String fileName,String filePath) {
        JSONObject resultObj = new JSONObject();

        if(fileName == null || "".equals(fileName)) {
            resultObj.put("reason", "fileName is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }
        if(filePath == null || "".equals(filePath)) {
            resultObj.put("reason", "unzipCSAR filePath is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        int status = DownloadCsarManager.unzipCSAR(fileName, filePath);

        if (Constant.UNZIP_SUCCESS == status) {
            resultObj.put("reason", "unzip csar file successfully.");
            resultObj.put("retCode", Constant.REST_SUCCESS);
        } else {
            resultObj.put("reason", "unzip csar file failed.");
            resultObj.put("retCode", Constant.REST_FAIL);
        }
        return resultObj;
    }

}
