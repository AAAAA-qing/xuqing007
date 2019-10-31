package com.fh.shopapi.utils;

public class SystemConstant {
    //token登陆的秘钥
    public static String SECRETKEY = "l/dsa!@#$das%^&fsa*das)dsp/vj";
    //商品的上架状态
    public static int PRODUCT_VALID_STATUS = 1;
    //redis中 商品购物车的 map名
    public static String PRODUCT_CART_MAP = "cartMap";

    public static int ORDER_STATUS_NO_PAY = 0; //未支付
    public static int ORDER_STATUS_YES_PAY = 1; //已支付

}
