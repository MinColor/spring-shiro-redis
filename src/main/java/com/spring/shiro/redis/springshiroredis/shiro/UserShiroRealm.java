package com.spring.shiro.redis.springshiroredis.shiro;

import com.spring.shiro.redis.springshiroredis.model.User;
import com.spring.shiro.redis.springshiroredis.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Classname UserShiroRealm
 * @Description TODO
 * @Date 2019/7/13 9:17
 * @Created by 颜小能
 */
public class UserShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 角色权限和对应权限添加
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.err.println("加载角色与菜单。。。。。。。。。。。。。。。。。");
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("-----------*************************------------>"+ primaryPrincipal);
        List<String> roles = new ArrayList<String>();
        List<String> permissions = new ArrayList<String>();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

       /* User user = userService.findByUserName(primaryPrincipal);
        if(user != null){
            for (Role role : user.getRoles()) {
                roles.add(role.getName());
                for (Permission p : role.getPermissions()) {
                    permissions.add(p.getPrivilege());
                }
            }
        }else{
            throw new AuthorizationException();
        }*/
        permissions.add("user:test1");
        permissions.add("user:test2");
        //给当前用户设置角色
        info.addRoles(roles);
        //给当前用户设置权限
        info.addStringPermissions(permissions);

        return info;
    }

    /**
     * 用户认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }

        String userName = authenticationToken.getPrincipal().toString();

        //只允许同一账户单个登录
        Subject subject = SecurityUtils.getSubject();
        Session nowSession = subject.getSession();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        if(sessions != null && sessions.size() > 0) {
            for (Session session : sessions) {
                if (!nowSession.getId().equals(session.getId()) && (session.getTimeout() == 0
                        || userName.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY))))) {
                    sessionDAO.delete(session);
                }
            }
        }

        User user = userService.findOneByUserName(userName);
        if (user == null) {
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
        }
    }
}
