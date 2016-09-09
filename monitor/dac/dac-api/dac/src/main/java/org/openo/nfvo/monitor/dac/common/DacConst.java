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
package org.openo.nfvo.monitor.dac.common;

public class DacConst {
    public static final String m_COLLECTEDTIME = "COLLECTEDTIME";
    public static final String MONITORNAME = "MONITORNAME";
    public static final String NETYPEID = "MOC";
    public static final String RESID = "RESID";
    public static final String HOSTTYPE = "HOSTTYPE";
    public static final String VERSION = "VERSION";
    public static final String IPADDRESS = "IPADDRESS";
    public static final String PORT = "PORT";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String DBPORT = "DBPORT";
    public static final String PROTOCOL = "PROTOCOL";
    public static final String CUSTOMPARA = "CUSTOMPARA";
    public static final String TELNET = "TELNET";
    public static final String SSH = "SSH";
    public static final String SNMPPORT = "SNMPPORT";
    public static final String SNMPVERSION = "SNMPVERSION";
    public static final String SNMPCOMMUNITY = "SNMPCOMMUNITY";
    public static final String SNMPWRITECOMMUNITY = "SNMPWRITECOMMUNITY";
    public static final String SNMPUSERNAME = "SNMPUSERNAME";
    public static final String SNMPSECURITYNAME = "SNMPSECURITYNAME";
    public static final String SNMPSECURITYLEVEL = "SNMPSECURITYLEVEL";
    public static final String SNMPAUTHPROTOCOL = "SNMPAUTHPROTOCOL";
    public static final String SNMPAUTHPASSWORD = "SNMPAUTHPASSWORD";
    public static final String SNMPPRIVPASSWORD = "SNMPPRIVPASSWORD";

//    public static final String DATAAQDIR = "dataaq";
    public static final String SYSTEMDIR = "system";
    public static final String FMDIR = "fm";
    public static final String MIBFILES = "*.mib";
    public static final String MIBINFOFILE = "loadedmibs.yml";
//    public static final String SCRIPTDIR = "scripts";

    public static final String IFSUCCESS = "IFSUCCESS";
    public static final String COLLECT_FAIL = "false";
    public static final String COLLECT_FAIL_DETAIL = "ERRORMESSAGE";

    public static final String REALMONITORNAME = "REALMONITORNAME";

    public static final int ERRORCODE_PROVIDERS = 1;
    public static final int ERRORCODE_NOTUPONDATA = 2;
    public static final int ERRORCODE_COLLECTERROR = 3;
    public static final int ERRORCODE_INCORRECTPARSERCONTENT = 4;
    public static final int ERRORCODE_PASSWORDERROR = 5;
    public static final int ERRORCODE_ILLEGALARGUMENT = 6;

    public static final int PING_PKGCOUNT = 4;
    public static final int PING_PKGSIZE = 64;
    public static final int PING_TTL = 128;
    public static final int PING_TIMEOUT = 2;

    public static final String MOC_NFV_VDU_LINUX = "nfv.vdu.linux";
    public static final String MOC_NFV_HOST_LINUX = "nfv.host.linux";

    public static final String m_SYSDESCR = "SYSDESCR";
    public static final String m_SYSNAME = "SYSNAME";
    public static final String m_SYSUPTIME = "SYSUPTIME";
    public static final String m_IFDESCR = "IFDESCR";
    public static final String m_IFNAME = "IFNAME";
    public static final String m_IFINDEX = "IFINDEX";
    public static final String m_IFTYPE = "IFTYPE";
    public static final String m_IFSPEED = "IFSPEED";
    public static final String m_IFINDISCARDS = "IFINDISCARDS";
    public static final String m_IFINERRORS = "IFINERRORS";
    public static final String m_IFINUCASTPKTS = "IFINUCASTPKTS";
    public static final String m_IFINNUCASTPKTS = "IFINNUCASTPKTS";
    public static final String m_IFINUCASTPKTS64 = "IFINUCASTPKTS64";
    public static final String m_IFINMULTICASTPKTS = "IFINMULTICASTPKTS";
    public static final String m_IFINMULTICASTPKTS64 = "IFINMULTICASTPKTS64";
    public static final String m_IFINBROADCASTPKTS = "IFINBROADCASTPKTS";
    public static final String m_IFINBROADCASTPKTS64 = "IFINBROADCASTPKTS64";
    public static final String m_IFOUTDISCARDS = "IFOUTDISCARDS";
    public static final String m_IFOUTERRORS = "IFOUTERRORS";
    public static final String m_IFOUTUCASTPKTS = "IFOUTUCASTPKTS";
    public static final String m_IFOUTNUCASTPKTS = "IFOUTNUCASTPKTS";
    public static final String m_IFOUTUCASTPKTS64 = "IFOUTUCASTPKTS64";
    public static final String m_IFOUTMULTICASTPKTS = "IFOUTMULTICASTPKTS";
    public static final String m_IFOUTMULTICASTPKTS64 ="IFOUTMULTICASTPKTS64";
    public static final String m_IFOUTBROADCASTPKTS = "IFOUTBROADCASTPKTS";
    public static final String m_IFOUTBROADCASTPKTS64 = "IFOUTBROADCASTPKTS64";
    public static final String m_INPACKETLOSSRATIO = "INPACKETLOSSRATIO";
    public static final String m_INPACKETERRORRATIO = "INPACKETWRONGRATIO";
    public static final String m_OUTPACKETLOSSRATIO = "OUTPACKETLOSSRATIO";
    public static final String m_OUTPACKETERRORRATIO = "OUTPACKETWRONGRATIO";
    //add by fcui for router/switch's port flux
    public static final String m_IFINOCTETS = "IFINOCTETS";
    public static final String m_IFINOCTETS64 = "IFINOCTETS64";
    public static final String m_IFOUTOCTETS = "IFOUTOCTETS";
    public static final String m_IFOUTOCTETS64 = "IFOUTOCTETS64";
    public static final String m_IFINPKTS = "IFINPKTS";
    public static final String m_IFOUTPKTS = "IFOUTPKTS";
    public static final String m_IFBRANDWIDTHRATIO = "IFBRANDWIDTHRATIO";
    public static final long TWO_OF32POWER = 4294967295L;

    public static final String NATIVEPING = "NATIVEPING";

}
