/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;
import com.marstor.msa.scsi.model.InitiatoriSCSITargetsBean;
import com.marstor.msa.scsi.model.InitiatorHostBean;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
/**
 *
 * @author Administrator
 */
public class SCSISession {
    public static FCTargetData getFCTargetDataFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        FCTargetData fc = (FCTargetData) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{FCTargetData}", FCTargetData.class).getValue(context.getELContext());
        return  fc;
    }
    public static ISCSITargetData getISCSITargetDataFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        ISCSITargetData data = (ISCSITargetData) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{iSCSITargetData}", ISCSITargetData.class).getValue(context.getELContext());
        return  data;
    }
    public static RemoteInitiatorData getRemoteInitiatorDataFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        RemoteInitiatorData data = (RemoteInitiatorData) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{remoteInitiatorData}", RemoteInitiatorData.class).getValue(context.getELContext());
        return  data;
    }
//    public static InitiatorGroupData getInitiatorGroupDataFromSession() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        InitiatorGroupData data = (InitiatorGroupData) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{initiatorGroupData}", InitiatorGroupData.class).getValue(context.getELContext());
//        return  data;
//    }
    public static iSCSIGlobalInitiatorData getiSCSIGlobalInitiatorDataFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        iSCSIGlobalInitiatorData data = (iSCSIGlobalInitiatorData) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{iSCSIGlobalInitiatorData}", iSCSIGlobalInitiatorData.class).getValue(context.getELContext());
        return  data;
    }
    //InitiatorHostBean

    public static InitiatorHostBean getInitiatorHostBeanFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        InitiatorHostBean data = (InitiatorHostBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{initiatorHostBean}", InitiatorHostBean.class).getValue(context.getELContext());
        return  data;
    }
   // InitiatoriSCSITargetsBean
    public static InitiatoriSCSITargetsBean getInitiatoriSCSITargetsBeanFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        InitiatoriSCSITargetsBean data = (InitiatoriSCSITargetsBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{initiatoriSCSITargetsBean}", InitiatoriSCSITargetsBean.class).getValue(context.getELContext());
        return  data;
    }
}
