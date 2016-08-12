package org.openo.orchestrator.nfv.dac.snmptrap.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MibUtil {
	private static MibNodeInfo nodeInfo = new MibNodeInfo();
	private static Set<String> nodesOids = new HashSet<String>();
    public static MibNodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public static void setNodeInfo(MibNodeInfo newNodeInfo) {
		nodeInfo = newNodeInfo;
		nodesOids = nodeInfo.getNodesMap().keySet();
	}

	public static void addNodeInfo(String strOid, String strLable)
    {
		nodesOids.add(strOid);
		nodeInfo.getNodesMap().put(strOid, strLable);
    }

    public static String getNodeLable(String strOid)
    {
    	String lable = nodeInfo.getNodesMap().get(strOid);
    	if (lable == null)
    	{
    		lable = nodeInfo.getNodesMap().get(processOid(strOid));
    		if (lable == null)
    		{
    	        String realOid = getRealOid(strOid);
    	        if (realOid != null)
    	        {
    	        	lable = nodeInfo.getNodesMap().get(realOid);
    	        }
    	        else
    	        {
    	        	lable = strOid;
    	        }
    		}
    	}
    	return lable;

    }
    
    /**
     * if .1.3.6.1.4.1.89.35.1.40.19 get nothing, use .1.3.6.1.4.1.89.35.1.40 to query
     */
    public static String processOid(String oid)
    {
        int pos = oid.lastIndexOf(".");
        return oid.substring(0, pos);
    }

    private static String getRealOid(String strOid)
    {
        String preOid = null;
        Iterator<String> iter = nodesOids.iterator();
        while (iter.hasNext())
        {
            String exitOid = iter.next().toString();
            if ((strOid + ".").indexOf(exitOid + ".") != -1)
            {
                preOid = exitOid;
                break;
            }
        }
        return preOid;
    }

    public static boolean isEmpty()
    {
        return nodeInfo.getNodesMap().isEmpty();
    }
      
    public static void setLoadedMibModule(String strLoadedMib)
    {
    	nodeInfo.getLoadedMibModule().add(strLoadedMib);
    }

    public static  ArrayList<String>  getLoadedMibModule()
    {
        return nodeInfo.getLoadedMibModule();
    }
}
