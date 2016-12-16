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
@ManagedBean(name = "virtualDiskNameModel")
@RequestScoped
public class VirtualDiskNameModel  extends ListDataModel<VirtualDiskName> implements SelectableDataModel<VirtualDiskName>, Serializable {

    /**
     * Creates a new instance of VirtualDiskNameModel
     */
    public VirtualDiskNameModel() {
    }
      public VirtualDiskNameModel(List<VirtualDiskName> data) {
        super(data);
    }


    @Override
    public Object getRowKey(VirtualDiskName vd) {
         return vd.name;
    }

    @Override
    public VirtualDiskName getRowData(String rowKey) {
          List<VirtualDiskName> vds = (List<VirtualDiskName>) getWrappedData();
        
        for(VirtualDiskName vd : vds) {
            if(vd.getName().equals(rowKey))
                return vd;
        }
        
        return null;
    }
   
}
