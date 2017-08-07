package org.cmdb.security.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by X on 2017/3/17.
 * 密码重试次数限制
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    /**
     * 密码重试次数
     */
    private static final int RETRYCOUNTNUMBER = 5;

    /**
     * 密码重试缓存
     */
    private Cache<String, AtomicInteger> passwordRetryCache;

    /**
     * 获取密码重试次数缓存对象
     *
     * @param cacheManager 缓存管理
     */
    public RetryLimitHashedCredentialsMatcher(final CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(final AuthenticationToken token, final AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > RETRYCOUNTNUMBER) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }
        String tokenPassword = new String((char[]) token.getCredentials());
        String infoPassword = info.getCredentials().toString();
        boolean matches = false;
        if (tokenPassword.equals(infoPassword)) {
            matches = true;
        }
        if (matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
