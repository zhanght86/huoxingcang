/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

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
@ManagedBean(name = "iSOPathDataModel")
@RequestScoped
public class ISOPathDataModel extends ListDataModel<ISOPathBean> implements SelectableDataModel<ISOPathBean>, Serializable {

    /**
     * Creates a new instance of ISOPathDataModel
     */
    public ISOPathDataModel() {
    }
    
    public ISOPathDataModel(List<ISOPathBean> data) {
        super(data);
    }

    public Object getRowKey(ISOPathBean iso) {
         return iso.isoPath;
    }

    public ISOPathBean getRowData(String rowKey) {
       List<ISOPathBean> isos = (List<ISOPathBean>) getWrappedData();
        
        for(ISOPathBean iso : isos) {
            if(iso.getIsoPath().equals(rowKey))
                return iso;
        }
        
        return null;
    }
}
