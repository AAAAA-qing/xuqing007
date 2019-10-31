package com.fh.shopapi.area.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Area implements Serializable {
    private Long id;

    private String cityName;

    @TableField("pid")
    private Long fatherId;


}
