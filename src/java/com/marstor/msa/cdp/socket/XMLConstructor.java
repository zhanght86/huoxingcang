/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.cdp.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.*;

public class XMLConstructor 
{
    protected Element element = null;
    protected DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;

    {
        try
        {
            builder = factory.newDocumentBuilder();
        }
        catch (Exception e)
        {
        }
    }
    protected Document document = (Document) builder.newDocument();
    protected XPathFactory m_pathFactory = XPathFactory.newInstance();
    protected XPath m_path = m_pathFactory.newXPath();
    protected String m_xmlPath = "";
    protected NodeList m_nodeList = null;
    /**xml编码格式*/
    public static String xmlEncoding = "utf-8";

    public XMLConstructor()
    {
        this.element = createBaseElement();
//        document.appendChild(element);
    }
    
    public XMLConstructor(int commandID,String description,String receiveModule, String sendModule, String guid)
    {
        this.element = createElement("MSA");
        this.document.appendChild(element);

        XMLConstructor base = new XMLConstructor("Base");
        base.addNode("CommandID", commandID);
        base.addNode("Description", description);
        base.addNode("ReceiveModule", receiveModule);
        base.addNode("SendModule",sendModule);
        base.addNode("Option",0);
        this.addNode(base);
        
        XMLConstructor parameters = new XMLConstructor("Parameters");
        parameters.addNode("Group", guid);
        this.addNode(parameters);
    }
    
    public XMLConstructor(int commandID,String description,String receiveModule, String sendModule)
    {
        this.element = createElement("MSA");
        this.document.appendChild(element);

        XMLConstructor base = new XMLConstructor("Base");
        base.addNode("CommandID", commandID);
        base.addNode("Description", description);
        base.addNode("ReceiveModule", receiveModule);
        base.addNode("SendModule",sendModule);
        base.addNode("Option",0);
        this.addNode(base);
    }
    
    public XMLConstructor(int commandID,String description,String receiveModule, String sendModule, int option)
    {
        this.element = createElement("MSA");
        this.document.appendChild(element);

        XMLConstructor base = new XMLConstructor("Base");
        base.addNode("CommandID", commandID);
        base.addNode("Description", description);
        base.addNode("ReceiveModule", receiveModule);
        base.addNode("SendModule",sendModule);
        base.addNode("Option",option);
        this.addNode(base);
    }

    public XMLConstructor(String nodeName, String nodeValue)
    {
        this.element = createElement(nodeName);
        this.element.appendChild(document.createTextNode(nodeValue));
        this.document.appendChild(element);
    }

    public XMLConstructor(String nodeName)
    {
        this.element = createElement(nodeName);
        this.document.appendChild(element);
    }

    public XMLConstructor(File file)
    {
        try
        {
            document = builder.parse(file);
            element = document.getDocumentElement();
        }
        catch (Exception ex)
        {
            Logger.getLogger(XMLConstructor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean setNodeValue(String type, String value)
    {
        boolean succeed = true;
        try
        {
            NodeList typeL = (NodeList) m_path.evaluate("MarsVtl/License/LicenseType", document, XPathConstants.NODESET);
            int count = 0;
            for (int i = 0; i < typeL.getLength(); i++)
            {
                Node typeN = typeL.item(i);
                String content = typeN.getNodeName();
                if (content.equals(type))
                {
                    count = i;
                }
            }
            NodeList codeL = (NodeList) m_path.evaluate("MarsVtl/License/Code", document, XPathConstants.NODESET);
            Node codeN = codeL.item(count);
            codeN.setTextContent(value);
        }
        catch (XPathExpressionException ex)
        {
            ex.printStackTrace();
            return false;
        }
        return succeed;
    }

    public boolean addNode(String name)
    {
        boolean succeed = true;
        Element newE = null;

        try
        {
            newE = createElement(name);
            this.element.appendChild(newE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            succeed = false;
        }

        return succeed;
    }

    public boolean addNode(String name, String value)
    {
        boolean done = true;
        Element newE = null;
        if (value == null)
        {
            value = "";
        }
        try
        {
            newE = createElement(name);
            //newE.setNodeValue(value);
            newE.appendChild(document.createTextNode(value));
            this.element.appendChild(newE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return done;
    }

    public boolean addNode(String name, long value)
    {
        return addNode(name, String.valueOf(value));
    }

    public boolean addNode(String name, int value)
    {
        return addNode(name, String.valueOf(value));
    }

    public boolean addNode(String name, char value)
    {
        return addNode(name, String.valueOf(value));
    }

    public boolean addNode(String name, byte value)
    {
        return addNode(name, String.valueOf(value));
    }

    public boolean addNode(String name, byte[] value)
    {
        boolean done = true;

        try
        {
            done = addNode(name, new String(value, xmlEncoding));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return done;
    }

    private void addNode(Element parent, Node child)
    {
        if (parent == null || child == null)
        {
            return;
        }

        Node node = child.getFirstChild();

        String nodeName = child.getNodeName();
        String nodeValue = "";

        if (node != null && node.getNodeType() == Node.TEXT_NODE)
        {
            nodeValue = node.getNodeValue();
        }

        Element KKKKK = this.createElement(nodeName);
        KKKKK.appendChild(document.createTextNode(nodeValue));
        parent.appendChild(KKKKK);

        while (node != null)
        {
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                addNode(KKKKK, node);
            }

            node = node.getNextSibling();
        }



    }

    public boolean addNode(XMLConstructor xmlGen)
    {
        boolean done = true;

        try
        {
            Element e = (Element) xmlGen.getElement();
            addNode(this.element, e);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            done = false;
        }

        return done;
    }

    public boolean addNode(String xPath, XMLConstructor xmlGen)
    {
        boolean done = true;

        try
        {
            Element addedElement = (Element) m_path.evaluate(xPath, document, XPathConstants.NODE);
            Element e = (Element) xmlGen.getElement();
            addNode(addedElement, e);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            done = false;
        }

        return done;
    }


    
     public boolean addNode(String xPath, String nodeName, String textContent)
    {
        boolean done = true;
        try
        {
            Element node= (Element) m_path.evaluate(xPath, document,XPathConstants.NODE);
            Element e = document.createElement(nodeName);
            if(!textContent.equals(""))
            {
                e.appendChild(document.createTextNode(textContent));
            }
            node.appendChild(e);
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return done;
    }

    public boolean addNode(Element element)
    {
        boolean done = true;

        if (element == null)
        {
            return done;
        }

        this.addNode(this.element, element);

        return done;
    }

    public boolean removeNode(String xPath)
    {
        boolean done = true;

        try
        {
            Element removedElement = (Element) m_path.evaluate(xPath, document, XPathConstants.NODE);
            int index = xPath.lastIndexOf("/");
            String parentXPath = xPath.substring(0, index);
            Element parentElement = (Element) m_path.evaluate(parentXPath, document, XPathConstants.NODE);
            
            parentElement.removeChild(removedElement);
     
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            done = false;
        }

        return done;
    }
    
    public String toXmlString()
    {
        StringWriter writer = new StringWriter();
        try
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");   
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty(omit-xml-declaration , "no");
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    public boolean toSaveXML(String path, String encoding)
    {
        try
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            PrintWriter pw = new PrintWriter(new File(path));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Element getElement()
    {
        return this.element;
    }

    protected Element createElement(String nodeName)
    {
        return document.createElement(nodeName);
    }

    protected Element createBaseElement(String licenseType, String code)
    {

        Element root = document.createElement("MarsVtl");
        Element baseE = document.createElement("License");
        Element typeE = document.createElement("LicenseType");
        typeE.appendChild(document.createTextNode(licenseType));
        baseE.appendChild(typeE);
        Element codeE = document.createElement("Code");
        codeE.appendChild(document.createTextNode(code));
        baseE.appendChild(codeE);
        root.appendChild(baseE);
        return root;
    }

    protected Element createBaseElement()
    {
        Element root = document.createElement("MarsVtl");
        return root;
    }

    protected Element createXmlBaseElement()
    {
        Element root = document.createElement("MarsVtl");
        Element base = document.createElement("Base");
        root.appendChild(base);
        return root;
    }

    public byte[] toByte()
    {
        try {
            Source source = new DOMSource(document);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            
            transformer.transform(source, result);
            return out.toByteArray();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;       
    }
}
