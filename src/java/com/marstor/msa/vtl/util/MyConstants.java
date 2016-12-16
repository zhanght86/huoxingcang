package com.marstor.msa.vtl.util;

import com.marstor.msa.bean.DriveTypeInformation;
import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.bean.TapeLibraryTypeInformation;
import com.marstor.msa.bean.VaultInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.web.VtlInterface;
import java.net.URL;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "vtl_util")
@ApplicationScoped
public class MyConstants {
    public static final int TAPE_STATE_ENABLE = 0;
    public static final int TAPE_STATE_CREATING = 1;
    public static final int TAPE_STATE_FAILED = 2;

    public static URL IMG_URL_ROOT;
    public static URL IMG_URL_LIB;
    public static URL IMG_URL_LIBS;
    public static URL IMG_URL_DRIVE;
    public static URL IMG_URL_TAPES;
    public static URL IMG_URL_TAPE;
    public static URL IMG_URL_VAULT;
    public static URL IMG_URL_VTL_MANAGER;
    //LogType
    public static final int USER_TYPE_NORMAL = 1;
    public static final int USER_TYPE_ADMIN = 2;
    //LUType
    public static final int VAULT_TYPE_FREE = 0;//带架
    public static final int VAULT_TYPE_IMPORT = 2;
    public static final int VAULT_TYPE_FOREIGN = 1;//
    public static int VAULT_TYPE_OFFLINE = -1;//离线带架
//    public static final int VAULT_TYPE_USER = 3;
    //磁带的位置
    public static final int CHANGER_ELEMENT_TYPE_ALL = 0;
    public static final int CHANGER_ELEMENT_TYPE_ROBOT = 1;//机械手
    public static final int CHANGER_ELEMENT_TYPE_SLOT = 2;//槽
    public static final int CHANGER_ELEMENT_TYPE_IEPORT = 3;//端口
    public static final int CHANGER_ELEMENT_TYPE_DRIVE = 4;
    public static final int LOCATION_TYPE_VAULT = 0;
    public static final int LOCATION_TYPE_TAPELIBRARY = 1;
    public static final int MAX_VAULTS = 32;
    public static final int MAX_SYSTEM_USERS = 32;
    public static final int LICENSE_TYPE_BASIC = 0;
    public static final int LICENSE_TYPE_DISK_SPACE = 1;
    public static final int LICENSE_TYPE_DRIVE_COUNT = 2;
    public static final int LICENSE_TYPE_TAPELIBRARY_COUNT = 3;
    public static final int LICENSE_TYPE_REAL_TAPELIBRARY = 4;
    public static final int LICENSE_TYPE_REMOTE_COPY = 5;
    public static final int LICENSE_TYPE_SLOT_COUNT = 6;
    public static final int LICENSE_TYPE_ENABLE_FC = 7;
    public static final int LICENSE_TYPE_ENABLE_ISCSI = 8;

    public String locationIDtoStr(TapeInformation tape) {
        
        if (tape.locationTypeID == MyConstants.LOCATION_TYPE_VAULT) {
            return "";
        } else if (tape.elementType == MyConstants.CHANGER_ELEMENT_TYPE_IEPORT) {
            return "出入口：" + tape.elementNumber;
        } else if (tape.elementType == MyConstants.CHANGER_ELEMENT_TYPE_SLOT) {
            return "槽位：" + tape.elementNumber;
        } else if (tape.elementType == MyConstants.CHANGER_ELEMENT_TYPE_DRIVE) {
            return "驱动器：" + tape.elementNumber;
        }
        return "";
    }

    public static String strLocation(TapeInformation tape) {
        initOFFLINE();
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        String location = "";
        boolean onVault = false;
        boolean unimport = false;
        if (tape.locationTypeID == MyConstants.LOCATION_TYPE_VAULT) {
            onVault = true;
            switch (tape.locationID) {
                case MyConstants.VAULT_TYPE_FREE:
                    location = "空白带架";
                    break;
                case MyConstants.VAULT_TYPE_IMPORT://  location = "导入带架";
                case MyConstants.VAULT_TYPE_FOREIGN:// location = "离线带架";
                default:
                    if (tape.locationID == MyConstants.VAULT_TYPE_OFFLINE) {
                        location = "离线带架";
                        break;
                    } else {
                        for (VaultInformation lib : vtl.getVaults()) {
                            if (lib.id == tape.locationID) {
                                location = lib.name;
                                break;
                            }
                        }
                    }
                    break;
            }
        } else if(tape.locationTypeID == MyConstants.LOCATION_TYPE_TAPELIBRARY){
            for (TapeLibraryInformation lib : vtl.getTapeLibrarysInfo()) {
                if (lib.id == tape.locationID) {
                    location = lib.name;
                    break;
                }
            }
        }else{
            location = "未导入";
            unimport = true;
        }
        if (onVault) {
            location = "带架" + ":" + location;
        } else {
            if(!unimport){
                location = "磁带库" + ":" + location;
            }
            
        }
        return location;
    }
    
    public static void initOFFLINE() {

        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        VaultInformation[] vaults = vtl.getVaults();
        if (vaults != null && vaults.length > 0) {
            for (int i = 0; i < vaults.length; i++) {
                if ("offline".equalsIgnoreCase(vaults[i].name)) {
                    MyConstants.VAULT_TYPE_OFFLINE = vaults[i].id;
                }
            }
        }


    }

    public static void initImgUrl() {
        if (IMG_URL_ROOT != null) {
            return;
        }
        IMG_URL_ROOT = MyConstants.class.getResource("resources/tree_servers.png");
        IMG_URL_LIB = MyConstants.class.getResource("resources/tree_virtual_tape_library.png");
        IMG_URL_LIBS = MyConstants.class.getResource("resources/tree_virtual_tape_libraries.png");
        IMG_URL_DRIVE = MyConstants.class.getResource("resources/tree_virtual_tape_drive.png");
        IMG_URL_TAPES = MyConstants.class.getResource("resources/tree_virtual_tapes.png");
        IMG_URL_TAPE = MyConstants.class.getResource("resources/tree_virtual_tape.png");
        IMG_URL_VAULT = MyConstants.class.getResource("resources/tree_virtual_vault.png");
        IMG_URL_VTL_MANAGER = MyConstants.class.getResource("resources/tree_vtl_manager.png");
    }
    public static TapeLibraryTypeInformation[] tapeLibraryType;
    public static DriveTypeInformation[] driveType;

    public static TapeLibraryTypeInformation[] getTapeLibraryType() {
        return tapeLibraryType;
    }

    public static void setTapeLibraryType(TapeLibraryTypeInformation[] tapeLibraryType) {
        MyConstants.tapeLibraryType = tapeLibraryType;
    }

    public static DriveTypeInformation[] getDriveType() {
        return driveType;
    }

    public static void setDriveType(DriveTypeInformation[] driveType) {
        MyConstants.driveType = driveType;
    }
    

    public static boolean checkDriveName(String name) {
        boolean flag = false;
        if (name.length() < 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z0-9._]+$");
        }
        return flag;
    }

    public static boolean checkName(String name) {
        boolean flag = false;
        if (name.length() < 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z0-9]+$");
        }
        return flag;
    }

    public static boolean checkTapeName(String name) {
        boolean flag = false;
        if (name.length() < 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z0-9_-]+$");
        }
        return flag;
    }

    public static boolean checkNum(String name) {
        boolean flag = false;
        if (name.length() > 0) {
            flag = name.matches("^[1-9]+[0-9]*$");
        }
        return flag;
    }

    public static boolean checkSize(String name) {
        boolean flag = false;
        if (name.length() <= 4) {
            flag = name.matches("^[0-9]+$");
        }
        if (flag) {
            flag = (Integer.valueOf(name) > 0);
        }
        return flag;
    }

    public static String sizeToString(long size) {
        SystemOutPrintln.print_vtl("size="+size);
        String unitStr;
        long intSize, pointSize;
        long unit;
        if (size >= ((long) 1024 * (long) 1024 * (long) 1024 * (long) 1024)) {
            unitStr = "TB";
            unit = (long) 1024 * (long) 1024 * (long) 1024 * (long) 1024;
        } else if (size >= (1024 * 1024 * 1024)) {
            unitStr = "GB";
            unit = 1024 * 1024 * 1024;
        } else if (size >= (1024 * 1024)) {
            unitStr = "MB";
            unit = 1024 * 1024;
        } else if (size >= 1024) {
            unitStr = "KB";
            unit = 1024;
        } else {
            unitStr = "B";
            unit = 1;
        }
        intSize = size / unit;
        pointSize = ((size % unit) * 100) / unit;
        if (intSize >= 100) {
            return intSize + unitStr;
        } else if (intSize >= 10) {
            return intSize + "." + pointSize / 10 + unitStr;
        } else {
            return intSize + "." + pointSize / 10 + pointSize % 10 + unitStr;
        }
    }
}
