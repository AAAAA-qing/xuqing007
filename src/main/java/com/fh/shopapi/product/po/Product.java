package com.fh.shopapi.product.po;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class Product implements Serializable {
    private Long id;

    private String productName;

    private BigDecimal price;

    private Integer stock;

    private Integer isSellWell;

    private Integer isValid;

    private String mainImage;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer  brandId;

}
