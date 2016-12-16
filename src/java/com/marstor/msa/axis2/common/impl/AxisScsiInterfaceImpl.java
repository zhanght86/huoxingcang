/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.common.impl;

import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;

/**
 *
 * @author tianwenbo
 */
public class AxisScsiInterfaceImpl {
    
    private SCSIInterface scsi;
    
    public AxisScsiInterfaceImpl()
    {
        scsi = InterfaceFactory.getSCSIInterfaceInstance();
    }

    public String getAllTarget()
    {
        MessageContext context = MessageContext.getCurrentMessageContext();
        ServiceContext serviceContext = context.getServiceContext();
        System.out.println("scsi serviceContext1="+serviceContext.getProperty("user"));
//        ServiceGroupContext groupContext = context.getServiceGroupContext();
//        System.out.println("scsi groupContext2="+groupContext.getProperty("user"));
        return (String)serviceContext.getProperty("user");
    }
    
    public String getAllTarget2()
    {
        AxisLoginServices service = new AxisLoginServices();
        return service.checklogin();
    }
}
