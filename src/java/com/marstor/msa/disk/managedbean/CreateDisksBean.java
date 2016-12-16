/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.DiskInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "createDisksBean")
@ViewScoped
public class CreateDisksBean implements Serializable {
//  public String fileSystemName;  //磁盘区名
  public String diskcount;
  public String diskSize;
  public boolean isEnable = true;
  public int index = 0;
  public boolean cantuse;
  public boolean istip;
  public String poolZFSPath;
  public String poolname;
    /**
     * Creates a new instance of CreateDisksBean
     */
    public CreateDisksBean() {
        initCreateDisks();
        cantuse = false;
        istip = false;
    }
    public void initCreateDisks(){
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        poolZFSPath = request.getParameter("poolZFSPath");
        poolname = request.getParameter("poolName");
    }
    
    public String saveCreateDisk(){
        MSAResource resources = new MSAResource();
        index = 0;
        
        //缺 创建磁盘 显示提示信息？？？？？？？？？
        
        
        String returnStr = null;
        if(!ValidateUtility.checkSize(diskcount.trim(), false, 100)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.createdisk", "countlimit"), ""));
            return null;
        }
        
         if(!ValidateUtility.checkSize(diskSize.trim(), false, 32769)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.createdisk", "sizelimit"), ""));
            return null;
        }
        
//         if (!ValidateUtility.checkNum(diskcount.trim(), false)) {
//             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "无效的磁盘数", "。"));
//            return null;
//        }
        
//  ???容量      LicenseInformation capacityLicense = node.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//        long licenseCapacity = capacityLicense.getFunctionNumber() * 1024L;
//        
//        if (!ValidateUtility.checkSize(diskSize.trim(), false, licenseCapacity)) {
//             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "磁盘大小超出上限", "。"));
//            return null;
//        }
        cantuse = true;
        istip = true;
        boolean bDynamic = isEnable;

        int szDiskNumber = Integer.valueOf(diskcount.trim()).intValue();
        boolean flag = true;
        System.out.println("szDiskNumber+1="+(szDiskNumber+1));
        for (int i = 0; i < szDiskNumber + 1; i++) {
            System.out.println("index=" + (index));
            if (index == szDiskNumber) {
                System.out.println("index=szDiskNumber=" + (index));
                cantuse = false;
                istip = false;
                if (flag) {
                    if (isEnable == true) {
                        System.out.println("555555555555555555");
                        RequestContext.getCurrentInstance().execute("adddiskok.show()");
                        return null;
                    } else {
                        System.out.println("8888888888888888888888");
                        returnStr = "virtualdisklist?faces-redirect=true";
                    }
                    break;
                }
//                return "diskarea?faces-redirect=true";
            }
            if (index == szDiskNumber && flag && isEnable) {
            } else {
                index = index + 1;
                DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
                System.out.println("poolZFSPath=" + poolZFSPath);
                System.out.println("Integer.valueOf(diskSize.trim())=" + Integer.valueOf(diskSize.trim()));
                System.out.println("bDynamic=" + bDynamic);
                boolean create = diskIn.createDisk(poolZFSPath, Integer.valueOf(diskSize.trim()), bDynamic);
                if (create == false) {
                    flag = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.createdisk", "creatfalse1") + index + resources.get("disk.createdisk", "creatfasle2"), ""));
                    cantuse = false;
                    istip = false;
                    return null;
                }
            }
            
//            index = index + 1 ;
//            DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
//            System.out.println("poolZFSPath="+poolZFSPath);
//            System.out.println("Integer.valueOf(diskSize.trim())="+Integer.valueOf(diskSize.trim()));
//            System.out.println("bDynamic="+bDynamic);
//            boolean create = diskIn.createDisk(poolZFSPath, Integer.valueOf(diskSize.trim()), bDynamic);
//            if (create == false) {
//                flag = false;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.createdisk", "creatfalse1") + index + resources.get("disk.createdisk", "creatfasle2"), ""));
//                cantuse = false;
//                istip = false;
//                return null;
//            }
        }
        
       

        return returnStr;
    }
    
     public String toLUNMapping(){
         String param = "zfsPath=" + poolZFSPath + "&" + "poolName=" + poolname+ "&" + "GUID=" + "";
        return "lunmapping?faces-redirect=true&amp;" + param;
         
//        System.out.println("6666666666666666666666666");
//        RequestContext.getCurrentInstance().execute("window.location.href='lunmapping.xhtml'");
    }
     
    public void toVirtualdisklist(){
        System.out.println("6666666666666666666666666");
        RequestContext.getCurrentInstance().execute("window.location.href='virtualdisklist.xhtml'");
    }

//    public String getFileSystemName() {
//        return fileSystemName;
//    }
//
//    public void setFileSystemName(String fileSystemName) {
//        this.fileSystemName = fileSystemName;
//    }

    public String getDiskcount() {
        return diskcount;
    }

    public void setDiskcount(String diskcount) {
        this.diskcount = diskcount;
    }

    public String getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isCantuse() {
        return cantuse;
    }

    public void setCantuse(boolean cantuse) {
        this.cantuse = cantuse;
    }

    public boolean isIstip() {
        return istip;
    }

    public void setIstip(boolean istip) {
        this.istip = istip;
    }

    public String getPoolZFSPath() {
        return poolZFSPath;
    }

    public void setPoolZFSPath(String poolZFSPath) {
        this.poolZFSPath = poolZFSPath;
    }
    
}
