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
package org.openo.orchestrator.nfv.umc.fm.resource.bean.response;

import java.util.HashMap;
import java.util.Map;

/**
 * result of query current alarm
 */

import javax.xml.bind.annotation.XmlRootElement;

import org.openo.orchestrator.nfv.umc.i18n.I18n;

@XmlRootElement
public class NgictAlarmData implements IZenapAlarmData {
    private long id = 0;

    private String aid = "";

    private String alarmKey = "";

    private String moc = "";

    private String mocName = "";

    private String position1 = "";

    private String position1DisplayName = "";

    private String subPosition1 = "";

    private String subName1 = "";

    private String position2 = "";

    private String subPosition2 = "";

    private long alarmRaisedTime = 0;

    private long alarmChangedTime = 0;

    private short systemType = 0;

    private String systemTypeName = "";

    private long probableCauseCode = 0;

    private String probableCauseCodeName = "";

    private byte alarmType = 0;

    private String alarmTypeName = "";

    private byte perceivedSeverity = 0;

    private long reasonCode = -1;

    private String specificProblem = "";

    private String additionalText = "";

    private String[] customAttrs = new String[0];

    private byte ackState = ACKSTATE_UNACKNOWLEDGED;

    private long ackTime = 0;

    private String ackUserId = "";

    private String ackSystemId = "";

    private String commentText = "";

    private long commentTime = 0;

    private String commentUserId = "";

    private String commentSystemId = "";

    private long alarmClearedTime = 0;

    private String clearUserId = "";

    private String clearSystemId = "";

    private byte clearType = CLEARTYPE_NORMAL;

    private boolean visible = true;

    private String timeZoneID = "";

    private int timeZoneOffset = 0;

    private int DSTSaving = 0;

    private String neIp = "";

    private String pathName = "";

    private String[] pathIds = new String[0];

    private String link = "";

    private byte originalPerceivedSeverity = SEVERITY_INDETERMINATE;

    private boolean isADMC = false;

    private String ackInfo = "";

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    @Override
    public String getAlarmKey() {
        return alarmKey;
    }

    public void setAlarmKey(String alarmKey) {
        this.alarmKey = alarmKey;
    }

    @Override
    public String getMoc() {
        return moc;
    }

    public void setMoc(String moc) {
        this.moc = moc;
    }

    @Override
    public String getMocName() {
        return mocName;
    }

    public void setMocName(String mocName) {
        this.mocName = mocName;
    }

    @Override
    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    @Override
    public String getPosition1DisplayName() {
        return position1DisplayName;
    }

    public void setPosition1DisplayName(String position1DisplayName) {
        this.position1DisplayName = (position1DisplayName==null)?"unkown":position1DisplayName;
    }

    @Override
    public String getSubPosition1() {
        return subPosition1;
    }

    public void setSubPosition1(String subPosition1) {
        this.subPosition1 = subPosition1;
    }

    @Override
    public String getSubName1() {
        return subName1;
    }

    public void setSubName1(String subName1) {
        this.subName1 = subName1;
    }

    @Override
    public String getPosition2() {
        return position2;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    @Override
    public String getSubPosition2() {
        return subPosition2;
    }

    public void setSubPosition2(String subPosition2) {
        this.subPosition2 = subPosition2;
    }

    @Override
    public long getAlarmRaisedTime() {
        return alarmRaisedTime;
    }

    public void setAlarmRaisedTime(long alarmRaisedTime) {
        this.alarmRaisedTime = alarmRaisedTime;
    }

    @Override
    public long getAlarmChangedTime() {
        return alarmChangedTime;
    }

    public void setAlarmChangedTime(long alarmChangedTime) {
        this.alarmChangedTime = alarmChangedTime;
    }

    @Override
    public short getSystemType() {
        return systemType;
    }

    public void setSystemType(short systemType) {
        this.systemType = systemType;
    }

    public String getSystemTypeName() {
        return systemTypeName;
    }

    public void setSystemTypeName(String systemTypeName) {
        this.systemTypeName = systemTypeName;
    }

    @Override
    public long getProbableCauseCode() {
        return probableCauseCode;
    }

    public void setProbableCauseCode(long probableCauseCode) {
        this.probableCauseCode = probableCauseCode;
    }

    @Override
    public String getProbableCauseCodeName() {
        return probableCauseCodeName;
    }

    public void setProbableCauseCodeName(String probableCauseCodeName) {
        this.probableCauseCodeName = probableCauseCodeName;
    }

    @Override
    public byte getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(byte alarmType) {
        this.alarmType = alarmType;
    }

    @Override
    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    @Override
    public byte getPerceivedSeverity() {
        return perceivedSeverity;
    }

    public void setPerceivedSeverity(byte perceivedSeverity) {
        this.perceivedSeverity = perceivedSeverity;
    }

    @Override
    public long getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(long reasonCode) {
        this.reasonCode = reasonCode;
    }

    @Override
    public String getSpecificProblem() {
        return specificProblem;
    }

    public void setSpecificProblem(String specificProblem) {
        this.specificProblem = specificProblem;
    }

    @Override
    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    @Override
    public String[] getCustomAttrs() {
        return customAttrs;
    }

    public void setCustomAttrs(String[] customAttrs) {
        this.customAttrs = customAttrs;
    }

    @Override
    public byte getAckState() {
        return ackState;
    }

    public void setAckState(byte ackState) {
        this.ackState = ackState;
    }

    @Override
    public long getAckTime() {
        return ackTime;
    }

    public void setAckTime(long ackTime) {
        this.ackTime = ackTime;
    }

    @Override
    public String getAckUserId() {
        return ackUserId;
    }

    public void setAckUserId(String ackUserId) {
        this.ackUserId = ackUserId;
    }

    @Override
    public String getAckSystemId() {
        return ackSystemId;
    }

    public void setAckSystemId(String ackSystemId) {
        this.ackSystemId = ackSystemId;
    }

    @Override
    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @Override
    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    @Override
    public String getCommentSystemId() {
        return commentSystemId;
    }

    public void setCommentSystemId(String commentSystemId) {
        this.commentSystemId = commentSystemId;
    }

    @Override
    public long getAlarmClearedTime() {
        return alarmClearedTime;
    }

    public void setAlarmClearedTime(long alarmClearedTime) {
        this.alarmClearedTime = alarmClearedTime;
    }

    @Override
    public String getClearUserId() {
        return clearUserId;
    }

    public void setClearUserId(String clearUserId) {
        this.clearUserId = clearUserId;
    }

    @Override
    public String getClearSystemId() {
        return clearSystemId;
    }

    public void setClearSystemId(String clearSystemId) {
        this.clearSystemId = clearSystemId;
    }

    @Override
    public byte getClearType() {
        return clearType;
    }

    public void setClearType(byte clearType) {
        this.clearType = clearType;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String getTimeZoneID() {
        return timeZoneID;
    }

    public void setTimeZoneID(String timeZoneID) {
        this.timeZoneID = timeZoneID;
    }

    @Override
    public int getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(int timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    @Override
    public int getDSTSaving() {
        return DSTSaving;
    }

    public void setDSTSaving(int dSTSaving) {
        DSTSaving = dSTSaving;
    }

    @Override
    public String getNeIp() {
        return neIp;
    }

    public void setNeIp(String neip) {
        this.neIp = neip;
    }

    @Override
    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @Override
    public String[] getPathIds() {
        return pathIds;
    }

    public void setPathIds(String[] pathIds) {
        this.pathIds = pathIds;
    }

    @Override
    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public byte getOriginalPerceivedSeverity() {
        return this.originalPerceivedSeverity;
    }


    public void setOriginalPerceivedSeverity(byte originalPerceivedSeverity) {
        this.originalPerceivedSeverity = originalPerceivedSeverity;
    }

    public void setADMC(boolean isADMC) {
        this.isADMC = isADMC;
    }

    @Override
    public boolean isADMC() {
        return this.isADMC;
    }

    public void setAckInfo(String ackInfo) {
        this.ackInfo = ackInfo;
    }

    @Override
    public String getAckInfo() {
        return this.ackInfo;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer().append("alarmKey:").append(this.alarmKey).append("ID:")
                .append(this.id).append(" position1:").append(this.position1);
        return sb.toString();
    }

    public void translate(String language) {
        I18n i18n = I18n.getInstance(language);

        if(i18n==null){
            return;
        }

        this.alarmTypeName = i18n.translate(this.alarmTypeName);
        this.probableCauseCodeName = i18n.translate(this.probableCauseCodeName);
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
