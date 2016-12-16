/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import java.util.ResourceBundle;

/**
 *
 * @author Administrator
 */
public class GetResource {
    public static String getString(String str){
        return ResourceBundle.getBundle("/com/marstor/msa/oracle/res/ORA").getString(str);
    }
    
}
