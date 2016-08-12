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
