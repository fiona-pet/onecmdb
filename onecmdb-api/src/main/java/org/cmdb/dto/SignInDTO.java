package org.cmdb.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户登录
 * Created by tom on 16/1/21.
 */
@Getter
@Setter
@ToString
public class SignInDTO implements Serializable {

    private String name;

    private String password;
}
