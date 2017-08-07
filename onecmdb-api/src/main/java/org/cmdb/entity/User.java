package org.cmdb.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户信息
 * <p>
 * Created by tom on 2017-03-07 15:29:42.
 */
@Entity
@Table(name = "t_user")
@DynamicInsert
@DynamicUpdate
@Setter
@Getter
@ToString
public class User extends IdEntity {
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
