/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class AuthorityNames implements Serializable{

//    public static String read = AccessAuthority.getAuthor(AccessAuthority.read);
//    public static String write = AccessAuthority.getAuthor(AccessAuthority.write);
//    public static String readFile = AccessAuthority.getAuthor(AccessAuthority.readFile);
//    public static String listContent = AccessAuthority.getAuthor(AccessAuthority.listContent);
//    public static String readBasicAttribute = AccessAuthority.getAuthor(AccessAuthority.readBasicAttribute);
//    public static String readExpandAttribute = AccessAuthority.getAuthor(AccessAuthority.readExpandAttribute);
//    public static String readACL = AccessAuthority.getAuthor(AccessAuthority.readACL);
//    public static String writeFile = AccessAuthority.getAuthor(AccessAuthority.writeFile);
//    public static String writeFileOwner = AccessAuthority.getAuthor(AccessAuthority.writeFileOwner);
//    public static String writeFileBasicAttribute = AccessAuthority.getAuthor(AccessAuthority.writeFileBasicAttribute);
//    public static String writeFileExpandAttribute = AccessAuthority.getAuthor(AccessAuthority.writeFileExpandAttribute);
//    public static String writeACl = AccessAuthority.getAuthor(AccessAuthority.writeACl);
//    public static String create = AccessAuthority.getAuthor(AccessAuthority.create);
//    public static String createSubDirectory = AccessAuthority.getAuthor(AccessAuthority.createSubDirectory);
//    public static String createFile = AccessAuthority.getAuthor(AccessAuthority.createFile);
//    public static String execute = AccessAuthority.getAuthor(AccessAuthority.execute);
//    public static String executeFile = AccessAuthority.getAuthor(AccessAuthority.executeFile);
//    public static String delete = AccessAuthority.getAuthor(AccessAuthority.delete);
//    public static String deleteFile = AccessAuthority.getAuthor(AccessAuthority.deleteFile);
//    public static String deleteChild = AccessAuthority.getAuthor(AccessAuthority.deleteChild);
//    public static String fileInherit = AccessAuthority.getAuthor(AccessAuthority.fileInherit);
//    public static String dirInherit = AccessAuthority.getAuthor(AccessAuthority.dirInherit);
//    public static String inherit = AccessAuthority.getAuthor(AccessAuthority.inherit);
//    public static String writeIn = AccessAuthority.getAuthor(AccessAuthority.writeIn);
//    public static String positiveControl = AccessAuthority.getAuthor(AccessAuthority.positiveControl);
//    public static String update = AccessAuthority.getAuthor(AccessAuthority.update);
//    public static String special = AccessAuthority.getAuthor(AccessAuthority.special);
//    public static String readAndExecute = AccessAuthority.getAuthor(AccessAuthority.readAndExecute);
//    public static String listFolderAndContent = AccessAuthority.getAuthor(AccessAuthority.listFolderAndContent);
//
//    public static String fetch = AccessAuthority.getAuthor(AccessAuthority.fetch);
    
    public static final String positiveControl = "positiveControl";
    public static final String update = "update";
    public static final String readAndExecute = "readAndExecute";
    public static final String listFolderAndContent = "listFolderAndContent";
    public static final String read = "read";
    public static final String write = "write";
    public static final String special = "special";
    public static final String readFile = "readFile";
    public static final String listContent = "listContent";
    public static final String readBasicAttribute = "readBasicAttribute";
    public static final String readExpandAttribute = "readExpandAttribute";
    public static final String readACL = "readACL";
    public static final String writeFile = "writeFile";
    public static final String writeFileOwner = "writeFileOwner";
    public static final String writeFileBasicAttribute = "writeFileBasicAttribute";
    public static final String writeFileExpandAttribute = "writeFileExpandAttribute";
    public static final String writeACl = "writeACl";
    public static final String createFile = "createFile";
    public static final String createSubDirectory = "createSubDirectory";
    public static final String execute = "execute";
    public static final String deleteFile = "deleteFile";
    public static final String deleteChild = "deleteChild";
    public static final String fileInherit = "fileInherit";
    public static final String dirInherit = "dirInherit";
    public static final String delete = "delete";
    public static final String executeFile = "executeFile";
    public static final String inherit = "inherit";
    public static final String fetch = "fetch";
    public static final String writeIn = "writeIn";
    public static final String create = "create";
    public static final String sys = "sys";
    public static final String user = "user";
    public static final String group = "group";

//    public static String getWriteIn() {
//        return writeIn;
//    }
//
//    public static void setWriteIn(String writeIn) {
//        AuthorityNames.writeIn = writeIn;
//    }
//
//    public String getCreate() {
//        return create;
//    }
//
//    public void setCreate(String create) {
//        this.create = create;
//    }
//
//    public String getCreateFile() {
//        return createFile;
//    }
//
//    public void setCreateFile(String createFile) {
//        this.createFile = createFile;
//    }
//
//    public String getCreateSubDirectory() {
//        return createSubDirectory;
//    }
//
//    public void setCreateSubDirectory(String createSubDirectory) {
//        this.createSubDirectory = createSubDirectory;
//    }
//
//    public String getDelete() {
//        return delete;
//    }
//
//    public void setDelete(String delete) {
//        this.delete = delete;
//    }
//
//    public String getDeleteChild() {
//        return deleteChild;
//    }
//
//    public void setDeleteChild(String deleteChild) {
//        this.deleteChild = deleteChild;
//    }
//
//    public String getDeleteFile() {
//        return deleteFile;
//    }
//
//    public void setDeleteFile(String deleteFile) {
//        this.deleteFile = deleteFile;
//    }
//
//    public String getDirInherit() {
//        return dirInherit;
//    }
//
//    public void setDirInherit(String dirInherit) {
//        this.dirInherit = dirInherit;
//    }
//
//    public String getExecute() {
//        return execute;
//    }
//
//    public void setExecute(String execute) {
//        this.execute = execute;
//    }
//
//    public String getExecuteFile() {
//        return executeFile;
//    }
//
//    public void setExecuteFile(String executeFile) {
//        this.executeFile = executeFile;
//    }
//
//    public String getFileInherit() {
//        return fileInherit;
//    }
//
//    public void setFileInherit(String fileInherit) {
//        this.fileInherit = fileInherit;
//    }
//
//    public String getInherit() {
//        return inherit;
//    }
//
//    public void setInherit(String inherit) {
//        this.inherit = inherit;
//    }
//
//    public String getListContent() {
//        return listContent;
//    }
//
//    public void setListContent(String listContent) {
//        this.listContent = listContent;
//    }
//
//    public String getRead() {
//        return read;
//    }
//
//    public void setRead(String read) {
//        this.read = read;
//    }
//
//    public String getReadACL() {
//        return readACL;
//    }
//
//    public void setReadACL(String readACL) {
//        this.readACL = readACL;
//    }
//
//    public String getReadBasicAttribute() {
//        return readBasicAttribute;
//    }
//
//    public void setReadBasicAttribute(String readBasicAttribute) {
//        this.readBasicAttribute = readBasicAttribute;
//    }
//
//    public String getReadExpandAttribute() {
//        return readExpandAttribute;
//    }
//
//    public void setReadExpandAttribute(String readExpandAttribute) {
//        this.readExpandAttribute = readExpandAttribute;
//    }
//
//    public String getReadFile() {
//        return readFile;
//    }
//
//    public void setReadFile(String readFile) {
//        this.readFile = readFile;
//    }
//
//    public String getWrite() {
//        return write;
//    }
//
//    public void setWrite(String write) {
//        this.write = write;
//    }
//
//    public String getWriteACl() {
//        return writeACl;
//    }
//
//    public void setWriteACl(String writeACl) {
//        this.writeACl = writeACl;
//    }
//
//    public String getWriteFile() {
//        return writeFile;
//    }
//
//    public void setWriteFile(String writeFile) {
//        this.writeFile = writeFile;
//    }
//
//    public String getWriteFileBasicAttribute() {
//        return writeFileBasicAttribute;
//    }
//
//    public void setWriteFileBasicAttribute(String writeFileBasicAttribute) {
//        this.writeFileBasicAttribute = writeFileBasicAttribute;
//    }
//
//    public String getWriteFileExpandAttribute() {
//        return writeFileExpandAttribute;
//    }
//
//    public void setWriteFileExpandAttribute(String writeFileExpandAttribute) {
//        this.writeFileExpandAttribute = writeFileExpandAttribute;
//    }
//
//    public String getWriteFileOwner() {
//        return writeFileOwner;
//    }
//
//    public void setWriteFileOwner(String writeFileOwner) {
//        this.writeFileOwner = writeFileOwner;
//    }
}
