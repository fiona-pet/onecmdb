package org.cmdb.security.realm;

import org.cmdb.dto.ListFilter;
import org.cmdb.dto.SearchFilter;
import org.cmdb.entity.User;
import org.cmdb.service.UserService;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by X on 2017/3/17.
 * smart
 */
public class SmartRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

//    @Autowired
//    private RoleUserRelService roleUserRelService;
//
//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private RoleFunctionRelService roleFunctionRelService;
//
//    @Autowired
//    private FunctionService functionService;

    /**
     * 查询用户
     *
     * @param loginName 登录名
     * @return 用户
     */
    private User getUser(String loginName) {
        return userService.getUserByLoginName(loginName);
    }

    /**
     * 用户角色关系
     *
     * @param user 用户
     * @return 关系
     */
//    private List<RoleUserRel> getRoleUserRel(User user) {
//        SearchFilter searchFilter = new SearchFilter();
//        searchFilter.setFieldName("userId");
//        searchFilter.setOperator(SearchFilter.Operator.EQ.name());
//        searchFilter.setValue(user.getId());
//        ListFilter listFilter = new ListFilter();
//        listFilter.addFilters(searchFilter);
//        return roleUserRelService.list(listFilter);
//    }

    /**
     * 获取角色
     *
     * @param roleUserRels 角色用户关系
     * @return 角色
     */
//    private List<Role> getRole(List<RoleUserRel> roleUserRels) {
//        List<Role> roles = Lists.newArrayList();
//        if (roleUserRels != null && !roleUserRels.isEmpty()) {
//            for (RoleUserRel roleUserRel : roleUserRels) {
//                roles.add(roleService.detail(roleUserRel.getRoleId()));
//            }
//        }
//        return roles;
//    }

//    private List<RoleFunctionRel> getRoleFunctionRel(List<Role> roles) {
//        List<RoleFunctionRel> roleFunctionRels = Lists.newArrayList();
//        if (roles != null && !roles.isEmpty()) {
//            for (Role role : roles) {
//                SearchFilter searchFilter = new SearchFilter();
//                searchFilter.setFieldName("roleId");
//                searchFilter.setOperator(SearchFilter.Operator.EQ.name());
//                searchFilter.setValue(role.getId());
//                ListFilter listFilter = new ListFilter();
//                listFilter.addFilters(searchFilter);
//                roleFunctionRels.addAll(roleFunctionRelService.list(listFilter));
//            }
//        }
//        return roleFunctionRels;
//    }

    /**
     * 获取菜单
     *
     * @param roleFunctionRels 角色菜单关系
     * @return 菜单
     */
//    private List<Function> getFunction(List<RoleFunctionRel> roleFunctionRels) {
//        List<Function> functions = Lists.newArrayList();
//        if (roleFunctionRels != null && !roleFunctionRels.isEmpty()) {
//            for (RoleFunctionRel roleFunctionRel : roleFunctionRels) {
//                functions.add(functionService.detail(roleFunctionRel.getFunctionId()));
//            }
//        }
//        return functions;
//    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        String username = (String) principals.getPrimaryPrincipal();
//        User user = getUser(username);
//        List<RoleUserRel> roleUserRels = getRoleUserRel(user);
//        List<Role> roles = getRole(roleUserRels);
//        for (Role role : roles) {
//            authorizationInfo.addRole(role.getDisplayName());
//        }
//        List<RoleFunctionRel> roleFunctionRels = getRoleFunctionRel(roles);
//        List<Function> functions = getFunction(roleFunctionRels);
//        Map<String, Function> functionMap = Maps.newHashMap();
//        for (Function function : functions) {
//            functionMap.put(function.getId(), function);
//        }
//        for (RoleFunctionRel roleFunctionRel : roleFunctionRels) {
//            Function function = functionMap.get(roleFunctionRel.getFunctionId());
//            String moduleName = function.getName();
//            StringBuffer permissionStr = new StringBuffer();
//            permissionStr.append("+");
//            permissionStr.append(moduleName);
//            permissionStr.append("+");
//            permissionStr.append(roleFunctionRel.getPermissions());
//            authorizationInfo.addObjectPermission(new BitPermission(permissionStr.toString()));
//            if(null != function.getCode()) {
//                Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
//                List<String> codes = splitter.splitToList(function.getCode());
//                for (String code : codes) {
//                    permissionStr = new StringBuffer();
//                    permissionStr.append("+");
//                    permissionStr.append(code);
//                    permissionStr.append("+");
//                    permissionStr.append(roleFunctionRel.getPermissions());
//                    authorizationInfo.addObjectPermission(new BitPermission(permissionStr.toString()));
//                }
//            }
//        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String) token.getPrincipal();

        User user = getUser(username);

        if (user == null) {
            throw new UnknownAccountException();//没找到帐号
        }

/*        if(Boolean.TRUE.equals(user.get)) {
            throw new LockedAccountException(); //帐号锁定
        }*/

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getLoginName(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes("smart"),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }


    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
