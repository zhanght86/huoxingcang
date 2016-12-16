/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.Constant;
import com.marstor.msa.nas.bean.NFSParametersForSharePath;
import com.marstor.msa.nas.validator.IPValidate;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.component.html.HtmlCommandButton;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ConfigNFSBean implements Serializable{

    private String path;
    private HtmlCommandButton button;
    private boolean status;
    private boolean anonoyRW, anonoyRWEnable;
    private boolean anonoyVisit, anonoyVisitEnable;
    private boolean openOrNot;
    private boolean previousOpenOrNot;
    private boolean buttonEnable;
    private ArrayList<String> rw = new ArrayList<String>(); //有读写权限的IP地址
    private ArrayList<String> readOnly = new ArrayList<String>();
    private ArrayList<String> root = new ArrayList<String>();
    private String statusDisplay;
    private String rwIP;
    private String selectedRW = "";
    private String roIP;
    private String selectedRO = "";
    private String rootIP;
    private String selectedRoot = "";

    public ConfigNFSBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        NFSParametersForSharePath parameters = InterfaceFactory.getNASInterfaceInstance().getSpecifiedNFSParameters(path);

//        String startOrNot = parameters.isStart() ? "1" : "0";
//        String anonoyVisit = parameters.isAnonymousCanVisit() ? "1" : "0";
//        String anonoyRW = parameters.isAnonymousCanReadWrite() ? "1" : "0";
        if (parameters == null) {
            return;
        }
        if (parameters.isStart()) {
            this.previousOpenOrNot = true;
            this.openOrNot = true;
        } else {
            this.previousOpenOrNot = false;
            this.openOrNot = false;
        }
        // this.openOrNot = parameters.isStart();
        if (!this.openOrNot) {
            this.anonoyRWEnable = true;
            this.anonoyVisitEnable = true;
            this.buttonEnable = true;
        } else {
            this.anonoyRWEnable = false;
            this.anonoyVisitEnable = false;
            this.buttonEnable = false;
        }

        this.anonoyVisit = parameters.isAnonymousCanVisit();
        this.anonoyRW = parameters.isAnonymousCanReadWrite();
        Debug.print("nfs annoyvisit : " + this.anonoyVisit);
        Debug.print("nfs rw : " + this.anonoyRW);
        String rwList = parameters.getRwIpList();
        String roList = parameters.getRoIpList();
        String rootList = parameters.getCanAsRootVisitIP();
        // this.path = request.getParameter("path");
//        String startOrNot = request.getParameter("startOrNot");
//        if (startOrNot == null) {
//            return;
//        }
//        String anonoyVisit = request.getParameter("anonoyVisit");
//        if (anonoyVisit == null) {
//            return;
//        }
//        String anonoyRW = request.getParameter("anonoyRW");
//        if (anonoyRW == null) {
//            return;
//        }
//        String rwList = request.getParameter("rwList");
//        if (rwList == null) {
//            return;
//        }
//        String roList = request.getParameter("roList");
//        if (roList == null) {
//            return;
//        }
//        String rootList = request.getParameter("rootList");
//        if (rootList == null) {
//            return;
//        }
//        if (startOrNot.equals("1")) {
//            this.status = true;
//        } else {
//            this.status = false;
//        }
//        if (anonoyVisit.equals("1")) {
//            this.anonoyVisit = true;
//        } else {
//            this.anonoyVisit = false;
//        }
//        if (anonoyRW.equals("1")) {
//            this.anonoyRW = true;
//
//        } else {
//            this.anonoyRW = false;
//        }
        String array[];
        if (!rwList.equals("")) {
            array = rwList.trim().split(":");
            if (array != null && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    this.rw.add(array[i]);
                }
            }
        }
        if (!roList.equals("")) {
            array = roList.trim().split(":");
            if (array != null && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    this.readOnly.add(array[i]);
                }
            }

        }
        if (!rootList.equals("")) {
            array = rootList.trim().split(":");
            if (array != null && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    this.root.add(array[i]);
                }
            }

        }
        if (rwList.equals("") && roList.equals("")) {
            this.readOnly.add("*");
        }


    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isAnonoyRWEnable() {
        return anonoyRWEnable;
    }

    public void setAnonoyRWEnable(boolean anonoyRWEnable) {
        this.anonoyRWEnable = anonoyRWEnable;
    }

    public boolean isAnonoyVisitEnable() {
        return anonoyVisitEnable;
    }

    public void setAnonoyVisitEnable(boolean anonoyVisitEnable) {
        this.anonoyVisitEnable = anonoyVisitEnable;
    }

    public boolean isOpenOrNot() {
        return openOrNot;
    }

    public void setOpenOrNot(boolean openOrNot) {
        this.openOrNot = openOrNot;
    }

    public boolean isButtonEnable() {
        return buttonEnable;
    }

    public void setButtonEnable(boolean buttonEnable) {
        this.buttonEnable = buttonEnable;
    }

    public String getSelectedRoot() {
        return selectedRoot;
    }

    public void setSelectedRoot(String selectedRoot) {
        this.selectedRoot = selectedRoot;
    }

    public String getRootIP() {
        return rootIP;
    }

    public void setRootIP(String rootIP) {
        this.rootIP = rootIP;
    }

    public String getSelectedRO() {
        return selectedRO;
    }

    public void setSelectedRO(String selectedRO) {
        this.selectedRO = selectedRO;
    }

    public String getSelectedRW() {
        return selectedRW;
    }

    public void setSelectedRW(String selectedRW) {
        this.selectedRW = selectedRW;
    }

    public String getRwIP() {
        return rwIP;
    }

    public void setRwIP(String rwIP) {
        this.rwIP = rwIP;
    }

    public HtmlCommandButton getButton() {
        return button;
    }

    public void setButton(HtmlCommandButton button) {
        this.button = button;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public boolean isAnonoyRW() {
        return anonoyRW;
    }

    public void setAnonoyRW(boolean anonoyRW) {
        this.anonoyRW = anonoyRW;
    }

    public boolean isAnonoyVisit() {
        return anonoyVisit;
    }

    public void setAnonoyVisit(boolean anonoyVisit) {
        this.anonoyVisit = anonoyVisit;
    }

    public ArrayList<String> getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(ArrayList<String> readOnly) {
        this.readOnly = readOnly;
    }

    public ArrayList<String> getRoot() {
        return root;
    }

    public void setRoot(ArrayList<String> root) {
        this.root = root;
    }

    public ArrayList<String> getRw() {
        return rw;
    }

    public void setRw(ArrayList<String> rw) {
        this.rw = rw;
    }

    public String getRoIP() {
        return roIP;
    }

    public void setRoIP(String roIP) {
        this.roIP = roIP;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        if (this.status) {
            this.statusDisplay = Constant.open;
        } else {
            this.statusDisplay = Constant.notOpen;
        }
    }

    public void addRWIP() {
        if (this.rw.contains("*")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("rwExistAll"), resource.get("rwExistAll")));
            return;
        }
        if (this.rwIP.equals("")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("emptyIP"), resource.get("emptyIP")));
            return;
        }
        if (this.rw.contains(this.rwIP)) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("exist"), resource.get("exist")));
            return;
        }
        if (this.readOnly.contains(this.rwIP)) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("existInRO"), resource.get("existInRO")));
            return;
        }
        if (!IPValidate.isIp(this.rwIP) && !this.rwIP.equals("*")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("invalidIP"), resource.get("invalidIP")));
            return;
        }

        this.rw.add(this.rwIP);
    }

    public void delRWIP() {
        if (this.selectedRW == null || this.selectedRW.equals("")) {
            // selectNothing
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("selectNothing"), resource.get("selectNothing")));
            return;
        }
        this.rw.remove(this.selectedRW);
    }

    public void addROIP() {
        if (this.readOnly.contains("*")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("roExistAll"), resource.get("roExistAll")));
            return;
        }
        if (this.roIP.equals("")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("emptyIP"), resource.get("emptyIP")));
            return;
        }
        if (this.readOnly.contains(this.roIP)) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("exist"), resource.get("exist")));
            return;
        }
        if (this.rw.contains(this.roIP)) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("existInRW"), resource.get("existInRW")));
            return;
        }
        if (!IPValidate.isIp(this.roIP) && !this.roIP.equals("*")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("invalidIP"), resource.get("invalidIP")));
            return;
        }

        this.readOnly.add(this.roIP);
    }

    public void delROIP() {
        if (this.selectedRO == null || this.selectedRO.equals("")) {
            // selectNothing
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("selectNothing"), resource.get("selectNothing")));
            return;
        }
        this.readOnly.remove(this.selectedRO);
    }

    public void addRootIP() {

        if (this.root.contains(this.rootIP)) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("exist"), resource.get("exist")));
            return;
        }
        if (this.rootIP.equals("")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("emptyIP"), resource.get("emptyIP")));
            return;
        }
        if (this.rootIP.equals("*")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("rootNotAll"), resource.get("rootNotAll")));
            return;
        }
        if (!IPValidate.isIp(this.rootIP) && !this.rootIP.equals("*")) {
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("invalidIP"), resource.get("invalidIP")));
            return;
        }
        this.root.add(this.rootIP);
    }

    public void delRootIP() {
        if (this.selectedRoot == null || this.selectedRoot.equals("")) {
            // selectNothing
            MSAResource resource = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("selectNothing"), resource.get("selectNothing")));
            return;
        }
        this.root.remove(this.selectedRoot);
    }

    public void changeOpen() {
        if (!this.openOrNot) {
            this.anonoyRWEnable = true;
            this.anonoyVisitEnable = true;
            this.buttonEnable = true;
        } else {
            this.anonoyRWEnable = false;
            this.anonoyVisitEnable = false;
            this.buttonEnable = false;
        }
    }

    public String save() {
        if (!this.openOrNot) {
            if (this.previousOpenOrNot) {
                RequestContext.getCurrentInstance().execute("turnoffnfs.show()");
                return null;
            }

        } else {
            String rwIPs = "", roIPs = "", rootIPs = "";
            if (this.readOnly.size() == 0 && this.rw.size() == 0) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("atLeastOne"), res.get("atLeastOne")));
                return null;
            }
            for (int i = 0; i < this.rw.size(); i++) {
                rwIPs = rwIPs + rw.get(i) + ":";
            }
            for (int i = 0; i < this.readOnly.size(); i++) {
                roIPs = roIPs + readOnly.get(i) + ":";
            }
            for (int i = 0; i < this.root.size(); i++) {
                rootIPs = rootIPs + this.root.get(i) + ":";
            }
            Debug.print("nfs rwIPs : " + rwIPs);
            Debug.print("nfs roIPs : " + roIPs);
            Debug.print("nfs rootIPs : " + rootIPs);
            Debug.print("nfs annoyvisit : " + this.anonoyVisit);
            Debug.print("nfs rw : " + this.anonoyRW);
            if(rwIPs.contains(":")) {
                rwIPs=rwIPs.substring(0, rwIPs.lastIndexOf(":"));
            }
            if(roIPs.contains(":")) {
                roIPs=roIPs.substring(0, roIPs.lastIndexOf(":"));
            }
            if(rootIPs.contains(":")) {
                rootIPs=rootIPs.substring(0, rootIPs.lastIndexOf(":"));
            }
            NFSParametersForSharePath param = new NFSParametersForSharePath(this.path, rwIPs, roIPs, anonoyVisit, anonoyRW, rootIPs);
            NASInterface nas = InterfaceFactory.getNASInterfaceInstance();
            boolean flag = nas.configNFSForSharePath(param);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
                Debug.print("config nfs" + flag);
                return null;
            }
        }

        return "nas_path?faces-redirect=true";
    }

    public String turnOffNFS() {
        boolean flag = InterfaceFactory.getNASInterfaceInstance().turnOffNFSForSharePath(this.path);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
            Debug.print("turn off nfs" + flag);
            return null;
        }
        return "nas_path?faces-redirect=true";
    }
}
