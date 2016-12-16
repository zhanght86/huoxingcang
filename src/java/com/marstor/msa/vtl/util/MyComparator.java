/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.util;

import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class MyComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null && o2 != null) {
            return -1;
        }
        if (o1 != null && o2 == null) {
            return 1;
        }
        return o1.toString().compareTo(o2.toString());
    }
}
