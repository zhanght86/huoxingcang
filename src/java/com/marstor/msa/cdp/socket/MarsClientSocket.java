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
 * ������ʽ��Socket����װ��Socket�࣬ʵ���˶�Mars��׼��Socketͨ������
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
     * ���캯��
     */
    public MarsClientSocket() {
        this.m_Socket = new Socket();
    }

    /**
     * ���캯��
     * @param socket Socket�׽���
     */
    public MarsClientSocket(Socket socket) {
        super(socket);
        m_bConnected = true;
        m_bRunning = true;
    }

    /**
     * ��������
     * @param strhostName ������
     * @param nPort �˿ں�
     * @return ���ӳɹ�����true�����򷵻�false
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
            m_Socket.connect(address, 1000 * 60); // 60�볬ʱ
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
