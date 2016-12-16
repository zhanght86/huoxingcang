 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.fc.bean;

import com.marstor.msa.bean.IMarsXMLBean;
import com.marstor.msa.common.bean.FCInitiatorInformation;
import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class FCInitiator
{
    public String initiator_name = "";
    public String os_device_name = "";
    public String driver_name = "";
    public String initiator_state = "";
    public String supported_speeds = "";
    public String current_speed = "";

    public FCInitiator() {
    }

    public FCInitiator(FCInitiatorInformation origin) {
        this.initiator_name = origin.name;
        this.os_device_name = origin.OSDeviceName;
        this.driver_name = origin.DriverName;
        this.initiator_state = origin.State;
        this.supported_speeds = origin.SupportedSpeeds;
        this.current_speed = origin.CurrentSpeed;
    }   

    public String getName()
    {
        return initiator_name;
    }

    public void setName(String name)
    {
        this.initiator_name = name;
    }

    public String getOSDeviceName()
    {
        return os_device_name;
    }

    public void setOSDeviceName(String OSDeviceName)
    {
        this.os_device_name = OSDeviceName;
    }

    public String getDriverName()
    {
        return driver_name;
    }

    public void setDriverName(String DriverName)
    {
        this.driver_name = DriverName;
    }

    public String getState()
    {
        return initiator_state;
    }

    public void setState(String State)
    {
        this.initiator_state = State;
    }

    public String getSupportedSpeeds()
    {
        return supported_speeds;
    }

    public void setSupportedSpeeds(String SupportedSpeeds)
    {
        this.supported_speeds = SupportedSpeeds;
    }

    public String getCurrentSpeed()
    {
        return current_speed;
    }

    public void setCurrentSpeed(String CurrentSpeed)
    {
        this.current_speed = CurrentSpeed;
    }
                                                   
}