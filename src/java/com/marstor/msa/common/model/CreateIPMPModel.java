/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.NetConfigInformation;
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
@ManagedBean(name = "createIPMPModel")
@RequestScoped
public class CreateIPMPModel  extends ListDataModel<NetConfigInformation> implements SelectableDataModel<NetConfigInformation>, Serializable  {
    public String iPMPGroupName;
    

    /**
     * Creates a new instance of CreateIPMPModel
     */
    public CreateIPMPModel() {
    }
    
     public CreateIPMPModel(List<NetConfigInformation> data) {
        super(data);
    }


    public Object getRowKey(NetConfigInformation net) {
         return net.name;
    }

    public NetConfigInformation getRowData(String rowKey) {
        List<NetConfigInformation> cards = (List<NetConfigInformation>) getWrappedData();
        
        for(NetConfigInformation car : cards) {
            if(car.name.equals(rowKey))
                return car;
        }
        return null;
    }
}
