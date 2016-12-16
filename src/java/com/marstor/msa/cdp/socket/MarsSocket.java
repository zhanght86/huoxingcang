/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.socket;

import com.marstor.xml.XMLConstructor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Administrator
 */
public class MarsSocket {

    /**
     * Socket对象
     */
    protected Socket m_Socket = null;
    /**
     * InputStream on Socket
     */
    protected InputStream m_in = null;
    /**
     * OutputStream on Socket
     */
    protected OutputStream m_out = null;
    /**
     * 从Socket接收到的完整XML
     */
    protected byte[] m_byteData = null;
    /**
     * 异常消息
     */
    protected String m_strError = "";
    protected String m_hostName = "";
    protected int m_port = 0;
    protected String m_localHostName = "";
    protected int m_localPort = 0;
    protected int m_timeout_in_seconds = 0; // 0: wait for erver. never close.
    protected boolean m_bTimeout = false;
    protected MarsSocketFlag m_socketFlag = new MarsSocketFlag();
    public static byte[] NULL_BYTE = new byte[]{0};

    protected void initSocket(Socket socket) {
        if (socket != null) {
            try {
                m_in = socket.getInputStream();
                m_out = socket.getOutputStream();
                m_Socket.setSoTimeout(1000);
                this.m_hostName = socket.getInetAddress().getHostAddress();
                this.m_port = socket.getPort();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    protected void setSocket(Socket socket) {
        m_Socket = socket;
        initSocket(socket);
    }

    /**
     * 构造函数
     * @param socket Socket对象，在MarsMasterListenSocket对象中产生的客户端连接
     * @roseuid 47341E5B031C
     */
    public MarsSocket(Socket socket) {
        setSocket(socket);
    }

    protected MarsSocket() {
    }

    public void setTimeout(int seconds) {
        try {
            this.m_timeout_in_seconds = seconds;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读socket，从指定的位置得到指定的大小时间，返回的数据以时间大小为准。
     * @param buffer
     * @param offset
     * @param needLength
     * @return
     */
    protected int read(byte[] buffer, int offset, int needLength) {
        int nReadLen = 0;
        int currentReadLen = 0;
        int position = offset;
        int timeoutCount = 0;
        this.m_bTimeout = false;
        while (nReadLen < needLength) {
            try {
//               System.out.print("Buffer Size= " + buffer.length + ", index=" + position + ", needLen=" + needLength + "  ");
                currentReadLen = m_in.read(buffer, position, needLength);
//               System.out.println(",ReadLen = " + currentReadLen);
                if (currentReadLen < 0) {
                    m_strError = "Server Close the Socket";
                    return currentReadLen;
                }
                position += currentReadLen;
                nReadLen += currentReadLen;
                needLength -= currentReadLen;
            } catch (SocketTimeoutException ee) {
                timeoutCount++;
                if (m_timeout_in_seconds != 0 && timeoutCount > m_timeout_in_seconds) {
                    m_strError = "Timeout in " + m_timeout_in_seconds + " seconds. has received " + nReadLen + " bytes";
                    this.m_bTimeout = true;
                    return -1;
                }
                if (timeoutCount > 50000) {
                    timeoutCount = 1;
                }

                continue;
            } catch (IOException e) {
                // e.printStackTrace();
                return -1;
            } catch (Exception eee) {
                eee.printStackTrace();
            }
        }
        return nReadLen;
    }

    /**
     * 读Socket，如果符合Mars标准，则调用onCommand函数，然后把onCommand函数返回的XML从Socket上发送出去
     * @return boolean
     * @roseuid 473FF5C1034B
     */
    public boolean onRead() {
        int currentReadLen = 0;
        int readLen = 0;
        int totalLen = 0;

        byte byteHeaderFlag[] = new byte[4];
        byte byteHeaderLen[] = new byte[4];

        int headerFlagReadLen = 0;
        int headerLenReadLen = 0;

        while (headerFlagReadLen < 4) {
            currentReadLen = read(byteHeaderFlag, headerFlagReadLen, 4 - headerFlagReadLen);
            if (currentReadLen < 1) {
                return false;
            }
            headerFlagReadLen += currentReadLen;
        }
        while (headerLenReadLen < 4) {
            currentReadLen = read(byteHeaderLen, headerLenReadLen, 4 - headerLenReadLen);
            if (currentReadLen < 1) {
                return false;
            }
            headerLenReadLen += currentReadLen;
        }
        m_socketFlag.parserFlag(byteHeaderFlag, byteHeaderLen);
        totalLen = m_socketFlag.getFlagLen();
//       System.out.println("TotalLen = " + totalLen);
        this.m_byteData = new byte[(int) totalLen];

        while (readLen < totalLen) {
            currentReadLen = read(m_byteData, readLen, totalLen - readLen);
            if (currentReadLen < 1) {
                return false;
            }
            readLen += currentReadLen;
        }
        return true;
    }

    public boolean sendEcho(String strReceiveModuleName) {
        XMLConstructor xml = new XMLConstructor(40, "Echo", strReceiveModuleName, "WWW", 0);
        return this.sendXmlData(xml.toByte());
    }

    protected boolean sendData(String flag, byte[] data, boolean needNULLByte) {
        try {
            m_out.write(flag.getBytes("UTF-8"));
            int len = data.length;
            if (needNULLByte) {
                len += 1;
            }
            m_out.write(Convert.convertLong2Bytes(len));
            if (data.length > 0) {
                m_out.write(data);
            }
            if (needNULLByte) {
                m_out.write(NULL_BYTE);
            }
            m_out.flush();
        } catch (Exception e) {
            e.printStackTrace();
//            MBALog.LogPrintf(MBALog.LOG_ERROR,"向" + this.m_hostName + ":"  + this.m_port + "发送XML数据失败！\n" + strSendData);
            return false;
        }
        return true;
    }

    public boolean sendXmlData(byte[] xmlData) {
        return sendData(MarsSocketFlag.FLAG_XML_DATA, xmlData, true);
    }

    public boolean sendVData(byte[] vData) {

        return sendData(MarsSocketFlag.FLAG_V_DATA, vData, false);
    }

    public byte[] getData() {
        return this.m_byteData;
    }

    /**
     * 关闭连接
     */
    public void Close() {
        try {
            m_Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取原始套节字
     * @return 返回套节字
     */
    public Socket getSocket() {
        return this.m_Socket;
    }

    /**
     * 获取套节字输入流
     * @return 返回套节字输入流
     */
    public InputStream getInputStream() {
        return m_in;
    }

    /**
     * 获取套节字输出流
     * @return 返回套节字输出流
     */
    public OutputStream getOutputStream() {
        return m_out;
    }

    public boolean isTimeOut() {
        return this.m_bTimeout;
    }

    public String getError() {
        return m_strError;
    }
}
