package com.marstor.msa.cdp.socket.action;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import org.w3c.dom.*;


public class CDPRES_GET_SOURCE_DISK_DETAIL extends CDP_RESPONSE
{
    public ClientDiskDetail DiskDetail;

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

                DiskDetail = new ClientDiskDetail();
                if (!DiskDetail.ParseInfo(objNode)) 
                {
                    m_strError = DiskDetail.getError();
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
