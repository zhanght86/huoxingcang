/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;


public class OracleInfo implements Serializable {
    private String item = "";
    private int number = 0;
    
    public OracleInfo(String item, int number){
        this.item = item;
        this.number = number;
    }
    
    public String getItem(){
        return item;
    }
    
    public int getNumber(){
        return number;
    }
    
    public static void main(String [] arg){
        
    }
    
}