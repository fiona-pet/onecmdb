package org.cmdb.service;

import org.cmdb.entity.User;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 用户信息
 * Created by tom on 2017-03-07 13:25:01.
 */
public interface UserService extends CURDService<User> {
      /**
       * 根据登录名获取用户信息
       *
       * @param loginName 登录名
       *
       * @return
       */
      User getUserByLoginName(@NotBlank @NotNull String loginName);
}
