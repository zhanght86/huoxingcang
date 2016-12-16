package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;

public class LinuxVolumeGroupInfo  extends CommonInfo
{
    public String vgName;
    public long vgSize;
    public int vCount;
    public LinuxVolumeInfo[] VolumeInfos;
    
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

                //disk size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Size' not found.";
                    break;
                }
                vgSize = Long.parseLong(strText);
                
                NodeList PartitionInfoList = GetXmlNodeList(objNode, "Volume");
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
                    VolumeInfos = new LinuxVolumeInfo[nPartitionCnt];
                    for (int i = 0; i < nPartitionCnt; i++) 
                    {
                        Element partitionNode = (Element) PartitionInfoList.item(i);

                        VolumeInfos[i] = new LinuxVolumeInfo();
                        VolumeInfos[i].vgName = vgName;
                        
                        if (!VolumeInfos[i].ParseInfo(partitionNode)) 
                        {
                            m_strError = VolumeInfos[i].getError();

                            bRet = false;
                            break;
                        }
                    }
                }
                if (!bRet) 
                    break;

                vCount = nPartitionCnt;
            } catch (Exception e) {
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse source disk information error! " + m_strError;

        return false;
    }
}
