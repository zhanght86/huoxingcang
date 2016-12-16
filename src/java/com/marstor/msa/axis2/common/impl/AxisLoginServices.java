/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.common.impl;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.LoginBean;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;

/**
 *
 * @author tianwenbo
 */
public class AxisLoginServices {

    public int login(String username,String password) {
        System.out.println("AxisLoginServices login");
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        SystemUserInformation user = common.login(username, password, 0);
        if(user == null){
            return 0;
        }
        return user.type;
//        MessageContext context = MessageContext.getCurrentMessageContext();
//        ServiceContext serviceContext = context.getServiceContext();
//        serviceContext.setProperty("user", user);
//        ServiceGroupContext groupContext = context.getServiceGroupContext();
//        groupContext.setProperty("user", user);
//        return true;
    }

    public String checklogin() {
        MessageContext context = MessageContext.getCurrentMessageContext();
        ServiceContext serviceContext = context.getServiceContext();
        System.out.println("login serviceContext1="+serviceContext.getProperty("user"));
//        ServiceGroupContext groupContext = context.getServiceGroupContext();
//        System.out.println("login groupContext2="+groupContext.getProperty("user"));
        return (String)serviceContext.getProperty("user");
    }
    
    public boolean logout(){
        MessageContext context = MessageContext.getCurrentMessageContext();
        ServiceContext serviceContext = context.getServiceContext();
//        serviceContext.
        return true;
    }
    
    

}
