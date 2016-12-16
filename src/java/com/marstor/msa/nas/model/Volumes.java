/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
@javax.faces.bean.ManagedBean(name = "volumes")
@javax.faces.bean.RequestScoped
public class Volumes  implements Serializable{
    
    private String  selectedName;
    //private List<Volumes>  volumes;
   private List<String>  names;
   
    public Volumes() {
        
        names = new ArrayList<String>();
        names.add("SYSVOL");
        names.add("test");
        
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }
    
    
    
}
