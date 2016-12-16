/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.model.Address;
import com.marstor.msa.oracle.model.NSNameInstance;
import com.marstor.msa.oracle.bean.NetServiceName;
import com.marstor.msa.oracle.bean.NetServiceNameNotSaved;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.util.encrypt.MyEncryp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "NSNameBean")
@ViewScoped
public class NetServiceNameBean implements Serializable {

    private String nsName = "";
    private NSNameInstance selectedNSName = null;
    private ArrayList<NSNameInstance> nsNameList = new ArrayList<NSNameInstance>();
    private List<String> netServiceNameList = null;
    private String user = "";
    private String pass = "";
    private NetServiceNameNotSaved notSave = null;
    private MSAResource res = new MSAResource();

    public NetServiceNameBean() {
        List<NetServiceName> netServiceNameInfo = InterfaceFactory.getOracleInterfaceInstance().getNetServiceNameInfo();

        for (int i = 0; i < netServiceNameInfo.size(); i++) {
            List<Address> list = new ArrayList<Address>();

            HashMap map = netServiceNameInfo.get(i).ipMap;
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String ip = (String) iterator.next();
                String port = (String) map.get(ip);
                list.add(new Address(ip, port));
            }

            NSNameInstance nsname = new NSNameInstance(netServiceNameInfo.get(i).netServiceName,
                    netServiceNameInfo.get(i).dbServiceName, list);

            nsNameList.add(nsname);
        }
    }

    public ArrayList<NSNameInstance> getNetServiceName() {
        return nsNameList;
    }

    public NSNameInstance getSelectedNSName() {
        return this.selectedNSName;
    }

    public void setSelectedNSName(NSNameInstance selectedNSName) {
        this.selectedNSName = selectedNSName;
    }

    public void deleteNSName() {
        if (InterfaceFactory.getOracleInterfaceInstance().deleteNSName(selectedNSName.getNetServiceName())) {
            nsNameList.remove(this.selectedNSName);
        } else {
            FacesContext context = FacesContext.getCurrentInstance();  //取session域      
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DeleteNetServiceNameFailed"), ""));
        }
    }

    public int getNSNameNumber() {
        return nsNameList.size();
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }
       
    /**
     * 添加新的网络服务名
     *
     * @param nsName
     * @param dbName
     * @param list
     * @return
     */
    public String addNewNSName(String nsName, String dbName, List<Address> list) {
        FacesContext context = FacesContext.getCurrentInstance();  //取session域


        if ("".equals(nsName)) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NetServiceNameIllegal"), ""));
            return null;
        }

        if ("".equals(dbName)) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DatabaseNameIllegal"), ""));
            return null;
        }

        if (list.size() < 1) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("IPCannotBeNull"), ""));
            return null;
        }

        NetServiceName netServiceName = new NetServiceName();
        netServiceName.netServiceName = nsName;
        netServiceName.dbServiceName = dbName;
        for (int i = 0; i < list.size(); i++) {
            netServiceName.ipMap.put(list.get(i).ip, list.get(i).port);
        }

        boolean save = InterfaceFactory.getOracleInterfaceInstance().modifyNSName(1, netServiceName);
        if (!save) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("AddNetServiceNameFailed"), ""));
            return null;
        }

        NSNameInstance newNSName = new NSNameInstance();
        newNSName.setNetServiceName(nsName);
        newNSName.setDBServiceName(dbName);
        newNSName.setList(list);

        NetServiceNameBean nsNameBean = (NetServiceNameBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{NSNameBean}", NetServiceNameBean.class).getValue(context.getELContext());
        nsNameBean.getNetServiceName().add(newNSName);

        return "oracle_nsname?faces-redirect=true";
    }

    /**
     * 给一个网络服务名添加新的IP
     *
     * @param ip
     * @param port
     */
    public void addIPToNSName(String ip, String port) {
        FacesContext context = FacesContext.getCurrentInstance();  //取session域

        if ("".equals(ip)) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ErrorIP"), ""));
            return;
        }

        if ("".equals(port)) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ErrorPort"), ""));
            return;
        }

        NetServiceNameBean nsNameBean = (NetServiceNameBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{NSNameBean}", NetServiceNameBean.class).getValue(context.getELContext());
        NSNameInstance nsNameInstance = nsNameBean.getSelectedNSName();

        NetServiceName nsName = new NetServiceName();
        nsName.netServiceName = nsNameInstance.getNetServiceName();
        nsName.dbServiceName = nsNameInstance.getDBServiceName();
        List<Address> list = nsNameInstance.getList();
        for (int i = 0; i < list.size(); i++) {
            nsName.ipMap.put(list.get(i).ip, list.get(i).port);
        }
        nsName.ipMap.put(ip, port);

        boolean save = InterfaceFactory.getOracleInterfaceInstance().modifyNSName(2, nsName);
        if (!save) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ModifyNetServiceNameFailed"), ""));
            return;
        }

        NSNameInstance newNSName = new NSNameInstance();
        newNSName.setNetServiceName(nsNameInstance.getNetServiceName());
        newNSName.setDBServiceName(nsNameInstance.getDBServiceName());
        newNSName.setList(nsNameInstance.getList());
        Address address = new Address(ip, port);
        newNSName.getList().add(address);

        nsNameBean.remove();
        nsNameBean.getNetServiceName().add(newNSName);

        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("AddIPSuccess"), ""));
    }

    /**
     * 测试一个没有被保存过的网络服务名
     *
     * @param user
     * @param pass
     * @return
     */
    public void testBeforeSave(String user, String pass) {
        FacesContext context = FacesContext.getCurrentInstance();
        if ("".equals(user)) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NetServiceNameIllegal"), ""));
            return;
        }

        if ("".equals(pass)) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DatabaseNameIllegal"), ""));
            return;
        }

        NSNameInstance nsNameInstance = (NSNameInstance) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{NSName}", NSNameInstance.class).getValue(context.getELContext());

        NetServiceNameNotSaved nsName = new NetServiceNameNotSaved();
        nsName.user = user;
        nsName.password = pass;
        nsName.netServiceName = nsNameInstance.getNetServiceName();
        nsName.dbServiceName = nsNameInstance.getDBServiceName();

        List<Address> list = nsNameInstance.getList();
        for (int i = 0; i < list.size(); i++) {
            nsName.ipMap.put(list.get(i).ip, list.get(i).port);
        }

        boolean test = InterfaceFactory.getOracleInterfaceInstance().testNSName(nsName);

        if (test) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("LinkSuccess"), ""));
        } else {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("LinkFailed"), ""));
        }
    }

    public void setNSName(String nsName) {
        this.nsName = nsName;
    }

    public String getSNName() {
        return nsName;
    }

    public List<String> getNetServiceNameList() {
        netServiceNameList = new ArrayList<String>();
        for (int i = 0; i < nsNameList.size(); i++) {
            netServiceNameList.add(nsNameList.get(i).getNetServiceName());
        }
        if (0 == nsNameList.size()) {
            netServiceNameList.add("");
        }

        return netServiceNameList;
    }

    private void remove() {
        nsNameList.remove(selectedNSName);
    }

    public void testConnect() {
        if (this.user.equals("") || this.pass.equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UserOrPasswordNotNULl"), ""));
            RequestContext.getCurrentInstance().execute("dlg.hide()");
            return;
        }


        NetServiceName singleNSName = null;
        try {
            singleNSName = InterfaceFactory.getOracleInterfaceInstance().getSingleNSName(this.selectedNSName.getNetServiceName());
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("FindNetServiceNameInfoFailed"), ""));
            RequestContext.getCurrentInstance().execute("dlg.hide()");
            this.user = "";
            this.pass = "";
            return;
        }

        notSave = new NetServiceNameNotSaved();
        notSave.netServiceName = singleNSName.netServiceName;
        notSave.dbServiceName = singleNSName.dbServiceName;
        notSave.ipMap = singleNSName.ipMap;
        notSave.user = user;
        System.out.println(user + pass);
        notSave.password = String.valueOf(MyEncryp.Encode64(pass.toCharArray()));

        boolean testNSName = InterfaceFactory.getOracleInterfaceInstance().testNSName(notSave);
        if (testNSName) {
            FacesContext context = FacesContext.getCurrentInstance();  //取session域
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("TestContectSuccess"), ""));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();  //取session域     
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("TesetContectFailed"), ""));
        }
        RequestContext.getCurrentInstance().execute("dlg.hide()");
        this.user = "";
        this.pass = "";
    }

    public void closeDialog() {
        RequestContext.getCurrentInstance().execute("dlg.hide()");
    }

    public void showDialog() {
        this.user = "";
        this.pass = "";
        RequestContext.getCurrentInstance().execute("dlg.show()");
    }

    public void deleteConfirm() {
        RequestContext.getCurrentInstance().execute("deletecomfirm.show()");
    }
}
