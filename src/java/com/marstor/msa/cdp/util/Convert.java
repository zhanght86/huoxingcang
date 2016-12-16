package com.marstor.msa.cdp.util;
 
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
public final class Convert
{
  public static final boolean HIGHBYTEAHEAD=true;
  //低位在前
  public static long convertBytes2Long(byte[] src)throws Exception{
      if(!HIGHBYTEAHEAD)return convertBytes2Long2(src);
      long ret=0;
      if(src==null||src.length<4)
          throw new Exception("字节数组不能为空，且长度必须为4!");
      for(int i=0;i<4;i++){
          //Constants.println(src[i]);
          for(int j=0;j<8;j++)
              ret=ret|((src[i]>>j)&1)<<(i*8+j);
      }
      return ret;
  }
  //低位在前
  public static byte[] convertLong2Bytes(long src)throws Exception{
      if(!HIGHBYTEAHEAD)return convertLong2Bytes2(src);
      byte[] ret=new byte[4];
      ret[0]=0;
      ret[1]=0;
      ret[2]=0;
      ret[3]=0;
      for(int i=0;i<4;i++)
          for(int j=0;j<8;j++){
              long xs = (src >> (i * 8 + j)) & 1;
              if(xs==1)
                  ret[i]|=1<<j;
              //ret[i] = ret[i] | (((src >> (i * 8 + j)) & 1) << j);
          }
      return ret;
  }
  //高位在前
  private static long convertBytes2Long2(byte[] src)throws Exception{
      long ret=0;
      if(src==null||src.length<4)
          throw new Exception("字节数组不能为空，且长度必须为4!");
      for(int i=0;i<4;i++){
          //Constants.println(src[i]);
          for(int j=0;j<8;j++)
              ret=ret|((src[i]>>j)&1)<<(((3-i)*8+j));
      }
      return ret;
  }
  //高位在前
  private static byte[] convertLong2Bytes2(long src)throws Exception{
      byte[] ret=new byte[4];
      ret[0]=0;
      ret[1]=0;
      ret[2]=0;
      ret[3]=0;
      for(int i=0;i<4;i++)
          for(int j=0;j<8;j++){
              long xs = (src >> ((3-i) * 8 + j)) & 1;
              if(xs==1)
                  ret[i]|=1<<j;
              //ret[i] = ret[i] | (((src >> (i * 8 + j)) & 1) << j);
          }
      return ret;
  }
  //下面为测试用
  private static void show(byte b){
      for (int i = 0;i < 8;i++)
          System.out.print(b >> i & 1);
  }
  private static void show(long b,int num){
      for (int i = 0;i < num;i++)
          System.out.print(b >> i & 1);
  }
  public static void main(String[] args) throws Exception{
      byte[] bs=new byte[]{(byte)0x92,(byte)0x03,(byte)0,(byte)0};
      show((long)914,64);
      for (int i = 0;i < 4;i++){
          show(bs[i]);
      }
      byte[] ret=convertLong2Bytes(914L);
      for (int i = 0;i < 4;i++){
          show(ret[i]);
      }
    }
}
