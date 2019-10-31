package com.fh.shopapi.brand.param;

import com.fh.shopapi.common.Page;
import lombok.Data;

import java.io.Serializable;
@Data
public class BrandSearchParam extends Page implements Serializable {
    private String brandName;

    private Integer isSellWell;

}
