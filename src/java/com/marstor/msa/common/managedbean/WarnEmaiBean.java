/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.parameter.EmailParameter;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "warnEmaiBean")
@ViewScoped
public class WarnEmaiBean implements Serializable {

    public boolean isStart;
    public String sendEmail;
    public String receiveEmail;
    public List<String> receiveEmails;
    public String selectReceiveEmail;
    public String smtp;
    public String port = "25";
    public boolean isEnable;
    public static String account="";
    public static String password="";
    public boolean notStartUse;
    public boolean notEnableUse;
    public boolean notdelete;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_warnemail";
    public boolean flag = true;

    /**
     * Creates a new instance of WarnEmaiBean
     */
    public WarnEmaiBean() {
        isStart = false;

        addReceiveEmails();
//        if (flag) {
//            this.account = "admin";
//            this.password = "admin";
//            flag = false;
//        }

    }

    public void addReceiveEmails() {
        receiveEmails = new ArrayList();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        EmailParameter email = common.getEmailParameter();
        SystemOutPrintln.print_common("set email=" + (email == null));
        if (email != null) {
            isStart = email.enableEmail;
            SystemOutPrintln.print_common("email isStart=" + isStart);

//            changeBooleanCheckbox_isStart();
            sendEmail = email.fromAddress;
            port = email.stmpPort;
            if (port == null || port.equals("")) {
                port = "25";
            }
            smtp = email.stmp;
            account = email.userName;
            password = email.password;
            SystemOutPrintln.print_common("set  email password=" + password);
            isEnable = email.bNeedAuthorize;

            if (isStart == true) {
                notStartUse = false;
                changeBooleanCheckbox_isEnable();
                notdelete = false;
                setDeleteReceiveAddressButtonEnable();
            } else {
                notStartUse = true;
                notEnableUse = true;
                notdelete = true;
            }

            for (int i = 0; i < email.toAddress.length; i++) {
                receiveEmails.add(email.toAddress[i]);
            }

        }
        setDeleteReceiveAddressButtonEnable();


//        receiveEmails.add("liming@126.com");
//        receiveEmails.add("zhangsan@163.com");
    }

    public boolean isIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public List<String> getReceiveEmails() {
        return receiveEmails;
    }

    public void setReceiveEmails(List<String> receiveEmails) {
        this.receiveEmails = receiveEmails;
    }

    public String getSelectReceiveEmail() {
        return selectReceiveEmail;
    }

    public void setSelectReceiveEmail(String selectReceiveEmail) {
        this.selectReceiveEmail = selectReceiveEmail;
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNotStartUse() {
        return notStartUse;
    }

    public void setNotStartUse(boolean notStartUse) {
        this.notStartUse = notStartUse;
    }

    public boolean isNotEnableUse() {
        return notEnableUse;
    }

    public void setNotEnableUse(boolean notEnableUse) {
        this.notEnableUse = notEnableUse;
    }

    public boolean isNotdelete() {
        return notdelete;
    }

    public void setNotdelete(boolean notdelete) {
        this.notdelete = notdelete;
    }

    public void changeBooleanCheckbox_isStart() {
        if (isStart == true) {
            notStartUse = false;
            isEnable = false;
            changeBooleanCheckbox_isEnable();
            notdelete = false;
            setDeleteReceiveAddressButtonEnable();
        } else {
            notStartUse = true;
            notEnableUse = true;
            notdelete = true;
        }
    }

    public void changeBooleanCheckbox_isEnable() {
        if (isEnable == true) {
            notEnableUse = false;
        } else {
            notEnableUse = true;
        }
        SystemOutPrintln.print_common("account: " + this.account);
    }

    public boolean validateMailTxt(String strMail) {;
        return ValidateUtility.checkE_Mail(strMail);
    }

    public void addReceiveEmail(String nameStr) {

//        String strNEmail = Constants.showInput(
//                java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/ConfigMailDialog").getString("输入一个接收邮件地址")
//                , java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/ConfigMailDialog").getString("添加")
//                , JOptionPane.QUESTION_MESSAGE);
        if (nameStr == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "receive_one"), ""));
            return;
        }
        if (nameStr.trim().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "receive_no"), ""));
            return;
        }
        if (nameStr.trim().length() > 64) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "receive_no"), ""));
            return;
        }
        if (!this.validateMailTxt(nameStr)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "receive_no"), ""));
            return;
        }
        int itemCount = receiveEmails.size();
        for (int i = 0; i < itemCount; i++) {
            if (receiveEmails.get(i).trim().equals(nameStr.toString())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "receive_exit"), ""));
                return;
            }
        }
        if (nameStr != null) {
//            ntp = new Property();
//            ntp.setNumber(1);
//            ntp.setName(nameStr);
            receiveEmails.add(nameStr);
            receiveEmail = "";
        }
        SystemOutPrintln.print_common("size=" + receiveEmails.size());
        setDeleteReceiveAddressButtonEnable();
    }

    public void deleteReceiveEmail(String nameStr) {

        if (nameStr == null || nameStr.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "receive_select"), ""));
            return;
        }

        SystemOutPrintln.print_common("addNTP5=" + nameStr);
        if (receiveEmails != null) {
            receiveEmails.remove(nameStr);
        }
        SystemOutPrintln.print_common("size=" + receiveEmails.size());
        setDeleteReceiveAddressButtonEnable();
    }

    private void setDeleteReceiveAddressButtonEnable() {
        int i2 = receiveEmails.size();

        if (i2 < 1) {
            notdelete = true;
        } else {
            notdelete = false;
        }
    }

    public void testEmail() {
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        if (!getText()) {
            return;
        }
        EmailParameter email = new EmailParameter();
        email.enableEmail = this.isStart;
        email.fromAddress = this.sendEmail;
        email.bNeedAuthorize = this.isEnable;
        email.stmp = this.smtp;
        email.stmpPort = this.port;
        email.userName = this.account;
        if (this.password != null && !this.password.equals("")) {
            char[] c = this.password.toCharArray();
            char[] b = MyEncryp.Encode64(c);
            email.password = new String(b);
            SystemOutPrintln.print_common("email email.password=" + email.password);
        }
        String receive[] = new String[this.receiveEmails.size()];
        if (this.receiveEmails != null) {
            for (int i = 0; i < this.receiveEmails.size(); i++) {
                receive[i] = this.receiveEmails.get(i);
            }
        }

        email.toAddress = receive;
        for (int k = 0; k < email.toAddress.length; k++) {
            SystemOutPrintln.print_common("test email email.enableEmail=" + email.toAddress[k]);
        }
        boolean send = common.sendTestEmail(email, res.get(basename, "email"), res.get(basename, "test"));
        SystemOutPrintln.print_common("test email email.enableEmail=" + email.enableEmail + "email.fromAddress=" + email.fromAddress);

        if (send == true) {
            SystemOutPrintln.print_common("test email succeed");
//            this.addReceiveEmails();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "test_ok"), ""));
        } else {
            SystemOutPrintln.print_common("test email false");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "test_fail"), ""));
        }
    }

    public void save() {
        if (!getText()) {
            return;
        }
        EmailParameter email = new EmailParameter();
        email.enableEmail = this.isStart;
        email.fromAddress = this.sendEmail;
        email.bNeedAuthorize = this.isEnable;
        email.stmp = this.smtp;
        email.stmpPort = this.port;
        email.userName = this.account;
        if (this.password != null && !this.password.equals("")) {
            char[] c = this.password.toCharArray();
            char[] b = MyEncryp.Encode64(c);
            email.password = new String(b);
            SystemOutPrintln.print_common("email.password=" + email.password);
        }

        String receive[] = new String[this.receiveEmails.size()];
        if (this.receiveEmails != null) {
            for (int i = 0; i < this.receiveEmails.size(); i++) {
                receive[i] = this.receiveEmails.get(i);
            }
        }

        email.toAddress = receive;

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean send = common.setEmailParameter(email);
//        send = false;
        if (send == true) {
            SystemOutPrintln.print_common("send email succeed");
            this.addReceiveEmails();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "set_ok"), ""));
        } else {
            SystemOutPrintln.print_common("send email false");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "set_fail"), ""));
        }

//        System.out.println("isStart=" + isStart);
//         System.out.println("sendEmail=" + sendEmail);
//          System.out.println("receiveEmails=" + receiveEmails.size());

    }

    private boolean getText() {
        if (sendEmail.trim().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sendemail_no"), ""));
            return false;
        }
        if (!this.validateMailTxt(sendEmail.trim())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sendemail_no"), ""));
            return false;
        }
        if (smtp.trim().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "smtp_no"), ""));
            return false;
        }
        try {
            if (Integer.parseInt(port.trim()) < 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "port_no"), ""));
                return false;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "port_no"), ""));
            return false;
        }
        return true;
    }
}
