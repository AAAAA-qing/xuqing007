package com.fh.shopapi.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("t_order_detail")
public class OrderDetail implements Serializable {

    private String orderId;//订单id

    private Long userId;//用户ID

    private Long productId;//商品id

    private String productName;//商品名字

    private String image;//图片

    private BigDecimal price;//价格

    private Long count;//个数

    private String subTotalCount;//小计
}
