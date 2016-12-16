/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.AggregationInformation;
import com.marstor.msa.common.model.NICAggregation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "modifyAggregateBean")
@ViewScoped
public class ModifyAggregateBean implements Serializable {

    public String name;
    public String nic;
    public String ip;
    public String subnet;
    public String state;
    public String mode;
    NICAggregation selectAgg;
    public boolean notIP;
    String returnStr = null;
    public String tip = "";
    private MSAResource res = new MSAResource();
    private String basename = "common.system_network_modifyaggregate";
     public String aggName;
    public String gateway;
    public boolean defaultgateway;
    public boolean notdefault;
    public boolean notGameway;
    /**
     * Creates a new instance of ModifyAggregateBean
     */
    public ModifyAggregateBean() {
         ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        aggName = request.getParameter("aggName");
        SystemOutPrintln.print_common("aggName="+aggName);
        getAgg();
    }
    private void getAgg() {
        
     
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        AggregationInformation[] aggrs = common.getAggregation();
        NICAggregation nicAgg = null;
        if (aggrs != null && aggrs.length > 0) {

            for (int i = 0; i < aggrs.length; i++) {
                if (aggrs[i].getName().equals(aggName)) {
                    nicAgg = new NICAggregation();
                    nicAgg.setName(aggrs[i].getName());
                    String nicsStr = "";
                    String[] nics = aggrs[i].getNics();
                    if (nics != null && nics.length != 0) {
                        for (int j = 0; j < nics.length; j++) {
                            nicsStr = nicsStr + nics[j] + " ";
                        }
                    }
                    nicAgg.setNic(nicsStr);
                    String ip = aggrs[i].getAddress();
                    if (aggrs[i].getNetmask() != null && !aggrs[i].getNetmask().equals("")) {
                        ip = ip + "/" + aggrs[i].getNetmask();
                    }
                    nicAgg.setIp(ip);
                    nicAgg.setSubnet(aggrs[i].getNetmask());
                    nicAgg.setIpmp(aggrs[i].getGroupName());  //错误，其实真实值为聚合的名字
                    String modelStr = res.get(basename, "off");
                    if (aggrs[i].getModel() == AggregationInformation.OFF) {
                        modelStr = res.get(basename, "off");
                    } else if (aggrs[i].getModel() == AggregationInformation.ACTIVE) {
                        modelStr = res.get(basename, "active");
                    } else if (aggrs[i].getModel() == AggregationInformation.PASSIVE) {
                        modelStr = res.get(basename, "passive");
                    }
                    nicAgg.setMode(modelStr);
                    nicAgg.setEnable(aggrs[i].isIsEnable());
                    String dhcp = res.get(basename, "yes");
                    if (aggrs[i].isIsEnable() == true) {
                        if (aggrs[i].isIsStatic() == false) {
                            dhcp = res.get(basename, "yes");
                        } else {
                            dhcp = res.get(basename, "no");
                        }
                    } else {
                        dhcp = "";
                    }

                    nicAgg.setState(dhcp);
                    nicAgg.setGateway(aggrs[i].getGateway());
                    break;
                }

            }
            modifyAgg(nicAgg);

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isNotIP() {
        return notIP;
    }

    public void setNotIP(boolean notIP) {
        this.notIP = notIP;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public boolean isDefaultgateway() {
        return defaultgateway;
    }

    public void setDefaultgateway(boolean defaultgateway) {
        this.defaultgateway = defaultgateway;
    }

    public boolean isNotdefault() {
        return notdefault;
    }

    public void setNotdefault(boolean notdefault) {
        this.notdefault = notdefault;
    }

    public boolean isNotGameway() {
        return notGameway;
    }

    public void setNotGameway(boolean notGameway) {
        this.notGameway = notGameway;
    }
    
    public void changeBooleanCheckbox( boolean isSt){
         if(isSt == true){
            notGameway = true;
            
        }else{
            notGameway = false;
        }
     } 
    public void modifyAgg(NICAggregation select) {

//        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
//        NICAggregationBean share = (NICAggregationBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{nICAggregationBean}", NICAggregationBean.class).getValue(context.getELContext());
//        selectAgg = share.getSelectAgg();
        selectAgg = select;
        nic = selectAgg.getNic();
        if (selectAgg.getIp() != null && !selectAgg.getIp().equals("")) {
            ip = selectAgg.getIp().split("/")[0];
        } else {
            ip = "";
        }
        if (selectAgg.getSubnet() != null && !selectAgg.getSubnet().equals("")) {
            subnet = ModifyCardBean.netBitsToSubnet32(selectAgg.getSubnet());
        } else {
            subnet = "";
        }

        if (selectAgg.getState().equals(res.get(basename, "no"))) {  //静态
            state = "1";
        } else if (ip == null || ip.equalsIgnoreCase("")) {  //禁止
            state = "2";
        } else {  //动态
            state = "0";
        }
         gateway = selectAgg.getGateway();
        if(gateway!=null && !gateway.equals("")){
            this.defaultgateway = false;
        }else{
            this.defaultgateway = true;
        }
        changeBooleanCheckbox(defaultgateway);
        if (state.equals("0") || state.equals("2")) {
            notIP = true;
            this.notdefault = true;
            this.notGameway = true;
        } else {
            notIP = false;
            this.notdefault = false;
        }
        String modeStr = selectAgg.getMode();
        if (modeStr.equals(res.get(basename, "off"))) {
            mode = "0";
        } else if (modeStr.equals(res.get(basename, "active"))) {
            mode = "1";
        } else if (modeStr.equals(res.get(basename, "passive"))) {
            mode = "2";  //被动
        }
        
            ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String ipStr = request.getLocalAddr();
        boolean bCurrentIP = false;  
        if (ipStr.equals(ip)) {
            bCurrentIP = true;
           tip = res.get(basename, "using_nic");
            //弹出警告框              Constants.showWarningMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("选择网卡正在被当前连接使用"));
        }else{
            tip = res.get(basename, "tip_nic");
             
        }
//        return "system_network_modifyaggregate?faces-redirect=true";
    }

    public void changeBooleanCheckbox() {
        if (state.equals("1")) {  //静态
            notIP = false;
             this.notdefault = false;
            this.notGameway = false;
             changeBooleanCheckbox(defaultgateway);
        } else {  //动态、禁用
            notIP = true;
            this.notdefault = true;
            this.notGameway = true;
        }
    }

    public String confirm(){
           if (state.equals("1")) {  //静态
             if(null == ip || "".equals(ip)){
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_null"), ""));
                return null;
            }
            if(!ValidateUtility.checkIP(ip)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_no"), ""));
                return null;
            }
            if(null == subnet || "".equals(subnet)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "subnet_null"), ""));
                return null;
            }
            if(!ValidateUtility.checkIP(subnet)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "subnet_no"), ""));
                return null;
            }
            if(!defaultgateway){
                if (null == gateway || "".equals(gateway)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gateway_null"), ""));
                    return null;
                }
                if (!ValidateUtility.checkIP(gateway)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gateway_no"), ""));
                    return null;
                }
            }
            
        }
        
//        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        String ipStr = request.getLocalAddr();
//        boolean bCurrentIP = false;  
//        if (ipStr.equals(ip)) {
//            bCurrentIP = true;
//            returnStr = null;
//             RequestContext.getCurrentInstance().execute("modifyagg.show()");
//            return null;
//            //弹出警告框              Constants.showWarningMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("选择网卡正在被当前连接使用"));
//        }else{
             saveAgg();
//        }
         return returnStr;
    }
    public String saveAgg() {






//        selectAgg.setIp(ip);
//        selectAgg.setSubnet(subnet);
//        if (state.equals("0")) {  //动态
//            state = "是";
//        } else if (state.equals("1")) {  //静态
//            state = "否";
//        } else {  //禁止
//            state = "";
//        }
//        selectAgg.setState(state);
//        String modeStr = null;
//        if (mode.equals("0")) {
//            modeStr = "自动";
//        } else if (mode.equals("1")) {
//            modeStr = "主动";
//        } else {
//            modeStr = "被动"; //“2”
//        }
//        selectAgg.setMode(modeStr);
 SystemOutPrintln.print_common("修改聚合selectAgg ip="+ ip);
        AggregationInformation aggr = new AggregationInformation();
        aggr.setAddress(ip);
        if (!defaultgateway) {
             SystemOutPrintln.print_common("gateway="+gateway);
            aggr.setGateway(gateway);
        }
//        aggr.setGateway(selectAgg.getGateway());
        aggr.setGroupName(name);
        if (state.equals("0")) {  //动态
            aggr.setIsEnable(true);
            aggr.setIsStatic(false);
        } else if (state.equals("1")) {  //静态
            aggr.setIsEnable(true);
            aggr.setIsStatic(true);
        } else {  //禁止
            aggr.setIsEnable(false);
            aggr.setIsStatic(false);
        }
        int modelStr = AggregationInformation.OFF;
        if (selectAgg.getMode().equals("0")) {
            modelStr = AggregationInformation.OFF;
        } else if (selectAgg.getMode().equals("1")) {
            modelStr = AggregationInformation.ACTIVE;
        } else if (selectAgg.getMode().equals("2")) {
            modelStr = AggregationInformation.PASSIVE;
        }
        aggr.setModel(modelStr);
        aggr.setName(selectAgg.getName());
        if (subnet != null && !subnet.equals("")) {
            aggr.setNetmask(ModifyCardBean.sub32ToNetBits(subnet) + "");
        } else {
            aggr.setNetmask(subnet);
        }
        String[] nics = selectAgg.getNic().split("");
        aggr.setNics(nics);
//        boolean modifyUser = false;
//        if(aggr.isIsStatic()==true){
//            modifyUser = true;
//        }else{
//            modifyUser = false;
//        }

        boolean iniIsStatic = false;
        if (selectAgg.getState().equals(res.get(basename, "no"))) {  //静态
            iniIsStatic = true;
        } else if (ip == null || ip.equalsIgnoreCase("")) {  //禁止
            iniIsStatic = false;
        } else {  //动态
            iniIsStatic = false;
        }
        boolean modifyUser = !(iniIsStatic == aggr.isIsStatic());



        ModifyCardBean modify = new ModifyCardBean();
        //用于判断是否是最后一个静态IP
        if ((!aggr.isIsStatic()) && (!modify.canModifyNICOrAggrToDHCP(aggr.getName()))) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "min_onestatic"), ""));
            return null;
        }

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean modifyAggr = common.modifyAggregation(aggr, modifyUser);
        if (modifyAggr == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "modifyaggr_fail"), ""));
            returnStr = null;
        } else {
            returnStr = "system_network?faces-redirect=true&amp;accordionActive1=1";
             SystemOutPrintln.print_common("modify agg succeed");
        }


        return returnStr;
    }
}
