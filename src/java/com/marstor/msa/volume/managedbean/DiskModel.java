/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.volume.model.Disk;
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
@ManagedBean(name = "diskModel")
@RequestScoped
public class DiskModel extends ListDataModel<Disk> implements SelectableDataModel<Disk>, Serializable   {

    /**
     * Creates a new instance of DiskModel
     */
    public DiskModel() {
    }

    public DiskModel(List<Disk> data) {
        super(data);
    }
    
    @Override
    public Object getRowKey(Disk t) {
        return t.diskName;
    }

    @Override
    public Disk getRowData(String rowKey) {
        List<Disk> disks = (List<Disk>) getWrappedData();

        for (Disk disk : disks) {
            if (disk.getDiskName().equals(rowKey)) {
                return disk;
            }
        }
        return null;
    }
    
    



   
}
