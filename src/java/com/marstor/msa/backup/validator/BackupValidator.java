/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class BackupValidator {
        /*
     * ���˿ڸ�ʽ
     */
    public static boolean checkPort(String port)
    {
        Pattern p = Pattern.compile("[0-9]{1,5}$");
        Matcher m = p.matcher(port);
        
        if(m.matches()){
            if(Integer.valueOf(port) > 65535){
                return false;
            }
            
            if(Integer.valueOf(port) < 1){
                return false;
            }
        }
        
        return m.matches();
    }
    
    /*
     * �����Ҫ�������͵ĸ�ʽ
     */
    public static boolean checkInt(String str)
    {
        Pattern p = Pattern.compile("^[0-9]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /*
     * ���ͨ��������ĸ�ʽ
     */
    public static boolean checkChannelName(String str)
    {
        Pattern p = Pattern.compile("^([a-z]|[A-Z]){1}[\\S\\s]*$");
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /*
     * ����������������ĸ�ʽ
     */
    public static boolean checkNetServiceName(String netServiceName)
    {
        Pattern p = Pattern.compile("^([a-z]|[A-Z]|[0-9]|_){1,16}$");
        Matcher m = p.matcher(netServiceName);
        return m.matches();
    }
    /*
     * ���RMAN·���ĸ�ʽ
     */
    public static boolean checkRMANPath(String path)
    {
        Pattern p = Pattern.compile("^(/{1}[\\S\\s]+/{1})|((([a-z]|[A-Z]):\\\\){1}[\\S\\s]+\\\\{1})|((/){1})|((([a-z]|[A-Z]):\\\\){1})$");
        Matcher m = p.matcher(path);
        return m.matches();
    }

    
}
