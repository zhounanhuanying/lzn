package com.bfdb.controller;

import com.bfdb.config.BaseController;
import com.bfdb.entity.*;
import com.bfdb.service.LoginService;
import com.bfdb.service.RolePermissionService;
import com.bfdb.service.UserRoleService;
import com.bfdb.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private LoginService loginService;

//    @RequestMapping("/login")
//    public String userLogin(String userName, String userPassword, HttpServletRequest request) {
//        //创建subject对象
//        Subject subject = SecurityUtils.getSubject();
//        //新建token令牌，将令牌传到myrelam
//        subject.login( new UsernamePasswordToken( userName, userPassword ) );
//        boolean authenticated = subject.isAuthenticated();//是否认证成功
//        if (authenticated) {//是否认证成功
//            User user = (User) subject.getPrincipal();
//            //request.getSession().setAttribute("user", user);
//            subject.getSession().setAttribute( "user", user );//将用户信息存入session域中
//            return "home/index";
//        }
//        return "login";
//    }
//
    /**
     * 根据userId查询相关权限
     */
    @RequestMapping(value = "/selectPermissionByUserIdIndex", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectPermissionByUserIdIndex(Integer userId) {
        Map<String, Object> maps = new HashMap<>();
        maps.put( "code" ,1);
        Integer userid = Integer.parseInt( "123456789" );//管理员id
        //升序排序比较函数
        Comparator<Permission> comparator = new Comparator<Permission>() {
            public int compare(Permission p1, Permission p2) {
                if (p1.getPermissionType() < p2.getPermissionType()) {
                    return -1;
                } else if (p1.getPermissionType() > p2.getPermissionType()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        if (userid.equals( userId )) {
            List<Permission> permissions = loginService.findPermissionAll();
            if (permissions != null) {
                //升序排序
                Collections.sort( permissions, comparator );
                maps.put( "permissions", permissions );
            } else {
                fail();
            }
        } else {
            List<Permission> permissions = rolePermissionService.selectPermissionByUserId( userId ).stream() .collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(p->p.getPermissionId()))), ArrayList::new));
            if (permissions != null) {
                //升序排序
                Collections.sort( permissions, comparator );
                maps.put( "permissions", permissions );
                maps.put( "code" ,0);
            } else {
                fail();
            }
        }
        return maps;
    }

    /**
     * 根据userId查询相关权限
     */
//    @RequestMapping("/selectPermissionByUserId")
//    @ResponseBody
//    public Map<String, Object> selectPermissionByUserId(Integer userId) {
//        List<Permission> permissionList = null;
//
//        Integer userid = Integer.parseInt( "123456789" );//管理员id
//        if (userid.equals( userId )) {
//            permissionList = loginService.findPermissionAll();
//
//        } else {
//            permissionList = rolePermissionService.selectPermissionByUserId( userId );
//
//        }
//        return fun( permissionList );
//    }
//
//
//    private Map<String, Object> fun(List<Permission> permissionList) {
//        Map<String, Object> maps = new HashMap<>();
//        Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();
//        for (Permission deptEmtity : permissionList) {
//            permissionMap.put( deptEmtity.getPermissionId(), deptEmtity );
//        }
//        List<Permission> rootsList = new ArrayList<Permission>();
//
//        for (Permission deptEmtity : permissionList) {
//            if (deptEmtity.getPid() == 0) {
//                rootsList.add( deptEmtity );
//            } else {
//                Permission parent = permissionMap.get( deptEmtity.getPid() );
//                parent.getChildren().add( deptEmtity );
//            }
//        }
//        maps.put("permissionList"  ,rootsList);
//        return maps;
//    }

    /**
     *  添加用户
     *同时添加角色
     */
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> insertUser(String userName,String password,Integer status,Datas ds){
        Map<String,Object> maps =new HashMap<>();
        int i = 0;
        String salt ="abcd";
        int hashIterations =1;
        Md5Hash md5 = new Md5Hash(password,salt,hashIterations);
        Integer statusNub=0;
        String statusMessage="success";
        User user = new User();
        user.setUserName(userName);
        user.setPassword(md5.toString());
//        user.setStatus(status);
        User newUser =  userService.findByName(userName);
        if(newUser==null){
            if(!userName.substring(0,1).equals(" ") && !userName.equals(null) && !ds.getIds().isEmpty()) {
                i = userService.insertSelective(user);
            }
        }
        if(i>0){
            User user1 = userService.findByName(userName);
         /* userRoleService.insertUserRole(user1.getUserId(), roleId);*/
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("userId", user1.getUserId());
            paramMap.put("roleIds", ds.getIds());
            userRoleService.insertUserRoles(paramMap);
        }else{
            maps.put("code",statusNub);
            maps.put("msg",statusMessage);
            fail();
        }
        return maps;
    }

    /**
     *给用户分配角色
     */
    @RequestMapping(value = "/doAssign", method = RequestMethod.POST)
    @ResponseBody
    public Object doAssign(Integer userId, Datas ds) {
        start();
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("userId", userId);
            paramMap.put("roles", ds.getIds());
            userRoleService.insertUserRoles(paramMap);
            success();
        } catch ( Exception e ) {
            e.printStackTrace();
            fail();
        }
        return end();
    }



    /**
     *  查询所有用户
     *
     */
    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> dataAll(HttpServletRequest request){
        Map<String,Object> maps =new HashMap<>();
        Integer statusNub=0;
        String statusMessage="success";
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        Integer page = (Integer.parseInt(request.getParameter("page"))-1)*limit;
        User user = (User)request.getSession().getAttribute("user");
        int totalCount = userService.dataCount(user.getUserId() == 123456789?null:user.getUserId());
        List<User> users = userService.selectAllUser(page,limit,user.getUserId() == 123456789?null:user.getUserId());
        maps.put("code",statusNub);
        maps.put("msg",statusMessage);
        maps.put("count",totalCount);
        maps.put("data",users);

        return maps;
    }

    /**
     *  删除用户的同时删除角色
     */
    @RequestMapping(value = "/deleteByPrimaryKey", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteByPrimaryKey(Integer[] userId){
        Integer statusNub=0;
        String statusMessage="success";
        Map<String,Object> map =new HashMap<>();
        for (Integer integer : userId) {
            int i = userService.deleteByPrimaryKey(integer.intValue());
            if(i>0){
                userRoleService.deleteByPrimaryKey(integer.intValue());
                map.put("code",statusNub);
                map.put("msg",statusMessage);
            }else{
                fail();
            }
        }

        return map;
    }

    /**
     *  更新用户信息的同时更新角色
     */
    @RequestMapping(value = "/updateByPrimaryKeySelective", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateByPrimaryKeySelective(Integer userId, Integer status,String userName, String password, Datas ds){
        Integer statusNub=0;
        String statusMessage="success";
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        String salt ="abcd";
        int hashIterations =1;
        if(password.length()<32){
            Md5Hash md5 = new Md5Hash(password,salt,hashIterations);
            user.setPassword(md5.toString());
        }else {
            user.setPassword(password);
        }
        //Md5Hash md5 = new Md5Hash(password,salt,hashIterations);
        //user.setPassword(md5.toString());
//        user.setStatus(status);
        Map<String,Object> map =new HashMap<>();
        int i =0;
        if(!userName.equals("") && !userName.equals(null) && !ds.getIds().isEmpty()){
            i = userService.updateByPrimaryKeySelective(user);
        }
        if(i>0){
            User user1 = userService.findByName(userName);
            userRoleService.deleteByPrimaryKey(userId);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("userId", user1.getUserId());
            paramMap.put("roleIds", ds.getIds());
            userRoleService.insertUserRoles(paramMap);
            map.put("code",statusNub);
            map.put("msg",statusMessage);
        }else{
            fail();
        }
        return map;
    }


    /**
     *  根据userId查询所有
     */
    @RequestMapping("/selectAllByPrimaryKey")
    @ResponseBody
    public Map<String,Object> selectAllByPrimaryKey(Integer userId){

        Map<String,Object> maps =new HashMap<>();
        Integer statusNub=0;
        String statusMessage="success";
        List<UserRole> userRoleList = userRoleService.selectAllByPrimaryKey(userId);

        maps.put("code",statusNub);
        maps.put("msg",statusMessage);
        maps.put("stringObjectMap",userRoleList);

        return maps;

    }

}
