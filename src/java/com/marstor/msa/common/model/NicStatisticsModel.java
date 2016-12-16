/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.NicStatistics;
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
@ManagedBean(name = "nicStatisticsModel")
@RequestScoped
public class NicStatisticsModel extends ListDataModel<NicStatistics> implements SelectableDataModel<NicStatistics>, Serializable {

    /**
     * Creates a new instance of NicStatisticsModel
     */
    public NicStatisticsModel() {
    }
    
     public NicStatisticsModel(List<NicStatistics> data) {
        super(data);
    }

    public Object getRowKey(NicStatistics nic) {
        return nic.name;
    }

    public NicStatistics getRowData(String rowKey) {
        List<NicStatistics> nics = (List<NicStatistics>) getWrappedData();
        
        for(NicStatistics nic : nics) {
            if(nic.getName().equals(rowKey))
                return nic;
        }
        
        return null;
    }
}
