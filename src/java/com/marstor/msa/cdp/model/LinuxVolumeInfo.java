package com.marstor.msa.cdp.model;

import org.w3c.dom.*;

public class LinuxVolumeInfo extends CommonInfo
{
    public String vgName;
    public String vName;
    public long vSize;
    public String MountPoint;
    public int CDPType;
    public long FreeSize;
    public long UsedSize;
    public String CDP_VG;
    public String CDP_V;
    public int CDPStatus;
    public LinuxVolumeDetail detail;
    
    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //partition number
                String strText = GetXmlNodeString(objNode, "Name");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Number' not found.";
                    break;
                }
                vName = strText;
                
                //partition size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Size' not found.";
                    break;
                }
                vSize = Long.parseLong(strText);

                //mount point
                strText = GetXmlNodeString(objNode, "MountPoint");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/MountPoint' not found.";
                    break;
                }
                MountPoint = strText;
                
                //CDP type
                strText = GetXmlNodeString(objNode, "CDPType");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/CDPType' not found.";
                    break;
                }
                CDPType = Integer.parseInt(strText);
                
                //free size
                strText = GetXmlNodeString(objNode, "FreeSize");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/FreeSize' not found.";                    
                }else{
                    FreeSize = Long.parseLong(strText);
                }
                
                //free size
                strText = GetXmlNodeString(objNode, "UsedSize");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/UsedSize' not found.";                    
                }else{
                    UsedSize = Long.parseLong(strText);
                }

                //CDP disk number
                strText = GetXmlNodeString(objNode, "TargetDisk");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/TargetDisk' not found.";
                    break;
                }
                CDP_VG = strText;

                //CDP partition number
                strText = GetXmlNodeString(objNode, "TargetPartition");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/TargetPartition' not found.";
                    break;
                }
                CDP_V = strText;
                
                //CDP status 
                strText = GetXmlNodeString(objNode, "CDPStatus");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/CDPStatus' not found.";
                    break;
                }
                CDPStatus = Integer.parseInt(strText);
            } catch (Exception e) {
                e.printStackTrace();
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse source partition information error! " + m_strError;

        return false;
    }

    public LinuxVolumeDetail getDetail() {
        return detail;
    }

    public void setDetail(LinuxVolumeDetail detail) {
        this.detail = detail;
    }
    
}
