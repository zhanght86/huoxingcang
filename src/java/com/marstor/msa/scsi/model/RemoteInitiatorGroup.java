/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

import java.io.Serializable;
import org.primefaces.model.SelectableDataModel;
import java.util.ArrayList;
import javax.faces.model.ListDataModel;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
public class RemoteInitiatorGroup implements Serializable {
    
    private String groupName;
    private String style;
    private String binding = "";
    private ArrayList<RemoteInitiator> initiators = new ArrayList<RemoteInitiator>();
    private ArrayList<RemoteInitiator> avalibleInitiators = new ArrayList<RemoteInitiator>();
    private RemoteInitiator[] selectedInitiator;
    private ArrayList<String> initiatorNames = new ArrayList<String>();//组中所有成员
    private ArrayList<String> avalibleInitiatorNames = new ArrayList<String>();
    //private DualListModel<RemoteInitiator> model = new DualListModel<RemoteInitiator>(avalibleInitiators, initiators);
    private DualListModel<String> model = new DualListModel<String>(avalibleInitiatorNames, initiatorNames);
    private RemoteInitiatorDataModel dataModel;
    private String guid = "";
    public static final String disk = "disk";
    public static final String vtl = "vtl";
    
    public RemoteInitiatorGroup(String groupName, ArrayList<String> initiatorNames) {
        this.groupName = groupName;
        if (groupName.startsWith("fc")) {
            for (String initiator : initiatorNames) {
                this.initiatorNames.add(initiator.toUpperCase());
            }
        }else {
            this.initiatorNames = initiatorNames;
        }

        //dataModel = new RemoteInitiatorDataModel(avalibleInitiators);
    }
    
    public RemoteInitiatorGroup() {
    }
    
    public RemoteInitiatorGroup(String groupName) {
        this.groupName = groupName;
    }
    
    public RemoteInitiatorGroup(String groupName, String style) {
        this.groupName = groupName;
        this.style = style;
    }
    
    public String getGuid() {
        return guid;
    }
    
    public void setGuid(String guid) {
        this.guid = guid;
    }
    
    public String getStyle() {
        return style;
    }
    
    public void setStyle(String style) {
        this.style = style;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public ArrayList<String> getInitiatorNames() {
        return initiatorNames;
    }
    
    public void setInitiatorNames(ArrayList<String> initiatorNames) {
        this.initiatorNames = initiatorNames;
    }
    
    public ArrayList<String> getAvalibleInitiatorNames() {
        return avalibleInitiatorNames;
    }
    
    public void setAvalibleInitiatorNames(ArrayList<String> avalibleInitiatorNames) {
        this.avalibleInitiatorNames = avalibleInitiatorNames;
    }
    
    public void addInitiator(RemoteInitiator initiator) {
        this.initiators.add(initiator);
        // initiator.setBelongGroupOrNot(true);
        initiator.addAttributeGroupName(this.groupName);
        //this.addToAvaileble(initiator);
        
        
    }

    public void addToInitiatorNames(String initiator) {
        this.initiatorNames.add(initiator);
        //initiator.setBelongGroupOrNot(true);
        //this.addToAvaileble(initiator);

    }

    public void addToAvaileble(RemoteInitiator initiator) {
        this.avalibleInitiators.add(initiator);
        
    }
    
    public void addToAvailebleNames(String initiator) {
        this.avalibleInitiatorNames.add(initiator);
        
    }

    public void addInitiatorToModel(RemoteInitiator initiator) {
        // this.dataModel.addInitiator(initiator);
    }

    public RemoteInitiator[] getSelectedInitiator() {
        return selectedInitiator;
    }
    
    public void setSelectedInitiator(RemoteInitiator[] selectedInitiator) {
        this.selectedInitiator = selectedInitiator;
    }
    
    public String getBinding() {
        return binding;
    }
    
    public void setBinding(String binding) {
        this.binding = binding;
    }

//    @Override
//    public Object getRowKey(RemoteInitiator t) {
//        return t.getName();
//    }
//
//    @Override
//    public RemoteInitiator getRowData(String name) {
//        //List<RemoteInitiator> cars = (List<RemoteInitiator>) getWrappedData();   
//
//        for (RemoteInitiator initiator : this.initiators) {
//            if (initiator.getName().equals(name)) {
//                return initiator;
//            }
//        }
//
//        return null;
//    }
    public DualListModel<String> getModel() {
        return model;
    }
    
    public void setModel(DualListModel<String> model) {
        this.model = model;
    }

//    public void initModel() {
//        for (int i = 0; i < initiators.size(); i++) {
//            this.initiatorNames.add(initiators.get(i).getName());
//        }
//        for (int i = 0; i < avalibleInitiators.size(); i++) {
//            this.avalibleInitiatorNames.add(avalibleInitiators.get(i).getName());
//        }
//        model = new DualListModel<String>(avalibleInitiatorNames, initiatorNames);
//
//    }
    public ArrayList<RemoteInitiator> getInitiators() {
        return initiators;
    }
    
    public void setInitiators(ArrayList<RemoteInitiator> initiators) {
        this.initiators = initiators;
    }
    
    public ArrayList<RemoteInitiator> getAvalibleInitiators() {
        return avalibleInitiators;
    }
    
    public void setAvalibleInitiators(ArrayList<RemoteInitiator> avalibleInitiators) {
        this.avalibleInitiators = avalibleInitiators;
    }
    
    public RemoteInitiatorDataModel getDataModel() {
        return dataModel;
    }
    
    public void setDataModel(RemoteInitiatorDataModel dataModel) {
        this.dataModel = dataModel;
    }
}
