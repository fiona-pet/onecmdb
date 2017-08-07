package org.cmdb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/5/11
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
@ApiModel("用户信息")
@ToString
@Setter
@Getter
public class UserDTO {
    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名", required = true)
    private String loginName;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String name;
    /**
     * 加密密码
     */
    @ApiModelProperty(value = "加密密码", required = true)
    private String password;
    /**
     * 注册日期
     */
    @ApiModelProperty(value = "注册日期")
    private Date registerDate;
    /**
     * 所属企业id
     */
    @ApiModelProperty(value = "所属企业id", required = true)
    private String enterpriseId;
}
