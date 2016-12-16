package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.ACL;
import com.marstor.msa.nas.bean.AclItem;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class ConfigAuthoritiesBean implements Serializable{

    private String path = "";
    private ArrayList<ACL> acls = new ArrayList<ACL>();
    private ArrayList<ACL> sysAcls = new ArrayList<ACL>();
    //private ArrayList<ACL> notSysAcls = new ArrayList<ACL>();
    private ArrayList<ACL> userAcls = new ArrayList<ACL>();
    private ArrayList<ACL> groupAcls = new ArrayList<ACL>();
    private ArrayList<String> users = new ArrayList<String>();
    private ArrayList<String> groups = new ArrayList<String>();
    private ACL tempAcl;
    private boolean isApplyToSubDirectory;

    public ConfigAuthoritiesBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.path = request.getParameter("path");
        if (this.path == null) {
            return;
        }
        ArrayList<AclItem> items = InterfaceFactory.getNASInterfaceInstance().getSpecifiedSharePathAuthority(this.path);
        if (items == null) {
            return;
        }

        for (Iterator it = items.iterator(); it.hasNext();) {
            AclItem item = (AclItem) it.next();
            ACL acl = new ACL(item.getIndex(), item.getStyle(), item.getName(), item.getAuthorities());
            if (item.getStyle().equals("owner@") || item.getStyle().equals("group@") || item.getStyle().equals("everyone@")) {
                this.sysAcls.add(acl);
            } else {
                // this.notSysAcls.add(acl);
                if (item.getStyle().equals("user")) {
                    this.users.add(item.getName());
                    this.userAcls.add(acl);
                } else if (item.getStyle().equals("group")) {
                    this.groups.add(item.getName());
                    this.groupAcls.add(acl);
                }
            }

        }
        this.acls.addAll(this.sysAcls);
        //this.acls.addAll(this.notSysAcls);
        this.acls.addAll(this.userAcls);
        this.acls.addAll(this.groupAcls);
    }

    public void init() {
        ArrayList<ACL> aclList = new ArrayList<ACL>();
        ArrayList<ACL> sysList = new ArrayList<ACL>();
        ArrayList<ACL> userAclList = new ArrayList<ACL>();
        ArrayList<ACL> groupAclList = new ArrayList<ACL>();
        ArrayList<String> userList = new ArrayList<String>();
        ArrayList<String> groupList = new ArrayList<String>();
        ArrayList<AclItem> items = InterfaceFactory.getNASInterfaceInstance().getSpecifiedSharePathAuthority(this.path);
        if (items == null) {
            return;
        }

        for (Iterator it = items.iterator(); it.hasNext();) {
            AclItem item = (AclItem) it.next();
            ACL acl = new ACL(item.getIndex(), item.getStyle(), item.getName(), item.getAuthorities());
            if (item.getStyle().equals("owner@") || item.getStyle().equals("group@") || item.getStyle().equals("everyone@")) {
                sysList.add(acl);
            } else {
                if (item.getStyle().equals("user")) {
                    userList.add(item.getName());
                    userAclList.add(acl);
                } else if (item.getStyle().equals("group")) {
                    groupList.add(item.getName());
                    groupAclList.add(acl);
                }
            }

        }
        aclList.addAll(sysList);
        aclList.addAll(userAclList);
        aclList.addAll(groupAclList);
        this.users = userList;
        this.groups = groupList;
        this.acls = aclList;

    }

    public boolean isIsApplyToSubDirectory() {
        return isApplyToSubDirectory;
    }

    public void setIsApplyToSubDirectory(boolean isApplyToSubDirectory) {
        this.isApplyToSubDirectory = isApplyToSubDirectory;
    }

    public ACL getTempAcl() {
        return this.tempAcl;
    }

    public void setTempAcl(ACL tempAcl) {
        this.tempAcl = tempAcl;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<ACL> getAcls() {
        return this.acls;
    }

    public void setAcls(ArrayList<ACL> acls) {
        this.acls = acls;
    }

    public String doBeforeAddAuthor() {
        String userNames = "", groupNames = "";
        for (int i = 0; i < this.users.size(); i++) {
            if (i + 1 < users.size()) {
                userNames = userNames + users.get(i) + ",";
            } else {
                userNames = userNames + users.get(i);
            }
        }
        for (int i = 0; i < this.groups.size(); i++) {
            if (i + 1 < this.groups.size()) {
                groupNames = groupNames + this.groups.get(i) + ",";
            } else {
                groupNames = groupNames + this.groups.get(i);
            }
        }
        String param = "path=" + this.path + "&" + "users=" + userNames + "&" + "groups=" + groupNames;
        return "nas_add_author?faces-redirect=true&amp;" + param;
    }

    public String doBeforeEditAuthor(ACL acl) {
        ArrayList authors = acl.getAuthors();
        String authorStr = "";
        if (authors != null) {
            for (int i = 0; i < authors.size(); ++i) {
                if (i + 1 < authors.size()) {
                    authorStr = authorStr + ((String) authors.get(i)) + ",";
                } else {
                    authorStr = authorStr + ((String) authors.get(i));
                }

            }

        }

        String param = "path=" + this.path + "&" + "index=" + acl.getIndex() + "&" + "style=" + acl.getType() + "&" + "objectStyle=" + acl.getObjectStyle() + "&" + "name=" + acl.getObjectName() + "&" + "authors=" + authorStr;
        Debug.print("edit param : " + param);
        return "nas_edit_author?faces-redirect=true&amp;" + param;
    }

    public void deleteACL() {
        boolean flag = InterfaceFactory.getNASInterfaceInstance().delAuthority(this.tempAcl.getIndex(), this.path);
        if (!(flag)) {
            Debug.print("del author" + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deleletFailed"), res.get("deleletFailed")));
            return;
        }
//        ArrayList items = InterfaceFactory.getNASInterfaceInstance().getSpecifiedSharePathAuthority(this.path);
//        if (items == null) {
//            return;
//        }
//
//        ArrayList allAcls = new ArrayList();
//        for (Iterator i$ = items.iterator(); i$.hasNext();) {
//            AclItem item = (AclItem) i$.next();
//            ACL acl = new ACL(item.getIndex(), item.getStyle(), item.getName(), item.getAuthorities());
//            allAcls.add(acl);
//        }
//        this.acls = allAcls;
        this.init();
    }

    public String save() {
        if (this.isApplyToSubDirectory) {
            boolean flag = InterfaceFactory.getNASInterfaceInstance().setChiledAuthoritySameAsSharePath(path);
            if(!flag) {
            Debug.print("apply to subdirectory: " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("applyToSubFailed"), res.get("applyToSubFailed")));
            return null;
            }
        }
        return "nas_path?faces-redirect=true";

    }
}