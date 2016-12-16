/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Administrator
 */
public class TablesDataModel extends ListDataModel<String> implements SelectableDataModel<String>, Serializable{
    
    public TablesDataModel(){
    
    }
    
    public TablesDataModel(List<String> data) {  
        super(data);  
    }  

    @Override
    public Object getRowKey(String str) {
        return str;
    }

    @Override
    public String getRowData(String rowKey) {
        List<String> data = (List<String>)getWrappedData();  
        for(String table : data){
            if(table.equals(rowKey))  
                return table;  
        }
        
        return null;
    }
}
