package com.fh.shopapi.member.controller;

import com.fh.shopapi.annotation.Check;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.member.biz.IMemberService;
import com.fh.shopapi.member.po.Member;
import com.fh.shopapi.member.vo.MemberVo;
import com.fh.shopapi.utils.KeyUtil;
import com.fh.shopapi.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("members")
public class MemberController {
    @Resource(name = "memberService")
    private IMemberService memberService;
    @Autowired
    private HttpServletRequest request;
    
    //查询当前登陆的会员信息
    @Check
    @GetMapping("findMember")
    public ServerResponse findMember(MemberVo memberVo) {
        return ServerResponse.success(memberVo.getRealName());
    }

    //退出
    @Check
    @PostMapping("logout")
    public ServerResponse lonout(MemberVo memberVo){
        RedisUtil.del(KeyUtil.buildLoginMemberKey(memberVo.getMemberName(),memberVo.getUuid()));
        return ServerResponse.success();
    }

    //手机号登陆
    @PostMapping("smsLogin")
    public ServerResponse smsLogin(String phone, String smsCode){
        return memberService.smsLogin(phone,smsCode);
    }

    //会员账号登录
    @PostMapping("login")
    public ServerResponse memberLogin(Member member) {
        return memberService.memberLogin(member);
    }

    //通过手机查询会员
    @GetMapping("phone")
    public ServerResponse findMemberByPhone(String phone) {
        return memberService.findMemberOne("phone",phone);
    }

    //通过邮箱查询会员
    @GetMapping("email")
    public ServerResponse findMemberByEmail(String email) {
        return memberService.findMemberOne("email", email);
    }

    //通过会员名查询会员
    @GetMapping
    public ServerResponse findMemberByName(String memberName) {
        return memberService.findMemberOne("memberName", memberName);
    }
    
    //注册
    @PostMapping
    public ServerResponse addMember(Member member) {
        return memberService.addMember(member);
    }



}
