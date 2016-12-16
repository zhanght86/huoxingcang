/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.mdu.impl;

import com.marstor.msa.cdp.socket.XMLBaseParser;
import com.marstor.msa.mdu.bean.UserDir;
import com.marstor.msa.mdu.util.Debug;
import com.marstor.msa.mdu.web.MsaMDUInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.msa.lucene.Interface.FullTextIndexInterface;
import com.msa.lucene.Interface.FullTextIndexInterface.FileType;
import com.msa.lucene.Interface.FullTextIndexInterface.IndexType;
import com.msa.lucene.Interface.FullTextSearchInterface;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.databinding.utils.BeanUtil;

/**
 *
 * @author Administrator
 */
public class AxisMDUInterfaceImpl {

    private final MsaMDUInterface mdu = InterfaceFactory.getMsaMDUInterfaceInstance();
    private static String error;

    private void setError(String s) {
        error = s;
    }

    public String getError() {
        return error;
    }

    public String getShareName() {
        return mdu.getShareName();
    }

    public boolean addGroup(String name) {
        return mdu.addGroup(name);
    }

    public boolean addUser(String xml) {
        Debug.print("start addUser");
        XMLBaseParser parser = new XMLBaseParser(xml);
        String user = parser.getNodeContent("MDUAddUserParameter/user");
        int iGroups = parser.getNodeCount("MDUAddUserParameter/group");
        String[] groups = new String[iGroups];
        for (int i = 0; i < iGroups; i++) {
            String group = parser.getNodeContent("MDUAddUserParameter/group", i);
            if (group != null) {
                groups[i] = group;
            }
        }
        Debug.print(user);
        Debug.print(groups.toString());
        boolean result = mdu.addUser(user, groups);
        if (!result) {
            return false;
        }
        return true;
    }

    public String[] getGroups() {
        return mdu.getGroups();
    }

    public String[] getUsers() {
        return mdu.getUsers();
    }
    
    public boolean setUserPassword(String user, String pwd){
        return mdu.setUserPassword(user, pwd);
    }
    
    public boolean resetUserPassword(String user){
        return mdu.resetUserPassword(user);
    }

    public boolean addSharedDirectory(String xml) {
        XMLBaseParser parser = new XMLBaseParser(xml);
        String owner = parser.getNodeContent("MDUAddSharedDirParameter/owner");
        String directory = parser.getNodeContent("MDUAddSharedDirParameter/directory");

        int iUsers = parser.getNodeCount("MDUAddSharedDirParameter/user");
        String[] users = new String[iUsers];
        for (int i = 0; i < iUsers; i++) {
            String user = parser.getNodeContent("MDUAddSharedDirParameter/user", i);
            if (user != null) {
                users[i] = user;
            }
        }
        int sharedType = parser.getIntNodeContent("MDUAddSharedDirParameter/sharedType");

        Debug.print(owner);
        Debug.print(directory);
        Debug.print(users.toString());
        boolean result = mdu.addSharedDirectory(owner, directory, users, sharedType);
        if (!result) {
            return false;
        }
        return true;
    }

    public OMElement getUserDirectory(String user) {
        UserDir[] cdpDiskInfos = mdu.getUserDirectory(user);
        if (cdpDiskInfos == null) {
            return null;
        }
        com.marstor.msa.axis2.mdu.model.UserDir[] cdpDiskInfos_axis = new com.marstor.msa.axis2.mdu.model.UserDir[cdpDiskInfos.length];
        for (int i = 0; i < cdpDiskInfos.length; i++) {
            com.marstor.msa.axis2.mdu.model.UserDir cdpDiskInfo_axis = MduServerUtil.convertUserDirInfo(cdpDiskInfos[i]);
            cdpDiskInfos_axis[i] = cdpDiskInfo_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                cdpDiskInfos_axis, new QName("MduUserDirInfo"), false, null);
        return element;
    }

    public String[] getSharedUsers(String owner, String directory) {
        return mdu.getSharedUsers(owner, directory);
    }

    public boolean cancelSharedDirectory(String xml) {
        XMLBaseParser parser = new XMLBaseParser(xml);
        String owner = parser.getNodeContent("MDUCancelSharedDirParameter/owner");
        String directory = parser.getNodeContent("MDUCancelSharedDirParameter/directory");

        int iUsers = parser.getNodeCount("MDUCancelSharedDirParameter/user");
        boolean result;
        if (iUsers == 0) {
            Debug.print("cancel all");
            result = mdu.delSharedDirectory(owner, directory);
        } else {
            Debug.print("cancel some");
            String[] users = new String[iUsers];
            for (int i = 0; i < iUsers; i++) {
                String user = parser.getNodeContent("MDUCancelSharedDirParameter/user", i);
                if (user != null) {
                    users[i] = user;
                }
            }
            result = mdu.delSharedDirectory(owner, directory, users);
            Debug.print(users.toString());
        }
        Debug.print(owner);
        Debug.print(directory);
        if (!result) {
            return false;
        }
        return true;
    }
    
    public boolean setGroupAdministrator(String xml){
        XMLBaseParser parser = new XMLBaseParser(xml);
        int iUsers = parser.getNodeCount("MDUSetGroupAdminParameter/user");
        int iGroups = parser.getNodeCount("MDUSetGroupAdminParameter/group");
        
        if(iUsers == 0 || iGroups == 0){
            return false;
        }
        
        String[] users = new String[iUsers];
        String[] groups = new String[iGroups];
        
        for(int i = 0; i<iUsers; i++){
            users[i] = parser.getNodeContent("MDUSetGroupAdminParameter/user", i);
        }
        
        for(int i = 0; i<iGroups; i++){
            groups[i] = parser.getNodeContent("MDUSetGroupAdminParameter/group", i);
        }
        
        return mdu.setGroupAdministrator(users, groups);        
    }
    
    public boolean unsetGroupAdministrator(String xml){
        XMLBaseParser parser = new XMLBaseParser(xml);
        int iUsers = parser.getNodeCount("MDUUnSetGroupAdminParameter/user");
        int iGroups = parser.getNodeCount("MDUUnSetGroupAdminParameter/group");
        
        if(iUsers == 0 || iGroups == 0){
            return false;
        }
        
        String[] users = new String[iUsers];
        String[] groups = new String[iGroups];
        
        for(int i = 0; i<iUsers; i++){
            users[i] = parser.getNodeContent("MDUUnSetGroupAdminParameter/user", i);
        }
        
        for(int i = 0; i<iGroups; i++){
            groups[i] = parser.getNodeContent("MDUUnSetGroupAdminParameter/group", i);
        }
        
        return mdu.unsetGroupAdministrator(users, groups); 
    }
    
    public String[] getGroupAdministrator(String group){
        return mdu.getGroupAdministrator(group);
    }

    public void setIndexDirectory(String directory) {
        mdu.setIndexDirectory(directory);
    }

    public void setFields(String xml) {
        XMLBaseParser parser = new XMLBaseParser(xml);
        int iFields = parser.getNodeCount("MDUSetFieldsParameter/field");
        IndexType[] fields = new IndexType[iFields];
        for (int i = 0; i < iFields; i++) {
            String field = parser.getNodeContent("MDUSetFieldsParameter/field", i);
            if (field.equals("content")) {
                fields[i] = IndexType.content;
            } else if (field.equals("filename")) {
                fields[i] = IndexType.filename;
            } else if (field.equals("fullpath")) {
                fields[i] = IndexType.fullpath;
            } else if (field.equals("author")) {
                fields[i] = IndexType.author;
            } else if (field.equals("modifytime")) {
                fields[i] = IndexType.modifytime;
            }
        }
        mdu.setFields(fields);
    }

    public void setFileEndWith(String xml) {
        XMLBaseParser parser = new XMLBaseParser(xml);
        int iTypes = parser.getNodeCount("MDUSetFileEndWithParameter/type");
        FileType[] types = new FileType[iTypes];
        for (int i = 0; i < iTypes; i++) {
            String type = parser.getNodeContent("MDUSetFileEndWithParameter/type", i);
            if (type.equals("doc")) {
                types[i] = FileType.doc;
            } else if (type.equals("pdf")) {
                types[i] = FileType.pdf;
            } else if (type.equals("txt")) {
                types[i] = FileType.txt;
            } else if (type.equals("docx")) {
                types[i] = FileType.docx;
            }
        }
        mdu.setFileEndWith(types);
    }

    public boolean createIndex(String directory) {
        return mdu.createIndex(directory);
    }

    public boolean addFile2Index(String directory) {
        return mdu.addFile2Index(directory);
    }

    public boolean updateFile2Index(String directory) {
        return mdu.updateFile2Index(directory);
    }

    public boolean deleteAllIndex() {
        return mdu.deleteAllIndex();
    }

    public boolean delIndexOfFile(String directory) {
        return mdu.delIndexOfFile(directory);
    }

    public void setSearchDirectory(String directory) {
        mdu.setSearchDirectory(directory);
    }

    public String[] search(String field, String query) {
        IndexType it = IndexType.content;
        if (field.equals("content")) {
            it = IndexType.content;
        } else if (field.equals("filename")) {
            it = IndexType.filename;
        } else if (field.equals("fullpath")) {
            it = IndexType.fullpath;
        } else if (field.equals("author")) {
            it = IndexType.author;
        } else if (field.equals("modifytime")) {
            it = IndexType.modifytime;
        }
        
        List<String> result = mdu.search(it, query);
        return result.toArray(new String[result.size()]);
    }
}
