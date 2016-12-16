/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.NTPStatistic;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "system_time_NTPListBean")
@ViewScoped
public class System_time_NTPListBean implements Serializable {
    public boolean isStart = false;
    private String addNTP;
    public String selectNTP;
    public boolean notuse;
    public boolean notdelete;
    /**
     * Creates a new instance of system_time_NTPListBean
     */
//    private List<Property> ntpList;
//    private Property ntp;
    private ArrayList<String> ntpList;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_time";
    /**
     * Creates a new instance of Set_timeZoneBean
     */
    public System_time_NTPListBean() {
        
        this.getNTPs();
        changeBooleanCheckbox(isStart);
    }

    public void getNTPs() {
        ntpList = new ArrayList();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        NTPStatistic ntp = common.getNTP();
        if (ntp != null) {
            isStart = ntp.bEnable;
             SystemOutPrintln.print_common("ntp isStart="+isStart);
            if(ntp.servers != null){
               ntpList = ntp.servers;
            }

        }
//        if(isStart = true){
//            notuse = false;
//        }else{
//            notuse = true;
//        }


//        ntpList.add("pool.ntp.org");
//        ntpList.add("target.ntp.org");
//        ntpList.add("test.ntp.org");

    }

    public ArrayList<String> getNtpList() {
        return ntpList;
    }

    public void setNtpList(ArrayList<String> ntpList) {
        this.ntpList = ntpList;
    }

    public String getAddNTP() {
        return addNTP;
    }

    public void setAddNTP(String addNTP) {
        this.addNTP = addNTP;
    }

    public boolean isNotdelete() {
        return notdelete;
    }

    public void setNotdelete(boolean notdelete) {
        this.notdelete = notdelete;
    }
    
    public void testNTPServer(String nameStr) {
        // String strNEmail = Constants.showInput(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/TimeManagerDialog").getString("输入一个NTP服务器地址："), "");
        if (nameStr == null || nameStr.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ntpone"),  ""));
            return;
        }
        if (nameStr.trim().equals("") || (nameStr.trim().length() > 64) || (!(ValidateUtility.checkDomainName(nameStr) || ValidateUtility.checkIP(nameStr)))) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ntpno"),  ""));
            return;
        }
         CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean isNTP = common.testNTP(nameStr);
        if (isNTP == true) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "testok"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "testfail"), ""));
        }
        

    }

    public void addNTPServer(String nameStr) {
        // String strNEmail = Constants.showInput(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/TimeManagerDialog").getString("输入一个NTP服务器地址："), "");
        if (nameStr == null || nameStr.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ntpnull"),  ""));
            return;
        }
        if (nameStr.trim().equals("") || (nameStr.trim().length() > 64) || (!(ValidateUtility.checkDomainName(nameStr) || ValidateUtility.checkIP(nameStr)))) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ntpno"),  ""));
            return;
        }
        int count = ntpList.size();
        for (int i = 0; i < count; i++) {
            if (ntpList.get(i).trim().equals(nameStr.trim())) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ntpexit"),  ""));
                return;
            }
        }

         SystemOutPrintln.print_common("addNTP=" + nameStr);
        if (nameStr != null) {
//            ntp = new Property();
//            ntp.setNumber(1);
//            ntp.setName(nameStr);
            ArrayList<String> list = ntpList;
            ntpList = new ArrayList();
            ntpList.add(nameStr);
            if(list != null){
                for(int j=0; j<list.size(); j++){
                    ntpList.add(list.get(j));
                }
            }
            addNTP = "";
        }
         SystemOutPrintln.print_common("size==" + ntpList.size());
        setDeleteNTPServerButtonEnable();

    }

    public void deleteNTPServer(String nameStr) {
        
 
        if (nameStr == null || nameStr.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ntpselect"), ""));
            return;
        }

         SystemOutPrintln.print_common("addNTP5=" + nameStr);
        if (ntpList != null) {
            ntpList.remove(nameStr);
        }
         SystemOutPrintln.print_common("size=" + ntpList.size());
        setDeleteNTPServerButtonEnable();

    }
    
    private void setDeleteNTPServerButtonEnable() {
        int i2 =ntpList.size();

        if (i2 < 1) {
            notdelete = true;
        } else {
            notdelete = false;
        }
    }
    
    public void saveNTPServer() {
        NTPStatistic ntp = new NTPStatistic();
         SystemOutPrintln.print_common("save isStart=" + isStart);
        if (isStart) {
            if (ntpList != null && ntpList.size() > 0) {
                ntp.setbEnable(isStart);
                ntp.setServers(ntpList);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "minonentp"), ""));
                return;
            }
        } else {
            ntp.setbEnable(isStart);
            if (ntpList != null && ntpList.size() > 0) {
                ntp.setServers(ntpList);
            } else {
                ntpList = new ArrayList();
                ntp.setServers(ntpList);
            }
        }
       
       
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean setNTP = common.setNTP(ntp);
        if (setNTP == true) {
            this.getNTPs();
            changeBooleanCheckbox(isStart);
            addNTP = "";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "setntp_ok"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "setntp_fail"), ""));
        }

//         for(int i= 0;i<ntps.size();i++){
//               System.out.println("#############123"); 
//            System.out.println("#############"+ ntps.get(i)); 
//         }
//     
//        System.out.println("长度123=" + ntps.size());

    }

    public String getSelectNTP() {
        return selectNTP;
    }

    public void setSelectNTP(String selectNTP) {
        this.selectNTP = selectNTP;
    }

    public boolean isIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isNotuse() {
        return notuse;
    }

    public void setNotuse(boolean notuse) {
        this.notuse = notuse;
    }
//    <p:ajax update="ip subnet" listener="#{createAggregateBean.changeBooleanCheckbox()}" />
     public void changeBooleanCheckbox( boolean isSt){
         if(isSt == true){
            notuse = false;
            notdelete = false;
            setDeleteNTPServerButtonEnable();
        }else{
            notuse = true;
             notdelete = true;
        }
     }       
            
     
     
}
