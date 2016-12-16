package com.marstor.msa.mdu.axis2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Ecdguest {

    public static void testGetShareName(EcdClient mduClient) {
        System.out.println(mduClient.getShareName());
    }

    public static void testAddGroup(EcdClient mduClient) {
        System.out.println(mduClient.addGroup("group3"));
//        System.out.println(mduClient.addGroup("group2"));
//        System.out.println(mduClient.addGroup("group3"));
//        System.out.println(mduClient.addGroup("group4"));
    }

    public static void testGetGroups(EcdClient mduClient) {
        String[] groups = mduClient.getGroups();
        for (String group : groups) {
            System.out.println(group);
        }
    }

    public static void testAddUser(EcdClient mduClient) {
        String[] group = {"group3"};
        boolean bool1 = mduClient.addUser("lyn5", group);
        System.out.println(bool1);
    }

    public static void testGetUsers(EcdClient mduClient) {
        String[] users = mduClient.getUsers();
        for (String user : users) {
            System.out.println(user);
        }
    }

    public static void testAddSharedDirectory(EcdClient mduClient) {
//        String[] users = {"lyn4"};
//        String[] dirs = {"a"};
//        System.out.println(mduClient.addSharedDirectory("lyn3", dirs, users));
        String xml = "<MDUAddSharedDirParameter>"
                + "<dirs>a</dirs><dirs>b</dirs><dirs>c</dirs>"
                + "<owner>lyn3</owner>"
                + "<users>lyn4</users><users>lyn5</users><users>lyn6</users>"
                + "</MDUAddSharedDirParameter>";
        System.out.println(mduClient.addSharedDirectory(xml));
    }

//    public static void testGetUserDirectory(MduClient mduClient) {
//        System.out.println("mduClient"+mduClient);
//        UserDir[] userDirectorys = mduClient.getUserDirectory("lyn1");
//        System.out.println("userDirectorys"+userDirectorys);
//        for (UserDir userDirectory : userDirectorys) {
//            System.out.println(userDirectory);
//        }
//    }
    public static void testAddUserDirectory(EcdClient mduClient) {
        boolean bool = mduClient.addUserDirectory("lyn5", "a");
        System.out.println(bool);
    }

    public static void main(String[] args) {
        String ip = "192.168.1.232";
        int port = 80;
//        String ip = null;
//        int port = 0;
//        try {
//            System.out.println("服务器ip地址：");
//            BufferedReader buff = new BufferedReader(new InputStreamReader(System.in, "GBK"));
//            ip = buff.readLine();
//            System.out.println("服务器端口：");
//            buff = new BufferedReader(new InputStreamReader(System.in, "GBK"));
//            port = Integer.parseInt(buff.readLine());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        CdpClient cdpClient = new CdpClient(ip,port);
//        List<CdpDiskGroupInfo> cdpDiskGroupInfos = cdpClient.getDiskGroupInfos();
//        for(CdpDiskGroupInfo cdpDiskGroupInfo : cdpDiskGroupInfos){
//            System.out.println(cdpDiskGroupInfo.getDiskGroupGuid() + "  " + cdpDiskGroupInfo.getDiskGroupName());
//        }
        EcdClient mduClient = new EcdClient(ip, port);
        String code = null;
        boolean bool = false;
        System.out.println("MDU测试命令：\r\n"
                + "1 (getShareName)\r\n"
                + "2 (addGroup) \r\n"
                + "3 (getGroups) \r\n"
                + "4 (addUser) \r\n"
                + "5 (getUsers) \r\n"
                + "6 (addUserSharedDirectory)\r\n"
                + "7 (getUserDirectory)\r\n"
                + "8（addUserDirectory）");
        do {
            try {
                BufferedReader bfRead = new BufferedReader(new InputStreamReader(System.in, "GBK"));
                code = bfRead.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (code.equals("1")) {
                testGetShareName(mduClient);
                bool = true;
            } else if (code.equals("2")) {
                testAddGroup(mduClient);
                bool = true;
            } else if (code.equals("3")) {
                testGetGroups(mduClient);
                bool = true;
            } else if (code.equals("4")) {
                testAddUser(mduClient);
                bool = true;
            } else if (code.equals("5")) {
                testGetUsers(mduClient);
                bool = true;
            } else if (code.equals("6")) {
                testAddSharedDirectory(mduClient);
            } else if (code.equals("7")) {
//                testGetUserDirectory(mduClient);
                bool = true;
            } else if (code.equals("8")) {
                testAddUserDirectory(mduClient);
                bool = true;
            } else {
                System.out.println("输入命令有误！");
                System.exit(0);
            }
        } while (bool);
//        testGetShareName(mduClient);
//        testAddGroup(mduClient);
//        testGetGroups(mduClient);
//        testAddUser(mduClient);
//        testGetUsers(mduClient);
//        testAddSharedDirectory(mduClient); 
//        testGetUserDirectory(mduClient);
    }
}
