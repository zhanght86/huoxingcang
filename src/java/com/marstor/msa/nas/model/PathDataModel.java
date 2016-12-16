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
public class PathDataModel implements Serializable{
    
    private ArrayList<Path>  paths = new ArrayList<Path>();

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }
    
    public void addPath (Path  p) {
        this.paths.add(p);
    }
    public void removePath (Path  p) {
        this.paths.remove(p);
    }
    public Path  getPathByName (String pathName) {
        for (Path p : paths) {
            if(p.getName().equals(pathName)) {
                return p;
            }
        }
        return null;
    }
    public void configStatus(String pathName,boolean value) {
        Path p = this.getPathByName(pathName);
        p.setStatus(value);
    }
}
