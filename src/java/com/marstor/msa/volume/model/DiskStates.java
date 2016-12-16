/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "diskStates")
@SessionScoped
public class DiskStates  implements Serializable{
public final static int DISK_MODEL_DATA = 1;
    public final static int DISK_MODEL_SPARE = 2;
    public final static int DISK_MODEL_UNUSED = 3;
    public final static int DISK_MODEL_CACHE = 4;
    public final static int DISK_MODEL_LOG = 5;
    public final static int DATA_DISK_STATE_ONLINE = 1;
    public final static int DATA_DISK_STATE_OFFLINE = 2;
    public final static int SPARE_DISK_STATE_INUSE_SELF = 3;
    public final static int SPARE_DISK_STATE_INUSE_OTHER = 4;
    public final static int SPARE_DISK_STATE_AVAILIBLE = 5;
    public final static int UNUSED_DISK_STATE = 6;
    public final static int DATA_DISK_CANT_OPEN = 7;
    public final static int DATA_DISK_REMOVED = 8;
    public final static int SMART_BEING_FAILING = 9;
    public final static int CARD_MODE_SAS = 1;
    public final static int CARD_MODE_EXPANDER = 2;
//    public String diskName;
    public long diskSize;
    public String poolName = "";
    public int model;//1.datadisk,2.sparedisk,3.unuseddisk
//    public int state;//1.online,2.offline,3.spareinuse,4.spareavailible,5.unuseddisk
//    public String position = "";//≈ÃŒª∫≈
    public int cardModel = 0;  //0 other ;1 SASCard ; 2 Expander 
    public int cardIndex = 0;  //ø®∫≈ »ÁSASø® 1 £ª¿©’πœ‰ 1
    /**
     * Creates a new instance of diskStates
     */
    public DiskStates() {
    }
}
