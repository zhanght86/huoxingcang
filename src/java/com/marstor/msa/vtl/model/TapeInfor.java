/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

import com.marstor.msa.vtl.bean.TapeInformation;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "tapeInfor")
@RequestScoped
public class TapeInfor extends TapeInformation  implements Serializable{
public String capacity;
public String local;
public String used;
    /**
     * Creates a new instance of TapeInfor
     */
    public TapeInfor() {
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getLocationTypeID() {
        return locationTypeID;
    }

    public void setLocationTypeID(int locationTypeID) {
        this.locationTypeID = locationTypeID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public long getTapeSize() {
        return tapeSize;
    }

    public void setTapeSize(long tapeSize) {
        this.tapeSize = tapeSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(long usedSize) {
        this.usedSize = usedSize;
    }

    public long getWrittenSize() {
        return writtenSize;
    }

    public void setWrittenSize(long writtenSize) {
        this.writtenSize = writtenSize;
    }

    public boolean isWriteProtect() {
        return writeProtect;
    }

    public void setWriteProtect(boolean writeProtect) {
        this.writeProtect = writeProtect;
    }

    public boolean isOnDemand() {
        return onDemand;
    }

    public void setOnDemand(boolean onDemand) {
        this.onDemand = onDemand;
    }

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    public int getElementNumber() {
        return elementNumber;
    }

    public void setElementNumber(int elementNumber) {
        this.elementNumber = elementNumber;
    }

    public boolean isEnableCompression() {
        return enableCompression;
    }

    public void setEnableCompression(boolean enableCompression) {
        this.enableCompression = enableCompression;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
    
}
