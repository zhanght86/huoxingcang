/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.NetConfigInformation;
import com.marstor.msa.common.model.NetWorkCard;
import com.marstor.msa.common.model.NetWorkCardModel;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "netWorkCardBean")
@ViewScoped
public class NetWorkCardBean implements Serializable {

    public NetWorkCard cardBean;
    public List<NetWorkCard> cardList;
    private NetWorkCardModel cardModel;
    private NetWorkCard[] selectedCards;
    public NetWorkCard selectedCard;
    public List<NetWorkCard> cardCrAggrList;  //创建聚合时可以用的网卡
    private NetWorkCardModel cardCrAggrModel;  
    private String tabViewActive="";
    private String accordionActive1 = "";
    private MSAResource res = new MSAResource();
    private String basename = "common.network_createaggregate";
    
//     public List<NICAndAggrForJudageDHCP> listNICAndAggr = new ArrayList<NICAndAggrForJudageDHCP>();

    /**
     * Creates a new instance of NetWorkCardImpl
     */
    public NetWorkCardBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
         tabViewActive = request.getParameter("tabViewActive");
        if(tabViewActive==null || tabViewActive.equals("")) {
            tabViewActive = "0";
        }
        accordionActive1 = request.getParameter("accordionActive1");
        if (accordionActive1 == null || accordionActive1.equals("")) {
            accordionActive1 = "0";
        }
        myInit();   
        
    }

     public void myInit() {
//        listNICAndAggr.clear();//用于判断是否是最后一个静态IP
           SystemOutPrintln.print_common("initor card");
        addCardList();
        cardModel = new NetWorkCardModel(cardList);
        
        addcardCrAggrList();
        cardCrAggrModel = new NetWorkCardModel(cardCrAggrList);
     }
    public void addCardList() {
        cardList = new ArrayList();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        NetConfigInformation[] nets = common.getNetConfig();
        if (nets != null && nets.length > 0) {
           
            for (int i = 0; i < nets.length; i++) {
                cardBean = new NetWorkCard();
                cardBean.setCardName(nets[i].name);
                String ip= nets[i].address;
                if(nets[i].netmask != null && !nets[i].netmask.equals("")){
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
//                String dhcp = "是";
//                if(nets[i].isEnable == true){
//                    if( nets[i].isStatic== false){
//                        dhcp = "是";
//                    }else{
//                        dhcp = "否";
//                    }
//                }else{
//                    dhcp = "";
//                }
                 String dhcp = "";
                  if (nets[i].address.length() != 0) {
                    dhcp = nets[i].isStatic ? res.get(basename, "no") : res.get(basename, "yes");
                }
                cardBean.setState(dhcp);
                cardList.add(cardBean);
//                 //用于判断是否是最后一个静态IP
//                if ((!nets[i].isFCoE) && "".equals(nets[i].groupName) && "".equals(nets[i].aggregationName)) {
//                    NICAndAggrForJudageDHCP naafjd = new NICAndAggrForJudageDHCP();
//                    naafjd.NICOrAggrName = nets[i].name;
//                    naafjd.isNIC = true;
//                    naafjd.isStatic = nets[i].isStatic;
//                    listNICAndAggr.add(naafjd);
//                }
            }
        }

//        cardBean = new NetWorkCard();
//        cardBean.setCardName("e1000g3");
//        cardBean.setIp("");
//        cardBean.setSubnet("");
//        cardBean.setAggregation("aggr1");
//        cardBean.setIpmpGroupName("");
//        if ((cardBean.getAggregation() != null && !cardBean.getAggregation().equals("")) || (cardBean.getIpmpGroupName() != null && !cardBean.getIpmpGroupName().equals(""))) {
//            cardBean.setNotEdit(true);
//        } else {
//            cardBean.setNotEdit(false);
//        }
//        cardBean.setState("");
//        cardBean.setPCoECard("否");
//        cardBean.setEnable("否");
//        cardList.add(cardBean);

    }
    
    
    public void addcardCrAggrList() {
        cardCrAggrList = new ArrayList();
        if (cardList != null) {
            for (int i = 0; i < cardList.size(); i++) {
                if ((cardList.get(i).getAggregation() == null || cardList.get(i).getAggregation().equals("")) && (cardList.get(i).getIpmpGroupName() == null || cardList.get(i).getIpmpGroupName().equals(""))) {
                    if(cardList.get(i).cardName.startsWith("ib")){
                        continue;
                    }
                    cardCrAggrList.add(cardList.get(i));
                }
            }
        }


    }
    
        public String toModifyIP() {
        
        String param = "cardName=" + selectedCard.cardName;
         SystemOutPrintln.print_common("cardName="+selectedCard.cardName);
        return "network_modifycard?faces-redirect=true&amp;" + param;

    }

    public NetWorkCard getCardBean() {
        return cardBean;
    }

    public void setCardBean(NetWorkCard cardBean) {
        this.cardBean = cardBean;
    }

    public List<NetWorkCard> getCardList() {
        return cardList;
    }

    public void setCardList(List<NetWorkCard> cardList) {
        this.cardList = cardList;
    }

    public NetWorkCardModel getCardModel() {
        return cardModel;
    }

    public void setCardModel(NetWorkCardModel cardModel) {
        this.cardModel = cardModel;
    }

    public NetWorkCard[] getSelectedCards() {

        return selectedCards;
    }

    public void setSelectedCards(NetWorkCard[] selectedCards) {

        this.selectedCards = selectedCards;
    }

    public NetWorkCard getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(NetWorkCard selectedCard) {
        this.selectedCard = selectedCard;
    }

    public List<NetWorkCard> getCardCrAggrList() {
        return cardCrAggrList;
    }

    public void setCardCrAggrList(List<NetWorkCard> cardCrAggrList) {
        this.cardCrAggrList = cardCrAggrList;
    }

    public NetWorkCardModel getCardCrAggrModel() {
        return cardCrAggrModel;
    }

    public void setCardCrAggrModel(NetWorkCardModel cardCrAggrModel) {
        this.cardCrAggrModel = cardCrAggrModel;
    }

    public void handle() {
         SystemOutPrintln.print_common("selectedCards=" + selectedCards.length);
        for (int i = 0; i < selectedCards.length; i++) {
             SystemOutPrintln.print_common("selectedCard=" + selectedCards[i].getCardName());
        }

//         return "system_network?faces-redirect=true";

    }
    
//       public class NICAndAggrForJudageDHCP {
//
//        /** isNIC is true : NIC Name;<br/>  isNIC is false: Aggregation Name */
//        public String NICOrAggrName = "";
//        /** true : NIC;<br/>  false: Aggr*/
//        public boolean isNIC = false;
//        /** true : Static IP;<br/> false: DHCP*/
//        public boolean isStatic = false;
//    }

    public String getAccordionActive1() {
        return accordionActive1;
    }

    public void setAccordionActive1(String accordionActive1) {
        this.accordionActive1 = accordionActive1;
    }

    public String getTabViewActive() {
        return tabViewActive;
    }

    public void setTabViewActive(String tabViewActive) {
        this.tabViewActive = tabViewActive;
    }
    
}
