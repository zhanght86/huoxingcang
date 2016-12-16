/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.treeNode;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */
public class TreeNodeData extends DefaultTreeNode{  
//
    public String url;
    
    /**
     * Creates a new instance of TreeNodeData
     */
    public TreeNodeData(Object data, TreeNode parent,String strURL)
    {
        super(data, parent);
        this.url = strURL;

    }
    public TreeNodeData(String type, Object data, TreeNode parent, String strURL) {
        super(type, data, parent);
        this.url = strURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    
    
//    public void onNodeSelect(NodeSelectEvent event)
//    {
//
//        try
//        {
//            event.getTreeNode().setSelected(true);
//          event.getTreeNode().setExpanded(true);
//            System.out.println(" here " + event.getTreeNode().getData());
//   
////        FacesContext.getCurrentInstance().getExternalContext().redirect(event.getTreeNode().getData() + "");
//
//
//            
//            FacesContext
//                .getCurrentInstance()
//                .getApplication()
//                .getNavigationHandler()
//                .handleNavigation(FacesContext.getCurrentInstance(),
//                                  "null", event.getTreeNode().getData()+""+"?faces-redirect=true");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            // TODO: handle exception
//        }
//    }
}
