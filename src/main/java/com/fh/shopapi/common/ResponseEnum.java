package com.fh.shopapi.common;

import java.io.Serializable;

public enum ResponseEnum implements Serializable {
    LOGIN_INFO_TIMEOUT(201,"登录信息过期!"),
    HEADER_INFO_IS_UPDATE(208,"头信息被篡改!"),
    HEADER_INFO_IS_MISS(202,"头信息丢失!"),
    HEADER_INFO_IS_NULL(203,"头信息不存在!"),
    HEADER_TOKEN_IS_NULL(233,"token不存在!"),
    PASSWORD_IS_ERROR(204,"密码错误"),
    MEMBER_NAME_IS_NULL(205,"会员名不存在!"),
    PASSWORD_IS_NULL(207,"密码不能为空!"),
    MEMBERNAME_IS_NULL(209,"会员名不能为空!"),
    EMAIL_IS_EXIXT(210,"邮箱已存在!"),
    PHONE_IS_EXIXT(211,"手机号已存在!"),
    MEMBERNAME_IS_EXIXT(212,"用户名已存在!"),
    ALL_NOT_IS_NULL(213,"所有选项不能为空!"),
    BRAND_IS_NULL(214,"品牌不存在!"),
    BRAND_IDS_IS_NULL(215,"请选择删除品牌!"),
    SMS_CODE_ERROR(216,"验证码错误!"),
    PHONE_IS_NULL(218,"请输入手机号"),
    POST_CODE_ERROR(219,"您的手机号是错误格式"),
    PHONE_IS_11(220,"手机号应为11位数"),
    PHONE_STYLE_ERROR(221,"请填入正确的手机号"),
    PRODUCT_IS_NULL(222,"商品为空"),
    PRODUCT_IS_NOT_VALID(223,"商品未上架"),
    PRODUCT_STOCK_IS_MIN(224,"商品库存不足"),
    MEMBER_CART_IS_NULL(225,"购物车为空"),
    MEMBER_CART_ITEM_IS_NULL(226,"购物车中不存在该商品"),
    PHONE_SMSCODE_IS_NULL(227,"手机号和短信不能为空"),
    PHONE_IS_NOT_EXIXT(228,"手机号未注册"),
    ADDRESS_INFO_IS_NULL(229,"地址信息不能有空"),
    MEMBER_ADDRESS_INFO_IS_NULL(230,"无收货地址,请添加"),
    DLELTE_ADDRESS_IS_NULL(231,"删除收货的地址不存在"),
    METHOD_REPET_SUBMIT(232,"请勿重复提交"),//
    STOCK_IS_SHORTAGE(234,"您提交的商品库存不足,无法提交订单"),//
    ORDER_IS_MISS(235,"订单不存在"),
    NATIVE_OUT_TIME(236,"二维码过期"),
    SMS_CODE_EXPIRE(217,"验证码过期!");


    private Integer code;

    private String msg;

    ResponseEnum(Integer code,String msg) {
        this.code = code;
        this.msg =msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
