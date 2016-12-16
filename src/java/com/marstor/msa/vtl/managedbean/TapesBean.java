/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.bean.VaultInformation;
import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.model.TapeInfor;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class TapesBean implements Serializable {

    private TapeInformation[] disks;
    private List<TapeInfor> tapes;
    TapeInfor tapein;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.tapes";
    private String vault = "";
    private List<String> vaultList = new ArrayList();
    private String lib = "";
    private List<String> libList = new ArrayList();
    private String storage = "";
    private List<String> storageList = new ArrayList();
    private String[] volumeNames;
    VaultInformation[] vaults;
    TapeLibraryInformation[] libs;
    String name="";
    public TapesBean() {
       
        initLibs();
        initVaults();
        initStorages();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        name = request.getParameter("name");
        System.out.println("name="+name);
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        this.disks = vtl.getAllTapesInfo();
        if(name!=null && !name.equalsIgnoreCase("null") && !name.equals("")){
            init();
        }else{
            initTapes();
        }
    }
    
    public void init(){
        tapes = new ArrayList();
          VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
             TapeInformation[] allTape = vtl.getAllTapesInfo();
            if (allTape == null) {
                return;
            }
            String mediaPath = "/" + name;
            System.out.println("mediaPath="+mediaPath);
            for (int i = 0; i < allTape.length; i++) {
                if (allTape[i].path.equals(mediaPath)) {
                    tapein = new TapeInfor();
                    tapein.setBarcode(allTape[i].barcode);
                    tapein.setCapacity(MyConstants.sizeToString(allTape[i].tapeSize));
                    tapein.setUsed(MyConstants.sizeToString(allTape[i].usedSize));
                    tapein.setLocal(MyConstants.strLocation(allTape[i]));
                    tapes.add(tapein);
                }
            }
            storage = name;
         
    }
    

    public void initTapes() {
        tapes = new ArrayList();
        
        if (disks == null) {
            return;
        }
        for (TapeInformation disk : disks) {
            tapein = new TapeInfor();
            tapein.setBarcode(disk.barcode);
            tapein.setCapacity(MyConstants.sizeToString(disk.tapeSize));
            tapein.setUsed(MyConstants.sizeToString(disk.usedSize));
            tapein.setLocal(MyConstants.strLocation(disk));
            tapes.add(tapein);
        }
    }

    public void initVaults() {
        this.vaultList.add(res.get(basename, "all"));
        vault = res.get(basename, "all");
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        vaults = vtl.getVaults();
        if (vaults != null && vaults.length > 0) {
            for (VaultInformation va : vaults) {
                if (va.id == MyConstants.VAULT_TYPE_FREE) {
                    va.name = res.get(basename, "vault_type_free");
                } else if (va.id == MyConstants.VAULT_TYPE_IMPORT) {
//                vault.name = "导入带架";
                    continue;
                } else if (va.id == MyConstants.VAULT_TYPE_FOREIGN) {
//                vault.szName = "离线带架";
                    continue;
                } else if (va.name.equals("offline")) {
                    va.name = res.get(basename, "offline");
                }
                this.vaultList.add(va.name);
            }
        }
    }

    public void initLibs() {
        this.libList.add(res.get(basename, "all"));
        lib = res.get(basename, "all");
        //获得所有带库
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        libs = vtl.getTapeLibrarysInfo();

        if (libs != null && libs.length > 0) {
            for (int i = 0; i < libs.length; i++) {
                this.libList.add(libs[i].name);

            }
        }
    }

    public void initStorages() {
        storageList.add(res.get(basename, "all"));
        storage = res.get(basename, "all");
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        volumeNames = zfs.getVolumeNames();
        if (volumeNames == null) {
            return;
        }

        for (int i = 0; i < this.volumeNames.length; i++) {
            FileSystemInformation view = zfs.getFileSystemInformation(volumeNames[i] + "/TAPE");
            if (view == null) {
                continue;
            }
            storageList.add(volumeNames[i] + "/TAPE");
        }

    }

    public void refreshTable() {
        System.out.println("vault="+vault+",lib="+lib+",storage="+storage);
        tapes = new ArrayList();
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        if (vault.equals(res.get(basename, "all")) && lib.equals(res.get(basename, "all")) && storage.equals(res.get(basename, "all"))) {
            if (disks == null) {
                return;
            }
            for (TapeInformation disk : disks) {
                tapein = new TapeInfor();
                tapein.setBarcode(disk.barcode);
                tapein.setCapacity(MyConstants.sizeToString(disk.tapeSize));
                tapein.setUsed(MyConstants.sizeToString(disk.usedSize));
                tapein.setLocal(MyConstants.strLocation(disk));
                tapes.add(tapein);
            }
        }

        if (!lib.equals(res.get(basename, "all")) && !vault.equals(res.get(basename, "all"))) {
//            tapein = new TapeInfor();
        }
        
         int vaultid = 0;
         if(!vault.equals(res.get(basename, "all"))){
             if (vault.equals(res.get(basename, "vault_type_free"))) {
                vaultid = MyConstants.VAULT_TYPE_FREE;
            } else if (vault.equals(res.get(basename, "offline"))) {
                VaultInformation[] valuts = vtl.getVaults();
                for (int n = 0; n < valuts.length; n++) {
                    if (valuts[n].getName().equals("offline")) {
                        vaultid = valuts[n].getId();
                        break;
                    }
                }

            } else {
                if (vaults != null && vaults.length > 0) {
                    for (VaultInformation va : vaults) {
                        if (va.name.equals(vault)) {
                            vaultid = va.id;
                            break;
                        }
                    }
                }
            }
         }

        int tapeLibraryID = 0;
        if (!lib.equals(res.get(basename, "all"))) {
            if (libs != null && libs.length > 0) {
                for (int i = 0; i < libs.length; i++) {

                    if (libs[i].name.equals(lib)) {
                        tapeLibraryID = libs[i].id;
                    }

                }
            }
        }
        
         List<TapeInfor> valut_tapes = new ArrayList();
         List<TapeInfor> lib_tapes = new ArrayList();
         List<TapeInfor> storage_tapes = new ArrayList();
         if(!storage.equals(res.get(basename, "all"))){
             TapeInformation[] allTape = vtl.getAllTapesInfo();
            if (allTape == null) {
                return;
            }
            String mediaPath = "/" + storage;
            for (int i = 0; i < allTape.length; i++) {
                if (allTape[i].path.equals(mediaPath)) {
                    tapein = new TapeInfor();
                    tapein.setBarcode(allTape[i].barcode);
                    tapein.setCapacity(MyConstants.sizeToString(allTape[i].tapeSize));
                    tapein.setUsed(MyConstants.sizeToString(allTape[i].usedSize));
                    tapein.setLocal(MyConstants.strLocation(allTape[i]));
                    storage_tapes.add(tapein);
                }
            }
         }
         if (!lib.equals(res.get(basename, "all"))) {
            // 4023 获得指定磁带库的磁带信息
            TapeInformation[] taps = vtl.getTapeLibraryTapesInfo(tapeLibraryID);
            if (taps != null && taps.length > 0) {
                for (int i = 0; i < taps.length; i++) {
                    if (taps[i].elementType != MyConstants.CHANGER_ELEMENT_TYPE_DRIVE) {
                        tapein = new TapeInfor();
                        tapein.setBarcode(taps[i].barcode);
                        tapein.setCapacity(MyConstants.sizeToString(taps[i].tapeSize));
                        tapein.setUsed(MyConstants.sizeToString(taps[i].usedSize));
                        tapein.setLocal(MyConstants.strLocation(taps[i]));
                        lib_tapes.add(tapein);
                    }
                }
            }
        }
        if(!vault.equals(res.get(basename, "all"))){
             //获得左面相应带架下的磁带
            TapeInformation[] taps = vtl.getVaultTapesInfo(vaultid);
            for (TapeInformation tape : taps) {
                if (tape.state == TapePutin.TAPE_STATE_CREATING || tape.state == TapePutin.TAPE_STATE_FAILED) {
                    continue;
                }
                if (tape.locationTypeID != MyConstants.LOCATION_TYPE_VAULT) {
                    continue;
                }
                if (tape.locationID != vaultid) {
                    continue;
                }
                tapein = new TapeInfor();
                tapein.setBarcode(tape.barcode);
                tapein.setCapacity(MyConstants.sizeToString(tape.tapeSize));
                tapein.setUsed(MyConstants.sizeToString(tape.usedSize));
                tapein.setLocal(MyConstants.strLocation(tape));
                valut_tapes.add(tapein);

            }
        }
            
        if (!vault.equals(res.get(basename, "all")) && lib.equals(res.get(basename, "all")) && storage.equals(res.get(basename, "all"))) {     
           tapes = valut_tapes;
        }
        if (!vault.equals(res.get(basename, "all")) && lib.equals(res.get(basename, "all")) && !storage.equals(res.get(basename, "all"))) {
            for(TapeInfor valut_tape : valut_tapes){
                for(TapeInfor storage_tape :storage_tapes){
                    if(valut_tape.barcode.endsWith(storage_tape.barcode)){
                        tapes.add(valut_tape);
                        break;
                    }
                }
            }
            
        }
        if (vault.equals(res.get(basename, "all")) && !lib.equals(res.get(basename, "all")) && storage.equals(res.get(basename, "all"))) {
           tapes = lib_tapes;
        }
        if (vault.equals(res.get(basename, "all")) && lib.equals(res.get(basename, "all")) && !storage.equals(res.get(basename, "all"))) {
            tapes = storage_tapes;    
        }

        if (vault.equals(res.get(basename, "all")) && !lib.equals(res.get(basename, "all")) && !storage.equals(res.get(basename, "all"))) {
             for(TapeInfor lib_tape : lib_tapes){
                for(TapeInfor storage_tape :storage_tapes){
                    if(lib_tape.barcode.endsWith(storage_tape.barcode)){
                        tapes.add(lib_tape);
                        break;
                    }
                }
            }
        }





    }

    public void refreshTable(String vault) {
    }

    public List<TapeInfor> getTapes() {
        return tapes;
    }

    public void setTapes(List<TapeInfor> tapes) {
        this.tapes = tapes;
    }

    public TapeInfor getTapein() {
        return tapein;
    }

    public void setTapein(TapeInfor tapein) {
        this.tapein = tapein;
    }

    public String getVault() {
        return vault;
    }

    public void setVault(String vault) {
        this.vault = vault;
    }

    public List<String> getVaultList() {
        return vaultList;
    }

    public void setVaultList(List<String> vaultList) {
        this.vaultList = vaultList;
    }

    public String getLib() {
        return lib;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public List<String> getLibList() {
        return libList;
    }

    public void setLibList(List<String> libList) {
        this.libList = libList;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public List<String> getStorageList() {
        return storageList;
    }

    public void setStorageList(List<String> storageList) {
        this.storageList = storageList;
    }
}
