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
package org.openo.orchestrator.nfv.umc.util;

import org.openo.orchestrator.nfv.umc.i18n.I18n;

public class UMCUtil {
	private static String language = "en_US";

	public static String getLanguage() {
		return language;
	}

	public static void setLanguage(String language) {
		UMCUtil.language = language;
	}
	
	public static String getI18nValue(String key)
	{
		if (key == null)
		{
			return "";
		}
		
        I18n i18n = I18n.getInstance(language);
        
        if(i18n == null)
        {
            return key;
        }
        else
        {
        	return i18n.translate(key);
    	}
	}
	
	public static I18n getI18nInstance()
	{
		return I18n.getInstance(language);
	}
}
