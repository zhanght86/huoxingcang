package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class ClientDiskDetail extends CommonInfo {

    public int DiskNumber;
    public long DiskSize;
    public String VendorID;
    public String ProductID;
    public String SerialNumber;
    public String ProductRevision;
    public int CDPType;
    public int CDPDiskNumber;
    public boolean UseCache;
    public int CDPStatus;
    public long HookErrorCode;
    public long SyncWarningCode;
    public int RestoreStatus;
    public int SyncStatus;
    public int SyncPercent;
    public String SyncStartTime;
    public String SyncEndTime;
    public long SyncTotal;
    public int HookStatus;
    public String HookStartTime;
    public String HookEndTime;
    public long HookTotal;
    public long SystemErrorCode;

    public boolean ParseInfo(Element objNode) {
        while (true) {
            try {
                //disk number
                String strText = GetXmlNodeString(objNode, "Number");
                if (strText == null) {
                    m_strError = "Node 'Disk/Number' not found.";
                    break;
                }
                DiskNumber = Integer.parseInt(strText);

                //disk size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Size' not found.";
                    break;
                }
                DiskSize = Long.parseLong(strText);

                //vendor ID
                strText = GetXmlNodeString(objNode, "VendorID");
                if (strText == null) {
                    m_strError = "Node 'Disk/VendorID' not found.";
                    break;
                }
                if (strText.equals("")) {
                    strText = "±ê×¼´ÅÅÌÇý¶¯Æ÷";
                }
                VendorID = strText;

                //product ID
                strText = GetXmlNodeString(objNode, "ProductID");
                if (strText == null) {
                    m_strError = "Node 'Disk/ProductID' not found.";
                    break;
                }
                ProductID = strText;

                //serial number
                strText = GetXmlNodeString(objNode, "SerialNumber");
                if (strText == null) {
                    m_strError = "Node 'Disk/SerialNumber' not found.";
                    break;
                }
                SerialNumber = strText;

                //product revision
                strText = GetXmlNodeString(objNode, "ProductRevision");
                if (strText == null) {
                    m_strError = "Node 'Disk/ProductRevision' not found.";
                    break;
                }
                ProductRevision = strText;

                //CDP type
                strText = GetXmlNodeString(objNode, "CDPType");
                if (strText == null) {
                    m_strError = "Node 'Disk/CDPType' not found.";
                    break;
                }
                CDPType = Integer.parseInt(strText);

                //CDP disk number
                strText = GetXmlNodeString(objNode, "TargetDisk");
                if (strText == null) {
                    m_strError = "Node 'Disk/TargetDisk' not found.";
                    break;
                }
                CDPDiskNumber = Integer.parseInt(strText);

                //use cache
                strText = GetXmlNodeString(objNode, "UseCache");
                if (strText == null) {
                    m_strError = "Node 'Disk/UseCache' not found.";
                    break;
                }
                UseCache = strText.equals("1") ? true : false;

                //CDP status 
                strText = GetXmlNodeString(objNode, "CDPStatus");
                if (strText == null) {
                    m_strError = "Node 'Disk/CDPStatus' not found.";
                    break;
                }
                CDPStatus = Integer.parseInt(strText);

                //hook error code 
                strText = GetXmlNodeString(objNode, "HookErrorCode");
                if (strText == null) {
                    m_strError = "Node 'Disk/HookErrorCode' not found.";
                    break;
                }
                HookErrorCode = Integer.parseInt(strText);

                //system error code 
                strText = GetXmlNodeString(objNode, "SystemErrorCode");
                if (strText == null) {
                    m_strError = "Node 'Disk/SystemErrorCode' not found.";
                } else {
                    SystemErrorCode = Long.parseLong(strText);
                }


                //SyncWarningCode
                strText = GetXmlNodeString(objNode, "SyncWarningCode");
                if (strText == null) {
                    m_strError = "Node 'Disk/SyncWarningCode' not found.";
                } else {
                    SyncWarningCode = Long.parseLong(strText);
                }


                //restore status 
                strText = GetXmlNodeString(objNode, "RestoreStatus");
                if (strText == null) {
                    m_strError = "Node 'Disk/RestoreStatus' not found.";
                    break;
                }
                RestoreStatus = Integer.parseInt(strText);

                if (RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                    //sync status 
                    strText = GetXmlNodeString(objNode, "SyncStatus");
                    if (strText == null) {
                        m_strError = "Node 'Disk/SyncStatus' not found.";
                        break;
                    }
                    SyncStatus = Integer.parseInt(strText);

                    //sync percent 
                    strText = GetXmlNodeString(objNode, "SyncPercent");
                    if (strText == null) {
                        m_strError = "Node 'Disk/SyncPercent' not found.";
                        break;
                    }
                    SyncPercent = Integer.parseInt(strText);

                    //sync start time 
                    strText = GetXmlNodeString(objNode, "SyncStartTime");
                    if (strText == null) {
                        m_strError = "Node 'Disk/SyncStartTime' not found.";
                        break;
                    }
                    SyncStartTime = strText;

                    //sync end time 
                    strText = GetXmlNodeString(objNode, "SyncEndTime");
                    if (strText == null) {
                        m_strError = "Node 'Disk/SyncEndTime' not found.";
                        break;
                    }
                    SyncEndTime = strText;

                    //sync total 
                    strText = GetXmlNodeString(objNode, "SyncTotal");
                    if (strText == null) {
                        m_strError = "Node 'Disk/SyncTotal' not found.";
                        break;
                    }
                    SyncTotal = Long.parseLong(strText);

                    //hook total 
                    strText = GetXmlNodeString(objNode, "HookTotal");
                    if (strText == null) {
                        m_strError = "Node 'Disk/HookTotal' not found.";
                        break;
                    }
                    HookTotal = Long.parseLong(strText);
                } else {
                    if (CDPType != CDPConstants.CDP_TYPE_INIT
                            && CDPStatus != CDPConstants.CDP_STATUS_INIT) {
                        //sync status 
                        strText = GetXmlNodeString(objNode, "SyncStatus");
                        if (strText == null) {
                            m_strError = "Node 'Disk/SyncStatus' not found.";
                            break;
                        }
                        SyncStatus = Integer.parseInt(strText);

                        //sync percent 
                        strText = GetXmlNodeString(objNode, "SyncPercent");
                        if (strText == null) {
                            m_strError = "Node 'Disk/SyncPercent' not found.";
                            break;
                        }
                        SyncPercent = Integer.parseInt(strText);

                        //sync start time 
                        strText = GetXmlNodeString(objNode, "SyncStartTime");
                        if (strText == null) {
                            m_strError = "Node 'Disk/SyncStartTime' not found.";
                            break;
                        }
                        SyncStartTime = strText;

                        //sync end time 
                        strText = GetXmlNodeString(objNode, "SyncEndTime");
                        if (strText == null) {
                            m_strError = "Node 'Disk/SyncEndTime' not found.";
                            break;
                        }
                        SyncEndTime = strText;

                        //sync total 
                        strText = GetXmlNodeString(objNode, "SyncTotal");
                        if (strText == null) {
                            m_strError = "Node 'Disk/SyncTotal' not found.";
                            break;
                        }
                        SyncTotal = Long.parseLong(strText);

                        //hook status 
                        strText = GetXmlNodeString(objNode, "HookStatus");
                        if (strText == null) {
                            m_strError = "Node 'Disk/HookStatus' not found.";
                            break;
                        }
                        HookStatus = Integer.parseInt(strText);

                        //hook start time 
                        strText = GetXmlNodeString(objNode, "HookStartTime");
                        if (strText == null) {
                            m_strError = "Node 'Disk/HookStartTime' not found.";
                            break;
                        }
                        HookStartTime = strText;

                        //hook end time 
                        strText = GetXmlNodeString(objNode, "HookEndTime");
                        if (strText == null) {
                            m_strError = "Node 'Disk/HookEndTime' not found.";
                            break;
                        }
                        HookEndTime = strText;

                        //hook total 
                        strText = GetXmlNodeString(objNode, "HookTotal");
                        if (strText == null) {
                            m_strError = "Node 'Disk/HookTotal' not found.";
                            break;
                        }
                        HookTotal = Long.parseLong(strText);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse source disk detail error! " + m_strError;

        return false;
    }
}
