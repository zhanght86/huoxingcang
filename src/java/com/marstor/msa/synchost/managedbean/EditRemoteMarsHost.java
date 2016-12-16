/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.synchost.managedbean;

import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class EditRemoteMarsHost implements Serializable {

    private List<MarsHost> marsHosts;
    private MsaSYNCInterface syncObj;
    private String oldIP;
    private String ip;
    private String port;
    private String username="";
    private String pwd="";
    private String sshport;
    private String sshpwd;
    private String confirmSshPwd;
    private boolean defaultRoot = true;
    private String defaultRootPwd = "4rfVBNji9";
    private String sshEncryp;
    private String pwdEncryp;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();

    public EditRemoteMarsHost() {
        syncObj = InterfaceFactory.getSYNCInterfaceInstance();
        marsHosts = syncObj.getMarsHost();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        oldIP = ip;
        for (MarsHost m : marsHosts) {
            if (m.getIp().equals(ip)) {
                port = m.getPort();
                username = m.getUsername();
                pwdEncryp = m.getPassword();
                sshport = m.getSshport();
                sshEncryp = m.getSshPassword();
                pwd = new String(MyEncryp.Decode(pwdEncryp.toCharArray()));
                sshpwd = new String(MyEncryp.Decode(sshEncryp.toCharArray()));
                confirmSshPwd = sshpwd;
//                System.out.println("admin encryp = "+new String (MyEncryp.Encode64("admin".toCharArray())));
//                System.out.println("password encryp = "+new String (MyEncryp.Encode64("password".toCharArray())));
//                admin encryp = aYVtcYG=
//                password encryp = w6FAw5dxwoU=
                System.out.println("pwd=" + pwd);
                System.out.println("sshpwd=" + sshpwd);
                if (!sshpwd.equals(defaultRootPwd)) {
                    defaultRoot = false;
                }
            }
        }
    }

    public String modifyMarsHost() {
        if (!validator()) {
            return null;
        }
        if (sshport.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nullSSHPort"), global.get("error_mark")));
            return null;
        }
        if (!SyncHostUtil.checkNum(sshport, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("errorSSHPort"), global.get("error_mark")));
            return null;
        }
        if (!SyncHostUtil.checkPort(sshport)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("errorSSHPortRange"), global.get("error_mark")));
            return null;
        }
        if (!defaultRoot) {
            if (sshpwd.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nullPwd"), global.get("error_mark")));
                return null;
            }
            if (!sshpwd.equals(confirmSshPwd)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("errorConfirmPwd"), global.get("error_mark")));
                return null;
            }
            sshEncryp = new String(MyEncryp.Encode64(sshpwd.toCharArray()));
        } else {
            sshEncryp = new String(MyEncryp.Encode64(defaultRootPwd.toCharArray()));
        }
        pwdEncryp = new String(MyEncryp.Encode64(pwd.toCharArray()));
        MarsHost host = new MarsHost(ip, port, username, pwdEncryp, sshport, sshEncryp);
        if (syncObj.modifyMarsHost(oldIP,host)) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("success"), global.get("error_mark")));
            return "sync_list_mars_host?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("failed"), global.get("error_mark")));
            return null;
        }
    }

    public void test() {
        if (validator()) {
            pwdEncryp = new String(MyEncryp.Encode64(pwd.toCharArray()));
            Object o = syncObj.getTargetServerFileSystem(ip, port, "anonymoususer", pwdEncryp);
            if (o == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectFailed"), global.get("error_mark")));
                return;
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("connectSuccess"), global.get("error_mark")));
        }
    }

    private boolean validator() {
        if (ip == null || ip.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nullIP"), global.get("error_mark")));
            return false;
        }
//        if (!SyncHostUtil.checkIP(ip)) {
//            System.out.println(ip);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("errorIP"), global.get("error_mark")));
//            return false;
//        }
        if (port == null || port.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nullPort"), global.get("error_mark")));
            return false;
        }
        if (!SyncHostUtil.checkNum(port, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("errorPort"), global.get("error_mark")));
            return false;
        }
        if (!SyncHostUtil.checkPort(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("errorPortRange"), global.get("error_mark")));
            return false;
        }
//        if (username == null || username.equals("")) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nullUser"), global.get("error_mark")));
//            return false;
//        }
//        if (username.length() < 5 ||  !username.matches("^[a-zA-Z0-9]+$")) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("errorUser"), global.get("error_mark")));
//            return false;
//        }
//        if (pwd == null || pwd.equals("")) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nullPwd"), global.get("error_mark")));
//            return false;
//        }
        return true;
    }

    public List<MarsHost> getMarsHosts() {
        return marsHosts;
    }

    public void setMarsHosts(List<MarsHost> marsHosts) {
        this.marsHosts = marsHosts;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSshport() {
        return sshport;
    }

    public void setSshport(String sshport) {
        this.sshport = sshport;
    }

    public String getSshpwd() {
        return sshpwd;
    }

    public void setSshpwd(String sshpwd) {
        this.sshpwd = sshpwd;
    }

    public String getConfirmSshPwd() {
        return confirmSshPwd;
    }

    public void setConfirmSshPwd(String confirmSshPwd) {
        this.confirmSshPwd = confirmSshPwd;
    }

    public boolean isDefaultRoot() {
        return defaultRoot;
    }

    public void setDefaultRoot(boolean defaultRoot) {
        this.defaultRoot = defaultRoot;
    }
}
