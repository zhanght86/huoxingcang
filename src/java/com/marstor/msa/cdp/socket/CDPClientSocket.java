/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.socket;

import java.io.*;
import java.net.*;

public class CDPClientSocket {

    /**
     * InputStream on Socket
     */
    private InputStream m_in;
    /**
     * OutputStream on Socket
     */
    private OutputStream m_out;
    /**
     * Socket object
     */
    private Socket m_socket;
    /**
     * Whether the m_socket be connected
     */
    private boolean m_bConnected;
    /**
     * Connection timeouts in seconds.
     */
    private int m_TimeOut;
    /**
     *  Received Data in bytes
     */
    private byte m_ReceiveData[];
    private boolean m_bRunning = true;
    private String m_strError = "";
    private String m_hostName = "";
    private int m_port = 0;
    boolean m_isTimeOut = false;

    /**
     * ���캯��
     */
    public CDPClientSocket() {
        m_TimeOut = 0;
        m_bRunning = true;
    }

    /**
     * ���캯��
     * @param socket Socket�׽���
     */
    public CDPClientSocket(Socket socket) {
        m_socket = socket;
        try {
            m_socket.setReceiveBufferSize(m_port);
            m_socket.setSoTimeout(0);
            m_in = m_socket.getInputStream();
            m_out = m_socket.getOutputStream();
            this.m_hostName = socket.getInetAddress().getHostAddress();
            this.m_port = socket.getPort();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        m_bConnected = true;
        m_bRunning = true;
    }

    public static void test331() {
        CDPClientSocket mars = new CDPClientSocket();
        boolean bConn = mars.Connect("192.168.0.163", 41013);
        if (!bConn) {
            System.out.println("Connect Master Failed!");
            return;
        } else {
            System.out.println("Connect Master Successfully!");
        }
        XMLConstructor xml = new XMLConstructor(331, "Query Tramsmitte Rate From DataTransmitter To JobManage", "MarsMaster", "");

        xml.addNode("SenderTaskID", "21");
        xml.addNode("ReceiverTaskID", "45");
        boolean bA = true;
        while (bA) {
            if (mars.sendData(xml.toXmlString())) {
                System.out.println("Send Data Successfully");
            } else {
                System.out.println("Send Data Failed");
            }

            if (mars.OnRead()) {
                try {
                    System.out.println(new String(mars.getContent(), "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Receive Data Successfully");
                System.out.println(mars.getContent());
            } else {
                System.out.println("Receive Data failed");
            }
            try {
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mars.Close();
    }

    public void Close() {
        try {
            if (m_bConnected) {
                m_socket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * ��ȡԭʼ�׽���
     * @return �����׽���
     */
    public Socket getSocket() {
        return m_socket;
    }

    /**
     * ��ȡ�׽���������
     * @return �����׽���������
     */
    public InputStream getInputStream() {
        return m_in;
    }

    public OutputStream getOutputStream() {
        return m_out;
    }

    public void setTimeOut(int nTimeOut) {
        this.m_TimeOut = nTimeOut;
    }

    public boolean Connect(String strhostName, int nPort) { 
        this.Close();
        try {
            this.m_hostName = strhostName;
            this.m_port = nPort;
            SocketAddress address = new InetSocketAddress(strhostName, nPort);
            m_socket = new Socket();
            //m_socket.setReuseAddress(true);
            m_socket.connect(address, 0); // 60�볬ʱ
//      m_socket.setSoTimeout(1000);
            m_socket.setSoTimeout(0);
            m_in = m_socket.getInputStream();
            m_out = m_socket.getOutputStream();
            m_bConnected = true;
        } catch (IOException e) {
            m_strError = getStringRC("ConnectFail");
            m_strError += "\n" + getStringRC("Address=") + strhostName + "  " + getStringRC("Port=") + nPort;
            System.out.println(e.toString());
            return false;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ��Socket����ȡ����ݱ�����m_ReceiveData����
     * @return ��Socket�ɹ����򷵻�true�����򷵻�false��
     */
    public boolean OnRead() {

        long nReadLen = 0;
        long nLeftLen = 0;
        long nTotalLen = 0;
        byte Receive8[] = new byte[8];
        byte byteHeaderFlag[] = new byte[4];
        byte byteHeaderLen[] = new byte[4];

        m_isTimeOut = false;

        String MarsFlag = null;
        m_ReceiveData = null; //new byte[(int)nTotalLen];
        if (!m_bConnected) {
            m_strError = getStringRC("SocketNotConnected");
            return false;
        }

        int timeoutCount = 0;
        while (m_bRunning) {
            // Receive Mars Header
            // Acquire the Command length
            if (nLeftLen < 1) {
                try {
                    nReadLen = m_in.read(byteHeaderFlag, 0, 4);
                    nReadLen = m_in.read(byteHeaderLen, 0, 4);
                    if (nReadLen < 0) {
                        m_strError = "Server Close the Socket";
                        return false;
                    }
                } catch (SocketTimeoutException ee) {
                    timeoutCount++;
                    if (timeoutCount > m_TimeOut && m_TimeOut > 0) {
                        m_isTimeOut = true;
                        return false;
                    } else {
                        continue;
                    }

                } catch (IOException e) {
                    return false;
                }

                try {
                    MarsFlag = new String(byteHeaderFlag, "UTF-8");
                    if (!MarsFlag.equals("MARS")) {
                        return false;
                    }
                    nTotalLen = Convert.convertBytes2Long(byteHeaderLen);
                    m_ReceiveData = new byte[(int) nTotalLen];
                    nLeftLen = nTotalLen;
                } catch (Exception e) {
                    return false;
                }
                continue;
            }
            // Begin receive Main Command
            try {
                nReadLen = m_in.read(m_ReceiveData, (int) (nTotalLen - nLeftLen), (int) nLeftLen);
                if (nReadLen < 0) {
                    m_strError = "Server Close the Socket";
                    return false;
                }
                nLeftLen = nLeftLen - nReadLen;
            } catch (SocketTimeoutException ee) {
                timeoutCount++;
                if (timeoutCount > m_TimeOut && m_TimeOut > 0) {
                    m_isTimeOut = true;
                    return false;
                } else {
                    continue;
                }
            } catch (IOException e) {
                return false;
            }
            if (nLeftLen < 1) {
                break;
            }
        }
        int index = m_ReceiveData.length - 1;
        while (m_ReceiveData[index] == 0) {
            m_ReceiveData[index] = 10;
            index--;
            if (index < 0) {
                break;
            }
        }
        return true;
    }

    public boolean OnReadTDAT() {
        long nReadLen = 0;
        long nLeftLen = 0;
        long nTotalLen = 0;
        byte Receive8[] = new byte[8];
        byte byteHeaderFlag[] = new byte[4];
        byte byteHeaderLen[] = new byte[4];

        m_isTimeOut = false;
        String MarsFlag = null;
        m_ReceiveData = null; //new byte[(int)nTotalLen];
        if (!m_bConnected) {
            m_strError = getStringRC("SocketNotConnected");
            return false;
        }
        int timeoutCount = 0;
        while (m_bRunning) {
            // Receive Mars Header
            // Acquire the Command length
            if (nLeftLen < 1) {
                try {
                    nReadLen = m_in.read(byteHeaderFlag, 0, 4);
                    nReadLen = m_in.read(byteHeaderLen, 0, 4);
                    if (nReadLen < 0) {
                        m_strError = "Server Close the Socket";
                        return false;
                    }
                } catch (SocketTimeoutException ee) {
                    timeoutCount++;
                    if (timeoutCount > m_TimeOut && m_TimeOut > 0) {
                        m_isTimeOut = true;
                        return false;
                    } else {
                        continue;
                    }
                } catch (IOException e) {
                    return false;
                }

                try {
                    MarsFlag = new String(byteHeaderFlag, "UTF-8");
                    if (!MarsFlag.equals("TDAT")) {
                        return false;
                    }
                    nTotalLen = Convert.convertBytes2Long(byteHeaderLen);
                    m_ReceiveData = new byte[(int) nTotalLen];
                    nLeftLen = nTotalLen;
                    if (nTotalLen < 1) {
                        break;
                    }
                } catch (Exception e) {
                    return false;
                }
                continue;
            }
            // Begin receive Main Command
            try {
                nReadLen = m_in.read(m_ReceiveData, (int) (nTotalLen - nLeftLen), (int) nLeftLen);
                if (nReadLen < 0) {
                    m_strError = "Server Close the Socket";
                    return false;
                }
                nLeftLen = nLeftLen - nReadLen;
            } catch (SocketTimeoutException ee) {
                timeoutCount++;
                if (timeoutCount > m_TimeOut && m_TimeOut > 0) {
                    m_isTimeOut = true;
                    return false;
                } else {
                    continue;
                }
            } catch (IOException e) {
                return false;
            }
            if (nLeftLen < 1) {
                break;
            }
        }
        return true;
    }

    public boolean isTimeOut() {
        return this.m_isTimeOut;
    }

    /**
     * ��ȡ��Socket������
     * @return ����Socket����
     */
    public byte[] getContent() {
        return m_ReceiveData;
    }

    public String getReturnXml() {
        if (OnRead()) {
            try {
                return new String(getContent(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean sendData(String strSendData) {
        if (strSendData == null) {
            return false;
        }
        try {
            byte[] byteSendData = strSendData.getBytes("UTF-8");
            int iLength = byteSendData.length;
            byte[] byteTotalData = new byte[iLength + 9];
            System.arraycopy("MARS".getBytes("UTF-8"), 0, byteTotalData, 0, 4);
            System.arraycopy(Convert.convertLong2Bytes(iLength + 1), 0, byteTotalData, 4, 4);
            System.arraycopy(byteSendData, 0, byteTotalData, 8, iLength);
            byteTotalData[iLength + 8] = 0;
            m_out.write(byteTotalData);
            m_out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendTDAT(byte[] data) {
        if (data == null) {
            return false;
        }
        try {
            byte[] byteSendData = data;
            int iLength = byteSendData.length;
            if (iLength > 0) {
                iLength += 4;
            }
            byte[] byte8 = new byte[8];
            System.arraycopy("TDAT".getBytes("UTF-8"), 0, byte8, 0, 4);
            System.arraycopy(Convert.convertLong2Bytes(iLength), 0, byte8, 4, 4);
            m_out.write(byte8);
            if (iLength > 0) {
                m_out.write(data);
                byte[] crc = MSACRC32.generateTDATCRC32(data);
                if (crc != null) {
                    m_out.write(crc);
                }
            }
            m_out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendEcho(String strReceiveModuleName) {
        XMLConstructor xml = new XMLConstructor(40, "Echo", "", "");
        return sendData(xml.toXmlString());

    }

    public void stopRead() {
        m_bRunning = false;
    }

    public String getError() {
        return m_strError;
    }

    protected String getStringRC(String strName) {
        return strName;
    }
}
