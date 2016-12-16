/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.util.SnapUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class CdpSnapshot implements Comparable {
    private CdpSnapshotInfo snapshot;
    private boolean saveRollEnabled, cancelRollEnabled, rollEnabled;
    private boolean saveRollRender, cancelRollRender, rollRender = true;

    public CdpSnapshot() {
    }

    public CdpSnapshot(CdpSnapshotInfo snapshot) {
        this.rollRender = true;
    }


    public boolean isRollRender() {
        return rollRender;
    }

    public void setRollRender(boolean rollRender) {
        this.rollRender = rollRender;
    }


    public boolean isSaveRollEnabled() {
        return saveRollEnabled;
    }

    public void setSaveRollEnabled(boolean saveRollEnabled) {
        this.saveRollEnabled = saveRollEnabled;
    }

    public boolean isCancelRollEnabled() {
        return cancelRollEnabled;
    }

    public void setCancelRollEnabled(boolean cancelRollEnabled) {
        this.cancelRollEnabled = cancelRollEnabled;
    }

    public boolean isRollEnabled() {
        return rollEnabled;
    }

    public void setRollEnabled(boolean rollEnabled) {
        this.rollEnabled = rollEnabled;
    }

    public boolean isSaveRollRender() {
        return saveRollRender;
    }

    public void setSaveRollRender(boolean saveRollRender) {
        this.saveRollRender = saveRollRender;
    }

    public boolean isCancelRollRender() {
        return cancelRollRender;
    }

    public void setCancelRollRender(boolean cancelRollRender) {
        this.cancelRollRender = cancelRollRender;
    }

    @Override
    public int compareTo(Object o) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        CdpSnapshot snap = (CdpSnapshot) o;

        return snap.snapshot.getSnapshotTime().compareTo(this.snapshot.getSnapshotTime());
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }

    public static Date stringToDate(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(SnapUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
