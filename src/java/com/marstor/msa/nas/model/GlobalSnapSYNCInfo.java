/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class GlobalSnapSYNCInfo implements Serializable{

    private static GlobalSnapSYNCInfo instance = null;
    private int autoSnapInterval;
    private int minAutoSnapNum;
    private int maxAutoSnapNum;
    
    private MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();

    public static synchronized GlobalSnapSYNCInfo getInstance() {
        if (instance == null) {
            instance = new GlobalSnapSYNCInfo();
        }
        return instance;
    }

    private GlobalSnapSYNCInfo() {
    }

    public int[] getServerCfg() {
        return sync.getServerCfg();
    }

    public int getAutoSnapInterval() {
        int cfgs[] = this.getServerCfg();
        if (cfgs == null || cfgs.length < 1) {
            return -1;
        }

        return cfgs[0];
    }

    public void setAutoSnapInterval(int autoSnapInterval) {
        this.autoSnapInterval = autoSnapInterval;
    }

    public int getMinAutoSnapNum() {
        int cfgs[] = this.getServerCfg();
        if (cfgs == null || cfgs.length < 2) {
            return -1;
        }

        return cfgs[1];
    }

    public void setMinAutoSnapNum(int minAutoSnapNum) {
        this.minAutoSnapNum = minAutoSnapNum;
    }

    public int getMaxAutoSnapNum() {
       int cfgs[] = this.getServerCfg();
        if (cfgs == null || cfgs.length < 3) {
            return -1;
        }

        return cfgs[2];
    }
    public int getTransferPort() {
       int cfgs[] = this.getServerCfg();
        if (cfgs == null || cfgs.length < 5) {
            return -1;
        }

        return cfgs[4];
    }
    
    public void setMaxAutoSnapNum(int maxAutoSnapNum) {
        this.maxAutoSnapNum = maxAutoSnapNum;
    }

    public MsaSYNCInterface getSync() {
        return sync;
    }

    public void setSync(MsaSYNCInterface sync) {
        this.sync = sync;
    }
    

    
    
}
