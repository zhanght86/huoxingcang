/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import com.marstor.msa.nas.managedbean.EditUserGroup;
/**
 *
 * @author Administrator
 */
public class MySession {
    public static UserGroupData getGroupsFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserGroupData gs = (UserGroupData) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{userGroupData}", UserGroupData.class).getValue(context.getELContext());
        return  gs;
    }
    public static ShareListBean  getShareFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
        return  share;
    }
    public static EditUserGroup getEditGroups() {
        FacesContext context = FacesContext.getCurrentInstance();
        EditUserGroup gs = (EditUserGroup) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{editUserGroup}", EditUserGroup.class).getValue(context.getELContext());
        return  gs;
    }
    public static NFSDataTable getNFSData() {
        FacesContext context = FacesContext.getCurrentInstance();
        NFSDataTable nfs = (NFSDataTable) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{nFSDataTable}", NFSDataTable.class).getValue(context.getELContext());
        return  nfs;
    }
    public static DomainBean getDomainData() {
        FacesContext context = FacesContext.getCurrentInstance();
        DomainBean data = (DomainBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{domainData}", DomainBean.class).getValue(context.getELContext());
        return  data;
    }
    public static SharePathTable getShareData() {
        FacesContext context = FacesContext.getCurrentInstance();
        SharePathTable data = (SharePathTable) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharePathTable}", SharePathTable.class).getValue(context.getELContext());
        return  data;
    }
}
