package com.marstor.msa.cdp.socket.action;

import com.marstor.msa.cdp.model.LinuxClientDiskDetail;
import org.w3c.dom.*;


public class CDPRES_GET_LINUX_SOURCE_DISK_DETAIL extends CDP_RESPONSE
{
    public LinuxClientDiskDetail linuxDiskDetail;

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
                
                Element objNode = GetXmlChildNode(m_RootNode, "Disk");
                if (objNode == null) {
                    m_strError = "Node 'Disk' not found!";
                    break;
                }

                linuxDiskDetail = new LinuxClientDiskDetail();
                if (!linuxDiskDetail.ParseInfo(objNode)) 
                {
                    m_strError = linuxDiskDetail.getError();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse get_source_disk_detail response error! " + m_strError +
                "\nXmlCmd=" + m_strXmlCmd;

        return false;
    }
}
