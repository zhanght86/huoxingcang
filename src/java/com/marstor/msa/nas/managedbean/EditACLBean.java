/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.nas.model.ACL;
import com.marstor.msa.nas.model.ShareListBean;
import com.marstor.msa.nas.model.Share;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@javax.faces.bean.ManagedBean(name = "editacl")
@javax.faces.bean.RequestScoped
public class EditACLBean implements ValueChangeListener {

    private ArrayList<ACL> acls = new ArrayList<ACL>();
    private ACL selectedACL;
    private String userName;

    public EditACLBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
       // acls = share.getAclsByPath(share.getPath());

//        if (share.getSelectedACL() == null) {
        if (acls.size() > 0) {
            selectedACL = acls.get(0);
            // share.setSelectedACL(selectedACL);
        }
        if (share.getSelectedACL() == null && acls.size() > 0) {
            share.setSelectedACL(acls.get(0));
        }
        // share.setSelectedACL(selectedACL);
//        } 
//        else {
//            selectedACL = share.getSelectedACL();
//        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<ACL> getAcls() {
        return acls;
    }

    public void setAcls(ArrayList<ACL> acls) {
        this.acls = acls;
    }

    public ACL getSelectedACL() {
        return selectedACL;
    }

    public void setSelectedACL(ACL selectedACL) {
        this.selectedACL = selectedACL;
    }

    public void changeUser(ValueChangeEvent event) {
        userName = this.selectedACL.getObjectName();
    }

    @Override
    public void processValueChange(ValueChangeEvent vce) throws AbortProcessingException {
        userName = this.selectedACL.getObjectName();
    }

    public void onRowSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
        ACL acl = (ACL) event.getObject();
        share.setSelectedACL(acl);
    }

//    public void deleteACL() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
//        ACL acl = (ACL) share.getSelectedACL();
//        Share path = share.getShareByPath(share.getPath());
//        path.deleteACL(acl);
//        if (acls.size() > 0) {
//            selectedACL = acls.get(0);
//        }
//    }
}
