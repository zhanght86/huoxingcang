package com.marstor.msa.cdp.model;

import org.w3c.dom.*;

public class LinuxClientPartitionInfo extends CommonInfo
{
    public String device;
    public String pDevice;
    public int PartitionType;
    public long PartitionSize;
    public String MountPoint;
    public int CDPType;
    public long FreeSize;
    public String cdpDevice;
    public String cdpPDevice;
    public int CDPStatus;
    public int diskCDPType;
    public boolean isMsa = false;
    public LinuxClientPartitionDetail detail;
    
    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //partition number
                String strText = GetXmlNodeString(objNode, "Device");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Number' not found.";
                    break;
                }
                pDevice = strText;

                //partition type
                strText = GetXmlNodeString(objNode, "Type");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Type' not found.";
                    break;
                }
                PartitionType = Integer.parseInt(strText);

                //partition size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Size' not found.";
                    break;
                }
                PartitionSize = Long.parseLong(strText);
                
                //free size
                strText = GetXmlNodeString(objNode, "FreeSize");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/FreeSize' not found.";                    
                }else{
                    FreeSize = Long.parseLong(strText);
                }
                

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

                //CDP disk number
                strText = GetXmlNodeString(objNode, "TargetDisk");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/TargetDisk' not found.";
                    break;
                }
                cdpDevice = strText;

                //CDP partition number
                strText = GetXmlNodeString(objNode, "TargetPartition");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/TargetPartition' not found.";
                    break;
                }
                cdpPDevice = strText;
                
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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPDevice() {
        return pDevice;
    }

    public void setPDevice(String pDevice) {
        this.pDevice = pDevice;
    }

    public int getPartitionType() {
        return PartitionType;
    }

    public void setPartitionType(int PartitionType) {
        this.PartitionType = PartitionType;
    }

    public long getPartitionSize() {
        return PartitionSize;
    }

    public void setPartitionSize(long PartitionSize) {
        this.PartitionSize = PartitionSize;
    }

    public String getMountPoint() {
        return MountPoint;
    }

    public void setMountPoint(String MountPoint) {
        this.MountPoint = MountPoint;
    }

    public int getCDPType() {
        return CDPType;
    }

    public void setCDPType(int CDPType) {
        this.CDPType = CDPType;
    }

    public String getCdpDevice() {
        return cdpDevice;
    }

    public void setCdpDevice(String cdpDevice) {
        this.cdpDevice = cdpDevice;
    }

    public String getCdpPDevice() {
        return cdpPDevice;
    }

    public void setCdpPDevice(String cdpPDevice) {
        this.cdpPDevice = cdpPDevice;
    }

    public int getCDPStatus() {
        return CDPStatus;
    }

    public void setCDPStatus(int CDPStatus) {
        this.CDPStatus = CDPStatus;
    }

    public String getpDevice() {
        return pDevice;
    }

    public void setpDevice(String pDevice) {
        this.pDevice = pDevice;
    }

    public LinuxClientPartitionDetail getDetail() {
        return detail;
    }

    public void setDetail(LinuxClientPartitionDetail detail) {
        this.detail = detail;
    }

    public int getDiskCDPType() {
        return diskCDPType;
    }

    public void setDiskCDPType(int diskCDPType) {
        this.diskCDPType = diskCDPType;
    }

    public boolean isIsMsa() {
        return isMsa;
    }

    public void setIsMsa(boolean isMsa) {
        this.isMsa = isMsa;
    }

    public long getFreeSize() {
        return FreeSize;
    }

    public void setFreeSize(long FreeSize) {
        this.FreeSize = FreeSize;
    }
    
    
    
}
