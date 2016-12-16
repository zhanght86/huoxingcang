package com.marstor.msa.cdp.socket.action;

import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.util.CDPConstants;
import org.w3c.dom.*;


public class CDPRES_GET_MIRROR_DISKS extends CDP_RESPONSE
{
    public ClientMirroredDiskInfo[] lpDiskInfo;

    @Override
    public boolean ParseResponse()
    {
        while (true) 
        {
            try 
            {
                if (!super.ParseResponse()) 
                    break;
                
                if (nStatus != 0) 
                    return true;
                
                NodeList DiskInfoList = GetXmlNodeList(m_RootNode, "Disk");
                if (DiskInfoList == null) {
                    m_strError = "Node 'Disk' not found!";
                    break;
                }

                int nDiskCnt = DiskInfoList.getLength();
                if (nDiskCnt < 0 || nDiskCnt > CDPConstants.MAX_MIRROR_DISK) {
                    m_strError = "Invalid disk count!";
                    break;
                }

                boolean bRet = true;
                if (nDiskCnt >= 0) 
                {
                    lpDiskInfo = new ClientMirroredDiskInfo[nDiskCnt];
                    for (int i = 0; i < nDiskCnt; i++)
                    {
                        Element objNode = (Element) DiskInfoList.item(i);

                        lpDiskInfo[i] = new ClientMirroredDiskInfo();
                        if (!lpDiskInfo[i].ParseInfo(objNode)) 
                        {
                            m_strError = lpDiskInfo[i].getError();

                            bRet = false;
                            break;
                        }
                    }
                }
                if (!bRet) 
                    break;
            } catch (Exception e) {
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse get_mirror_disks response error! " + m_strError +
                "\nXmlCmd=" + m_strXmlCmd;

        return false;
    }
}
