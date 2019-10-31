package com.fh.shopapi.payLog.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("t_paylog")
public class PayLog implements Serializable {
    @TableId(value="out_trade_no",type = IdType.INPUT)
    private String outTradeNo;

    private String orderId;

    private Long userId;

    private Date createTime;

    private Date payTime;

    private BigDecimal payPrice;

    private Integer payStatic;

    private Integer payType;

    private String transactionId;
}
