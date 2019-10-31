package com.fh.shopapi.member.biz;

import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.member.po.Member;

public interface IMemberService {

    ServerResponse addMember(Member member);

    ServerResponse memberLogin(Member member);

    ServerResponse findMemberOne(String key, Object value);

    ServerResponse smsLogin(String phone, String smsCode);
}
