package com.marstor.msa.cdp.socket.action;

import com.marstor.msa.cdp.model.ClientPartitionDetail;
import org.w3c.dom.*;


public class CDPRES_GET_SOURCE_PARTITION_DETAIL extends CDP_RESPONSE
{
    public ClientPartitionDetail PartitionDetail;

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
                
                Element objNode = GetXmlChildNode(m_RootNode, "Partition");
                if (objNode == null) {
                    m_strError = "Node 'Partition' not found!";
                    break;
                }

                PartitionDetail = new ClientPartitionDetail();
                if (!PartitionDetail.ParseInfo(objNode)) 
                {
                    m_strError = PartitionDetail.getError();
                    break;
                }
            } catch (Exception e) {
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse get_source_partition_detail response error! " + m_strError +
                "\nXmlCmd=" + m_strXmlCmd;

        return false;
    }
}
