/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.DiskModel;
import com.marstor.msa.common.bean.DiskStatistics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class DiskStatus implements Serializable{

//    public static final String b57 = "../resources/common/picture/57b.png";
//    public static final String b58 = "../resources/common/picture/58b.png";
//    public static final String b47 = "../resources/common/picture/47b.png";
//    public static final String b48 = "../resources/common/picture/48b.png";
//    public static final String a18 = "../resources/common/picture/17a.png";
//    public static final String a17 = "../resources/common/picture/18a.png";
//    public static final String a15 = "../resources/common/picture/15a.png";
//    public static final String a16 = "../resources/common/picture/16a.png";
//    public static final String c57 = "../resources/common/picture/57c.png";
//    public static final String c58 = "../resources/common/picture/58c.png";
    public static final String trayBlueRed = "../resources/oempic/diskpicture/trayBlueRed.png";
    public static final String trayBlueGreen = "../resources/oempic/diskpicture/trayBlueGreen.png";
    public static final String trayRed = "../resources/oempic/diskpicture/trayRed.png";
    // public static final String temp50 = "../resources/common/picture/temp50.png";
    //public static final String temp51 = "../resources/common/picture/temp51.png";
    public static final String temp01 = "../resources/oempic/diskpicture/temp01.png";
    public static final String temp02 = "../resources/oempic/diskpicture/temp02.png";
    public static final String temp03 = "../resources/oempic/diskpicture/temp03.png";
    public static final String temp04 = "../resources/oempic/diskpicture/temp04.png";
    public static final String temp05 = "../resources/oempic/diskpicture/temp05.png";
    public static final String temp06 = "../resources/oempic/diskpicture/temp06.png";
    public static final String temp07 = "../resources/oempic/diskpicture/temp07.png";
    public static final String temp08 = "../resources/oempic/diskpicture/temp08.png";
    public static final String temp09 = "../resources/oempic/diskpicture/temp09.png";
    public static final String temp10 = "../resources/oempic/diskpicture/temp10.png";
    public static final String temp11 = "../resources/oempic/diskpicture/temp11.png";
    public static final String temp12 = "../resources/oempic/diskpicture/temp12.png";
    public static final String temp13 = "../resources/oempic/diskpicture/temp13.png";
    public static final String temp14 = "../resources/oempic/diskpicture/temp14.png";
    public static final String temp15 = "../resources/oempic/diskpicture/temp15.png";
    public static final String temp16 = "../resources/oempic/diskpicture/temp16.png";
    public static final String temp17 = "../resources/oempic/diskpicture/temp17.png";
    public static final String temp18 = "../resources/oempic/diskpicture/temp18.png";
    public static final String temp19 = "../resources/oempic/diskpicture/temp19.png";
    public static final String temp20 = "../resources/oempic/diskpicture/temp20.png";
    public static final String temp21 = "../resources/oempic/diskpicture/temp21.png";
    public static final String temp22 = "../resources/oempic/diskpicture/temp22.png";
    public static final String temp23 = "../resources/oempic/diskpicture/temp23.png";
    public static final String temp24 = "../resources/oempic/diskpicture/temp24.png";
    public static final String temp25 = "../resources/oempic/diskpicture/temp25.png";
    public static final String temp26 = "../resources/oempic/diskpicture/temp26.png";
    public static final String temp27 = "../resources/oempic/diskpicture/temp27.png";
    public static final String temp28 = "../resources/oempic/diskpicture/temp28.png";
    public static final String temp29 = "../resources/oempic/diskpicture/temp29.png";
    public static final String temp30 = "../resources/oempic/diskpicture/temp30.png";
    public static final String temp31 = "../resources/oempic/diskpicture/temp31.png";
    public static final String temp32 = "../resources/oempic/diskpicture/temp32.png";
    public static final String temp33 = "../resources/oempic/diskpicture/temp33.png";
    public static final String temp34 = "../resources/oempic/diskpicture/temp34.png";
    public static final String temp35 = "../resources/oempic/diskpicture/temp35.png";
    public static final String temp36 = "../resources/oempic/diskpicture/temp36.png";
    public static final String temp37 = "../resources/oempic/diskpicture/temp37.png";
    public static final String temp38 = "../resources/oempic/diskpicture/temp38.png";
    public static final String temp39 = "../resources/oempic/diskpicture/temp39.png";
    public static final String temp40 = "../resources/oempic/diskpicture/temp40.png";
    public static final String temp41 = "../resources/oempic/diskpicture/temp41.png";
    public static final String temp42 = "../resources/oempic/diskpicture/temp42.png";
    public static final String temp43 = "../resources/oempic/diskpicture/temp43.png";
    public static final String temp44 = "../resources/oempic/diskpicture/temp44.png";
    public static final String temp45 = "../resources/oempic/diskpicture/temp45.png";
    public static final String temp46 = "../resources/oempic/diskpicture/temp46.png";
    public static final String temp47 = "../resources/oempic/diskpicture/temp47.png";
    public static final String temp48 = "../resources/oempic/diskpicture/temp48.png";
    public static final String temp49 = "../resources/oempic/diskpicture/temp49.png";
    public static final String temp50 = "../resources/oempic/diskpicture/temp50.png";
    public static final String temp51 = "../resources/oempic/diskpicture/temp51.png";
    public static final String temp52 = "../resources/oempic/diskpicture/temp52.png";
    public static final String temp53 = "../resources/oempic/diskpicture/temp53.png";
    public static final String temp54 = "../resources/oempic/diskpicture/temp54.png";
    public static final String temp55 = "../resources/oempic/diskpicture/temp55.png";
    public static final String temp56 = "../resources/oempic/diskpicture/temp56.png";
    public static final String temp57 = "../resources/oempic/diskpicture/temp57.png";
    public static final String temp58 = "../resources/oempic/diskpicture/temp58.png";
    public static final String temp59 = "../resources/oempic/diskpicture/temp59.png";
    public static final String temp60 = "../resources/oempic/diskpicture/temp60.png";
    public static HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
    public static HashMap<String, String> trayMap = new HashMap<String, String>();
    private String Disk_6_1_state;
    private String Disk_6_1_temp;
    private String Disk_8_1;
    private String Disk_16_1;
    private String disk;
    private String temp;
    private Disk disk_6_1, disk_6_2, disk_6_3, disk_6_4, disk_6_5, disk_6_6;
    private Disk disk_8_1, disk_8_2, disk_8_3, disk_8_4, disk_8_5, disk_8_6, disk_8_7, disk_8_8;
    private Disk disk_16_1, disk_16_2, disk_16_3, disk_16_4, disk_16_5, disk_16_6, disk_16_7, disk_16_8, disk_16_9, disk_16_10, disk_16_11, disk_16_12, disk_16_13, disk_16_14, disk_16_15, disk_16_16;
    private Disk[] disk6 = new Disk[7];
    private Disk[] disk8 = new Disk[9];
    private Disk[] disk16 = new Disk[17];
    private boolean isDisk6, isDisk8, isDisk16;

    static {
        tempMap.put(1, temp01);
        tempMap.put(2, temp02);
        tempMap.put(3, temp03);
        tempMap.put(4, temp04);
        tempMap.put(5, temp05);
        tempMap.put(6, temp06);
        tempMap.put(7, temp07);
        tempMap.put(8, temp08);
        tempMap.put(9, temp09);
        tempMap.put(10, temp10);
        tempMap.put(11, temp11);
        tempMap.put(12, temp12);
        tempMap.put(13, temp13);
        tempMap.put(14, temp14);
        tempMap.put(15, temp15);
        tempMap.put(16, temp16);
        tempMap.put(17, temp17);
        tempMap.put(18, temp18);
        tempMap.put(19, temp19);
        tempMap.put(20, temp20);
        tempMap.put(21, temp21);
        tempMap.put(22, temp22);
        tempMap.put(23, temp23);
        tempMap.put(24, temp24);
        tempMap.put(25, temp25);
        tempMap.put(26, temp26);
        tempMap.put(27, temp27);
        tempMap.put(28, temp28);
        tempMap.put(29, temp29);
        tempMap.put(30, temp30);
        tempMap.put(31, temp31);
        tempMap.put(32, temp32);
        tempMap.put(33, temp33);
        tempMap.put(34, temp34);
        tempMap.put(35, temp35);
        tempMap.put(36, temp36);
        tempMap.put(37, temp37);
        tempMap.put(38, temp38);
        tempMap.put(39, temp39);
        tempMap.put(40, temp40);
        tempMap.put(41, temp41);
        tempMap.put(42, temp42);
        tempMap.put(43, temp43);
        tempMap.put(44, temp44);
        tempMap.put(45, temp45);
        tempMap.put(46, temp46);
        tempMap.put(47, temp47);
        tempMap.put(48, temp48);
        tempMap.put(49, temp49);
        tempMap.put(50, temp50);
        tempMap.put(51, temp51);
        tempMap.put(52, temp52);
        tempMap.put(53, temp53);
        tempMap.put(54, temp54);
        tempMap.put(55, temp55);
        tempMap.put(56, temp56);
        tempMap.put(57, temp57);
        tempMap.put(58, temp58);
        tempMap.put(59, temp59);
        tempMap.put(60, temp60);

    }

    public DiskStatus() {
    }

    public DiskStatus(HashMap<Integer, DiskStatistics> diskStatistics) {
        int disksLotCount = DiskModel.diskslot_count;
        DiskStatistics disk;
        if (disksLotCount < 7) {
            this.isDisk6 = true;
            this.isDisk16 = false;
            this.isDisk8 = false;
            for (int i = 1; i < 7; i++) {
                disk = diskStatistics.get(i);
                if (disk != null) {
                    this.disk6[i] = new Disk(disk.state, disk.temperature, true);
                } else {
                    this.disk6[i] = new Disk(0, 0, false);
                }
            }
            this.disk_6_1 = this.disk6[1];
            this.disk_6_2 = this.disk6[2];
            this.disk_6_3 = this.disk6[3];
            this.disk_6_4 = this.disk6[4];
            this.disk_6_5 = this.disk6[5];
            this.disk_6_6 = this.disk6[6];

        } else if (disksLotCount > 6 && disksLotCount < 9) { //如果是八盘位
            this.isDisk6 = false;
            this.isDisk16 = false;
            this.isDisk8 = true;
            for (int i = 1; i < 9; i++) {
                disk = diskStatistics.get(i);
                if (disk != null) {
                    this.disk8[i] = new Disk(disk.state, disk.temperature, true);
                } else {
                    this.disk8[i] = new Disk(0, 0, false);
                }
            }
            this.disk_8_1 = this.disk8[1];
            this.disk_8_2 = this.disk8[2];
            this.disk_8_3 = this.disk8[3];
            this.disk_8_4 = this.disk8[4];
            this.disk_8_5 = this.disk8[5];
            this.disk_8_6 = this.disk8[6];
            this.disk_8_7 = this.disk8[7];
            this.disk_8_8 = this.disk8[8];

        } else {
            this.isDisk6 = false;
            this.isDisk16 = true;
            this.isDisk8 = false;
            for (int i = 1; i < 17; i++) {
                disk = diskStatistics.get(i);
                if (disk != null) {
                    this.disk16[i] = new Disk(disk.state, disk.temperature, true);
                } else {
                    this.disk16[i] = new Disk(0, 0, false);
                }
            }
            this.disk_16_1 = this.disk16[1];
            this.disk_16_2 = this.disk16[2];
            this.disk_16_3 = this.disk16[3];
            this.disk_16_4 = this.disk16[4];
            this.disk_16_5 = this.disk16[5];
            this.disk_16_6 = this.disk16[6];
            this.disk_16_7 = this.disk16[7];
            this.disk_16_8 = this.disk16[8];
            this.disk_16_9 = this.disk16[9];
            this.disk_16_10 = this.disk16[10];
            this.disk_16_11 = this.disk16[11];
            this.disk_16_12 = this.disk16[12];
            this.disk_16_13 = this.disk16[13];
            this.disk_16_14 = this.disk16[14];
            this.disk_16_15 = this.disk16[15];
            this.disk_16_16 = this.disk16[16];
        }
    }

    public void updateDisk_6(HashMap<Integer, DiskStatistics> diskStatistics) {
        this.isDisk6 = true;
        DiskStatistics disk;
        for (int i = 1; i < 7; i++) {
            disk = diskStatistics.get(i);
            if (disk != null) {
                this.disk6[i] = new Disk(disk.state, disk.temperature, true);
            } else {
                this.disk6[i] = new Disk(0, 0, false);
            }
        }
        this.disk_6_1 = this.disk6[1];
        this.disk_6_2 = this.disk6[2];
        this.disk_6_3 = this.disk6[3];
        this.disk_6_4 = this.disk6[4];
        this.disk_6_5 = this.disk6[5];
        this.disk_6_6 = this.disk6[6];

    }

    public void updateDisk_8(HashMap<Integer, DiskStatistics> diskStatistics) {
        this.isDisk8 = true;
        DiskStatistics disk;
        for (int i = 1; i < 9; i++) {
            disk = diskStatistics.get(i);
            if (disk != null) {
                this.disk8[i] = new Disk(disk.state, disk.temperature, true);
            } else {
                this.disk8[i] = new Disk(0, 0, false);
            }
        }
        this.disk_8_1 = this.disk8[1];
        this.disk_8_2 = this.disk8[2];
        this.disk_8_3 = this.disk8[3];
        this.disk_8_4 = this.disk8[4];
        this.disk_8_5 = this.disk8[5];
        this.disk_8_6 = this.disk8[6];
        this.disk_8_7 = this.disk8[7];
        this.disk_8_8 = this.disk8[8];
    }

    public void updateDisk_16(HashMap<Integer, DiskStatistics> diskStatistics) {
        DiskStatistics disk;
        for (int i = 1; i < 17; i++) {
            disk = diskStatistics.get(i);
            if (disk != null) {
                this.disk16[i] = new Disk(disk.state, disk.temperature, true);
            } else {
                this.disk16[i] = new Disk(0, 0, false);
            }
        }
        this.disk_16_1 = this.disk16[1];
        this.disk_16_2 = this.disk16[2];
        this.disk_16_3 = this.disk16[3];
        this.disk_16_4 = this.disk16[4];
        this.disk_16_5 = this.disk16[5];
        this.disk_16_6 = this.disk16[6];
        this.disk_16_7 = this.disk16[7];
        this.disk_16_8 = this.disk16[8];
        this.disk_16_9 = this.disk16[9];
        this.disk_16_10 = this.disk16[10];
        this.disk_16_11 = this.disk16[11];
        this.disk_16_12 = this.disk16[12];
        this.disk_16_13 = this.disk16[13];
        this.disk_16_14 = this.disk16[14];
        this.disk_16_15 = this.disk16[15];
        this.disk_16_16 = this.disk16[16];
    }

    public boolean isIsDisk6() {
        return isDisk6;
    }

    public void setIsDisk6(boolean isDisk6) {
        this.isDisk6 = isDisk6;
    }

    public boolean isIsDisk8() {
        return isDisk8;
    }

    public void setIsDisk8(boolean isDisk8) {
        this.isDisk8 = isDisk8;
    }

    public boolean isIsDisk16() {
        return isDisk16;
    }

    public void setIsDisk16(boolean isDisk16) {
        this.isDisk16 = isDisk16;
    }

    public Disk[] getDisk6() {
        return disk6;
    }

    public void setDisk6(Disk[] disk6) {
        this.disk6 = disk6;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getTmp() {
        return temp;
    }

    public void setTmp(String temp) {
        this.temp = temp;
    }

    public String getDisk_6_1_temp() {
        return Disk_6_1_temp;
    }

    public void setDisk_6_1_temp(String Disk_6_1_temp) {
        this.Disk_6_1_temp = Disk_6_1_temp;
    }

    public Disk getDisk_6_1() {
        return disk_6_1;
    }

    public void setDisk_6_1(Disk disk_6_1) {
        this.disk_6_1 = disk_6_1;
    }

    public Disk getDisk_6_2() {
        return disk_6_2;
    }

    public void setDisk_6_2(Disk disk_6_2) {
        this.disk_6_2 = disk_6_2;
    }

    public Disk getDisk_6_3() {
        return disk_6_3;
    }

    public void setDisk_6_3(Disk disk_6_3) {
        this.disk_6_3 = disk_6_3;
    }

    public Disk getDisk_6_4() {
        return disk_6_4;
    }

    public void setDisk_6_4(Disk disk_6_4) {
        this.disk_6_4 = disk_6_4;
    }

    public Disk getDisk_6_5() {
        return disk_6_5;
    }

    public void setDisk_6_5(Disk disk_6_5) {
        this.disk_6_5 = disk_6_5;
    }

    public Disk getDisk_6_6() {
        return disk_6_6;
    }

    public void setDisk_6_6(Disk disk_6_6) {
        this.disk_6_6 = disk_6_6;
    }

    public Disk getDisk_8_1() {
        return disk_8_1;
    }

    public void setDisk_8_1(Disk disk_8_1) {
        this.disk_8_1 = disk_8_1;
    }

    public Disk getDisk_8_2() {
        return disk_8_2;
    }

    public void setDisk_8_2(Disk disk_8_2) {
        this.disk_8_2 = disk_8_2;
    }

    public Disk getDisk_8_3() {
        return disk_8_3;
    }

    public void setDisk_8_3(Disk disk_8_3) {
        this.disk_8_3 = disk_8_3;
    }

    public Disk getDisk_8_4() {
        return disk_8_4;
    }

    public void setDisk_8_4(Disk disk_8_4) {
        this.disk_8_4 = disk_8_4;
    }

    public Disk getDisk_8_5() {
        return disk_8_5;
    }

    public void setDisk_8_5(Disk disk_8_5) {
        this.disk_8_5 = disk_8_5;
    }

    public Disk getDisk_8_6() {
        return disk_8_6;
    }

    public void setDisk_8_6(Disk disk_8_6) {
        this.disk_8_6 = disk_8_6;
    }

    public Disk getDisk_8_7() {
        return disk_8_7;
    }

    public void setDisk_8_7(Disk disk_8_7) {
        this.disk_8_7 = disk_8_7;
    }

    public Disk getDisk_8_8() {
        return disk_8_8;
    }

    public void setDisk_8_8(Disk disk_8_8) {
        this.disk_8_8 = disk_8_8;
    }

    public Disk getDisk_16_1() {
        return disk_16_1;
    }

    public void setDisk_16_1(Disk disk_16_1) {
        this.disk_16_1 = disk_16_1;
    }

    public Disk getDisk_16_2() {
        return disk_16_2;
    }

    public void setDisk_16_2(Disk disk_16_2) {
        this.disk_16_2 = disk_16_2;
    }

    public Disk getDisk_16_3() {
        return disk_16_3;
    }

    public void setDisk_16_3(Disk disk_16_3) {
        this.disk_16_3 = disk_16_3;
    }

    public Disk getDisk_16_4() {
        return disk_16_4;
    }

    public void setDisk_16_4(Disk disk_16_4) {
        this.disk_16_4 = disk_16_4;
    }

    public Disk getDisk_16_5() {
        return disk_16_5;
    }

    public void setDisk_16_5(Disk disk_16_5) {
        this.disk_16_5 = disk_16_5;
    }

    public Disk getDisk_16_6() {
        return disk_16_6;
    }

    public void setDisk_16_6(Disk disk_16_6) {
        this.disk_16_6 = disk_16_6;
    }

    public Disk getDisk_16_7() {
        return disk_16_7;
    }

    public void setDisk_16_7(Disk disk_16_7) {
        this.disk_16_7 = disk_16_7;
    }

    public Disk getDisk_16_8() {
        return disk_16_8;
    }

    public void setDisk_16_8(Disk disk_16_8) {
        this.disk_16_8 = disk_16_8;
    }

    public Disk getDisk_16_9() {
        return disk_16_9;
    }

    public void setDisk_16_9(Disk disk_16_9) {
        this.disk_16_9 = disk_16_9;
    }

    public Disk getDisk_16_10() {
        return disk_16_10;
    }

    public void setDisk_16_10(Disk disk_16_10) {
        this.disk_16_10 = disk_16_10;
    }

    public Disk getDisk_16_11() {
        return disk_16_11;
    }

    public void setDisk_16_11(Disk disk_16_11) {
        this.disk_16_11 = disk_16_11;
    }

    public Disk getDisk_16_12() {
        return disk_16_12;
    }

    public void setDisk_16_12(Disk disk_16_12) {
        this.disk_16_12 = disk_16_12;
    }

    public Disk getDisk_16_13() {
        return disk_16_13;
    }

    public void setDisk_16_13(Disk disk_16_13) {
        this.disk_16_13 = disk_16_13;
    }

    public Disk getDisk_16_14() {
        return disk_16_14;
    }

    public void setDisk_16_14(Disk disk_16_14) {
        this.disk_16_14 = disk_16_14;
    }

    public Disk getDisk_16_15() {
        return disk_16_15;
    }

    public void setDisk_16_15(Disk disk_16_15) {
        this.disk_16_15 = disk_16_15;
    }

    public Disk getDisk_16_16() {
        return disk_16_16;
    }

    public void setDisk_16_16(Disk disk_16_16) {
        this.disk_16_16 = disk_16_16;
    }

    public String getDisk_6_1_state() {
        return Disk_6_1_state;
    }

    public void setDisk_6_1_state(String Disk_6_1_state) {
        this.Disk_6_1_state = Disk_6_1_state;
    }

    public void setDisk_8_1(String Disk_8_1) {
        this.Disk_8_1 = Disk_8_1;
    }

    public void setDisk_16_1(String Disk_16_1) {
        this.Disk_16_1 = Disk_16_1;
    }
}
