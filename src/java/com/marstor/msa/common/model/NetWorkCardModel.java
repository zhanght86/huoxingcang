/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "netWorkCardModel")
@RequestScoped
public class NetWorkCardModel extends ListDataModel<NetWorkCard> implements SelectableDataModel<NetWorkCard>, Serializable  {

    /**
     * Creates a new instance of NetWorkCardModel
     */
    public NetWorkCardModel() {
    }
    
    public NetWorkCardModel(List<NetWorkCard> data) {
        super(data);
    }

    public Object getRowKey(NetWorkCard net) {
        return net.cardName;
    }

    public NetWorkCard getRowData(String rowKey) {
       List<NetWorkCard> cards = (List<NetWorkCard>) getWrappedData();
        
        for(NetWorkCard car : cards) {
            if(car.getCardName().equals(rowKey))
                return car;
        }
        return null;
    }
}
