package com.fh.shopapi.brand.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class BrandVo implements Serializable {
    private Long id;

    private String brandName;

    private String photo;

    private Integer isSellWell;

    private Integer sort;
}
