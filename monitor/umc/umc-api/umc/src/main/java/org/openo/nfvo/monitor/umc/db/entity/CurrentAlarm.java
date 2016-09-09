/**
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
package org.openo.nfvo.monitor.umc.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.openo.nfvo.monitor.umc.db.IEntityClass;
import org.openo.nfvo.monitor.umc.i18n.I18n;

@Entity
@Table(name = "currentAlarm")
@NamedQueries({
        @NamedQuery(name = "org.openo.nfvo.monitor.umc.fm.entity.CurrentAlarm.findAll", query = "SELECT p FROM CurrentAlarm p"),
        @NamedQuery(name = "org.openo.nfvo.monitor.umc.fm.entity.CurrentAlarm.findByCondition", query = "SELECT curAlarm FROM CurrentAlarm curAlarm ")})
public class CurrentAlarm implements Serializable, IEntityClass {

    private static final long serialVersionUID = 4581696346693379882L;

    @Id
    @Column(name = "alarmKey", length=500, nullable = false)
    private String alarmKey;

    @Column(name = "aid", nullable = true)
    private String aid;

    @Column(name = "id", nullable = false)
    private long id;

    // network element type
    @Column(name = "moc", nullable = false)
    private String moc;

    // network element oid
    // snmp：alarmManagedObjectInstance
    @Column(name = "position1", nullable = false)
    private String position1;

    @Column(name = "subPosition1", nullable = true)
    private String subPosition1;

    @Column(name = "subName1", nullable = true)
    private String subName1;

    @Column(name = "alarmSource", nullable = false)
    private String alarmSource;

    @Column(name = "position2", nullable = true)
    private String position2;

    @Column(name = "subPosition2", nullable = true)
    private String subPosition2;

    @Column(name = "link", nullable = true)
    private String link;

    @Column(name = "neIp", nullable = true)
    private String neIp;

    @Column(name = "perceivedSeverity", nullable = false)
    private int perceivedSeverity;

    @Column(name = "originalPerceivedSeverity", nullable = false)
    private int originalPerceivedSeverity;

    @Column(name = "alarmType", nullable = false)
    private int alarmType;

    @Column(name = "code", nullable = false)
    private long code;

    @Column(name = "systemType", nullable = false)
    private int systemType;

    @Column(name = "standardCause", nullable = true)
    private int standardCause;

    @Column(name = "reason", nullable = true)
    private int reason;

    @Column(name = "specificProblem", length=1000, nullable = true)
    private String specificProblem;

    @Column(name = "product1", nullable = true)
    private int product1;

    @Column(name = "product2", nullable = true)
    private int product2;

    @Column(name = "product3", nullable = true, length = 100)
    private int product3;

    @Column(name = "product4", nullable = true)
    private int product4;

    @Column(name = "product5", nullable = true)
    private int product5;

    @Column(name = "product6", nullable = true)
    private int product6;

    @Column(name = "product7", nullable = true)
    private int product7;

    @Column(name = "product8", nullable = true)
    private int product8;

    @Column(name = "product9", nullable = true)
    private int product9;

    @Column(name = "dispProduct", nullable = false)
    private String dispproduct;

    @Column(name = "alarmRaisedTime", nullable = false)
    private Date alarmRaisedTime;

    @Column(name = "alarmRaisedTime_gmt", nullable = false)
    private Date alarmRaisedTime_gmt;

    @Column(name = "timeZoneID", nullable = true)
    private String timeZoneID;

    @Column(name = "timeZoneOffset", nullable = false)
    private int timeZoneOffset;

    @Column(name = "dstsaving", nullable = true)
    private int dstsaving;

    @Column(name = "serverTime", nullable = true)
    private Date serverTime;

    @Column(name = "alarmChangedTime", nullable = true)
    private Date alarmChangedTime;

    @Column(name = "additionalText", length=1000, nullable = true)
    private String additionalText;

    @Column(name = "ackState", nullable = true)
    private int ackState;

    @Column(name = "ackUserId", nullable = true)
    private String ackUserId;

    @Column(name = "ackSystemId", nullable = true)
    private String ackSystemId;

    @Column(name = "ackTime", nullable = true)
    private Date ackTime;

    @Column(name = "ackInfo", nullable = true)
    private String ackInfo;

    @Column(name = "clearType", nullable = true)
    private int clearType;

    @Column(name = "clearUserId", nullable = true)
    private String clearUserId;

    @Column(name = "clearSystemId", nullable = true)
    private String clearSystemId;

    @Column(name = "alarmClearedTime", nullable = true)
    private Date alarmClearedTime;

    @Column(name = "alarmClearedTime_gmt", nullable = true)
    private Date alarmClearedTime_gmt;

    @Column(name = "commentText", nullable = true)
    private String commentText;

    @Column(name = "commentUserId", nullable = true)
    private String commentUserId;

    @Column(name = "commentSystemId", nullable = true)
    private String commentSystemId;

    @Column(name = "commentTime", nullable = true)
    private Date commentTime;

    @Column(name = "customAttr1", nullable = true)
    private String customAttr1;

    @Column(name = "customAttr2", nullable = true)
    private String customAttr2;

    @Column(name = "customAttr3", nullable = true)
    private String customAttr3;

    @Column(name = "customAttr4", nullable = true)
    private String customAttr4;

    @Column(name = "customAttr5", nullable = true)
    private String customAttr5;

    @Column(name = "customAttr6", nullable = true)
    private String customAttr6;

    @Column(name = "customAttr7", nullable = true)
    private String customAttr7;

    @Column(name = "customAttr8", nullable = true)
    private String customAttr8;

    @Column(name = "customAttr9", nullable = true)
    private String customAttr9;

    @Column(name = "customAttr10", nullable = true)
    private String customAttr10;

    @Column(name = "customAttr11", nullable = true)
    private String customAttr11;

    @Column(name = "customAttr12", nullable = true)
    private String customAttr12;

    @Column(name = "visible", nullable = true)
    private int visible;

    @Column(name = "admc", nullable = true)
    private int admc;

    @Column(name = "reserve1", nullable = true)
    private String reserve1;

    @Column(name = "reserve2", nullable = true)
    private String reserve2;

    @Column(name = "reserve3", nullable = true)
    private String reserve3;

    @Column(name = "reserve4", nullable = true)
    private String reserve4;

    @Column(name = "reserve5", nullable = true)
    private String reserve5;

    @Column(name = "changetrack", length=1000, nullable = true)
    private String changetrack;

    @Column(name = "dataType", nullable = false)
    private int dataType;

    @Column(name = "ommid", nullable = true)
    private String ommid;

    @Column(name = "pathid", nullable = true)
    private String pathid;

    @Column(name = "pathName", nullable = true)
    private String pathName;

    @Column(name = "alarmReportCount", nullable = true)
    private int alarmReportCount;

    @Column(name = "plmn1", nullable = true)
    private int plmn1;

    @Column(name = "plmn2", nullable = true)
    private int plmn2;

    @Column(name = "plmn3", nullable = true)
    private int plmn3;

    @Column(name = "plmn4", nullable = true)
    private int plmn4;

    @Column(name = "plmn5", nullable = true)
    private int plmn5;

    @Column(name = "plmn6", nullable = true)
    private int plmn6;

    @Column(name = "ispublic2plmn", nullable = true)
    private int ispublic2plmn;

    @Column(name = "emsid", nullable = true)
    private String emsid;

    @Column(name = "relationFlag", nullable = true)
    private int relationFlag;

    @Column(name = "isnaffilter", nullable = true)
    private int isnaffilter;

    @Column(name = "thresholdInfo", length=500, nullable = true)
    private String thresholdInfo;

    @Column(name = "interMittenceCount", nullable = true)
    private int interMittenceCount;

    @Column(name = "debugState", nullable = true)
    private int debugState;

    @Column(name = "sortInfo", nullable = true)
    private String sortInfo;

    @Column(name = "clearInfo", nullable = true)
    private String clearInfo;

    public String getAlarmKey() {
        return alarmKey;
    }

    public void setAlarmKey(String alarmKey) {
        this.alarmKey = alarmKey;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMoc() {
        return moc;
    }

    public void setMoc(String moc) {
        this.moc = moc;
    }

    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public String getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }

    public String getSubPosition1() {
        return subPosition1;
    }

    public void setSubPosition1(String subPosition1) {
        this.subPosition1 = subPosition1;
    }

    public String getSubName1() {
        return subName1;
    }

    public void setSubName1(String subName1) {
        this.subName1 = subName1;
    }

    public String getPosition2() {
        return position2;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public String getSubPosition2() {
        return subPosition2;
    }

    public void setSubPosition2(String subPosition2) {
        this.subPosition2 = subPosition2;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNeIp() {
        return neIp;
    }

    public void setNeIp(String neIp) {
        this.neIp = neIp;
    }

    public int getPerceivedSeverity() {
        return perceivedSeverity;
    }

    public int getOriginalPerceivedSeverity() {
        return originalPerceivedSeverity;
    }

    public void setOriginalPerceivedSeverity(int originalPerceivedSeverity) {
        this.originalPerceivedSeverity = originalPerceivedSeverity;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public int getSystemType() {
        return systemType;
    }

    public void setSystemType(int systemType) {
        this.systemType = systemType;
    }

    public int getStandardCause() {
        return standardCause;
    }

    public void setStandardCause(int standardCause) {
        this.standardCause = standardCause;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getSpecificProblem() {
        return specificProblem;
    }

    public void setSpecificProblem(String specificProblem) {
        this.specificProblem = specificProblem;
    }

    public int getProduct1() {
        return product1;
    }

    public void setProduct1(int product1) {
        this.product1 = product1;
    }

    public int getProduct2() {
        return product2;
    }

    public void setProduct2(int product2) {
        this.product2 = product2;
    }

    public int getProduct3() {
        return product3;
    }

    public void setProduct3(int product3) {
        this.product3 = product3;
    }

    public int getProduct4() {
        return product4;
    }

    public void setProduct4(int product4) {
        this.product4 = product4;
    }

    public int getProduct5() {
        return product5;
    }

    public void setProduct5(int product5) {
        this.product5 = product5;
    }

    public int getProduct6() {
        return product6;
    }

    public void setProduct6(int product6) {
        this.product6 = product6;
    }

    public int getProduct7() {
        return product7;
    }

    public void setProduct7(int product7) {
        this.product7 = product7;
    }

    public int getProduct8() {
        return product8;
    }

    public void setProduct8(int product8) {
        this.product8 = product8;
    }

    public int getProduct9() {
        return product9;
    }

    public void setProduct9(int product9) {
        this.product9 = product9;
    }

    public String getDispproduct() {
        return dispproduct;
    }

    public void setDispproduct(String dispproduct) {
        this.dispproduct = dispproduct;
    }

    public Date getAlarmRaisedTime() {
        return alarmRaisedTime;
    }

    public void setAlarmRaisedTime(Date alarmRaisedTime) {
        this.alarmRaisedTime = alarmRaisedTime;
    }

    public Date getAlarmRaisedTime_gmt() {
        return alarmRaisedTime_gmt;
    }

    public void setAlarmRaisedTime_gmt(Date alarmRaisedTime_gmt) {
        this.alarmRaisedTime_gmt = alarmRaisedTime_gmt;
    }

    public String getTimeZoneID() {
        return timeZoneID;
    }

    public void setTimeZoneID(String timeZoneID) {
        this.timeZoneID = timeZoneID;
    }

    public int getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(int timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public int getDstsaving() {
        return dstsaving;
    }

    public void setDstsaving(int dstsaving) {
        this.dstsaving = dstsaving;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public Date getAlarmChangedTime() {
        return alarmChangedTime;
    }

    public void setAlarmChangedTime(Date alarmChangedTime) {
        this.alarmChangedTime = alarmChangedTime;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public int getAckState() {
        return ackState;
    }

    public void setAckState(int ackState) {
        this.ackState = ackState;
    }

    public String getAckUserId() {
        return ackUserId;
    }

    public void setAckUserId(String ackUserId) {
        this.ackUserId = ackUserId;
    }

    public String getAckSystemId() {
        return ackSystemId;
    }

    public void setAckSystemId(String ackSystemId) {
        this.ackSystemId = ackSystemId;
    }

    public Date getAckTime() {
        return ackTime;
    }

    public void setAckTime(Date ackTime) {
        this.ackTime = ackTime;
    }

    public String getAckInfo() {
        return ackInfo;
    }

    public void setAckInfo(String ackInfo) {
        this.ackInfo = ackInfo;
    }

    public int getClearType() {
        return clearType;
    }

    public void setClearType(int clearType) {
        this.clearType = clearType;
    }

    public String getClearUserId() {
        return clearUserId;
    }

    public void setClearUserId(String clearUserId) {
        this.clearUserId = clearUserId;
    }

    public String getClearSystemId() {
        return clearSystemId;
    }

    public void setClearSystemId(String clearSystemId) {
        this.clearSystemId = clearSystemId;
    }

    public Date getAlarmClearedTime() {
        return alarmClearedTime;
    }

    public void setAlarmClearedTime(Date alarmClearedTime) {
        this.alarmClearedTime = alarmClearedTime;
    }

    public Date getAlarmClearedTime_gmt() {
        return alarmClearedTime_gmt;
    }

    public void setAlarmClearedTime_gmt(Date alarmClearedTime_gmt) {
        this.alarmClearedTime_gmt = alarmClearedTime_gmt;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentSystemId() {
        return commentSystemId;
    }

    public void setCommentSystemId(String commentSystemId) {
        this.commentSystemId = commentSystemId;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getCustomAttr1() {
        return customAttr1;
    }

    public void setCustomAttr1(String customAttr1) {
        this.customAttr1 = customAttr1;
    }

    public String getCustomAttr2() {
        return customAttr2;
    }

    public void setCustomAttr2(String customAttr2) {
        this.customAttr2 = customAttr2;
    }

    public String getCustomAttr3() {
        return customAttr3;
    }

    public void setCustomAttr3(String customAttr3) {
        this.customAttr3 = customAttr3;
    }

    public String getCustomAttr4() {
        return customAttr4;
    }

    public void setCustomAttr4(String customAttr4) {
        this.customAttr4 = customAttr4;
    }

    public String getCustomAttr5() {
        return customAttr5;
    }

    public void setCustomAttr5(String customAttr5) {
        this.customAttr5 = customAttr5;
    }

    public String getCustomAttr6() {
        return customAttr6;
    }

    public void setCustomAttr6(String customAttr6) {
        this.customAttr6 = customAttr6;
    }

    public String getCustomAttr7() {
        return customAttr7;
    }

    public void setCustomAttr7(String customAttr7) {
        this.customAttr7 = customAttr7;
    }

    public String getCustomAttr8() {
        return customAttr8;
    }

    public void setCustomAttr8(String customAttr8) {
        this.customAttr8 = customAttr8;
    }

    public String getCustomAttr9() {
        return customAttr9;
    }

    public void setCustomAttr9(String customAttr9) {
        this.customAttr9 = customAttr9;
    }

    public String getCustomAttr10() {
        return customAttr10;
    }

    public void setCustomAttr10(String customAttr10) {
        this.customAttr10 = customAttr10;
    }

    public String getCustomAttr11() {
        return customAttr11;
    }

    public void setCustomAttr11(String customAttr11) {
        this.customAttr11 = customAttr11;
    }

    public String getCustomAttr12() {
        return customAttr12;
    }

    public void setCustomAttr12(String customAttr12) {
        this.customAttr12 = customAttr12;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getAdmc() {
        return admc;
    }

    public void setAdmc(int admc) {
        this.admc = admc;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    public String getReserve5() {
        return reserve5;
    }

    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }

    public String getChangetrack() {
        return changetrack;
    }

    public void setChangetrack(String changetrack) {
        this.changetrack = changetrack;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getOmmid() {
        return ommid;
    }

    public void setOmmid(String ommid) {
        this.ommid = ommid;
    }

    public String getPathid() {
        return pathid;
    }

    public void setPathid(String pathid) {
        this.pathid = pathid;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public int getAlarmReportCount() {
        return alarmReportCount;
    }

    public void setAlarmReportCount(int alarmReportCount) {
        this.alarmReportCount = alarmReportCount;
    }

    public int getPlmn1() {
        return plmn1;
    }

    public void setPlmn1(int plmn1) {
        this.plmn1 = plmn1;
    }

    public int getPlmn2() {
        return plmn2;
    }

    public void setPlmn2(int plmn2) {
        this.plmn2 = plmn2;
    }

    public int getPlmn3() {
        return plmn3;
    }

    public void setPlmn3(int plmn3) {
        this.plmn3 = plmn3;
    }

    public int getPlmn4() {
        return plmn4;
    }

    public void setPlmn4(int plmn4) {
        this.plmn4 = plmn4;
    }

    public int getPlmn5() {
        return plmn5;
    }

    public void setPlmn5(int plmn5) {
        this.plmn5 = plmn5;
    }

    public int getPlmn6() {
        return plmn6;
    }

    public void setPlmn6(int plmn6) {
        this.plmn6 = plmn6;
    }

    public int getIspublic2plmn() {
        return ispublic2plmn;
    }

    public void setIspublic2plmn(int ispublic2plmn) {
        this.ispublic2plmn = ispublic2plmn;
    }

    public String getEmsid() {
        return emsid;
    }

    public void setEmsid(String emsid) {
        this.emsid = emsid;
    }

    public int getRelationFlag() {
        return relationFlag;
    }

    public void setRelationFlag(int relationFlag) {
        this.relationFlag = relationFlag;
    }

    public int getIsnaffilter() {
        return isnaffilter;
    }

    public void setIsnaffilter(int isnaffilter) {
        this.isnaffilter = isnaffilter;
    }

    public String getThresholdInfo() {
        return thresholdInfo;
    }

    public void setThresholdInfo(String thresholdInfo) {
        this.thresholdInfo = thresholdInfo;
    }

    public int getInterMittenceCount() {
        return interMittenceCount;
    }

    public void setInterMittenceCount(int interMittenceCount) {
        this.interMittenceCount = interMittenceCount;
    }

    public int getDebugState() {
        return debugState;
    }

    public void setDebugState(int debugState) {
        this.debugState = debugState;
    }

    public String getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(String sortInfo) {
        this.sortInfo = sortInfo;
    }

    public String getClearInfo() {
        return clearInfo;
    }

    public void setClearInfo(String clearInfo) {
        this.clearInfo = clearInfo;
    }



    public void setPerceivedSeverity(int perceivedSeverity) {
        this.perceivedSeverity = perceivedSeverity;
    }

    public void translate(String language) {
        I18n i18n = I18n.getInstance(language);

        if(i18n==null){
            return;
        }

        this.commentText = i18n.translate(this.commentText);

        // special process for additional text
        String[] key_vals = this.additionalText.split("\\|");
        String template_key = key_vals[0];

        String[] varWord = new String[] {"", "POATTRIBUTENAME", "VALUE", "DIRECT", "WARN", "MINOR",
                "MAJOR", "CRITICAL"};

        Map<String, String> vals = new HashMap<String, String>();
        for (int i = 1; i < key_vals.length; i++) {
            if(i==1 || i==3){
                vals.put(varWord[i], i18n.translate(key_vals[i]));
            }else{
                vals.put(varWord[i], key_vals[i]);
            }
        }
        this.additionalText = i18n.translate(template_key, vals);
    }
}
