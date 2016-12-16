/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class VTLBean implements Serializable {

    private int id;
    private String name;
    private String vendorID;
    private String productID;
    private String revision;
    private String serialNumber;
    private String GUID;
    private int firstDriveNumber;
    private int numberOfDrives;
    private int firstSlotNumber;
    private int numberOfSlots;
    private int firstPortNumber;
    private int numberOfPorts;
    private int firstChangerNumber;
    private int numberOfChangers;
    private int numberOfTapes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public int getFirstChangerNumber() {
        return firstChangerNumber;
    }

    public void setFirstChangerNumber(int firstChangerNumber) {
        this.firstChangerNumber = firstChangerNumber;
    }

    public int getFirstDriveNumber() {
        return firstDriveNumber;
    }

    public void setFirstDriveNumber(int firstDriveNumber) {
        this.firstDriveNumber = firstDriveNumber;
    }

    public int getFirstPortNumber() {
        return firstPortNumber;
    }

    public void setFirstPortNumber(int firstPortNumber) {
        this.firstPortNumber = firstPortNumber;
    }

    public int getFirstSlotNumber() {
        return firstSlotNumber;
    }

    public void setFirstSlotNumber(int firstSlotNumber) {
        this.firstSlotNumber = firstSlotNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfChangers() {
        return numberOfChangers;
    }

    public void setNumberOfChangers(int numberOfChangers) {
        this.numberOfChangers = numberOfChangers;
    }

    public int getNumberOfDrives() {
        return numberOfDrives;
    }

    public void setNumberOfDrives(int numberOfDrives) {
        this.numberOfDrives = numberOfDrives;
    }

    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(int numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public int getNumberOfTapes() {
        return numberOfTapes;
    }

    public void setNumberOfTapes(int numberOfTapes) {
        this.numberOfTapes = numberOfTapes;
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

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }
    
}
