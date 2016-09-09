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
package org.openo.nfvo.monitor.umc.fm.resource.bean.response;

/**
 * definition of alarm data
 *
 */

public interface IZenapAlarmData {
    // --------------------alarm type constant----------------------------
    /**
     * alarm type constant：communications alarm
     */
    final byte COMMUNICATIONS_ALARM = 0;

    /**
     * alarm type constant：process error alarm
     */
    final byte PROCESSING_ERROR_ALARM = 1;

    /**
     * alarm type constant：quality of service alarm
     */
    final byte QUALITY_OF_SERVICE_ALARM = 2;

    /**
     * alarm type constant：equipment alarm
     */
    final byte EQUIPMENT_ALARM = 3;

    /**
     * alarm type constant：environmental alrm
     */
    final byte ENVIRONMENTAL_ALARM = 4;

    /**
     * alarm type constant：omc alarm
     */
    final byte OMC_ALARM = 5;

    /**
     * alarm type constant：integerity violation
     */
    final byte INTEGRITY_VIOLATION = 6;

    /**
     * alarm type constant：operational violtion
     */
    final byte OPERATIONAL_VIOLATION = 7;

    /**
     * alarm type constant：physical violation
     */
    final byte PHYSICAL_VIOLATION = 8;

    /**
     * alarm type constant：security violation
     */
    final byte SECURITY_VIOLATION = 9;

    /**
     * alarm type constant：time domain violation
     */
    final byte TIME_DOMAIN_VIOLATION = 10;

    // --------------------alarm severity constant----------------------------
    /**
     * alarm severity constant：undifine
     */
    final byte SEVERITY_INDETERMINATE = 0;

    /**
     * alarm severity constant：critical
     */
    final byte SEVERITY_CRITICAL = 1;

    /**
     * alarm severity constant：serious
     */
    final byte SEVERITY_MAJOR = 2;

    /**
     * alarm severity constant：secondery serious
     */
    final byte SEVERITY_MINOR = 3;

    /**
     * alarm severity constant：warning
     */
    final byte SEVERITY_WARNING = 4;

    /**
     * alarm severity constant：cleared
     */
    final byte SEVERITY_CLEARED = 5;

    // ---------------------the state of alarm acknowledge---------------------------------
    /**
     * the state of alarm acknowledge constant：acknowledge
     *
     * @see #ACKSTATE_UNACKNOWLEDGED
     */
    final byte ACKSTATE_ACKNOWLEDGED = 1;

    /**
     * the state of alarm acknowledge constant： unacknowledge
     *
     * @see #ACKSTATE_ACKNOWLEDGED
     */
    final byte ACKSTATE_UNACKNOWLEDGED = 2;

    /**
     * alarm clear type:discard
     */
    final byte CLEARTYPE_DISCARD = 0;

    /**
     * alarm clear type:normal
     */
    final byte CLEARTYPE_NORMAL = 1;

    /**
     * alarm clear type:user
     */
    final byte CLEARTYPE_USER = 2;

    /**
     * alarm clear type:sync
     */
    final byte CLEARTYPE_SYNC = 3;

    /**
     * alarm clear type:restart
     */
    final byte CLEARTYPE_NE_RESTART = 4;

    /**
	 * alarm clear type:delete
     */
    final byte CLEARTYPE_NE_DELETE = 5;

    /**
     * alarm clear type:rule
     */
    final byte CLEARTYPE_RULE = 6;

    /**
     * alarm clear type:admc
     */
    final byte CLEARTYPE_ADMC = 7;

    /**
     * alarm clear type:overflow
     */
    final byte CLEARTYPE_POOL_OVERFLOW = 8;

    /**
     * get id,id is unique for every alarm
     * @return
     */
    long getId();

    /**
     * get serial id
     * @return
     */
    String getAid();

    /**
     * get alarm key,alarm key is unique for every alarm,use to match alarm and alarm recovery.
     * @return
     */
    String getAlarmKey();

    /**
     * get ne type
     *
     * @return
     */
    String getMoc();

    /**
     * get alarm major position ,it's resource id defined by resource management module
     * @return resource id
     */
    String getPosition1();


    String getSubPosition1();


    String getPosition2();


    String getSubPosition2();

    /**
     * get alarm raise time
     */
    long getAlarmRaisedTime();


    String getTimeZoneID();

    /**
     * get time zone offset in milliseconds
     * @return
     */
    int getTimeZoneOffset();

    /**
     * get DST in milliseconds
     *
     */
    int getDSTSaving();


    byte getClearType();

    long getAlarmChangedTime();

    long getAlarmClearedTime();

    byte getAlarmType();

    String getSpecificProblem();

    long getReasonCode();

    byte getPerceivedSeverity();

    String getAdditionalText();

    /**
     * custom attributes, max number is 14
     */
    String[] getCustomAttrs();

    byte getAckState();

    long getAckTime();

    String getAckUserId();

    String getAckSystemId();

    String getClearUserId();

    String getClearSystemId();

    String getCommentText();

    long getCommentTime();

    String getCommentUserId();

    String getCommentSystemId();

    /**
     * is alarm visible
     *
     * @return
     */
    boolean isVisible();

    String getPathName();

    String[] getPathIds();

    String getSubName1();

    String getNeIp();

    String getAlarmTypeName();

    String getProbableCauseCodeName();

    long getProbableCauseCode();

    short getSystemType();

    String getPosition1DisplayName();

    String getMocName();

    String getLink();

    byte getOriginalPerceivedSeverity();

    public boolean isADMC();

    public String getAckInfo();
}
