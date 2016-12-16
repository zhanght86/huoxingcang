/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class AuthoritiesUpdate {
    
    private ALLAuthoritiesValue  authorsForAdd ;
    private ALLAuthoritiesValue  authorsForEdit = new ALLAuthoritiesValue();

    public AuthoritiesUpdate() {
        authorsForAdd = new ALLAuthoritiesValue();
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
        //SharePathBean  path = share.getShareByPath(share.getPath());
        ACL acl = share.getSelectedACL();
       authorsForEdit.setReadFile(acl.getReadFileAuthor());
       authorsForEdit.setListContent(acl.getlistContentAuthor());
       authorsForEdit.setReadBasicAttribute(acl.getReadBasicAttributeAuthor());
       authorsForEdit.setReadExpandAttribute(acl.getreadExpandAttribute());
       authorsForEdit.setReadACL(acl.getReadACLAuthor());
       
       authorsForEdit.setWriteFile(acl.getWriteFileAuthor());
       authorsForEdit.setWriteFileBasicAttribute(acl.getWriteFileAuthor());
       authorsForEdit.setWriteFileOwner(acl.getWriteFileAuthor());
       authorsForEdit.setWriteFileExpandAttribute(acl.getWriteFileExpandAttribute());
       authorsForEdit.setWriteACl(acl.getWriteAClAuthor());
       
       authorsForEdit.setCreateFile(acl.getCreateFileAuthor());
       authorsForEdit.setCreateSubDirectory(acl.getCreateSubDirectoryAuthor());
       authorsForEdit.setExecuteFile(acl.getExcuteFileAuthor());
       authorsForEdit.setDeleteFile(acl.getDeleteFileAuthor());
       authorsForEdit.setDeleteChild(acl.getDeleteChildAuthor());
       authorsForEdit.setFileInherit(acl.getFileInheritAuthor());
       authorsForEdit.setDirInherit(acl.getDirInheritAuthor());

        if (acl.getReadACLAuthor()  && acl.getReadBasicAttributeAuthor() && acl.getreadExpandAttribute() && acl.getReadFileAuthor()) {
            authorsForEdit.setRead(true) ;
        }
        if (acl.getWriteAClAuthor() && acl.getWriteFileAuthor() && acl.getWriteFileBasicAttributeAuthor() && acl.getWriteFileExpandAttribute() && acl.getWriteFileOwnerAuthor()) {
            authorsForEdit.setWrite(true) ;
        }
        if (acl.getExcuteFileAuthor()) {
           // execute = true;
            authorsForEdit.setExecute(true);
        }
        if (acl.getFileInheritAuthor() && acl.getDirInheritAuthor()) {
            authorsForEdit.setInherit(true);
        }
        if (acl.getDeleteChildAuthor() && acl.getDeleteFileAuthor()) {
            authorsForEdit.setDelete(true);
        }
        if(acl.getCreateFileAuthor()&& acl.getCreateSubDirectoryAuthor()) {
            authorsForEdit.setCreate(true);
        }
    }

    public ALLAuthoritiesValue getAuthorsForAdd() {
        return authorsForAdd;
    }

    public void setAuthorsForAdd(ALLAuthoritiesValue authorsForAdd) {
        this.authorsForAdd = authorsForAdd;
    }

    public ALLAuthoritiesValue getAuthorsForEdit() {
        return authorsForEdit;
    }

    public void setAuthorsForEdit(ALLAuthoritiesValue authorsForEdit) {
        this.authorsForEdit = authorsForEdit;
    }
    public void updateAuthorities() {
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
        //SharePathBean  path = share.getShareByPath(share.getPath());
        ACL acl = share.getSelectedACL();
       authorsForEdit.setReadFile(acl.getReadFileAuthor());
       authorsForEdit.setListContent(acl.getlistContentAuthor());
       authorsForEdit.setReadBasicAttribute(acl.getReadBasicAttributeAuthor());
       authorsForEdit.setReadExpandAttribute(acl.getreadExpandAttribute());
       authorsForEdit.setReadACL(acl.getReadACLAuthor());
       
       authorsForEdit.setWriteFile(acl.getWriteFileAuthor());
       authorsForEdit.setWriteFileBasicAttribute(acl.getWriteFileAuthor());
       authorsForEdit.setWriteFileOwner(acl.getWriteFileAuthor());
       authorsForEdit.setWriteFileExpandAttribute(acl.getWriteFileExpandAttribute());
       authorsForEdit.setWriteACl(acl.getWriteAClAuthor());
       
       authorsForEdit.setCreateFile(acl.getCreateFileAuthor());
       authorsForEdit.setCreateSubDirectory(acl.getCreateSubDirectoryAuthor());
       authorsForEdit.setExecuteFile(acl.getExcuteFileAuthor());
       authorsForEdit.setDeleteFile(acl.getDeleteFileAuthor());
       authorsForEdit.setDeleteChild(acl.getDeleteChildAuthor());
       authorsForEdit.setFileInherit(acl.getFileInheritAuthor());
       authorsForEdit.setDirInherit(acl.getDirInheritAuthor());

        if (acl.getReadACLAuthor()  && acl.getReadBasicAttributeAuthor() && acl.getreadExpandAttribute() && acl.getReadFileAuthor()) {
            authorsForEdit.setRead(true) ;
        }
        if (acl.getWriteAClAuthor() && acl.getWriteFileAuthor() && acl.getWriteFileBasicAttributeAuthor() && acl.getWriteFileExpandAttribute() && acl.getWriteFileOwnerAuthor()) {
            authorsForEdit.setWrite(true) ;
        }
        if (acl.getExcuteFileAuthor()) {
           // execute = true;
            authorsForEdit.setExecute(true);
        }
        if (acl.getFileInheritAuthor() && acl.getDirInheritAuthor()) {
            authorsForEdit.setInherit(true);
        }
        if (acl.getDeleteChildAuthor() && acl.getDeleteFileAuthor()) {
            authorsForEdit.setDelete(true);
        }
        if(acl.getCreateFileAuthor()&& acl.getCreateSubDirectoryAuthor()) {
            authorsForEdit.setCreate(true);
        }
        
    }
    
    
}
