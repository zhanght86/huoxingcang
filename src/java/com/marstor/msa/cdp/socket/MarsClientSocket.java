/**
 * <p>Title: Mars Backup Advanced SysManager</p>
 *
 * <p>Description: This is Mars Backup Advanced SysManager Module Written In
 * Java</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Beijing Asian Corp. Ltd</p>
 *
 * @author Ding Huaming
 * @version 2.0
 */
package com.marstor.msa.cdp.socket;

import java.io.*;
import java.net.*;

/**
 * 堵塞方式的Socket。封装了Socket类，实现了读Mars标准的Socket通信内容
 */
public class MarsClientSocket extends MarsSocket {

    /**
     * Whether the m_socket be connected
     */
    private boolean m_bConnected = false;
    /**
     *  Received Data in bytes
     */
    private boolean m_bRunning = true;

    /**
     * 构造函数
     */
    public MarsClientSocket() {
        this.m_Socket = new Socket();
    }

    /**
     * 构造函数
     * @param socket Socket套节字
     */
    public MarsClientSocket(Socket socket) {
        super(socket);
        m_bConnected = true;
        m_bRunning = true;
    }

    /**
     * 连接主机
     * @param strhostName 主机名
     * @param nPort 端口号
     * @return 连接成功返回true，否则返回false
     * return false.
     */
    public boolean Connect(String strhostName, int nPort) {
        this.Close();
        try {
            this.m_hostName = strhostName;
            this.m_port = nPort;
            System.out.println("host:" + this.m_hostName);
            System.out.println("port:" + this.m_port);
            SocketAddress address = new InetSocketAddress(strhostName, nPort);
            this.m_Socket = new Socket();

            //m_socket.setReuseAddress(true);
            m_Socket.connect(address, 1000 * 60); // 60秒超时
            this.initSocket(m_Socket);
//      m_socket.setSoTimeout(0);

            m_bConnected = true;
        } catch (IOException e) {
            System.out.println("Connect to server failed  IOException");
            e.printStackTrace();
            return false;
        } catch (Exception ee) {
            System.out.println("Connect to server failed  Exception");
            return false;
        }
        return true;
    }
}
