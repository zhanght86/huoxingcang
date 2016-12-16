/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.util;

/**
 *
 * @author Li Mengyang <li.mengyang@marstor.com>
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
 
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
 
@ManagedBean
public class TabbedView {
     
     
    public void onTabChange(TabChangeEvent event) {
        System.out.println("TabChanged : " + event.getTab().getTitle());
//        FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " + event.getTab().getTitle());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().execute("javascript:alert('"+ event.getTab().getTitle() +"');");
        RequestContext.getCurrentInstance().execute("javascript:toECD();");
    }
         
    public void onTabClose(TabCloseEvent event) {
        FacesMessage msg = new FacesMessage("Tab Closed", "Closed tab: " + event.getTab().getTitle());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}