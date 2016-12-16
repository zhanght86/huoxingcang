/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.model.UserInfo;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "system_userInfoListBean")
@ViewScoped
public class System_userInfoListBean implements Serializable {
    
    private List<UserInfo> userInfoList;
    private UserInfo selectedUser;
    private UserInfo userInfo;
    private String userName;
    private String userType;
    private String password;
    private String secPassword;
    private List<String> userTypeList;
     boolean notad = false;
     private MSAResource res = new MSAResource();
    private String basename = "common.system_user";
    public String tip;

    /**
     * Creates a new instance of system_userInfoListBean
     */
    public System_userInfoListBean() {
      tip =res.get(basename, "delete_tip");
       this.usersInfo();
       userTypeList();
        
    }
    
    public void usersInfo(){
       
        this.setUserInformation();
        SystemOutPrintln.print_common("userInfoList=" + userInfoList.size());
    }
    public void userTypeList(){
        userTypeList = new ArrayList();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        String currUserName = user.getName();
        int currUserType = user.getType();
        MSAGlobalResource re = new MSAGlobalResource();
        if (currUserType == 2) {
            userTypeList.add(re.get("admin"));
            userTypeList.add(re.get("common"));
            userTypeList.add(re.get("authorized"));
        }else{
            userTypeList.add(re.get("auditor"));
        }
        
        
    }
    
    public UserInfo getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSecPassword() {
        return secPassword;
    }
    
    public void setSecPassword(String secPassword) {
        this.secPassword = secPassword;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
    
    private void setUserInformation() {
         userInfoList = new ArrayList<UserInfo>();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        String currUserName = user.getName();
        int currUserType = user.getType();
        MSAGlobalResource re = new MSAGlobalResource();
        
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        SystemUserInformation[] users = common.getUsers();


        if (currUserType == 2) {
            notad = false;
            SystemOutPrintln.print_common("notad1="+notad);
        } else {
            notad = true;
            
             SystemOutPrintln.print_common("notad="+notad);
        }
//         List<SystemUserInformation> users_admin = new ArrayList();
//         List<SystemUserInformation> users_audit = new ArrayList();
//         List<SystemUserInformation> users_common = new ArrayList();
//         if(userList != null && userList.length>0){
//              for (int j = 0; j < userList.length; j++) {
//                  if (userList[j].getType() == 2) {  //管理员
//                    users_admin.add(userList[j]);
//                } else if (userList[j].getType() == 3) {  //审计员
//                     users_audit.add(userList[j]);
//                } else {
//                     users_common.add(userList[j]);  //普通用户为1
//                }
//              }
//         }
//         List<SystemUserInformation> user_list = new ArrayList();
//         if(users_admin.size()>0){
//             for(int k=0; k<users_admin.size();k++){
//                  user_list.add(users_admin.get(k));
//             }
//         }
//          if(users_audit.size()>0){
//             for(int m=0; m<users_audit.size();m++){
//                  user_list.add(users_audit.get(m));
//             }
//         }
//           if(users_common.size()>0){
//             for(int n=0; n<users_common.size();n++){
//                  user_list.add(users_common.get(n));
//             }
//         }
// 
//        SystemUserInformation[] users = new SystemUserInformation[user_list.size()];
//        for(int k=0; k<user_list.size();k++){
//            users[k] = user_list.get(k);
//        }

        if (users != null && users.length>0) {

//            System.out.println("用户个数=" + users.length);
            for (int i = 0; i < users.length; i++) {
                userInfo = new UserInfo();
//                System.out.println("用户名=" + users[i].getName());
                userInfo.setUserName(users[i].getName());
                userInfo.setUserType(users[i].getType() + "");
                if (userInfo.getUserType().equals("2")) {  //管理员
                    if(userInfo.getUserName().equalsIgnoreCase("admin")){
                        userInfo.setUserTypeStr(re.get("in_admin"));
                    }else{
                        userInfo.setUserTypeStr(re.get("admin"));
                    }  
                } else if (userInfo.getUserType().equals("3")) {  //审计员
                    userInfo.setUserTypeStr(re.get("auditor"));
                } else if (userInfo.getUserType().equals("4")) {  //授权用户
                    userInfo.setUserTypeStr(re.get("authorized"));
                }else {
                    userInfo.setUserTypeStr(re.get("common"));  //普通用户为1
                }
                boolean notde = false;
                boolean notmo = false;
                
                
                if (currUserType != 2 && currUserType != 3) {  //普通用户、授权用户
                    notde = true;
                    if(userInfo.getUserName().equalsIgnoreCase(currUserName)){
                         notmo = false;
                    }else{
                         notmo = true;
                    }
                   
                } else if (currUserType == 2) {  //管理员     
                    if (currUserName.equals("admin")) {
                        if (userInfo.getUserType().equals("3")) {
                            notde = true;
                            notmo = true;
                        } else {
                            if (userInfo.getUserName().equalsIgnoreCase("admin")) {
                                notde = true;
                                notmo = false;
                            } else {
                                notde = false;
                                notmo = true;
                            }

                        }
                    } else {
                        if (userInfo.getUserType().equals("3") || userInfo.getUserName().equals("admin")) {
                            notde = true;
                            notmo = true;
                        } else {
                            if (currUserName.equals(userInfo.getUserName())) {
                                notde = true;
                                notmo = false;
                            } else {
                                notde = false;
                                notmo = true; 
                            }   
                        }
                    }                    
                } else if (currUserType == 3) {  //审计员
                        if (currUserName.equalsIgnoreCase(userInfo.getUserName())) {  //目前只有一个审计员
                            notde = true;
                            notmo = false;
                        }else{
                            notde = true;
                            notmo = true;
                        }
                }
                
//                userInfo.setNotadd(notad);
                userInfo.setNotdelete(notde);
                userInfo.setNotmodify(notmo);
                userInfoList.add(userInfo);
               
            }
        }
     
    }
    
    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }
    
    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
    
    public UserInfo getSelectedUser() {
        return selectedUser;
    }
    
    public void setSelectedUser(UserInfo selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<String> getUserTypeList() {
        return userTypeList;
    }

    public void setUserTypeList(List<String> userTypeList) {
        this.userTypeList = userTypeList;
    }

    public boolean isNotad() {
        return notad;
    }

    public void setNotad(boolean notad) {
        this.notad = notad;
    }
    
    public void selectUser(UserInfo select){
        selectedUser = select;
          SystemOutPrintln.print_common("set selectedUser="+selectedUser.getUserName());
    }
    
    public void deleteUser_tip() {
         SystemOutPrintln.print_common("selectedUser.getUserName()="+selectedUser.getUserName());
        tip = res.get(basename, "delete_left") + selectedUser.getUserName() + res.get(basename, "delete_right");
         SystemOutPrintln.print_common("tip="+tip);
         RequestContext.getCurrentInstance().execute("deleteUser.show()");
    }
    
    
    public void deleteUser() {

        SystemOutPrintln.print_common("delete user ="+selectedUser.getUserName());
        MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
        String[] auths = sync.getAuthedUser();
        boolean isAuth = false;
        if(auths!= null && auths.length>0){
            for(String auth:auths){
                if(auth.equals(selectedUser.getUserName())){
                    isAuth = true;
                    break;
                }
            }
        }
        if (isAuth) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "user") + selectedUser.getUserName() + res.get(basename, "isauthername"), ""));
            return;
        }
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean deleteUser = common.deleteUser(selectedUser.getUserName());
        if (deleteUser == true) {
            this.setUserInformation();
//            userInfoList.remove(selectedUser);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deleteuser")+selectedUser.getUserName()+res.get(basename, "fail") ,  ""));
        }
        
        
        
    }
    
    public void deleteRow(UserInfo user) {
        userInfoList.remove(user);
    }
    
    public void deleteUserInfo() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        String userName = getParam(context);
        context.getViewRoot().setLocale(new Locale(userName));

//        FacesContext context2 = FacesContext.getCurrentInstance();
        String userType = getParamType(context);
        context.getViewRoot().setLocale(new Locale(userType));
        
        String name = "";
        String type = "";
        for (int i = 0; i < userInfoList.size(); i++) {
            name = userInfoList.get(i).getUserName();
            type = userInfoList.get(i).getUserType();
            if (userName.equals(name) && userType.equals(type)) {
                userInfoList.remove(userInfoList.get(i));
                break;
            }
        }
    }
    
    private String getParam(FacesContext comtext) {
        
        
        
        
        Map<String, String> params = comtext.getExternalContext().getRequestParameterMap();
        return params.get("userName");
    }
    
    private String getParamType(FacesContext comtext) {
        Map<String, String> params = comtext.getExternalContext().getRequestParameterMap();
        return params.get("userType");
    }
    
    public String addUserInfo() {
        //        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
         MSAGlobalResource re = new MSAGlobalResource();
         int type= 1;
        if(userType.equals(re.get("admin"))){
            type = 2;
        }else if(userType.equals(re.get("auditor"))){
            type = 3;
        }else if(userType.equals(re.get("authorized"))){
            type = 4;
        }else{
            type= 1;
        }
          
        SystemOutPrintln.print_common("add user");
    

        if ("".equals(userName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "username_no"), ""));
            return null;
        }
        if (!userName.matches("^[a-z]+[a-z0-9]*+$") || userName.length() > 8 || userName.length() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "username_limit"), ""));
            return null;
        }
        
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        SystemUserInformation[] users = common.getUsers();
        if (users != null && users.length>0) {
     
            for (int i = 0; i < users.length; i++) {
                if (users[i].getName().equalsIgnoreCase(userName.trim())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "username_exit"), ""));
                    return null;
                }
            }
        }
        if (password.length() == 0 || secPassword.length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_limit"), ""));
            return null;
        }
        if (!password.matches("[a-zA-Z0-9]+") || password.length() < 6 || password.length() > 32) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_limit"), ""));
            return null;
        }
        if (!secPassword.matches("[a-zA-Z0-9]+") || secPassword.length() < 6 || secPassword.length() > 32) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_limit"), ""));
           return null;
        }

        if (!checkPassword(password, secPassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_notsame"), ""));
            return null;
        }
        
        
       
         

        boolean addUser = common.createUser(userName, type, password);
        String returnstr = null;
        if (addUser == true) {
//            userInfo = new User();
//
//            userInfo.setUserName(userName);
//            userInfo.setUserType(userType);
//
//            if (userType.equals("1")) {
//                userInfo.setUserTypeStr("管理员");
//            } else if (userType.equals("2")) {
//                userInfo.setUserTypeStr("审计员");
//            } else {
//                userInfo.setUserTypeStr("普通");
//            }
//            userInfoList.add(userInfo);
            userName = "";
            SystemOutPrintln.print_common("add user succeed");
            this.setUserInformation();
            returnstr = "system_user?faces-redirect=true";
           
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "adduser_fail"), ""));
        }
        return returnstr;
        
        
        
        
    }
    
    public String cancleButton(){
    
            return "system_user?faces-redirect=true";
    }
    
       public boolean checkPassword(String inpsw, String inpsw2) {
        boolean flag = false;
        if (inpsw.equals(inpsw2)) {
            flag = true;
        }
        return flag;
    }
    
    public void login(ActionEvent actionEvent) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        
        if (password != null && secPassword != null && password.equals(secPassword)) {
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mdify OK", "Effective validation");
        } else {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mdify Error", "Invalid credentials");
        }
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }
    
//    public String editUser(String name,String pass,String secPass) {
//         if (pass.length() == 0 || secPass.length() == 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "无效的密码", "。"));
//            return null;
//        }
//        if (pass.length() < 6 || pass.length() > 32) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "无效的密码，密码长度须在6~32位之间。", ""));
//            return null;
//        }
//        if (secPass.length() < 6 || secPass.length() > 32) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "无效的密码，密码长度须在6~32位之间。", ""));
//           return null;
//        }
//
//        if (!checkPassword(pass, secPass)) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "密码不一致。", ""));
//            return null;
//        }
//          System.out.println("33333333");
//         System.out.println("name=" + name);
//           System.out.println("pass=" + pass);
//        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
//        
//        boolean modifyUser = common.modifyUserPassword(name,pass);
//        String returnstr = null;
//        if (modifyUser) {
////            String type = selectedUser.getUserType();
////            System.out.println("类型=" + selectedUser.getUserType());
////
////            selectedUser.setUserType(type);
////            if (type.equals("1")) {
////                selectedUser.setUserTypeStr("管理员");
////            } else if (type.equals("2")) {
////                selectedUser.setUserTypeStr("审计员");
////            } else if (type.equals("0")) {
////                selectedUser.setUserTypeStr("普通");
////            }
//             System.out.println("edit user succeed");
//            this.setUserInformation();
//            returnstr = "system_user?faces-redirect=true";
//        } else {
//               System.out.println("edit user fail");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改用户失败。", ""));
//        }
//        
//        
//        return returnstr;
//    }
    
    public String maxUsers() {
        userTypeList();
        MSAGlobalResource re = new MSAGlobalResource();
        String returnStr = null;
        if (userInfoList.size() >= Integer.valueOf(re.get("maxusers"))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "usernum"), ""));
        } else {
            return "system_user_adduser?faces-redirect=true";
        }
        return returnStr;
    }
    
    public String toEditUser(String userName){
        String param = "userName=" + userName;
        return "system_user_edit?faces-redirect=true&amp;" + param;
        
    }
//    
//    public void return_user(){
//        this.usersInfo();
//       
//    }
    
//    public String toEditUser(UserInfo user){
//        this.selectedUser = user;
//        return "system_user_edit?faces-redirect=true";
//    }
}
