package com.fh.shopapi.pay.result;

import lombok.Data;

import java.io.Serializable;
@Data
public class PayResult implements Serializable {
    private String codeUrl;//二维码

    private String outTradeNo;//交易单号

    private String payPrice;//支付金额
}
