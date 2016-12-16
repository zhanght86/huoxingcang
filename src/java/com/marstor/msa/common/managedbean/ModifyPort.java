/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.common.util.MSAResource;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.primefaces.context.RequestContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "modifyPort")
@ViewScoped
public class ModifyPort implements Serializable{

    private MSAResource res = new MSAResource();
    private String strName;

    public ModifyPort() {
    }

    public void modify() {
        boolean flag = this.modifyPort();
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("modifyFailed"), ""));
            return;
        }
        RequestContext.getCurrentInstance().execute("window.top.location.href='../volume/rebootX.xhtml?port="+ strName + "'");
    }
    
    public boolean modifyPort() {
        try {
// 1.得到DOM解析器的工厂实例
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
// 2.从DOM工厂里获取DOM解析器
            DocumentBuilder db = dbf.newDocumentBuilder();
// 3.解析XML文档，得到document，即DOM树
            Document doc = db.parse("/var/tomcat6/conf/server.xml");

            NodeList list = doc.getElementsByTagName("Connector");
            for (int i = 0; i < list.getLength(); i++) {
                Element brandElement = (Element) list.item(i);
                String protocol = brandElement.getAttribute("protocol");
                if(protocol.contains("HTTP")){
                    brandElement.setAttribute("port", this.strName);
                }
            }

//保存xml文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            
//设置编码类型
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            StreamResult result = new StreamResult(new FileOutputStream("/var/tomcat6/conf/server.xml"));
//把DOM树转换为xml文件
            transformer.transform(domSource, result);  
            
            result = new StreamResult(new FileOutputStream("/SYSVOL/SYSTEM/tomcat6/conf/server.xml"));
//把DOM树转换为xml文件
            transformer.transform(domSource, result);  
            

        } catch (Exception ex) {            
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public void clickModify() {
        if (strName == null || strName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostnamenotnull"), ""));
            return;
        }
                
        
        if(!AgentUtility.checkNum(strName, false) || !AgentUtility.checkPort(strName)) {
          //  hostNameLength
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostNameLength"), ""));
            return;
        }
        RequestContext.getCurrentInstance().execute("modify.show();");
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
}
