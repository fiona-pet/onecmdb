package org.cmdb.config;

import org.cmdb.security.credentials.RetryLimitHashedCredentialsMatcher;
import org.cmdb.security.listener.ExpirationSessionListener;
import org.cmdb.security.realm.SmartRealm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 */
@Configuration
public class ShiroConfiguration {

    @Bean(name = "cacheManager")
    public EhCacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:META-INF/spring/ehcache.xml");
        return cacheManager;
    }

    @Bean(name = "credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher credentialsMatcher(EhCacheManager ehCacheManager) {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(ehCacheManager);
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }


    @Bean(name = "smartRealm")
    public SmartRealm smartRealm(RetryLimitHashedCredentialsMatcher credentialsMatcher) {
        SmartRealm smartRealm = new SmartRealm();
        smartRealm.setCredentialsMatcher(credentialsMatcher);
        smartRealm.setCachingEnabled(true);
        smartRealm.setAuthenticationCachingEnabled(true);
        smartRealm.setAuthenticationCacheName("authenticationCache");
        smartRealm.setAuthorizationCachingEnabled(true);
        smartRealm.setAuthorizationCacheName("authorizationCache");
        return smartRealm;
    }

    @Bean(name = "sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean(name = "sessionIdGenerator")
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean(name = "sessionValidationScheduler")
    public QuartzSessionValidationScheduler sessionValidationScheduler(DefaultSessionManager sessionManager) {
        QuartzSessionValidationScheduler quartzSessionValidationScheduler = new QuartzSessionValidationScheduler();
        quartzSessionValidationScheduler.setSessionValidationInterval(1800000);
        quartzSessionValidationScheduler.setSessionManager(sessionManager);
        return quartzSessionValidationScheduler;
    }

    @Bean(name = "sessionDAO")
    public EnterpriseCacheSessionDAO sessionDAO(JavaUuidSessionIdGenerator sessionIdGenerator) {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        return sessionDAO;
    }

    @Bean(name = "expirationSessionListener")
    public ExpirationSessionListener expirationSessionListener() {
        return new ExpirationSessionListener();
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager(SessionDAO sessionDAO, ExpirationSessionListener expirationSessionListener, SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager defaultSessionManager = new DefaultWebSessionManager();
        defaultSessionManager.setGlobalSessionTimeout(1800000);
        defaultSessionManager.setDeleteInvalidSessions(true);
        defaultSessionManager.setSessionValidationSchedulerEnabled(true);
//        defaultSessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        defaultSessionManager.setSessionDAO(sessionDAO);
        defaultSessionManager.setSessionListeners(ImmutableList.of(expirationSessionListener));
        defaultSessionManager.setSessionIdCookie(sessionIdCookie);
        return defaultSessionManager;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(DefaultSessionManager sessionManager, EhCacheManager cacheManager, SmartRealm smartRealm) {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setSessionManager(sessionManager);
        defaultSecurityManager.setCacheManager(cacheManager);
        defaultSecurityManager.setRealm(smartRealm);
        return defaultSecurityManager;
    }

    @Bean(name = "methodInvokingFactoryBean")
    public MethodInvokingFactoryBean methodInvokingFactoryBean(DefaultSecurityManager securityManager) {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(new Object[]{securityManager});
        return methodInvokingFactoryBean;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("index.html");
        shiroFilterFactoryBean.setSuccessUrl("index.html");
        Map<String, String> filterChainDefinitionMap = Maps.newHashMap();
        filterChainDefinitionMap.put("/**", "user");
        filterChainDefinitionMap.put("/api/accounts/**", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/doc/**", "anon");
        filterChainDefinitionMap.put("/api/doc/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
