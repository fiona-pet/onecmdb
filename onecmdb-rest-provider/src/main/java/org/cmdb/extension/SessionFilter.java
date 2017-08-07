package org.cmdb.extension;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ThreadContext;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.UriInfo;

/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/5/5
 * Time: 9:50
 * 用户cookie登陆
 */
@Priority(Priorities.USER)
public class SessionFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        UriInfo uriInfo = requestContext.getUriInfo();
        String uri = uriInfo.getPath();

        boolean flag = false;
        if (uri.contains("sign-in") || uri.contains("sign-out")|| uri.endsWith("doc/swagger")) {
            flag = true;
        }

        Cookie cookie = requestContext.getCookies().get("sid");

        if (!flag && cookie != null) {
            DefaultSubjectContext defaultSubjectContext = new DefaultSubjectContext();
            defaultSubjectContext.setSessionId(cookie.getValue());
            Subject subject = SecurityUtils.getSecurityManager().createSubject(defaultSubjectContext);
            ThreadContext.bind(subject);
            flag = subject.isAuthenticated();
        }
        if (!flag) {
            throw new AuthenticationException();
        }
    }
}
