package org.openo.orchestrator.nfv.umc.pm.wrapper;

import org.openo.orchestrator.nfv.umc.i18n.I18n;
import org.openo.orchestrator.nfv.umc.pm.bean.GranularityBean;

public class GranularityServiceWrapper {
	public static GranularityBean[] getGranularitys(String resourceTypeId, String moTypeId, String language) {
	    I18n i18n = I18n.getInstance(language);
        return new GranularityBean[]{new GranularityBean(i18n.translate("i18n.pm.common.word.5minute"), 300)};
	}
}
