package org.openo.orchestrator.nfv.umc.fm.wrapper;

import java.util.List;

import org.openo.orchestrator.nfv.umc.fm.cache.FmCacheProcess;
import org.openo.orchestrator.nfv.umc.fm.db.entity.AlarmType;
import org.openo.orchestrator.nfv.umc.fm.db.process.FmDBProcess;
import org.openo.orchestrator.nfv.umc.fm.resource.bean.request.AlarmTypeQueryRequest;

public class AlarmTypeServiceWrapper {

    public AlarmType getAlarmType(int typeid, String language) {
        AlarmType alarmType = FmCacheProcess.queryAlarmType(typeid);
        alarmType.translate(language);
        return alarmType;
    }

    public AlarmType[] getAlarmTypes(AlarmTypeQueryRequest req, String language) {
        List<AlarmType> alarmType = FmCacheProcess.queryAlarmTypes(req.getTypeid());
        for(int i=0;i<alarmType.size();i++){
            alarmType.get(i).translate(language);
        }
        return alarmType.toArray(new AlarmType[alarmType.size()]);
    }

}
