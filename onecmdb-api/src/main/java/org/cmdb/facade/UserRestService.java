package org.cmdb.facade;

import org.cmdb.converter.Converter;
import org.cmdb.dto.UserDTO;
import org.cmdb.entity.User;

/**
 * 用户信息 接口
 * <p>
 * Created by tom on 2017-03-07 13:25:01.
 */
 public interface UserRestService extends CURDRestService<UserDTO>,Converter<UserDTO, User> {

 }
