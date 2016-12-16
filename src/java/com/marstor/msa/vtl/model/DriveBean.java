package com.marstor.msa.vtl.model;

import java.io.Serializable;

public class DriveBean implements Serializable
{

    public int id;
    public String name;
    public String vendorID;
    public String productID;
    public String revision;
    public String serialNumber;
    public String GUID;
    public int number;
    public int full;
    public int tapeID;
    public int tapeLibraryID;

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getTapeID() {
        return tapeID;
    }

    public void setTapeID(int tapeID) {
        this.tapeID = tapeID;
    }

    public int getTapeLibraryID() {
        return tapeLibraryID;
    }

    public void setTapeLibraryID(int tapeLibraryID) {
        this.tapeLibraryID = tapeLibraryID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    
}
