package org.cmdb.extension;

import org.cmdb.security.BitPermission;
import org.cmdb.security.PermissionEnum;

import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;

import java.io.IOException;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;

/**
 * 权限检查
 *
 * @author MQ
 */
@Priority(Priorities.USER)
public class PermissionFilter implements ContainerRequestFilter {

    private boolean post(String uri, Subject subject) {
        String modelName = uri.replace("/api/", "");
        int indexof;
        BitPermission bitPermission;
        if ((indexof = modelName.lastIndexOf("/page")) > -1) {
            modelName = modelName.substring(0, indexof);
            bitPermission = new BitPermission("+" + modelName + "+" + PermissionEnum.QUERY.getValue());

        } else if ((indexof = modelName.lastIndexOf("/list")) > -1) {
            modelName = modelName.substring(0, indexof);
            bitPermission = new BitPermission("+" + modelName + "+" + PermissionEnum.QUERY.getValue());
        } else {
            bitPermission = new BitPermission("+" + modelName + "+" + PermissionEnum.CREATE.getValue());
        }
        return subject.isPermitted(bitPermission);
    }

    private boolean get(String uri, Subject subject) {
        String modelName = uri.replace("/api/", "");
        modelName = modelName.substring(0, modelName.lastIndexOf("/"));
        BitPermission bitPermission = new BitPermission("+" + modelName + "+" + PermissionEnum.QUERY.getValue());
        return subject.isPermitted(bitPermission);
    }

    private boolean delete(String uri, Subject subject) {
        String modelName = uri.replace("/api/", "");
        modelName = modelName.substring(0, modelName.lastIndexOf("/"));
        BitPermission bitPermission = new BitPermission("+" + modelName + "+" + PermissionEnum.DELETE.getValue());
        return subject.isPermitted(bitPermission);
    }

    private boolean allowUrI(String uri) {
        String modelName = uri.replace("/api/", "");
        modelName = modelName.substring(0, modelName.lastIndexOf("/"));
        List<String> modelNames = Lists.newArrayList();
        modelNames.add("accounts");
        modelNames.add("doc");
        return modelNames.contains(modelName.toLowerCase());
    }

    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 检查是否可以放过的请求地址
        UriInfo uriInfo = requestContext.getUriInfo();
        String uri = uriInfo.getPath();
        boolean isPermitted = this.allowUrI(uri);
        if (!isPermitted) {
            //　检查是否登录
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                throw new AuthenticationException();
            }
            //检查当前用户是否有权限
            String method = requestContext.getMethod();
            switch (method.toUpperCase()) {
                case "POST":
                    isPermitted = this.post(uri, subject);
                    break;
                case "GET":
                    isPermitted = this.get(uri, subject);
                    break;
                case "DELETE":
                    isPermitted = this.delete(uri, subject);
                    break;
                default:
                    isPermitted = false;
            }
            if (!isPermitted) {
                throw new UnauthorizedException();
            }
        }
    }

}
