/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.cdp.socket;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientDiskInfo;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ClientMirroredPartitionInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.ClientPartitionInfo;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_MIRROR_DISKS;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_SOURCE_DISKS;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_SOURCE_DISK_DETAIL;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_SOURCE_PARTITION_DETAIL;
import com.marstor.msa.cdp.socket.action.CDP_RESPONSE;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.util.Utility;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
public class ClientAPI {
    private static int m_nErrorCode = -1;
    
    private static String m_strError = "";
    private static boolean m_bDebug = false;
    
    public static int GetErrorCode()
    {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "获取数据失败,错误码:" + m_nErrorCode, ""));
        Debug.print("Get CDP Data Failed! Error Code:" + m_nErrorCode + " Details:" + m_strError);
        return m_nErrorCode;
    }
    
    public static String GetErrorString()
    {
        return m_strError;
    }   
    
    public static ClientDiskInfo[] GetSourceDisksInfo(ClientInfo client)
    {
        
        System.out.println("Call CDPClientAPI.GetSourceDisksInfo.");
        XMLConstructor xmlGetSourceDisks = new XMLConstructor(CommandID.GET_SOURCE_DISKS_ID, CommandID.GET_SOURCE_DISKS_STR, "MarsCDP", "MarsCDP Console");
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return null;
        }
        if (!clientSock.sendData(xmlGetSourceDisks.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return null;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(clientSock.getError());
        System.out.println(xmlRet);

        long lStartTime = new Date().getTime();        
                
        //parse response
        CDPRES_GET_SOURCE_DISKS CDPRes = new CDPRES_GET_SOURCE_DISKS();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return null;
        }

        long lEndTime = new Date().getTime();
        System.out.println("Total Time: " + String.valueOf(lEndTime - lStartTime));

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return null;
        }
        
        System.out.println( clientSock.m_isTimeOut);  
        return CDPRes.lpDiskInfo;
    }
    
    public static ClientMirroredDiskInfo[] GetMirrorDisksInfo(ClientInfo client)
    {
        XMLConstructor xmlGetMirrorDisks = new XMLConstructor(CommandID.GET_MIRROR_DISKS_ID, CommandID.GET_MIRROR_DISKS_STR, "MarsCDP", "MarsCDP Console");
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return null;
        }
        if (!clientSock.sendData(xmlGetMirrorDisks.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return null;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDPRES_GET_MIRROR_DISKS CDPRes = new CDPRES_GET_MIRROR_DISKS();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return null;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return null;
        }
        return CDPRes.lpDiskInfo;
    }
    
    public static boolean CreateCDP(int nCDPType, int nSourceDiskNumber,
            int nSourcePartitionNumber, int nMirrorDiskNumber, 
            int nMirrorPartitionNumber, boolean bUseCache, ClientInfo client)
    {
        XMLConstructor xmlCreateCDP = new XMLConstructor(CommandID.CREATE_CDP_ID, CommandID.CREATE_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlCreateCDP.addNode("CDPType", nCDPType);
        xmlCreateCDP.addNode("SourceDiskNumber", nSourceDiskNumber);
        xmlCreateCDP.addNode("SourcePartitionNumber", nSourcePartitionNumber);
        xmlCreateCDP.addNode("TargetDiskNumber", nMirrorDiskNumber);
        xmlCreateCDP.addNode("TargetPartitionNumber", nMirrorPartitionNumber);
        xmlCreateCDP.addNode("UseCache", bUseCache?"1":"0");

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
        if (!clientSock.sendData(xmlCreateCDP.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);

        //parse response
        CDP_RESPONSE CDPRes = new CDP_RESPONSE();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return false;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return false;
        }
        return true;
    }
    
    public static boolean DeleteCDP(int nCDPType, int nDiskNumber, int nPartitionNumber, ClientInfo client)
    {
        XMLConstructor xmlDeleteCDP = new XMLConstructor(CommandID.DELETE_CDP_ID, CommandID.DELETE_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlDeleteCDP.addNode("CDPType", nCDPType);
        xmlDeleteCDP.addNode("DiskNumber", nDiskNumber);
        xmlDeleteCDP.addNode("PartitionNumber", nPartitionNumber);

       CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
        if (!clientSock.sendData(xmlDeleteCDP.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDP_RESPONSE CDPRes = new CDP_RESPONSE();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return false;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return false;
        }
        return true;
    }
    
    public static boolean StartCDPSyn(int nCDPType, int mode, int auto, int fast,  ClientInfo client, int dNum, int pNum)
    {        
                System.out.println("StartCDPSyn pNum="+pNum);

        XMLConstructor xmlStartCDP = new XMLConstructor(CommandID.START_CDP_SYN_ID, CommandID.START_CDP_SYN_STR, "MarsCDP", "MarsCDP Console");
        xmlStartCDP.addNode("CDPType", nCDPType);
        xmlStartCDP.addNode("SyncMode", mode);
        xmlStartCDP.addNode("AutoStart", auto);
        xmlStartCDP.addNode("NTFSMode", fast);
        xmlStartCDP.addNode("DiskNumber", dNum);
        xmlStartCDP.addNode("PartitionNumber", pNum);

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
        Date date = new Date();
        System.out.println(date.toString());
        System.out.println(xmlStartCDP.toXmlString());
        if (!clientSock.sendData(xmlStartCDP.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        date = new Date();
        System.out.println(date.toString());
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDP_RESPONSE CDPRes = new CDP_RESPONSE();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return false;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return false;
        }
        return true;
    }
    
    public static boolean StartCDP(int nCDPType, int nDiskNumber, int nPartitionNumber, ClientInfo client)
    {
        XMLConstructor xmlStartCDP = new XMLConstructor(CommandID.START_CDP_ID, CommandID.START_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlStartCDP.addNode("CDPType", nCDPType);
        xmlStartCDP.addNode("DiskNumber", nDiskNumber);
        xmlStartCDP.addNode("PartitionNumber", nPartitionNumber);

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
        if (!clientSock.sendData(xmlStartCDP.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDP_RESPONSE CDPRes = new CDP_RESPONSE();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return false;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return false;
        }
        return true;
    }
    
    public static boolean StopCDP(int nCDPType, int nDiskNumber, int nPartitionNumber, ClientInfo client)
    {
        XMLConstructor xmlStopCDP = new XMLConstructor(CommandID.STOP_CDP_ID, CommandID.STOP_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlStopCDP.addNode("CDPType", nCDPType);
        xmlStopCDP.addNode("DiskNumber", nDiskNumber);
        xmlStopCDP.addNode("PartitionNumber", nPartitionNumber);

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
        System.out.println(xmlStopCDP.toXmlString());
        if (!clientSock.sendData(xmlStopCDP.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDP_RESPONSE CDPRes = new CDP_RESPONSE();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return false;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return false;
        }
        return true;
    }
        
    public static ClientDiskDetail GetSourceDiskDetail(int nDiskNumber, ClientInfo client)
    {
        XMLConstructor xmlGetDiskDetail = new XMLConstructor(CommandID.GET_SOURCE_DISK_DETAIL_ID, CommandID.GET_SOURCE_DISK_DETAIL_STR, "MarsCDP", "MarsCDP Console");
        xmlGetDiskDetail.addNode("DiskNumber", nDiskNumber);

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {                              
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return null;
        }
        if (!clientSock.sendData(xmlGetDiskDetail.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return null;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDPRES_GET_SOURCE_DISK_DETAIL CDPRes = new CDPRES_GET_SOURCE_DISK_DETAIL();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return null;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return null;
        }
        return CDPRes.DiskDetail;
    }

    public static ClientPartitionDetail GetSourcePartitionDetail(int nDiskNumber, int nPartitionNumber, ClientInfo client)
    {
        XMLConstructor xmlGetPartitionDetail = new XMLConstructor(CommandID.GET_SOURCE_PARTITION_DETAIL_ID, CommandID.GET_SOURCE_PARTITION_DETAIL_STR, "MarsCDP", "MarsCDP Console");
        xmlGetPartitionDetail.addNode("DiskNumber", nDiskNumber);
        xmlGetPartitionDetail.addNode("PartitionNumber", nPartitionNumber);

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return null;
        }
        if (!clientSock.sendData(xmlGetPartitionDetail.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return null;
        }
        
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDPRES_GET_SOURCE_PARTITION_DETAIL CDPRes = new CDPRES_GET_SOURCE_PARTITION_DETAIL();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return null;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return null;
        }
        return CDPRes.PartitionDetail;
    }
    
    public static boolean RestoreData(int nRestoreType, int fast, int nSourceDiskNumber,
            int nSourcePartitionNumber, int nTargetDiskNumber, int nTargetPartitionNumber, ClientInfo client)
    {
       
        XMLConstructor xmlRestoreData = new XMLConstructor(CommandID.RESTORE_DATA_ID, CommandID.RESTORE_DATA_STR, "MarsCDP", "MarsCDP Console");
        xmlRestoreData.addNode("RestoreType", nRestoreType);
        xmlRestoreData.addNode("NTFSMode", fast);
        xmlRestoreData.addNode("SourceDiskNumber", nSourceDiskNumber);
        xmlRestoreData.addNode("SourcePartitionNumber", nSourcePartitionNumber);
        xmlRestoreData.addNode("TargetDiskNumber", nTargetDiskNumber);
        xmlRestoreData.addNode("TargetPartitionNumber", nTargetPartitionNumber);
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }        
        
        Date date = new Date();
        System.out.println(date.toString());
        System.out.println(xmlRestoreData.toXmlString());
        if (!clientSock.sendData(xmlRestoreData.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        
        date = new Date();
        System.out.println(date.toString());
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);

        //parse response
        CDP_RESPONSE CDPRes = new CDP_RESPONSE();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return false;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return false;
        }
        return true;
    }
    
    public static boolean CancelRestore(int nRestoreType, int nDiskNumber, int nPartitionNumber, ClientInfo client)
    {
        XMLConstructor xmlCancelRestore = new XMLConstructor(CommandID.CANCEL_RESTORE_ID, CommandID.CANCEL_RESTORE_STR, "MarsCDP", "MarsCDP Console");
        xmlCancelRestore.addNode("RestoreType", nRestoreType);
        xmlCancelRestore.addNode("SourceDiskNumber", nDiskNumber);
        xmlCancelRestore.addNode("SourcePartitionNumber", nPartitionNumber);
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
        
        if (!clientSock.sendData(xmlCancelRestore.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        System.out.println(xmlCancelRestore.toXmlString());
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDP_RESPONSE CDPRes = new CDP_RESPONSE();
        CDPRes.setXmlCmd(xmlRet);
        if (!CDPRes.ParseResponse())
        {
            m_nErrorCode = CDPConstants.SYSERROR_PARSE_XML;
            m_strError = CDPRes.getError();
            return false;
        }

        if(CDPRes.nStatus != CDPConstants.ERROR_SUCCESS)
        {
            m_nErrorCode = (int)CDPRes.nStatus;
            m_strError = "Execute command failed. Status=" + CDPRes.nStatus 
                    + " Error=" + Utility.getErrorString((int)CDPRes.nStatus)
                    + " ErrorMessage=" + CDPRes.strErrorMessage;
            return false;
        }
        return true;
    }
    
    public static ClientDiskInfo GetSourceDiskInfoByNumber(int nDiskNumber, 
            ClientDiskInfo[] DiskInfos)
    {
        for(int i=0;i<DiskInfos.length;i++) {
            ClientDiskInfo DiskInfo = DiskInfos[i];
            if(DiskInfo.DiskNumber == nDiskNumber)
                return DiskInfo;
        }
        
        return null;
    }

    public static ClientPartitionInfo GetSourcePartitionInfoByNumber(int nDiskNumber,
            int nPartitionNumber, ClientDiskInfo[] DiskInfos)
    {
        for(int i=0;i<DiskInfos.length;i++) {
            ClientDiskInfo DiskInfo = DiskInfos[i];

            for (int j = 0; j < DiskInfo.PartitionCount; j++) 
            {
                ClientPartitionInfo PartitionInfo = DiskInfo.PartitionInfos[j];
                if (PartitionInfo.DiskNumber == nDiskNumber
                        && PartitionInfo.PartitionNumber == nPartitionNumber) {
                    return PartitionInfo;
                }
            }
        }
        
        return null;
    }
    
    public static int GetTotalSourcePartitionCount(ClientDiskInfo[] DiskInfos)
    {
        int nPartitionCnt = 0;
        for(int i=0;i<DiskInfos.length;i++) {
            ClientDiskInfo DiskInfo = DiskInfos[i];
            nPartitionCnt += DiskInfo.PartitionCount;
        }
        
        return nPartitionCnt;
    }
}
