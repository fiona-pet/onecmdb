package org.cmdb.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户注册 输入
 * Created by tom on 16/1/21.
 */
@Getter
@Setter
@ToString
public class SignUpDTO implements Serializable {
    /**
     * 电话
     */
    @NotNull
    @Size(min = 11, max = 11)
    private String phone_number;
    /**
     * 密码
     */
    @NotNull
    @Size(min = 6, max = 32)
    private String password;
    /**
     * 名称
     */
    @NotNull
    @Size(min = 2, max = 32)
    private String username;
    /**
     * 验证码
     */
    @NotNull
    private String verification_code;
}
