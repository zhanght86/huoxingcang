/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.oracle.model.Channel;
import com.marstor.msa.oracle.model.DBInstance;
import com.marstor.msa.oracle.bean.DatabaseName;
import com.marstor.msa.oracle.bean.NetServiceName;
import com.marstor.msa.oracle.bean.NetServiceNameNotSaved;
import com.marstor.msa.oracle.bean.Node;
import com.marstor.msa.oracle.validator.OracleValidator;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "createDBBean")
@SessionScoped 
public class CreateDBBean implements Serializable{

    private DBInstance instance = new DBInstance();
    private Channel sChannel = null;
    private MSAResource res = new MSAResource();

    public CreateDBBean() {
    }

    public void setSChannel(Channel sChannel) {
        this.sChannel = sChannel;
    }

    public Channel getSChannel() {
        return sChannel;
    }

    public void setInstance(DBInstance instance) {
        this.instance = instance;
    }

    public DBInstance getInstance() {
        return instance;
    }

    public String testConnect() {
        if (instance.getUser().equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UserNameError"), ""));
            return null;
        }

        if (instance.getPass().equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("PasswordError"), ""));
            return null;
        }
        NetServiceName singleNSName = InterfaceFactory.getOracleInterfaceInstance().getSingleNSName(instance.getNetServiceName());
        NetServiceNameNotSaved notSave = new NetServiceNameNotSaved();

        notSave.netServiceName = singleNSName.netServiceName;
        notSave.dbServiceName = singleNSName.dbServiceName;
        notSave.ipMap = singleNSName.ipMap;
        notSave.user = instance.getUser();
        notSave.password = String.valueOf(MyEncryp.Encode64(instance.getPass().toCharArray()));

        boolean testNSName = InterfaceFactory.getOracleInterfaceInstance().testNSName(notSave);
        if(testNSName){
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("TestContectSuccess") , ""));
            return null;
        }else{
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("TesetContectFailed"), ""));
            return null;
        }
    }

    public String addDatabase() {
        if (!OracleValidator.checkNetServiceName(instance.getDBName()) ){
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DatabaseNameError"), ""));
            return null;
        }

        if (instance.getUser().equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UserNameError"), ""));
            return null;
        }

        if (instance.getPass().equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("PasswordError"), ""));
            return null;
        }

        if (instance.getList().size() < 1) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ChannelCannotBeNull"), ""));
            return null;
        }

        DatabaseName db = new DatabaseName();
        db.databaseName = instance.getDBName();
        db.netServiceName = instance.getNetServiceName();
        db.user = instance.getUser();
        db.password = String.valueOf(MyEncryp.Encode64(instance.getPass().toCharArray()));

        for (Channel c : instance.getList()) {
            Node node = new Node();
            node.nodeNetServiceName = c.getNSName();
            node.nodeMap.put(c.getName(), c.getPath());
            db.list.add(node);
        }
        boolean modifyDBNameInfo = InterfaceFactory.getOracleInterfaceInstance().modifyDBNameInfo(1, db);
        if (!modifyDBNameInfo) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("AddDBFailed"), ""));
            return null;
        } else {
            return "oracle_database?faces-redirect=true";
        }
    }

    public String addChannel(String channelName, String nsName, String path) {
         for(Channel c : instance.getList()){
            if(channelName.equals(c.getName())){
                FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ChannelNameExist"), ""));
                return null;
            }
        }
        
        if(!OracleValidator.checkChannelName(channelName)){
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ChannelNameError"), ""));
            return null;
        }
        
        if(!OracleValidator.checkRMANPath(path)){
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ChannelPathError"), ""));
            return null;
        }
        
        path=path.replaceAll("\\\\", "/");
      //  System.out.println("\\192.168.1.105\\share".replaceAll("\\\\", "/"));
        System.out.println(channelName + nsName + path);
        Channel channel = new Channel();
        channel.setName(channelName);
        channel.setNSName(nsName);
        channel.setPath(path);
        boolean add = instance.getList().add(channel);

        if (add) {
            
            return "oracle_add_database?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("AddChannelFailed"), ""));
            return null;
        }
    }

    public void deleteChannel() {
        boolean remove = instance.getList().remove(this.sChannel);
        if (!remove) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DeleteChannelFailed"), ""));
        }
    }

    public String init() {
        instance = new DBInstance();
        sChannel = null;
        return "oracle_add_database?faces-redirect=true";
    }
}
