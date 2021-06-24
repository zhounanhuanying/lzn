package com.bfdb.controller;

import com.alibaba.fastjson.JSONObject;
import com.bfdb.config.Constant;
import com.bfdb.entity.User;
import com.bfdb.entity.vo.LoginVo;
import com.bfdb.service.UserService;
import com.bfdb.untils.JwtUtil;
import com.bfdb.untils.Operation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据登陆用户生成token
 * “ignoreResourceNotFound = true” 表示当找不到这个配置文件时，则跳过
 */
@Controller
@PropertySource(value = {"classpath:token.properties"}, ignoreResourceNotFound = true)
public class LoginController extends AbstractController {

    //配置token的有效时间
    @Value("${token.validity.time}")
    private String tokenValidityTime;


    @Autowired
    private UserService userService;

    /**
     * 根据登陆用户生成token
     *
     * @param request
     * @param response
//     * @param userName
//     * @param password
     * @return
     * @throws Exception
     */
    @Operation(name = "登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Object login(HttpServletRequest request, HttpServletResponse response,@RequestBody LoginVo loginVo) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        JSONObject jsonObject = new JSONObject();
        String token = null;
        jsonObject.put( "code", 0 );
        //创建subject对象
        Subject subject = SecurityUtils.getSubject();
        try {
            //身份验证是否成功
            subject.login( new UsernamePasswordToken( loginVo.getUserName().trim(), loginVo.getPassword()));
            boolean authenticated = subject.isAuthenticated();//是否认证成功
            if (authenticated) {
                User user = (User) subject.getPrincipal();
                //返回token
                token = JwtUtil.createJWT( Constant.JWT_ID, loginVo.getUserName().trim() );
//                token = JwtUtil.createJWT( Constant.JWT_ID,userName, Long.valueOf( tokenValidityTime ) );
                if (token != null) {
                    jsonObject.put( "code", Constant.RESPONSE_SUCCESS_CODE );
                    jsonObject.put( "access_token", token );
                    jsonObject.put( "responseDescription",Constant.RESPONSE_SUCCESS_DESCRIPTION );
                    //token信息保存到数据库
                    // 生成JWT的时间
                    long nowMillis = System.currentTimeMillis();
                    //token有效时间
                    long ttlMillis = Long.valueOf( tokenValidityTime );
                    long expMillis =0;
                    if (ttlMillis>= 0) {
                        expMillis = nowMillis + ttlMillis;
                        Date exp = new Date( expMillis );
                        System.out.println( "令牌过期时间：" + sdf.format( exp ) );
                    }
                    //根据用户名字获取用户信息
                    User user1 = userService.selectByName( user.getUserName() );
                    user1.setUserToken( token );
                    user1.setTokenTime( String.valueOf(expMillis));
                    //更新添加用户token和过期时间信息
                    userService.updateByPrimaryKeySelective( user1 );
                    SecurityUtils.getSubject().getSession().setTimeout( -1000l );//设置session时长
                    subject.getSession().setAttribute( "user", user1 );//将用户信息存入session域中
                    jsonObject.put("JSESSIONID", subject.getSession().getId());
                    return jsonObject;

                }
            }
        } catch (Exception ex) {
            jsonObject.put( "code", Constant.RESPONSE_FAIL_CODE );
            jsonObject.put( "msg","用户不存在或者密码不正确");
            logger.error( "用户验证生成token失败！" + ex.getMessage() );
        }
        return jsonObject;
    }

    /**
     * 用户退出
     *
     * @param session
     * @return
     */
    @Operation(name = "退出")
    @RequestMapping("/logout")
    public ModelAndView loginOut(HttpSession session) {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        view.setViewName( "login" );
        return view;
    }


//
//    /**
//     * 根据登陆用户生成token
//     *登录页面
//     * 根据用户名和密码进行查询
//     * @param request
//     * @param response
//     * @param userName
//     * @param password
//     * @return
//     * @throws Exception
//     */
//    @Operation(name="登录")
//    @RequestMapping(value = "/loginToken", method = RequestMethod.POST)
//    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response,
//                                     String userName, String password) throws Exception {
//        Map<String, Object> resultMap = new HashMap<>();
//        User user1 = userService.getUserByName(userName);
//        if(user1!=null){//判断用户是否存在
//            resultMap.put("code", -1);
//            return resultMap;
//        }
//        String token = null;
//        //创建subject对象
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            //身份验证是否成功
//            subject.login(new UsernamePasswordToken(userName.trim(), password));
//            boolean authenticated = subject.isAuthenticated();//是否认证成功
//            if (authenticated) {
//                //返回token
//                token = JwtUtil.createJWT(Constant.JWT_ID, "Anson", userName, Long.valueOf(tokenValidityTime));
//                if (token != null) {
//                    User user = (User) subject.getPrincipal();
//                    resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
//                    resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
//                    resultMap.put("token", token);
//                    subject.getSession().setAttribute("user", user);//将用户信息存入session域中
//                    return "home/index";
//                }
//            }
//        } catch (Exception ex) {
//            resultMap.put("code", Constant.RESPONSE_FAIL_CODE);
//            logger.error("用户验证生成token失败！" + ex.getMessage());
//        }
//        return resultMap;
//    }
//
//

}
