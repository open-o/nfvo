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

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.dao.inf.VimDao;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class ValidateVim {

	private static final Logger LOG = LoggerFactory.getLogger(ValidateVim.class);

	private ValidateVim() {
		// constructor
	}

	public static boolean validLen(String url) {
		return url.length() < Constant.MIN_URL_LENGTH || url.length() > Constant.MAX_URL_LENGTH;
	}

	public static boolean checkUrl(VimOpResult result, Vim vim) {
		String type = vim.getType();
		String url = vim.getUrl();

		if (ValidateVim.validLen(url)) {
			LOG.error("function=checkVimUrl, msg=The url is unvalidate, url=" + url);
			result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
			result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.format"));
			return false;
		}
		if (Constant.OPENSTACK.equals(type)) {
			String regex = "http[s]?://(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}:[0-9]{2,5}";
			Pattern pattern = Pattern.compile(regex);
			Matcher mat = pattern.matcher(url);
			if (!mat.matches()) {
				result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
				result.setErrorMessage(
						ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.format"));
				return false;
			}
		}

		return true;
	}

	public static boolean validSuccessStatus(int retCode) {
		return retCode == Constant.HTTP_OK_STATUS_CODE || retCode == Constant.HTTP_CREATED_STATUS_CODE;
	}

	public static boolean validateVimData(VimOpResult result, Vim vim, VimDao vimDao) {
		String name = vim.getName();
		String type = vim.getType();
		List<Vim> vims;
		try {
			vims = Collections.unmodifiableList(vimDao.indexVims(0, 0));
		} catch (ServiceException e) {
			LOG.error("function=validateVimData, msg=serviceException occurs, e = {}.", e);
			return false;
		}
		if (!Constant.VIMTYPELIST.contains(type)) {
			LOG.warn("function=validateVimData.msg=The type is unvalidate, type:" + type);
			result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
			result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.type"));
			return false;
		}
		if (!checkVimName(name, vims, result)) {
			LOG.warn("function=validateVimData.msg=The name is unvalidate, name:" + name);
			return false;
		}
		String url = vim.getUrl();
		String extraInfo = vim.getExtraInfo();

		if (!checkVimUrl(url, type, extraInfo, vims, result)) {
			LOG.warn("function=validateVimData.msg=The url is unvalidate, url:" + url);
			return false;
		}

		JSONObject extraInfoJsonObject = JSONObject.fromObject(extraInfo);
		String authenticMode = extraInfoJsonObject.getString("authenticMode");

		if (!Constant.AUTHLIST.contains(authenticMode)) {
			LOG.error("function=validateVimData, msg=The extraInfo is invalid");
			result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
			result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.extraInfo"));
			return false;
		}

		return true;
	}

	private static boolean checkVimName(String name, List<Vim> vims, VimOpResult result) {
		Pattern p = Pattern.compile("[a-z0-9A-Z_]+");
		Matcher m = p.matcher(name);
		if (!m.matches()) {
			LOG.error("function=checkVimName, msg=The name contains special character , name=" + name);
			result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
			result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.name"));
			return false;
		}
		if (name.length() < Constant.MIN_VIM_NAME_LENGTH || name.length() > Constant.MAX_VIM_NAME_LENGTH) {
			LOG.error("function=checkVimName, msg=The name is unvalidate, name=" + name);
			result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
			result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.name"));
			return false;
		}
		for (Vim vim : vims) {
			if (name.equals(vim.getName())) {
				LOG.error("function=checkVimName, msg=The name has exist, name=" + name);
				result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
				result.setErrorMessage(
						ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.name.exist"));
				return false;
			}
		}
		return true;
	}

	public static boolean checkVimUpdateName(String name, String vimId, VimDao vimDao) {
		if (name.length() < Constant.MIN_VIM_NAME_LENGTH || name.length() > Constant.MAX_VIM_NAME_LENGTH) {
			LOG.error("function=checkVimUpdateName, msg=The name is unvalidate, name=" + name);
			return false;
		}
		int countUpdateName = vimDao.getVimByUpdateName(name, vimId);
		if (countUpdateName != 0) {
			LOG.error("function=checkVimUpdateName, msg=The name has been used, name=" + name);
			return false;
		}
		return true;
	}

	private static boolean checkVimUrl(String url, String type, String extraInfo, List<Vim> vims, VimOpResult result) {
		JSONObject extraInfoJsonObject = JSONObject.fromObject(extraInfo);

		if (validDomin(url, extraInfoJsonObject)) {
			result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
			result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.extraInfo"));
			return false;
		}

		for (Vim vim : vims) {
			try {
				if (StringUtil.isSameUrlIgnorePort(url, vim.getUrl(), type, vim.getType())) {
					LOG.error("function=checkVimUrl, msg=The url has exist, param url is {}", url);
					result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
					result.setErrorMessage(
							ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.exist"));
					return false;
				}
			} catch (MalformedURLException e) {
				LOG.error("function=checkVimUrl, msg=param or url from db isn't legal format. e= {}", e);
				result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
				result.setErrorMessage(
						ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.format"));
				return false;
			}
		}
		return true;
	}

	private static boolean validDomin(String type, JSONObject extraInfoJsonObject) {
		return (Constant.OPENSTACK.equals(type))
				&& (!extraInfoJsonObject.containsKey("domain") || !extraInfoJsonObject.containsKey("authenticMode"));
	}

	public static boolean validateUpdateData(Vim argVim, VimOpResult result, VimDao vimDao) {
		boolean isEnableName = true;
		boolean isEnableUserName = true;
		if (null != argVim && null != argVim.getName()) {
			Pattern p = Pattern.compile("[a-z0-9A-Z_]+");
			Matcher m = p.matcher(argVim.getName());
			if (!m.matches()) {
				result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
				result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.name"));
				return false;
			}
			isEnableName = ValidateVim.checkVimUpdateName(argVim.getName(), argVim.getId(), vimDao);
		}
		if (null != argVim && null != argVim.getUserName()) {
			Pattern p = Pattern.compile("[a-z0-9A-Z_]+");
			Matcher m = p.matcher(argVim.getUserName());
			isEnableUserName = m.matches();
			if (!m.matches()) {
				result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
				result.setErrorMessage(
						ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.error.username"));
			}
		}
		boolean isPass = isEnableName && isEnableUserName;

		if (isPass) {
			LOG.info("function=validateUpdateData, msg=update validate is pass");
		} else {
			LOG.error("function=validateUpdateData, msg=update validate failed");
		}
		return isPass;
	}

}
