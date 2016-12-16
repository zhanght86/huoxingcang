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
public class RemoteInitiatorDataModel extends ListDataModel<RemoteInitiator> implements SelectableDataModel<RemoteInitiator>, Serializable {

    public RemoteInitiatorDataModel(List<RemoteInitiator> list) {
        super(list);
    }

    public RemoteInitiatorDataModel() {
    }

    @Override
    public Object getRowKey(RemoteInitiator initiator) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return initiator.getName();
    }

    @Override
    public RemoteInitiator getRowData(String name) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<RemoteInitiator> initiators = (List<RemoteInitiator>) getWrappedData();

        for (RemoteInitiator initiator : initiators) {
            if (initiator.getName().equals(name)) {
                return initiator;
            }
        }

        return null;
    }

    public void addInitiator(RemoteInitiator initiator) {
        List<RemoteInitiator> initiators = (List<RemoteInitiator>) getWrappedData();
        initiators.add(initiator);
    }

    public void setInitiatorList(List<RemoteInitiator> initiators) {
        this.setWrappedData(initiators);
    }
}
