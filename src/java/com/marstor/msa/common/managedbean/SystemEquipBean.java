/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.SystemConfig;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "systemEquip")
@ViewScoped
public class SystemEquipBean implements Serializable {

    private MSAResource res = new MSAResource();
    private List<SystemConfig> list = new ArrayList<SystemConfig>();

    public List<SystemConfig> getList() {
        return list;
    }

    public void setList(List<SystemConfig> list) {
        this.list = list;
    }

    public SystemEquipBean() {
        initList();
    }

    private void initList() {

        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = f.format(date);


        SystemConfig config = new SystemConfig();
        config.setName("NAS");
        config.setTime(format);
        config.setUser("lucy");

        SystemConfig config1 = new SystemConfig();
        config1.setName("ÐéÄâ»ú");
        config1.setTime(format);
        config1.setUser("lucy");

        SystemConfig config2 = new SystemConfig();
        config2.setName("ÏµÍ³");
        config2.setTime(format);
        config2.setUser("lucy");

        list.add(config);
        list.add(config1);
        list.add(config2);
    }

    public void reboot() {
        RequestContext.getCurrentInstance().execute("window.top.location.href='../volume/reboot.xhtml'");
    }
    
    public void reboot1() {
        RequestContext.getCurrentInstance().execute("window.top.location.href='../volume/reboot.xhtml?init=1'");
    }
    
    public void reboot0() {
        RequestContext.getCurrentInstance().execute("window.top.location.href='../volume/reboot.xhtml?init=0'");
    }
    
    public void rebootQ1() {
        RequestContext.getCurrentInstance().execute("rebootQ.hide()");
        RequestContext.getCurrentInstance().execute("reboot1.show()");
    }
    
    public void rebootQ0() {
        RequestContext.getCurrentInstance().execute("rebootQ.hide()");
        RequestContext.getCurrentInstance().execute("reboot0.show()");
    }

    public void closeReboot() {
        RequestContext.getCurrentInstance().execute("reboot.hide();");
    }
    
    public void shutdown() {
        boolean powerOff = InterfaceFactory.getCommonInterfaceInstance().powerOff();
        RequestContext.getCurrentInstance().execute("window.top.location.href='shutdown.xhtml'");
    }

    public void closeShutdown() {
        RequestContext.getCurrentInstance().execute("shutdown.hide();");
    }
}
