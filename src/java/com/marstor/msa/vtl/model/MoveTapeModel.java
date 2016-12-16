/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

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
@ManagedBean(name = "moveTapeModel")
@RequestScoped
public class MoveTapeModel extends ListDataModel<DeleteTapeWeb> implements SelectableDataModel<DeleteTapeWeb>, Serializable  {

       
    public MoveTapeModel() {
    }
    
     public MoveTapeModel(List<DeleteTapeWeb> data) {
        super(data);
    }
    
    @Override
    public Object getRowKey(DeleteTapeWeb t) {
         return t.name;
    }

    @Override
    public DeleteTapeWeb getRowData(String rowKey) {
        List<DeleteTapeWeb> dels = (List<DeleteTapeWeb>) getWrappedData();

        for (DeleteTapeWeb del : dels) {
            if (del.name.equals(rowKey)) {
                return del;
            }
        }
        return null;
    }
}
