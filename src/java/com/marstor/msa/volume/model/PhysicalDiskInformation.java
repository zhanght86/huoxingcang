package com.marstor.msa.volume.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author root
 */
public class PhysicalDiskInformation implements Serializable
{

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
    public String diskName;
    public long diskSize;
    public String poolName = "";
    public int model;//1.datadisk,2.sparedisk,3.unuseddisk
    public int state;//1.online,2.offline,3.spareinuse,4.spareavailible,5.unuseddisk
    public String position = "";//≈ÃŒª∫≈
    public int cardModel = 0;  //0 other ;1 SASCard ; 2 Expander 
    public int cardIndex = 0;  //ø®∫≈ »ÁSASø® 1 £ª¿©’πœ‰ 1
//    public SASCard parentCard;
//    public Expander parentExpander;
//    public DiskProduct product ; 
    public String phys = "";
    public int physnum = 0;      //¥≈≈Ã‘⁄sasø®µƒŒª÷√

//    public XMLConstructor toXMLConstrutor()
//    {
//        XMLConstructor bean = new XMLConstructor("Bean");
//        bean.addNode("DiskName", diskName);
//        bean.addNode("DiskSize", diskSize);
//        bean.addNode("PoolName", poolName);
//        bean.addNode("Model", model);
//        bean.addNode("State", state);
//        bean.addNode("Position", position);
//        bean.addNode("cardModel", cardModel);
//        bean.addNode("cardIndex", cardIndex);
////        bean.addNode("Phys", phys);
////        bean.addNode("physnum", physnum);
//        return bean;
//    }
//
//    public void generateByXMLConstructor(XMLParser bean)
//    {
//        diskName = bean.getNodeContent("Bean/DiskName");
//        diskSize = bean.getLongNodeContent("Bean/DiskSize");
//        poolName = bean.getNodeContent("Bean/PoolName");
//        model = bean.getIntNodeContent("Bean/Model");
//        state = bean.getIntNodeContent("Bean/State");
//        position = bean.getNodeContent("Bean/Position");
//        cardModel = bean.getIntNodeContent("Bean/cardModel");
//        cardIndex = bean.getIntNodeContent("Bean/cardIndex");
////        phys = bean.getNodeContent("Bean/Phys");
////        physnum = bean.getIntNodeContent("Bean/physnum");
//    }
//
//    public int compareTo(PhysicalDiskInformation o)
//    {
//        if (this.cardModel != o.cardModel)
//        {
//            return this.cardModel - o.cardModel;
//        }
//        if (this.cardIndex != o.cardIndex)
//        {
//            return this.cardIndex - o.cardIndex;
//        }
//        if (position != null && !position.equals(""))
//        {
//            try
//            {
//                return Integer.valueOf(this.position) - (Integer.valueOf(o.position));
//            }
//            catch (Exception e)
//            {
//                return this.position.compareTo(o.position);
//            }
//        }
//        return this.diskName.compareTo(o.diskName);
//    }
}
