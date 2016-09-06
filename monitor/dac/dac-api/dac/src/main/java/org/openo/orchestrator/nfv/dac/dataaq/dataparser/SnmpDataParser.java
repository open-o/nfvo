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
package org.openo.orchestrator.nfv.dac.dataaq.dataparser;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.util.Calculator;
import org.openo.orchestrator.nfv.dac.common.util.DacUtil;
import org.openo.orchestrator.nfv.dac.dataaq.common.DataAcquireException;
import org.openo.orchestrator.nfv.dac.dataaq.common.IDataParser;
import org.openo.orchestrator.nfv.dac.dataaq.common.MonitorException;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.DaPerfCounterInfo;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnmpDataParser implements IDataParser {
	private static final Logger dMsg = LoggerFactory.getLogger(TelnetDataParser.class);
	private static final ConcurrentHashMap resultCache = new ConcurrentHashMap();

	// 性能任务采集粒度
	private static long collectInterval;

	// 系统更新时间，用于判断节点是否重启
	private long sysUpTimeLast;
	private long sysUpTimeThis;

	// 采集时间，用于差值计算判断是否有数据缺失
	private long cdTimeLast;
	private long cdTimeThis;
	@Override
	public Object parse(Vector dataCollected, DaPerfCounterInfo perfCounterInfo)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Object parse(String value, Map dataCollected)
	{
		return dataCollected.get(value);
	}
	
	public Map parse(MonitorTaskInfo taskInfo, Map paras, Map resultMap) throws DataAcquireException
	{
		String monitorName = (String) paras.get(DacConst.REALMONITORNAME);
        if (resultMap.get("SYSDESCR") != null)
        {
            Vector ifDescr = (Vector) resultMap.get("SYSDESCR");
            if (ifDescr != null && ifDescr.size() != 0)
            {
                String str = (String) ifDescr.get(0);
                ifDescr.set(0, str.replace('\n', ' '));
                resultMap.put("SYSDESCR", ifDescr);
            }
        }
        if (resultMap.get("IFIPADDR") != null)
        {
            ifIpAddrToPort(resultMap);
        }
        if (resultMap.get("IFIPADDR") != null && resultMap.get("IFSPEED") != null)
        {
            reviseInterfacesConfigInfo(resultMap);
        }
        if (resultMap.get("CPURATIO") != null)
        {
            avgVectorCounter(resultMap, "CPURATIO");
        }
        if (resultMap.get("MEMRATIO") != null)
        {
            avgVectorCounter(resultMap, "MEMRATIO");
        }
        if (resultMap.get("IFNAME") != null)
        {
            treatIfName(resultMap);
        }
        if (resultMap.get("IFSPEED") != null)
        {
            treatIfSpeed(resultMap);
        }

        if (resultMap.get("IFTYPE") != null)
        {
            Vector ifType = (Vector) resultMap.get("IFTYPE");
            resultMap.put("IFTYPE", ifTypeConvert(ifType));
        }
        if (resultMap.get("IFADMINSTATUS") != null)
        {
            Vector ifAdminStatus = (Vector) resultMap.get("IFADMINSTATUS");
            resultMap.put("IFADMINSTATUS", ifStatusConvert(ifAdminStatus));
        }
        if (resultMap.get("IFOPERSTATUS") != null)
        {
            Vector ifOperStatus = (Vector) resultMap.get("IFOPERSTATUS");
            resultMap.put("IFOPERSTATUS", ifStatusConvert(ifOperStatus));
        }
        if (resultMap.get("IFIPADDR") != null)
        {
            ifIpAddrToPort(resultMap);
        }
        if (resultMap.get("IFIPADDR") != null && resultMap.get("IFSPEED") != null)
        {
            reviseInterfacesConfigInfo(resultMap);
        }
        if (monitorName.indexOf("INOUTSTATISTICS") != -1 && resultMap.get("IFINOCTETS") != null)
        {
            treatIfHCOctets(resultMap);
            resultMap = cacheAndCaculateResult(taskInfo, resultMap, new Date());
        }
		return resultMap;
	}

	
	protected void treatIfSpeed(Map resultMap)
	{
		Vector ifIndex = (Vector) resultMap.get("IFINDEX");
		Vector ifIndexOidAppend = (Vector) resultMap.get("IFINDEXOIDAPPEND");

		Vector ifLSpeed = (Vector) resultMap.get("IFSPEED");
		Vector ifHSpeed = (Vector) resultMap.get("IFHIGHSPEED");
		Vector ifLSpeedOidAppend = (Vector) resultMap.get("IFSPEEDOIDAPPEND");
		Vector ifHSpeedOidAppend = (Vector) resultMap.get("IFHIGHSPEEDOIDAPPEND");

		int sizeIfIndex = (ifIndex != null) ? ifIndexOidAppend.size() : 0;
		int sizeIfLSpeed = (ifLSpeed != null) ? ifLSpeedOidAppend.size() : 0;
		int sizeIfHSpeed = (ifHSpeed != null) ? ifHSpeedOidAppend.size() : 0;

		Vector tmpifValue = null;
		Object tmpOidAppend = null;
		Object tmpOid2Value = null;

		// 处理ifSpeed
		if (sizeIfHSpeed != 0 && sizeIfHSpeed >= sizeIfIndex)
		{
			tmpifValue = new Vector();
			Map mTemp = new HashMap();

			String str = null;
			double speed = 1.0;
			for (int i = 0; i < sizeIfHSpeed; i++)
			{
				mTemp.put(ifHSpeedOidAppend.get(i), ifHSpeed.get(i));
			}
			for (int j = 0; j < sizeIfIndex; j++)
			{
				tmpOidAppend = ifLSpeedOidAppend.get(j);
				tmpOid2Value = mTemp.get(tmpOidAppend);
				if (tmpOid2Value != null)
				{
					if (tmpOid2Value.toString().equalsIgnoreCase("0"))
					{
						str = (String) ifLSpeed.get(j);
						speed = Long.parseLong(str) / 1000000.0;
						tmpOid2Value = Double.toString(speed);
					}
					tmpifValue.add(j, tmpOid2Value.toString());
				}
			}
			ifLSpeed = tmpifValue;
		} else
		{
			String str = null;
			double speed = 1.0;
			for (int i = 0; i < sizeIfLSpeed; i++)
			{
				str = (String) ifLSpeed.get(i);
				speed = Long.parseLong(str) / 1000000.0;
				ifLSpeed.set(i, Double.toString(speed));
			}
		}
		resultMap.put("IFSPEED", DacUtil.vectorSetScale(ifLSpeed, 3));
		resultMap.put("IFSPEEDOIDAPPEND", ifLSpeedOidAppend);
	}
	
	protected Vector ifStatusConvert(Vector ifStatus)
	{
		for (int i = 0, sizev = ifStatus.size(); i < sizev; i++)
		{
			switch (Integer.parseInt((String) ifStatus.get(i)))
			{
			case 1:
				ifStatus.set(i, "up(1)");
				break;
			case 2:
				ifStatus.set(i, "down(2)");
				break;
			case 3:
				ifStatus.set(i, "testing(3)");
				break;
			default:
				break;
			}
		}
		return ifStatus;
	}
	
	protected void treatIfName(Map resultMap)
	{
		Vector ifIndex = (Vector) resultMap.get("IFINDEX");
		Vector ifIndexOidAppend = (Vector) resultMap.get("IFINDEXOIDAPPEND");

		Vector ifName = (Vector) resultMap.get("IFNAME");
		Vector ifDescr = (Vector) resultMap.get("IFDESCR");
		Vector ifNameOidAppend = (Vector) resultMap.get("IFNAMEOIDAPPEND");
		Vector ifDescrOidAppend = (Vector) resultMap.get("IFDESCROIDAPPEND");

		int sizeIfIndex = (ifIndex != null) ? ifIndexOidAppend.size() : 0;
		int sizeIfName = (ifName != null) ? ifNameOidAppend.size() : 0;
		int sizeIfDescr = (ifDescr != null) ? ifDescrOidAppend.size() : 0;

		Vector tmpifValue = null;
		Object tmpOidAppend = null;
		Object tmpOid2Value = null;

		// 处理ifName
		// 如果IF-MIB中ifName无法获取，则使用RFC1213-MIB的ifDescr
		// 对于ifName>ifDescr,则需要进行调整,个数为ifDescr,但具体使用ifName中的
		if (sizeIfName < sizeIfDescr)
		{
			resultMap.put("IFNAME", ifDescr);
			resultMap.put("IFNAMEOIDAPPEND", ifDescrOidAppend);
		} else if (sizeIfName != 0 && sizeIfName > sizeIfIndex)
		{
			// 说明RF1213与IF-MIB返回行数不一致（ZTER10会出现这种情况），以RF1213的返回为准
			tmpifValue = new Vector();
			Map mTemp = new HashMap();
			for (int i = 0; i < sizeIfName; i++)
			{
				mTemp.put(ifNameOidAppend.get(i), ifName.get(i));
			}
			for (int j = 0; j < sizeIfIndex; j++)
			{
				tmpOidAppend = ifDescrOidAppend.get(j);
				tmpOid2Value = mTemp.get(tmpOidAppend);
				if (tmpOid2Value != null)
				{
					tmpifValue.add(j, tmpOid2Value.toString());
				}
			}
			ifName = tmpifValue;

			resultMap.put("IFNAME", ifName);
			resultMap.put("IFNAMEOIDAPPEND", ifIndexOidAppend);
		}
	}
	protected void avgVectorCounter(Map resultMap, String CounterName)
	{
		Vector counterVec = (Vector) resultMap.get(CounterName);
		int size = counterVec.size();
		double avgValue = 0.0;
		for (int i = 0; i < size; i++)
		{
			avgValue = avgValue + Double.parseDouble((String) counterVec.get(i));
		}
		avgValue = avgValue / size;
		Vector newCounterVec = new Vector();
		if (size != 0)
		{
			newCounterVec.add(Double.toString(avgValue));
		}
		resultMap.put(CounterName, newCounterVec);
	}
	protected void ifIpAddrToPort(Map resultMap)
	{
		Vector vecIfAddr = (Vector) resultMap.get("IFIPADDR");
		Vector vecIfIndex = (Vector) resultMap.get("IFINDEX");
		Vector vecIfIp2Index = (Vector) resultMap.get("IFIP2INDEX");
		Vector vecRetIfAddr = new Vector();
		for (int i = 0; i < vecIfIndex.size(); i++)
		{
			vecRetIfAddr.add(" ");
		}
		Iterator iter = vecIfIp2Index.iterator();
		while (iter.hasNext())
		{
			String strIfIndex = iter.next().toString();
			int index_ = vecIfIp2Index.indexOf(strIfIndex);
			int indexOf = vecIfIndex.indexOf(strIfIndex);
			if (indexOf != -1)
			{
				vecRetIfAddr.set(indexOf, vecIfAddr.get(index_));
			}
		}
		resultMap.put("IFIPADDR", vecRetIfAddr);
	}
	
	protected static Map reviseInterfacesConfigInfo(Map resultMap)
    {
        Vector ifIpaddr = adjustResult(resultMap, "IFIPADDR");
        Vector ifAdminStatus = adjustResult(resultMap, "IFADMINSTATUS");
        Vector ifOperStatus = adjustResult(resultMap, "IFOPERSTATUS");
        Vector ifSpeed = adjustResult(resultMap, DacConst.m_IFSPEED);

        resultMap.put("IFIPADDR", ifIpaddr);
        resultMap.put("IFADMINSTATUS", ifAdminStatus);
        resultMap.put("IFOPERSTATUS", ifOperStatus);
        resultMap.put(DacConst.m_IFSPEED, ifSpeed);

        return resultMap;
    }
    
	protected static Vector adjustResult(Map resultMap, String nodeName)
	{
		Vector ifIndexOidAppend = (Vector) resultMap.get("IFINDEXOIDAPPEND");
		Vector ifNodeName = (Vector) resultMap.get(nodeName);
		Vector ifNodeNameOidAppend = (Vector) resultMap.get(nodeName + "OIDAPPEND");

		Vector vecAdjustedResult = new Vector();
		if (ifNodeName == null || ifNodeNameOidAppend == null)
		{
			for (int i = 0; i < ifIndexOidAppend.size(); i++)
			{
				vecAdjustedResult.add("0");
			}
		} else
		{
			Map mTemp = new HashMap();
			Object tmpOidAppend = null;
			Object tmpOid2Value = null;
			String tmpRes;
			int size = (ifNodeName.size() > ifNodeNameOidAppend.size()) ? ifNodeNameOidAppend.size() : ifNodeName.size();
			for (int i = 0; i < size; i++)
			{
				mTemp.put(ifNodeNameOidAppend.get(i), ifNodeName.get(i));
			}
			for (int j = 0; j < ifIndexOidAppend.size(); j++)
			{
				tmpOidAppend = ifIndexOidAppend.get(j);
				tmpOid2Value = mTemp.get(tmpOidAppend);
				tmpRes = (tmpOid2Value == null) ? "0" : tmpOid2Value.toString();
				vecAdjustedResult.add(tmpRes);
			}

			// }
		}
		return vecAdjustedResult;
	}
	
	protected static Vector<String> ifTypeConvert(Vector<String> vec)
	{
		for (int i = 0, sizev = vec.size(); i < sizev; i++)
		{
			switch (Integer.parseInt(vec.get(i)))
			{
			case 1:
				vec.set(i, "other(1)");
				break;
			case 2:
				vec.set(i, "regular1822(2)");
				break;
			case 3:
				vec.set(i, "hdh1822(3)");
				break;
			case 4:
				vec.set(i, "ddnX25(4)");
				break;
			case 5:
				vec.set(i, "rfc877x25(5)");
				break;
			case 6:
				vec.set(i, "ethernetCsmacd(6)");
				break;
			case 7:
				vec.set(i, "iso88023Csmacd(7)");
				break;
			case 8:
				vec.set(i, "iso88024TokenBus(8)");
				break;
			case 9:
				vec.set(i, "iso88025TokenRing(9)");
				break;
			case 10:
				vec.set(i, "iso88026Man(10)");
				break;
			case 11:
				vec.set(i, "starLan(11)");
				break;
			case 12:
				vec.set(i, "proteon10Mbit(12)");
				break;
			case 13:
				vec.set(i, "proteon80Mbit(13)");
				break;
			case 14:
				vec.set(i, "hyperchannel(14)");
				break;
			case 15:
				vec.set(i, "fddi(15)");
				break;
			case 16:
				vec.set(i, "lapb(16)");
				break;
			case 17:
				vec.set(i, "sdlc(17)");
				break;
			case 18:
				vec.set(i, "ds1(18)");
				break;
			case 19:
				vec.set(i, "e1(19)");
				break;
			case 20:
				vec.set(i, "basicISDN(20)");
				break;
			case 21:
				vec.set(i, "primaryISDN(21)");
				break;
			case 22:
				vec.set(i, "propPointToPointSerial(22)");
				break;
			case 23:
				vec.set(i, "ppp(23)");
				break;
			case 24:
				vec.set(i, "softwareLoopback(24)");
				break;
			case 25:
				vec.set(i, "eon(25)");
				break;
			case 26:
				vec.set(i, "ethernet3Mbit(26)");
				break;
			case 27:
				vec.set(i, "nsip(27)");
				break;
			case 28:
				vec.set(i, "slip(28)");
				break;
			case 29:
				vec.set(i, "ultra(29)");
				break;
			case 30:
				vec.set(i, "ds3(30)");
				break;
			case 31:
				vec.set(i, "sip(31)");
				break;
			case 32:
				vec.set(i, "frameRelay(32)");
				break;
			case 33:
				vec.set(i, "rs232(33)");
				break;
			case 34:
				vec.set(i, "para(34)");
				break;
			case 35:
				vec.set(i, "arcnet(35)");
				break;
			case 36:
				vec.set(i, "arcnetPlus(36)");
				break;
			case 37:
				vec.set(i, "atm(37)");
				break;
			case 38:
				vec.set(i, "miox25(38)");
				break;
			case 39:
				vec.set(i, "sonet(39)");
				break;
			case 40:
				vec.set(i, "x25ple(40)");
				break;
			case 41:
				vec.set(i, "iso88022llc(41)");
				break;
			case 42:
				vec.set(i, "localTalk(42)");
				break;
			case 43:
				vec.set(i, "smdsDxi(43)");
				break;
			case 44:
				vec.set(i, "frameRelayService(44)");
				break;
			case 45:
				vec.set(i, "v35(45)");
				break;
			case 46:
				vec.set(i, "hssi(46)");
				break;
			case 47:
				vec.set(i, "hippi(47)");
				break;
			case 48:
				vec.set(i, "modem(48)");
				break;
			case 49:
				vec.set(i, "aal5(49)");
				break;
			case 50:
				vec.set(i, "sonetPath(50)");
				break;
			case 51:
				vec.set(i, "sonetVT(51)");
				break;
			case 52:
				vec.set(i, "smdsIcip(52)");
				break;
			case 53:
				vec.set(i, "propVirtual(53)");
				break;
			case 54:
				vec.set(i, "propMultiplexor(54)");
				break;
			case 55:
				vec.set(i, "ieee80212(55)");
				break;
			case 56:
				vec.set(i, "fibreChannel(56)");
				break;
			case 57:
				vec.set(i, "hippiInterface(57)");
				break;
			case 58:
				vec.set(i, "frameRelayInterconnect(58)");
				break;
			case 59:
				vec.set(i, "aflane8023(59)");
				break;
			case 60:
				vec.set(i, "aflane8025(60)");
				break;
			case 61:
				vec.set(i, "cctEmul(61)");
				break;
			case 62:
				vec.set(i, "fastEther(62)");
				break;
			case 63:
				vec.set(i, "isdn(63)");
				break;
			case 64:
				vec.set(i, "v11(64)");
				break;
			case 65:
				vec.set(i, "v36(65)");
				break;
			case 66:
				vec.set(i, "g703at64k(66)");
				break;
			case 67:
				vec.set(i, "g703at2mb(67)");
				break;
			case 68:
				vec.set(i, "qllc(68)");
				break;
			case 69:
				vec.set(i, "fastEtherFX(69)");
				break;
			case 70:
				vec.set(i, "channel(70)");
				break;
			case 71:
				vec.set(i, "ieee80211(71)");
				break;
			case 72:
				vec.set(i, "ibm370parChan(72)");
				break;
			case 73:
				vec.set(i, "escon(73)");
				break;
			case 74:
				vec.set(i, "dlsw(74)");
				break;
			case 75:
				vec.set(i, "isdns(75)");
				break;
			case 76:
				vec.set(i, "isdnu(76)");
				break;
			case 77:
				vec.set(i, "lapd(77)");
				break;
			case 78:
				vec.set(i, "ipSwitch(78)");
				break;
			case 79:
				vec.set(i, "rsrb(79)");
				break;
			case 80:
				vec.set(i, "atmLogical(80)");
				break;
			case 81:
				vec.set(i, "ds0(81)");
				break;
			case 82:
				vec.set(i, "ds0Bundle(82)");
				break;
			case 83:
				vec.set(i, "bsc(83)");
				break;
			case 84:
				vec.set(i, "async(84)");
				break;
			case 85:
				vec.set(i, "cnr(85)");
				break;
			case 86:
				vec.set(i, "iso88025Dtr(86)");
				break;
			case 87:
				vec.set(i, "eplrs(87)");
				break;
			case 88:
				vec.set(i, "arap(88)");
				break;
			case 89:
				vec.set(i, "propCnls(89)");
				break;
			case 90:
				vec.set(i, "hostPad(90)");
				break;
			case 91:
				vec.set(i, "termPad(91)");
				break;
			case 92:
				vec.set(i, "frameRelayMPI(92)");
				break;
			case 93:
				vec.set(i, "x213(93)");
				break;
			case 94:
				vec.set(i, "adsl(94)");
				break;
			case 95:
				vec.set(i, "radsl(95)");
				break;
			case 96:
				vec.set(i, "sdsl(96)");
				break;
			case 97:
				vec.set(i, "vdsl(97)");
				break;
			case 98:
				vec.set(i, "iso88025CRFPInt(98)");
				break;
			case 99:
				vec.set(i, "myrinet(99)");
				break;
			case 100:
				vec.set(i, "voiceEM(100)");
				break;
			case 101:
				vec.set(i, "voiceFXO(101)");
				break;
			case 102:
				vec.set(i, "voiceFXS(102)");
				break;
			case 103:
				vec.set(i, "voiceEncap(103)");
				break;
			case 104:
				vec.set(i, "voiceOverIp(104)");
				break;
			case 105:
				vec.set(i, "atmDxi(105)");
				break;
			case 106:
				vec.set(i, "atmFuni(106)");
				break;
			case 107:
				vec.set(i, "atmIma (107)");
				break;
			case 108:
				vec.set(i, "pppMultilinkBundle(108)");
				break;
			case 109:
				vec.set(i, "ipOverCdlc (109)");
				break;
			case 110:
				vec.set(i, "ipOverClaw (110)");
				break;
			case 111:
				vec.set(i, "stackToStack (111)");
				break;
			case 112:
				vec.set(i, "virtualIpAddress (112)");
				break;
			case 113:
				vec.set(i, "mpc (113)");
				break;
			case 114:
				vec.set(i, "ipOverAtm (114)");
				break;
			case 115:
				vec.set(i, "iso88025Fiber (115)");
				break;
			case 116:
				vec.set(i, "tdlc (116)");
				break;
			case 117:
				vec.set(i, "gigabitEthernet (117)");
				break;
			case 118:
				vec.set(i, "hdlc (118)");
				break;
			case 119:
				vec.set(i, "lapf (119)");
				break;
			case 120:
				vec.set(i, "v37 (120)");
				break;
			case 121:
				vec.set(i, "x25mlp (121)");
				break;
			case 122:
				vec.set(i, "x25huntGroup (122)");
				break;
			case 123:
				vec.set(i, "trasnpHdlc (123)");
				break;
			case 124:
				vec.set(i, "interleave (124)");
				break;
			case 125:
				vec.set(i, "fast (125)");
				break;
			case 126:
				vec.set(i, "ip (126)");
				break;
			case 127:
				vec.set(i, "docsCableMaclayer (127)");
				break;
			case 128:
				vec.set(i, "docsCableDownstream (128)");
				break;
			case 129:
				vec.set(i, "docsCableUpstream (129)");
				break;
			case 130:
				vec.set(i, "a12MppSwitch (130)");
				break;
			case 131:
				vec.set(i, "tunnel (131)");
				break;
			case 132:
				vec.set(i, "coffee (132)");
				break;
			case 133:
				vec.set(i, "ces (133)");
				break;
			case 134:
				vec.set(i, "atmSubInterface (134)");
				break;
			case 135:
				vec.set(i, "l2vlan (135)");
				break;
			case 136:
				vec.set(i, "l3ipvlan (136)");
				break;
			case 137:
				vec.set(i, "l3ipxvlan (137)");
				break;
			case 138:
				vec.set(i, "digitalPowerline (138)");
				break;
			case 139:
				vec.set(i, "mediaMailOverIp (139)");
				break;
			case 140:
				vec.set(i, "dtm (140)");
				break;
			case 141:
				vec.set(i, "dcn (141)");
				break;
			case 142:
				vec.set(i, "ipForward (142)");
				break;
			case 143:
				vec.set(i, "msdsl (143)");
				break;
			case 144:
				vec.set(i, "ieee1394 (144)");
				break;
			case 145:
				vec.set(i, "if-gsn (145)");
				break;
			case 146:
				vec.set(i, "dvbRccMacLayer (146)");
				break;
			case 147:
				vec.set(i, "dvbRccDownstream (147)");
				break;
			case 148:
				vec.set(i, "dvbRccUpstream (148)");
				break;
			case 149:
				vec.set(i, "atmVirtual (149)");
				break;
			case 150:
				vec.set(i, "mplsTunnel (150)");
				break;
			case 151:
				vec.set(i, "srp (151)");
				break;
			case 152:
				vec.set(i, "voiceOverAtm (152)");
				break;
			case 153:
				vec.set(i, "voiceOverFrameRelay (153)");
				break;
			case 154:
				vec.set(i, "idsl (154)");
				break;
			case 155:
				vec.set(i, "compositeLink (155)");
				break;
			case 156:
				vec.set(i, "ss7SigLink (156)");
				break;
			case 157:
				vec.set(i, "propWirelessP2P (157)");
				break;
			case 158:
				vec.set(i, "frForward (158)");
				break;
			case 159:
				vec.set(i, "rfc1483 (159)");
				break;
			case 160:
				vec.set(i, "usb (160)");
				break;
			case 161:
				vec.set(i, "ieee8023adLag (161)");
				break;
			case 162:
				vec.set(i, "bgppolicyaccounting (162)");
				break;
			case 163:
				vec.set(i, "frf16MfrBundle (163)");
				break;
			case 164:
				vec.set(i, "h323Gatekeeper (164)");
				break;
			case 165:
				vec.set(i, "h323Proxy (165)");
				break;
			case 166:
				vec.set(i, "mpls (166)");
				break;
			case 167:
				vec.set(i, "mfSigLink (167)");
				break;
			case 168:
				vec.set(i, "hdsl2 (168)");
				break;
			case 169:
				vec.set(i, "shdsl (169)");
				break;
			case 170:
				vec.set(i, "ds1FDL (170)");
				break;
			case 171:
				vec.set(i, "pos (171)");
				break;
			case 172:
				vec.set(i, "dvbAsiIn (172)");
				break;
			case 173:
				vec.set(i, "dvbAsiOut (173)");
				break;
			case 174:
				vec.set(i, "plc (174)");
				break;
			case 175:
				vec.set(i, "nfas (175)");
				break;
			case 176:
				vec.set(i, "tr008 (176)");
				break;
			case 177:
				vec.set(i, "gr303RDT (177)");
				break;
			case 178:
				vec.set(i, "gr303IDT (178)");
				break;
			case 179:
				vec.set(i, "isup (179)");
				break;
			case 180:
				vec.set(i, "propDocsWirelessMaclayer (180)");
				break;
			case 181:
				vec.set(i, "propDocsWirelessDownstream (181)");
				break;
			case 182:
				vec.set(i, "propDocsWirelessUpstream (182)");
				break;
			case 183:
				vec.set(i, "hiperlan2 (183)");
				break;
			case 184:
				vec.set(i, "propBWAp2Mp (184)");
				break;
			case 185:
				vec.set(i, "sonetOverheadChannel (185)");
				break;
			case 186:
				vec.set(i, "digitalWrapperOverheadChannel (186)");
				break;
			case 187:
				vec.set(i, "aal2 (187)");
				break;
			case 188:
				vec.set(i, "radioMAC (188)");
				break;
			case 189:
				vec.set(i, "atmRadio (189)");
				break;
			case 190:
				vec.set(i, "imt (190)");
				break;
			case 191:
				vec.set(i, "mvl (191)");
				break;
			case 192:
				vec.set(i, "reachDSL (192)");
				break;
			case 193:
				vec.set(i, "frDlciEndPt (193)");
				break;
			case 194:
				vec.set(i, "atmVciEndPt (194)");
				break;
			case 195:
				vec.set(i, "opticalChannel (195)");
				break;
			case 196:
				vec.set(i, "opticalTransport (196)");
				break;
			case 197:
				vec.set(i, "propAtm (197)");
				break;
			case 198:
				vec.set(i, "voiceOverCable (198)");
				break;
			case 199:
				vec.set(i, "infiniband (199)");
				break;
			case 200:
				vec.set(i, "teLink (200)");
				break;
			case 201:
				vec.set(i, "q2931 (201)");
				break;
			case 202:
				vec.set(i, "virtualTg (202)");
				break;
			case 203:
				vec.set(i, "sipTg (203)");
				break;
			case 204:
				vec.set(i, "sipSig (204)");
				break;
			case 205:
				vec.set(i, "docsCableUpstreamChannel (205)");
				break;
			case 206:
				vec.set(i, "econet (206)");
				break;
			case 207:
				vec.set(i, "pon155 (207)");
				break;
			case 208:
				vec.set(i, "pon622 (208)");
				break;
			case 209:
				vec.set(i, "bridge (209)");
				break;
			case 210:
				vec.set(i, "linegroup (210)");
				break;
			case 211:
				vec.set(i, "voiceEMFGD (211)");
				break;
			case 212:
				vec.set(i, "voiceFGDEANA (212)");
				break;
			case 213:
				vec.set(i, "voiceDID (213)");
				break;
			case 214:
				vec.set(i, "mpegTransport (214)");
				break;
			case 215:
				vec.set(i, "sixToFour (215)");
				break;
			case 216:
				vec.set(i, "gtp (216)");
				break;
			case 217:
				vec.set(i, "pdnEtherLoop1 (217)");
				break;
			case 218:
				vec.set(i, "pdnEtherLoop2 (218)");
				break;
			case 219:
				vec.set(i, "opticalChannelGroup (219)");
				break;
			case 220:
				vec.set(i, "homepna (220)");
				break;
			case 221:
				vec.set(i, "gfp (221)");
				break;
			case 222:
				vec.set(i, "ciscoISLvlan (222)");
				break;
			case 223:
				vec.set(i, "actelisMetaLOOP (223)");
				break;
			case 224:
				vec.set(i, "fcipLink (224)");
				break;
			case 225:
				vec.set(i, "rpr (225)");
				break;
			case 226:
				vec.set(i, "qam (226)");
				break;
			case 227:
				vec.set(i, "lmp (227)");
				break;
			case 228:
				vec.set(i, "cblVectaStar (228)");
				break;
			case 229:
				vec.set(i, "docsCableMCmtsDownstream (229)");
				break;
			case 230:
				vec.set(i, "adsl2 (230)");
				break;
			case 231:
				vec.set(i, "macSecControlledIF (231)");
				break;
			case 232:
				vec.set(i, "macSecUncontrolledIF (232)");
				break;
			case 233:
				vec.set(i, "aviciOpticalEther (233)");
				break;
			case 234:
				vec.set(i, "atmbond (234)");
				break;
			case 235:
				vec.set(i, "voiceFGDOS (235)");
				break;
			case 236:
				vec.set(i, "mocaVersion1 (236)");
				break;
			default:
				break;
			}
		}
		return vec;
	}
	public Map cacheAndCaculateResult(MonitorTaskInfo mtaskInfo, Map result, Date date) throws DataAcquireException
	{
		String ip = mtaskInfo.getMonitorProperty(DacConst.IPADDRESS).toString();
		StringBuffer sb = new StringBuffer("TaskId: ");
		sb.append(mtaskInfo.getJobId()).append(" IP: ");
		sb.append(ip);
		mtaskInfo.cachedMessage = "";
		StringBuffer cacheMsg = new StringBuffer();
		cacheMsg.append(sb).append("\n");

		collectInterval = mtaskInfo.getGranularity();

		// Added by liyuanping at 2008-7-23
		result = reviseInterfaces(result);

		String sTaskId = String.valueOf(mtaskInfo.getJobId());
		Map map1 = (Map) resultCache.get(sTaskId);

		// 将当前采集原始数据缓存,缓存最近的一次结果
		Vector vecCollectTime = new Vector();
		vecCollectTime.add(String.valueOf(System.currentTimeMillis() / 1000));
		result.put(DacConst.m_COLLECTEDTIME, vecCollectTime);
		resultCache.put(sTaskId, result);

		// 如果不是性能任务，直接返回结果
		if (sTaskId.equals("0"))
		{
			return result;
		}

		// 取ifIndex,端口索引
		Vector ifindex0 = (Vector) result.get(DacConst.m_IFINDEX);
		if (ifindex0 == null)
		{
			sb.append(" Base monitor perfcounter: ").append("m_IFINDEX").append(" acquired null!");
			dMsg.warn(sb.toString());
			return null;
		}

		int size = ifindex0.size();
		if (size == 0)
		{
			sb.append(" Base monitor perfcounter: ").append("m_IFINDEX").append(" acquired 0!");
			dMsg.warn(sb.toString());
			return null;
		}
		cacheMsg.append("Base perfcounter size is ifindex: ").append(size).append("\n");
		// Vector ifindexOidAppend = (Vector)result.get("IFINDEXOIDAPPEND");//增加对ZTER10的处理
		// 取ifName,端口描述
		Vector ifname0 = (Vector) result.get(DacConst.m_IFNAME);
		if (ifname0 == null)
		{
			sb.append(" Get monitor perfcounter null: ").append("m_IFNAME");
			dMsg.warn(sb.toString());
			return null;
		}

		// 对该任务的第一次采集数据直接返回
		if (map1 == null)
		{
			throw new DataAcquireException(DacConst.ERRORCODE_NOTUPONDATA, "Taskid: " + sTaskId
					+ " The first data acquisition from " + ip + ", not reporting data.");
		}

		// 取系统初始化时间sysUpTime,用以判断系统是否在采集周期内重启
		sysUpTimeThis = DacUtil.toTimeticks(((Vector) result.get(DacConst.m_SYSUPTIME)).get(0).toString());
		cdTimeThis = System.currentTimeMillis() / 1000;

		// 取ifInoctets,端口流入字节数
		Vector ifinoctets0 = (Vector) result.get(DacConst.m_IFINOCTETS);
		Vector ifindiscards0 = (Vector) result.get(DacConst.m_IFINDISCARDS);
		Vector ifinerrors0 = (Vector) result.get(DacConst.m_IFINERRORS);
		Vector ifinucastpkts0 = (Vector) result.get(DacConst.m_IFINUCASTPKTS);
		Vector ifinnucastpkts0 = (Vector) result.get(DacConst.m_IFINNUCASTPKTS);
		Vector ifoutoctets0 = (Vector) result.get(DacConst.m_IFOUTOCTETS);
		Vector ifoutdiscards0 = (Vector) result.get(DacConst.m_IFOUTDISCARDS);
		Vector ifouterrors0 = (Vector) result.get(DacConst.m_IFOUTERRORS);
		Vector ifoutucastpkts0 = (Vector) result.get(DacConst.m_IFOUTUCASTPKTS);
		Vector ifoutnucastpkts0 = (Vector) result.get(DacConst.m_IFOUTNUCASTPKTS);

		// 对当前采集结果进行cacheMsg保存，在异常时分析。
		Map ret = result;
		Set keySet = ret.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			Object obj = ret.get(key);
			cacheMsg.append("current dataaq key: ").append(key).append("\n");
			if (obj instanceof Vector)
			{
				Vector vec = (Vector) obj;
				for (int j = 0, vsize = vec.size(); j < vsize; j++)
				{
					String neirong = (String) vec.get(j);
					cacheMsg.append(j).append(": ").append(neirong).append("\n");
				}
			} else
			{
				cacheMsg.append("value:").append(obj.toString()).append("\n");
			}
		}
		mtaskInfo.cachedMessage = cacheMsg.toString();

		// 确认当前采集到的各项指标的Vector大小是否与基准值相同，否则报错。对这种错误需要提前代码保证。
		if (ifinoctets0.size() != size)
		{
			sb.append("IFINOCTETS's size ").append(ifinoctets0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifindiscards0.size() != size)
		{
			sb.append("IFINDISCARDS's size ").append(ifindiscards0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifinerrors0.size() != size)
		{
			sb.append("IFINERRORS's size ").append(ifinerrors0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifinucastpkts0.size() != size)
		{
			sb.append("IFINUCASTPKTS's size ").append(ifinucastpkts0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifoutoctets0.size() != size)
		{
			sb.append("IFOUTOCTETS's size ").append(ifoutoctets0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifoutdiscards0.size() != size)
		{
			sb.append("IFOUTDISCARDS's size ").append(ifoutdiscards0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifouterrors0.size() != size)
		{
			sb.append("IFOUTERRORS's size ").append(ifouterrors0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifoutucastpkts0.size() != size)
		{
			sb.append("IFOUTUCASTPKTS's size ").append(ifoutucastpkts0.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}

		// 获取该任务上次缓存的数据。
		sysUpTimeLast = DacUtil.toTimeticks(((Vector) map1.get(DacConst.m_SYSUPTIME)).get(0).toString());
		cdTimeLast = Long.parseLong(((Vector) map1.get(DacConst.m_COLLECTEDTIME)).get(0).toString());
		Vector ifinoctets1 = (Vector) map1.get(DacConst.m_IFINOCTETS);
		Vector ifindiscards1 = (Vector) map1.get(DacConst.m_IFINDISCARDS);
		Vector ifinerrors1 = (Vector) map1.get(DacConst.m_IFINERRORS);
		Vector ifinucastpkts1 = (Vector) map1.get(DacConst.m_IFINUCASTPKTS);
		Vector ifinnucastpkts1 = (Vector) map1.get(DacConst.m_IFINNUCASTPKTS);
		Vector ifoutoctets1 = (Vector) map1.get(DacConst.m_IFOUTOCTETS);
		Vector ifoutdiscards1 = (Vector) map1.get(DacConst.m_IFOUTDISCARDS);
		Vector ifouterrors1 = (Vector) map1.get(DacConst.m_IFOUTERRORS);
		Vector ifoutucastpkts1 = (Vector) map1.get(DacConst.m_IFOUTUCASTPKTS);
		Vector ifoutnucastpkts1 = (Vector) map1.get(DacConst.m_IFOUTNUCASTPKTS);
		// 对缓存的采集结果进行cacheMsg保存，在异常时分析。
		ret = map1;
		keySet = ret.keySet();
		it = keySet.iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			Object obj = ret.get(key);
			cacheMsg.append("cached dataaq key: ").append(key).append("\n");
			if (obj instanceof Vector)
			{
				Vector vec = (Vector) obj;
				for (int j = 0, vsize = vec.size(); j < vsize; j++)
				{
					String neirong = (String) vec.get(j);
					cacheMsg.append(j).append(": ").append(neirong).append("\n");
				}
			} else
			{
				cacheMsg.append("value:").append(obj.toString()).append("\n");
			}
		}
		mtaskInfo.cachedMessage = cacheMsg.toString();
		// 保证上次缓存的数据与本次采集的Vector数据大小一致。
		if (ifinoctets1.size() != size)
		{
			sb.append("Saved IFINOCTETS's size ").append(ifinoctets1.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifindiscards1.size() != size)
		{
			sb.append("Saved IFINDISCARDS's size ").append(ifindiscards1.size()).append("while base size ")
					.append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifinerrors1.size() != size)
		{
			sb.append("Saved IFINERRORS's size ").append(ifinerrors1.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifinucastpkts1.size() != size)
		{
			sb.append("Saved IFINUCASTPKTS's size ").append(ifinucastpkts1.size()).append("while base size ").append(
					size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifoutoctets1.size() != size)
		{
			sb.append("Saved IFOUTOCTETS's size ").append(ifoutoctets1.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifoutdiscards1.size() != size)
		{
			sb.append("Saved IFOUTDISCARDS's size ").append(ifoutdiscards1.size()).append("while base size ").append(
					size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifouterrors1.size() != size)
		{
			sb.append("Saved IFOUTERRORS's size ").append(ifouterrors1.size()).append("while base size ").append(size);
			dMsg.warn(sb.toString());
			return null;
		}
		if (ifoutucastpkts1.size() != size)
		{
			sb.append("Saved IFOUTUCASTPKTS's size ").append(ifoutucastpkts1.size()).append("while base size ").append(
					size);
			dMsg.warn(sb.toString());
			return null;
		}

		// 对当前采集结果和缓存的采集结果进行差值计算。
		// 由于对IFHC*的数据是Counter64位的且是无符号的在处理时使用BigInteger进行计算，而原有Counter32仍然用long型进行计算。
		Vector ifinoctets64 = (Vector) result.get(DacConst.m_IFINOCTETS64);
		Vector ifinucastpkts64 = (Vector) result.get(DacConst.m_IFINUCASTPKTS64);
		Vector ifinmulticastpkts64 = (Vector) result.get(DacConst.m_IFINMULTICASTPKTS64);
		Vector ifinbroadcastpkts64 = (Vector) result.get(DacConst.m_IFINBROADCASTPKTS64);
		Vector ifoutoctets64 = (Vector) result.get(DacConst.m_IFOUTOCTETS64);
		Vector ifoutucastpkts64 = (Vector) result.get(DacConst.m_IFOUTUCASTPKTS64);
		Vector ifoutmulticastpkts64 = (Vector) result.get(DacConst.m_IFOUTMULTICASTPKTS64);
		Vector ifoutbroadcastpkts64 = (Vector) result.get(DacConst.m_IFOUTBROADCASTPKTS64);
		// 对当前采集结果是否是Cunter64型进行cacheMsg保存，在异常时分析。
		cacheMsg.append("ifinoctets64: ").append(ifinoctets64).append("\n");
		cacheMsg.append("ifinucastpkts64: ").append(ifinucastpkts64).append("\n");
		cacheMsg.append("ifinmulticastpkts64: ").append(ifinmulticastpkts64).append("\n");
		cacheMsg.append("ifinbroadcastpkts64: ").append(ifinbroadcastpkts64).append("\n");
		cacheMsg.append("ifoutoctets64: ").append(ifoutoctets64).append("\n");
		cacheMsg.append("ifoutucastpkts64: ").append(ifoutucastpkts64).append("\n");
		cacheMsg.append("ifoutmulticastpkts64: ").append(ifoutmulticastpkts64).append("\n");
		cacheMsg.append("ifoutbroadcastpkts64: ").append(ifoutbroadcastpkts64).append("\n");
		mtaskInfo.cachedMessage = cacheMsg.toString();

		// 计算采集周期内的实际流量。进行差值计算，由于要处理无符号Counter64位，所以依赖标志if***64是否为"true"
		// 并处理采集周期内可能出现的溢出，因为counter32,64都会在到达最大值后进行清零，这样前面计算出来的差值将会存在负值。
		// 并进行byte-->kbytes，packets-->kpackets的转换
		// 2009-02-20 修改,不做packets-->kpackets的转换
		Vector ifindiscards2 = vectorMinus32(ifindiscards0, ifindiscards1, false);
		Vector ifinerrors2 = vectorMinus32(ifinerrors0, ifinerrors1, false);
		Vector ifoutdiscards2 = vectorMinus32(ifoutdiscards0, ifoutdiscards1, false);
		Vector ifouterrors2 = vectorMinus32(ifouterrors0, ifouterrors1, false);

		Vector ifinnucastpkts2 = vectorMinus32(ifinnucastpkts0, ifinnucastpkts1, false);
		Vector ifoutnucastpkts2 = vectorMinus32(ifoutnucastpkts0, ifoutnucastpkts1, false);

		Vector ifinoctets2 = null;
		if (ifinoctets64 != null && ((String) ifinoctets64.get(0)).equalsIgnoreCase("true"))
		{
			ifinoctets2 = vectorMinus64(ifinoctets0, ifinoctets1, true);
		} else
		{
			ifinoctets2 = vectorMinus32(ifinoctets0, ifinoctets1, true);
		}
		Vector ifinucastpkts2 = null;
		if (ifinucastpkts64 != null && ((String) ifinucastpkts64.get(0)).equalsIgnoreCase("true"))
		{
			ifinucastpkts2 = vectorMinus64(ifinucastpkts0, ifinucastpkts1, false);
		} else
		{
			ifinucastpkts2 = vectorMinus32(ifinucastpkts0, ifinucastpkts1, false);
		}
		Vector ifoutoctets2 = null;
		if (ifoutoctets64 != null && ((String) ifoutoctets64.get(0)).equalsIgnoreCase("true"))
		{
			ifoutoctets2 = vectorMinus64(ifoutoctets0, ifoutoctets1, true);
		} else
		{
			ifoutoctets2 = vectorMinus32(ifoutoctets0, ifoutoctets1, true);
		}
		Vector ifoutucastpkts2 = null;
		if (ifoutucastpkts64 != null && ((String) ifoutucastpkts64.get(0)).equalsIgnoreCase("true"))
		{
			ifoutucastpkts2 = vectorMinus64(ifoutucastpkts0, ifoutucastpkts1, false);
		} else
		{
			ifoutucastpkts2 = vectorMinus32(ifoutucastpkts0, ifoutucastpkts1, false);
		}

		if (ifindiscards2 == null || ifinerrors2 == null || ifoutdiscards2 == null || ifouterrors2 == null
				|| ifinnucastpkts2 == null || ifoutnucastpkts2 == null || ifinoctets2 == null || ifinucastpkts2 == null
				|| ifoutoctets2 == null || ifoutucastpkts2 == null)
		{

			throw new DataAcquireException(DacConst.ERRORCODE_NOTUPONDATA, "Taskid: " + sTaskId
					+ " Recovery of the data acquisition from " + ip + ", not reporting first data.");
		}

		// 差值计算完成。只有流入、流出的丢包数、错误包数单位是byte,pkt/s，其他都是kbytes,kpkt/s
		HashMap newArguments = new HashMap();
		newArguments.put(DacConst.m_IFINDEX, ifindex0);
		newArguments.put(DacConst.m_IFSPEED, result.get(DacConst.m_IFSPEED));
		ifinoctets2 = DacUtil.vectorSetScale(ifinoctets2, 3);
		newArguments.put(DacConst.m_IFINOCTETS, ifinoctets2);
		newArguments.put(DacConst.m_IFINDISCARDS, ifindiscards2);
		newArguments.put(DacConst.m_IFINERRORS, ifinerrors2);
		newArguments.put(DacConst.m_IFINUCASTPKTS, ifinucastpkts2);
		newArguments.put(DacConst.m_IFINNUCASTPKTS, ifinnucastpkts2);
		ifoutoctets2 = DacUtil.vectorSetScale(ifoutoctets2, 3);
		newArguments.put(DacConst.m_IFOUTOCTETS, ifoutoctets2);
		newArguments.put(DacConst.m_IFOUTDISCARDS, ifoutdiscards2);
		newArguments.put(DacConst.m_IFOUTERRORS, ifouterrors2);
		newArguments.put(DacConst.m_IFOUTUCASTPKTS, ifoutucastpkts2);
		newArguments.put(DacConst.m_IFOUTNUCASTPKTS, ifoutnucastpkts2);

		Calculator cal = new Calculator();
		try
		{
			// 计算流入包、流出包数
			Vector inpackets = new Vector();
			Vector outpackets = new Vector();

			String arrayip[] = { "IFINUCASTPKTS", "IFINNUCASTPKTS" };
			inpackets = cal.fcalculate(newArguments, "IFINUCASTPKTS+IFINNUCASTPKTS", arrayip);
			String arrayop[] = { "IFOUTUCASTPKTS", "IFOUTNUCASTPKTS" };
			outpackets = cal.fcalculate(newArguments, "IFOUTUCASTPKTS+IFOUTNUCASTPKTS", arrayop);

			newArguments.put(DacConst.m_IFINPKTS, DacUtil.vectorSetScale(inpackets, 0));
			newArguments.put(DacConst.m_IFOUTPKTS, DacUtil.vectorSetScale(outpackets, 0));
			// 计算丢包率，误码率
			String arrayiplr[] = { "IFINDISCARDS", "IFINPKTS" };
			Vector inpacketlossratio = cal.fcalculate(newArguments, "IFINDISCARDS/IFINPKTS*100", arrayiplr);
			String arrayiper[] = { "IFINERRORS", "IFINPKTS" };
			Vector inpacketerrorratio = cal.fcalculate(newArguments, "IFINERRORS/IFINPKTS*100", arrayiper);
			String arrayoplr[] = { "IFOUTDISCARDS", "IFOUTPKTS" };
			Vector outpacketlossratio = cal.fcalculate(newArguments, "IFOUTDISCARDS/IFOUTPKTS*100", arrayoplr);
			String arrayoper[] = { "IFOUTERRORS", "IFOUTPKTS" };
			Vector outpacketerrorratio = cal.fcalculate(newArguments, "IFOUTERRORS/IFOUTPKTS*100", arrayoper);
			inpacketlossratio = DacUtil.vectorSetScale(inpacketlossratio, 2);
			inpacketerrorratio = DacUtil.vectorSetScale(inpacketerrorratio, 2);
			outpacketlossratio = DacUtil.vectorSetScale(outpacketlossratio, 2);
			outpacketerrorratio = DacUtil.vectorSetScale(outpacketerrorratio, 2);
			newArguments.put(DacConst.m_INPACKETLOSSRATIO, inpacketlossratio);
			newArguments.put(DacConst.m_INPACKETERRORRATIO, inpacketerrorratio);
			newArguments.put(DacConst.m_OUTPACKETLOSSRATIO, outpacketlossratio);
			newArguments.put(DacConst.m_OUTPACKETERRORRATIO, outpacketerrorratio);

			// 计算带宽利用率。
			StringBuffer StrBuf = new StringBuffer();
			StrBuf.append("0.8*IFINOCTETS/IFSPEED").append("/").append(collectInterval);
			String arrInbdr[] = { "IFINOCTETS", "IFSPEED" };
			Vector inUtilization = cal.fcalculate(newArguments, StrBuf.toString(), arrInbdr);// 计算流入带宽利用率
			inUtilization = DacUtil.vectorSetScale(inUtilization, 4);
			newArguments.put("INPUTUTILIZATION", inUtilization);
			StrBuf = new StringBuffer();
			StrBuf.append("0.8*IFOUTOCTETS/IFSPEED").append("/").append(collectInterval);
			String arrOutbdr[] = { "IFOUTOCTETS", "IFSPEED" };
			Vector outUtilization = cal.fcalculate(newArguments, StrBuf.toString(), arrOutbdr);// 计算流出带宽利用率
			outUtilization = DacUtil.vectorSetScale(outUtilization, 4);
			newArguments.put("OUTPUTUTILIZATION", outUtilization);

			// 计算端口流入、流出速率bps
			StringBuffer sbInRates = new StringBuffer();
			sbInRates.append("8192*IFINOCTETS/").append(collectInterval);
			String strInOctets[] = { "IFINOCTETS" };
			Vector inRates = cal.fcalculate(newArguments, sbInRates.toString(), strInOctets);// 计算流入带宽利用率
			inRates = DacUtil.vectorSetScale(inRates, 4);
			newArguments.put("IFINRATES", inRates);

			StringBuffer sbOutRates = new StringBuffer();
			sbOutRates.append("8192*IFOUTOCTETS/").append(collectInterval);
			String strOutOctets[] = { "IFOUTOCTETS" };
			Vector outRates = cal.fcalculate(newArguments, sbOutRates.toString(), strOutOctets);// 计算流入带宽利用率
			outRates = DacUtil.vectorSetScale(outRates, 4);
			newArguments.put("IFOUTRATES", outRates);

			newArguments.put(DacConst.m_IFNAME, ifname0);
			Vector ifSuccV = new Vector();
			ifSuccV.add("true");
			newArguments.put(DacConst.IFSUCCESS, ifSuccV);

			// 对返回的采集结果进行cacheMsg保存，在异常时分析。
			ret = newArguments;
			keySet = ret.keySet();
			it = keySet.iterator();
			while (it.hasNext())
			{
				String key = (String) it.next();
				Object obj = ret.get(key);
				cacheMsg.append("returned dataaq key: ").append(key).append("\n");
				if (obj instanceof Vector)
				{
					Vector vec = (Vector) obj;
					for (int j = 0, vsize = vec.size(); j < vsize; j++)
					{
						String neirong = (String) vec.get(j);
						cacheMsg.append(j).append(": ").append(neirong).append("\n");
					}
				} else
				{
					cacheMsg.append("value:").append(obj.toString()).append("\n");
				}
			}
			mtaskInfo.cachedMessage = cacheMsg.toString();

			return newArguments;
		}
		catch (MonitorException e)
		{
			dMsg.error("Calculation error." + e.getMessage());
			return null;
		}
	}
	private static Map reviseInterfaces(Map resultMap)
	{
		Vector ifInOctets = adjustResult(resultMap, DacConst.m_IFINOCTETS);
		Vector ifOutOctets = adjustResult(resultMap, DacConst.m_IFOUTOCTETS);
		Vector ifInDiscards = adjustResult(resultMap, DacConst.m_IFINDISCARDS);
		Vector ifOutDiscards = adjustResult(resultMap, DacConst.m_IFOUTDISCARDS);
		Vector ifInErrors = adjustResult(resultMap, DacConst.m_IFINERRORS);
		Vector ifOutErrors = adjustResult(resultMap, DacConst.m_IFOUTERRORS);
		Vector ifInUcastPkts = adjustResult(resultMap, DacConst.m_IFINUCASTPKTS);
		Vector ifOutUcastPkts = adjustResult(resultMap, DacConst.m_IFOUTUCASTPKTS);

		Vector ifInNUcastPkts = adjustResult(resultMap, DacConst.m_IFINNUCASTPKTS);
		Vector ifOutNUcastPkts = adjustResult(resultMap, DacConst.m_IFOUTNUCASTPKTS);
		Vector ifSpeed = adjustResult(resultMap, DacConst.m_IFSPEED);

		resultMap.put(DacConst.m_IFINOCTETS, ifInOctets);
		resultMap.put(DacConst.m_IFOUTOCTETS, ifOutOctets);
		resultMap.put(DacConst.m_IFINDISCARDS, ifInDiscards);
		resultMap.put(DacConst.m_IFOUTDISCARDS, ifOutDiscards);
		resultMap.put(DacConst.m_IFINERRORS, ifInErrors);
		resultMap.put(DacConst.m_IFOUTERRORS, ifOutErrors);
		resultMap.put(DacConst.m_IFINUCASTPKTS, ifInUcastPkts);
		resultMap.put(DacConst.m_IFOUTUCASTPKTS, ifOutUcastPkts);
		resultMap.put(DacConst.m_IFINNUCASTPKTS, ifInNUcastPkts);
		resultMap.put(DacConst.m_IFOUTNUCASTPKTS, ifOutNUcastPkts);
		resultMap.put(DacConst.m_IFSPEED, ifSpeed);

		return resultMap;
	}
	Vector vectorMinus32(Vector vec0, Vector vec1, boolean isToKunit)
	{
		int size = vec0.size();
		long lvec0 = 0;
		long lvec1 = 0;
		long lresult = 0;
		String svec0 = null;
		String svec1 = null;
		Vector result = new Vector();
		for (int i = 0; i < size; i++)
		{
			svec0 = (String) vec0.get(i);
			if (svec0.indexOf('.') != -1)
			{
				svec0 = svec0.substring(0, svec0.indexOf('.'));
			}
			svec1 = (String) vec1.get(i);
			if (svec1.indexOf('.') != -1)
			{
				svec1 = svec1.substring(0, svec1.indexOf('.'));
			}
			lvec0 = Long.parseLong(svec0);
			lvec1 = Long.parseLong(svec1);
			// 若本采集时间点的系统时间小于上一个周期采集点的系统时间,说明系统重启了,直接返回本次采集数据
			if (sysUpTimeThis < sysUpTimeLast && lvec0 < lvec1)
			{
				lresult = lvec0;
			} else
			{
				// 采集时间差△t大于采集粒度周期,说明出现了丢失数据的情况,则对恢复正常采集的第一条数据不上报处理，以避免异常数据的出现
				// 为避免采集所消耗的时间带来的潜在问题，这里加上了180s的偏移时间
				if ((cdTimeThis - cdTimeLast) > (collectInterval + 180))
				{
					return null;
				} else
				{
					lresult = lvec0 - lvec1;
					if (lresult < 0)
					{
						lresult = lresult + DacConst.TWO_OF32POWER;// 增加2^32-1，因为端口流量为counter32类型。
					}
				}
			}
			if (isToKunit)
			{
				double fval = lresult / 1024.0;// bytes->kbytes
				result.add(Double.toString(fval));
			} else
			{
				result.add(Double.toString((double) lresult));
			}
		}
		return result;
	}
	
	Vector vectorMinus64(Vector vec0, Vector vec1, boolean isToKunit)
	{
		int size = vec0.size();
		BigInteger lvec0 = null;
		BigInteger lvec1 = null;
		BigInteger lresult = null;
		Vector result = new Vector();
		for (int i = 0; i < size; i++)
		{
			String svec0 = (String) vec0.get(i);
			String svec1 = (String) vec1.get(i);
			if (svec0.startsWith("0x") || svec0.startsWith("0X"))
			{
				lvec0 = new BigInteger(svec0.substring(2), 16);
			} else
			{
				lvec0 = new BigInteger(svec0);
			}

			if (svec1.startsWith("0x") || svec1.startsWith("0X"))
			{
				lvec1 = new BigInteger(svec1.substring(2), 16);
			} else
			{
				lvec1 = new BigInteger(svec1);
			}
			// 1、若本采集时间点的系统时间小于上一个周期采集点的系统时间,并且本次采集数据小于上一次，说明系统重启了,直接返回本次采集数据
			// 2、若本采集时间点的系统时间小于上一个周期采集点的系统时间,并且本次采集数据大于上一次，说明时间计数器归0,不能直接返回本次采集数据
			if (sysUpTimeThis < sysUpTimeLast && lvec0.compareTo(lvec1) < 0)
			{
				lresult = lvec0;
			} else
			{
				// 采集时间差△t大于采集粒度周期,说明出现了丢失数据的情况,则对恢复正常采集的第一条数据不上报处理，以避免异常数据的出现
				// 为避免采集所消耗的时间带来的潜在问题，这里加上了180s的偏移时间
				if ((cdTimeThis - cdTimeLast) > (collectInterval + 180))
				{
					return null;
				} else
				{
					lresult = lvec0.subtract(lvec1);
					if (lresult.signum() < 0)
					{
						lresult = lresult.add(new BigInteger("18446744073709551615"));// 增加2^64-1，因为IFHC*为counter64类型。
						// 处理假64位计数器如ZTE数据设备
						// 与(2^64-2^32)比较，如果大于说明是假64位真32位的数值
						if (lresult.compareTo(new BigInteger("18446744069414584320")) >= 0)
						{
							lresult = lresult.subtract(new BigInteger("18446744073709551615"));
							lresult = lresult.add(new BigInteger("4294967295"));
						}
					}
				}
			}
			if (isToKunit)
			{
				double fval = lresult.doubleValue() / 1024.0;// bytes->kbytes
				result.add(Double.toString(fval));
			} else
			{
				result.add(Double.toString(lresult.doubleValue()));
			}
		}
		return result;
	}
	
	public void treatIfHCOctets(Map resultMap)
	{
		// 对端口返回的ifInOctets/ifOutOctets/ifInUcastPkts/ifOutUcastPkts/ifinMulticastPkts/ifOutMulticastPkts
		// ifinBroadcastPkts/ifOutBroadcastPkts与相应的ifHC开头的指标进行比较如果ifHC返回数据不为空，则以ifHC为准
		// 因为ifHC*是Counter64类型最大2^64-1,而if*是Counter32类型最大2^32-1，对高速设备每5分钟采集粒度应以ifHC计算。
		Vector ifInOctets = (Vector) resultMap.get("IFINOCTETS");
		Vector ifOutOctets = (Vector) resultMap.get("IFOUTOCTETS");
		Vector ifInUcastPkts = (Vector) resultMap.get("IFINUCASTPKTS");
		Vector ifOutUcastPkts = (Vector) resultMap.get("IFOUTUCASTPKTS");
		Vector ifInMulticastPkts = (Vector) resultMap.get("IFINMULTICASTPKTS");
		Vector ifOutMulticastPkts = (Vector) resultMap.get("IFOUTMULTICASTPKTS");
		Vector ifInBroadcastPkts = (Vector) resultMap.get("IFINBROADCASTPKTS");
		Vector ifOutBroadcastPkts = (Vector) resultMap.get("IFOUTBROADCASTPKTS");
		Vector ifHCInOctets = (Vector) resultMap.get("IFHCINOCTETS");
		Vector ifHCOutOctets = (Vector) resultMap.get("IFHCOUTOCTETS");
		Vector ifHCInUcastPkts = (Vector) resultMap.get("IFHCINUCASTPKTS");
		Vector ifHCOutUcastPkts = (Vector) resultMap.get("IFHCOUTUCASTPKTS");
		Vector ifHCInMulticastPkts = (Vector) resultMap.get("IFHCINMULTICASTPKTS");
		Vector ifHCOutMulticastPkts = (Vector) resultMap.get("IFHCOUTMULTICASTPKTS");
		Vector ifHCInBroadcastPkts = (Vector) resultMap.get("IFHCINBROADCASTPKTS");
		Vector ifHCOutBroadcastPkts = (Vector) resultMap.get("IFHCOUTBROADCASTPKTS");
		
		Vector<String> right = new Vector<String>();
		right.add("true");
		
		if (ifHCInOctets.size() == ifInOctets.size())
		{
			// 说明RFC1213返回端口数与RFC2233返回端口数一致，此时应以RFC2233为准。这是标准情况
			resultMap.put("IFINOCTETS", ifHCInOctets);
			resultMap.put(DacConst.m_IFINOCTETS64, right);
		} else if (ifHCInOctets.size() != ifInOctets.size())
		{
			// 说明RFC2233返回端口数大于RFC1213，ZXR10会出现这种情况，需将RFC2233的多返回的端口删除。以处理过的RFC2233为准
			ifHCInOctets = ZXR10Treatment(ifHCInOctets, ifInOctets, resultMap, "IFINOCTETSOIDAPPEND", "IFHCINOCTETSOIDAPPEND");
			resultMap.put("IFINOCTETS", ifHCInOctets);
			resultMap.put(DacConst.m_IFINOCTETS64, right);
		}
		if (ifHCOutOctets.size() == ifOutOctets.size())
		{
			resultMap.put("IFOUTOCTETS", ifHCOutOctets);
			resultMap.put(DacConst.m_IFOUTOCTETS64, right);
		} else if (ifHCOutOctets.size() != ifOutOctets.size())
		{
			ifHCOutOctets = ZXR10Treatment(ifHCOutOctets, ifOutOctets, resultMap, "IFOUTOCTETSOIDAPPEND", "IFHCOUTOCTETSOIDAPPEND");
			resultMap.put("IFOUTOCTETS", ifHCOutOctets);
			resultMap.put(DacConst.m_IFOUTOCTETS64, right);
		}
		if (ifHCInUcastPkts.size() == ifInUcastPkts.size())
		{
			resultMap.put("IFINUCASTPKTS", ifHCInUcastPkts);
			resultMap.put(DacConst.m_IFINUCASTPKTS64, right);
		} else if (ifHCInUcastPkts.size() != ifInUcastPkts.size())
		{
			ifHCInUcastPkts = ZXR10Treatment(ifHCInUcastPkts, ifInUcastPkts, resultMap, "IFINUCASTPKTSOIDAPPEND",
					"IFHCINUCASTPKTSOIDAPPEND");
			resultMap.put("IFINUCASTPKTS", ifHCInUcastPkts);
			resultMap.put(DacConst.m_IFINUCASTPKTS64, right);
		}
		if (ifHCOutUcastPkts.size() == ifOutUcastPkts.size())
		{
			resultMap.put("IFOUTUCASTPKTS", ifHCOutUcastPkts);
			resultMap.put(DacConst.m_IFOUTUCASTPKTS64, right);
		} else if (ifHCOutUcastPkts.size() != ifOutUcastPkts.size())
		{
			ifHCOutUcastPkts = ZXR10Treatment(ifHCOutUcastPkts, ifOutUcastPkts, resultMap, "IFOUTUCASTPKTSOIDAPPEND",
					"IFHCOUTUCASTPKTSOIDAPPEND");
			resultMap.put("IFOUTUCASTPKTS", ifHCOutUcastPkts);
			resultMap.put(DacConst.m_IFOUTUCASTPKTS64, right);
		}
		if (ifHCInMulticastPkts.size() == ifInMulticastPkts.size())
		{
			resultMap.put("IFINMULTICASTPKTS", ifHCInMulticastPkts);
			resultMap.put(DacConst.m_IFINMULTICASTPKTS64, right);
		} else if (ifHCInMulticastPkts.size() != ifInMulticastPkts.size())
		{
			ifHCInMulticastPkts = ZXR10Treatment(ifHCInMulticastPkts, ifInMulticastPkts, resultMap, "IFINMULTICASTPKTSOIDAPPEND",
					"IFHCINMULTICASTPKTSOIDAPPEND");
			resultMap.put("IFINMULTICASTPKTS", ifHCInMulticastPkts);
			resultMap.put(DacConst.m_IFINMULTICASTPKTS64, right);
		}
		if (ifHCOutMulticastPkts.size() == ifOutMulticastPkts.size())
		{
			resultMap.put("IFOUTMULTICASTPKTS", ifHCOutMulticastPkts);
			resultMap.put(DacConst.m_IFOUTMULTICASTPKTS64, right);
		} else if (ifHCOutMulticastPkts.size() != ifOutMulticastPkts.size())
		{
			ifHCOutMulticastPkts = ZXR10Treatment(ifHCOutMulticastPkts, ifOutMulticastPkts, resultMap, "IFOUTMULTICASTPKTSOIDAPPEND",
					"IFHCOUTMULTICASTPKTSOIDAPPEND");
			resultMap.put("IFOUTMULTICASTPKTS", ifHCOutMulticastPkts);
			resultMap.put(DacConst.m_IFOUTMULTICASTPKTS64, right);
		}
		if (ifHCInBroadcastPkts.size() == ifInBroadcastPkts.size())
		{
			resultMap.put("IFINBROADCASTPKTS", ifHCInBroadcastPkts);
			resultMap.put(DacConst.m_IFINBROADCASTPKTS64, right);
		} else if (ifHCInBroadcastPkts.size() != ifInBroadcastPkts.size())
		{
			ifHCInBroadcastPkts = ZXR10Treatment(ifHCInBroadcastPkts, ifInBroadcastPkts, resultMap, "IFINBROADCASTPKTSOIDAPPEND",
					"IFHCINBROADCASTPKTSOIDAPPEND");
			resultMap.put("IFINBROADCASTPKTS", ifHCInBroadcastPkts);
			resultMap.put(DacConst.m_IFINBROADCASTPKTS64, right);
		}
		if (ifHCOutBroadcastPkts.size() == ifOutBroadcastPkts.size())
		{
			resultMap.put("IFOUTBROADCASTPKTS", ifHCOutBroadcastPkts);
			resultMap.put(DacConst.m_IFOUTBROADCASTPKTS64, right);
		} else if (ifHCOutBroadcastPkts.size() != ifOutBroadcastPkts.size())
		{
			ifHCOutBroadcastPkts = ZXR10Treatment(ifHCOutBroadcastPkts, ifOutBroadcastPkts, resultMap, "IFOUTBROADCASTPKTSOIDAPPEND",
					"IFHCOUTBROADCASTPKTSOIDAPPEND");
			resultMap.put("IFOUTBROADCASTPKTS", ifHCOutBroadcastPkts);
			resultMap.put(DacConst.m_IFOUTBROADCASTPKTS64, right);
		}
	}
	
    private static Vector ZXR10Treatment(Vector ifHCRFC2233, Vector ifRFC1213, Map resultMap, String RFC1213, String RFC2233)
    {
        // 因为对ZXR10设备RFC2233返回的端口数不等于RFC1213返回的端口数，这里应当以RFC1213的端口数为准
        Vector ifRFC1213OidAppend = (Vector) resultMap.get(RFC1213);
        Vector ifHCRFC2233OidAppend = (Vector) resultMap.get(RFC2233);
        Vector tmpifHCRFC2233 = new Vector();
        // 处理返回增加的OID，将其认为字符串，需要匹配（不区分大小写！！！）
        int iL = ifRFC1213OidAppend.size();
        int iH = ifHCRFC2233OidAppend.size();

        Map mHTemp = new HashMap();
        Map mLTemp = new HashMap();
        Object tmpOidAppend = null;
        Object tmpOid2Value = null;
        String tmpRes;
        for (int i = 0; i < iL; i++)
        {
            mLTemp.put(ifRFC1213OidAppend.get(i), ifRFC1213.get(i));
        }
        for (int i = 0; i < iH; i++)
        {
            mHTemp.put(ifHCRFC2233OidAppend.get(i), ifHCRFC2233.get(i));
        }
        for (int j = 0; j < iL; j++)
        {
            tmpOidAppend = ifRFC1213OidAppend.get(j);
            tmpOid2Value = mHTemp.get(tmpOidAppend);
            if (tmpOid2Value != null)
            {
                tmpifHCRFC2233.add(j, tmpOid2Value);
            }
            else if (iL > iH)
            {
                tmpifHCRFC2233.add(j, mLTemp.get(tmpOidAppend));
            }
        }
        return tmpifHCRFC2233;
    }
}
