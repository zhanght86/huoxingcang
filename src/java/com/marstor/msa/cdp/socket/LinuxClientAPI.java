/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.cdp.socket;

import com.marstor.msa.cdp.model.ClientDiskInfo;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ClientMirroredPartitionInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.ClientPartitionInfo;
import com.marstor.msa.cdp.model.LinuxClientDiskDetail;
import com.marstor.msa.cdp.model.LinuxClientDiskInfo;
import com.marstor.msa.cdp.model.LinuxClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.LinuxClientMirroredVGInfo;
import com.marstor.msa.cdp.model.LinuxClientPartitionDetail;
import com.marstor.msa.cdp.model.LinuxClientPartitionInfo;
import com.marstor.msa.cdp.model.LinuxVolumeDetail;
import com.marstor.msa.cdp.model.LinuxVolumeGroupInfo;
import com.marstor.msa.cdp.model.LinuxVolumeInfo;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_LINUX_MIRROR_DISKS;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_LINUX_MIRROR_VGS;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_LINUX_SOURCE_DISKS;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_LINUX_SOURCE_DISK_DETAIL;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_LINUX_SOURCE_PARTITION_DETAIL;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_LINUX_VGS;
import com.marstor.msa.cdp.socket.action.CDPRES_GET_LINUX_VOLUME_DETAIL;
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
public class LinuxClientAPI {
    private static int m_nErrorCode = -1;
    
    private static String m_strError = "";
    private static boolean m_bDebug = false;
    
    public static void SetDebug(boolean bDebug)
    {
        m_bDebug = bDebug;
        
    }
    
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
    
    public static LinuxClientDiskInfo[] GetSourceDisksInfo(ClientInfo client)
    {
        
        System.out.println("Call LinuxLinuxCDPClientAPI.GetSourceDisksInfo.");
        
//        if (m_bDebug)
//            return Debug_GetSourceDisksInfo();
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
        CDPRES_GET_LINUX_SOURCE_DISKS CDPRes = new CDPRES_GET_LINUX_SOURCE_DISKS();
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
    
    public static LinuxVolumeGroupInfo[] GetVGsInfo(ClientInfo client)
    {
        
        System.out.println("Call LinuxCDPClientAPI.GetVGsInfo.");
        
//        if (m_bDebug)
//            return Debug_GetSourceDisksInfo();
        XMLConstructor xmlGetSourceDisks = new XMLConstructor(CommandID.GET_VGS_ID, CommandID.GET_VGS_STR, "MarsCDP", "MarsCDP Console");
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
        CDPRES_GET_LINUX_VGS CDPRes = new CDPRES_GET_LINUX_VGS();
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
    
    public static LinuxClientMirroredDiskInfo[] GetMirrorDisksInfo(ClientInfo client)
    {
//        if(m_bDebug)
//            return Debug_GetMirrorDisksInfo();
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
        CDPRES_GET_LINUX_MIRROR_DISKS CDPRes = new CDPRES_GET_LINUX_MIRROR_DISKS();
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
    
    public static LinuxClientMirroredVGInfo[] GetMirrorVGsInfo(ClientInfo client)
    {
//        if(m_bDebug)
//            return Debug_GetMirrorDisksInfo();
        XMLConstructor xmlGetMirrorDisks = new XMLConstructor(CommandID.GET_MIRROR_VGS_ID, CommandID.GET_MIRROR_VGS_STR, "MarsCDP", "MarsCDP Console");
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
        CDPRES_GET_LINUX_MIRROR_VGS CDPRes = new CDPRES_GET_LINUX_MIRROR_VGS();
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
    
    public static boolean CreateCDP(int nCDPType, String nSourceDiskNumber,
            String nSourcePartitionNumber, String nMirrorDiskNumber, 
            String nMirrorPartitionNumber, boolean bUseCache, ClientInfo client)
    {
//        if (m_bDebug)
//            return Debug_CreateCDP(nCDPType, nSourceDiskNumber, nSourcePartitionNumber,
//                    nMirrorDiskNumber, nMirrorPartitionNumber, bUseCache);
//        
        /*if(true)
        {
            m_nErrorCode = CDPConstants.ERROR_RESTORE_INVALID_SOURCE_HOOK_STATUS;
            return false;
        }*/
        XMLConstructor xmlCreateCDP = new XMLConstructor(CommandID.CREATE_CDP_ID, CommandID.CREATE_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlCreateCDP.addNode("CDPType", nCDPType);
        xmlCreateCDP.addNode("SourceDisk", nSourceDiskNumber);
        xmlCreateCDP.addNode("SourcePartition", nSourcePartitionNumber);
        xmlCreateCDP.addNode("TargetDisk", nMirrorDiskNumber);
        xmlCreateCDP.addNode("TargetPartition", nMirrorPartitionNumber);
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
        System.out.println(xmlCreateCDP.toXmlString());
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
    
    public static boolean ScanDisks(ClientInfo client)
    {
        XMLConstructor xmlCreateCDP = new XMLConstructor(CommandID.SCAN_DISKS_ID, CommandID.SCAN_DISKS_STR, "MarsCDP", "MarsCDP Console");

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
        System.out.println(xmlCreateCDP.toXmlString());
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
    
    public static boolean CreateVCDP(int nCDPType, String nSourceDiskNumber,
            String nSourcePartitionNumber, String nMirrorDiskNumber, 
            String nMirrorPartitionNumber, boolean bUseCache, ClientInfo client)
    {
//        if (m_bDebug)
//            return Debug_CreateCDP(nCDPType, nSourceDiskNumber, nSourcePartitionNumber,
//                    nMirrorDiskNumber, nMirrorPartitionNumber, bUseCache);
        
        /*if(true)
        {
            m_nErrorCode = CDPConstants.ERROR_RESTORE_INVALID_SOURCE_HOOK_STATUS;
            return false;
        }*/
        XMLConstructor xmlCreateCDP = new XMLConstructor(CommandID.CREATE_CDP_ID, CommandID.CREATE_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlCreateCDP.addNode("CDPType", nCDPType);
        xmlCreateCDP.addNode("SourceVolumeGroup", nSourceDiskNumber);
        xmlCreateCDP.addNode("SourceVolume", nSourcePartitionNumber);
        xmlCreateCDP.addNode("TargetDisk", nMirrorDiskNumber);
        xmlCreateCDP.addNode("TargetPartition", nMirrorPartitionNumber);
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
        System.out.println(xmlCreateCDP.toXmlString());
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
    
    public static boolean ScanVolumes(ClientInfo client)
    {
        XMLConstructor xmlCreateCDP = new XMLConstructor(CommandID.SCAN_VOLUMES_ID, CommandID.SCAN_VOLUMES_STR, "MarsCDP", "MarsCDP Console");

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
        System.out.println(xmlCreateCDP.toXmlString());
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
    
    public static boolean DeleteCDP(int nCDPType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug)
//            return Debug_DeleteCDP(nCDPType, nDiskNumber, nPartitionNumber);
        XMLConstructor xmlDeleteCDP = new XMLConstructor(CommandID.DELETE_CDP_ID, CommandID.DELETE_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlDeleteCDP.addNode("CDPType", nCDPType);
        xmlDeleteCDP.addNode("DiskDevice", nDiskNumber);
        xmlDeleteCDP.addNode("PartitionDevice", nPartitionNumber);

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
    
    public static boolean DeleteVCDP(int nCDPType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug)
//            return Debug_DeleteCDP(nCDPType, nDiskNumber, nPartitionNumber);
        XMLConstructor xmlDeleteCDP = new XMLConstructor(CommandID.DELETE_CDP_ID, CommandID.DELETE_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlDeleteCDP.addNode("CDPType", nCDPType);
        xmlDeleteCDP.addNode("VolumeGroupName", nDiskNumber);
        xmlDeleteCDP.addNode("VolumeName", nPartitionNumber);

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
    
    public static boolean StartCDP(int nCDPType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_StartCDP(nCDPType, nDiskNumber, nPartitionNumber);
        XMLConstructor xmlStartCDP = new XMLConstructor(CommandID.START_CDP_ID, CommandID.START_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlStartCDP.addNode("CDPType", nCDPType);
        xmlStartCDP.addNode("DiskDevice", nDiskNumber);
        xmlStartCDP.addNode("PartitionDevice", nPartitionNumber);

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
    
    public static boolean StartCDPSyn(int nCDPType, int mode, int auto, int fast,  ClientInfo client, String dNum, String pNum)
    {        
                System.out.println("StartCDPSyn pNum="+pNum);

        XMLConstructor xmlStartCDP = new XMLConstructor(CommandID.START_CDP_SYN_ID, CommandID.START_CDP_SYN_STR, "MarsCDP", "MarsCDP Console");
        xmlStartCDP.addNode("CDPType", nCDPType);
        xmlStartCDP.addNode("SyncMode", mode);
        xmlStartCDP.addNode("AutoStart", auto);
        xmlStartCDP.addNode("NTFSMode", fast);
        xmlStartCDP.addNode("DiskDevice", dNum);
        xmlStartCDP.addNode("PartitionDevice", pNum);

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
    
    public static boolean StartVCDP(int nCDPType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_StartCDP(nCDPType, nDiskNumber, nPartitionNumber);
        XMLConstructor xmlStartCDP = new XMLConstructor(CommandID.START_CDP_ID, CommandID.START_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlStartCDP.addNode("CDPType", nCDPType);
        xmlStartCDP.addNode("VolumeGroupName", nDiskNumber);
        xmlStartCDP.addNode("VolumeName", nPartitionNumber);

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
    
    public static boolean StartVCDPSyn(int nCDPType, int mode, int auto, int fast,  ClientInfo client, String dNum, String pNum)
    {        
                System.out.println("StartCDPSyn pNum="+pNum);

        XMLConstructor xmlStartCDP = new XMLConstructor(CommandID.START_CDP_SYN_ID, CommandID.START_CDP_SYN_STR, "MarsCDP", "MarsCDP Console");
        xmlStartCDP.addNode("CDPType", nCDPType);
        xmlStartCDP.addNode("SyncMode", mode);
        xmlStartCDP.addNode("AutoStart", auto);
        xmlStartCDP.addNode("NTFSMode", fast);
        xmlStartCDP.addNode("VolumeGroupName", dNum);
        xmlStartCDP.addNode("VolumeName", pNum);

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
    
    public static boolean StopCDP(int nCDPType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_StopCDP(nCDPType, nDiskNumber, nPartitionNumber);
        XMLConstructor xmlStopCDP = new XMLConstructor(CommandID.STOP_CDP_ID, CommandID.STOP_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlStopCDP.addNode("CDPType", nCDPType);
        xmlStopCDP.addNode("DiskDevice", nDiskNumber);
        xmlStopCDP.addNode("PartitionDevice", nPartitionNumber);

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
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
    
    public static boolean StopVCDP(int nCDPType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_StopCDP(nCDPType, nDiskNumber, nPartitionNumber);
        XMLConstructor xmlStopCDP = new XMLConstructor(CommandID.STOP_CDP_ID, CommandID.STOP_CDP_STR, "MarsCDP", "MarsCDP Console");
        xmlStopCDP.addNode("CDPType", nCDPType);
        xmlStopCDP.addNode("VolumeGroupName", nDiskNumber);
        xmlStopCDP.addNode("VolumeName", nPartitionNumber);

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }
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
        
    public static LinuxClientDiskDetail GetSourceDiskDetail(String nDiskNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_GetSourceDiskDetail(nDiskNumber);
        XMLConstructor xmlGetDiskDetail = new XMLConstructor(CommandID.GET_SOURCE_DISK_DETAIL_ID, CommandID.GET_SOURCE_DISK_DETAIL_STR, "MarsCDP", "MarsCDP Console");
        xmlGetDiskDetail.addNode("DiskDevice", nDiskNumber);

        System.out.println(xmlGetDiskDetail.toXmlString());
        
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
        CDPRES_GET_LINUX_SOURCE_DISK_DETAIL CDPRes = new CDPRES_GET_LINUX_SOURCE_DISK_DETAIL();
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
        return CDPRes.linuxDiskDetail;
    }

    public static LinuxClientPartitionDetail GetSourcePartitionDetail(String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_GetSourcePartitionDetail(nDiskNumber, nPartitionNumber);
        XMLConstructor xmlGetPartitionDetail = new XMLConstructor(CommandID.GET_SOURCE_PARTITION_DETAIL_ID, CommandID.GET_SOURCE_PARTITION_DETAIL_STR, "MarsCDP", "MarsCDP Console");
        xmlGetPartitionDetail.addNode("DiskDevice", nDiskNumber);
        xmlGetPartitionDetail.addNode("PartitionDevice", nPartitionNumber);

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
        System.out.println(xmlGetPartitionDetail.toXmlString());
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDPRES_GET_LINUX_SOURCE_PARTITION_DETAIL CDPRes = new CDPRES_GET_LINUX_SOURCE_PARTITION_DETAIL();
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
    
    public static LinuxVolumeDetail GetVolumeDetail(String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_GetSourcePartitionDetail(nDiskNumber, nPartitionNumber);
        XMLConstructor xmlGetPartitionDetail = new XMLConstructor(CommandID.GET_VOLUME_DETAIL_ID, CommandID.GET_VOLUME_DETAIL_STR, "MarsCDP", "MarsCDP Console");
        xmlGetPartitionDetail.addNode("VolumeGroupName", nDiskNumber);
        xmlGetPartitionDetail.addNode("VolumeName", nPartitionNumber);

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
        
        System.out.println(xmlGetPartitionDetail.toXmlString());
        String xmlRet = clientSock.getReturnXml();
        System.out.println(xmlRet);
        
        //parse response
        CDPRES_GET_LINUX_VOLUME_DETAIL CDPRes = new CDPRES_GET_LINUX_VOLUME_DETAIL();
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
    
    public static boolean RestoreData(int nRestoreType, int fast, String nSourceDiskNumber,
            String nSourcePartitionNumber, String nTargetDiskNumber, String nTargetPartitionNumber, ClientInfo client)
    {
        XMLConstructor xmlRestoreData = new XMLConstructor(CommandID.RESTORE_DATA_ID, CommandID.RESTORE_DATA_STR, "MarsCDP", "MarsCDP Console");
        xmlRestoreData.addNode("RestoreType", nRestoreType);
        xmlRestoreData.addNode("NTFSMode", fast);
        xmlRestoreData.addNode("SourceDisk", nSourceDiskNumber);
        xmlRestoreData.addNode("SourcePartition", nSourcePartitionNumber);
        xmlRestoreData.addNode("TargetDisk", nTargetDiskNumber);
        xmlRestoreData.addNode("TargetPartition", nTargetPartitionNumber);
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }        
        if (!clientSock.sendData(xmlRestoreData.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        
        System.out.println(xmlRestoreData.toXmlString());
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
    
    public static boolean RestoreVData(int nRestoreType, int fast, String nSourceDiskNumber,
            String nSourcePartitionNumber, String nTargetDiskNumber, String nTargetPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug)
//            return Debug_RestoreData(nRestoreType, nSourceDiskNumber, nSourcePartitionNumber,
//                    nTargetDiskNumber, nTargetPartitionNumber);
        XMLConstructor xmlRestoreData = new XMLConstructor(CommandID.RESTORE_DATA_ID, CommandID.RESTORE_DATA_STR, "MarsCDP", "MarsCDP Console");
        xmlRestoreData.addNode("RestoreType", nRestoreType);
        xmlRestoreData.addNode("NTFSMode", fast);
        xmlRestoreData.addNode("SourceVolumeGroup", nSourceDiskNumber);
        xmlRestoreData.addNode("SourceVolume", nSourcePartitionNumber);
        xmlRestoreData.addNode("TargetDisk", nTargetDiskNumber);
        xmlRestoreData.addNode("TargetPartition", nTargetPartitionNumber);
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(client.getIp(), client.getPort())) {
            m_nErrorCode = CDPConstants.SYSERROR_CONNECT_CLIENT;
            m_strError = "Connect client failed.";
            return false;
        }        
        if (!clientSock.sendData(xmlRestoreData.toXmlString())) {
            m_nErrorCode = CDPConstants.SYSERROR_SEND_XML;
            m_strError = "Send data failed.";
            return false;
        }
        
        System.out.println(xmlRestoreData.toXmlString());
        
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
    
    public static boolean CancelRestore(int nRestoreType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
        XMLConstructor xmlCancelRestore = new XMLConstructor(CommandID.CANCEL_RESTORE_ID, CommandID.CANCEL_RESTORE_STR, "MarsCDP", "MarsCDP Console");
        xmlCancelRestore.addNode("RestoreType", nRestoreType);
        xmlCancelRestore.addNode("SourceDisk", nDiskNumber);
        xmlCancelRestore.addNode("SourcePartition", nPartitionNumber);
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
    
    public static boolean CancelVRestore(int nRestoreType, String nDiskNumber, String nPartitionNumber, ClientInfo client)
    {
//        if (m_bDebug) 
//            return Debug_CancelRestore(nRestoreType, nDiskNumber, nPartitionNumber);
        XMLConstructor xmlCancelRestore = new XMLConstructor(CommandID.CANCEL_RESTORE_ID, CommandID.CANCEL_RESTORE_STR, "MarsCDP", "MarsCDP Console");
        xmlCancelRestore.addNode("RestoreType", nRestoreType);
        xmlCancelRestore.addNode("SourceVolumeGroup", nDiskNumber);
        xmlCancelRestore.addNode("SourceVolume", nPartitionNumber);
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
     
    public static LinuxClientDiskInfo GetSourceDiskInfoByNumber(String nDiskNumber, 
            LinuxClientDiskInfo[] DiskInfos)
    {
        for(int i=0;i<DiskInfos.length;i++) {
            LinuxClientDiskInfo DiskInfo = DiskInfos[i];
            if(DiskInfo.device.equals(nDiskNumber))
                return DiskInfo;
        }
        
        return null;
    }
    
    public static LinuxVolumeGroupInfo GetVGByName(String nDiskNumber, 
            LinuxVolumeGroupInfo[] DiskInfos)
    {
        for(int i=0;i<DiskInfos.length;i++) {
            LinuxVolumeGroupInfo DiskInfo = DiskInfos[i];
            if(DiskInfo.vgName.equals(nDiskNumber))
                return DiskInfo;
        }
        
        return null;
    }

    public static LinuxClientPartitionInfo GetSourcePartitionInfoByNumber(String nDiskNumber,
            String nPartitionNumber, LinuxClientDiskInfo[] DiskInfos)
    {
        for(int i=0;i<DiskInfos.length;i++) {
            LinuxClientDiskInfo DiskInfo = DiskInfos[i];

            for (int j = 0; j < DiskInfo.PartitionCount; j++) 
            {
                LinuxClientPartitionInfo PartitionInfo = DiskInfo.PartitionInfos[j];
                if (PartitionInfo.device.equals(nDiskNumber)
                        && PartitionInfo.pDevice.equals(nPartitionNumber)) {
                    return PartitionInfo;
                }
            }
        }
        
        return null;
    }
    
    public static LinuxVolumeInfo GetVolumeByName(String nVGName,
            String nVName, LinuxVolumeGroupInfo[] DiskInfos)
    {
        for(int i=0;i<DiskInfos.length;i++) {
            LinuxVolumeGroupInfo DiskInfo = DiskInfos[i];

            for (int j = 0; j < DiskInfo.vCount; j++) 
            {
                LinuxVolumeInfo PartitionInfo = DiskInfo.VolumeInfos[j];
                if (PartitionInfo.vgName.equals(nVGName)
                        && PartitionInfo.vName.equals(nVName)) {
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
