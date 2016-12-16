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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "channelBean")
@SessionScoped
public class ChannelBean implements Serializable{
    private MSAResource res = new MSAResource();
    private DBInstance instance;
    private Channel sChannel;
    private String dbName;
    private String jobName;
    private Map<String,List<Channel>> Allchannelmap=new HashMap();
    
    public ChannelBean() {
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

    public String dbProperty(String dbName, String jobName) {
        this.dbName = dbName;
        this.jobName = jobName;

        List<DatabaseName> dbNameList = InterfaceFactory.getOracleInterfaceInstance().getDBInfo();
        
        for (int i = 0; i < dbNameList.size(); i++) {
            if (dbNameList.get(i).databaseName.equals(dbName)) {
                ArrayList<Node> nodeList = dbNameList.get(i).list;
                List<Channel> channelList = new ArrayList<Channel>();
                
                if (0 != nodeList.size()) {
                    for (int y = 0; y < nodeList.size(); y++) {

                        HashMap<String, String> map = nodeList.get(y).nodeMap;
                        for (String name : map.keySet()) {
                            String path = map.get(name);
                            channelList.add(new Channel(name, path, nodeList.get(y).nodeNetServiceName));
                        }
                        this.Allchannelmap.put(nodeList.get(y).nodeNetServiceName, channelList);
                    }
                }
                    
                
                channelList=this.Allchannelmap.get(dbNameList.get(i).netServiceName);
                instance = new DBInstance(dbNameList.get(i).databaseName, dbNameList.get(i).user,
                     String.valueOf( MyEncryp.Decode( dbNameList.get(i).password.toCharArray())), 
                        channelList, dbNameList.get(i).netServiceName);
            }
        }
        return "oracle_channel?faces-redirect=true";
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
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("TestContectSuccess"), ""));
            return null;
        }else{
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("TesetContectFailed"), ""));
            return null;
        }
    }
    
    
    public String modifyDatabase() {
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
        boolean modifyDBNameInfo = InterfaceFactory.getOracleInterfaceInstance().modifyDBNameInfo(2, db);
        if (!modifyDBNameInfo) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("ChangeDBChannelConfigError"), ""));
            return null;
        } else {
            String param = "dbName=" + dbName + "&jobName=" + jobName+"&active=" + 2;
            return "create_restore_script?faces-redirect=true" + param;
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
       
        Channel channel = new Channel();
        channel.setName(channelName);
        channel.setNSName(nsName);
        channel.setPath(path);
        List<Channel> channellist = Allchannelmap.get(nsName);
        if(channellist==null)
        {
            channellist=new ArrayList<Channel>();
            Allchannelmap.put(nsName, channellist);
        }
        boolean add = channellist.add(channel);
        Set<String> keySet = Allchannelmap.keySet();
        System.out.println(Thread.currentThread().getStackTrace()[1].getClassName()+":"+Thread.currentThread().getStackTrace()[1].getMethodName()+"__oracle模块通道添加输出信息:");
        for(String str:keySet)
        {
            System.out.print("网络服务名（networkServiceName）："+str+"含有如下通道：");
            for(Channel channel2:Allchannelmap.get(str))
            {
                System.out.print(channel2.getName()+":");
            }
            System.out.println("");
        }
        
        this.instance.setList(Allchannelmap.get(nsName));
        this.instance.setNetServiceName(nsName);
        System.out.println(Thread.currentThread().getStackTrace()[1].getClassName()+":"+Thread.currentThread().getStackTrace()[1].getMethodName()+"__oracle模块通道添加输出信息结束");
        if (add) {
            return "oracle_channel?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("AddChannelFailed"), ""));
            return null;
        }
    }
    
    public void deleteChannel(){
        boolean remove = instance.getList().remove(sChannel);
        if(!remove){
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DeleteChannelFailed"), ""));
        }
    }
    
    public String backToRestore(){
        String param = "dbName=" + dbName + "&jobName="  + jobName + "&active=" + 2  ;
        
        return "create_restore_script?faces-redirect=true" + param;
    }
    public void changeNetName()
    {
//        System.out.println(this.instance.getNetServiceName());
        //临时channellist，从全部channel中截取一部分网络服务相对应的，存入当前的channellist
        List<Channel> channellist = this.Allchannelmap.get(this.instance.getNetServiceName());
        if(channellist==null)
        {
            channellist=new ArrayList<Channel>();
            this.Allchannelmap.put(this.instance.getNetServiceName(), channellist);
        }
        
        this.instance.setList(channellist);
    }
    
}
