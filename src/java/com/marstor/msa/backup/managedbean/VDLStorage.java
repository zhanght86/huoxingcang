/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.backup.model.VDL;
import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class VDLStorage {

    private List<VDL> vdlList = new ArrayList<VDL>();

    public List<VDL> getVdlList() {
        return vdlList;
    }

    public void setVdlList(List<VDL> vdlList) {
        this.vdlList = vdlList;
    }

    public VDLStorage() {
        init();
    }

    public void init() {
        //int i=0;i<vdlList.size();i++
        List<com.marstor.msa.ba.bean.VDL> allVDL = InterfaceFactory.getMBAInterfaceInstance().getVDL();
        if (allVDL == null) {
//            System.out.println("############ Get VDL list failed.");
            return;
        }
        for (com.marstor.msa.ba.bean.VDL vdl : allVDL) {
            String pool = vdl.getPoolName();
            String path = pool + "/VDL";
            FileSystemInformation fileSys = InterfaceFactory.getZFSInterfaceInstance().getFileSystemInformation(path);
            VDL vdlInfo = new VDL(fileSys.getUsed(), fileSys.getAvailable(), path, pool);
            this.vdlList.add(vdlInfo);
        }

    }

    public String configProperty(VDL vdl) {

        // System.out.println("selected.name=" + selected.name);
        String title = "vdl";
        String fileSystemName = vdl.getPath(); //ÀýÈç£ºSYSVOL/TAPE »òSYSVOL/XX/XX
        String returnStr = "/backups/maintenance";
        String param = "fileSystemName=" + fileSystemName + "&" + "title=" + title + "&" + "return=" + returnStr;
        return "/disk/filesystem_set?faces-redirect=true&amp;" + param;
    }

    public String cleanVDL(VDL vdl) {
        String param = "poolName=" + vdl.getPoolName();
        return "/backups/cleanVDL?faces-redirect=true&amp;" + param;
    }
}
