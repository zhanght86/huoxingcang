/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.AggregationInformation;
import com.marstor.msa.common.model.NetWorkCard;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "createAggregateBean")
@ViewScoped
public class CreateAggregateBean implements Serializable{

    public String name;
    public String aggState = "0";
    public String ip;
    public String subnet;
    public String LACPType = "0";
    private NetWorkCard[] selectedCards;
    public boolean notIP;
    AggregationInformation aggr = null;
    String returnStr = null;
    private MSAResource res = new MSAResource();
    private String basename = "common.network_createaggregate";
    public String gateway;
    public boolean defaultgateway = true;
    public boolean notdefault;
    public boolean notGameway;

    /**
     * Creates a new instance of CreateAggregateBean
     */
    public CreateAggregateBean() {
        changeBooleanCheckbox();
    }
    
    public void changeBooleanCheckbox() {
        if (aggState.equals("1")) {  //静态
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAggState() {
        return aggState;
    }

    public void setAggState(String aggState) {
        this.aggState = aggState;
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

    public String getLACPType() {
        return LACPType;
    }

    public void setLACPType(String LACPType) {
        this.LACPType = LACPType;
    }

    public NetWorkCard[] getSelectedCards() {
        return selectedCards;
    }

    public void setSelectedCards(NetWorkCard[] selectedCards) {
        this.selectedCards = selectedCards;
    }

    public boolean isNotIP() {
        return notIP;
    }

    public void setNotIP(boolean notIP) {
        this.notIP = notIP;
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
    
    public void changeBooleanCheckbox(boolean isSt) {
        if (isSt == true) {
            notGameway = true;

        } else {
            notGameway = false;
        }
    } 

    public String handle(NetWorkCard[] selects,String state, String ip, String subnet, String lacp) {

//        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
//        NetWorkCardBean share = (NetWorkCardBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{netWorkCardBean}", NetWorkCardBean.class).getValue(context.getELContext());
//        System.out.println("11###################selectedCards=" + share.cardList.size());
//
//        selectedCards = share.getSelectedCards();
        selectedCards = selects;
        SystemOutPrintln.print_common("创建聚合selectedCards.lenght="+selectedCards.length);
        
        if (selectedCards != null) {
            if (selectedCards.length < 2) {//至少得两个
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "mintwo"), ""));
                return null;
            }
        } else {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "mintwo"), ""));
            return null;
        }  
        
        if (state.equals("1")) {  //静态
             if(null == ip || "".equals(ip)){
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ipnull"), ""));
                return null;
            }
            if(!ValidateUtility.checkIP(ip)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ipno"), ""));
                return null;
            }
            if(null == subnet || "".equals(subnet)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "subnetnull"), ""));
                return null;
            }
            if(!ValidateUtility.checkIP(subnet)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "subnetno"), ""));
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
          
        
          //是否为当前IP
        boolean bCurrentIP = false;
        //用于判断是否是最后一个静态IP
        ArrayList<String> nicOfAggr = new ArrayList<String>();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String ipStr = request.getLocalAddr();
        SystemOutPrintln.print_common("request.getLocalAddr()=" + request.getLocalAddr());
        for (int j = 0; j < selectedCards.length; j++) {
            nicOfAggr.add(selectedCards[j].cardName);
            SystemOutPrintln.print_common("selectedCards[j].getIp()=" + selectedCards[j].getIp());
            if (selectedCards[j].getIp().equals(ipStr)) {
                bCurrentIP = true;
                //弹出提示框            Constants.showWarningMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("选择网卡正在被当前连接使用"));
            }

        }
        
        String[] cards = new String[selectedCards.length];
        for (int i = 0; i < selectedCards.length; i++) {
            cards[i] = selectedCards[i].getCardName();
        }
//        AggregationInformation aggr= new AggregationInformation();
        aggr= new AggregationInformation();
        aggr.setAddress(ip);
        if (!defaultgateway) {
            aggr.setGateway(gateway);
        }
        
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
        if (lacp.equals("0")) {
            modelStr = AggregationInformation.OFF;
        } else if (lacp.equals("1")) {
            modelStr = AggregationInformation.ACTIVE;
        } else if (lacp.equals("2")) {
            modelStr = AggregationInformation.PASSIVE;
        }
        aggr.setModel(modelStr);
        aggr.setName("");
         if(subnet!=null && !subnet.equals("")){
            aggr.setNetmask(ModifyCardBean.sub32ToNetBits(subnet)+"");
        }else{
            aggr.setNetmask("");
        }
        aggr.setNics(cards);
        
         ModifyCardBean modify = new ModifyCardBean();
          //用于判断是否是最后一个静态IP
        if ((!aggr.isIsStatic()) && (!modify.canCreateAggrToDHCP(nicOfAggr))) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "min_ip"),  ""));
          
            return null;
        }
        returnStr = null;
        if (bCurrentIP) {
            RequestContext.getCurrentInstance().execute("create.show()");
            returnStr = null;
            return null;
// 已添加弹出提示框           int a = Constants.showQuestionMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("正在使用当前网卡，修改后必须重新登录"));
//            if (a != 0) {
//                return null;
//            }

        } else {
            creat_real();
        }
        
// //       boolean createAggregation(AggregationInformation aggr)
        
//        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
//        boolean createAggr = common.createAggregation(aggr);
//        String returnStr = null;
//        if(createAggr == false){
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "创建聚合",  "失败。"));
//            returnStr = null;
//        }else{
//            returnStr = "system_network?faces-redirect=true";
//        }
        
        
        
        
        
//         String cards = "";
//        for (int i = 0; i < selectedCards.length; i++) {
//            cards = cards + " " + selectedCards[i].getCardName();
//        }
//        NICAggregation nicAgg = new NICAggregation();
//        nicAgg.setName("aggr1");
//        nicAgg.setNic(cards);
//        nicAgg.setIp(ip);
//        nicAgg.setSubnet(subnet);
//        nicAgg.setIpmp("");
//        String stateStr = "";
//        if(state.equals("0")){
//            stateStr= "是";
//        }else if(state.equals("1")){
//             stateStr= "否";
//        }else{
//             stateStr= "";
//        }
//        nicAgg.setState(stateStr);
//         String lacpStr = "";
//        if(lacp.equals("0")){
//            lacpStr= "自动";
//        }else if(lacp.equals("1")){
//             lacpStr= "主动";
//        }else{
//             lacpStr= "被动";
//        }
//        nicAgg.setMode(lacpStr);
//        nicAgg.setEnable(false);
//
//        NICAggregationBean agg = (NICAggregationBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{nICAggregationBean}", NICAggregationBean.class).getValue(context.getELContext());
//        agg.getNicAggList().add(nicAgg);
        return returnStr;

    }
    
    public String creat_real(){
        SystemOutPrintln.print_common("创建聚合ip="+aggr.getAddress());
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean createAggr = common.createAggregation(aggr);
  
        if(createAggr == false){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "createaggr_fail"),  ""));
            returnStr = null;
        }else{
            returnStr = "system_network?faces-redirect=true&amp;accordionActive1=1";
        }
        
        SystemOutPrintln.print_common("创建聚合跳转");
        return returnStr;
    }
}
