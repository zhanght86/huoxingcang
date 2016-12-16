/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.nas.model.ACL;
import com.marstor.msa.nas.model.Authority;
import com.marstor.msa.nas.model.Constant;
import com.marstor.msa.nas.model.ShareListBean;
import com.marstor.msa.nas.model.Share;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import java.util.*;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Administrator
 */
@javax.faces.bean.ManagedBean(name = "editauthor")
@javax.faces.bean.RequestScoped
public class EditAuthorBean implements Serializable{

    private String style;
    private String name;
    private boolean  read;
    private List<String> selectedReads;
    private Map<String, String> reads;
    private List<String> selectedWrites;
    private Map<String, String> writes;
    private List<String> selectedExecutes;
    private Map<String, String> executes;
    private List<String> selectedDeletes;
    private Map<String, String>  deletes;
    private List<String> selectedCreates;
    private Map<String, String>  creates;
    private List<String> selectedInherits;
    private Map<String, String>  inherits;

    public EditAuthorBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
        //SharePathBean  oneShare = share.getShareByPath(share.getPath());

        ACL acl = share.getSelectedACL();
        style = acl.getObjectStyle();
        name = acl.getObjectName();

        reads = new HashMap<String, String>();
        reads.put(Constant.getAuthor(Constant.readFile), Constant.getAuthor(Constant.readFile));
        reads.put(Constant.getAuthor(Constant.listContent), Constant.getAuthor(Constant.listContent));
        reads.put(Constant.getAuthor(Constant.readBasicAttribute), Constant.getAuthor(Constant.readBasicAttribute));
        reads.put(Constant.getAuthor(Constant.readExpandAttribute), Constant.getAuthor(Constant.readExpandAttribute));
        reads.put(Constant.getAuthor(Constant.readACL), Constant.getAuthor(Constant.readACL));
       
        writes = new HashMap<String, String>();
        writes.put(Constant.getAuthor(Constant.writeFile), Constant.getAuthor(Constant.writeFile));
        writes.put(Constant.getAuthor(Constant.writeFileOwner), Constant.getAuthor(Constant.writeFileOwner));
        writes.put(Constant.getAuthor(Constant.writeFileBasicAttribute), Constant.getAuthor(Constant.writeFileBasicAttribute));
        writes.put(Constant.getAuthor(Constant.writeFileExpandAttribute), Constant.getAuthor(Constant.writeFileExpandAttribute));
        writes.put(Constant.getAuthor(Constant.writeACl), Constant.getAuthor(Constant.writeACl));
        
        creates = new HashMap<String, String>();
        creates.put(Constant.getAuthor(Constant.createFile), Constant.getAuthor(Constant.createFile));
        creates.put(Constant.getAuthor(Constant.createSubDirectory), Constant.getAuthor(Constant.createSubDirectory));
        
        
        executes = new HashMap<String, String>();
        executes.put(Constant.getAuthor(Constant.execute), Constant.getAuthor(Constant.execute));

        deletes = new HashMap<String, String>();
        deletes.put(Constant.getAuthor(Constant.deleteFile), Constant.getAuthor(Constant.deleteFile));
        deletes.put(Constant.getAuthor(Constant.deleteChild), Constant.getAuthor(Constant.deleteChild));
        
        inherits = new HashMap<String, String>();
        inherits.put(Constant.getAuthor(Constant.fileInherit), Constant.getAuthor(Constant.fileInherit));
        inherits.put(Constant.getAuthor(Constant.dirInherit), Constant.getAuthor(Constant.dirInherit));
        
        selectedReads = new ArrayList<String>();
        selectedReads.add(Constant.getAuthor(Constant.listContent));
    }

    public Map<String, String> getInherits() {
        return inherits;
    }

    public void setInherits(Map<String, String> inherits) {
        this.inherits = inherits;
    }

    
    public List<String> getSelectedInherits() {
        return selectedInherits;
    }

    public void setSelectedInherits(List<String> selectedInherits) {
        this.selectedInherits = selectedInherits;
    }

    
    public Map<String, String> getCreates() {
        return creates;
    }

    public void setCreates(Map<String, String> creates) {
        this.creates = creates;
    }

    public List<String> getSelectedCreates() {
        return selectedCreates;
    }

    public void setSelectedCreates(List<String> selectedCreates) {
        this.selectedCreates = selectedCreates;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Map<String, String> getExecutes() {
        return executes;
    }

    public void setExecutes(Map<String, String> executes) {
        this.executes = executes;
    }

    public Map<String, String> getReads() {
        return reads;
    }

    public void setReads(Map<String, String> reads) {
        this.reads = reads;
    }

    public List<String> getSelectedExecutes() {
        return selectedExecutes;
    }

    public void setSelectedExecutes(List<String> selectedExecutes) {
        this.selectedExecutes = selectedExecutes;
    }

    public List<String> getSelectedReads() {
        return selectedReads;
    }

    public void setSelectedReads(List<String> selectedReads) {
        this.selectedReads = selectedReads;
    }

    public List<String> getSelectedWrites() {
        return selectedWrites;
    }

    public void setSelectedWrites(List<String> selectedWrites) {
        this.selectedWrites = selectedWrites;
    }

    public Map<String, String> getWrites() {
        return writes;
    }

    public void setWrites(Map<String, String> writes) {
        this.writes = writes;
    }

    public Map<String, String> getDeletes() {
        return deletes;
    }

    public void setDeletes(Map<String, String> deletes) {
        this.deletes = deletes;
    }

    public List<String> getSelectedDeletes() {
        return selectedDeletes;
    }

    public void setSelectedDeletes(List<String> selectedDeletes) {
        this.selectedDeletes = selectedDeletes;
    }
    public void changeList(ValueChangeEvent event) {
        if(selectedReads.size()>0) {
            this.read =true;
        }
    }
    public ArrayList<String>  unSelected(ArrayList<String> total,List<String> selected){
        ArrayList<String> unSelected = new ArrayList<String>();
        for(int i=0;i<total.size();i++){
            int j;
            for(j=0;j<selected.size();j++) {
                if(total.get(i).equals(selected.get(j))) {
                    break;
                }
            }
            if(j>=selected.size()) {
                unSelected.add(total.get(i));
            }
        }
        return unSelected;
    }
//    public String  save() {
//        
//        FacesContext context = FacesContext.getCurrentInstance();
//        ShareListBean share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
//        Share  sharePath = share.getShareByPath(share.getPath());
//        ACL acl = sharePath.getACLByNameAndStyle(name, style);
//        ArrayList<Authority>  authors = new ArrayList<Authority>();
//        
//        for(int i=0;i<selectedReads.size();i++) {
//            Authority  author = new Authority();
//            author.setName(selectedReads.get(i));
//            author.setEnabled(true);
//            authors.add(author);
//        }
//        
//        return null;
//    }
}
