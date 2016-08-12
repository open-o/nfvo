package org.openo.orchestrator.nfv.umc.pm.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.common.PmOsfUtil;
import org.openo.orchestrator.nfv.umc.pm.datacollect.IPmDataConsumer;
import org.openo.orchestrator.nfv.umc.pm.db.dao.IHistoryPmDataPo;
import org.openo.orchestrator.nfv.umc.pm.db.process.PmDBProcess;
import org.openo.orchestrator.nfv.umc.util.ExtensionAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * save pm data to db
 * @author 10081541
 *
 */
public class PmDataToDB implements IPmDataConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmDataToDB.class);
	
	@Override
	public void consume(Map<String, Object> attrList) {
		// save data to database
		save(attrList);
		
		// put in the queue for threshold calculation
		PmOsfUtil.putPmDataToThresholdQueue(attrList);

	}
	@SuppressWarnings("unchecked")
    private void save(Map<String, Object> attrList) {
		String pmType = (String)attrList.get(PmConst.pmType);
		String tableName = CacheUtil.getDataTableNameByPMType(pmType);

		List<List<Object>> pmDataList = (List<List<Object>>)attrList.get(PmConst.pmDataList);
		List<IHistoryPmDataPo> pmDataPoList = new ArrayList<>();
		for (List<Object> data : pmDataList) {
            Map<String, Object> columnName2ValueMap = buildPoColumnName2ValueMap(data);
            IHistoryPmDataPo pmDataPo = (IHistoryPmDataPo) ExtensionAccess.getExtension(IHistoryPmDataPo.class.getName(), tableName);
            pmDataPo.initValue(columnName2ValueMap);
            
            pmDataPoList.add(pmDataPo);
		}
		
		if (pmDataPoList.size() > 0) {
	        try {
	            PmDBProcess.save(tableName, pmDataPoList);
	        } catch (PmException e) {
	            LOGGER.info("PmDBProcess.insert failed.", e);
	        }
		}
	}
	
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildPoColumnName2ValueMap(List<Object> data) {
        Map<String, Object> columnName2ValueMap = new HashMap<>();
        
        columnName2ValueMap.put("BEGINTIME", PmOsfUtil.parseDate((String) data.get(PmConst.PM_DATA_INDEX_BEGINTIME), 
        		PmConst.DATE_PATTERN).getTime());
        columnName2ValueMap.put("ENDTIME", PmOsfUtil.parseDate((String) data.get(PmConst.PM_DATA_INDEX_ENDTIME), 
        		PmConst.DATE_PATTERN).getTime());
        columnName2ValueMap.put("GRANULARITY", data.get(PmConst.PM_DATA_INDEX_GRANULARITY));
        columnName2ValueMap.put("NEDN", data.get(PmConst.PM_DATA_INDEX_NEID));
        columnName2ValueMap.putAll((Map<String, Object>)data.get(PmConst.PM_DATA_INDEX_DATA));
        return columnName2ValueMap;
    }
    


}
