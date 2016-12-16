package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class LinuxClientMirroredVGInfo extends CommonInfo
{
    public String vgName;
    public long vgSize;
    public int PartitionCount;
    public String serialNumber;
    public LinuxClientMirroredVolumeInfo[] PartitionInfos;

    @Override
    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //disk number
                String strText = GetXmlNodeString(objNode, "Name");
                if (strText == null) {
                    m_strError = "Node 'Disk/Number' not found.";
                    break;
                }
                vgName = strText;
                
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/SerialNumber' not found.";
                    break;
                }
                vgSize = Long.parseLong(strText);

                NodeList PartitionInfoList = GetXmlNodeList(objNode, "Volume");
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
                    PartitionInfos = new LinuxClientMirroredVolumeInfo[nPartitionCnt];
                    for (int i = 0; i < nPartitionCnt; i++) 
                    {
                        Element partitionNode = (Element) PartitionInfoList.item(i);

                        PartitionInfos[i] = new LinuxClientMirroredVolumeInfo();
                        PartitionInfos[i].vgName = vgName;
                        
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
}
