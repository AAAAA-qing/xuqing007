package com.fh.shopapi.address.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class AddressVo implements Serializable {
    private String id;

    private String realName;

    private String streetFloor;

    private String areaName;

    private String consigneePhone;

    private String postcode;
}
