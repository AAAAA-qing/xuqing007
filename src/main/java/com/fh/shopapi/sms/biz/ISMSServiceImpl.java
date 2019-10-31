package com.fh.shopapi.sms.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.sms.result.SMSResult;
import com.fh.shopapi.utils.KeyUtil;
import com.fh.shopapi.utils.RedisUtil;
import com.fh.shopapi.utils.SMSUtil;
import com.fh.shopapi.utils.SystemConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("smsService")
@Transactional(rollbackFor = Exception.class)
public class ISMSServiceImpl implements ISMSService {

    //发送短信验证码
    @Override
    public ServerResponse postPhoneCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return ServerResponse.error(ResponseEnum.PHONE_IS_NULL);
        }
        //手机号 正则验证
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            //手机号应为11位数
            return ServerResponse.error(ResponseEnum.PHONE_IS_11);
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if (!isMatch) {
            //您的手机号是错误格式
            return ServerResponse.error(ResponseEnum.PHONE_STYLE_ERROR);
        }

        //发送验证码
        String result = SMSUtil.postPhoneSMS(phone);
        SMSResult smsResult = JSON.parseObject(result, SMSResult.class);
        Integer code = smsResult.getCode();
        //判断是否发送成功
        if (code != 200) {
            return ServerResponse.error(ResponseEnum.POST_CODE_ERROR);
        }
        //获取验证码  存储到redis
        String SMSCode = smsResult.getObj();
        RedisUtil.setEx(KeyUtil.buildSMSCodeKey(phone),SMSCode,SystemConst.SMS_CODE_EXPIRE);
        return ServerResponse.success();
    }
}
