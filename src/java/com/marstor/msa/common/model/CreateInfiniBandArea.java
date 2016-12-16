/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.InfinibandPartition;
import com.marstor.msa.common.managedbean.ModifyCardBean;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
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
@ManagedBean(name = "createInfiniBandArea")
@ViewScoped
public class CreateInfiniBandArea implements Serializable{
    public String ib ;  //IB卡值
    public List<String> ibList; //卷组列表
    public String pkey = "FFFF";
    private MSAResource res = new MSAResource();
    private String basename = "common.system_network_creatinfiniband";
    public String net;
    public NetWorkCard cardBean;
    /**
     * Creates a new instance of CreateInfiniBandArea
     */
    public CreateInfiniBandArea() { 
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        mediaPath = "/" + request.getParameter("name");
         ib = request.getParameter("ib");
//        ibList = new ArrayList(); //cpu列表
//        addIBList();
    }

//     public void addIBList(){  
//            ibList.add("SYSVOL");
//            ibList.add("YUTEST");
//            ibList.add("TEST");
//      
//    }
     
    public List<String> getIbList() {
        return ibList;
    }

    public void setIbList(List<String> ibList) {
        this.ibList = ibList;
    }

    public String getIb() {
        return ib;
    }

    public void setIb(String ib) {
        this.ib = ib;
    }

    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }
    
//    public String setIBValue(){
//        FacesContext context = FacesContext.getCurrentInstance();
//        String ibName = context.getExternalContext().getRequestParameterMap().get("IBName");
//        ib = ibName;
//       
//
//      
//        return "system_network_creatinfiniband?faces-redirect=true";
//    }
    
    public String creatInfiniBandArea(String ib,String pkey){
        if (!checkNull()) {
            return null;
        }
        
        ArrayList<InfinibandPartition> exists;
         CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        InfinibandPartition[] areas = common.getAllInfinibanddPartition();
         if (areas == null || areas.length <= 0) {
            exists = null;
        }else{
             exists = new ArrayList<InfinibandPartition>();
             exists.addAll(Arrays.asList(areas));
         }

        int n = 0;
        String card = ib;
        ArrayList nums = new ArrayList();
        if (exists != null) {
            for (int i = 0; i < exists.size(); i++) {
                nums.add(exists.get(i).name.substring(exists.get(i).name.length() - 1));
            }

            for (n = 0; n < exists.size(); n++) {
                if (!nums.contains(String.valueOf(n))) {
                    break;
                }
            }
        }
        
        SystemOutPrintln.print_common("card="+card+",pkey="+pkey+",part="+card+ ".p" + n);
        boolean creatArea = common.createIBPart(card, pkey, card+ ".p" + n);
           String returnStr = null;
        if(creatArea == false){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "creatarea_fail"),  ""));
            returnStr = null;
        }else{
            net = card+ ".p" + n;
             SystemOutPrintln.print_common("net="+net);
//             CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
//        NetConfigInformation[] nets = common.getNetConfig();
//        if (nets != null && nets.length > 0) {
//           
//            for (int i = 0; i < nets.length; i++) {
//                if(nets[i].name.equals(net)){
//                    cardBean = new NetWorkCard();
//                    System.out.println("#################nets[i].name="+nets[i].name);
//                cardBean.setCardName(nets[i].name);
//                String ip= nets[i].address;
//                if(nets[i].netmask != null && !nets[i].netmask.equals("")){
//                    ip = ip + "/" + nets[i].netmask;
//                }
//                cardBean.setIp(ip);
//                cardBean.setSubnet(nets[i].netmask);
//                cardBean.setAggregation(nets[i].aggregationName);
//                cardBean.setIpmpGroupName(nets[i].groupName);
//                 if ((cardBean.getAggregation() != null && !cardBean.getAggregation().equals("")) || (cardBean.getIpmpGroupName() != null && !cardBean.getIpmpGroupName().equals(""))) {
//                    cardBean.setNotEdit(true);
//                } else {
//                    cardBean.setNotEdit(false);
//                }
//
////                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
////                HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
////                HttpSession session = request.getSession();
////                SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
////                int userType = user.getType();
////                if (userType != 2) {
////                    cardBean.setNotEdit(true);
////                }
//
//                cardBean.setPCoECard(nets[i].isFCoE);
//                cardBean.setEnable(nets[i].isEnable);
//                cardBean.setGateway(nets[i].gateway);
//
//                 String dhcp = "";
//                  if (nets[i].address.length() != 0) {
//                    dhcp = nets[i].isStatic ? res.get(basename, "no") : res.get(basename, "yes");
//                }
//                cardBean.setState(dhcp);
//                break;
//                }
//
//            }
//        }
            RequestContext.getCurrentInstance().execute("setip.show()");
            return null;
//            pkey = "FFFF";
//            returnStr = "system_network?faces-redirect=true&amp;tabViewActive=2";
        }
        

        return returnStr;
    }

    public String toModifyIP() {
        
        String param = "cardName=" + this.net;
        return "network_modifycard?faces-redirect=true&amp;" + param;

    }
    
    public String toCancle(){
        return "system_network?faces-redirect=true&amp;tabViewActive=2";
    }
    
      private boolean checkNull() {
        if (pkey == null || pkey.equals("")) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "pkeynull"),  ""));
           
            return false;
        }
        return true;
    }
    
}
