/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.sync;

import com.marstor.msa.nas.model.Sync;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.util.InterfaceFactory;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class SyncInformation {
    //private List<SyncMapping>  mapList = new ArrayList<SyncMapping>();

    private ArrayList<Sync> syncInfos = new ArrayList<Sync>();
    
    public SyncInformation() {
        List<SyncMapping> mapList = InterfaceFactory.getSYNCInterfaceInstance().getAllSyncMapping();
        if(mapList==null) {
            return ;
        }
        for (SyncMapping map : mapList) {
            Sync sync = new Sync(map.getStrSrcFileSystem(), map.getStrDescFileSystem(), map.getStrIP(), map.getiSyncStatus(), map.getStrSyncSizeSpeed());
            syncInfos.add(sync);
        }
    }
    
    public ArrayList<Sync> getSyncInfos() {
        return syncInfos;
    }
    
    public void setSyncInfos(ArrayList<Sync> syncInfos) {
        this.syncInfos = syncInfos;
    }
    public void update() {
         ArrayList<Sync> syncList = new ArrayList<Sync>();
         List<SyncMapping> mapList = InterfaceFactory.getSYNCInterfaceInstance().getAllSyncMapping();
        if(mapList==null) {
            return ;
        }
        for (SyncMapping map : mapList) {
            Sync sync = new Sync(map.getStrSrcFileSystem(), map.getStrDescFileSystem(), map.getStrIP(), map.getiSyncStatus(), map.getStrSyncSizeSpeed());
            syncList.add(sync);
        }
        this.syncInfos = syncList;
    }
    
}
