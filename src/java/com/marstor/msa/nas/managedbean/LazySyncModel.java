/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;


import static com.marstor.msa.common.managedbean.SystemLogBean.getTime;
import com.marstor.msa.common.util.JavaDBConnect;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.PathUtil;
import com.marstor.msa.javadb.SyncDBUtil;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.vtl.util.MyUtility;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
public class LazySyncModel extends LazyDataModel<Snapshot> {

    private static MSAResource res = new MSAResource();
    private static PathUtil pu = new PathUtil();

    private List<String> typeList;
    private List<Snapshot> list;

    
    public String path;
     /**
     * 解析的XML的键值对存放的map，long型为ID，List为信息
     */
    private HashMap<Long, List<String>> map;

    public LazySyncModel() {
    

    }

    public LazySyncModel(String zfsName) {
        path = zfsName;
    }
    


    @Override
    public Snapshot getRowData(String rowKey) {
        for (Snapshot sync : list) {
            if (sync.getCreatedTime().equals(rowKey)) {
                return sync;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Snapshot sync) {
        return sync.getCreatedTime();
    }

    @Override
    public List<Snapshot> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        SyncDBUtil dbu = new SyncDBUtil();
       
        int pagecount = dbu.querySnapshotCount(path);
        this.setRowCount(pagecount);
        list = dbu.querySnapshot(path, first, pageSize);
         
                    
        return list;
    }



   
}
