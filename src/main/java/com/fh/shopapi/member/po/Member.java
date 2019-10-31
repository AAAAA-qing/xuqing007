package com.fh.shopapi.member.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
public class Member implements Serializable {
    private Long id;

    private String memberName;

    private String password;

    private String realName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String email;

    private String salt;

    private String phone;

    @TableField(exist = false)
    private String smsCode;

    private Long area1;

    private Long area2;

    private Long area3;

}
