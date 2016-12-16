package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class LinuxClientDiskInfo  extends CommonInfo
{
    public String device;
    public long size;
    public int CDPType;
    public String cdpDevice;
    public int CDPStatus;
    public int PartitionCount;
    public boolean isMsa = false;
    public LinuxClientPartitionInfo[] PartitionInfos;
    public LinuxClientDiskDetail detail;
    
    @Override
    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //disk number
                String strText = GetXmlNodeString(objNode, "Device");
                if (strText == null) {
                    m_strError = "Node 'Disk/Number' not found.";
                    break;
                }
                device = strText;

                //disk size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Size' not found.";
                    break;
                }
                size = Long.parseLong(strText);

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
                cdpDevice = strText;

                //CDP status 
                strText = GetXmlNodeString(objNode, "CDPStatus");
                if (strText == null) {
                    m_strError = "Node 'Disk/CDPStatus' not found.";
                    break;
                }
                CDPStatus = Integer.parseInt(strText);
                
                NodeList PartitionInfoList = GetXmlNodeList(objNode, "Partition");
                if (PartitionInfoList == null) {
                    m_strError = "Node 'Disk/Partition' not found.";
                    break;
                }
                
                int nPartitionCnt = PartitionInfoList.getLength();
                if (nPartitionCnt < 0 || nPartitionCnt > CDPConstants.MAX_SOURCE_PARTITION) {
                    m_strError = "Invalid partition count.";
                    break;
                }
                
                boolean bRet = true;
                if (nPartitionCnt >= 0) 
                {
                    PartitionInfos = new LinuxClientPartitionInfo[nPartitionCnt];
                    for (int i = 0; i < nPartitionCnt; i++) 
                    {
                        Element partitionNode = (Element) PartitionInfoList.item(i);

                        PartitionInfos[i] = new LinuxClientPartitionInfo();
                        PartitionInfos[i].device = device;
                        
                        if (!PartitionInfos[i].ParseInfo(partitionNode)) 
                        {
                            m_strError = PartitionInfos[i].getError();

                            bRet = false;
                            break;
                        }
                    }
                }
                if (!bRet) 
                    break;

                PartitionCount = nPartitionCnt;
            } catch (Exception e) {
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse source disk information error! " + m_strError;

        return false;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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

    public int getCDPStatus() {
        return CDPStatus;
    }

    public void setCDPStatus(int CDPStatus) {
        this.CDPStatus = CDPStatus;
    }

    public int getPartitionCount() {
        return PartitionCount;
    }

    public void setPartitionCount(int PartitionCount) {
        this.PartitionCount = PartitionCount;
    }

    public LinuxClientPartitionInfo[] getPartitionInfos() {
        return PartitionInfos;
    }

    public void setPartitionInfos(LinuxClientPartitionInfo[] PartitionInfos) {
        this.PartitionInfos = PartitionInfos;
    }

    public LinuxClientDiskDetail getDetail() {
        return detail;
    }

    public void setDetail(LinuxClientDiskDetail detail) {
        this.detail = detail;
    }

    public boolean isIsMsa() {
        return isMsa;
    }

    public void setIsMsa(boolean isMsa) {
        this.isMsa = isMsa;
    }
    
    
}
