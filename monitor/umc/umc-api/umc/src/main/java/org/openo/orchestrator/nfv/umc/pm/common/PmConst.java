package org.openo.orchestrator.nfv.umc.pm.common;

public class PmConst {
	public static final String VDU = "nfv.vdu.linux";
	public static final String HOST = "nfv.host.linux";
	public static final String VDU_TYPE = "VDU";
	public static final String HOST_TYPE = "HOST";
	public static final String MOC = "MOC";
	public static final String RESID = "RESID";
	public static final String oid = "oid";
	public static final String moc = "moc";
	public static final String data = "data";
	public static final String deleteIds = "deleteIds";
	public static final String operationType = "operationType";
	public static final String resourceType = "resourceType";
	public static final String IPADDRESS = "IPADDRESS";
	public static final String ipAddress = "ipAddress";
	public static final String customPara = "customPara";
	public static final String CUSTOMPARA = "CUSTOMPARA";
    public static final String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
    public static final String TASK_ACTIVE = "0";
    public static final String TASK_INACTIVE = "1";
    public static final String TASK_ALLVERSION = "-1";
    public static final String MOTYPE = "MOCID";
    public static final String MONAME = "PONAME";
    public static final String PMTYPE = "POID";
    public static final String JOBID = "JOBID";
    public static final String BEGINTIME = "BEGINTIME";
    public static final String NETYPEID = "NETYPEID";
    public static final String GRANULARITY = "GRANULARITY";
    public static final String TASKSTATUS = "TASKSTATUS";
    public static final String PONAME = "POATTRIBUTECLMNNAME";
    public static final String EXTENSIONTYPE = "EXTENSIONTYPE";
    public static final String RESERVATIONA = "RESERVATIONA";
    public static final String RESERVATIONB = "RESERVATIONB";
    public static final String RESERVATIONC = "RESERVATIONC";
    public static final String PROXY = "PROXY";
    public static final String OID = "OID";
    public static final String NAME = "name";
    public static final String ORIGIN ="origin";
    public static final String pmType = "pmType";
    public static final String pmDataList = "pmDataList";
    public static final String CONSUMERID = "pm.data";
    
    public static final String PM_TASK = "PM_TASK";
    public static final String PM_TASK_THRESHOLD = "PM_TASK_THRESHOLD";
    public static final String MONITOR_INFO = "MONITOR_INFO";

    public static final String PROXYIP = "PROXYIP";
    public static final String VERSION = "VERSION";
    public static final String LOCAL_IPADDRESS = "127.0.0.1";
    /**
     * For the isindex field corresponding PM nepo PM_NEPO_ATTRIBUTEDEF_TABLE table
     * represents the counter is the only field, to distinguish the measured object ID
     */
    public static final String RESERVATIONA_KEY = "1";
    
    /**
     * In reporting the counter the Po performance data, real time data, 
     * if not the counter values reported to the counter fill a value of 0.
     * This value is mainly used to monitor the PO counter.
     * for example: PO=88834, have counter C1，C2，C3，C4，C5,
     * and the define value of C1 Reserved field A is 2。
     */
    public static final String RESERVATIONA_PATCH_ZERO = "2";
    
    /**
     * 2--no instances of the type of resource (Said network element itself component resources)
     */
    public static final String MOTYPEFLAG_NO_PO = "2";
    
    public static final int NEID_INDEX = 3;
    public static final int PMDATA_INDEX = 4;
    public static final int ALARM_DIRECT_DOWN = 1;
    public static final int ALARM_DIRECT_UP = 0;
    public static final int ALARM_RESTORE = 0;
    
	// Definition of the type of alarm event reporting
	public static final byte EVENTTYPE_ALARM = 1;
	
	// Definition of the type of alarm recovery event reporting
	public static final byte EVENTTYPE_RESTORE = 2;
	
	// Definition of notification type
	public static final byte EVENTTYPE_NOTICE = 3;
	public static final int STATUS_FAIL = 1;
	public static final int JOBID_ERROR = -1;
	public static final String PMTASKTYPE = "PMTASK";
	public static final String MONITORNAME = "MONITORNAME";
	public final static String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";
	
	public static final String JAVA_LANG_CLASS_INTEGER = "java.lang.Integer";
	public static final String JAVA_LANG_CLASS_DOUBLE = "java.lang.Double";
 
	public static final String JAVA_LANG_CLASS_FLOAT = "java.lang.Float";

	public static final String JAVA_LANG_CLASS_LONG = "java.lang.Long";

	public static final String JAVA_LANG_CLASS_SHORT = "java.lang.Short";

	public static final String JAVA_LANG_CLASS_STRING = "java.lang.String"; 

	public static final String JAVA_LANG_CLASS_BYTE = "java.lang.Byte";

	public static final String JAVA_LANG_CLASS_BOOLEAN = "java.lang.Boolean";

	public static final String JAVA_UTIL_CLASS_DATE = "java.util.Date";

	public static final String JAVA_SQL_CLASS_DATE = "java.sql.Date";

	public static final String JAVA_SQL_CLASS_TIME = "java.sql.Time";

	public static final String JAVA_SQL_CLASS_TIMESTAMP = "java.sql.Timestamp";

	public static final char SEPARATOR = 2;

	public static final int IT_PM_PROCERROR = 1;
	
	public static final int PM_DATA_INDEX_BEGINTIME = 0;
	public static final int PM_DATA_INDEX_ENDTIME = 1;
	public static final int PM_DATA_INDEX_GRANULARITY = 2;
	public static final int PM_DATA_INDEX_NEID =3;
	public static final int PM_DATA_INDEX_DATA =4;
}
