package com.marstor.msa.cdp.model;

import org.w3c.dom.*;

public class ClientPartitionInfo extends CommonInfo
{
    public int DiskNumber;
    public int PartitionNumber;
    public int PartitionType;
    public long PartitionSize;
    public long FreeSize;
    public String MountPoint;
    public int CDPType;
    public int CDPDiskNumber;
    public int CDPPartitionNumber;
    public int CDPStatus;
    public int diskCDPType;
    public boolean isMsa = false;
    public ClientPartitionDetail detail;
    
    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //partition number
                String strText = GetXmlNodeString(objNode, "Number");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Number' not found.";
                    break;
                }
                PartitionNumber = Integer.parseInt(strText);

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
                    break;
                }
                FreeSize = Long.parseLong(strText);

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
                CDPDiskNumber = Integer.parseInt(strText);

                //CDP partition number
                strText = GetXmlNodeString(objNode, "TargetPartition");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/TargetPartition' not found.";
                    break;
                }
                CDPPartitionNumber = Integer.parseInt(strText);
                
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

    public int getDiskNumber() {
        return DiskNumber;
    }

    public void setDiskNumber(int DiskNumber) {
        this.DiskNumber = DiskNumber;
    }

    public int getPartitionNumber() {
        return PartitionNumber;
    }

    public void setPartitionNumber(int PartitionNumber) {
        this.PartitionNumber = PartitionNumber;
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

    public int getCDPDiskNumber() {
        return CDPDiskNumber;
    }

    public void setCDPDiskNumber(int CDPDiskNumber) {
        this.CDPDiskNumber = CDPDiskNumber;
    }

    public int getCDPPartitionNumber() {
        return CDPPartitionNumber;
    }

    public void setCDPPartitionNumber(int CDPPartitionNumber) {
        this.CDPPartitionNumber = CDPPartitionNumber;
    }

    public int getCDPStatus() {
        return CDPStatus;
    }

    public void setCDPStatus(int CDPStatus) {
        this.CDPStatus = CDPStatus;
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

    public ClientPartitionDetail getDetail() {
        return detail;
    }

    public void setDetail(ClientPartitionDetail detail) {
        this.detail = detail;
    }

    
    
}
