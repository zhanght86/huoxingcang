/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DualListModel;
/**
 *
 * @author Administrator
 */
@ManagedBean (name="cifsuser")
@RequestScoped
public class CIFSUser implements Serializable {
    private int number;
    private String  name;
    private String  passwd;
    private String  confirmPasswd;
    //private ArrayList<Group>  attributeGroups = new ArrayList<Group>();
    //private ArrayList<Group>  sourceGroups = new ArrayList<Group>();
    private ArrayList<String> inGroups = new ArrayList<String>();
    private ArrayList<String>  sourceGroups = new ArrayList<String>();
//    private DualListModel<String> modelGroups = new DualListModel<String>(sourceGroups, inGroups);
    private DualListModel<String> modelGroups = new DualListModel<String>(inGroups, sourceGroups);
    private String  attributeGroups;

    public CIFSUser(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public CIFSUser() {
    }

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public ArrayList<String> getInGroups() {
        return inGroups;
    }

    public void setInGroups(ArrayList<String> inGroups) {
        this.inGroups = inGroups;
    }

    public ArrayList<String> getSourceGroups() {
        return sourceGroups;
    }

    public void setSourceGroups(ArrayList<String> sourceGroups) {
        this.sourceGroups = sourceGroups;
    }

    public DualListModel<String> getModelGroups() {
        return modelGroups;
    }

    public void setModelGroups(DualListModel<String> modelGroups) {
        this.modelGroups = modelGroups;
    }


 
    public void addInGroups(String name) {
        this.inGroups.add(name);
    }

    public String getAttributeGroups() {
        return attributeGroups;
    }

    public void setAttributeGroups(String attributeGroups) {
        this.attributeGroups = attributeGroups;
    }
    public void  updateAttributeGroupStr(ArrayList<String> groupNames) {
        StringBuffer names = new StringBuffer();
        for (int i = 0; i < groupNames.size(); i++) {
            names.append(groupNames.get(i));
            if ((i + 1) < groupNames.size()) {
                names.append(",");
            }
        }
        this.attributeGroups = names.toString();

    }
    
    
}
