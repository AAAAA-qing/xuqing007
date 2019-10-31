package com.fh.shopapi.token.biz;

import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.utils.RedisUtil;
import com.fh.shopapi.utils.SystemConst;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("tokenService")
public class ITokenServiceImpl implements ITokenService {

    //生成token
    @Override
    public ServerResponse createToken() {
        String token = UUID.randomUUID().toString();
        //将token存储到redis中
        RedisUtil.setEx(token,token,SystemConst.REDIS_TOKEN_TIME);//30分钟存活时间
        return ServerResponse.success(token);
    }

}
