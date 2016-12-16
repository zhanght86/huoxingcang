/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.bean.User;
import java.util.ArrayList;
import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;
import com.marstor.msa.nas.xml.MyParser;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SystemGroup implements Serializable{

    private String name = "";
    private int id = 0;
    private ArrayList<User> users = new ArrayList<User>();
    private boolean existOrNotInSystem = false;
    private String info = ""; // /etc/group文件中每个用户组的一条信息
    private ArrayList<String> userNames = new ArrayList<String>();
    private  String  userStrs="";
    public SystemGroup() {
    }

    public SystemGroup(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public SystemGroup(int id,String name,String info,ArrayList<User> users) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.users = users;
        for(User u : this.users) {
            this.userNames.add(u.getName());
        }
        StringBuffer names = new StringBuffer();
        for (int i = 0; i < userNames.size(); i++) {
            names.append(userNames.get(i));
            if ((i + 1) < userNames.size()) {
                names.append(",");
            }
        }
        this.userStrs = names.toString();
    }

    public String getUserStrs() {
        return userStrs;
    }

    public void setUserStrs(String userStrs) {
        this.userStrs = userStrs;
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }



    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> Users) {
        this.users = Users;
    }

    public boolean isExistOrNotInSystem() {
        return existOrNotInSystem;
    }

    public void setExistOrNotInSystem(boolean existOrNotInSystem) {
        this.existOrNotInSystem = existOrNotInSystem;
    }

    public String[] toParam(String erroinfo) {

        String args[] = {String.valueOf(this.id), this.name, erroinfo};
        return args;

    }

    public String[] toParam() {

        String args[] = {String.valueOf(this.id), this.name};
        return args;

    }

    public XMLConstructor toXMLConstrutor() {
        XMLConstructor bean = new XMLConstructor("Group");
        bean.addNode("Name", name);
        bean.addNode("ID", this.id);
        bean.addNode("Info", this.info);

        return bean;
    }

//    public ArrayList<UserGroup> generateByXMLConstructor(MyParser parser) {
//        ArrayList<UserGroup> groups = new ArrayList<UserGroup>();
//        int count = parser.getParameterCount("Group");
//        for (int i = 0; i < count; i++) {
//            SystemGroup u = new SystemGroup();
//            u.name = parser.getParameter("Group/Name", i);
//            u.id = parser.getIntParameter("Group/ID", i);
//            u.info = parser.getParameter("Group/Info", i);
//
//            groups.add(u);
//        }
//        return groups;
//    }
}
