package com.fh.shopapi.brand.po;

import lombok.Data;

import java.io.Serializable;
@Data
public class Brand implements Serializable {
    private Long id;

    private String brandName;

    private String photo;

    private Integer isSellWell;

    private Integer sort;

}
