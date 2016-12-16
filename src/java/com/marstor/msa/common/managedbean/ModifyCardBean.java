/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.IpmpGroupInformation;
import com.marstor.msa.common.bean.NetConfigInformation;
import com.marstor.msa.common.model.NetWorkCard;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "modifyCardBean")
@ViewScoped
public class ModifyCardBean implements Serializable {

    public String name;
    public String aggState;
    public String ip;
    public String subnet;
    public String gateway;
    public boolean defaultgateway;
    public boolean notdefault;
    public String state;
    NetWorkCard selectCard = null;
    public boolean notIP;
    public boolean notSubnet;
    public boolean notGameway;
    //�����û��IPMP��(һ���Ǿ�̬IP)������£�
    //���е������;ۺ϶����ó�DHCP��������Ҳ�Ͳ�֪��IP��ʲô�ˡ�
    public boolean hasIPMPConfig = false;
    public List<NICAndAggrForJudageDHCP> listNICAndAggr = null;
    boolean bCurrentIP = false;
    String returnStr = null;
    String tip = "";
    private MSAResource res = new MSAResource();
    private String basename = "common.network_modifycard";
    public String cardName;

    /**
     * Creates a new instance of ModifyCardBean
     */
    public ModifyCardBean() {
         ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        cardName = request.getParameter("cardName");
        if(cardName == null){
            cardName = "";
        }
         SystemOutPrintln.print_common("cardName="+cardName);
        getCard();
        this.listNICAndAggr();
        this.hasIPMPConfig();
    }

    private void listNICAndAggr() {
        listNICAndAggr = new ArrayList<NICAndAggrForJudageDHCP>();//�����ж��Ƿ������һ����̬IP

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        NetConfigInformation[] nets = common.getNetConfig();
        if (nets != null && nets.length > 0) {
            for (int i = 0; i < nets.length; i++) {
                 SystemOutPrintln.print_common("nets[i].groupName=" + nets[i].groupName);
                 SystemOutPrintln.print_common("nets[i].aggregationName=" + nets[i].aggregationName);
                 SystemOutPrintln.print_common("nets[i].isFCoE=" + nets[i].isFCoE);
                //�����ж��Ƿ������һ����̬IP
                if ((!nets[i].isFCoE) && (nets[i].aggregationName == null || "".equals(nets[i].groupName)) && (nets[i].aggregationName == null || "".equals(nets[i].aggregationName))) {
                    NICAndAggrForJudageDHCP naafjd = new NICAndAggrForJudageDHCP();
                    naafjd.NICOrAggrName = nets[i].name;
                    naafjd.isNIC = true;
                    naafjd.isStatic = nets[i].isStatic;
                    listNICAndAggr.add(naafjd);
                }
            }
        }

         SystemOutPrintln.print_common("listNICAndAggr.size()=" + listNICAndAggr.size());

    }

    private void hasIPMPConfig() {


        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        IpmpGroupInformation[] ipmpDroups = common.getIPMPGroupInformation();

        String stateStr = "";
        if (ipmpDroups != null && ipmpDroups.length > 0) {
            for (int i = 0; i < ipmpDroups.length; i++) {

                if (ipmpDroups[i].getState().equalsIgnoreCase("ok")) {
                    stateStr = res.get(basename, "ok");
                    hasIPMPConfig = (hasIPMPConfig || true);
                } else if (ipmpDroups[i].getState().equalsIgnoreCase("failed")) {
                    stateStr = res.get(basename, "failed");
                    hasIPMPConfig = (hasIPMPConfig || false);
                } else if (ipmpDroups[i].getState().equalsIgnoreCase("degraded")) {
                    stateStr = res.get(basename, "degraded");
                    hasIPMPConfig = (hasIPMPConfig || true);
                } else {
                    stateStr = ipmpDroups[i].getState();
                    hasIPMPConfig = (hasIPMPConfig || false);
                }



            }

        }
    }

    public boolean isNotGameway() {
        return notGameway;
    }

    public void setNotGameway(boolean notGameway) {
        this.notGameway = notGameway;
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
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

    public boolean isNotIP() {
        return notIP;
    }

    public void setNotIP(boolean notIP) {
        this.notIP = notIP;
    }

    public boolean isNotSubnet() {
        return notSubnet;
    }

    public void setNotSubnet(boolean notSubnet) {
        this.notSubnet = notSubnet;
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
    
    private void getCard() {
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        NetConfigInformation[] nets = common.getNetConfig();
        NetWorkCard cardBean = null;
        if (nets != null && nets.length > 0) {

            for (int i = 0; i < nets.length; i++) {
                if (cardName.equals(nets[i].name)) {
                    cardBean = new NetWorkCard();
                    cardBean.setCardName(nets[i].name);
                     SystemOutPrintln.print_common("nets[i].name=" + nets[i].name);
                    String ip = nets[i].address;
                    if (nets[i].netmask != null && !nets[i].netmask.equals("")) {
                        ip = ip + "/" + nets[i].netmask;
                    }

                    cardBean.setIp(ip);
                    cardBean.setSubnet(nets[i].netmask);
                    cardBean.setAggregation(nets[i].aggregationName);
                    cardBean.setIpmpGroupName(nets[i].groupName);
                    if ((cardBean.getAggregation() != null && !cardBean.getAggregation().equals("")) || (cardBean.getIpmpGroupName() != null && !cardBean.getIpmpGroupName().equals(""))) {
                        cardBean.setNotEdit(true);
                    } else {
                        cardBean.setNotEdit(false);
                    }

                    ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
                    HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
                    HttpSession session = request.getSession();
                    SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
                    int userType = user.getType();
                    if (userType != 2) {
                        cardBean.setNotEdit(true);
                    }

                    cardBean.setPCoECard(nets[i].isFCoE);
                    cardBean.setEnable(nets[i].isEnable);
                    cardBean.setGateway(nets[i].gateway);

                    String dhcp = "";
                    if (nets[i].address.length() != 0) {
                        dhcp = nets[i].isStatic ? res.get(basename, "no") : res.get(basename, "yes");
                    }
                    cardBean.setState(dhcp);
                    break;
                }


            }
            modifyCard(cardBean);

        }
    }
    public void modifyCard(NetWorkCard select) {
        if(select==null){
            return;
        }
        this.selectCard = select;
//        FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
//        NetWorkCardBean share = (NetWorkCardBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{netWorkCardBean}", NetWorkCardBean.class).getValue(context.getELContext());
//        selectCard = share.getSelectedCard();
        name = selectCard.getCardName();
         SystemOutPrintln.print_common("name="+name);
        if (selectCard.getIp() != null && !selectCard.getIp().equals("")) {
            ip = selectCard.getIp().split("/")[0];
        } else {
            ip = "";
        }
        if (selectCard.getSubnet() != null && !selectCard.getSubnet().equals("")) {
            subnet = netBitsToSubnet32(selectCard.getSubnet());
        }

        if (selectCard.getState().equals(res.get(basename, "no"))) {  //��̬
            state = "1";
        } else if (selectCard.getState().equals(res.get(basename, "yes"))) {  //��̬
            state = "0";
        } else {  // ��ֹip==null||ip.equalsIgnoreCase("")
            state = "2";
        }
        
         gateway = selectCard.getGateway();
        if(gateway!=null && !gateway.equals("")){
            this.defaultgateway = false;
        }else{
            this.defaultgateway = true;
        }
        changeBooleanCheckbox(defaultgateway);
        
        
        if (state.equals("0") || state.equals("2")) {
            notIP = true;
            notSubnet = true;
            this.notdefault = true;
            this.notGameway = true;
        } else {
            notIP = false;
            notSubnet = false;
            this.notdefault = false;
        }
        
        
        
        
        this.confirmUseIp();
        
       
        
 

         SystemOutPrintln.print_common("ֵ=" + selectCard.getCardName());
//        return "network_modifycard?faces-redirect=true";
    }

    public static String netBitsToSubnet32(String subnet) {  //ת���������룬���硰/24����Ϊ��255.255.255.0��
        String subnet32 = "";
        String data = "";
        for (int i = 0; i < Integer.valueOf(subnet); i++) {
            data = data + "1";
        }
        for (int j = 0; j < (32 - Integer.valueOf(subnet)); j++) {
            data = data + "0";
        }
        List<String> sub = new ArrayList();
        for (int k = 0; k < 4; k++) {
            sub.add(data.substring(0 + k * 8, 8 + k * 8));
        }
        String subStr = "";
        for (int k = 0; k < 4; k++) {
            if (k == 3) {
                subStr = subStr + Integer.valueOf(sub.get(k), 2).toString();
            } else {
                subStr = subStr + Integer.valueOf(sub.get(k), 2).toString() + ".";
            }

        }
        return subStr;
    }

    public static int sub32ToNetBits(String strip) {  //ת���������룬���硰255.255.255.0����Ϊ��/24��
        StringBuffer sbf;
        String str;
        int inetmask = 0;
        int count = 0;       //����������д����
        String[] ipList = strip.split("\\.");

        for (int n = 0; n < ipList.length; n++) {
            sbf = toBin(Integer.parseInt(ipList[n]));
            str = sbf.reverse().toString();

            //ͳ��2�����ַ�����1�ĸ���
            count = 0;
            for (int i = 0; i < str.length(); i++) {
                i = str.indexOf('1', i);  //���� �ַ�'1'���ֵ�λ��                         
                if (i == -1) {
                    break;
                }
                count++;  //ͳ���ַ����ִ���
            }
            inetmask += count;

        }
        return inetmask;


    }

    static StringBuffer toBin(int x) {
        StringBuffer result = new StringBuffer();
        result.append(x % 2);
        x /= 2;
        while (x > 0) {
            result.append(x % 2);
            x /= 2;
        }
        return result;
    }

    public void changeBooleanCheckbox() {
        if (state.equals("1")) {  //��̬
            notIP = false;
            notSubnet = false;
            this.notdefault = false;
            this.notGameway = false;
             changeBooleanCheckbox(defaultgateway);
        } else {  //��̬������
            notIP = true;
            notSubnet = true;
            this.notdefault = true;
            this.notGameway = true;
        }
    }

    public void confirmUseIp() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        String ipStr = request.getLocalAddr();
        //�Ƿ�Ϊ��ǰIP
         SystemOutPrintln.print_common("ipStrֵ=" + ipStr);
         SystemOutPrintln.print_common("ip=" + ip);
        if (ip.equals(ipStr)) {
            bCurrentIP = true;
            tip = res.get(basename, "tip");
             SystemOutPrintln.print_common("talk dialog");
//         //������ʾ��
//         //   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ѡ���������ڱ���ǰ����ʹ��,",  "�޸ĺ�������µ�¼��"));
        } else {
            tip = "";
        }
         SystemOutPrintln.print_common("bCurrentIP=" + bCurrentIP);

    }

    public String real_save() {
        NetConfigInformation nic = new NetConfigInformation();
         SystemOutPrintln.print_common("ip=" + ip);
         SystemOutPrintln.print_common("subnet=" + subnet);
         SystemOutPrintln.print_common("selectCard.getCardName()=" + selectCard.getCardName());
         SystemOutPrintln.print_common("state=" + state);
         if (state.equals("1")) {  //��̬
              nic.setAddress(ip);

            if (subnet != null && !subnet.equals("")) {
                nic.setNetmask(sub32ToNetBits(subnet) + "");
            } else {
                nic.setNetmask(subnet);
            }
            if(!defaultgateway){
                  nic.setGateway(gateway);
            }
        } else {
            nic.setAddress("");
            nic.setNetmask("");
            
        }
     
        nic.setAggregationName(selectCard.getAggregation());
       // nic.setGateway(selectCard.getGateway());
        nic.setGroupName(selectCard.getIpmpGroupName());
        nic.setIsFCoE(selectCard.isPCoECard());

        nic.setName(selectCard.getCardName());
        if (state.equals("0")) {  //��̬
            nic.setIsEnable(true);
            nic.setIsStatic(false);
        } else if (state.equals("1")) {  //��̬
            nic.setIsEnable(true);
            nic.setIsStatic(true);
        } else {  //��ֹ
            nic.setIsEnable(false);
            nic.setIsStatic(false);
        }

        boolean iniIsStatic = false;
        if (selectCard.getState().equals(res.get(basename, "no"))) {  //��̬
            iniIsStatic = true;
        } else if (ip == null || ip.equalsIgnoreCase("")) {  //��ֹ
            iniIsStatic = false;
        } else {  //��̬
            iniIsStatic = false;
        }

        boolean modifyUser = !(nic.isIsStatic() == iniIsStatic);
         SystemOutPrintln.print_common("�޸�����bCurrentIP=" + bCurrentIP);
//        
//        selectCard.setIp(ip);
//        selectCard.setSubnet(subnet);
//        if(state.equals("0")){  //��̬
//            state = "��";
//        }else if(state.equals("1")){  //��̬
//            state = "��";
//        }else {  //��ֹ
//             state = "";
//        }
//        selectCard.setState(state);

        //�����ж��Ƿ������һ����̬IP
         SystemOutPrintln.print_common("nic.isStatic=" + nic.isStatic);
         SystemOutPrintln.print_common("nic.name=" + nic.name);
        if ((!nic.isStatic) && (!canModifyNICOrAggrToDHCP(nic.name))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "min_onestatic"), ""));
            return null;
        }
         SystemOutPrintln.print_common("bCurrentIP=" + bCurrentIP);
        if (bCurrentIP && !nic.isEnable) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "using"), ""));
            return null;
        }


        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();

//        if (bCurrentIP) {
//            if (Constants.showQuestionMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("����ʹ�õ�ǰ�������޸ĺ�������µ�¼")) != 0) {
//                return null;
//            }  
//        }
        boolean modifyNic = common.setNetConfig(nic, modifyUser);

        if (modifyNic == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "modifynic_fail"), ""));
            returnStr = null;
        } else {
             SystemOutPrintln.print_common("modify card succeed !");
            returnStr = "system_network?faces-redirect=true&amp;accordionActive1=0";
        }

        return returnStr;
    }

    public String save() {

        if (state.equals("1")) {  //��̬
            if (null == ip || "".equals(ip)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_null"), ""));
                return null;
            }
            if (!ValidateUtility.checkIP(ip)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_no"), ""));
                return null;
            }
            if (null == subnet || "".equals(subnet)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "subnet_null"), ""));
                return null;
            }
            if (!ValidateUtility.checkIP(subnet)) {
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

        returnStr = null;
         SystemOutPrintln.print_common("�޸�����bCurrentIP=" + bCurrentIP);
//         if(confirmUseIp() == true){
//            RequestContext.getCurrentInstance().execute("useip.show()");
//             returnStr = null;
//            return null;
//         }else{
        this.real_save();
//         }
        return returnStr;


//        selectCard.setIp(ip);
//        selectCard.setSubnet(subnet);
//        if(state.equals("0")){  //��̬
//            state = "��";
//        }else if(state.equals("1")){  //��̬
//            state = "��";
//        }else {  //��ֹ
//             state = "";
//        }
//        selectCard.setState(state);


    }

    public boolean canModifyNICOrAggrToDHCP(String NICOrAggrName) {
        if (null == NICOrAggrName || "".equals(NICOrAggrName)) {
            return false;
        }

        if (hasIPMPConfig) {
            return true;
        }
        //��¼��̬IP�ĸ���
        int staticIPCount = 0;

        for (NICAndAggrForJudageDHCP nicORaggr : listNICAndAggr) {
             SystemOutPrintln.print_common("listNICAndAggr.size()=" + listNICAndAggr.size());

            if (nicORaggr.isStatic) {
                staticIPCount++;
                 SystemOutPrintln.print_common("staticIPCount++=" + staticIPCount);
            }
             SystemOutPrintln.print_common("NICOrAggrName=" + NICOrAggrName);
             SystemOutPrintln.print_common("nicORaggr.NICOrAggrName=" + nicORaggr.NICOrAggrName);
            if (nicORaggr.NICOrAggrName.equals(NICOrAggrName) && (nicORaggr.isStatic)) {
                //����������Ӿ�̬��Ϊ��̬
                if (nicORaggr.isStatic) {
                    staticIPCount--;
                     SystemOutPrintln.print_common("staticIPCount--=" + staticIPCount);
                } else {
                    //�������ԭ������DHCP������������Ϊ��ΪDHCP
                    return true;
                }
            }
        }
         SystemOutPrintln.print_common("staticIPCount=" + staticIPCount);
        //����������Ӿ�̬��Ϊ��̬�󣬾�̬IP�ĸ���Ϊ0���򲻿��޸�
        if (staticIPCount == 0) {
            return false;
        }

        return true;
    }

    public boolean canCreateAggrToDHCP(List<String> nicToCreateAggr) {
        if (null == nicToCreateAggr) {
            return false;
        }
        if (nicToCreateAggr.isEmpty()) {
            return false;
        }
        if (hasIPMPConfig) {
            return true;
        }
        //��¼��̬IP�ĸ���
        int staticIPCount = 0;
        for (NICAndAggrForJudageDHCP nicORaggr : listNICAndAggr) {
            if (nicORaggr.isStatic) {
                staticIPCount++;
            }
            for (String nic : nicToCreateAggr) {
                if (nicORaggr.NICOrAggrName.equals(nic) && (nicORaggr.isStatic)) {
                    //����������Ӿ�̬��Ϊ��̬
                    staticIPCount--;
                }
            }
        }
        //����������Ӿ�̬��Ϊ��̬�󣬾�̬IP�ĸ���Ϊ0���򲻿��޸�
        if (staticIPCount == 0) {
            return false;
        }

        return true;
    }
    
     public void changeBooleanCheckbox( boolean isSt){
         if(isSt == true){
            notGameway = true;
            
        }else{
            notGameway = false;
        }
     } 

    public class NICAndAggrForJudageDHCP {

        /**
         * isNIC is true : NIC Name;<br/> isNIC is false: Aggregation Name
         */
        public String NICOrAggrName = "";
        /**
         * true : NIC;<br/> false: Aggr
         */
        public boolean isNIC = false;
        /**
         * true : Static IP;<br/> false: DHCP
         */
        public boolean isStatic = false;
    }
}
