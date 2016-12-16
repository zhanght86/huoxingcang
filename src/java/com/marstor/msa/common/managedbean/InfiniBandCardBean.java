/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.InfinibandCard;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "infiniBandCardBean")
@ViewScoped
public class InfiniBandCardBean implements Serializable {

    List<InfinibandCard> infiniBanList;
    InfinibandCard card;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_network";
    public InfinibandCard selectedInf;
    /**
     * Creates a new instance of InfiniBandCardImpl
     */
    public InfiniBandCardBean() {
        getInfiniBancars();
    }
    
    public void getInfiniBancars(){
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        InfinibandCard[] cards = common.getAllInfinibandCard();

        if (cards == null || cards.length <= 0) {
            infiniBanList = null;
            return;
        }else{
            for(int i=0; i<cards.length; i++){
                String state = cards[i].getState();
                if(state.equalsIgnoreCase("UP")){
                    cards[i].setState(res.get(basename, "up"));
                }else if(state.equalsIgnoreCase("DOWN")){
                    cards[i].setState(res.get(basename, "down"));
                }
            }
        }
        infiniBanList = new ArrayList<InfinibandCard>();
        this.infiniBanList.addAll(Arrays.asList(cards));
        
//        infiniBanList = new ArrayList();
//        
//        card = new InfiniBandCard();
//        card.setStorageName("ibp0");
//        card.setState("up");
//        card.setPkeys("FFFF");
//        infiniBanList.add(card);
//        
//        card = new InfiniBandCard();
//        card.setStorageName("ibp1");
//        card.setState("down");
//        card.setPkeys("BFFA");
//        infiniBanList.add(card);
        
    }

    public List<InfinibandCard> getInfiniBanList() {
        return infiniBanList;
    }

    public void setInfiniBanList(List<InfinibandCard> infiniBanList) {
        this.infiniBanList = infiniBanList;
    }

    public InfinibandCard getSelectedInf() {
        return selectedInf;
    }

    public void setSelectedInf(InfinibandCard selectedInf) {
        this.selectedInf = selectedInf;
    }
    
      public String setIBValue(){
//        FacesContext context = FacesContext.getCurrentInstance();
//        String ibName = context.getExternalContext().getRequestParameterMap().get("IBName");
//        ib = ibName;
       
          String param = "ib=" + selectedInf.name;

          return "system_network_creatinfiniband?faces-redirect=true&amp;" + param;
    }


    
    
}
