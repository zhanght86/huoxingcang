/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;
import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
/**
 *
 * @author Administrator
 */
public class ISCSITarget {
    private String name;
    private String alias;
    private String status ="";
    private Chap chap = new Chap();
   // private IPBinding bing = new IPBinding();
    private boolean isStartBind;
    private boolean  isStartChap;
    //private boolean authenInfo;
    private ArrayList<IPBinding>  bindings = new ArrayList<IPBinding>();
    private TargetInformation  targetInfo;

    public ISCSITarget(String name, String alias, String status,Chap  chap,boolean isStartBind) {
        this.name = name;
        this.alias = alias;
        this.status = status;
        this.chap = chap;
        this.isStartBind = isStartBind;
//        this.isStart = isStart;
//        this.authenInfo = authenInfo;
    }

    public ISCSITarget() {
    }

    public ISCSITarget(TargetInformation targetInfo) {
        this.targetInfo = targetInfo;
        String  state = targetInfo.getState();
        MSAResource res = new MSAResource();
        if(state.equals("Online")) {
            this.status = res.get("online");
        }else if(status.equals("Offline")) {
            this.status = res.get("offLine");
        }else if(status.equals("Offlining")) {
            this.status = res.get("offLining");
        }
    }

    public boolean isIsStartChap() {
//        if(this.targetInfo.chapUser.equals("<none>")) {
//            return  false;
//        }else {
//            return  true;
//        }
        if(this.targetInfo.getAuthType()==0) {
            return  false;
        }else {
            return  true;
        }
       // return isStartChap;
    }

    public void setIsStartChap(boolean isStartChap) {
        this.isStartChap = isStartChap;
    }

    public TargetInformation getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(TargetInformation targetInfo) {
        this.targetInfo = targetInfo;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Chap getChap() {
        return chap;
    }

    public void setChap(Chap chap) {
        this.chap = chap;
    }

    //    public IPBinding getBing() {
    //        return bing;
    //    }
    //
    //    public void setBing(IPBinding bing) {
    //        this.bing = bing;
    //    }
    public ArrayList<IPBinding> getBindings() {
        return bindings;
    }

    public void setBindings(ArrayList<IPBinding> bindings) {
        this.bindings = bindings;
    }

    //    public boolean isIsStart() {
    //        return isStart;
    //    }
    //
    //    public void setIsStart(boolean isStart) {
    //        this.isStart = isStart;
    //    }
    //
    //    public boolean isAuthenInfo() {
    //        return authenInfo;
    //    }
    //
    //    public void setAuthenInfo(boolean authenInfo) {
    //        this.authenInfo = authenInfo;
    //    }
    public boolean isIsStartBind() {
        if(this.targetInfo.tpgs.size()==1&&this.targetInfo.tpgs.get(0).equals("default")) {
            return  false;
        }else {
            return  true;
        }
        //return isStartBind;
    }

    public void setIsStartBind(boolean isStartBind) {
        this.isStartBind = isStartBind;
    }
    
    
}
