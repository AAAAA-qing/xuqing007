package com.fh.shopapi.address.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
@Data
public class Address implements Serializable {
    @TableId(type = IdType.INPUT)
    private String id;

    private Long memberId;

    private String realName;

    private Long area1;

    private Long area2;

    private Long area3;

    private String streetFloor;

    private String consigneePhone;

    private String postcode;

    @TableField(exist = false)
    private String areaName;
}
