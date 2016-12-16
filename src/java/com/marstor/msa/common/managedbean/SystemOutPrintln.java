/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.common.managedbean;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SystemOutPrintln implements Serializable{

    public SystemOutPrintln() {
    }

    public static void print_common(String s) {

        System.out.println("Common_PAGE_INFO: " + s);

    }
    
    public static void print_vm(String s) {

        System.out.println("VM_PAGE_INFO: " + s);

    }
    public static void print_volume(String s) {

        System.out.println("VOLUME_PAGE_INFO: " + s);

    }
    public static void print_vtl(String s) {

        System.out.println("VTL_PAGE_INFO: " + s);

    }

}
