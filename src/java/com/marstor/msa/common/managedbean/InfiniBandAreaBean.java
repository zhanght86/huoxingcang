/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.InfinibandPartition;
import com.marstor.msa.common.bean.IpmpGroupInformation;
import com.marstor.msa.common.bean.NetConfigInformation;
import com.marstor.msa.common.model.NetWorkCard;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@ManagedBean(name = "infiniBandAreaBean")
@ViewScoped
public class InfiniBandAreaBean implements Serializable {
    List<InfinibandPartition> areaList;
    InfinibandPartition area;
    public InfinibandPartition selectedInfo;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_network";
    public boolean hasIPMPConfig = false;
    public List<NICAndAggrForJudageDHCP> listNICAndAggr = null;
    public String partName;
    public NetWorkCard selectCard;
    public String ipselect;
    boolean bCurrentIP = false;
    String returnStr = null;
    String tip = "";
    public String deleteArea;
    public String deleteArea_forbiddenIP;
    /**
     * Creates a new instance of InfiniBandAreaImpl
     */
    public InfiniBandAreaBean() {
        deleteArea = res.get(basename, "deletearea");
        deleteArea_forbiddenIP = res.get(basename, "deletearea_forbiddenip");
        getInfiniBancars();
    }
    public void getInfiniBancars(){
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        InfinibandPartition[] areas = common.getAllInfinibanddPartition();
         if (areas == null || areas.length <= 0) {
            areaList = null;
            return;
        }
        areaList = new ArrayList<InfinibandPartition>();
        this.areaList.addAll(Arrays.asList(areas));
        
        
        
        
//        areaList = new ArrayList();
//        
//        area = new InfiniBandArea();
//        area.setAreaName("ibp0.p1");
//        area.setState("up");
//        area.setBelongCard("ibp2");
//        area.setPkey("FFFF");
//        areaList.add(area);
//        
//        area = new InfiniBandArea();
//        area.setAreaName("ibp0.p2");
//        area.setState("down");
//        area.setBelongCard("ibp3");
//        area.setPkey("FFFF");
//        areaList.add(area);
        
    }
    
    
    public void deteteAreaList() {
         SystemOutPrintln.print_common("删除分区=" + selectedInfo.getName());
        if (!selectedInfo.state.equalsIgnoreCase("unknown")) {
            deleteArea_forbiddenIP = res.get(basename, "deletearea_forbiddenip_left") + selectedInfo.name + res.get(basename, "deletearea_forbiddenip_right");
            RequestContext.getCurrentInstance().execute("deleteArea_forbiddenIP.show()");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "forbidden"), ""));
            return;
        } else {
            deleteArea = res.get(basename, "delete_left") + selectedInfo.name + res.get(basename, "delete_right");
            RequestContext.getCurrentInstance().execute("deleteArea.show()");
            return;
        }
    }
    public void deteteAreaList_real(){
//        selectedInfo = select;
       
        partName = selectedInfo.getName();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean deleteArea = common.deleteIBPart(partName);
        if (deleteArea == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deletearea_fail"), ""));
            return;
        }
        getInfiniBancars();
        
//        System.out.println("删除 infiniband="+selectedInfo.getName());
//         areaList.remove(selectedInfo);
        
    }
    
     public void deteteAreaList_forbiddenIP_real(){
         partName = selectedInfo.getName();
         this.confirmUseIp();
         this.listNICAndAggr();
         this.hasIPMPConfig();
         NetConfigInformation nic = new NetConfigInformation();

          SystemOutPrintln.print_common("selectCard.getCardName()=" + partName);
         nic.setAddress("");
         nic.setNetmask("");

         nic.setAggregationName(selectCard.getAggregation());
         nic.setGateway(selectCard.getGateway());
         nic.setGroupName(selectCard.getIpmpGroupName());
         nic.setIsFCoE(selectCard.isPCoECard());

         nic.setName(selectCard.getCardName());

         nic.setIsEnable(false);
         nic.setIsStatic(false);
        boolean iniIsStatic = false;  //禁止
        boolean modifyUser = !(nic.isIsStatic() == iniIsStatic);
         SystemOutPrintln.print_common("修改网卡bCurrentIP=" + bCurrentIP);

        //用于判断是否是最后一个静态IP
         SystemOutPrintln.print_common("nic.isStatic=" + nic.isStatic);
         SystemOutPrintln.print_common("nic.name=" + nic.name);
        if ((!nic.isStatic) && (!canModifyNICOrAggrToDHCP(nic.name))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "min_onestatic"), ""));
            return;
        }
         SystemOutPrintln.print_common("bCurrentIP=" + bCurrentIP);
        if (bCurrentIP && !nic.isEnable) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "using"), ""));
            return;
        }


        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();

        boolean modifyNic = common.setNetConfig(nic, modifyUser);

        if (modifyNic == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "modifynic_fail"), ""));
            returnStr = null;
        } else {
            deteteAreaList_real();
        }
         
     }
     
     public void confirmUseIp() {
         CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
          NetConfigInformation[] nets = common.getNetConfig();
        if (nets != null && nets.length > 0) {
           
            for (int i = 0; i < nets.length; i++) {
                if(nets[i].name.equals(partName)){
                    selectCard = new NetWorkCard();
                     SystemOutPrintln.print_common("nets[i].name="+nets[i].name);
                selectCard.setCardName(nets[i].name);
                String ip= nets[i].address;
                ipselect = ip;
                if(nets[i].netmask != null && !nets[i].netmask.equals("")){
                    ip = ip + "/" + nets[i].netmask;
                }
                selectCard.setIp(ip);
                selectCard.setSubnet(nets[i].netmask);
                selectCard.setAggregation(nets[i].aggregationName);
                selectCard.setIpmpGroupName(nets[i].groupName);
                 if ((selectCard.getAggregation() != null && !selectCard.getAggregation().equals("")) || (selectCard.getIpmpGroupName() != null && !selectCard.getIpmpGroupName().equals(""))) {
                    selectCard.setNotEdit(true);
                } else {
                    selectCard.setNotEdit(false);
                }

//                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//                HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//                HttpSession session = request.getSession();
//                SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
//                int userType = user.getType();
//                if (userType != 2) {
//                    cardBean.setNotEdit(true);
//                }

                selectCard.setPCoECard(nets[i].isFCoE);
                selectCard.setEnable(nets[i].isEnable);
                selectCard.setGateway(nets[i].gateway);

                 String dhcp = "";
                  if (nets[i].address.length() != 0) {
                    dhcp = nets[i].isStatic ? res.get(basename, "no") : res.get(basename, "yes");
                }
                selectCard.setState(dhcp);
                break;
                }

            }
        }
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        String ipStr = request.getLocalAddr();
        
        //是否为当前IP
        SystemOutPrintln.print_common("ipStr=" + ipStr);
        SystemOutPrintln.print_common("ipselect=" + ipselect);
        if (ipselect.equals(ipStr)) {
            bCurrentIP = true;
            tip = res.get(basename, "tip");
             SystemOutPrintln.print_common("talk dialog");
//         //弹出提示框
//         //   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "选择网卡正在被当前连接使用,",  "修改后必须重新登录。"));
        } else {
            tip = "";
        }
         SystemOutPrintln.print_common("bCurrentIP=" + bCurrentIP);

    }
      public void listNICAndAggr() {
        listNICAndAggr = new ArrayList<NICAndAggrForJudageDHCP>();//用于判断是否是最后一个静态IP

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        NetConfigInformation[] nets = common.getNetConfig();
        if (nets != null && nets.length > 0) {
            for (int i = 0; i < nets.length; i++) {
                 SystemOutPrintln.print_common("nets[i].groupName=" + nets[i].groupName);
                 SystemOutPrintln.print_common("nets[i].aggregationName=" + nets[i].aggregationName);
                 SystemOutPrintln.print_common("nets[i].isFCoE=" + nets[i].isFCoE);
                //用于判断是否是最后一个静态IP
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

    public void hasIPMPConfig() {


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
    
    public boolean canModifyNICOrAggrToDHCP(String NICOrAggrName) {
        if (null == NICOrAggrName || "".equals(NICOrAggrName)) {
            return false;
        }

        if (hasIPMPConfig) {
            return true;
        }
        //记录静态IP的个数
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
                //假设该网卡从静态改为动态
                if (nicORaggr.isStatic) {
                    staticIPCount--;
                    SystemOutPrintln.print_common("staticIPCount--=" + staticIPCount);
                } else {
                    //如果网卡原来就是DHCP，则允许它改为设为DHCP
                    return true;
                }
            }
        }
        SystemOutPrintln.print_common("staticIPCount=" + staticIPCount);
        //假设该网卡从静态改为动态后，静态IP的个数为0，则不可修改
        if (staticIPCount == 0) {
            return false;
        }

        return true;
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

    public List<InfinibandPartition> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<InfinibandPartition> areaList) {
        this.areaList = areaList;
    }

    public InfinibandPartition getArea() {
        return area;
    }

    public void setArea(InfinibandPartition area) {
        this.area = area;
    }

    public InfinibandPartition getSelectedInfo() {
        return selectedInfo;
    }

    public void setSelectedInfo(InfinibandPartition selectedInfo) {
        this.selectedInfo = selectedInfo;
    }

    public String getDeleteArea() {
        return deleteArea;
    }

    public void setDeleteArea(String deleteArea) {
        this.deleteArea = deleteArea;
    }

    public String getDeleteArea_forbiddenIP() {
        return deleteArea_forbiddenIP;
    }

    public void setDeleteArea_forbiddenIP(String deleteArea_forbiddenIP) {
        this.deleteArea_forbiddenIP = deleteArea_forbiddenIP;
    }

   
    
    public String creatInfiniBandArea(String key){
//        FacesContext context = FacesContext.getCurrentInstance();
//        String ibName = context.getExternalContext().getRequestParameterMap().get("IBName");
//        System.out.println("##########ibName=" + ibName);
////        String pKey = context.getExternalContext().getRequestParameterMap().get("PKeyValue");
//        String keValue = key;
//        System.out.println("##########PKeyValue=" + keValue);
//        area = new InfiniBandArea();
//        area.setAreaName(ibName + ".p1");
//        area.setState("up");
//        area.setBelongCard(ibName);
//        area.setPkey(keValue);
//        areaList.add(area);


        return "system_network?faces-redirect=true";
    }


    
}
