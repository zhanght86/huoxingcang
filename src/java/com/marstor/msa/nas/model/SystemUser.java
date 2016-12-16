/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.bean.Volume;
import com.marstor.xml.XMLConstructor;
import com.marstor.msa.nas.xml.MyParser;
import java.io.Serializable;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class SystemUser  implements Serializable {

    private int id = 0;
    private String name = "";
    private String password = "";
   // private String sharepath = "";
    private ArrayList<String>  groupNames = new ArrayList<String>();
    protected  boolean  existOrNotInSystem = false;
    private  String info =""; // /etc/passwd中每个用户的一条信息
    //private Volume volume = new Volume();
    private  String  smbPasswd=""; // /var/smb/smbpasswd中每个用户的一条信息（主要是密码）
    private  String  groupStrs="";
    public SystemUser() {
    }

    public SystemUser(int id,String name,String password,String  smbPasswd,String info,ArrayList<String>  groups) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.smbPasswd = smbPasswd;
        this.info = info;
        this.groupNames = groups;
        StringBuffer names = new StringBuffer();
        for (int i = 0; i < groupNames.size(); i++) {
            names.append(groupNames.get(i));
            if ((i + 1) < groupNames.size()) {
                names.append(",");
            }
        }
        this.groupStrs = names.toString();
    }

    public String getGroupStrs() {
        return groupStrs;
    }

    public void setGroupStrs(String groupStrs) {
        this.groupStrs = groupStrs;
    }

    public ArrayList<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(ArrayList<String> groupNames) {
        this.groupNames = groupNames;
    }

   

    public void setSmbPasswd(String smbPasswd) {
        this.smbPasswd = smbPasswd;
    }

    public String getSmbPasswd() {
        return smbPasswd;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
    
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

//    public String getSharepath() {
//
//        return sharepath;
//    }
//
//    public void setSharepath(String sharepath) {
//        this.sharepath = sharepath;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public void setVolume(Volume volume) {
//        this.volume = volume;
//    }
//
//    public Volume getVolume() {
//        return volume;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExistOrNotInSystem() {
        return existOrNotInSystem;
    }

    public void setExistOrNotInSystem(boolean existOrNotInSystem) {
        this.existOrNotInSystem = existOrNotInSystem;
    }
    public XMLConstructor  toXMLConstrutor() {
        XMLConstructor bean = new XMLConstructor("User");
        bean.addNode("Name", this.name);
        bean.addNode("ID",this.id);
        bean.addNode("Passwd", this.password);
        bean.addNode("Info", this.info);
        bean.addNode("SMBPasswd", this.smbPasswd);
        
        return  bean;
    }
//    public ArrayList<User>  generateByXMLConstructor(MyParser parser) {
//        ArrayList<User>  users = new ArrayList<User>();
//        int  count = parser.getParameterCount("SystemUser");
//        for(int i=0;i<count;i++) {
//            SystemUser  u = new SystemUser();
//            u.name = parser.getParameter("SystemUser/Name", i);
//            u.id = parser.getIntParameter("SystemUser/ID", i);
//            u.info = parser.getParameter("SystemUser/Info", i);
//            u.password = parser.getParameter("SystemUser/Passwd", i);
//            u.smbPasswd = parser.getParameter("SystemUser/SMBPasswd", i);
//            
//            users.add(u);
//        }
//        return  users;
//    }
    
}
