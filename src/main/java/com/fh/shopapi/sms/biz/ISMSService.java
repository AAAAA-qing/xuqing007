package com.fh.shopapi.sms.biz;

import com.fh.shopapi.common.ServerResponse;

public interface ISMSService {
    ServerResponse postPhoneCode(String phone);
}
