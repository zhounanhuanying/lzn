package com.bfdb.shirorelam;
import com.bfdb.entity.Permission;
import com.bfdb.entity.User;
import com.bfdb.service.LoginService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private LoginService loginService;

    /**
     * shiro授权方法
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //定义用于存放权限的 list
        List<String> perms = new ArrayList<String>();
        //定义存放用户数据库权限的list
        List<Permission> permissions=new ArrayList<>();
        //获取当前认证的用户信息
        User user = (User) principals.getPrimaryPrincipal();
        if(user.getUserName().equals("Administrator")){
            permissions = loginService.findPermissionAll();
            for (Permission permission : permissions) {
                System.out.println(permission);
            }//获得权限表所有权限
            System.out.println(permissions);
        }else {
            //根据用户信息查询所具有的权限列表
            //permissions = userService.findPermissionByUserId(user.getUserId());//根据id获得对应权限
            Permission permission=new Permission();
             permission.setPermissionCode("test:page");
             permissions.add(permission);
        }
        if (permissions != null && permissions.size() > 0) {
            for (Permission perm : permissions) {
                String permCode = perm.getPermissionCode();
                if (permCode != null && !"".equals(permCode)) {
                    perms.add(permCode);
                }
            }
        }
        //查询权限数据，返回 AuthorizationInfo 对象
        SimpleAuthorizationInfo AuthorizationInfo = new SimpleAuthorizationInfo();
        //将权限数据添加到对象
        AuthorizationInfo.addStringPermissions(perms);
        return AuthorizationInfo;
    }

    /**
     * shiro认证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从 token 中获取用户身份信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        //数据库密码
        User user=new User();
        if("Administrator".equals(username)){
            String daoPassword="d30ede312968fca1a5a1f93df99ed6f9";
            user.setUserName("Administrator");
            user.setUserId(123456789);//在项目左边动态列表处用到
            user.setPassword(daoPassword);
        }else{
            //根据用户名从数据库中获取密码和盐值信息（暂时不用获取盐值）
            user = loginService.getUserByName(username);
            //如果查询不到返回 null
            // System.out.println(user);
            if(user == null){
                return null;
            }
        }
        //自定义盐值，而不从数据库获取
        String salt="abcd";
        //如果查询到结果信息，则返回 authenticationInfo 对象，封装用户，密码，自定义realm 名称
        SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(user, user.getPassword(),//数据库密码，登录输入的密码在shiro内部
                        ByteSource.Util.bytes(salt), getName());
        return simpleAuthenticationInfo;
    }
}
