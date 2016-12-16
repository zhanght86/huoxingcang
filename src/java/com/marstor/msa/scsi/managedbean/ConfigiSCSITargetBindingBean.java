/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.scsi.model.IPBindingDataModel;
import com.marstor.msa.scsi.model.ISCSITarget;
import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.model.IPBinding;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ConfigiSCSITargetBindingBean {

    private boolean isStart;
    private ArrayList<IPBinding> allIPList = new ArrayList<IPBinding>();
    private IPBinding[] selectedIP;
    private IPBindingDataModel dataModel = new IPBindingDataModel(new ArrayList<IPBinding>());
    private ISCSITarget temp;
    private String targetName = "";
    private String radioValue = "";
    private boolean selectRender;
    private boolean tableRender;
    // private boolean tableEnabled = true;

    public ConfigiSCSITargetBindingBean() {
        //dataModel = new IPBindingDataModel();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.targetName = request.getParameter("targetName");
        String startOrNot = request.getParameter("startOrNot");
        String allIP = request.getParameter("allIP");
        String allIPArray[] = allIP.trim().split(",");
        allIPList = new ArrayList<IPBinding>();
        for (int i = 0; i < allIPArray.length; i++) {
            IPBinding ip = new IPBinding(allIPArray[i]);
            allIPList.add(ip);
        }
        if (startOrNot.equals("0")) {
            //this.isStart = false;
            this.radioValue = "1";
            //this.selectRender = false;
            this.tableRender = false;
            //ArrayList<IPBinding> ipList = new ArrayList<IPBinding>();
            // this.dataModel = new IPBindingDataModel(bindedIP);
//            dataModel.setIPList(ipList);
//            this.selectedIP = (IPBinding[]) ipList.toArray(new IPBinding[ipList.size()]);
            dataModel.setIPList(allIPList);
            this.selectedIP = (IPBinding[]) allIPList.toArray(new IPBinding[allIPList.size()]);
        } else {
            // this.isStart = true;
            this.radioValue = "2";
            //this.selectRender = true;
            this.tableRender = true;
            dataModel.setIPList(allIPList);
            String bindedIP = request.getParameter("bindedIP");
            String bindedIPArray[] = bindedIP.trim().split(",");
            ArrayList<IPBinding> bindedIPList = new ArrayList<IPBinding>();
            for (int i = 0; i < bindedIPArray.length; i++) {
                IPBinding ip = new IPBinding(bindedIPArray[i]);
                bindedIPList.add(ip);
            }
            this.selectedIP = (IPBinding[]) bindedIPList.toArray(new IPBinding[bindedIPList.size()]);
        }
//        ISCSITargetData data = SCSISession.getISCSITargetDataFromSession();
//        this.allIPList = data.getGlobalBindings();
//        temp = data.getTemp();
//        if (temp.isIsStartBind()) {
//            //this.isStart = true;
//            ArrayList<IPBinding> allBindings = temp.getBindings();
//            // dataModel = new IPBindingDataModel(allBindings);
//            dataModel.setIPList(allBindings);
//            ArrayList<IPBinding> bindedIP = new ArrayList<IPBinding>();
//            this.selectedIP = (IPBinding[]) bindedIP.toArray(new IPBinding[bindedIP.size()]);
//        } else {
//            ArrayList<IPBinding> bindedIP = new ArrayList<IPBinding>();
//            dataModel.setIPList(bindedIP);
//            this.selectedIP = (IPBinding[]) bindedIP.toArray(new IPBinding[bindedIP.size()]);
//        }
    }

    public boolean isTableRender() {
        return tableRender;
    }

    public void setTableRender(boolean tableRender) {
        this.tableRender = tableRender;
    }

    public boolean isSelectRender() {
        return selectRender;
    }

    public void setSelectRender(boolean selectRender) {
        this.selectRender = selectRender;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public boolean isIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public ArrayList<IPBinding> getBindings() {
        return allIPList;
    }

    public void setBindings(ArrayList<IPBinding> bindings) {
        this.allIPList = bindings;
    }

    public IPBinding[] getSelectedIP() {
        return selectedIP;
    }

    public void setSelectedIP(IPBinding[] selectedIP) {
        this.selectedIP = selectedIP;
    }

    public IPBindingDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(IPBindingDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void listen() {
        if (this.radioValue.equals("2")) {
            // ArrayList<IPBinding> bindedIP = new ArrayList<IPBinding>();
            //this.dataModel = new IPBindingDataModel(bindedIP);
            //this.selectRender = true;
            this.tableRender = true;
            dataModel.setIPList(allIPList);
            this.selectedIP = new IPBinding[0];
            // this.selectedIP = (IPBinding[]) bindedIP.toArray(new IPBinding[bindedIP.size()]);
        } else {
            //this.dataModel = new IPBindingDataModel(bindings);
            this.tableRender = false;
            this.dataModel.setIPList(allIPList);
            this.selectedIP = (IPBinding[]) allIPList.toArray(new IPBinding[allIPList.size()]);
            //Debug.print("$$$$$$$$$$###########" + this.selectedIP.length);
        }
    }

    public String save() {
//        ISCSITargetData data = SCSISession.getISCSITargetDataFromSession();
//        temp = data.getTemp();
//        if (!temp.isIsStartBind()) {
//            ArrayList<IPBinding> bindedIP = new ArrayList<IPBinding>();
//            this.temp.setBindings(bindedIP);
//        } 
//        else {
//
//            ArrayList<IPBinding> ips = (ArrayList<IPBinding>) this.dataModel.getBinds();
//
//            ArrayList<String> binded = new ArrayList<String>();
//            for (int i = 0; i < this.selectedIP.length; i++) {
//                binded.add(selectedIP[i].getIp());
//            }
//            for (int j = 0; j < ips.size(); j++) {
//                if (binded.contains(ips.get(j).getIp())) {
//                    ips.get(j).setIsStart(true);
//                } else {
//                    ips.get(j).setIsStart(false);
//                }
//            }
//            this.temp.setBindings(ips);
//        }
        boolean flag;
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        if (this.radioValue.equals("1")) {
            flag = scsi.ModifyTargetTPG("default", this.targetName);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("bindFailed"), res.get("bindFailed")));
                Debug.print("bind IP" + flag);
            }
        } else {
            if (this.selectedIP.length == 0) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("noIPSelect"), res.get("noIPSelect")));
                Debug.print("no IP select ");
                return null;
            }
            String tpg = "";
            for (int i = 0; i < this.selectedIP.length; i++) {
                if (i + 1 < selectedIP.length) {
                    tpg = tpg + selectedIP[i].getIp() + ",";
                } else {
                    tpg = tpg + selectedIP[i].getIp();
                }
            }
            Debug.print(" tpg= " + tpg);
            flag = scsi.ModifyTargetTPG(tpg, this.targetName);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("bindFailed"), res.get("bindFailed")));
                Debug.print(" bind IP " + flag);
            }
//            TargetInformation[] allTargetInfo = scsi.getAllTarget();
//            for (TargetInformation targetInfo : allTargetInfo) {
//                if (targetInfo.getProtocal().equals("iscsi")) {
//
//                    Debug.print( targetInfo.getTargetName());
//                    Debug.print("binded IP");
//                    ArrayList<String> tpgs = targetInfo.getTpgs();
//                    for (String bind : tpgs) {
//                        Debug.print( bind);
//                    }
//
//                }
//                //  targets.add(targetInfo.);
//            }

        }

        return "scsi_target?faces-redirect=true&amp;activeIndex=1";
    }

    public ISCSITarget getTemp() {
        return temp;
    }

    public void setTemp(ISCSITarget temp) {
        this.temp = temp;
    }
}
