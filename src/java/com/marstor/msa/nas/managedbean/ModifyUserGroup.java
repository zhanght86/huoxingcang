/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ModifyUserGroup implements Serializable{

    private ArrayList<String> inGroups = new ArrayList<String>();
    private ArrayList<String> sourceGroups = new ArrayList<String>();
    private String userName;
//    private DualListModel<String> modelGroups = new DualListModel<String>(sourceGroups, inGroups);
    private DualListModel<String> model = new DualListModel<String>(inGroups, sourceGroups);

    public ModifyUserGroup() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.userName = request.getParameter("userName");
        String source = request.getParameter("allGroups");
        String[] array;
        if (source != null) {
            array = source.trim().split(",");
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    this.sourceGroups.add(array[i]);
                }
            }
        }
        String target = request.getParameter("inGroups");
        if (target != null) {
            array = target.trim().split(",");
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    this.inGroups.add(array[i]);
                }
            }

        }
        model = new DualListModel<String>(inGroups, sourceGroups);
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

    public DualListModel<String> getModel() {
        return model;
    }

    public void setModel(DualListModel<String> model) {
        this.model = model;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String save() {
        inGroups= (ArrayList<String>) this.model.getSource();
         Debug.print("in groups size :" + inGroups.size());
         if(inGroups.size()>16) {
             MSAResource res = new MSAResource();
            Debug.print("group num > 16 ." );
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("groupSize"), res.get("groupSize")));
            return null;
         }
//        for(int i = 0; i < inGroups.size(); i++) {
//             Debug.print(inGroups.get(i) );
//        }
        boolean flag = InterfaceFactory.getNASInterfaceInstance().modifyUser(userName, inGroups);
        if (!flag) {
            MSAResource res = new MSAResource();
            Debug.print("modify user group" + flag);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
            return null;
        }
        return "nas_domain_config?faces-redirect=true&amp;tabViewActive=1";
    }
}
