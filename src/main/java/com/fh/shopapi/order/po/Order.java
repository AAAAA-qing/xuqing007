package com.fh.shopapi.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {
    @TableId(type = IdType.INPUT)
    private String id;//主键 订单id

    private Long userId;//用户id

    private Integer status;//状态

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//订单创建时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;//订单支付时间

    private String totalPrice;//订单总价

    private Integer payType;//支付类型

    private Long totalCount;//订单总个数

    private String orderStatusDescribe;//订单状态描述

    private String consignee;//收货人

    private String consigneePhone;//收货人电话

    private String address;//收货人地址

    private String postcode;//邮编

    private BigDecimal postage;//邮费

    private Integer invoice;//发票

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderCloseTime;//订单关闭时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderSuccessTime;//订单完成时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shipmentsTime;//订单发货时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderCommentTime;//订单完成评论时间

}
