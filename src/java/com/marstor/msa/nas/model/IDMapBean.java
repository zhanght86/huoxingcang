/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.IDMapRule;
import com.marstor.msa.nas.bean.User;
import com.marstor.msa.nas.bean.UserGroup;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "idmapbean")
@ViewScoped
public class IDMapBean implements Serializable {

    private IDMapDataModel dataModel = new IDMapDataModel();
    private IDMap selectedMap = new IDMap();
    private IDMap deletedMap = new IDMap();
    private ArrayList<String> winUserNames = new ArrayList<String>();
    private ArrayList<String> nasUserNames = new ArrayList<String>();
    private ArrayList<String> winGroupNames = new ArrayList<String>();
    private ArrayList<String> nasGroupNames = new ArrayList<String>();
    private ArrayList<String> winTypes = new ArrayList<String>();
    private ArrayList<String> unixTypes = new ArrayList<String>();
    // private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> winNames = new ArrayList<String>();
    private ArrayList<String> unixNames = new ArrayList<String>();
    private String selectedWinType = "";
    private String selectedUnixType = "";
    private String selectedWinName = "";
    private String selectedUnixName = "";
    private String winTypeName = "";
    private String unixTypeName = "";
    private MSAResource res = new MSAResource();
    private ArrayList<IDMap> maps = new ArrayList<IDMap>();

    public IDMapBean() {
        winTypes.add(res.get("winUser"));
        winTypes.add(res.get("winGroup"));
        unixTypes.add(res.get("nasUser"));
        unixTypes.add(res.get("nasGroup"));
        this.winUserNames = InterfaceFactory.getNASInterfaceInstance().getAllUsersInDomain();
        if (winUserNames == null) {
            winUserNames = new ArrayList<String>();
            //return;
        }
//       winUserNames.add("abc@company.com");
//        winUserNames.add("def@company.com");
        this.winGroupNames = InterfaceFactory.getNASInterfaceInstance().getAllUserGroupsInDomain();
        if (this.winGroupNames == null) {
            winGroupNames = new ArrayList<String>();
            // return;
        }
        // winGroupNames.add("gr@company.com");
        ArrayList<User> sysUsers = InterfaceFactory.getNASInterfaceInstance().getAllSystemUsersInfo();
        if (sysUsers == null) {
            sysUsers = new ArrayList<User>();
            //return;
        }
        for (int i = 0; i < sysUsers.size(); i++) {
            nasUserNames.add(sysUsers.get(i).getName());
        }
//      UserGroupData groupData = MySession.getGroupsFromSession();
//        nasUserNames = groupData.getUserModel().getUserNames();
        ArrayList<UserGroup> sysGroups = InterfaceFactory.getNASInterfaceInstance().getAllSystemUserGroups();
        if (sysGroups == null) {
            sysGroups = new ArrayList<UserGroup>();
        }
        for (UserGroup group : sysGroups) {
            nasGroupNames.add(group.getName());
        }
        //  nasGroupNames = groupData.getGroupModel().getGroupNames();

        this.selectedWinType = res.get("winUser");
        this.selectedUnixType = res.get("nasUser");
        this.winNames = winUserNames;
        if (this.winNames.size() == 0) {
            this.winNames.add("");
        }
        this.unixNames = nasUserNames;
        if (this.unixNames.size() == 0) {
            this.unixNames.add("");
        }
//        this.winTypeName = Constant.winUserName;
//        this.unixTypeName = Constant.unixUserName;
        ArrayList<IDMapRule> idMaps = InterfaceFactory.getNASInterfaceInstance().getAllIDMapRule();
        if (idMaps == null) {
            return;
        }
        for (int i = 0; i < idMaps.size(); i++) {
            IDMapRule rule = idMaps.get(i);
            IDMap map = new IDMap(0, rule.getWinType(), rule.getUnixType(), rule.getWinName(), rule.getUnixName());
            this.maps.add(map);
        }
//        IDMap map = new IDMap(this.dataModel.getNum(), Constant.winUser, Constant.unixUser, "abc@company.com", "abc");
//
//        dataModel.save(map);




    }

    public ArrayList<IDMap> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<IDMap> maps) {
        this.maps = maps;
    }

    public IDMap getDeletedMap() {
        return deletedMap;
    }

    public void setDeletedMap(IDMap deletedMap) {
        this.deletedMap = deletedMap;
    }

    public IDMap getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(IDMap selectedMap) {
        this.selectedMap = selectedMap;
    }

    public String getUnixTypeName() {
        return unixTypeName;
    }

    public void setUnixTypeName(String unixTypeName) {
        this.unixTypeName = unixTypeName;
    }

    public String getWinTypeName() {
        return winTypeName;
    }

    public void setWinTypeName(String winTypeName) {
        this.winTypeName = winTypeName;
    }

    public ArrayList<String> getUnixNames() {
        return unixNames;
    }

    public void setUnixNames(ArrayList<String> unixNames) {
        this.unixNames = unixNames;
    }

    public ArrayList<String> getWinNames() {
        return winNames;
    }

    public void setWinNames(ArrayList<String> winNames) {
        this.winNames = winNames;
    }

//    public ArrayList<String> getNames() {
//        return names;
//    }
//
//    public void setNames(ArrayList<String> names) {
//        this.names = names;
//    }
    public IDMapDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(IDMapDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public ArrayList<String> getNasGroupNames() {
        return nasGroupNames;
    }

    public void setNasGroupNames(ArrayList<String> nasGroupNames) {
        this.nasGroupNames = nasGroupNames;
    }

    public ArrayList<String> getNasUserNames() {
        return nasUserNames;
    }

    public void setNasUserNames(ArrayList<String> nasUserNames) {
        this.nasUserNames = nasUserNames;
    }

    public String getSelectedUnixName() {
        return selectedUnixName;
    }

    public void setSelectedUnixName(String selectedUnixName) {
        this.selectedUnixName = selectedUnixName;
    }

    public String getSelectedUnixType() {
        return selectedUnixType;
    }

    public void setSelectedUnixType(String selectedUnixType) {
        this.selectedUnixType = selectedUnixType;
    }

    public String getSelectedWinName() {
        return selectedWinName;
    }

    public void setSelectedWinName(String selectedWinName) {
        this.selectedWinName = selectedWinName;
    }

    public String getSelectedWinType() {
        return selectedWinType;
    }

    public void setSelectedWinType(String selectedWinType) {
        this.selectedWinType = selectedWinType;
    }

    public ArrayList<String> getUnixTypes() {
        return unixTypes;
    }

    public void setUnixTypes(ArrayList<String> unixTypes) {
        this.unixTypes = unixTypes;
    }

    public ArrayList<String> getWinGroupNames() {
        return winGroupNames;
    }

    public void setWinGroupNames(ArrayList<String> winGroupNames) {
        this.winGroupNames = winGroupNames;
    }

    public ArrayList<String> getWinTypes() {
        return winTypes;
    }

    public void setWinTypes(ArrayList<String> winTypes) {
        this.winTypes = winTypes;
    }

    public ArrayList<String> getWinUserNames() {
        return winUserNames;
    }

    public void setWinUserNames(ArrayList<String> winUserNames) {
        this.winUserNames = winUserNames;
    }

    public void changeWinTypeValue() {

        if (this.selectedWinType.equals(res.get("winUser"))) {
            this.selectedUnixType = res.get("nasUser");
            this.winNames = this.winUserNames;
            this.unixNames = this.nasUserNames;
//            this.winTypeName = Constant.winUserName;
//            this.unixTypeName = Constant.unixUserName;

        }
        if (this.selectedWinType.equals(res.get("winGroup"))) {
            this.selectedUnixType = res.get("nasGroup");
            this.winNames = this.winGroupNames;
            this.unixNames = this.nasGroupNames;
//            this.winTypeName = Constant.winGroupName;
//            this.unixTypeName = Constant.unixGroupName;

        }
        if (this.winNames.size() == 0) {
            this.winNames.add("");
        }
        if (this.unixNames.size() == 0) {
            this.unixNames.add("");
        }

    }

    public void changeUnixTypeValue() {
        if (this.selectedUnixType.equals(res.get("nasUser"))) {
            this.selectedWinType = res.get("winUser");
            this.unixNames = this.nasUserNames;
            this.winNames = this.winUserNames;
//            this.winTypeName = Constant.winUserName;
//            this.unixTypeName = Constant.unixUserName;
        }
        if (this.selectedUnixType.equals(res.get("nasGroup"))) {
            this.selectedWinType = res.get("winGroup");
            this.unixNames = this.nasGroupNames;
            this.winNames = this.winGroupNames;
//            this.winTypeName = Constant.winGroupName;
//            this.unixTypeName = Constant.unixGroupName;
        }
        if (this.winNames.size() == 0) {
            this.winNames.add("");
        }
        if (this.unixNames.size() == 0) {
            this.unixNames.add("");
        }

    }

    public String save() {
        Debug.print("select winname: " + this.selectedWinName);
        
        if (this.selectedWinName == null || this.selectedWinName.equals("")) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyWinUser"), res.get("emptyWinUser")));
//            return;
            if (this.selectedWinType.trim().equals(res.get("winUser"))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyWinUser"), res.get("emptyWinUser")));
                return null;
            }
            if (this.selectedWinType.trim().equals(res.get("winGroup"))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyWinGroup"), res.get("emptyWinGroup")));
                return null;
            }
        }
//        if (!isValidName(this.selectedWinName)) {
//            // MSAResource res = new MSAResource();
//            if (this.selectedWinType.trim().equals(res.get("winUser"))) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidWinUser"), res.get("invalidWinUser")));
//                return null;
//            }
//            if (this.selectedWinType.trim().equals(res.get("winGroup"))) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidWinGroup"), res.get("invalidWinGroup")));
//                return null;
//            }
//        }
        //判断指定的Windows用户或用户组名称是否存在
//        if (this.selectedWinType.equals(Constant.winUser)) {
//            if (this.winUserNames.size() > 0) {
//                if (!this.winUserNames.contains(this.selectedWinName)) {
//                    RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongWinUserNameContain);
//                    return;
//                }
//            }
//        }
//        if (this.selectedWinType.equals(Constant.winGroup)) {
//            if (this.winGroupNames.size() > 0) {
//                if (!this.winGroupNames.contains(this.selectedWinName)) {
//                    RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongWinGroupNameContain);
//                    return;
//                }
//            }
//        }
//        if (this.selectedUnixType.equals(Constant.unixUser)) {
//            if (this.nasUserNames.size() > 0) {
//                if (!this.nasUserNames.contains(this.selectedUnixName)) {
//                    RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongUnixUserNameContain);
//                    return;
//                }
//            }
//        }
//        if (this.selectedUnixType.equals(Constant.unixGroup)) {
//            if (this.nasGroupNames.size() > 0) {
//                if (!this.nasGroupNames.contains(this.selectedUnixName)) {
//                    RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongUnixGroupNameContain);
//                    return;
//                }
//            }
//        }


        if (this.isExistWinName(this.selectedWinName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("multiple"), res.get("multiple")));
            //     RequestContext.getCurrentInstance().addCallbackParam("result", Constant.existIDMap);
            return null;
        }
        //   IDMap map = new IDMap(0, this.selectedWinType, this.selectedUnixType, this.selectedWinName, this.selectedUnixName);
        Debug.print("selectedWinType: " + this.selectedWinType);
        Debug.print("selectedUnixType: " + this.selectedUnixType);
        Debug.print("selectedWinName: " + this.selectedWinName);
        Debug.print("selectedUnixName: " + this.selectedUnixName);
        String type="";
        if (this.selectedWinType.trim().equals(res.get("winUser"))) {
           type = "user";
        }else {
            type = "group";
        }
        IDMapRule rule = new IDMapRule(type, type, this.selectedWinName, this.selectedUnixName);
        boolean flag = InterfaceFactory.getNASInterfaceInstance().createIDMapRule(rule);
        //this.dataModel.save(map);
        // RequestContext.getCurrentInstance().addCallbackParam("result", "success");
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createFailed"), res.get("createFailed")));
            Debug.print("create id map " + flag);
            return null;
        }
//        this.maps = new ArrayList<IDMap>();
//         ArrayList<IDMapRule> idMaps = InterfaceFactory.getNASInterfaceInstance().getAllIDMapRule();
//        for (int i = 0; i < idMaps.size(); i++) {
//            IDMapRule map = idMaps.get(i);
//            IDMap idmap = new IDMap(0, map.getWinType(), map.getUnixType(), map.getWinName(), map.getUnixName());
//            this.maps.add(idmap);
//        }
        return "nas_domain_config?faces-redirect=true&amp;accordionActive1=1";
    }

    public void removeIDMap() {
//
//        if (this.deletedMap != null) {
//            this.dataModel.removeIDMap(deletedMap);
//        }
        IDMapRule rule = new IDMapRule(this.selectedMap.getWinType(), this.selectedMap.getUnixType(), this.selectedMap.getWinName(), this.selectedMap.getUnixName());
        boolean flag = InterfaceFactory.getNASInterfaceInstance().removeSpecifiedIDMap(rule);
        if (!flag) {
            Debug.print("remove idmap " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deleteIDmapFailed"), res.get("deleteIDmapFailed")));
            return;
        }
        this.maps = new ArrayList<IDMap>();
        ArrayList<IDMapRule> idMaps = InterfaceFactory.getNASInterfaceInstance().getAllIDMapRule();
        for (int i = 0; i < idMaps.size(); i++) {
            IDMapRule map = idMaps.get(i);
            IDMap idmap = new IDMap(0, map.getWinType(), map.getUnixType(), map.getWinName(), map.getUnixName());
            this.maps.add(idmap);
        }
    }

    public void doBeforeRemoveIDMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        String indexStr = context.getExternalContext().getRequestParameterMap().get("index");
        int index = Integer.parseInt(indexStr);
        //IDMap  map = this.dataModel.getMapByIndex(index);
        this.deletedMap = new IDMap();
        this.deletedMap = this.dataModel.getMapByIndex(index);
    }

    public static boolean isValidName(String name) {
//        if(!name.contains("@") || !name.contains(".") ) {
//            return false;
//        }

        if (getCharacterCountInStr(name, '@') != 1 || getCharacterCountInStr(name, '.') != 1) {
            return false;
        }
        String nameArray1[] = name.split("@");
        if (nameArray1[0].equals("") || nameArray1[1].equals("") || !nameArray1[0].matches("^[a-zA-Z0-9]+$")) {
            return false;
        }
        if (!nameArray1[1].contains(".")) {
            return false;
        }
        String nameArray2[] = nameArray1[1].split("\\.");
        if (nameArray2[0].equals("") || nameArray2[1].equals("") || !nameArray2[0].matches("^[a-zA-Z0-9]+$") || !nameArray2[1].matches("^[a-zA-Z0-9]+$")) {
            return false;
        }
        return true;

    }

    public static int getCharacterCountInStr(String str, char c) {
        char[] chs = str.toCharArray();
        int count = 0;
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == c) {
                count++;
            }
        }
        return count;
    }

    public static void main(String args[]) {
        String ss = "s123fd@s123df.com";
        Debug.print(String.valueOf(getCharacterCountInStr(ss, '.')));
        String aa[] = ss.split("@");
        if (aa[0].equals("")) {
            Debug.print("ok");
        }
        if (aa[1].matches("^[a-zA-Z0-9]+$")) {
            Debug.print("ok");
        }
        if (isValidName(ss)) {
            Debug.print("good");
        }
        Debug.print(aa[1]);
    }

    public boolean isExistWinName(String winName) {
        for (IDMap map : maps) {
            if (map.getWinName().equals(winName)) {
                return true;
            }
        }
        return false;
    }
}
