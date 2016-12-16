/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.ModuleBean;
import com.marstor.msa.common.treeNode.JavaDBConnect;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.mdu.axis2.client.EcdClient;
import com.marstor.msa.mdu.util.Debug;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author
 */
@ManagedBean(name = "register")
@ViewScoped
public class RegisterBean implements Serializable {

    private MSAResource res = new MSAResource();
    private List<ModuleBean> moduleList = null;
    private String systemKey = "";
    public static boolean test = false;
    private boolean bUpload = true;
    private boolean bRegister = true;
    private String register = null;
    private int taste = 1;


    public void setTaste(int a) {
        taste = a;
    }

    public int getTaste() {
        return taste;
    }

    public RegisterBean() {
        System.out.println("step 1");
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        systemKey = res.get("registercode") + commonInterfaceInstance.getSystemKey();
        System.out.println("step 2");
        setList();
         System.out.println("step 3");
        if ((moduleList == null) || moduleList.isEmpty()) {
            taste = 0;
        } else {
        }
        System.out.println("step 4");
    }

    /**
     * 启动时的信息
     */
    private void setList() {

        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();
        if (license == null) {
            return;
        }

        int count = license.length;
        moduleList = new ArrayList();

        for (int i = 0; i < count; i++) {
            Reg temp = license[i];

            String first = String.valueOf(temp.getModuleID());
            String module = "";
            try {
                module = java.util.ResourceBundle.getBundle("com/marstor/msa/common/util/resources/MyUtility").getString(res.get("module_") + first);
            } catch (Exception ex) {

                Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
                continue;

            }
            
            String second = String.valueOf(temp.getResouceID());
            String describe = java.util.ResourceBundle.getBundle("com/marstor/msa/common/util/resources/LicenseManagerDialog").getString(res.get("code_") + second);

            String third = String.valueOf(temp.getUnitResourceID());
            String value = "";
            if (temp.getUnitResourceID() >= 0 && temp.getUnitResourceID() <= 2) {
                if (0 != temp.getUnitResourceID()) {
                    value = temp.getFunctionNumber() + java.util.ResourceBundle.getBundle("com/marstor/msa/common/util/resources/LicenseManagerDialog").getString(res.get("unit_") + third);
                } else {
                    value = java.util.ResourceBundle.getBundle("com/marstor/msa/common/util/resources/LicenseManagerDialog").getString(res.get("unit_") + third);
                }
            } else {
                value = String.valueOf(temp.getFunctionNumber());
            }

            if (Module.FUNCTIONID_COMMON == license[i].getFunctionID()) {
                if (Module.MODULE_CDP != license[i].getModuleID()) {
                    moduleList.add(new ModuleBean(module, describe, value));
                }
            }

            if (Module.MODULE_BASIC == license[i].getModuleID() && Module.FUNCTIONID_BASIC_DISK_CAPACITY == license[i].getFunctionID()) {
                moduleList.add(new ModuleBean(module, describe, value));
            }

            if (Module.MODULE_CDP == license[i].getModuleID() && Module.FUNCTIONID_CDP_CAPACITY == license[i].getFunctionID()) {
                moduleList.add(new ModuleBean(module, describe, value));
            }
        }
    }

    /**
     * 具体上传文件
     *
     * @param event
     * @throws IOException
     */
    public void fileUpload(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        UploadedFile file = event.getFile();
        System.out.println(file.getFileName());

        InputStream input = null;
        try {
            input = file.getInputstream();
        } catch (Exception ex) {
            Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    res.get("importFailed1"), ""));
            return;
        }
        try {
            XMLParser parser = new XMLParser(input);
            int count = parser.getNodeCount("CustomRegisterInfo/RegInfo/RegItem/Reg");
            if (0 == count) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        res.get("importFailed2"), ""));
                return;
            }

            String[] SerialNumbers = new String[count];
            for (int i = 0; i < count; i++) {
                SerialNumbers[i] = parser.getNodeContent("CustomRegisterInfo/RegInfo/RegItem/Reg", i);
            }

            CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
            boolean addLicense = commonInterfaceInstance.addLicense(SerialNumbers);

            if (addLicense) {
                Reg[] license = commonInterfaceInstance.getLicense();
                for (int i = 0; i < license.length; i++) {
                    if ((Module.MODULE_ECD == license[i].getModuleID() && Module.FUNCTIONID_ECD == license[i].getFunctionID())) {
                        File db = new File("/var/msa/ecd");
                        if (!db.exists()) {
                            break;
                        }
//                        Debug.print("Register Module ID : " + license[i].getModuleID());
//                        com.marstor.msa.sync.util.Debug.print("GroupNum=" + license[i].getGroupNumber());
//                        com.marstor.msa.sync.util.Debug.print("UserNum=" + license[i].getUserNumber());                       

                        boolean result = ECDRegister(999999, license[i].getFunctionNumber());
                        if(!result){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "ECD注册信息写入失败。", ""));
                        }
                        break;
                    }
                }
                RequestContext.getCurrentInstance().execute("jump.show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        res.get("importFailed3"), ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    res.get("importFailed3"), ""));
        }
    }

    public boolean ECDRegister(int groupNum, int userNum) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpClient httpclient = new HttpClient();
        
        String uuid = UUID.randomUUID().toString();
        int temp = groupNum * userNum / 3;
        String tempX16 = "" + Integer.toHexString(temp);
        String tempKey = uuid.replaceAll("-", "").substring(0, 8) + tempX16 + uuid.replaceAll("-", "").substring(20, 31);

        GetMethod getMethod = new GetMethod("http://127.0.0.1/ecd/services/ecd/modifyLicense?groupnum=" + groupNum
                + "&usernum=" + userNum + "&uuid=" + uuid + "&secretkey=" + tempKey);
        try {
            Debug.print("DB Operation URK:" + getMethod.getURI().toString());
        } catch (URIException ex) {
            Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            httpclient.executeMethod(getMethod);
            Debug.print("After execute Method.");
            byte[] responseBody = getMethod.getResponseBody();
            String result = new String(responseBody, "utf-8");
            Debug.print("Interface Result:" + result);
            if(result.contains("true")){
                return true;
            }else{
                return false;
            }
//        Debug.print("groupNum=" + groupNum);
//        Debug.print("userNum=" + userNum);
//        try {
//            DriverManager.getConnection("jdbc:derby:;shutdown=true");
//        } catch (SQLException ex) {
//            Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        JavaDBConnect db = new JavaDBConnect();
//        if (!db.connect()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "连接数据库失败。", ""));
//            return;
//        }
//        db.update(groupNum, userNum);
        } catch (HttpException e) {
            // TODO: handle exception
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("the line is wrong!");
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();//释放链接
//            postMethod.releaseConnection();
        }
        return false;
    }

    public void setModuleList(List<ModuleBean> list) {
        this.moduleList = list;
    }

    public List<ModuleBean> getModuleList() {
        return moduleList;
    }

    public String getSystemCode() {
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        systemKey = res.get("registercode") + commonInterfaceInstance.getSystemKey();
        return systemKey;
    }

    public void jumpPage() {
        RequestContext.getCurrentInstance().execute("window.top.location.href='/template/framework.xhtml'");
    }

    public void jumpPage1() {
        RequestContext.getCurrentInstance().execute("jump .hide()");
    }

    public void closePage() {
        taste = 1;
        RequestContext.getCurrentInstance().execute("prompt.hide()");
    }

    public void test() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                res.get("importFailed1"), ""));
        RequestContext.getCurrentInstance().execute("prompt.show()");
    }
}
