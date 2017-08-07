package org.cmdb.security.listener;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.util.Collection;

/**
 * Created by X on 2017/4/17.
 */
public class ExpirationSessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
        System.out.println("会话创建：" + session.getId());
    }

    @Override
    public void onStop(Session session) {
        System.out.println("会话停止：" + session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        Collection<Realm> realms = ((RealmSecurityManager) SecurityUtils.getSecurityManager())
                .getRealms();
        realms.stream().filter(realm -> realm instanceof AuthenticatingRealm).forEach(realm -> {
            PrincipalCollection principals = (PrincipalCollection) session.getAttribute(
                    DefaultSubjectContext.class.getName()
                            + "_PRINCIPALS_SESSION_KEY");
            if (principals != null)
                ((AuthenticatingRealm) realm).onLogout(principals);
        });
    }
}
