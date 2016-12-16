package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class LinuxClientPartitionDetail extends CommonInfo {

    public String device;
    public String pDevice;
    public int PartitionType;
    public long PartitionSize;
    public String MountPoint;
    public int CDPType;
    public String cdpDevice;
    public String cdpPDevice;
    public boolean UseCache;
    public int CDPStatus;
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
    public long FreeSize;
    public long UsedSize;
    public long UsedPercent;

    public boolean ParseInfo(Element objNode) {
        while (true) {
            try {
                //disk number
                String strText = GetXmlNodeString(objNode, "DiskDevice");
                if (strText == null) {
                    m_strError = "Node 'Partition/DiskNumber' not found.";
                    break;
                }
                device = strText;

                //partition number
                strText = GetXmlNodeString(objNode, "PartitionDevice");
                if (strText == null) {
                    m_strError = "Node 'Partition/PartitionNumber' not found.";
                    break;
                }
                pDevice = strText;

                //partition type
                strText = GetXmlNodeString(objNode, "Type");
                if (strText == null) {
                    m_strError = "Node 'Partition/Type' not found.";
                    break;
                }
                PartitionType = Integer.parseInt(strText);

                //partition size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Partition/Size' not found.";
                    break;
                }
                PartitionSize = Long.parseLong(strText);

                //free size
                strText = GetXmlNodeString(objNode, "FreeSize");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/FreeSize' not found.";
                } else {
                    FreeSize = Long.parseLong(strText);
                }

                //free size
                strText = GetXmlNodeString(objNode, "UsedSize");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/FreeSize' not found.";
                } else {
                    UsedSize = Long.parseLong(strText);
                }

                //free size
                strText = GetXmlNodeString(objNode, "UsedPercent");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/FreeSize' not found.";
                } else {
                    UsedPercent = Long.parseLong(strText);
                }

                //mount point
                strText = GetXmlNodeString(objNode, "MountPoint");
                if (strText == null) {
                    m_strError = "Node 'Partition/MountPoint' not found.";
                    break;
                }
                MountPoint = strText;

                //CDP type
                strText = GetXmlNodeString(objNode, "CDPType");
                if (strText == null) {
                    m_strError = "Node 'Partition/CDPType' not found.";
                    break;
                }
                CDPType = Integer.parseInt(strText);

                //CDP disk number
                strText = GetXmlNodeString(objNode, "TargetDisk");
                if (strText == null) {
                    m_strError = "Node 'Partition/TargetDisk' not found.";
                    break;
                }
                cdpDevice = strText;

                //CDP partition number
                strText = GetXmlNodeString(objNode, "TargetPartition");
                if (strText == null) {
                    m_strError = "Node 'Partition/TargetPartition' not found.";
                    break;
                }
                cdpPDevice = strText;

                //use cache
                strText = GetXmlNodeString(objNode, "UseCache");
                if (strText == null) {
                    m_strError = "Node 'Partition/UseCache' not found.";
                    break;
                }
                UseCache = strText.equals("1") ? true : false;

                //CDP status 
                strText = GetXmlNodeString(objNode, "CDPStatus");
                if (strText == null) {
                    m_strError = "Node 'Partition/CDPStatus' not found.";
                    break;
                }
                CDPStatus = Integer.parseInt(strText);

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
                        m_strError = "Node 'Partition/SyncStatus' not found.";
                        break;
                    }
                    SyncStatus = Integer.parseInt(strText);

                    //sync percent 
                    strText = GetXmlNodeString(objNode, "SyncPercent");
                    if (strText == null) {
                        m_strError = "Node 'Partition/SyncPercent' not found.";
                        break;
                    }
                    SyncPercent = Integer.parseInt(strText);

                    //sync start time 
                    strText = GetXmlNodeString(objNode, "SyncStartTime");
                    if (strText == null) {
                        m_strError = "Node 'Partition/SyncStartTime' not found.";
                        break;
                    }
                    SyncStartTime = strText;

                    //sync end time 
                    strText = GetXmlNodeString(objNode, "SyncEndTime");
                    if (strText == null) {
                        m_strError = "Node 'Partition/SyncEndTime' not found.";
                        break;
                    }
                    SyncEndTime = strText;

                    //sync total 
                    strText = GetXmlNodeString(objNode, "SyncTotal");
                    if (strText == null) {
                        m_strError = "Node 'Partition/SyncTotal' not found.";
                        break;
                    }
                    SyncTotal = Long.parseLong(strText);

                    //hook total 
                    strText = GetXmlNodeString(objNode, "HookTotal");
                    if (strText == null) {
                        m_strError = "Node 'Partition/HookTotal' not found.";
                        break;
                    }
                    HookTotal = Long.parseLong(strText);
                } else {
                    if (CDPType != CDPConstants.CDP_TYPE_INIT
                            && CDPStatus != CDPConstants.CDP_STATUS_INIT) {
                        //sync status 
                        strText = GetXmlNodeString(objNode, "SyncStatus");
                        if (strText == null) {
                            m_strError = "Node 'Partition/SyncStatus' not found.";
                            break;
                        }
                        SyncStatus = Integer.parseInt(strText);

                        //sync percent 
                        strText = GetXmlNodeString(objNode, "SyncPercent");
                        if (strText == null) {
                            m_strError = "Node 'Partition/SyncPercent' not found.";
                            break;
                        }
                        SyncPercent = Integer.parseInt(strText);

                        //sync start time 
                        strText = GetXmlNodeString(objNode, "SyncStartTime");
                        if (strText == null) {
                            m_strError = "Node 'Partition/SyncStartTime' not found.";
                            break;
                        }
                        SyncStartTime = strText;

                        //sync end time 
                        strText = GetXmlNodeString(objNode, "SyncEndTime");
                        if (strText == null) {
                            m_strError = "Node 'Partition/SyncEndTime' not found.";
                            break;
                        }
                        SyncEndTime = strText;

                        //sync total 
                        strText = GetXmlNodeString(objNode, "SyncTotal");
                        if (strText == null) {
                            m_strError = "Node 'Partition/SyncTotal' not found.";
                            break;
                        }
                        SyncTotal = Long.parseLong(strText);

                        //hook status 
                        strText = GetXmlNodeString(objNode, "HookStatus");
                        if (strText == null) {
                            m_strError = "Node 'Partition/HookStatus' not found.";
                            break;
                        }
                        HookStatus = Integer.parseInt(strText);

                        //hook start time 
                        strText = GetXmlNodeString(objNode, "HookStartTime");
                        if (strText == null) {
                            m_strError = "Node 'Partition/HookStartTime' not found.";
                            break;
                        }
                        HookStartTime = strText;

                        //hook end time 
                        strText = GetXmlNodeString(objNode, "HookEndTime");
                        if (strText == null) {
                            m_strError = "Node 'Partition/HookEndTime' not found.";
                            break;
                        }
                        HookEndTime = strText;

                        //hook total 
                        strText = GetXmlNodeString(objNode, "HookTotal");
                        if (strText == null) {
                            m_strError = "Node 'Partition/HookTotal' not found.";
                            break;
                        }
                        HookTotal = Long.parseLong(strText);
                    }
                }
            } catch (Exception e) {
                m_strError = e.getMessage();
                e.printStackTrace();
                break;
            }

            return true;
        }

        m_strError = "Parse source partition detail error! " + m_strError;

        return false;
    }
}
