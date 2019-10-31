package com.fh.shopapi.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IdUtil {

    //生成雪花算法的id  时间精确到分
    public static String getID() {
        DateTimeFormatter yyyyMMddHHmm = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String id = LocalDateTime.now().format(yyyyMMddHHmm) + IdWorker.getId();
        return id;
    }


}
