package org.cmdb.extension;

import org.cmdb.dto.RestResult;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by X on 2017/3/23.
 * smart 异常处理
 */
public class SmartExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException ex) {
      RestResult restResult = new RestResult();
      if (ex instanceof UnauthorizedException) {
          restResult.setErrorCode(Response.Status.FORBIDDEN.getStatusCode());
          restResult.setErrorMessage("请检查资源权限");
          LOGGER.warn("请检查资源权限");
          return Response.status(Response.Status.OK).entity(restResult).type(ContentType.APPLICATION_JSON_UTF_8).build();
      }
      if (ex instanceof AuthenticationException) {
          restResult.setErrorCode(Response.Status.UNAUTHORIZED.getStatusCode());
          restResult.setErrorMessage("用户认证失败");
          LOGGER.warn("用户认证失败");
          return Response.status(Response.Status.OK).entity(restResult).type(ContentType.APPLICATION_JSON_UTF_8).build();
      }

      if (ex instanceof NotFoundException) {
          restResult.setErrorCode(Response.Status.NOT_FOUND.getStatusCode());
          restResult.setErrorMessage("找不到资源地址");
          LOGGER.warn("找不到资源地址");
          return Response.status(Response.Status.OK).entity(restResult).type(ContentType.APPLICATION_JSON_UTF_8).build();
      }
      String message = ex.getMessage();
      if (message == null) {
          restResult.setErrorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
          restResult.setErrorMessage("服务器内部错误");
          LOGGER.error("服务器内部错误", ex);
      } else if (message.contains("IncorrectCredentialsException")) {
          restResult.setErrorCode(Response.Status.UNAUTHORIZED.getStatusCode());
          restResult.setErrorMessage("用户名/密码错误");
          LOGGER.warn("用户名/密码错误");
      } else if (message.contains("AuthenticationException")) {
          restResult.setErrorCode(Response.Status.UNAUTHORIZED.getStatusCode());
          restResult.setErrorMessage("用户认证失败");
          LOGGER.warn("用户认证失败");
      } else if (message.contains("ConstraintViolationException")) {
          restResult.setErrorCode(Response.Status.BAD_REQUEST.getStatusCode());
          restResult.setErrorMessage("数据验证失败");
          LOGGER.error("数据验证失败", ex);
      } else {
          restResult.setErrorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
          restResult.setErrorMessage(message);
          LOGGER.error(message, ex);
      }
      return Response.status(Response.Status.OK).entity(restResult).type(ContentType.APPLICATION_JSON_UTF_8).build();
    }
}
