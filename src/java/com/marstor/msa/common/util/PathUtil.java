/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.util;
import java.io.UnsupportedEncodingException;  
import java.net.URL;  
import java.net.URLDecoder;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
/**
 *
 * @author Administrator
 */
@ManagedBean(name = "pathUtil")
@SessionScoped
public class PathUtil {
    
    public String webInfPath;
    
    public String getWebInfoPath(){  
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();  
        String path = url.toString();  
        int index = path.indexOf("WEB-INF");  
          
        if(index == -1){  
            index = path.indexOf("classes");  
        }  
          
        if(index == -1){  
            index = path.indexOf("bin");  
        }  
          
        path = path.substring(0, index);  
          
        if(path.startsWith("zip")){//��class�ļ���war��ʱ����ʱ����zip:D:/...������·��  
            path = path.substring(4);  
        }else if(path.startsWith("file")){//��class�ļ���class�ļ���ʱ����ʱ����file:/D:/...������·��  
            path = path.substring(6);  
        }else if(path.startsWith("jar")){//��class�ļ���jar�ļ�����ʱ����ʱ����jar:file:/D:/...������·��  
            path = path.substring(10);   
        }  
        try {  
            path =  URLDecoder.decode(path, "UTF-8");  
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
   //     System.out.println("&&&&&&&&&&&&" + path.substring(1, 2));
        
        if(!path.substring(1,2).equals(":")){
            path = "/" + path;
        }
          
        return path;  
    }  

    
    public String getLanguagePath(){  
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();  
        String path = url.toString();  
        int index = path.indexOf("WEB-INF");  
          
        if(index == -1){  
            index = path.indexOf("classes");  
        }  
          
        if(index == -1){  
            index = path.indexOf("bin");  
        }  
          
        path = path.substring(0, index);  
          
        if(path.startsWith("zip")){//��class�ļ���war��ʱ����ʱ����zip:D:/...������·��  
            path = path.substring(4);  
        }else if(path.startsWith("file")){//��class�ļ���class�ļ���ʱ����ʱ����file:/D:/...������·��  
            path = path.substring(6);  
        }else if(path.startsWith("jar")){//��class�ļ���jar�ļ�����ʱ����ʱ����jar:file:/D:/...������·��  
            path = path.substring(10);   
        }  
        try {  
            path =  URLDecoder.decode(path, "UTF-8");  
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
    //  System.out.println("&&&&&&&&&&&&" + path.substring(1, 2));
        
        if(!path.substring(1,2).equals(":")){
            path = "/" + path;
        }
          
        return path+"resources/languages/";  
    }  
    
    public String getVerifyCodePath(){  
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();  
        String path = url.toString();  
        int index = path.indexOf("WEB-INF");  
          
        if(index == -1){  
            index = path.indexOf("classes");  
        }  
          
        if(index == -1){  
            index = path.indexOf("bin");  
        }  
          
        path = path.substring(0, index);  
          
        if(path.startsWith("zip")){//��class�ļ���war��ʱ����ʱ����zip:D:/...������·��  
            path = path.substring(4);  
        }else if(path.startsWith("file")){//��class�ļ���class�ļ���ʱ����ʱ����file:/D:/...������·��  
            path = path.substring(6);  
        }else if(path.startsWith("jar")){//��class�ļ���jar�ļ�����ʱ����ʱ����jar:file:/D:/...������·��  
            path = path.substring(10);   
        }  
        try {  
            path =  URLDecoder.decode(path, "UTF-8");  
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        if(!path.substring(1,2).equals(":")){
            path = "/" + path;
        }
        
        path = path.replaceAll("/", "\\\\");
        
 //       System.out.println("&&&&&&&&&&&&&&&&&" + path);
        
        return path + "resources\\common\\picture\\verifycode.jpg";  
    }  

    public void setWebInfPath(String webInfPath) {
        this.webInfPath = webInfPath;
    }
    
        public String getLogPath(){  
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();  
        String path = url.toString();  
        int index = path.indexOf("WEB-INF");  
          
        if(index == -1){  
            index = path.indexOf("classes");  
        }  
          
        if(index == -1){  
            index = path.indexOf("bin");  
        }  
          
        path = path.substring(0, index);  
          
        if(path.startsWith("zip")){//��class�ļ���war��ʱ����ʱ����zip:D:/...������·��  
            path = path.substring(4);  
        }else if(path.startsWith("file")){//��class�ļ���class�ļ���ʱ����ʱ����file:/D:/...������·��  
            path = path.substring(6);  
        }else if(path.startsWith("jar")){//��class�ļ���jar�ļ�����ʱ����ʱ����jar:file:/D:/...������·��  
            path = path.substring(10);   
        }  
        try {  
            path =  URLDecoder.decode(path, "UTF-8");  
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
    //  System.out.println("&&&&&&&&&&&&" + path.substring(1, 2));
        
        if(!path.substring(1,2).equals(":")){
            path = "/" + path;
        }
          
        return path+"resources/log/";  
    } 
    
}
