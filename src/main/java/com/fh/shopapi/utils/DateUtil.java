package com.fh.shopapi.utils;


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil implements Serializable {

    public static final String Y_M_D="yyyy-MM-dd";
    public static final String FUFF_YEAR="yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHMMSS="yyyyMMddHHmmss";

    //将日期格式转化为字符串
    public static final String date2str(Date date, String pattern){
        if (date == null){
            return "";
        }
        SimpleDateFormat sim=new SimpleDateFormat(pattern);
        String datestr=sim.format(date);
        return datestr;
    }

    //将字符串格式转为日期格式
    public static final Date str2date(String date, String pattern){
        if (StringUtils.isEmpty(date)){
            return null;
        }
        SimpleDateFormat sim=new SimpleDateFormat(pattern);
        Date currDate = null;
        try {
            currDate = sim.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currDate;
    }



}
