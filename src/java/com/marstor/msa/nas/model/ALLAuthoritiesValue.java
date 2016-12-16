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
public class ALLAuthoritiesValue implements Serializable{

    public boolean read;
    public boolean write;
    // public static final int special = 7;
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
    private String style;
    private String name;
    private String result;

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isCreateFile() {
        return createFile;
    }

    public void setCreateFile(boolean createFile) {
        this.createFile = createFile;
    }

    public boolean isCreateSubDirectory() {
        return createSubDirectory;
    }

    public void setCreateSubDirectory(boolean createSubDirectory) {
        this.createSubDirectory = createSubDirectory;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isDeleteChild() {
        return deleteChild;
    }

    public void setDeleteChild(boolean deleteChild) {
        this.deleteChild = deleteChild;
    }

    public boolean isDeleteFile() {
        return deleteFile;
    }

    public void setDeleteFile(boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

    public boolean isDirInherit() {
        return dirInherit;
    }

    public void setDirInherit(boolean dirInherit) {
        this.dirInherit = dirInherit;
    }

    public boolean isExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public boolean isExecuteFile() {
        return executeFile;
    }

    public void setExecuteFile(boolean executeFile) {
        this.executeFile = executeFile;
    }

    public boolean isFileInherit() {
        return fileInherit;
    }

    public void setFileInherit(boolean fileInherit) {
        this.fileInherit = fileInherit;
    }

    public boolean isInherit() {
        return inherit;
    }

    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }

    public boolean isListContent() {
        return listContent;
    }

    public void setListContent(boolean listContent) {
        this.listContent = listContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isReadACL() {
        return readACL;
    }

    public void setReadACL(boolean readACL) {
        this.readACL = readACL;
    }

    public boolean isReadBasicAttribute() {
        return readBasicAttribute;
    }

    public void setReadBasicAttribute(boolean readBasicAttribute) {
        this.readBasicAttribute = readBasicAttribute;
    }

    public boolean isReadExpandAttribute() {
        return readExpandAttribute;
    }

    public void setReadExpandAttribute(boolean readExpandAttribute) {
        this.readExpandAttribute = readExpandAttribute;
    }

    public boolean isReadFile() {
        return readFile;
    }

    public void setReadFile(boolean readFile) {
        this.readFile = readFile;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isWriteACl() {
        return writeACl;
    }

    public void setWriteACl(boolean writeACl) {
        this.writeACl = writeACl;
    }

    public boolean isWriteFile() {
        return writeFile;
    }

    public void setWriteFile(boolean writeFile) {
        this.writeFile = writeFile;
    }

    public boolean isWriteFileBasicAttribute() {
        return writeFileBasicAttribute;
    }

    public void setWriteFileBasicAttribute(boolean writeFileBasicAttribute) {
        this.writeFileBasicAttribute = writeFileBasicAttribute;
    }

    public boolean isWriteFileExpandAttribute() {
        return writeFileExpandAttribute;
    }

    public void setWriteFileExpandAttribute(boolean writeFileExpandAttribute) {
        this.writeFileExpandAttribute = writeFileExpandAttribute;
    }

    public boolean isWriteFileOwner() {
        return writeFileOwner;
    }

    public void setWriteFileOwner(boolean writeFileOwner) {
        this.writeFileOwner = writeFileOwner;
    }

    public void changeRead() {
        if (this.read) {
            this.readACL = true;
            this.readBasicAttribute = true;
            this.readExpandAttribute = true;
            this.readFile = true;
            this.listContent = true;
        }
        if (!this.read) {
            this.readACL = false;
            this.readBasicAttribute = false;
            this.readExpandAttribute = false;
            this.readFile = false;
            this.listContent = false;
        }

    }

    public void changeReadAuthor() {
        if (this.readACL || this.readBasicAttribute || this.readExpandAttribute || this.readFile || this.listContent) {
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
        if (!this.write) {
            this.writeACl = false;
            this.writeFile = false;
            this.writeFileBasicAttribute = false;
            this.writeFileExpandAttribute = false;
            this.writeFileOwner = false;
        }
    }

    public void changeWriteAuthor() {
        if (this.writeACl || this.writeFile || this.writeFileBasicAttribute || this.writeFileExpandAttribute || this.writeFileOwner) {
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
        if (!this.create) {
            this.createFile = false;
            this.createSubDirectory = false;
        }
    }

    public void changeCreateAuthor() {
        if (this.createFile || this.createSubDirectory) {
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
        if (!this.delete) {
            this.deleteChild = false;
            this.deleteFile = false;
        }

    }

    public void changeDeleteAuthor() {
        if (this.deleteChild || this.deleteFile) {
            this.delete = true;
        } else {
            this.delete = false;
        }
    }

    public void changeExecute() {
        if (this.execute) {
            this.executeFile = true;
        }
        if (!this.execute) {
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
        if (!this.inherit) {
            this.fileInherit = false;
            this.dirInherit = false;
        }
    }

    public void changeInheritAuthor() {
        if (this.fileInherit || this.dirInherit ){
            this.inherit = true;
        }else {
            this.inherit = false;
        }

        
    }
    public boolean isConfigAuthors() {
        if(this.read || this.write || this.create || this.delete || this.execute || this.inherit) {
            return  true;
        }
        return  false;
    }
    
}
