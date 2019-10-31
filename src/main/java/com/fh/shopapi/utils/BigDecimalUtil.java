package com.fh.shopapi.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {

    //BigDecimal类型的乘法运算
    public static BigDecimal mul(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.multiply(b2).setScale(2);
    }

    //BigDecimal类型的加法运算
    public static BigDecimal add(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.add(b2).setScale(2);
    }
    
}
