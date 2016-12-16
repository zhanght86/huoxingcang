/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.util;

/**
 *
 * @author wanghe
 */
public class SpacesTrans
{

    public static String getHumanReadingSizeB(long sizeB)
    {
        if (sizeB < 1024)
        {
            return formatHumanReadingSize("" + sizeB + " B");
        }

        return getHumanReadingSizeKB(sizeB / 1024);
    }

    public static String getHumanReadingSizeKB(long sizeKB)
    {
        String ret = "";
        long blockMB = 1024L;
        long blockGB = blockMB * 1024;
        long blockTB = blockGB * 1024;

        long sizeMB_A = sizeKB / blockMB;
        long sizeMB_B = sizeKB % blockMB;

        long sizeGB_A = sizeKB / blockGB;
        long sizeGB_B = sizeKB % blockGB;

        long sizeTB_A = sizeKB / blockTB;
        long sizeTB_B = sizeKB % blockTB;

        if (sizeMB_A < 1)
        {
            return formatHumanReadingSize("" + sizeKB + "KB");
        }
        if (sizeGB_A < 1)
        {
            return formatHumanReadingSize("" + (sizeMB_A + ((double) sizeMB_B / blockMB)) + "MB");
        }
        if (sizeTB_A < 1)
        {
            return formatHumanReadingSize("" + (sizeGB_A + ((double) sizeGB_B / blockGB)) + "GB");
        }
        else
        {
            return formatHumanReadingSize("" + (sizeTB_A + ((double) sizeTB_B / blockTB)) + "TB");
        }


    }

    static String formatHumanReadingSize(String size)
    {
        if (size == null)
        {
            return "";
        }
        if (size.length() < 2)
        {
            return "";
        }
        int index = size.indexOf(".");
        if (index < 0)
        {
            return size;
        }
        if (index == (size.length() - 1))
        {
            return size;
        }

        String a = size.substring(0, index);                                                                                                                                                                                                                             
        String b = size.substring(index + 1);
        String c = "";
        if (b.length() > 4)
        {
            c = b.substring(0, 3);
            b = b.substring(3);

            char d = b.charAt(0);
            while (true)
            {
                if (d <= '9' && d >= '0')
                {
                    b = b.substring(1);
                    d = b.charAt(0);
                    continue;
                }
                break;
            }
            c = c + b;
        } else
        {
            c = b;
        }

        return a + "." + c;

    }

    public static void main(String[] args)
    {
        System.out.println(getHumanReadingSizeB(0));
        System.out.println(getHumanReadingSizeB(1));
        System.out.println(getHumanReadingSizeB(1024));
        System.out.println(getHumanReadingSizeB(1023));
        System.out.println(getHumanReadingSizeB(1025));
        System.out.println(getHumanReadingSizeB(2049));
        System.out.println(getHumanReadingSizeB(1024*1024+1));
        System.out.println(getHumanReadingSizeB(1024*1024-1));
        System.out.println(getHumanReadingSizeB(1024*1024*1024-1));
        System.out.println(getHumanReadingSizeB(1024*1024*1024+1));
        System.out.println(getHumanReadingSizeB(1024*1024*1024*1024+1));

    }

}

