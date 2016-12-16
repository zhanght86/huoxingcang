/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.ba.bean.VDL;
import com.marstor.msa.backup.model.BackupVDL;
import com.marstor.msa.backup.model.DatabaseParameter;
import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "backupVDL")
@ViewScoped
public class BClearVDLBean implements Serializable {

    private MSAResource res = new MSAResource();
    private String pool = "";
    private List<String> poolNames = new ArrayList<String>();
    private String vdl = "";
    private List<String> vdlNames;
    private List<BackupVDL> bVDL;
    private List<String> pathList;
    private boolean bContain = false; 
    private boolean isrender = false;

    public BClearVDLBean() {
        init();
        if(!bContain){
            isrender = true;
        }
    }

    public boolean isIsrender() {
        return isrender;
    }

    public void setIsrender(boolean isrender) {
        this.isrender = isrender;
    }

    public void setBVDL(List<BackupVDL> vdl) {
        this.bVDL = vdl;
    }

    public List<BackupVDL> getBVDL() {
        return bVDL;
    }

    public void setVDL(String vdl) {
        this.vdl = vdl;
    }

    public String getVDL() {
        return vdl;
    }

    private void init() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.pool = request.getParameter("poolName");
       List<VDL> vdlList = InterfaceFactory.getMBAInterfaceInstance().getVDL(); 
        pathList = new ArrayList<String>();
        InputStream in = Util.getRemoteXMLSteram();
        if (null == in) {
            return;
        }

        DatabaseParameter dbBean = Util.getDBBean(in);
        pathList = getVDLPath(dbBean);
        if (null == vdlList) {
            return;
        }

        bVDL = new ArrayList<BackupVDL>();
        for (int i = 0; i < vdlList.size(); i++) {
//            if (0 == i) {
//                vdlNames = new ArrayList<String>();
//                for (File f : vdlList.get(0).getFiles()) {
//                    String strPath = f.getPath().replace("\\", "/");
//                    if (!pathList.contains(strPath)) {
//                        vdlNames.add(f.getName());
//                    }
//                }
//
//                if (0 == vdlNames.size()) {
//                    vdlNames.add("");
//                }
//            }

            BackupVDL backup = new BackupVDL();
            backup.setName(vdlList.get(i).getPoolName());

            if (vdlList.get(i).getFiles() != null) {
                backup.getFileList().addAll(Arrays.asList(vdlList.get(i).getFiles()));
            }

            bVDL.add(backup);
            poolNames.add(vdlList.get(i).getPoolName());
        }
        vdlNames();
    }

    public void setPoolNames(List<String> names) {
        this.poolNames = names;
    }

    public List<String> getPoolNames() {
        return poolNames;
    }

    public void setPool(String pool) {
        System.out.println("Set pool name is : " + pool);
        this.pool = pool;
    }

    public String getPool() {
        return pool;
    }

    public List<String> getVDLNames() {
        return vdlNames;
    }

    public void vdlNames() {
        vdlNames = new ArrayList<String>();

        for (BackupVDL b : bVDL) {
            if (b.getName().equals(pool)) {
                for (File f : b.getFileList()) {
                    String strPath = f.getPath().replace("\\", "/");
                    if (!pathList.contains(strPath)) {
                        vdlNames.add(f.getName());
                    }
                }
            }
        }
        
        if (0 == vdlNames.size()) {
            vdlNames.add("");
        }
    }

    public void clearVDL() {
        boolean configIP = false;
        for (BackupVDL b : bVDL) {
            if (b.getName().equals(pool)) {
                for (File f : b.getFileList()) {
                    if (f.getName().equals(vdl)) {
                        configIP = InterfaceFactory.getMBAInterfaceInstance().clearVDL(f.getPath().replace("\\", "/"));
                    }
                }
            }
        }

        if (configIP) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("ClearVDLSuccess"), "¡£"));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ClearVDLFailed"), "¡£"));
        }
    }

    public List<String> getVDLPath(DatabaseParameter dbBean) {
        List<String> listVDLPath = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
       
            String strIP = dbBean.getStrServerName();
            String[] allIP = InterfaceFactory.getSCSIInterfaceInstance().getAllIP();
            if(strIP==null || allIP==null) {
                return listVDLPath;
            }
            for(String s : allIP){
                if(strIP.equals(s)){
                    bContain = true;
                    break;
                }
            }

            if(!bContain){
                isrender = true;
                return listVDLPath;
            }
            
            String url = "jdbc:postgresql://" + dbBean.getStrServerName() + ":"
                    + dbBean.getStrPort() + "/" + dbBean.getStrDBName();
            System.out.println(url);
            DriverManager.setLoginTimeout(15);
            conn = DriverManager.getConnection(url, dbBean.getStrUser(), dbBean.getStrPassword());
            String sql = "SELECT deviceaccessname FROM storagedevice WHERE devicetype = 'VirtualLibary'";
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("deviceaccessname");
                listVDLPath.add(name);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listVDLPath;
    }
    public String goBack() {
        return "maintenance?faces-redirect=true";
    }
}
