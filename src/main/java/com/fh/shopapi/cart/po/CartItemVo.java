package com.fh.shopapi.cart.po;

import lombok.Data;

import java.io.Serializable;
@Data
public class CartItemVo implements Serializable {
    private Long productId;

    private String productName;

    private String price;

    private Long count;

    private String image;

    private String subTotalPrice;

}
