package com.fh.shopapi.utils;

public class SystemConst {
    public static final String CUURREND_USER="user";
    public static final String LOGIN_MEMBER="member";
    //当前登录用户的菜单资源集合
    public static final String LOGIN_USER_RESOURCE="menuList";
    //所有菜单集合
    public static final String RESOURCE_LIST="resourceList";
    //当前用户的所有资源
    public static final String LOGIN_USER_ALL_RESOURCE="userResourceList";
    //拦截页面
    public static final String INTERCEPTOR="/interceptor.jsp";
    public static int CONGT_1 = 1;
    public static int CONGT_0 = 0;
    public static int LOGIN_ERROR_MAX_COUNT = 3;
    //用户默认密码
    public static final String USER_DEFAULT_PASSWORD = "111";
    public static final String COMPANY_NAME = "1902";//导出pdf文件的单位
    public static final String TEMPLATE_PATH = "/template";//模板所属文件夹
    public static final String PRODUCT_PDF_TEMPLATE_FILE = "info.html";//制定pdf模板html
    public static final Integer LOG_STATTUS_SUCCESS = 1;//日志记录
    public static final Integer LOG_STATTUS_ERROR = 0;//日志记录




    //收件人邮箱
    public static final String receiveMailAccount = "734481016@qq.com";
    // 发送邮件的账号
    public static final String ownEmailAccount = "1815356773@qq.com";
    // 发送邮件的密码------》授权码
    public static final String ownEmailPassword = "befugefcqraidced";
    // 发送邮件的smtp 服务器 地址
    public static final String myEmailSMTPHost = "smtp.qq.com";




    public static final String IMAGES_PATH = "/images/";

    //上传图片到阿里OSS
    public static final String ENDPOINT ="https://oss-cn-qingdao.aliyuncs.com";
    public static final String ACCESSKEYID = "LTAI4FmB67hfvNPjtMKPi6W9";
    public static final String ACCESSKEYSECRET = "0eEsFfv4HFLv1K4u7RmaNE8es4qlvC";
    public static final String BUCKETNAME = "gao-yuan-hang";
    public static final String HOST = "http://gao-yuan-hang.oss-cn-qingdao.aliyuncs.com/";

    //分布式session
    public static final String NAME = "fh_id";//sessionId
    public static final String DOMAIN = "admin.shop.com";//域名
    public static final int CODE_EXPIRE = 3*60;//验证码失效时间
    public static final int SMS_CODE_EXPIRE = 5*60;//验证码失效时间
    public static final int USER_EXPIRE = 30*60;//用户登录失效时间
    public static final int SHOW_PRODUCT_TIME = 30*60;//前台商品缓存 生命周期

    public static final int MEMBER_REDIS_TIME_OUT = 30*60;//会员登录后 生命周期
    public static final int REDIS_TOKEN_TIME = 30*60;//redis中token存活时间


}
