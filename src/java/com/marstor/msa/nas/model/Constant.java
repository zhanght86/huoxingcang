/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Constant implements Serializable{

    public static final int positiveControl = 1;
    public static final int update = 2;
    public static final int readAndExecute = 3;
    public static final int listFolderAndContent = 4;
    public static final int read = 5;
    public static final int write = 6;
    public static final int special = 7;
    public static final int readFile = 8;
    public static final int listContent = 9;
    public static final int readBasicAttribute = 10;
    public static final int readExpandAttribute = 11;
    public static final int readACL = 12;
    public static final int writeFile = 13;
    public static final int writeFileOwner = 14;
    public static final int writeFileBasicAttribute = 15;
    public static final int writeFileExpandAttribute = 16;
    public static final int writeACl = 17;
    public static final int createFile = 18;
    public static final int createSubDirectory = 19;
    public static final int execute = 20;
    public static final int deleteFile = 21;
    public static final int deleteChild = 22;
    public static final int fileInherit = 23;
    public static final int dirInherit = 24;
    public static final int delete = 25;
    public static final int executeFile = 26;
    public static final int inherit = 27;
    public static final int fetch = 28;
    public static final int writeIn = 29;
    public static final int create =  30;
    public static final int sys = 1;
    public static final int user = 2;
    public static final int group = 3;
    private static final String[] authorityNames = {"", "��ȫ����", "�޸�", "��ȡ������", "�г��ļ���Ŀ¼",
        "��", "д", "�ر��Ȩ��", "��ȡ�ļ�", "�г�Ŀ¼", "��ȡ�ļ���������", "��ȡ�ļ���չ����", "��ȡACL",
        "д�ļ�", "д�ļ�����������", "д�ļ���������", "д�ļ���չ����", "дACL", "�����ļ�", "������Ŀ¼", "ִ���ļ�",
        "ɾ���ļ�", "ɾ�����ļ�", "�ļ��̳�", "��Ŀ¼�̳�","ɾ��","ִ���ļ�","�̳�","��ȡ","д��","����"};
    private static final String[] styleNames = {"", "ϵͳACL", "�û�", "��"};

    public static String  success = "�ɹ�";
    public static String  failed = "ʧ��";
    public static String  open = "����";
    public static String  notOpen = "δ����";
    public static String  wrongIP = "IP��ַ��ʽ�������������룡";
    public static String  wrongConfigNFS = "ֻ��Ȩ�����дȨ������������һ�";
    public static String  configSuccess = "���óɹ���";
    public static String  aclExist = "���û�Ȩ���Ѵ��ڣ�";
    public static String  configACL = "������Ȩ�ޣ�";
    public static String  winUser = "Windows���û�";
    public static String  unixUser = "NAS�û�";
    public static String  winGroup = "Windows���û���";
    public static String  unixGroup = "NAS��";
    public static String  winUserName = " Windows�û�����:";
    public static String  winGroupName = "Windows�û�������:";
    public static String  unixUserName =  " NAS�û�����:";
    public static String  unixGroupName = "NAS�û�������:";
    public static String  emptyUserName = "Windows��NAS�û����ƶ�����Ϊ��";
    public static String  emptygroupName = "Windows��NAS�û������ƶ�����Ϊ��";
    public static String  wrongWinName = "Windows�û����û����ʽ����ʾ����administrator@company.com";
    public static String  wrongWinUserNameContain = "ָ����Windows�û����Ʋ����ڣ�";
    public static String  wrongWinGroupNameContain = "ָ����Windows�û������Ʋ����ڣ�";
        public static String  wrongUnixUserNameContain = "ָ����NAS�û����Ʋ����ڣ�";
    public static String  wrongUnixGroupNameContain = "ָ����NAS�û������Ʋ����ڣ�";
    public static String  existIDMap ="ָ����IDӳ���Ѵ���";
    public static String  userStyle = "�û�:";
    public static String  groupStyle = "��:";
    public static String  yes = "��";
    public static String  no = "��";
    public static String  on = "����";
    public static String  off = "����";
    public static String  onLine = "����";
    public static String  offLine = "����";
    public static String  closeShare ="��رչ����ܣ�";
    public static String  emptyShareName ="�������Ʋ���Ϊ�գ�";
    public static String errorShareName= "������������ĸ��������ɣ��ҳ��Ȳ�����32λ��";
    public static String emptyPathName = "Ŀ¼���Ʋ���Ϊ�գ�";
    public static String errorPathName= "Ŀ¼��������ĸ��������ɣ��ҳ��Ȳ�����32λ��";
    public static String joinedToDomain = "�Ѽ�����";
    public static String domainName = "����";
    public static String domainController = "�������";
    
    public static String getAuthor(int i) {
        return authorityNames[i];
    }

    public static String getOneStyleName(int i) {
        return styleNames[i];
    }

    public ArrayList<String> getReadAuthoritis() {
        ArrayList<String> reads = new ArrayList<String>();
        reads.add(authorityNames[Constant.readFile]);
        reads.add(authorityNames[Constant.listContent]);
        reads.add(authorityNames[Constant.readBasicAttribute]);
        reads.add(authorityNames[Constant.readExpandAttribute]);
        reads.add(authorityNames[Constant.readACL]);

        return reads;
    }

    public ArrayList<String> getWriteAuthoritis() {
        ArrayList<String> writes = new ArrayList<String>();
        writes.add(authorityNames[Constant.writeFile]);
        writes.add(authorityNames[Constant.writeFileOwner]);
        writes.add(authorityNames[Constant.writeFileBasicAttribute]);
        writes.add(authorityNames[Constant.writeFileExpandAttribute]);
        writes.add(authorityNames[Constant.writeACl]);

        return writes;
    }
    public ArrayList<String> getCreatesAuthoritis() {
        ArrayList<String> creates = new ArrayList<String>();
        creates.add(authorityNames[Constant.createFile]);
        creates.add(authorityNames[Constant.createSubDirectory]);
       

        return creates;
    }
    public ArrayList<String> getExecutesAuthoritis() {
        ArrayList<String> executes = new ArrayList<String>();
        executes.add(authorityNames[Constant.execute]);
       

        return executes;
    }
    public ArrayList<String> getDeletesAuthoritis() {
        ArrayList<String> deletes = new ArrayList<String>();
        deletes.add(authorityNames[Constant.deleteFile]);
        deletes.add(authorityNames[Constant.deleteChild]);
       

        return deletes;
    }
    public ArrayList<String> getInheritsAuthoritis() {
        ArrayList<String> inherits = new ArrayList<String>();
        inherits.add(authorityNames[Constant.fileInherit]);
        inherits.add(authorityNames[Constant.dirInherit]);
       

        return inherits;
    }
    
    
}
