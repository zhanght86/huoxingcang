/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.AggregationInformation;
import com.marstor.msa.common.model.NICAggregation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.el.ELContext;
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
@ManagedBean(name = "nICAggregationBean")
@ViewScoped
public class NICAggregationBean implements Serializable{

    public List<NICAggregation> nicAggList;
    public NICAggregation nicAgg;
    public NICAggregation selectAgg;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_network";
    private String deleteaggtip;
    private String deleteaggiptip;

    /**
     * Creates a new instance of NICAggregationImpl
     */
    public NICAggregationBean() {
        deleteaggtip = "确定要删除该链路聚合吗？";
        deleteaggiptip = "选择网卡正在被当前连接使用,修改后必须重新登录,继续操作吗？";
        addNicAggList();

    }

    private void addNicAggList() {
        nicAggList = new ArrayList();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        AggregationInformation[] aggrs = common.getAggregation();
        if (aggrs != null && aggrs.length > 0) {
//            Arrays.sort(aggrs);
            for (int i = 0; i < aggrs.length; i++) {

                nicAgg = new NICAggregation();
                nicAgg.setName(aggrs[i].getName());
                String nicsStr = "";
                String[] nics = aggrs[i].getNics();
                if (nics != null && nics.length != 0) {
                    for (int j = 0; j < nics.length; j++) {
                        nicsStr = nicsStr + nics[j] + " ";
                    }
                }
                nicAgg.setNic(nicsStr);
                String ip = aggrs[i].getAddress();
                if (aggrs[i].getNetmask() != null && !aggrs[i].getNetmask().equals("")) {
                    ip = ip + "/" + aggrs[i].getNetmask();
                }
                nicAgg.setIp(ip);
                nicAgg.setSubnet(aggrs[i].getNetmask());
                nicAgg.setIpmp(aggrs[i].getGroupName());  //错误，其实真实值为聚合的名字
                String modelStr = res.get(basename, "off");
                if (aggrs[i].getModel() == AggregationInformation.OFF) {
                    modelStr = res.get(basename, "off");
                } else if (aggrs[i].getModel() == AggregationInformation.ACTIVE) {
                    modelStr = res.get(basename, "active");
                } else if (aggrs[i].getModel() == AggregationInformation.PASSIVE) {
                    modelStr = res.get(basename, "passive");
                }
                nicAgg.setMode(modelStr);
                nicAgg.setEnable(aggrs[i].isIsEnable());
                String dhcp = res.get(basename, "yes");
                if (aggrs[i].isIsEnable() == true) {
                    if (aggrs[i].isIsStatic() == false) {
                        dhcp = res.get(basename, "yes");
                    } else {
                        dhcp = res.get(basename, "no");
                    }
                } else {
                    dhcp = "";
                }

                nicAgg.setState(dhcp);
                nicAgg.setGateway(aggrs[i].getGateway());

                nicAggList.add(nicAgg);

            }

        }

    }

    public List<NICAggregation> getNicAggList() {
        
        return nicAggList;
    }

    public void setNicAggList(List<NICAggregation> nicAggList) {
        this.nicAggList = nicAggList;
    }

    public NICAggregation getSelectAgg() {
        return selectAgg;
    }

    public void setSelectAgg(NICAggregation selectAgg) {
        this.selectAgg = selectAgg;
    }

    public void selectAgg(NICAggregation select) {
        selectAgg = select;
         SystemOutPrintln.print_common("set  selectAgg=" + selectAgg.getName());
    }

    public void deleteAgg() {
//        selectAgg= select;
         SystemOutPrintln.print_common("删除聚合selectAgg ip=" + selectAgg.ip);
        String ip = "";
        if (selectAgg.getIp() != null && !selectAgg.getIp().equals("")) {
            ip = selectAgg.getIp().split("/")[0];
        } else {
            ip = "";
        }


        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String ipStr = request.getLocalAddr();
        boolean bCurrentIP = false;
        if (ip.equals(ipStr)) {
            bCurrentIP = true;
            //弹出警告框           Constants.showWarningMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("选择网卡正在被当前连接使用"));
        }

        if (bCurrentIP) {
            deleteaggiptip = res.get(basename, "deleteaggip_left") + selectAgg.name + res.get(basename, "deleteaggip_right");
            RequestContext.getCurrentInstance().execute("deleteAggip.show()");
            return;

        } else {
            deleteaggtip = res.get(basename, "delete_left") + selectAgg.name + res.get(basename, "delete_right");
            RequestContext.getCurrentInstance().execute("deleteAgg.show()");
            return;
        }



//        nicAggList.remove(selectAgg);
    }

    public void deleteAgg_real() {
        String ip = "";
        if (selectAgg.getIp() != null && !selectAgg.getIp().equals("")) {
            ip = selectAgg.getIp().split("/")[0];
        } else {
            ip = "";
        }
         SystemOutPrintln.print_common("delete agg ip="+ip);
        AggregationInformation aggr = new AggregationInformation();

        aggr.setAddress(ip);
        aggr.setGateway(selectAgg.getGateway());
        aggr.setGroupName(selectAgg.getIpmp());
        aggr.setIsEnable(selectAgg.isEnable());

        if (selectAgg.getState().equals(res.get(basename, "yes")) || selectAgg.getState().equals("")) {
            aggr.setIsStatic(false);
        } else {
            aggr.setIsStatic(true);
        }
        int modelStr = AggregationInformation.OFF;
        if (selectAgg.getMode().equals(res.get(basename, "off"))) {
            modelStr = AggregationInformation.OFF;
        } else if (selectAgg.getMode().equals(res.get(basename, "active"))) {
            modelStr = AggregationInformation.ACTIVE;
        } else if (selectAgg.getMode().equals(res.get(basename, "passive"))) {
            modelStr = AggregationInformation.PASSIVE;
        }
           SystemOutPrintln.print_common("delete agg modelStr="+modelStr);
        aggr.setModel(modelStr);
//        System.out.println("删除聚合selectAgg modelStr=" + modelStr);
        aggr.setName(selectAgg.getName());
//        System.out.println("删除聚合selectAgg name=" + selectAgg.getName());
        aggr.setNetmask(selectAgg.getSubnet());
//        System.out.println("删除聚合selectAgg getSubnet=" + selectAgg.getSubnet());
        String[] nics = selectAgg.getNic().split(" ");
        for(int k=0;k<nics.length;k++){
                SystemOutPrintln.print_common("删除聚合selectAgg nic i=" + nics[k]);
        }
        aggr.setNics(nics);
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean modifyNic = common.deleteAggregation(aggr);
        if (modifyNic == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deletenic_fail"), ""));
            return;
        }
         SystemOutPrintln.print_common("delete agg  succeed");
        addNicAggList();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        NetWorkCardBean lBean = (NetWorkCardBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "netWorkCardBean");
        lBean.myInit();
    }
    
    public String toModifyAgg() {

        String param = "aggName=" + selectAgg.name;
         SystemOutPrintln.print_common("aggName=" + selectAgg.name);
        return "system_network_modifyaggregate?faces-redirect=true&amp;" + param;
    }

    public String getDeleteaggtip() {
        return deleteaggtip;
    }

    public void setDeleteaggtip(String deleteaggtip) {
        this.deleteaggtip = deleteaggtip;
    }

    public String getDeleteaggiptip() {
        return deleteaggiptip;
    }

    public void setDeleteaggiptip(String deleteaggiptip) {
        this.deleteaggiptip = deleteaggiptip;
    }
    
    
}
