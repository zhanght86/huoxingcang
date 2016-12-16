/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.servlet;

import com.marstor.msa.common.managedbean.LoginBean;
import com.marstor.msa.util.InterfaceFactory;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author tianwenbo
 */
public class MSASessionLisener implements HttpSessionListener {
 
  private static int totalActiveSessions;
  public static HttpSession adminSession = null;
  public static int getTotalActiveSession(){
	return totalActiveSessions;
  }
 
  @Override
  public void sessionCreated(HttpSessionEvent arg0) {
	totalActiveSessions++;
	System.out.println("sessionCreated - add one session into counter");
  }
 
  @Override
  public void sessionDestroyed(HttpSessionEvent arg0) {
	totalActiveSessions--;
        arg0.getSession().equals(MSASessionLisener.adminSession);
        {
            System.out.println("admin session destroy");
            MSASessionLisener.adminSession = null;
           
        }
         System.out.println("¼àÊÓÆ÷¹Ø±Õ");
            InterfaceFactory.getMonitorInterfaceInstance().stopThreads();
	System.out.println("sessionDestroyed - deduct one session from counter");
  }	
}
    

