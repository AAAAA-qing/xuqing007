package com.fh.shopapi.member.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.member.mapper.IMemberMapper;
import com.fh.shopapi.member.po.Member;
import com.fh.shopapi.member.vo.MemberVo;
import com.fh.shopapi.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.UUID;

@Service("memberService")
@Transactional(rollbackFor = Exception.class)
public class IMemberServiceImpl implements IMemberService {
    @Autowired
    private IMemberMapper memberMapper;

    //新增用户
    @Override
    public ServerResponse addMember(Member member) {
        //非空验证
        String memberName = member.getMemberName();
        String phone = member.getPhone();
        String email = member.getEmail();
        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(member.getPassword()) || StringUtils.isEmpty(member.getRealName()) ||
                StringUtils.isEmpty(email) || StringUtils.isEmpty(email) || StringUtils.isEmpty(member.getArea1().toString()) || StringUtils.isEmpty(member.getArea2().toString())
                || StringUtils.isEmpty(member.getArea3().toString()) || StringUtils.isEmpty(DateUtil.date2str(member.getBirthday(),DateUtil.Y_M_D))){
            return ServerResponse.error(ResponseEnum.ALL_NOT_IS_NULL);
        }
        //验证验证码
        String SMSCode = RedisUtil.get(KeyUtil.buildSMSCodeKey(phone));
        if (SMSCode == null) {
            return ServerResponse.error(ResponseEnum.SMS_CODE_EXPIRE);
        }
        if (!member.getSmsCode().equals(SMSCode)) {
            return ServerResponse.error(ResponseEnum.SMS_CODE_ERROR);
        }
        //用户名 唯一验证
        QueryWrapper<Member> queryWrapperMemberName = new QueryWrapper<>();
        queryWrapperMemberName.eq("memberName",memberName);
        Member memberMemberName = memberMapper.selectOne(queryWrapperMemberName);
        if (memberMemberName != null) {
            return ServerResponse.error(ResponseEnum.MEMBERNAME_IS_EXIXT);
        }
        //电话 唯一 验证
        QueryWrapper<Member> queryWrapperPhone = new QueryWrapper<>();
        queryWrapperPhone.eq("phone",phone);
        Member memberPhone = memberMapper.selectOne(queryWrapperPhone);
        if (memberPhone != null) {
            return ServerResponse.error(ResponseEnum.PHONE_IS_EXIXT);
        }
        //邮箱 唯一 验证
        QueryWrapper<Member> queryWrapperEmail = new QueryWrapper<>();
        queryWrapperEmail.eq("email",email);
        Member memberEmail = memberMapper.selectOne(queryWrapperEmail);
        if (memberEmail != null) {
            return ServerResponse.error(ResponseEnum.EMAIL_IS_EXIXT);
        }
        //加盐
        String salt = UUID.randomUUID().toString();
        member.setSalt(salt);
        //密码 加密加盐再加密
        String password = Md5Util.encodePassword(member.getPassword(), salt);
        member.setPassword(password);
        memberMapper.insert(member);
        return ServerResponse.success();
    }

    //会员登录
    @Override
    public ServerResponse memberLogin(Member member) {
        //登录信息非空验证
        String memberName = member.getMemberName();
        String password = member.getPassword();
        if (StringUtils.isEmpty(memberName)) {
            return ServerResponse.error(ResponseEnum.MEMBERNAME_IS_NULL);
        }
        if (StringUtils.isEmpty(password)) {
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_NULL);
        }
        //判断会员是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberName", memberName);
        Member memberInfo = memberMapper.selectOne(queryWrapper);
        if (memberInfo == null) {
            return ServerResponse.error(ResponseEnum.MEMBER_NAME_IS_NULL);
        }
        //判断密码是否正确
        String md5Password = Md5Util.encodePassword(password, memberInfo.getSalt());
        if (!md5Password.equals(memberInfo.getPassword())) {
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_ERROR);
        }
        String uuid = UUID.randomUUID().toString();
        MemberVo memberVo = new MemberVo();
        memberVo.setId(memberInfo.getId());
        memberVo.setMemberName(memberName);
        memberVo.setUuid(uuid);
        memberVo.setRealName(memberInfo.getRealName());
        //将用户信息转为json对象
        String memberVoJson = JSONObject.toJSONString(memberVo);
        //进行base64编码
        String member64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes());
        //根据密钥 进行md5 散列算法 生成签名/指纹/摘要
        String sign = Md5Util.sign(member64, SystemConstant.SECRETKEY);
        //将用户信息 和 签名进行 base64编码处理 返回到前台
        String signInfo = Base64.getEncoder().encodeToString(sign.getBytes());
        //将用户信息存储到redis
        RedisUtil.setEx(KeyUtil.buildLoginMemberKey(memberName,uuid),"1",SystemConst.MEMBER_REDIS_TIME_OUT);
        //拼接返回前台的数据
        String result = member64 + "." + signInfo;
        System.out.println("base64签名"+result);
        return ServerResponse.success(result);
    }

    //通过一个字段的值查询 一个对象
    @Override
    public ServerResponse findMemberOne(String key, Object value) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(key, value);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member == null) {
            return ServerResponse.error();
        }
        return ServerResponse.success(member);
    }

    //手机号登陆
    @Override
    public ServerResponse smsLogin(String phone, String smsCode) {
        //非空判断
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsCode)){
            return ServerResponse.error(ResponseEnum.PHONE_SMSCODE_IS_NULL);
        }
        //验证 短信验证码
        String SMSCode = RedisUtil.get(KeyUtil.buildSMSCodeKey(phone));
        if (SMSCode == null) {
            return ServerResponse.error(ResponseEnum.SMS_CODE_EXPIRE);
        }
        if (!smsCode.equals(SMSCode)) {
            return ServerResponse.error(ResponseEnum.SMS_CODE_ERROR);
        }
        //判断手机号是否注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member == null) {
            //手机号未注册
            return ServerResponse.error(ResponseEnum.PHONE_IS_NOT_EXIXT);
        }

        //验证成功
        String uuid = UUID.randomUUID().toString();
        MemberVo memberVo = new MemberVo();
        memberVo.setId(member.getId());
        memberVo.setMemberName(member.getMemberName());
        memberVo.setUuid(uuid);
        memberVo.setRealName(member.getRealName());
        //将用户信息转为json对象
        String memberVoJson = JSONObject.toJSONString(memberVo);
        //进行base64编码
        String member64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes());
        //根据密钥 进行md5 散列算法 生成签名/指纹/摘要
        String sign = Md5Util.sign(member64, SystemConstant.SECRETKEY);
        //将用户信息 和 签名进行 base64编码处理 返回到前台
        String signInfo = Base64.getEncoder().encodeToString(sign.getBytes());
        //将用户信息存储到redis
        RedisUtil.setEx(KeyUtil.buildLoginMemberKey(member.getMemberName(),uuid),"1",SystemConst.MEMBER_REDIS_TIME_OUT);
        //拼接返回前台的数据
        String result = member64 + "." + signInfo;
        System.out.println("base64签名"+result);
        return ServerResponse.success(result);
    }

}
