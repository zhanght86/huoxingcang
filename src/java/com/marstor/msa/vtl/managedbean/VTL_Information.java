/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.vtl.model.DriveBean;
import com.marstor.msa.vtl.model.VTLBean;
import com.marstor.msa.vtl.model.VaultBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "vtl_Information")
@SessionScoped
public class VTL_Information  implements Serializable{

    private VTLBean vtlBean;
    private List<DriveBean> driveList;
    private DriveBean driveBean;
    private int addedDriveAmount;
    private int deletedDriveAmount;
    private int tabIndex = 0;

    public DriveBean getDriveBean() {
        return driveBean;
    }

    public void setDriveBean(DriveBean driveBean) {
        this.driveBean = driveBean;
    }

    /**
     * Creates a new instance of System_hardWareStateList
     */
    public VTL_Information() {
        this.getDriveInformation();
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public int getAddedDriveAmount() {
        return addedDriveAmount;
    }

    public int getDeletedDriveAmount() {
        return deletedDriveAmount;
    }

    public void setDeletedDriveAmount(int deletedDriveAmount) {
        this.deletedDriveAmount = deletedDriveAmount;
    }

    public void setAddedDriveAmount(int addedDriveAmount) {
        this.addedDriveAmount = addedDriveAmount;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public VTLBean getVtlBean() {
        vtlBean = new VTLBean();
        vtlBean.setId(1);
        vtlBean.setName("lmy");
        vtlBean.setVendorID("ADIC");
        vtlBean.setProductID("Scalar 24");
        vtlBean.setRevision("2.78");
        vtlBean.setNumberOfSlots(20);
        vtlBean.setNumberOfPorts(2);
        vtlBean.setNumberOfDrives(2);
        vtlBean.setNumberOfTapes(0);
        vtlBean.setSerialNumber("000051a40eeb0001");
        vtlBean.setGUID("89206130b0927f00000051a40eeb0001");
        return vtlBean;
    }

    public void setVtlBean(VTLBean vtlBean) {
        this.vtlBean = vtlBean;
    }

    public void setDriveList(List<DriveBean> driveList) {
        this.driveList = driveList;
    }

    public List<DriveBean> getDriveList() {
        return driveList;
    }

    public void getDriveInformation() {
        driveList = new ArrayList();

        driveBean = new DriveBean();
        driveBean.setName("nvtl_drive_001");
        driveBean.vendorID = "IBM";
        driveBean.productID = "ULTRIUM-TD01";
        driveBean.revision = "27Q1";
        driveList.add(driveBean);

        driveBean = new DriveBean();
        driveBean.setName("nvtl_drive_002");
        driveBean.vendorID = "IBM";
        driveBean.productID = "ULTRIUM-TD01";
        driveBean.revision = "27Q1";
        driveList.add(driveBean);
    }

    public void addDrive() {
        FacesContext context = FacesContext.getCurrentInstance();
        VTL_Information vtl_Information = (VTL_Information) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{vtl_Information}", VTL_Information.class).getValue(context.getELContext());
        for (int i = 0; i < addedDriveAmount; i++) {
            driveBean = new DriveBean();
            driveBean.setName("nvtl_drive_001");
            driveBean.vendorID = "IBM";
            driveBean.productID = "ULTRIUM-TD01";
            driveBean.revision = "27Q1";
            vtl_Information.driveList.add(driveBean);
        }
    }
    
    public void unloadTape(){
    
    }
    
    public void deleteDrive(){
    
    }
    
    public void renameDrive(){
    
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Edited", ((VaultBean) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Cancelled", ((VaultBean) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
