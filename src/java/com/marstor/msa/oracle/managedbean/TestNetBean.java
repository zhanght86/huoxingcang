/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.bean.NetServiceName;
import com.marstor.msa.oracle.bean.NetServiceNameNotSaved;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.util.encrypt.MyEncryp;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "testNetBean")
@ViewScoped
public class TestNetBean implements Serializable {

    private String name = null;
    private String user = "";
    private String pass = "";
    private NetServiceNameNotSaved notSave = null;
    private MSAResource res = new MSAResource();

    public TestNetBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        name = request.getParameter("name");

        if (null == name) {
            FacesContext context = FacesContext.getCurrentInstance();  //取session域
            CreateNSNameBean bean = (CreateNSNameBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{createNSNameBean}", CreateNSNameBean.class).getValue(context.getELContext());
            this.notSave = bean.getNotSave();

        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public String testResult() {

        System.out.println("user is " + user + "pass is " + pass);

        if (user.equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UserNameError"), ""));
            return null;
        }

        if (pass.equals("")) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("PasswordError"), ""));
            return null;
        }

        if (null == notSave) {
            NetServiceName singleNSName = InterfaceFactory.getOracleInterfaceInstance().getSingleNSName(name);
            notSave = new NetServiceNameNotSaved();
            notSave.netServiceName = singleNSName.netServiceName;
            notSave.dbServiceName = singleNSName.dbServiceName;
            notSave.ipMap = singleNSName.ipMap;
            notSave.user = this.user;
            notSave.password = String.valueOf(MyEncryp.Encode64(pass.toCharArray()));

            boolean testNSName = InterfaceFactory.getOracleInterfaceInstance().testNSName(notSave);
            if (testNSName) {
                FacesContext context = FacesContext.getCurrentInstance();  //取session域
                context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("TestContectSuccess"), ""));
                return null;
            } else {
                FacesContext context = FacesContext.getCurrentInstance();  //取session域     
                CreateNSNameBean bean = (CreateNSNameBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{createNSNameBean}", CreateNSNameBean.class).getValue(context.getELContext());
                bean.setBSave(false);
                context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("TesetContectFailed"), ""));
                return null;
            }

        } else {
            notSave.user = this.user;
            notSave.password = String.valueOf(MyEncryp.Encode64(pass.toCharArray()));

            boolean testNSName = InterfaceFactory.getOracleInterfaceInstance().testNSName(notSave);
            if (testNSName) {
                FacesContext context = FacesContext.getCurrentInstance();  //取session域
                CreateNSNameBean bean = (CreateNSNameBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{createNSNameBean}", CreateNSNameBean.class).getValue(context.getELContext());
                bean.setBSave(false);

                context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("TestContectSuccess"), ""));
                return null;
            } else {
                FacesContext context = FacesContext.getCurrentInstance();  //取session域      
                context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("TesetContectFailed"), ""));
                return null;
            }

        }
    }
}
