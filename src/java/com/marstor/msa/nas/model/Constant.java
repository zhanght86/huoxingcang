/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Constant implements Serializable{

    public static final int positiveControl = 1;
    public static final int update = 2;
    public static final int readAndExecute = 3;
    public static final int listFolderAndContent = 4;
    public static final int read = 5;
    public static final int write = 6;
    public static final int special = 7;
    public static final int readFile = 8;
    public static final int listContent = 9;
    public static final int readBasicAttribute = 10;
    public static final int readExpandAttribute = 11;
    public static final int readACL = 12;
    public static final int writeFile = 13;
    public static final int writeFileOwner = 14;
    public static final int writeFileBasicAttribute = 15;
    public static final int writeFileExpandAttribute = 16;
    public static final int writeACl = 17;
    public static final int createFile = 18;
    public static final int createSubDirectory = 19;
    public static final int execute = 20;
    public static final int deleteFile = 21;
    public static final int deleteChild = 22;
    public static final int fileInherit = 23;
    public static final int dirInherit = 24;
    public static final int delete = 25;
    public static final int executeFile = 26;
    public static final int inherit = 27;
    public static final int fetch = 28;
    public static final int writeIn = 29;
    public static final int create =  30;
    public static final int sys = 1;
    public static final int user = 2;
    public static final int group = 3;
    private static final String[] authorityNames = {"", "完全控制", "修改", "读取和运行", "列出文件夹目录",
        "读", "写", "特别的权限", "读取文件", "列出目录", "读取文件基本属性", "读取文件扩展属性", "读取ACL",
        "写文件", "写文件的属主或组", "写文件基本属性", "写文件扩展属性", "写ACL", "创建文件", "创建子目录", "执行文件",
        "删除文件", "删除子文件", "文件继承", "子目录继承","删除","执行文件","继承","读取","写入","创建"};
    private static final String[] styleNames = {"", "系统ACL", "用户", "组"};

    public static String  success = "成功";
    public static String  failed = "失败";
    public static String  open = "开启";
    public static String  notOpen = "未开启";
    public static String  wrongIP = "IP地址格式错误，请重新输入！";
    public static String  wrongConfigNFS = "只读权限与读写权限请至少设置一项！";
    public static String  configSuccess = "设置成功！";
    public static String  aclExist = "该用户权限已存在！";
    public static String  configACL = "请配置权限！";
    public static String  winUser = "Windows域用户";
    public static String  unixUser = "NAS用户";
    public static String  winGroup = "Windows域用户组";
    public static String  unixGroup = "NAS组";
    public static String  winUserName = " Windows用户名称:";
    public static String  winGroupName = "Windows用户组名称:";
    public static String  unixUserName =  " NAS用户名称:";
    public static String  unixGroupName = "NAS用户组名称:";
    public static String  emptyUserName = "Windows和NAS用户名称都不能为空";
    public static String  emptygroupName = "Windows和NAS用户组名称都不能为空";
    public static String  wrongWinName = "Windows用户或用户组格式错误！示例：administrator@company.com";
    public static String  wrongWinUserNameContain = "指定的Windows用户名称不存在！";
    public static String  wrongWinGroupNameContain = "指定的Windows用户组名称不存在！";
        public static String  wrongUnixUserNameContain = "指定的NAS用户名称不存在！";
    public static String  wrongUnixGroupNameContain = "指定的NAS用户组名称不存在！";
    public static String  existIDMap ="指定的ID映射已存在";
    public static String  userStyle = "用户:";
    public static String  groupStyle = "组:";
    public static String  yes = "是";
    public static String  no = "否";
    public static String  on = "在线";
    public static String  off = "离线";
    public static String  onLine = "上线";
    public static String  offLine = "离线";
    public static String  closeShare ="请关闭共享功能！";
    public static String  emptyShareName ="共享名称不能为空！";
    public static String errorShareName= "共享名称由字母和数字组成，且长度不超过32位！";
    public static String emptyPathName = "目录名称不能为空！";
    public static String errorPathName= "目录名称由字母和数字组成，且长度不超过32位！";
    public static String joinedToDomain = "已加入域";
    public static String domainName = "域名";
    public static String domainController = "域控制器";
    
    public static String getAuthor(int i) {
        return authorityNames[i];
    }

    public static String getOneStyleName(int i) {
        return styleNames[i];
    }

    public ArrayList<String> getReadAuthoritis() {
        ArrayList<String> reads = new ArrayList<String>();
        reads.add(authorityNames[Constant.readFile]);
        reads.add(authorityNames[Constant.listContent]);
        reads.add(authorityNames[Constant.readBasicAttribute]);
        reads.add(authorityNames[Constant.readExpandAttribute]);
        reads.add(authorityNames[Constant.readACL]);

        return reads;
    }

    public ArrayList<String> getWriteAuthoritis() {
        ArrayList<String> writes = new ArrayList<String>();
        writes.add(authorityNames[Constant.writeFile]);
        writes.add(authorityNames[Constant.writeFileOwner]);
        writes.add(authorityNames[Constant.writeFileBasicAttribute]);
        writes.add(authorityNames[Constant.writeFileExpandAttribute]);
        writes.add(authorityNames[Constant.writeACl]);

        return writes;
    }
    public ArrayList<String> getCreatesAuthoritis() {
        ArrayList<String> creates = new ArrayList<String>();
        creates.add(authorityNames[Constant.createFile]);
        creates.add(authorityNames[Constant.createSubDirectory]);
       

        return creates;
    }
    public ArrayList<String> getExecutesAuthoritis() {
        ArrayList<String> executes = new ArrayList<String>();
        executes.add(authorityNames[Constant.execute]);
       

        return executes;
    }
    public ArrayList<String> getDeletesAuthoritis() {
        ArrayList<String> deletes = new ArrayList<String>();
        deletes.add(authorityNames[Constant.deleteFile]);
        deletes.add(authorityNames[Constant.deleteChild]);
       

        return deletes;
    }
    public ArrayList<String> getInheritsAuthoritis() {
        ArrayList<String> inherits = new ArrayList<String>();
        inherits.add(authorityNames[Constant.fileInherit]);
        inherits.add(authorityNames[Constant.dirInherit]);
       

        return inherits;
    }
    
    
}
