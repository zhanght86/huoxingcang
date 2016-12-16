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
@ManagedBean(name = "diskPathModel")
@RequestScoped
public class DiskPathModel extends ListDataModel<ClearHDInfo> implements SelectableDataModel<ClearHDInfo>, Serializable  {

    /**
     * Creates a new instance of DiskPathModel
     */
    public DiskPathModel() {
    }
    public DiskPathModel(List<ClearHDInfo> data) {
        super(data);
    }

    @Override
    public Object getRowKey(ClearHDInfo hd) {
        return hd.hdName;
    }

    @Override
    public ClearHDInfo getRowData(String rowKey) {
        List<ClearHDInfo> hds = (List<ClearHDInfo>) getWrappedData();
        
        for(ClearHDInfo hd : hds) {
            if(hd.getHdName().equals(rowKey))
                return hd;
        }
        
        return null;
    }
}
