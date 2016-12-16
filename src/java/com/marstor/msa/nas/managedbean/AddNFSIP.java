/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.nas.model.Constant;
import com.marstor.msa.nas.model.MySession;
import com.marstor.msa.nas.model.NFS;
import com.marstor.msa.nas.model.NFSDataTable;
import com.marstor.msa.nas.model.ShareListBean;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class AddNFSIP  implements Serializable{

    private String IP;
    private String rwIP;
    private String roIP;
    private String rootIP;
    private boolean  errorDisplay;

    public AddNFSIP() {
    }

    public boolean isErrorDisplay() {
        return errorDisplay;
    }

    public void setErrorDisplay(boolean errorDisplay) {
        this.errorDisplay = errorDisplay;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void addRWIP() {
        this.errorDisplay = true;
        if(!this.validateIP(this.rwIP)) {
             RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongIP);
             return ;
        }
        //保存
        ShareListBean  share = MySession.getShareFromSession();
        NFS nfs = share.getEditPath().getNfs();
        nfs.addToRW(this.rwIP);
//        NFSDataTable nfsData = MySession.getNFSData();
//        
//        NFS selectNFS = nfsData.getConfigNFS();
//        selectNFS.addToRW(this.rwIP);
        RequestContext.getCurrentInstance().addCallbackParam("result", "success");
    }
    public void addROIP() {
        if(!this.validateIP(this.roIP)) {
             RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongIP);
             return ;
        }
        //保存
        ShareListBean  share = MySession.getShareFromSession();
        NFS nfs = share.getEditPath().getNfs();
        nfs.addToRO(this.roIP);
//        NFSDataTable nfsData = MySession.getNFSData();
//        NFS selectNFS = nfsData.getConfigNFS();
//        selectNFS.addToRO(this.roIP);
        RequestContext.getCurrentInstance().addCallbackParam("result", "success");
    }
    public void addRootIP() {
        if(!this.validateIP(this.rootIP)) {
             RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongIP);
             return ;
        }
        //保存
         ShareListBean  share = MySession.getShareFromSession();
        NFS nfs = share.getEditPath().getNfs();
        nfs.addToRoot(this.rootIP);
//        NFSDataTable nfsData = MySession.getNFSData();
//        NFS selectNFS = nfsData.getConfigNFS();
//        selectNFS.addToRoot(this.rootIP);
        RequestContext.getCurrentInstance().addCallbackParam("result", "success");
    }

    public boolean  validateIP(String ip) {
        //this.IP = removeSpace(IP);
        //String ip = this.IP;
        //String success = "success";
        int n = counter(ip, '/');
        //String errorinfo = Constant.wrongIP;
        if (n > 1) {
            //Debug.print("IP地址格式错误");
           // RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
            return false;
        }
        if (n == 1) {
            String IP1 = ip.substring(0, ip.lastIndexOf("/"));
            String IP2 = ip.substring(ip.indexOf("/") + 1, ip.length() - 1);
            if (IP2.length() > 2 || !IP2.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                //Debug.print("不是一个IP");
               // RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
                return false;
            }

            int number = Integer.parseInt(IP2);
            if (number < 1 || number > 32) {
               // RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
                return  false;
            }
            if (!isIp(IP1)) {
               // RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
                return false;
            }
//            RequestContext.getCurrentInstance().addCallbackParam("result", success);
//            return;
        }
        if (n < 1) {
            if (!isIp(ip)) {
                //RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
                return false;
            }
//            else {
//                RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
//                return;
//            }
            // Debug.print(isIp(s) ? "是一个IP" : "不是一个IP");
        }
        return  true;
        

    }

    public int counter(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public boolean isIp(String IP) {//判断是否是一个IP
        boolean b = false;
        //IP = removeSpace(IP);
//        int n = counter(IP, '/');
//        if (n>1) {
//            return  false;
//        }
//        
//        if(n == 1) {
//            
//        }
        // String IP1= IP.substring(0, IP.lastIndexOf("/"));
        //Debug.print(IP1);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255) {
                if (Integer.parseInt(s[1]) < 255) {
                    if (Integer.parseInt(s[2]) < 255) {
                        if (Integer.parseInt(s[3]) < 255) {
                            b = true;
                        }
                    }
                }
            }
        }
        return b;
    }

    public String removeSpace(String IP) {//去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    public String getRoIP() {
        return roIP;
    }

    public void setRoIP(String roIP) {
        this.roIP = roIP;
    }

    public String getRootIP() {
        return rootIP;
    }

    public void setRootIP(String rootIP) {
        this.rootIP = rootIP;
    }

    public String getRwIP() {
        return rwIP;
    }

    public void setRwIP(String rwIP) {
        this.rwIP = rwIP;
    }
    public void addRWListen() {
        this.errorDisplay = false;
    }
    
}
