/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.cdp.socket;

import java.util.zip.CRC32;

public class MSACRC32 
{
    public static byte[] generateTDATCRC32(byte[] data)
    {
        byte[] byteCRC32 = null;
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        long lCRC32 = crc32.getValue();
        try
        {
            byteCRC32 = Convert.convertLong2Bytes(lCRC32);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return byteCRC32;
    }
    
    public static boolean isTDATCRC32OK(byte[] data)
    {
        CRC32 crc32 = new CRC32();
        int dataLength = data.length;
        crc32.update(data,0,dataLength - 4);
        long lNewCRC32 = crc32.getValue();
        int orginalCRCStartPosition = dataLength - 4;
        byte [] newCRC32 = new byte[4];
        try
        { 
            newCRC32 = Convert.convertLong2Bytes(lNewCRC32);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        for(int i=0; i<4; i++)
        {
            if(newCRC32[i] != data[orginalCRCStartPosition + i])
            {
                return false;
            }
        }
        return true;
    }
}
