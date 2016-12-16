package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class LinuxClientMirroredDiskInfo extends CommonInfo
{
    public String device;
    public long DiskSize;
    public boolean Used;
    public int PartitionCount;
    public String serialNumber;
    public LinuxClientMirroredPartitionInfo[] PartitionInfos;

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
                
                strText = GetXmlNodeString(objNode, "SerialNumber");
                if (strText == null) {
                    m_strError = "Node 'Disk/SerialNumber' not found.";
                    break;
                }
                serialNumber = strText;

                //disk size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Size' not found.";
                    break;
                }
                DiskSize = Long.parseLong(strText);

                //used
                strText = GetXmlNodeString(objNode, "Used");
                if (strText == null) {
                    m_strError = "Node '/Disk/Used' not found.";
                    break;
                }
                Used = strText.equals("1")?true:false;

                NodeList PartitionInfoList = GetXmlNodeList(objNode, "Partition");
                if (PartitionInfoList == null) {
                    m_strError = "Node 'Disk/Partition' not found.";
                    break;
                }
                
                int nPartitionCnt = PartitionInfoList.getLength();
                if (nPartitionCnt < 0 || nPartitionCnt > CDPConstants.MAX_MIRROR_PARTITION) {
                    m_strError = "Invalid partition count.";
                    break;
                }
                
                boolean bRet = true;
                if (nPartitionCnt >= 0) 
                {
                    PartitionInfos = new LinuxClientMirroredPartitionInfo[nPartitionCnt];
                    for (int i = 0; i < nPartitionCnt; i++) 
                    {
                        Element partitionNode = (Element) PartitionInfoList.item(i);

                        PartitionInfos[i] = new LinuxClientMirroredPartitionInfo();
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
                e.printStackTrace();
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse mirror disk information error! " + m_strError;

        return false;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getDiskSize() {
        return DiskSize;
    }

    public void setDiskSize(long DiskSize) {
        this.DiskSize = DiskSize;
    }

    public boolean isUsed() {
        return Used;
    }

    public void setUsed(boolean Used) {
        this.Used = Used;
    }

    public int getPartitionCount() {
        return PartitionCount;
    }

    public void setPartitionCount(int PartitionCount) {
        this.PartitionCount = PartitionCount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LinuxClientMirroredPartitionInfo[] getPartitionInfos() {
        return PartitionInfos;
    }

    public void setPartitionInfos(LinuxClientMirroredPartitionInfo[] PartitionInfos) {
        this.PartitionInfos = PartitionInfos;
    }
    
}
