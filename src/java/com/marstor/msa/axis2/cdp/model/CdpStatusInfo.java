/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class CdpStatusInfo implements Serializable{

    private int ExistSnapshot;//�Ƿ��п��գ�1�ǣ�0����
    private int IsSrcSync;//�Ƿ���ͬ��Դ�ļ�ϵͳ��1�ǣ�0����
    private int IsDescSync;//�Ƿ���ͬ��Ŀ���ļ�ϵͳ��1�ǣ�0����
    private int IsMount;//Ŀ���ļ�ϵͳ�Ƿ���أ�1�ǣ�0����
    private int IsPause;//Ŀ���ļ�ϵͳ�Ƿ�����ͣ״̬��1�ǣ�0����
    private int IsLocalDescSync;
    private int IsRollback;

    public CdpStatusInfo() {
    }

    public CdpStatusInfo(int ExistSnapshot, int IsSrcSync, int IsDescSync, int IsMount, int IsPause, int IsLocalDescSync, int IsRollback) {
        this.ExistSnapshot = ExistSnapshot;
        this.IsSrcSync = IsSrcSync;
        this.IsDescSync = IsDescSync;
        this.IsMount = IsMount;
        this.IsPause = IsPause;
        this.IsLocalDescSync = IsLocalDescSync;
        this.IsRollback = IsRollback;
    }

    public int getExistSnapshot() {
        return ExistSnapshot;
    }

    public void setExistSnapshot(int ExistSnapshot) {
        this.ExistSnapshot = ExistSnapshot;
    }

    public int getIsSrcSync() {
        return IsSrcSync;
    }

    public void setIsSrcSync(int IsSrcSync) {
        this.IsSrcSync = IsSrcSync;
    }

    public int getIsDescSync() {
        return IsDescSync;
    }

    public void setIsDescSync(int IsDescSync) {
        this.IsDescSync = IsDescSync;
    }

    public int getIsMount() {
        return IsMount;
    }

    public void setIsMount(int IsMount) {
        this.IsMount = IsMount;
    }

    public int getIsPause() {
        return IsPause;
    }

    public void setIsPause(int IsPause) {
        this.IsPause = IsPause;
    }

    public int getIsLocalDescSync() {
        return IsLocalDescSync;
    }

    public void setIsLocalDescSync(int IsLocalDescSync) {
        this.IsLocalDescSync = IsLocalDescSync;
    }

    public int getIsRollback() {
        return IsRollback;
    }

    public void setIsRollback(int IsRollback) {
        this.IsRollback = IsRollback;
    }

}
