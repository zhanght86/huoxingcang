/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.service;

import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.bean.Volume;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.List;

/**
 *
 * @author zeroz
 */
public class NASServices {

    private NASInterface nas = InterfaceFactory.getNASInterfaceInstance();
//    private String error;

//    public String getError() {
//        return error;
//    }

    public List<Volume> getVolumes() {
        List<Volume> volumes = nas.getVolumes();
        if (volumes == null) {
            return null;
        }
        return volumes;
    }

    public List<SharePath> getAllSharePaths() {
        List<SharePath> sharePaths = nas.getAllSharePaths();
        if (sharePaths == null) {
            return null;
        }
        return sharePaths;
    }
    
    public boolean createSharePath(String volumeName, String nasName) {
        SharePath path = new SharePath(volumeName + "/NAS/" + nasName, volumeName);
        boolean result = nas.createSharePath(path);
        return result;
    }

}
