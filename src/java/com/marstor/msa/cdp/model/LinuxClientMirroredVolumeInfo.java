package com.marstor.msa.cdp.model;

import org.w3c.dom.*;

public class LinuxClientMirroredVolumeInfo extends CommonInfo
{
    public String vName;
    public String vgName;
    public long vSize;
    public boolean Used;

    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //partition number
                String strText = GetXmlNodeString(objNode, "Name");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Number' not found.";
                    break;
                }
                vName = strText;

                //partition size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Size' not found.";
                    break;
                }
                vSize = Long.parseLong(strText);

                //used
                strText = GetXmlNodeString(objNode, "Used");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Used' not found.";
                    break;
                }
                Used = strText.equals("1")?true:false;
            } catch (Exception e) {
                e.printStackTrace();
                m_strError = e.getMessage();
                break;
            }

            return true;
        }

        m_strError = "Parse mirror partition information error! " + m_strError;

        return false;
    }
}
