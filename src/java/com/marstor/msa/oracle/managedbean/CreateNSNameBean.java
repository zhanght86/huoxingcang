/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.oracle.model.Address;
import com.marstor.msa.oracle.model.NSNameInstance;
import com.marstor.msa.oracle.bean.NetServiceName;
import com.marstor.msa.oracle.bean.NetServiceNameNotSaved;
import com.marstor.msa.oracle.validator.OracleValidator;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.util.encrypt.MyEncryp;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "createNSNameBean")
@SessionScoped
public class CreateNSNameBean implements Serializable {

    private MSAResource res = new MSAResource();
    private NSNameInstance nsName = new NSNameInstance();
    private boolean bSave = true;
    NetServiceNameNotSaved notSave = null;
    private Address selectedAdd = new Address();
    private String user = "";
    private String pass = "";

    public CreateNSNameBean() {
    }
    
    public String jumpToCreate(){
        this.nsName = new NSNameInstance();
        return "oracle_add_nsname?faces-redirect=true";
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

    public void setNsName(NSNameInstance nsName) {
        this.nsName = nsName;
    }

    public NSNameInstance getNsName() {
        return nsName;
    }

    public String listAdd(String ip, String port) {

        if (!ValidateUtility.checkIP(ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ErrorIP"), ""));
            return null;
        }

        if (!OracleValidator.checkPort(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  res.get("ErrorPort"), ""));
            return null;
        }

        List<Address> list = nsName.getList();
        for (Address address : list) {
            if (address.ip.equals(ip) && address.port.equals(port)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  res.get("AddressExist"), ""));
                return null;
            }
        }

        boolean add = nsName.getList().add(new Address(ip, port));

        if (!add) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("AddIPFailed"), ""));
            return null;
        }

        return "oracle_add_nsname?faces-redirect=true";

    }

    public void deleteAddress() {

        for (int i = 0; i < nsName.getList().size(); i++) {
            if (nsName.getList().get(i).ip.equals(selectedAdd.ip) && nsName.getList().get(i).port.equals(selectedAdd.port)) {
                nsName.getList().remove(i);
            }
        }
    }

    public String saveNetServiceName() {
        NetServiceName net = new NetServiceName();
        net.netServiceName = nsName.getNetServiceName();
        net.dbServiceName = nsName.getDBServiceName();

        List<NetServiceName> netServiceNameInfo = InterfaceFactory.getOracleInterfaceInstance().getNetServiceNameInfo();
        for (NetServiceName name : netServiceNameInfo) {
            if (name.netServiceName.equals(nsName.getNetServiceName())) {
                FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NetServiceNameExist"), ""));
                return null;
            }
        }

        if (net.netServiceName.equals("") || !OracleValidator.checkNetServiceName(net.netServiceName)) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NetServiceNameIllegal"), ""));
            return null;
        }

//        if (net.dbServiceName.equals("") || !OracleValidator.checkNetServiceName(net.dbServiceName)) {
//            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DatabaseNameIllegal"), ""));
//            return null;
//        }

        if (0 >= nsName.getList().size()) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("IPCannotBeNull"), ""));
            return null;
        }

        for (Address add : nsName.getList()) {
            net.ipMap.put(add.ip, add.port);
        }
        boolean modifyNSName = InterfaceFactory.getOracleInterfaceInstance().modifyNSName(1, net);

        if (modifyNSName) {
            return "oracle_nsname?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("AddNetServiceNameFailed"), ""));
            return null;
        }
    }

    public void setBSave(boolean bSave) {
        this.bSave = bSave;
    }

    public boolean getBSave() {
        System.out.println(bSave);
        return bSave;
    }

    public String testConnect() {
        notSave = new NetServiceNameNotSaved();
        notSave.netServiceName = nsName.getNetServiceName();
        notSave.dbServiceName = nsName.getDBServiceName();

        List<NetServiceName> netServiceNameInfo = InterfaceFactory.getOracleInterfaceInstance().getNetServiceNameInfo();
        for (NetServiceName name : netServiceNameInfo) {
            if (name.netServiceName.equals(nsName.getNetServiceName())) {
                FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NetServiceNameExist"), ""));
                return null;
            }
        }

        if (!OracleValidator.checkNetServiceName(notSave.netServiceName)) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NetServiceNameIllegal"), ""));
            return null;
        }

//        if (!OracleValidator.checkNetServiceName(notSave.dbServiceName)) {
//            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NetServiceNameIllegal"), ""));
//            return null;
//        }

        if (0 >= nsName.getList().size()) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("IPCannotBeNull"), ""));
            return null;
        }

        for (int i = 0; i < nsName.getList().size(); i++) {
            notSave.ipMap.put(nsName.getList().get(i).ip, nsName.getList().get(i).port);
        }

        if (this.user.equals("") || this.pass.equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR,  res.get("UserOrPasswordNotNULl"), ""));
            RequestContext.getCurrentInstance().execute("dlg.hide()");
            this.user = "";
            this.pass = "";
            return null;
        }
        
        notSave.user = this.user;
        notSave.password = String.valueOf(MyEncryp.Encode64(pass.toCharArray()));
        boolean testNSName = InterfaceFactory.getOracleInterfaceInstance().testNSName(notSave);

        if (testNSName) {
            FacesContext context = FacesContext.getCurrentInstance();  //È¡sessionÓò
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("TestContectSuccess"), ""));
            this.bSave = false;
        } else {
            FacesContext context = FacesContext.getCurrentInstance();  //È¡sessionÓò      
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("TesetContectFailed"), ""));
        }

        RequestContext.getCurrentInstance().execute("dlg.hide()");
        this.user = "";
        this.pass = "";
        return null;
    }

    public NetServiceNameNotSaved getNotSave() {
        return this.notSave;
    }

    public void setSelectedAdd(Address add) {
        this.selectedAdd = add;
    }

    public Address getSelectedAdd() {
        return selectedAdd;
    }

    public void closeDialog() {
        RequestContext.getCurrentInstance().execute("dlg.hide()");
        this.user = "";
        this.pass = "";
    }
}
