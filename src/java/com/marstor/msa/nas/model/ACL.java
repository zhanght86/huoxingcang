package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.AccessAuthority;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.primefaces.context.RequestContext;

public class ACL implements Serializable {

    private int index;
    private String objectStyle;
    private String objectName;
    private String type = "";
    private String name = "";
    private ArrayList<Authority> generalAuthors = new ArrayList();
    private ArrayList<String> authors = new ArrayList();
    private HashMap<String, Boolean> authMap = new HashMap();
    private ALLAuthoritiesValue authorsForAdd = new ALLAuthoritiesValue();
    private ALLAuthoritiesValue authorsForEdit = new ALLAuthoritiesValue();
    private String delStr = "";
    private boolean delImageRendered = true;
    private boolean readRendered = false;
    private boolean writeRendered = false;
    private boolean createRendered = false;
    private boolean deleteRendered = false;
    private boolean executeRendered = false;
    private boolean inheritRendered = false;
    private MSAResource res = new MSAResource();

    public ACL(int index, String objectStyle, String objectName) {
        this.objectStyle = objectStyle;
        this.objectName = objectName;
        this.index = index;

        this.authMap.put("readFile", Boolean.FALSE);
        this.authMap.put("listContent", Boolean.FALSE);
        this.authMap.put("readBasicAttribute", Boolean.FALSE);
        this.authMap.put("readExpandAttribute", Boolean.FALSE);
        this.authMap.put("readACL", Boolean.FALSE);
        this.authMap.put("createFile", Boolean.FALSE);
        this.authMap.put("createSubDirectory", Boolean.FALSE);
        this.authMap.put("executeFile", Boolean.FALSE);
        this.authMap.put("writeFile", Boolean.FALSE);
        this.authMap.put("writeFileOwner", Boolean.FALSE);
        this.authMap.put("writeFileBasicAttribute", Boolean.FALSE);
        this.authMap.put("writeFileExpandAttribute", Boolean.FALSE);
        this.authMap.put("writeACl", Boolean.FALSE);
        this.authMap.put("deleteFile", Boolean.FALSE);
        this.authMap.put("deleteChild", Boolean.FALSE);
        this.authMap.put("fileInherit", Boolean.FALSE);
        this.authMap.put("dirInherit", Boolean.FALSE);

        updateGeneralAuthors();
    }

    public ACL(int index, String objectStyle, String objectName, ArrayList<String> authors) {
        this.index = index;
        MSAResource res = new MSAResource();
        if (objectStyle.equals("owner@")) {
            this.objectStyle = res.get("sys");
            this.objectName = res.get("owner");
            this.type = "owner@";
            this.delImageRendered = false;
            this.deleteRendered = false;
        } else if (objectStyle.equals("group@")) {
            this.objectStyle = res.get("sys");
            this.objectName = res.get("group");
            this.type = "group@";
            this.delImageRendered = false;
            this.deleteRendered = false;
        } else if (objectStyle.equals("everyone@")) {
            this.objectStyle = res.get("sys");
            this.objectName = res.get("everyone");
            this.type = "everyone@";
            this.delImageRendered = false;
            this.deleteRendered = false;
        } else if (objectStyle.equals("user")) {
            this.objectStyle = res.get("user");
            this.objectName = objectName;
            this.type = "user";
            this.delImageRendered = true;
            this.deleteRendered = true;
        } else if (objectStyle.equals("group")) {
            this.objectStyle = res.get("group");
            this.objectName = objectName;
            this.type = "group";
            this.delImageRendered = true;
            this.deleteRendered = true;
        }
        this.authors = authors;
        updateGeneralAuthors();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MSAResource getRes() {
        return this.res;
    }

    public void setRes(MSAResource res) {
        this.res = res;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean isReadRendered() {
        return this.readRendered;
    }

    public void setReadRendered(boolean readRendered) {
        this.readRendered = readRendered;
    }

    public boolean isWriteRendered() {
        return this.writeRendered;
    }

    public void setWriteRendered(boolean writeRendered) {
        this.writeRendered = writeRendered;
    }

    public boolean isDelImageRendered() {
        return this.delImageRendered;
    }

    public void setDelImageRendered(boolean delImageRendered) {
        this.delImageRendered = delImageRendered;
    }

    public String getDelStr() {
        return this.delStr;
    }

    public void setDelStr(String delStr) {
        this.delStr = delStr;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ALLAuthoritiesValue getAuthorsForAdd() {
        return this.authorsForAdd;
    }

    public void setAuthorsForAdd(ALLAuthoritiesValue authorsForAdd) {
        this.authorsForAdd = authorsForAdd;
    }

    public ALLAuthoritiesValue getAuthorsForEdit() {
        return this.authorsForEdit;
    }

    public void setAuthorsForEdit(ALLAuthoritiesValue authorsForEdit) {
        this.authorsForEdit = authorsForEdit;
    }

    public void updateAllEditAuthors() {
        this.authorsForEdit.setReadFile(getReadFileAuthor());
        this.authorsForEdit.setListContent(getlistContentAuthor());
        this.authorsForEdit.setReadBasicAttribute(getReadBasicAttributeAuthor());
        this.authorsForEdit.setReadExpandAttribute(getreadExpandAttribute());
        this.authorsForEdit.setReadACL(getReadACLAuthor());

        this.authorsForEdit.setWriteFile(getWriteFileAuthor());
        this.authorsForEdit.setWriteFileBasicAttribute(getWriteFileAuthor());
        this.authorsForEdit.setWriteFileOwner(getWriteFileAuthor());
        this.authorsForEdit.setWriteFileExpandAttribute(getWriteFileExpandAttribute());
        this.authorsForEdit.setWriteACl(getWriteAClAuthor());

        this.authorsForEdit.setCreateFile(getCreateFileAuthor());
        this.authorsForEdit.setCreateSubDirectory(getCreateSubDirectoryAuthor());
        this.authorsForEdit.setExecuteFile(getExcuteFileAuthor());
        this.authorsForEdit.setDeleteFile(getDeleteFileAuthor());
        this.authorsForEdit.setDeleteChild(getDeleteChildAuthor());
        this.authorsForEdit.setFileInherit(getFileInheritAuthor());
        this.authorsForEdit.setDirInherit(getDirInheritAuthor());

        if ((getReadACLAuthor()) && (getReadBasicAttributeAuthor()) && (getreadExpandAttribute()) && (getReadFileAuthor())) {
            this.authorsForEdit.setRead(true);
        }

        if ((getWriteAClAuthor()) && (getWriteFileAuthor()) && (getWriteFileBasicAttributeAuthor()) && (getWriteFileExpandAttribute()) && (getWriteFileOwnerAuthor())) {
            this.authorsForEdit.setWrite(true);
        }

        if (getExcuteFileAuthor()) {
            this.authorsForEdit.setExecute(true);
        }
        if ((getFileInheritAuthor()) && (getDirInheritAuthor())) {
            this.authorsForEdit.setInherit(true);
        }

        if ((getDeleteChildAuthor()) && (getDeleteFileAuthor())) {
            this.authorsForEdit.setDelete(true);
        }

        if ((getCreateFileAuthor()) && (getCreateSubDirectoryAuthor())) {
            this.authorsForEdit.setCreate(true);
        }
    }

    public void updateGeneralAuthors() {
        Authority auth;
//        Authority special;
//        if ((this.authors.contains(AccessAuthority.str[2])) && (this.authors.contains(AccessAuthority.str[3])) && (this.authors.contains(AccessAuthority.str[4])) && (this.authors.contains(AccessAuthority.str[6])) && (this.authors.contains(AccessAuthority.str[7])) && (this.authors.contains(AccessAuthority.str[8])) && (this.authors.contains(AccessAuthority.str[1])) && (this.authors.contains(AccessAuthority.str[16])) && (this.authors.contains(AccessAuthority.str[19])) && (this.authors.contains(AccessAuthority.str[14])) && (this.authors.contains(AccessAuthority.str[18])) && (this.authors.contains(AccessAuthority.str[15])) && (this.authors.contains(AccessAuthority.str[20])) && (this.authors.contains(AccessAuthority.str[21])) && (this.authors.contains(AccessAuthority.str[11])) && (this.authors.contains(AccessAuthority.str[12]))) {
//            auth = new Authority(this.res.get("positiveControl"), true);
//
//            this.generalAuthors.add(auth);
//            auth = new Authority(this.res.get("update"), Boolean.TRUE.booleanValue());
//            this.generalAuthors.add(auth);
//            special = new Authority(this.res.get("special"), Boolean.FALSE.booleanValue());
//        } else {
//            auth = new Authority(this.res.get("positiveControl"), false);
//            this.generalAuthors.add(auth);
//            auth = new Authority(this.res.get("update"), Boolean.FALSE.booleanValue());
//            this.generalAuthors.add(auth);
//            special = new Authority(this.res.get("special"), Boolean.TRUE.booleanValue());
//        }
//
//        if ((this.authors.contains(AccessAuthority.str[2])) && (this.authors.contains(AccessAuthority.str[3])) && (this.authors.contains(AccessAuthority.str[4])) && (this.authors.contains(AccessAuthority.str[5])) && (this.authors.contains(AccessAuthority.str[6])) && (this.authors.contains(AccessAuthority.str[1]))) {
//            auth = new Authority(this.res.get("readAndExecute"), true);
//
//            this.generalAuthors.add(auth);
//            auth = new Authority(this.res.get("listFolderAndContent"), Boolean.TRUE.booleanValue());
//            this.generalAuthors.add(auth);
//        } else {
//            auth = new Authority(this.res.get("readAndExecute"), false);
//            this.generalAuthors.add(auth);
//            auth = new Authority(this.res.get("listFolderAndContent"), Boolean.FALSE.booleanValue());
//            this.generalAuthors.add(auth);
//        }
//        if ((this.authors.contains(AccessAuthority.str[2])) && (this.authors.contains(AccessAuthority.str[3])) && (this.authors.contains(AccessAuthority.str[4])) && (this.authors.contains(AccessAuthority.str[5])) && (this.authors.contains(AccessAuthority.str[6]))) {
//            auth = new Authority(this.res.get("fetch"), true);
//            this.generalAuthors.add(auth);
//        } else {
//            auth = new Authority(this.res.get("fetch"), false);
//            this.generalAuthors.add(auth);
//        }
//        if ((this.authors.contains(AccessAuthority.str[2])) && (this.authors.contains(AccessAuthority.str[3])) && (this.authors.contains(AccessAuthority.str[4])) && (this.authors.contains(AccessAuthority.str[5])) && (this.authors.contains(AccessAuthority.str[6])) && (this.authors.contains(AccessAuthority.str[16])) && (this.authors.contains(AccessAuthority.str[19])) && (this.authors.contains(AccessAuthority.str[14])) && (this.authors.contains(AccessAuthority.str[18])) && (this.authors.contains(AccessAuthority.str[15])) && (this.authors.contains(AccessAuthority.str[7])) && (this.authors.contains(AccessAuthority.str[8]))) {
//            auth = new Authority(this.res.get("writeIn"), true);
//            this.generalAuthors.add(auth);
//        } else {
//            auth = new Authority(this.res.get("writeIn"), false);
//            this.generalAuthors.add(auth);
//        }
//        this.generalAuthors.add(special);

        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        //初始化map
        map.put("writeIn", false);
        map.put("special", false);
        map.put("fetch", false);
        map.put("listFolderAndContent", false);
        map.put("readAndExecute", false);
        map.put("update", true);
        map.put("positiveControl", false);
        //根据权限设置map
        if (this.authors.contains(AccessAuthority.str[7]) && this.authors.contains(AccessAuthority.str[8])
                //                 && this.authors.contains(AccessAuthority.str[9])
                && this.authors.contains(AccessAuthority.str[14])
                && this.authors.contains(AccessAuthority.str[16]) && this.authors.contains(AccessAuthority.str[18])) {
            // setCheckBox(5, true);
            map.put("writeIn", true);
        } else if (!this.authors.contains(AccessAuthority.str[7]) && !this.authors.contains(AccessAuthority.str[8])
                //                 && !this.authors.contains(AccessAuthority.str[9]) 
                && !this.authors.contains(AccessAuthority.str[14])
                && !this.authors.contains(AccessAuthority.str[16]) && !this.authors.contains(AccessAuthority.str[18])) {
            //setCheckBox(5, false);
            map.put("writeIn", false);
        } else {
            map.put("special", true);
            // setCheckBox(6, true);
        }

        if (this.authors.contains(AccessAuthority.str[2]) && this.authors.contains(AccessAuthority.str[3])
                && this.authors.contains(AccessAuthority.str[4]) && this.authors.contains(AccessAuthority.str[5])
                && this.authors.contains(AccessAuthority.str[6])) {
            // setCheckBox(4, true);
            map.put("fetch", true);
        } else if (!this.authors.contains(AccessAuthority.str[2]) && !this.authors.contains(AccessAuthority.str[3])
                && !this.authors.contains(AccessAuthority.str[4]) && !this.authors.contains(AccessAuthority.str[5])
                && !this.authors.contains(AccessAuthority.str[6])) {
            //setCheckBox(4, false);
            map.put("fetch", false);
        } else {
            // setCheckBox(6, true);
            map.put("special", true);
        }

        if (this.authors.contains(AccessAuthority.str[1]) && this.authors.contains(AccessAuthority.str[2]) && this.authors.contains(AccessAuthority.str[3])
                && this.authors.contains(AccessAuthority.str[4]) && this.authors.contains(AccessAuthority.str[5])
                && this.authors.contains(AccessAuthority.str[6])) {
            map.put("listFolderAndContent", true);
            map.put("readAndExecute", true);
//                 setCheckBox(3, true);
//                 setCheckBox(2, true);
        } else if (!this.authors.contains(AccessAuthority.str[1]) && !this.authors.contains(AccessAuthority.str[2]) && !this.authors.contains(AccessAuthority.str[3])
                && !this.authors.contains(AccessAuthority.str[4]) && !this.authors.contains(AccessAuthority.str[5])
                && !this.authors.contains(AccessAuthority.str[6])) {
            map.put("listFolderAndContent", false);
            map.put("readAndExecute", false);
//                 setCheckBox(3, false);
//                 setCheckBox(2, false);
        } else {
            //setCheckBox(6, true);
            map.put("special", true);
        }

        if (this.authors.contains(AccessAuthority.str[1]) && this.authors.contains(AccessAuthority.str[2])
                && this.authors.contains(AccessAuthority.str[3]) && this.authors.contains(AccessAuthority.str[4])
                && this.authors.contains(AccessAuthority.str[5]) && this.authors.contains(AccessAuthority.str[6])
                && this.authors.contains(AccessAuthority.str[7]) && this.authors.contains(AccessAuthority.str[8])
                //                 && this.authors.contains(AccessAuthority.str[9]) && this.authors.contains(AccessAuthority.str[10])
                && this.authors.contains(AccessAuthority.str[11]) && this.authors.contains(AccessAuthority.str[12])
                //                 && this.authors.contains(AccessAuthority.str[13]) && this.authors.contains(AccessAuthority.str[17])
                && this.authors.contains(AccessAuthority.str[16]) && this.authors.contains(AccessAuthority.str[14])
                && this.authors.contains(AccessAuthority.str[18]) && this.authors.contains(AccessAuthority.str[19])
                && this.authors.contains(AccessAuthority.str[20])) {
            // setCheckBox(1, true);
            map.put("update", true);
        } else if (!this.authors.contains(AccessAuthority.str[1]) && !this.authors.contains(AccessAuthority.str[2])
                && !this.authors.contains(AccessAuthority.str[3]) && !this.authors.contains(AccessAuthority.str[4])
                && !this.authors.contains(AccessAuthority.str[5]) && !this.authors.contains(AccessAuthority.str[6])
                && !this.authors.contains(AccessAuthority.str[7]) && !this.authors.contains(AccessAuthority.str[8])
                //                 && !this.authors.contains(AccessAuthority.str[9]) && !this.authors.contains(AccessAuthority.str[10])
                && !this.authors.contains(AccessAuthority.str[11]) && !this.authors.contains(AccessAuthority.str[12])
                //                 && !this.authors.contains(AccessAuthority.str[13]) && !this.authors.contains(AccessAuthority.str[17])
                && !this.authors.contains(AccessAuthority.str[16]) && !this.authors.contains(AccessAuthority.str[14])
                && !this.authors.contains(AccessAuthority.str[18]) && !this.authors.contains(AccessAuthority.str[19])
                && !this.authors.contains(AccessAuthority.str[20])) {
            //  setCheckBox(1, false);
            map.put("update", false);
        } else {
            //setCheckBox(6, true);
            map.put("special", true);
        }

        if (this.authors.contains(AccessAuthority.str[1]) && this.authors.contains(AccessAuthority.str[2])
                && this.authors.contains(AccessAuthority.str[3]) && this.authors.contains(AccessAuthority.str[4])
                && this.authors.contains(AccessAuthority.str[5]) && this.authors.contains(AccessAuthority.str[6])
                && this.authors.contains(AccessAuthority.str[7]) && this.authors.contains(AccessAuthority.str[8])
                //                 && this.authors.contains(AccessAuthority.str[9]) && this.authors.contains(AccessAuthority.str[10])
                && this.authors.contains(AccessAuthority.str[11]) && this.authors.contains(AccessAuthority.str[12])
                //                 && this.authors.contains(AccessAuthority.str[13]) && this.authors.contains(AccessAuthority.str[17])
                && this.authors.contains(AccessAuthority.str[16]) && this.authors.contains(AccessAuthority.str[14])
                && this.authors.contains(AccessAuthority.str[18]) && this.authors.contains(AccessAuthority.str[19])
                && this.authors.contains(AccessAuthority.str[20]) && this.authors.contains(AccessAuthority.str[15])
                && this.authors.contains(AccessAuthority.str[21])) {
            // setCheckBox(0, true);
            map.put("positiveControl", true);
        } else if (!this.authors.contains(AccessAuthority.str[1]) && !this.authors.contains(AccessAuthority.str[2])
                && !this.authors.contains(AccessAuthority.str[3]) && !this.authors.contains(AccessAuthority.str[4])
                && !this.authors.contains(AccessAuthority.str[5]) && !this.authors.contains(AccessAuthority.str[6])
                && !this.authors.contains(AccessAuthority.str[7]) && !this.authors.contains(AccessAuthority.str[8])
                //                 && !this.authors.contains(AccessAuthority.str[9]) && !this.authors.contains(AccessAuthority.str[10])
                && !this.authors.contains(AccessAuthority.str[11]) && !this.authors.contains(AccessAuthority.str[12])
                //                 && !this.authors.contains(AccessAuthority.str[13]) && !this.authors.contains(AccessAuthority.str[17])
                && !this.authors.contains(AccessAuthority.str[16]) && !this.authors.contains(AccessAuthority.str[14])
                && !this.authors.contains(AccessAuthority.str[18]) && !this.authors.contains(AccessAuthority.str[19])
                && !this.authors.contains(AccessAuthority.str[20]) && !this.authors.contains(AccessAuthority.str[15])
                && !this.authors.contains(AccessAuthority.str[21])) {
            //setCheckBox(0, false);
            map.put("positiveControl", false);
        } else {
            //  setCheckBox(6, true);
            map.put("special", true);
        }
        //Debug.print("positiveControl :" + map.get("positiveControl"));
        //根据map中的值，设置权限列表
        auth = new Authority(this.res.get("positiveControl"), map.get("positiveControl"));
        this.generalAuthors.add(auth);
        auth = new Authority(this.res.get("update"), map.get("update"));
        this.generalAuthors.add(auth);
        auth = new Authority(this.res.get("readAndExecute"), map.get("readAndExecute"));
        this.generalAuthors.add(auth);
        auth = new Authority(this.res.get("listFolderAndContent"), map.get("listFolderAndContent"));
        this.generalAuthors.add(auth);
        auth = new Authority(this.res.get("fetch"), map.get("fetch"));
        this.generalAuthors.add(auth);
        auth = new Authority(this.res.get("writeIn"), map.get("writeIn"));
        this.generalAuthors.add(auth);
//        auth = new Authority(this.res.get("special"), map.get("special"));
//        this.generalAuthors.add(auth);

    }

    public ArrayList<Authority> getGeneralAuthors() {
        return this.generalAuthors;
    }

    public void setGeneralAuthors(ArrayList<Authority> generalAuthors) {
        this.generalAuthors = generalAuthors;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectStyle() {
        return this.objectStyle;
    }

    public void setObjectStyle(String objectStyle) {
        this.objectStyle = objectStyle;
    }

    public boolean getAuthorByConstant(int n) {
        for (int i = 0; i < this.generalAuthors.size(); ++i) {
            Authority au = (Authority) this.generalAuthors.get(i);
            if (au.getName().equals(Constant.getAuthor(n))) {
                return au.isEnabled();
            }
        }

        return false;
    }

    public void setAuthorFromMap(String authname, boolean bool) {
        this.authMap.put(authname, Boolean.valueOf(bool));
    }

    public HashMap<String, Boolean> getAuthMap() {
        return this.authMap;
    }

    public void setAuthMap(HashMap<String, Boolean> authMap) {
        this.authMap = authMap;
    }

    public void setReadAuthor() {
        this.authMap.put("readFile", Boolean.TRUE);
        this.authMap.put("listContent", Boolean.TRUE);
        this.authMap.put("readBasicAttribute", Boolean.TRUE);
        this.authMap.put("readExpandAttribute", Boolean.TRUE);
        this.authMap.put("readACL", Boolean.TRUE);
    }

    public void setWriteAuthor() {
        this.authMap.put("writeFile", Boolean.TRUE);
        this.authMap.put("writeFileOwner", Boolean.TRUE);
        this.authMap.put("writeFileBasicAttribute", Boolean.TRUE);
        this.authMap.put("writeFileExpandAttribute", Boolean.TRUE);
        this.authMap.put("writeACl", Boolean.TRUE);
    }

    public void setExcuteAuthor() {
        this.authMap.put("executeFile", Boolean.TRUE);
    }

    public void setCreateAuthor() {
        this.authMap.put("createFile", Boolean.TRUE);
        this.authMap.put("createSubDirectory", Boolean.TRUE);
    }

    public void setInheritAuthor() {
        this.authMap.put("fileInherit", Boolean.TRUE);
        this.authMap.put("dirInherit", Boolean.TRUE);
    }

    public void setDeleteAuthor() {
        this.authMap.put("deleteFile", Boolean.TRUE);
        this.authMap.put("deleteChild", Boolean.TRUE);
    }

    public boolean getAuthorFromMap(String name) {
        return ((Boolean) this.authMap.get(name)).booleanValue();
    }

    public boolean getReadFileAuthor() {
        return ((Boolean) this.authMap.get("readFile")).booleanValue();
    }

    public boolean getlistContentAuthor() {
        return ((Boolean) this.authMap.get("listContent")).booleanValue();
    }

    public boolean getReadBasicAttributeAuthor() {
        return ((Boolean) this.authMap.get("readBasicAttribute")).booleanValue();
    }

    public boolean getreadExpandAttribute() {
        return ((Boolean) this.authMap.get("readExpandAttribute")).booleanValue();
    }

    public boolean getReadACLAuthor() {
        return ((Boolean) this.authMap.get("readACL")).booleanValue();
    }

    public boolean getWriteFileAuthor() {
        return ((Boolean) this.authMap.get("writeFile")).booleanValue();
    }

    public boolean getWriteFileOwnerAuthor() {
        return ((Boolean) this.authMap.get("writeFileOwner")).booleanValue();
    }

    public boolean getWriteFileBasicAttributeAuthor() {
        return ((Boolean) this.authMap.get("writeFileBasicAttribute")).booleanValue();
    }

    public boolean getWriteFileExpandAttribute() {
        return ((Boolean) this.authMap.get("writeFileExpandAttribute")).booleanValue();
    }

    public boolean getWriteAClAuthor() {
        return ((Boolean) this.authMap.get("writeACl")).booleanValue();
    }

    public boolean getExcuteFileAuthor() {
        return ((Boolean) this.authMap.get("executeFile")).booleanValue();
    }

    public boolean getCreateFileAuthor() {
        return ((Boolean) this.authMap.get("createFile")).booleanValue();
    }

    public boolean getCreateSubDirectoryAuthor() {
        return ((Boolean) this.authMap.get("createSubDirectory")).booleanValue();
    }

    public boolean getFileInheritAuthor() {
        return ((Boolean) this.authMap.get("fileInherit")).booleanValue();
    }

    public boolean getDirInheritAuthor() {
        return ((Boolean) this.authMap.get("dirInherit")).booleanValue();
    }

    public boolean getDeleteFileAuthor() {
        return ((Boolean) this.authMap.get("deleteFile")).booleanValue();
    }

    public boolean getDeleteChildAuthor() {
        return ((Boolean) this.authMap.get("deleteChild")).booleanValue();
    }

    public void updateAuthorityFromAllAuthors(ALLAuthoritiesValue authors) {
        setAuthorFromMap("readFile", authors.readFile);
        setAuthorFromMap("listContent", authors.listContent);
        setAuthorFromMap("readBasicAttribute", authors.readBasicAttribute);
        setAuthorFromMap("readExpandAttribute", authors.readExpandAttribute);
        setAuthorFromMap("readACL", authors.readACL);
        setAuthorFromMap("createFile", authors.createFile);
        setAuthorFromMap("createSubDirectory", authors.createSubDirectory);
        setAuthorFromMap("executeFile", authors.executeFile);
        setAuthorFromMap("writeFile", authors.writeFile);
        setAuthorFromMap("writeFileOwner", authors.writeFileOwner);
        setAuthorFromMap("writeFileBasicAttribute", authors.writeFileBasicAttribute);
        setAuthorFromMap("writeFileExpandAttribute", authors.writeFileExpandAttribute);
        setAuthorFromMap("writeACl", authors.writeACl);
        setAuthorFromMap("deleteFile", authors.deleteFile);
        setAuthorFromMap("deleteChild", authors.deleteChild);
        setAuthorFromMap("fileInherit", authors.fileInherit);
        setAuthorFromMap("dirInherit", authors.dirInherit);
        updateGeneralAuthors();
    }

    public void clickReadMore() {
        if (!(this.readRendered)) {
            this.readRendered = true;
        } else {
            this.readRendered = false;
        }
    }

    public void clickWriteMore() {
        if (!(this.writeRendered)) {
            this.writeRendered = true;
        } else {
            this.writeRendered = false;
        }
    }

    public void clickCreateMore() {
        if (!(this.createRendered)) {
            this.createRendered = true;
        } else {
            this.createRendered = false;
        }
    }

    public void clickDeleteMore() {
        if (!(this.deleteRendered)) {
            this.deleteRendered = true;
        } else {
            this.deleteRendered = false;
        }
    }

    public void clickExecuteMore() {
        if (!(this.executeRendered)) {
            this.executeRendered = true;
        } else {
            this.executeRendered = false;
        }
    }

    public void clickInheritMore() {
        if (!(this.inheritRendered)) {
            this.inheritRendered = true;
        } else {
            this.inheritRendered = false;
        }
    }

    public ArrayList<String> getAuthors() {
        return this.authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }
}