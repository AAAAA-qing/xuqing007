package com.fh.shopapi.sort.po;

import lombok.Data;

import java.io.Serializable;
@Data
public class Sort implements Serializable {
    private Long id;

    private String sortName;

    private Long fatherId;

}
