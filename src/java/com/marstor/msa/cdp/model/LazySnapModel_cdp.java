/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.PathUtil;
import com.marstor.msa.javadb.SyncDBUtil;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.vtl.util.MyUtility;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
public class LazySnapModel_cdp extends LazyDataModel<Snapshot> {

    private List<Snapshot> list;
    public String path;
    
    public LazySnapModel_cdp(String path) {        
        this.path = path;        
        this.setPageSize(10);
    }    

    @Override
    public Snapshot getRowData(String rowKey) {
        for (Snapshot log : list) {
            if (log.getAlias().equals(rowKey)) {
                return log;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Snapshot log) {
        return log.getAlias();
    }

    @Override
    public List<Snapshot> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        list = new ArrayList<Snapshot>();
        
        SyncDBUtil su = new SyncDBUtil();
        this.setRowCount(su.querySnapshotCount(path));
        list = su.querySnapshot(path, first, pageSize);        
        
        return list;
    }
}
