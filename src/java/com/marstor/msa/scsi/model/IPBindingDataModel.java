/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;
import java.io.Serializable;
import java.util.List;
/**
 *
 * @author Administrator
 */
public class IPBindingDataModel extends ListDataModel<IPBinding> implements SelectableDataModel<IPBinding>, Serializable {

    public IPBindingDataModel(List<IPBinding> list) {
        super(list);
    }

    public IPBindingDataModel() {
    }

    @Override
    public Object getRowKey(IPBinding bind) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          return  bind.getIp();
    }

    @Override
    public IPBinding getRowData(String ip) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<IPBinding> binds = (List<IPBinding>) getWrappedData();
        
        for(IPBinding bind : binds) {
            if(bind.getIp().equals(ip))
                return bind;
        }
        return null;
    }
    public List<IPBinding> getBinds() {
        List<IPBinding> binds = (List<IPBinding>) getWrappedData();
        
        return  binds;
    } 
   public void setIPList(List<IPBinding> ipBinds) {
//        List<IPBinding> binds = (List<IPBinding>) getWrappedData();
//        binds = ipBinds;
       this.setWrappedData(ipBinds);
    } 
}
