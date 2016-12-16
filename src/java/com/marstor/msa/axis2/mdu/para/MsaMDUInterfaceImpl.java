/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.mdu.para;

import com.marstor.msa.axis2.mdu.model.ListFiles;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MsaMDUInterfaceImpl implements MsaMDUInterface {

    private static MsaMDUInterfaceImpl mdu = null;
    private static String error;

    private MsaMDUInterfaceImpl() {
    }

    public static MsaMDUInterfaceImpl getInstance() {
        if (mdu == null) {
            mdu = new MsaMDUInterfaceImpl();
        }
        return mdu;
  
    }

    @Override
    public boolean loginMDU(String username) {
       
        return true;
    }

    @Override
    public boolean logoutMDU(String username) {
      
        return true;
    }   

    @Override
    public boolean mkdir(String username, String path) {
       
        return true;
    }


    @Override
    public String getError() {
        return "";
    }
    
    
    
//    @Override
//    public ListFiles getListFiles(String dir) {
//        this.MDU_println("MsaMDUInterfaceImpl getListFiles dir:" + dir);
//        ListFiles mdul = new ListFiles();
//        List<String> dirs = new ArrayList();
//        List<String> files = new ArrayList();
//
//        File f = new File(dir);
//        File[] fils = f.listFiles();
//        for (File file : fils) {
//            if (f.isDirectory()) {
//                dirs.add(file.getName());
//            }else{
//                files.add(file.getName());
//            }
//        }
//      
//        if (dirs.size() > 0) {
//            mdul.setDirs((String[]) dirs.toArray());
//        }
//        if (files.size() > 0) {
//            mdul.setFiles((String[]) files.toArray());
//        }
//        return mdul;
//    }
    
    @Override
     public String[] listDirectory(String username , String path){
        this.MDU_println("MsaMDUInterfaceImpl listDirectory username:" + username +",path="+path);

        List<String> dirs = new ArrayList();
        String[] dirstr = null;
        File f = new File(path);
        File[] fils = f.listFiles();
        for (File file : fils) {
            if (f.isDirectory()) {
//                this.MDU_println("MsaMDUInterfaceImpl listDirectory file.getName():" + file.getName());
                dirs.add(file.getName());
            }
        }

        if (dirs.size() > 0) {
            dirstr =new String[dirs.size()];
            dirs.toArray(dirstr);           
        }

        return dirstr;
    }

      public final boolean MDU_println(String para) {
        System.out.println("(MDU Webserverce) " + para);
        return true;
    }

    @Override
    public String getMDU() {
        return "";
    }

}
