/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class IDMapDataModel implements Serializable{
    private  ArrayList<IDMap>  maps = new ArrayList<IDMap>();
    private int num=0;

    public IDMapDataModel() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<IDMap> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<IDMap> maps) {
        this.maps = maps;
    }
    public void  addIDMap(IDMap map) {
        this.maps.add(map);
        this.num++;
        
    }
    public void  removeIDMap(IDMap map) {
        this.maps.remove(map);
    }
    public IDMap  getMapByIndex(int index) {
        for(int i=0;i<num;i++) {
            if(index == this.maps.get(i).getIndex()) {
                return  this.maps.get(i);
            }
        }
        return  null;
    }
    
    
    public boolean  isExistIDMap(IDMap map) {
        String  winName = map.getWinName();
        String  winType = map.getWinType();
        String  unixName = map.getUnixName();
        String  unixType = map.getUnixType();
        for(int i=0;i<this.maps.size();i++) {
            if(maps.get(i).getWinName().equals(winName)&&maps.get(i).getWinType().equals(winType)
             &&maps.get(i).getUnixName().equals(unixName)&&maps.get(i).getUnixType().equals(unixType)) {
                return  true;
            }
        }
        return  false;
    }
}
