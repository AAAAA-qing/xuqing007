package com.fh.shopapi.product.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class ProductVo implements Serializable {
    private Long id;

    private String productName;

    private String price;

    private Integer stock;

    private Integer isSellWell;

    private Integer isValid;

    private String mainImage;

    private String createTime;

    private String brandName;

    private Integer brandId;

}
