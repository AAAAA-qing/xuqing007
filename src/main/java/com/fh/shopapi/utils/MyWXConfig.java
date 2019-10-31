package com.fh.shopapi.utils;

import com.fh.github.wxpay.sdk.IWXPayDomain;
import com.fh.github.wxpay.sdk.WXPayConfig;
import com.fh.github.wxpay.sdk.WXPayConstants;

import java.io.InputStream;

public class MyWXConfig extends WXPayConfig {


    @Override
    public String getAppID() {
        return "wxa1e44e130a9a8eee";
    }

    @Override
    public String getMchID() {
        return "1507758211";
    }

    @Override
    public String getKey() {
        return "feihujiaoyu12345678yuxiaoyang123";
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }
}
