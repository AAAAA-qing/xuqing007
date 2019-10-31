package com.fh.shopapi.order.param;

import lombok.Data;

import java.io.Serializable;
@Data
public class OrderParam implements Serializable {

    private Integer payType;//支付方式

    private String addressId;//地址id


}
