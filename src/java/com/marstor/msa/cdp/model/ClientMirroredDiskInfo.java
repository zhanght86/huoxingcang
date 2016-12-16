package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class ClientMirroredDiskInfo extends CommonInfo
{
    public int DiskNumber;
    public long DiskSize;
    public boolean Used;
    public int PartitionCount;
    public String serialNumber;
    public ClientMirroredPartitionInfo[] PartitionInfos;

    @Override
    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //disk number
                String strText = GetXmlNodeString(objNode, "Number");
                if (strText == null) {
                    m_strError = "Node 'Disk/Number' not found.";
                    break;
                }
                DiskNumber = Integer.parseInt(strText);
                
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
                    PartitionInfos = new ClientMirroredPartitionInfo[nPartitionCnt];
                    for (int i = 0; i < nPartitionCnt; i++) 
                    {
                        Element partitionNode = (Element) PartitionInfoList.item(i);

                        PartitionInfos[i] = new ClientMirroredPartitionInfo();
                        PartitionInfos[i].DiskNumber = DiskNumber;
                        
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

    public int getDiskNumber() {
        return DiskNumber;
    }

    public void setDiskNumber(int DiskNumber) {
        this.DiskNumber = DiskNumber;
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

    public ClientMirroredPartitionInfo[] getPartitionInfos() {
        return PartitionInfos;
    }

    public void setPartitionInfos(ClientMirroredPartitionInfo[] PartitionInfos) {
        this.PartitionInfos = PartitionInfos;
    }
    
    
}
