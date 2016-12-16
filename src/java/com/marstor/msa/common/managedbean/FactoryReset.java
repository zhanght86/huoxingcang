/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.volume.managedbean.*;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "factoryResetBean")
@ViewScoped
public final class FactoryReset implements Serializable {

    public String name;
    private boolean autoNet = false;

    /**
     * Creates a new instance of RebootBean
     */
    public FactoryReset() {
        name =new MSAResource().get("tip");
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        request.getSession().invalidate();
        System.out.println(request.getSession().getAttribute("user"));
        if(request.getParameter("autoNet") != null){
            String str = request.getParameter("autoNet");
            if(str.equals("1")){
              autoNet = true;
            }            
        }
         reboot2();

    }

    public void reboot2() {
         SystemOutPrintln.print_common(" reboot2()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                reboot();
            }
        });
        thread.start();
    }

    public void reboot() {
        try {
            Thread.sleep(30000);

       

            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
             SystemOutPrintln.print_common("It is reboot.");
            if(!common.rollbackFactorySnapshot(this.autoNet))
            {
                 SystemOutPrintln.print_common("rollback failed.");
            }
            boolean reboot = common.reboot();
            if (!reboot) {
                 SystemOutPrintln.print_common("reboot false");
            } else {
//                          RequestContext.getCurrentInstance().execute("window.top.location.href='/template/login.xhtml'");   
                 SystemOutPrintln.print_common("reboot success");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FactoryReset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean testURL() {
        try {
            URL url = new URL("");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            /**
             * public int getResponseCode()throws IOException �� HTTP ��Ӧ��Ϣ��ȡ״̬�롣
             * ���磬������״̬����˵�� HTTP/1.0 200 OK HTTP/1.0 401 Unauthorized ���ֱ𷵻� 200
             * �� 401�� ����޷�����Ӧ��ʶ���κδ��루����Ӧ������Ч�� HTTP�����򷵻� -1��
             *
             * ���� HTTP ״̬��� -1
             */
            int state = conn.getResponseCode();
            SystemOutPrintln.print_common(state+"");
            if (state == 200) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
}
