package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class ClientDiskInfo  extends CommonInfo
{
    public int DiskNumber;
    public long DiskSize;
    public int CDPType;
    public int CDPDiskNumber;
    public int CDPStatus;
    public int PartitionCount;
    public boolean isMsa = false;
    public ClientPartitionInfo[] PartitionInfos;
    public ClientDiskDetail detail;
    
    
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

                //disk size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Size' not found.";
                    break;
                }
                DiskSize = Long.parseLong(strText);

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

                //CDP status 
                strText = GetXmlNodeString(objNode, "CDPStatus");
                if (strText == null) {
                    m_strError = "Node 'Disk/CDPStatus' not found.";
                    break;
                }
                CDPStatus = Integer.parseInt(strText);
                
                int nPartitionCnt;
                NodeList PartitionInfoList = GetXmlNodeList(objNode, "Partition");
                
                if (PartitionInfoList == null) {
                    nPartitionCnt = 0;
                }else{
                    nPartitionCnt = PartitionInfoList.getLength();
                }                
                
                if (nPartitionCnt < 0 || nPartitionCnt > CDPConstants.MAX_SOURCE_PARTITION) {
                    m_strError = "Invalid partition count.";
                    break;
                }
                
                boolean bRet = true;
                if (nPartitionCnt > 0) 
                {
                    PartitionInfos = new ClientPartitionInfo[nPartitionCnt];
                    for (int i = 0; i < nPartitionCnt; i++) 
                    {
                        Element partitionNode = (Element) PartitionInfoList.item(i);

                        PartitionInfos[i] = new ClientPartitionInfo();
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
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse source disk information error! " + m_strError;

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

    public ClientPartitionInfo[] getPartitionInfos() {
        return PartitionInfos;
    }

    public void setPartitionInfos(ClientPartitionInfo[] PartitionInfos) {
        this.PartitionInfos = PartitionInfos;
    }

    public boolean isIsMsa() {
        return isMsa;
    }

    public void setIsMsa(boolean isMsa) {
        this.isMsa = isMsa;
    }

    public ClientDiskDetail getDetail() {
        return detail;
    }

    public void setDetail(ClientDiskDetail detail) {
        this.detail = detail;
    }
    
    
}
