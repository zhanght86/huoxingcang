/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.backup.model.DatabaseParameter;
import com.marstor.msa.backup.model.DriveInfo;
import com.marstor.msa.backup.model.HostBean;
import com.marstor.msa.backup.model.StorageDeviceBean;
import com.marstor.msa.backup.model.TapeInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class QueryDatabase {

    public List<HostBean> getHostInfo(DatabaseParameter dbBean) {
        List<HostBean> listHostBean = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver"); 
            String url = "jdbc:postgresql://" + dbBean.getStrServerName() + ":"
                    + dbBean.getStrPort() + "/" + dbBean.getStrDBName();
            System.out.println(url);
            conn = DriverManager.getConnection(url, dbBean.getStrUser(), dbBean.getStrPassword());
            String sql = "SELECT clienthostid ,hostname,osname FROM clienthost";
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HostBean hostBean = new HostBean();
                hostBean.setiClienthostid(rs.getInt("clienthostid"));
                hostBean.setStrHost(rs.getString("hostname"));
                hostBean.setStrOSName(rs.getString("osname"));
                listHostBean.add(hostBean);
            }
            for (int i = 0; i < listHostBean.size(); i++) {
                int iLocale = 0;
                if (Locale.getDefault() == Locale.CHINA){
                    iLocale = 1;
                }else if (Locale.getDefault() == Locale.US){
                    iLocale = 2;
                }
                HostBean tempBean = listHostBean.get(i);
                sql = " SELECT message FROM resourceinfo WHERE resourceinfo.languagecode ="+iLocale+" AND resourceinfo.resourceid IN("
                        + "SELECT clienthostagent.resourceid FROM clienthostagent,agent WHERE "
                        + "clienthostagent.agentid = agent.agentid AND "
                        + "clienthostagent.clienthostid = " + tempBean.getiClienthostid()+")";
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                List<String> listAgent = new ArrayList();
                while (rs.next()) {
                    listAgent.add(rs.getString("message"));
                }
                tempBean.setListAgent(listAgent);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println(java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("SystemDatabaseConnection"));
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null){
                    rs.close();
                }
                if (stmt != null){
                    stmt.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listHostBean;
    }

    public List<StorageDeviceBean> getStorageDeviceInfo(DatabaseParameter dbBean) {
        List<StorageDeviceBean> listStorageDeviceBean = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rsTape = null;
        ResultSet rsDrive = null;
        Statement stmtTape = null;
        Statement stmtDrive = null;
        try {
            Class.forName("org.postgresql.Driver"); 
            String url = "jdbc:postgresql://" + dbBean.getStrServerName() + ":"
                    + dbBean.getStrPort() + "/" + dbBean.getStrDBName();
            System.out.println(url);
            conn = DriverManager.getConnection(url, dbBean.getStrUser(), dbBean.getStrPassword());
            String sql = "SELECT name,hostname,devicetype,slotnum FROM storagedevice";
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                StorageDeviceBean tempBean = new StorageDeviceBean();
                String strName = rs.getString("name");
                String strHostName = rs.getString("hostname");
                String strType = rs.getString("devicetype") ;
                tempBean.setStrName(strName);
                tempBean.setStrHostName(strHostName);
                tempBean.setStrType(strType);
                tempBean.setiSlotNum(rs.getInt("slotnum"));
                
                stmtDrive = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                sql = "SELECT drivenumber,islocked FROM driveinfo WHERE storagedeviceid IN "
                        + "(SELECT storagedeviceid FROM storagedevice WHERE name = \'"+strName+"\')" +"ORDER BY drivenumber";
                System.out.println(sql);
                rsDrive = stmtDrive.executeQuery(sql);
                List<DriveInfo> listDriveInfo = new ArrayList();
                while (rsDrive.next()){
                    DriveInfo driveBean = new DriveInfo();
                    driveBean.setiDriveNumber(rsDrive.getInt("drivenumber"));
                    driveBean.setiIsLocked(rsDrive.getInt("islocked"));
                    listDriveInfo.add(driveBean);
                }
                tempBean.setListDriveInfo(listDriveInfo);
                tempBean.setiDriveNum(listDriveInfo.size());
                
                stmtTape = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                sql = "SELECT barcode,label,remaincap,capacity,iswriteprotect,islocked,busedcycle,srcslot FROM tapeinfo WHERE storagedeviceid IN "
                        + "(SELECT storagedeviceid FROM storagedevice WHERE name = \'"+strName+"\') ORDER BY srcslot";
                System.out.println(sql);
                rsTape = stmtTape.executeQuery(sql);
                List<TapeInfo> listTapeInfo = new ArrayList();
                while(rsTape.next()){
                    TapeInfo tapeBean = new TapeInfo();
                    tapeBean.setStrBarCode(rsTape.getString("barcode"));
                    tapeBean.setStrLabel(rsTape.getString("label"));
                    tapeBean.setiIsLocked(rsTape.getInt("islocked"));
                    tapeBean.setiIsWriteProtect(rsTape.getInt("iswriteprotect"));
                    tapeBean.setiUsedCycle(rsTape.getInt("busedcycle"));
                    tapeBean.setlCapacity(rsTape.getLong("capacity"));
                    tapeBean.setlUsedSize(rsTape.getLong("remaincap"));
                    tapeBean.setiSrcslot(rsTape.getInt("srcslot"));
                    listTapeInfo.add(tapeBean);
                }
                tempBean.setListTapeInfo(listTapeInfo);
                listStorageDeviceBean.add(tempBean);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println(java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("SystemDatabaseConnection"));
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null){
                    rs.close();
                }
                if (rsTape != null){
                    rsTape.close();
                }
                if (rsDrive != null){
                    rsDrive.close();
                }
                if (stmt != null){
                    stmt.close();
                }
                if (stmtTape != null){
                    stmtTape.close();
                }
                if (stmtDrive != null){
                    stmtDrive.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return listStorageDeviceBean;
    }
    
    public List<String> getVDLPath(DatabaseParameter dbBean) {
        List<String> listVDLPath = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver"); 
            String url = "jdbc:postgresql://" + dbBean.getStrServerName() + ":"
                    + dbBean.getStrPort() + "/" + dbBean.getStrDBName();
            System.out.println(url);
            conn = DriverManager.getConnection(url, dbBean.getStrUser(), dbBean.getStrPassword());
            String sql = "SELECT deviceaccessname FROM storagedevice WHERE devicetype = 'VirtualLibary'";
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("deviceaccessname");
                listVDLPath.add(name);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println(java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("SystemDatabaseConnection"));
            Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null){
                    rs.close();
                }
                if (stmt != null){
                    stmt.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(QueryDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listVDLPath;
    }
}
