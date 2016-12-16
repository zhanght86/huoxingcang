package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.AclItem;
import com.marstor.msa.nas.model.ShareListBean;
import com.marstor.msa.nas.bean.User;
import com.marstor.msa.nas.bean.UserGroup;
import com.marstor.msa.nas.model.UserGroupData;
import com.marstor.msa.nas.model.Users;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "addauthority")
@ViewScoped
public class AddAuthoritiesBean  implements Serializable{

    private String selectedType;
    public boolean read;
    public boolean write;
    public boolean readFile;
    public boolean listContent;
    public boolean readBasicAttribute;
    public boolean readExpandAttribute;
    public boolean readACL;
    public boolean writeFile;
    public boolean writeFileOwner;
    public boolean writeFileBasicAttribute;
    public boolean writeFileExpandAttribute;
    public boolean writeACl;
    public boolean create;
    public boolean createSubDirectory;
    public boolean createFile;
    public boolean execute;
    public boolean executeFile;
    public boolean delete;
    public boolean deleteFile;
    public boolean deleteChild;
    public boolean fileInherit;
    public boolean dirInherit;
    public boolean inherit;
    public boolean readRendered;
    public boolean writeRendered;
    public boolean createRendered;
    public boolean deleteRendered;
    public boolean executeRendered;
    public boolean inheritRendered;
    private String style;
    private String name;
    private String result;
    private ArrayList<String> names = new ArrayList();
    private ArrayList<String> selectedNames = new ArrayList();
    private ArrayList<String> groupNames = new ArrayList();
    private ArrayList<String> userNames = new ArrayList();
    private String selectedName;
    private ArrayList<String> authoritis = new ArrayList();
    private String path;
    private String type;
    private boolean readFoldRendered;
    private boolean writeFoldRendered;
    private boolean createFoldRendered;
    private boolean deleteFoldRendered;
    private boolean executeFoldRendered;
    private boolean inheritFoldRendered;

    public AddAuthoritiesBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.path = request.getParameter("path");
        NASInterface nas = InterfaceFactory.getNASInterfaceInstance();

        //获取已配置的用户权限
        ArrayList<String> existUsers = new ArrayList<String>();
        String userList = request.getParameter("users");
        if (userList != null) {
            String userArray[] = userList.trim().split(",");
            if (userArray != null) {
                for (int i = 0; i < userArray.length; i++) {
                    existUsers.add(userArray[i]);
                }
            }
        }
        //获取已配置的用户组权限
        ArrayList<String> existGroups = new ArrayList<String>();
        String groupList = request.getParameter("groups");
        if (groupList != null) {
            String groupArray[] = groupList.trim().split(",");
            if (groupArray != null) {
                for (int i = 0; i < groupArray.length; i++) {
                    existGroups.add(groupArray[i]);
                }
            }
        }

        ArrayList<UserGroup> groups = nas.getAllSystemUserGroups();
        for (UserGroup group : groups) {
            if (!existGroups.contains(group.getName())) {
                this.groupNames.add(group.getName());
            }
        }
        ArrayList<User> users = nas.getAllSystemUsersInfo();
        for (User user : users) {
            if (!existUsers.contains(user.getName())) {
                this.userNames.add(user.getName());
            }
        }
        this.selectedNames = this.userNames;
        if (this.selectedNames.size() == 0) {
            this.selectedNames.add("");
        }
        this.type = "user";
        this.read = true;
        this.readACL = true;
        this.readBasicAttribute = true;
        this.readExpandAttribute = true;
        this.readFile = true;
        this.listContent = true;
        this.execute = true;
        this.executeFile = true;
    }

    public boolean isWriteFoldRendered() {
        return this.writeFoldRendered;
    }

    public void setWriteFoldRendered(boolean writeFoldRendered) {
        this.writeFoldRendered = writeFoldRendered;
    }

    public boolean isCreateFoldRendered() {
        return this.createFoldRendered;
    }

    public void setCreateFoldRendered(boolean createFoldRendered) {
        this.createFoldRendered = createFoldRendered;
    }

    public boolean isDeleteFoldRendered() {
        return this.deleteFoldRendered;
    }

    public void setDeleteFoldRendered(boolean deleteFoldRendered) {
        this.deleteFoldRendered = deleteFoldRendered;
    }

    public boolean isExecuteFoldRendered() {
        return this.executeFoldRendered;
    }

    public void setExecuteFoldRendered(boolean executeFoldRendered) {
        this.executeFoldRendered = executeFoldRendered;
    }

    public boolean isInheritFoldRendered() {
        return this.inheritFoldRendered;
    }

    public void setInheritFoldRendered(boolean inheritFoldRendered) {
        this.inheritFoldRendered = inheritFoldRendered;
    }

    public boolean isReadFoldRendered() {
        return this.readFoldRendered;
    }

    public void setReadFoldRendered(boolean readFoldRendered) {
        this.readFoldRendered = readFoldRendered;
    }

    public ArrayList<String> getAuthoritis() {
        return this.authoritis;
    }

    public void setAuthoritis(ArrayList<String> authoritis) {
        this.authoritis = authoritis;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSelectedName() {
        return this.selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public boolean isReadRendered() {
        return this.readRendered;
    }

    public void setReadRendered(boolean readRendered) {
        this.readRendered = readRendered;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<String> getNames() {
        return this.names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public boolean isCreateFile() {
        return this.createFile;
    }

    public void setCreateFile(boolean createFile) {
        this.createFile = createFile;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isExecuteFile() {
        return this.executeFile;
    }

    public void setExecuteFile(boolean executeFile) {
        this.executeFile = executeFile;
    }

    public boolean isInherit() {
        return this.inherit;
    }

    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }

    public boolean isDeleteFile() {
        return this.deleteFile;
    }

    public void setDeleteFile(boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

    public boolean isCreate() {
        return this.create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isCreateSubDirectory() {
        return this.createSubDirectory;
    }

    public void setCreateSubDirectory(boolean createSubDirectory) {
        this.createSubDirectory = createSubDirectory;
    }

    public boolean isDelete() {
        return this.delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isDeleteChild() {
        return this.deleteChild;
    }

    public void setDeleteChild(boolean deleteChild) {
        this.deleteChild = deleteChild;
    }

    public boolean isDirInherit() {
        return this.dirInherit;
    }

    public void setDirInherit(boolean dirInherit) {
        this.dirInherit = dirInherit;
    }

    public boolean isExecute() {
        return this.execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public boolean isFileInherit() {
        return this.fileInherit;
    }

    public void setFileInherit(boolean fileInherit) {
        this.fileInherit = fileInherit;
    }

    public boolean isListContent() {
        return this.listContent;
    }

    public void setListContent(boolean listContent) {
        this.listContent = listContent;
    }

    public boolean isRead() {
        return this.read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isReadACL() {
        return this.readACL;
    }

    public void setReadACL(boolean readACL) {
        this.readACL = readACL;
    }

    public boolean isReadBasicAttribute() {
        return this.readBasicAttribute;
    }

    public void setReadBasicAttribute(boolean readBasicAttribute) {
        this.readBasicAttribute = readBasicAttribute;
    }

    public boolean isReadExpandAttribute() {
        return this.readExpandAttribute;
    }

    public void setReadExpandAttribute(boolean readExpandAttribute) {
        this.readExpandAttribute = readExpandAttribute;
    }

    public boolean isReadFile() {
        return this.readFile;
    }

    public void setReadFile(boolean readFile) {
        this.readFile = readFile;
    }

    public boolean isWrite() {
        return this.write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isWriteACl() {
        return this.writeACl;
    }

    public void setWriteACl(boolean writeACl) {
        this.writeACl = writeACl;
    }

    public boolean isWriteFile() {
        return this.writeFile;
    }

    public void setWriteFile(boolean writeFile) {
        this.writeFile = writeFile;
    }

    public boolean isWriteFileBasicAttribute() {
        return this.writeFileBasicAttribute;
    }

    public void setWriteFileBasicAttribute(boolean writeFileBasicAttribute) {
        this.writeFileBasicAttribute = writeFileBasicAttribute;
    }

    public boolean isWriteFileExpandAttribute() {
        return this.writeFileExpandAttribute;
    }

    public void setWriteFileExpandAttribute(boolean writeFileExpandAttribute) {
        this.writeFileExpandAttribute = writeFileExpandAttribute;
    }

    public boolean isWriteFileOwner() {
        return this.writeFileOwner;
    }

    public void setWriteFileOwner(boolean writeFileOwner) {
        this.writeFileOwner = writeFileOwner;
    }

    public boolean isWriteRendered() {
        return this.writeRendered;
    }

    public void setWriteRendered(boolean writeRendered) {
        this.writeRendered = writeRendered;
    }

    public boolean isCreateRendered() {
        return this.createRendered;
    }

    public void setCreateRendered(boolean createRendered) {
        this.createRendered = createRendered;
    }

    public boolean isDeleteRendered() {
        return this.deleteRendered;
    }

    public void setDeleteRendered(boolean deleteRendered) {
        this.deleteRendered = deleteRendered;
    }

    public boolean isExecuteRendered() {
        return this.executeRendered;
    }

    public void setExecuteRendered(boolean executeRendered) {
        this.executeRendered = executeRendered;
    }

    public boolean isInheritRendered() {
        return this.inheritRendered;
    }

    public void setInheritRendered(boolean inheritRendered) {
        this.inheritRendered = inheritRendered;
    }

    public String getSelectedType() {
        return this.selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public ArrayList<String> getSelectedNames() {
        return this.selectedNames;
    }

    public void setSelectedNames(ArrayList<String> selectedNames) {
        this.selectedNames = selectedNames;
    }

    public ArrayList<String> getGroupNames() {
        return this.groupNames;
    }

    public void setGroupNames(ArrayList<String> groupNames) {
        this.groupNames = groupNames;
    }

    public ArrayList<String> getUserNames() {
        return this.userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    public void changeRead() {
        if (this.read) {
            this.readACL = true;
            this.readBasicAttribute = true;
            this.readExpandAttribute = true;
            this.readFile = true;
            this.listContent = true;
        }
        if (!(this.read)) {
            this.readACL = false;
            this.readBasicAttribute = false;
            this.readExpandAttribute = false;
            this.readFile = false;
            this.listContent = false;
        }
    }

    public void changeReadAuthor() {
        if ((this.readACL) || (this.readBasicAttribute) || (this.readExpandAttribute) || (this.readFile) || (this.listContent)) {
            this.read = true;
        } else {
            this.read = false;
        }
    }

    public void changeWrite() {
        if (this.write) {
            this.writeACl = true;
            this.writeFile = true;
            this.writeFileBasicAttribute = true;
            this.writeFileExpandAttribute = true;
            this.writeFileOwner = true;
        }
        if (!(this.write)) {
            this.writeACl = false;
            this.writeFile = false;
            this.writeFileBasicAttribute = false;
            this.writeFileExpandAttribute = false;
            this.writeFileOwner = false;
        }
    }

    public void changeWriteAuthor() {
        if ((this.writeACl) || (this.writeFile) || (this.writeFileBasicAttribute) || (this.writeFileExpandAttribute) || (this.writeFileOwner)) {
            this.write = true;
        } else {
            this.write = false;
        }
    }

    public void changeCreate() {
        if (this.create) {
            this.createFile = true;
            this.createSubDirectory = true;
        }
        if (!(this.create)) {
            this.createFile = false;
            this.createSubDirectory = false;
        }
    }

    public void changeCreateAuthor() {
        if ((this.createFile) || (this.createSubDirectory)) {
            this.create = true;
        } else {
            this.create = false;
        }
    }

    public void changeDelete() {
        if (this.delete) {
            this.deleteChild = true;
            this.deleteFile = true;
        }
        if (!(this.delete)) {
            this.deleteChild = false;
            this.deleteFile = false;
        }
    }

    public void changeDeleteAuthor() {
        if ((this.deleteChild) || (this.deleteFile)) {
            this.delete = true;
        } else {
            this.delete = false;
        }
    }

    public void changeExecute() {
        if (this.execute) {
            this.executeFile = true;
        }

        if (!(this.execute)) {
            this.executeFile = false;
        }
    }

    public void changeExecuteAuthor() {
        if (this.executeFile) {
            this.execute = true;
        } else {
            this.execute = false;
        }
    }

    public void changeInherit() {
        if (this.inherit) {
            this.fileInherit = true;
            this.dirInherit = true;
        }
        if (!(this.inherit)) {
            this.fileInherit = false;
            this.dirInherit = false;
        }
    }

    public void changeInheritAuthor() {
        if ((this.fileInherit) || (this.dirInherit)) {
            this.inherit = true;
        } else {
            this.inherit = false;
        }
    }

    public boolean isConfigAuthors() {
        return ((this.read) || (this.write) || (this.create) || (this.delete) || (this.execute) || (this.inherit));
    }

    public void clickReadMore() {
        if (!(this.readRendered)) {
            this.readRendered = true;
        } else {
            this.readRendered = false;
        }

        if (this.readFoldRendered) {
            this.readFoldRendered = false;
        } else {
            this.readFoldRendered = true;
        }
    }

    public void clickWriteMore() {
        if (!(this.writeRendered)) {
            this.writeRendered = true;
        } else {
            this.writeRendered = false;
        }

        if (this.writeFoldRendered) {
            this.writeFoldRendered = false;
        } else {
            this.writeFoldRendered = true;
        }
    }

    public void clickCreateMore() {
        if (!(this.createRendered)) {
            this.createRendered = true;
        } else {
            this.createRendered = false;
        }

        if (this.createFoldRendered) {
            this.createFoldRendered = false;
        } else {
            this.createFoldRendered = true;
        }
    }

    public void clickDeleteMore() {
        if (!(this.deleteRendered)) {
            this.deleteRendered = true;
        } else {
            this.deleteRendered = false;
        }

        if (this.deleteFoldRendered) {
            this.deleteFoldRendered = false;
        } else {
            this.deleteFoldRendered = true;
        }
    }

    public void clickExecuteMore() {
        if (!(this.executeRendered)) {
            this.executeRendered = true;
        } else {
            this.executeRendered = false;
        }

        if (this.executeFoldRendered) {
            this.executeFoldRendered = false;
        } else {
            this.executeFoldRendered = true;
        }
    }

    public void clickInheritMore() {
        if (!(this.inheritRendered)) {
            this.inheritRendered = true;
        } else {
            this.inheritRendered = false;
        }

        if (this.inheritFoldRendered) {
            this.inheritFoldRendered = false;
        } else {
            this.inheritFoldRendered = true;
        }
    }

    public ShareListBean getShareFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
        return share;
    }

    public Users getUserFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users users = (Users) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{users}", Users.class).getValue(context.getELContext());
        return users;
    }

    public UserGroupData getGroupsFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserGroupData groups = (UserGroupData) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{groups}", UserGroupData.class).getValue(context.getELContext());
        return groups;
    }

    public void changeValue() {
        MSAResource res = new MSAResource();
        if (this.selectedType.equals(res.get("group"))) {
            this.selectedNames = this.groupNames;
            if (this.selectedNames.size() == 0) {
                this.selectedNames.add("");
            }
            this.type = "group";
        }
        if (this.selectedType.equals(res.get("user"))) {
            this.selectedNames = this.userNames;
            if (this.selectedNames.size() == 0) {
                this.selectedNames.add("");
            }
            this.type = "user";
        }
    }

    public String save() {
        MSAResource res;
        if (this.selectedName == null || this.selectedName.equals("")) {
            res = new MSAResource();
            if (this.selectedType.equals(res.get("user"))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userNameEmpty"), res.get("userNameEmpty")));
                return null;
            }
             if (this.selectedType.equals(res.get("group"))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("groupNameEmpty"), res.get("groupNameEmpty")));
                return null;
            }
        }
        ArrayList items = InterfaceFactory.getNASInterfaceInstance().getSpecifiedSharePathAuthority(this.path);
        if (items == null) {
            Debug.print("get all authority failed!");
            res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getAuthorsFailed"), res.get("getAuthorsFailed")));
            return null;
        }
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            AclItem item = (AclItem) iterator.next();
            if ((item.getStyle().equals(this.type)) && (item.getName().equals(this.selectedName))) {
                Debug.print("this authority exist!");
                res = new MSAResource();
                if (this.type.equals("user")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existUserAuthor"), res.get("existUserAuthor")));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existGroupAuthor"), res.get("existGroupAuthor")));
                }

                return null;
            }
        }
        if (this.readFile) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[2]);
        }

        if (this.listContent) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[3]);
        }

        if (this.readBasicAttribute) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[4]);
        }

        if (this.readExpandAttribute) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[5]);
        }

        if (this.readACL) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[6]);
        }

        if (this.writeFile) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[16]);
        }

        if (this.writeFileOwner) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[19]);
        }

        if (this.writeFileBasicAttribute) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[14]);
        }

        if (this.writeFileExpandAttribute) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[18]);
        }

        if (this.writeACl) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[15]);
        }

        if (this.createFile) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[7]);
        }

        if (this.createSubDirectory) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[8]);
        }

        if (this.executeFile) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[1]);
        }

        if (this.deleteFile) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[20]);
        }

        if (this.deleteChild) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[21]);
        }

        if (this.fileInherit) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[11]);
        }

        if (this.dirInherit) {
            this.authoritis.add(com.marstor.msa.nas.bean.AccessAuthority.str[12]);
        }

        AclItem item = new AclItem(this.path, 0, this.type, "allow", this.selectedName, this.authoritis);
        boolean flag = InterfaceFactory.getNASInterfaceInstance().addAuthority(item);
        if (!(flag)) {
            res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addAuthorFailed"), res.get("addAuthorFailed")));
            return null;
        }

        String param = "path=" + this.path;
        return "nas_authority?faces-redirect=true&amp;" + param;
    }

    public String back() {
        String param = "path=" + this.path;
        return "nas_authority?faces-redirect=true&amp;" + param;
    }

    public void displayLocation() {
        FacesMessage msg = new FacesMessage("Selected", "Style:" + this.style + ", Name: " + this.name);

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}