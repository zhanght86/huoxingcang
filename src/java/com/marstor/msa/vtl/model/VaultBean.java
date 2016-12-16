/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

import java.io.Serializable;

/**
 *
 * @author admin
 */
public class VaultBean implements Serializable {

    public int id;
    public String name;
    public int numberOfTapes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfTapes() {
        return numberOfTapes;
    }

    public void setNumberOfTapes(int numberOfTapes) {
        this.numberOfTapes = numberOfTapes;
    }


}
