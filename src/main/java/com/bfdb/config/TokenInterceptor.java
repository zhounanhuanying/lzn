package com.bfdb.config;

import com.bfdb.entity.AnonymousAddress;
import com.bfdb.entity.Permission;
import com.bfdb.entity.PersonnelInterface;
import com.bfdb.entity.User;
import com.bfdb.mapper.AnonymousUrlMapper;
import com.bfdb.service.PermissionService;
import com.bfdb.service.PersonnelInterfaceService;
import com.bfdb.service.UserService;
import com.bfdb.untils.JwtUtil;
import com.bfdb.untils.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 自定义token接口拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    protected Logger logger = LoggerFactory.getLogger( TokenInterceptor.class );

    @Autowired
    AnonymousUrlMapper anonymousUrlMapper;

    @Autowired
    PermissionService permissionService;

    @Autowired
    PersonnelInterfaceService personnelInterfaceService;

    @Autowired
    TokenTime tokenValidityTime;

    @Autowired
    private UserService userService;

    //30秒
    private static final long tokenRefreshInterval = 30000;
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，如性能监控中我们可以在此记录结束时间并输
     * 出消耗时间，还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception {
    }

    /**
     * 该方法将在Controller的方法调用之后执行， 方法中可以对ModelAndView进行操作 ，
     * 该方法也只能在当前Interceptor的preHandle方法的返回值为true时才会执行。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView mv) throws Exception {
    }

    /**
     * preHandle方法是进行处理器拦截用的，该方法将在Controller处理之前进行调用，
     * 该方法的返回值为true拦截器才会继续往下执行，该方法的返回值为false的时候整个请求就结束了。
     * //在执行handler之前执行的
     * //用于用户认证校验、用户权限校验
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求的路径进行判断
        String servletPath = request.getServletPath();
//        System.out.println( servletPath );
        if("/login.jsp".contains(servletPath)){
            //重定向到登陆页面
            response.sendRedirect( request.getContextPath() + "/login" );
            return false;
        }
        //判断是否对外公开地址 ,可以匿名访问的URL
        List<AnonymousAddress> anonymousUrlList = anonymousUrlMapper.selectAnonymousUrlList();
        //判断是否对外公开地址 ,可以匿名访问的URL
        for (AnonymousAddress open_url : anonymousUrlList) {
            if (servletPath.contains( open_url.getAnonymousUrl())) {
                //如果是公开地址 则放行
                return true;
            }
        }

        //根据用id查询用户的相应权限
        List<Permission> permissionRoleList = null;

        //查询树形结构中公共的的页面路径
        List<Permission> permissionList = permissionService.selectByPermission();
        for (Permission permission : permissionList) {
            if (servletPath.equalsIgnoreCase( permission.getVisitorUrl() )) {
                //如果是公开，则放行
                return true;
            }
        }
        //获取token
        String token = request.getHeader( Constant.HEADER_AUTHORIZATION );
        //判断token是否为空
        String verificationResult = null;
        if (StringUtils.isNotBlank( token )) {
            //验证token，如验证失败返回失败信息
            verificationResult = JwtUtil.tokenVerification( token );
            if (Constant.ERROR_TOKEN.equals( verificationResult )) {//token数据错误
                dealErrorReturn( request, response, Constant.ERROR_TOKEN_CODE );
                return false;
            }
            //从token中获取用户信息
            String names = JwtUtil.parseJWTuserName( token );
            //根据用户查询用户信息
            User user = userService.selectByName( names );
            //此账号为超级管理员
            if ("Administrator".equals( user.getUserName() )) {
                return true;

            } else {
                //判断用户的token和时间
                if (StringUtils.isNotBlank( user.getUserToken() ) && StringUtils.isNotBlank( user.getTokenTime() )) {
                    //查询当前token和数据库的token是否一致
                    if (user.getUserToken().equals( token )) {
                        //当前时间
                        long nowMillis = System.currentTimeMillis();
                        //从数据库查出来的时间
                        long ttlMillis = Long.valueOf( user.getTokenTime() );
//                        Date datenowMilli11 = new Date( ttlMillis );
//                        System.out.println( "数据库token过期时间" + sdf.format( datenowMilli11 ) );
                        //如果当前时间不大数据库查出来的时间
                        //判断是否过期
                        if (nowMillis <= ttlMillis) {
                            // 过期时间向前推30秒
                            long nowMill = ttlMillis - tokenRefreshInterval;
                            //判断还有30秒过期，才可以对token过期时间，进行刷新
                            if (nowMill <= nowMillis) {
                                //当前时间加上token的过期时间
                                long newestTime = nowMillis + Long.valueOf( tokenValidityTime.getTokenValidityTime() );
                                Date datenowM = new Date( nowMill );
                                System.out.println( "转换过期时间" + sdf.format( datenowM ) );
                                user.setTokenTime( String.valueOf( newestTime ) );
                                //更新数据库
                                userService.updateByPrimaryKeySelective( user );
                            }
                            //获取人员接口管理信息
//                            List<Permission> permissionInterface = permissionService.selectPermissionInterface();
                            List<PersonnelInterface> permissionInterface = personnelInterfaceService.selectPersonnelInterfaceAll();
                            //循环遍历比较
                            for (PersonnelInterface permission : permissionInterface) {
                                if (servletPath.equalsIgnoreCase( permission.getInterfaceUrl() )) {
                                    //根据当前用户id以及请求url查询当前用户是否有接口管理的相关权限
                                    permissionRoleList = permissionService.selectByPermissionRole( user.getUserId(), servletPath );
                                    //判空
                                    if (ListUtils.isNotNullAndEmptyList( permissionRoleList )) {
                                        //如果不为空，说明有权限
                                        return true;
                                    } else {
                                        //此返回说明暂无权限
                                        dealErrorReturn( request, response, Constant.NONE_TOKEN_CODE );
                                        return false;
                                    }
                                }
                            }

                            return true;
                        } else {
                            //用这种方式解决token过期后跳转页面的问题
                            dealErrorReturn( request, response, Constant.OVERDUE_TOKEN_CODE );
                            return false;
                        }
                    } else {
                        //token不一致
                        dealErrorReturn( request, response, Constant.INCONSISTENT_TOKEN_CODE );
                        return false;
                    }
                }
            }
        } else {
            //重定向到登陆页面
            response.sendRedirect( request.getContextPath() + "/login" );
            return false;
        }
        //验证成功，返回true，调用接口方法
        return true;
    }

    /**
     * 返回错误信息给WEB
     *
     * @param httpServletRequest, httpServletResponse, obj
     * @return void
     * @methodName dealErrorReturn
     */
    public void dealErrorReturn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj) {
        String json = (String) obj;
        PrintWriter writer = null;
        httpServletResponse.setCharacterEncoding( "UTF-8" );
        httpServletResponse.setContentType( "application/json; charset=utf-8" );
        try {
            writer = httpServletResponse.getWriter();
            writer.print( json );
        } catch (IOException ex) {
            logger.error( "response error", ex );
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


}
