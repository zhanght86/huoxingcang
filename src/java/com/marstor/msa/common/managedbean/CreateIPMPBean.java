/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.AggregationInformation;
import com.marstor.msa.common.model.CreateIPMPModel;
import com.marstor.msa.common.model.IPMPAddress;
import com.marstor.msa.common.bean.IpmpGroupInformation;
import com.marstor.msa.common.bean.NetConfigInformation;
import com.marstor.msa.common.model.NetConfigInformationDetail;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "createIPMPBean")
@SessionScoped
public class CreateIPMPBean implements Serializable {

    public String ipmpGroupName;
    public List<NetConfigInformationDetail> cardList;
    public NetConfigInformationDetail cardBean;
    private CreateIPMPModel cardModel;
//    private NetConfigInformation[] selectedCards;
    private List<NetConfigInformation> selectedCards;
    public boolean isCurrentIP = false;
    String returnStr = null;
    private MSAResource res = new MSAResource();
    private String basename = "common.network_createipmp";
    private List<IPMPAddress> dataList = new ArrayList<IPMPAddress>();
    private IPMPAddress selectedDataAddress;
    public boolean ischeck;
    public boolean istarget;
    public String targetIP;
    public List<IPMPAddress> testList = new ArrayList<IPMPAddress>();
    private IPMPAddress selectedTestAddress;
    public boolean nottargetIP = true;

    /**
     * Creates a new instance of CreateIPMPBean
     */
    public CreateIPMPBean() {
      init();
       
//        cardModel = new CreateIPMPModel(cardList);
    }
    public void init(){
        ipmpGroupName = "";
        cardList = null;
        selectedCards = null;
        isCurrentIP = false;
        returnStr = null;
        dataList = new ArrayList<IPMPAddress>();
        selectedDataAddress = null;
        ischeck = false;
        istarget = false;
        targetIP = "";
        testList = new ArrayList<IPMPAddress>();
        selectedTestAddress = null;
        nottargetIP = true;
        ipmpGroupName = "ipmp"+this.getVMNICNum();
         cardList();
    }
    
    
    //获得新建IPMP编号
    public int getVMNICNum(){
          CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        IpmpGroupInformation[] ipmpDroups = common.getIPMPGroupInformation();
        
        ArrayList<Integer> attribute1 = new ArrayList<Integer>();//获得已经用掉的端口
        if (ipmpDroups != null && ipmpDroups.length > 0) {
            
            for (int i = 0; i < ipmpDroups.length; i++) {
            SystemOutPrintln.print_common("ipmpDroups[i].getGroupName().substring(4)="+ipmpDroups[i].getGroupName().substring(4));
            int vmNICNum = Integer.valueOf(ipmpDroups[i].getGroupName().substring(4));
            attribute1.add(vmNICNum);
        }
        }
        
        
        ArrayList<Integer> attribute2 = new ArrayList<Integer>();//获得未用的端口
        for (int i = 0; i < 9; i++) {  //给赋值
            boolean isHave = false;
            for (int j = 0; j < attribute1.size(); j++) {
                if (i == attribute1.get(j)) {
                    isHave = true;
                    break;
                } else {
                    isHave = false;
                }
            }
            if (isHave == false) {
                attribute2.add(i);
            }
        }

        int nic = attribute2.get(0);
        return nic;
    }
    public String toCreatIPMP(){
        init();
        return "network_createipmp?faces-redirect=true";
    }

//    public void addCardList() {
//
//        cardList = new ArrayList();
//
//        cardBean = new NetWorkCard();
//        cardBean.setCardName("e1000g0");
//        cardBean.setIp("192.168.1.224");
//        cardBean.setSubnet("255.255.255.0");
//        cardList.add(cardBean);
//
//        cardBean = new NetWorkCard();
//        cardBean.setCardName("e1000g1");
//        cardBean.setIp("192.168.100.224");
//        cardBean.setSubnet("255.255.255.0");
//        cardList.add(cardBean);
//
//    }
    public void cardList() {
        cardList = new ArrayList();

        ArrayList<AggregationInformation> aggregations = new ArrayList<AggregationInformation>();
        ArrayList<NetConfigInformation> netCards = new ArrayList<NetConfigInformation>();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        AggregationInformation[] aggrs = common.getAggregation();
        NetConfigInformation[] nets = common.getNetConfig();
        int aggreCount = 0;
        if (aggrs != null && aggrs.length > 0) {
            aggreCount = aggrs.length;
        }
        int netCount = 0;
        if (nets != null && nets.length > 0) {
            netCount = nets.length;
        }

        for (int i = 0; i < aggreCount; i++) {
            AggregationInformation object = aggrs[i];
            if (!(null == object.getGroupName() || "".equals(object.getGroupName()))) {
                continue;
            }
            if(object.getName().toLowerCase().startsWith("ibp")){
                continue;
            }
             
            
         
//            if ("0.0.0.0".equals(object.getAddress()) || null == object.getAddress() || "".equals(object.getAddress())) {
//                continue;
//            }
//            if (!object.isIsEnable()) {
//                continue;
//            }
//            if (!object.isIsStatic()) {
//                continue;
//            }
            aggregations.add(object);
        }
        for (int i = 0; i < netCount; i++) {
            NetConfigInformation object = nets[i];
            if (!(null == object.groupName || "".equals(object.groupName))) {
                continue;
            }
            if (!(null == object.aggregationName || "".equals(object.aggregationName))) {
                continue;
            }
            
             if(object.getName().toLowerCase().startsWith("ibp")){
                continue;
            }
//            if ("0.0.0.0".equals(object.address) || null == object.address || "".equals(object.address)) {
//                continue;
//            }
//            if (!object.isEnable) {
//                continue;
//            }
            if (object.isFCoE) {
                continue;
            }
//            if (!object.isStatic) {
//                continue;
//            }
            netCards.add(object);
        }

        for (int j = 0; j < aggregations.size(); j++) {
            cardBean = new NetConfigInformationDetail();
            cardBean.setIsused(false);
            cardBean.setAddress(aggregations.get(j).getAddress());
            cardBean.setGateway(aggregations.get(j).getGateway());
            cardBean.setGroupName(aggregations.get(j).getGroupName());
            cardBean.setIsEnable(aggregations.get(j).isIsEnable());
            cardBean.setIsStatic(aggregations.get(j).isIsStatic());
            cardBean.setName(aggregations.get(j).getName());
            String subnet = "";
            if (aggregations.get(j).getNetmask() != null && !aggregations.get(j).getNetmask().equals("")) {
                subnet = ModifyCardBean.netBitsToSubnet32(aggregations.get(j).getNetmask());
            } else {
                subnet = "";
            }
            cardBean.setNetmask(subnet);
            cardList.add(cardBean);

        }

        for (int k = 0; k < netCards.size(); k++) {
            cardBean = new NetConfigInformationDetail();
            cardBean.setIsused(false);
            cardBean.setAddress(netCards.get(k).getAddress());
            cardBean.setAggregationName(netCards.get(k).getAggregationName());
            cardBean.setGateway(netCards.get(k).getGateway());
            cardBean.setGroupName(netCards.get(k).getGroupName());
            cardBean.setIsEnable(netCards.get(k).isIsEnable());
            cardBean.setIsFCoE(netCards.get(k).isIsFCoE());
            cardBean.setIsStatic(netCards.get(k).isIsStatic());
            cardBean.setName(netCards.get(k).getName());
            String subnet = "";
            if (netCards.get(k).getNetmask() != null && !netCards.get(k).getNetmask().equals("")) {
                subnet = ModifyCardBean.netBitsToSubnet32(netCards.get(k).getNetmask());
            } else {
                subnet = "";
            }
            cardBean.setNetmask(subnet);
            cardList.add(cardBean);

        }
    }

    public void change() {
        testList = new ArrayList<IPMPAddress>();
        selectedCards = new ArrayList<NetConfigInformation>();
        for (int j = 0; j < cardList.size(); j++) {
            if (cardList.get(j).isused) {
                selectedCards.add(cardList.get(j));
            }
        }
        if (selectedCards != null) {
            SystemOutPrintln.print_common("selectedCards.size()=" + selectedCards.size());
            for (int i = 0; i < selectedCards.size(); i++) {
                testList.add(new IPMPAddress(selectedCards.get(i).name, selectedCards.get(i).address, selectedCards.get(i).netmask, ""));
            }
        }
    }

    public List<NetConfigInformationDetail> getCardList() {
        return cardList;
    }

    public void setCardList(List<NetConfigInformationDetail> cardList) {
        this.cardList = cardList;
    }

    public NetConfigInformation getCardBean() {
        return cardBean;
    }

    public void setCardBean(NetConfigInformationDetail cardBean) {
        this.cardBean = cardBean;
    }

    public CreateIPMPModel getCardModel() {
        return cardModel;
    }

    public void setCardModel(CreateIPMPModel cardModel) {
        this.cardModel = cardModel;
    }

    public List<NetConfigInformation> getSelectedCards() {
        return selectedCards;
    }

    public void setSelectedCards(List<NetConfigInformation> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public String getIpmpGroupName() {
        return ipmpGroupName;
    }

    public void setIpmpGroupName(String ipmpGroupName) {
        this.ipmpGroupName = ipmpGroupName;
    }

    public List<IPMPAddress> getDataList() {
        return dataList;
    }

    public void setDataList(List<IPMPAddress> dataList) {
        this.dataList = dataList;
    }

    public IPMPAddress getSelectedDataAddress() {
        return selectedDataAddress;
    }

    public void setSelectedDataAddress(IPMPAddress selectedDataAddress) {
        this.selectedDataAddress = selectedDataAddress;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public boolean isIstarget() {
        return istarget;
    }

    public void setIstarget(boolean istarget) {
        this.istarget = istarget;
    }

    public String getTargetIP() {
        return targetIP;
    }

    public void setTargetIP(String targetIP) {
        this.targetIP = targetIP;
    }

    public void deleteDataAddress() {

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).ip.equals(selectedDataAddress.ip)) {
                dataList.remove(i);
            }
        }
    }

    public String toModifyTestAddress() {

        String param = "nicname=" + selectedTestAddress.nic + "&" + "ip=" + selectedTestAddress.ip + "&" + "netmask=" + selectedTestAddress.netmask;
        return "ipmp_modify_testaddress?faces-redirect=true&amp;" + param;
//        IPMPAddress ipmp = new IPMPAddress(selectedTestAddress.nic, selectedTestAddress.ip, selectedTestAddress.netmask, "");
//        return "ipmp_modify_testaddress?faces-redirect=true";
    }

    public List<IPMPAddress> getTestList() {
        return testList;
    }

    public void setTestList(List<IPMPAddress> testList) {
        this.testList = testList;
    }

    public IPMPAddress getSelectedTestAddress() {
        return selectedTestAddress;
    }

    public void setSelectedTestAddress(IPMPAddress selectedTestAddress) {
        this.selectedTestAddress = selectedTestAddress;
    }

    public boolean isNottargetIP() {
        return nottargetIP;
    }

    public void setNottargetIP(boolean nottargetIP) {
        this.nottargetIP = nottargetIP;
    }

    public void isnottargetIP() {
        if (ischeck) {
            if (istarget) {
                nottargetIP = false;
            } else {
                nottargetIP = true;
            }
        }
        if (!ischeck) {
            nottargetIP = true;
        }
    }

    public String cancleIPMP() {
        init();
        return "system_network?faces-redirect=true&amp;accordionActive1=2";

    }
    
    public String createIPMP(String groupName) {
        if (null == ipmpGroupName || "".equals(ipmpGroupName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ipmpnull"), ""));
            return null;
        }
       

//         System.out.println("ipmpGroupName="+ipmpGroupName);
//        if (ipmpGroupName.length() != 5) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ipmpno"), ""));
//            return null;
//        }
//        if (!ValidateUtility.checkName(ipmpGroupName)) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ipmpno"), ""));
//            return null;
//        }

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        IpmpGroupInformation[] ipmpDroups = common.getIPMPGroupInformation();
        String stateStr = "";
        if (ipmpDroups != null && ipmpDroups.length > 0) {
            for (int i = 0; i < ipmpDroups.length; i++) {
                if (ipmpGroupName.trim().equalsIgnoreCase(ipmpDroups[i].getGroupName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ipmpexit"), ""));
                    return null;
                }
            }
        }


//        selectedCards = selectes;
        selectedCards = new ArrayList<NetConfigInformation>();
        for (int j = 0; j < cardList.size(); j++) {
            if (cardList.get(j).isused) {
                selectedCards.add(cardList.get(j));
            }
        }
        int selectCount = selectedCards.size();
        if (selectCount < 2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "mintwo"), ""));
            return null;
        }
        if(dataList.size()<1){
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "minone"), ""));
            return null;
        }
        if (ischeck && istarget) {
            if (null == targetIP || "".equals(targetIP)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_null"), ""));
                return null;
            }
            if (!ValidateUtility.checkIP(targetIP)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_no"), ""));
                return null;
            }
        }
        if (ischeck) {
            if (testList != null && testList.size() > 0) {

                for (int m = 0; m < testList.size(); m++) {
                    if (testList.get(m).ip == null || testList.get(m).netmask == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "test_ipsub_null"), ""));
                        return null;
                    }

                    if (testList.get(m).ip.equals("") || testList.get(m).netmask.equals("")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "test_ipsub_null"), ""));
                        return null;
                    }

               

            }
        }
        }

        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String currentIP = request.getLocalAddr();
//        boolean isStatic = true;
        isCurrentIP = false;
        for (int i = 0; i < selectCount; i++) {
            Object temp = selectedCards.get(i);
//            if (isStatic) {//如果有DHCP，便不再判断
//                if (temp instanceof NetConfigInformation) {
//                    isStatic = ((NetConfigInformation) temp).isStatic;
//                }
//                if (temp instanceof AggregationInformation) {
//                    isStatic = ((AggregationInformation) temp).isIsStatic();
//                }
//            }
            if (!isCurrentIP) {//如果是当前IP，便不再判断
                if (temp instanceof NetConfigInformation) {
                    isCurrentIP = currentIP.equals(((NetConfigInformation) temp).address);
                }
                if (temp instanceof AggregationInformation) {
                    isCurrentIP = currentIP.equals(((AggregationInformation) temp).getAddress());
                }
            }

//            if (isCurrentIP && (!isStatic)) {
//                System.out.println("33333333333331111111111111");
//                break;
//            }
             if (isCurrentIP) {
                break;
            }
        }

//        if (!isStatic && isCurrentIP) {
//            returnStr = null;
//            RequestContext.getCurrentInstance().execute("two.show()");
//            return null;
//        } else {

//            if (!isStatic) {
//                returnStr = null;
//                RequestContext.getCurrentInstance().execute("card.show()");
//                return null;
//            }
            if (isCurrentIP) {
                returnStr = null;
                RequestContext.getCurrentInstance().execute("connect.show()");
                return null;
            }
//        }

        createIPMP_real();



//        for (int i = 0; i < selectedCards.length; i++) {
//            String subnet = selectedCards[i].getNetmask();
//            if (subnet != null && !subnet.equals("")) {
//                selectedCards[i].setNetmask(ModifyCardBean.sub32ToNetBits(subnet) + "");
//            } else {
//                selectedCards[i].setNetmask("");
//            }
//        }
//        System.out.println("ipmpGroupName="+ipmpGroupName+",selectedCards="+selectedCards.length);
//        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
//        boolean creatIPMP = common.createIPMPGroup(ipmpGroupName,selectedCards);
//         String returnStr = null;
//        if(creatIPMP == false){
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "创建聚合",  "失败。"));
//            returnStr = null;
//        }else{
//            returnStr = "system_network?faces-redirect=true";
//        }


        return returnStr;

    }

    public String createIPMP_real() {
        for (int i = 0; i < selectedCards.size(); i++) {
            String subnet = selectedCards.get(i).getNetmask();
            if (subnet != null && !subnet.equals("")) {
                selectedCards.get(i).setNetmask(ModifyCardBean.sub32ToNetBits(subnet) + "");
            } else {
                selectedCards.get(i).setNetmask("");
            }
        }
        SystemOutPrintln.print_common("ipmpGroupName=" + ipmpGroupName + ",selectedCards=" + selectedCards.size());
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        NetConfigInformation[] dataAddresses = null;
        if (dataList != null && dataList.size() > 0) {
            dataAddresses = new NetConfigInformation[dataList.size()];
            NetConfigInformation net = null;
            for (int k = 0; k < dataList.size(); k++) {
                net = new NetConfigInformation();
                net.setAddress(dataList.get(k).ip);
                net.setNetmask(ModifyCardBean.sub32ToNetBits(dataList.get(k).netmask) + "");
                if (dataList.get(k).getGateway() == null) {
                    net.setGateway("");
                } else {
                    net.setGateway(dataList.get(k).gateway);
                }
                dataAddresses[k] = net;
                SystemOutPrintln.print_common("dataAddress dataAddresses[k].address=" + dataAddresses[k].address + " ,dataAddresses[k].netmask =" + dataAddresses[k].netmask + " , dataAddresses[k].gateway=" + dataAddresses[k].gateway);

            }
        }

        NetConfigInformation[] testAddresses = null;
        if (testList != null && testList.size() > 0) {
            testAddresses = new NetConfigInformation[testList.size()];
            NetConfigInformation net = null;
            for (int m = 0; m < testList.size(); m++) {
                net = new NetConfigInformation();
                net.setName(testList.get(m).nic);
                net.setAddress(testList.get(m).ip);
                if(testList.get(m).netmask== null || testList.get(m).netmask.equals("")){
                }else{
                    net.setNetmask(ModifyCardBean.sub32ToNetBits(testList.get(m).netmask) + "");
                }
                

                testAddresses[m] = net;
                SystemOutPrintln.print_common("dataAddress testAddresses[m].name=" + testAddresses[m].name + " ,testAddresses[m].address =" + testAddresses[m].address + " , testAddresses[m].netmask=" + testAddresses[m].netmask);

            }
        }
//        boolean creatIPMP = common.createIPMPGroup(ipmpGroupName, selectedCards.);
        if (!istarget) {
            targetIP = "";
        }
        SystemOutPrintln.print_common("dataAddress ipmpGroupName=" + ipmpGroupName + " ,ischeck =" + ischeck + " , targetIP=" + targetIP);
        boolean creatIPMP = common.createIPMPGroup(ipmpGroupName, ischeck, dataAddresses, testAddresses, targetIP);

        if (creatIPMP == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "creatipmp_fail"), ""));
            returnStr = null;
        } else {
             init();
            returnStr = "system_network?faces-redirect=true&amp;accordionActive1=2";
        }
        return returnStr;
    }
    
     public String toAddDataAddress() {
         String netmask = "";
         String gateway = "";
         boolean defaultgateway = true;
         boolean cantuse = false;

         if (dataList != null && dataList.size() > 0) {
             netmask = dataList.get(0).getNetmask();
             cantuse = true;
             if (dataList.get(0).getGateway().equalsIgnoreCase("")) {
                 defaultgateway = true;
             } else {
                 defaultgateway = false;
                 gateway = dataList.get(0).getGateway();
             }
         }
         String param = "netmask=" + netmask + "&" + "gateway=" + gateway + "&" + "defaultgateway=" + defaultgateway + "&" + "cantuse=" + cantuse;
         return "ipmp_add_dataaddress?faces-redirect=true&amp;" + param;
    }

    public String listAdd(String ip, String netmarsk, boolean defaultgateway, String gateway) {
        String addbasename = "common.ipmp_add_dataaddress";
        if (null == ip || "".equals(ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "ip_null"), ""));
            return null;
        }
        if (!ValidateUtility.checkIP(ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "ip_no"), ""));
            return null;
        }
        if (null == netmarsk || "".equals(netmarsk)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "subnet_null"), ""));
            return null;
        }
        if (!ValidateUtility.checkIP(netmarsk)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "subnet_no"), ""));
            return null;
        }
        if (!defaultgateway) {
            if (null == gateway || "".equals(gateway)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "gateway_null"), ""));
                return null;
            }
            if (!ValidateUtility.checkIP(gateway)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "gateway_no"), ""));
                return null;
            }
        }
        if (defaultgateway) {
            gateway = "";
        }
        if (dataList != null && dataList.size() > 0) {
            for (int k = 0; k < dataList.size(); k++) {
                if (dataList.get(k).ip.equals(ip)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "ipexist"), ""));
                    return null;
                }
            }
            if (!dataList.get(0).netmask.equals(netmarsk)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "notsame_netmask"), ""));
                return null;
            }
            if (!dataList.get(0).gateway.equals(gateway)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(addbasename, "notsame_gateway"), ""));
                return null;
            }

        }


        boolean add = dataList.add(new IPMPAddress(ip, netmarsk, gateway));

        if (!add) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("AddIPFailed"), ""));
            return null;
        }

        return "network_createipmp?faces-redirect=true";

    }

    public String modify(String nic, String ip, String netmarsk) {
        SystemOutPrintln.print_common("nic=" + nic);
        SystemOutPrintln.print_common("ip=" + ip);
        SystemOutPrintln.print_common("netmarsk=" + netmarsk);
        if (null == ip || "".equals(ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_null"), ""));
            return null;
        }
        if (!ValidateUtility.checkIP(ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ip_no"), ""));
            return null;
        }
        if (null == netmarsk || "".equals(netmarsk)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "subnet_null"), ""));
            return null;
        }
        if (!ValidateUtility.checkIP(netmarsk)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "subnet_no"), ""));
            return null;
        }

        SystemOutPrintln.print_common("testList size=" + testList.size());

        for (IPMPAddress address : testList) {
            if (address.nic.equals(nic)) {
                address.setIp(ip);
                address.setNetmask(netmarsk);
            }
        }


        return "network_createipmp?faces-redirect=true";

    }
}
