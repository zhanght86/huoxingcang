package com.marstor.msa.cdp.model;

import org.w3c.dom.*;

public class LinuxClientMirroredPartitionInfo extends CommonInfo
{
    public String device;
    public String pDevice;
    public int PartitionType;
    public long PartitionSize;
    public boolean Used;

    public boolean ParseInfo(Element objNode) 
    {
        while (true)
        {
            try 
            {
                //partition number
                String strText = GetXmlNodeString(objNode, "Device");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Number' not found.";
                    break;
                }
                pDevice = strText;

                //partition type
                strText = GetXmlNodeString(objNode, "Type");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Type' not found.";
                    break;
                }
                PartitionType = Integer.parseInt(strText);

                //partition size
                strText = GetXmlNodeString(objNode, "Size");
                if (strText == null) {
                    m_strError = "Node 'Disk/Partition/Size' not found.";
                    break;
                }
                PartitionSize = Long.parseLong(strText);

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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPDevice() {
        return pDevice;
    }

    public void setPDevice(String pDevice) {
        this.pDevice = pDevice;
    }

    public int getPartitionType() {
        return PartitionType;
    }

    public void setPartitionType(int PartitionType) {
        this.PartitionType = PartitionType;
    }

    public long getPartitionSize() {
        return PartitionSize;
    }

    public void setPartitionSize(long PartitionSize) {
        this.PartitionSize = PartitionSize;
    }

    public boolean isUsed() {
        return Used;
    }

    public void setUsed(boolean Used) {
        this.Used = Used;
    }
    
    
}
