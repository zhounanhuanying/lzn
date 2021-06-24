package com.bfdb.controller;

import com.bfdb.config.BaseController;
import com.bfdb.entity.Datas;
import com.bfdb.entity.Role;
import com.bfdb.entity.RolePermission;
import com.bfdb.entity.UserRole;
import com.bfdb.service.RolePermissionService;
import com.bfdb.service.RoleService;
import com.bfdb.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户角色
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 添加角色的同时
     * 给角色分配权限
     *
     * @param roleName
     * @param description
     * @param ds
     * @return
     */
    @RequestMapping(value = "/insertRole", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertRole(String roleName, String description, Datas ds) {
        boolean b = false;
        Map<String, Object> maps = new HashMap<>();
        if (description.length() > 500) {
            maps.put( "code", -1 );
            return maps;
        }
        Integer statusNub = 0;
        String statusMessage = "success";
        if (!roleName.substring( 0, 1 ).equals( " " ) && !roleName.equals( null ) && !ds.getWebpermissionId().isEmpty() || !ds.getDatapermissionId().isEmpty()) {
//        if(!roleName.substring(0,1).equals(" ") && !roleName.equals(null) && !ds.getIds().isEmpty()){
            Role role = roleService.getRoleByRoleName( roleName );
            if (role == null) {
                b = roleService.insertRole( roleName, description );
            } else {
                fail();
            }
        }

        if (b) {
            Role newRole = roleService.getRoleByRoleName( roleName );
            Map<String, Object> paramMap = null;

            //判断web权限不为空时
            if (!ds.getWebpermissionId().isEmpty()) {
                paramMap = new HashMap<String, Object>();
                paramMap.put( "roleId", newRole.getRoleId() );
                paramMap.put( "permissionIds", ds.getWebpermissionId() );
                paramMap.put( "webDataPermission", 1 );
                rolePermissionService.insertRolePermissions( paramMap );
                maps.put( "code", statusNub );
                maps.put( "msg", statusMessage );
                //判断数据权限不为空时
            }
            if (!ds.getDatapermissionId().isEmpty()) {
                paramMap = new HashMap<String, Object>();
                paramMap.put( "roleId", newRole.getRoleId() );
                paramMap.put( "permissionIds", ds.getDatapermissionId() );
                paramMap.put( "webDataPermission", 0 );
                rolePermissionService.insertRolePermissions( paramMap );
                maps.put( "code", statusNub );
                maps.put( "msg", statusMessage );
            }

        } else {
            fail();
        }
        return maps;
    }


    /**
     * 给角色分配权限
     *
     * @param roleId
     * @param ds
     * @return
     */
    @RequestMapping("/doAssign")
    @ResponseBody
    public Object doAssign(Integer roleId, Datas ds) {
        start();
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put( "roleId", roleId );
            paramMap.put( "permissionIds", ds.getIds() );
            rolePermissionService.insertRolePermissions( paramMap );
            success();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return end();
    }

    /**
     * 查询所有角色数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectAllRole", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selectAllRole(HttpServletRequest request) {
        Map<String, Object> maps = new HashMap<>();
        Integer statusNub = 0;
        String statusMessage = "success";
        Integer limit = Integer.parseInt( request.getParameter( "limit" ) );
        Integer page = (Integer.parseInt( request.getParameter( "page" ) ) - 1) * limit;
        int totalCount = roleService.dataCount();
        List<Role> roles = roleService.selectAllRole( page, limit );
        maps.put( "code", statusNub );
        maps.put( "msg", statusMessage );
        maps.put( "count", totalCount );
        maps.put( "data", roles );

        return maps;
    }

    /**
     * 查询所有角色--树
     *
     * @return
     */
    @RequestMapping(value = "/selectAllRolesTree", method = RequestMethod.POST)
    @ResponseBody
    public Object selectAllRolesTree() {
        List<Role> roots = new ArrayList<Role>();
        // 查询所有的角色
        List<Role> roles = roleService.selectAllRole2();
        Map<Integer, Role> roleMap = new HashMap<Integer, Role>();
        for (Role role : roles) {
            roleMap.put( role.getRoleId(), role );
        }
        for (Role role : roles) {
            Role child = role;
            if (child.getPid() == 0) {
                roots.add( role );
            } else {
                Role parent = roleMap.get( child.getPid() );
                parent.getChildren().add( child );
            }
        }
        return roots;
    }

    /**
     * 查询当前用户所拥有的角色
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectAllRolesTreeByuserId", method = RequestMethod.POST)
    @ResponseBody
    public Object selectAllRolesTreeByuserId(Integer userId) {
        List<Role> roots = new ArrayList<Role>();
        // 查询所有的角色数据
        List<Role> roles = roleService.selectAllRole2();

        // 查询当前用户已经分配的角色信息
        List<UserRole> roleIds = userRoleService.queryRoleIdsByUserId( userId );

        Map<Integer, Role> roleMap = new HashMap<Integer, Role>();
        for (UserRole roleId : roleIds) {
            for (Role role : roles) {
                if (roleId.getRoleId() == (role.getRoleId())) {
                    role.setChecked( true );
                    roleMap.put( role.getRoleId(), role );
                }
            }
        }
        for (Role role : roles) {
            Role child = role;
            if (child.getPid() == 0) {
                roots.add( role );
            } else {
                Role parent = roleMap.get( child.getPid() );
                parent.getChildren().add( child );
            }
        }
        return roots;
    }

    /**
     * 删除角色的同时删除对应的权限
     *
     * @param roleIds
     * @return
     */
    @RequestMapping(value = "/deleteRoleAndPermissionByRoleId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteRoleAndPermissionByRoleId(int[] roleIds) {
        Integer statusNub = 0;
        String statusMessage = "success";
        int i = 0;
        Map<String, Object> map = new HashMap<>();
        for (int roleId : roleIds) {
            List<UserRole> userRoleList = userRoleService.selectByPrimaryKey( roleId );
            if (userRoleList.isEmpty()) {
                i = roleService.deleteByPrimaryKey( roleId );
            }
            if (i > 0) {
                rolePermissionService.deleteByPrimaryKey( roleId );
                map.put( "code", statusNub );
                map.put( "msg", statusMessage );
            } else {
                fail();
            }
        }

        return map;
    }

    /**
     * 更新角色信息的同时更新权限
     *
     * @param roleId
     * @param roleName
     * @param description
     * @param ds
     * @return
     */
    @RequestMapping(value = "/updateByPrimaryRoleSelective", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateByPrimaryRoleSelective(Integer roleId, String roleName, String description, Datas ds) {
        Integer statusNub = 0;
        String statusMessage = "success";
        Role role = new Role();
        role.setRoleId( roleId );
        role.setRoleName( roleName );
        role.setDescription( description );
        Map<String, Object> map = new HashMap<>();
        int i = roleService.updateByPrimaryKeySelective( role );
        if (i > 0) {
            //根据角色id删除角色信息
            rolePermissionService.deleteByPrimaryKey( roleId );
            //查询根据角色名称查询角色信息
            Role newRole = roleService.getRoleByRoleName( roleName );
            Map<String, Object> paramMap = null;
            //判断web权限不为空时
            if (!ds.getWebpermissionId().isEmpty()) {
                paramMap = new HashMap<String, Object>();
                paramMap.put( "roleId", newRole.getRoleId() );
                paramMap.put( "permissionIds", ds.getWebpermissionId() );
                paramMap.put( "webDataPermission", 1 );
                rolePermissionService.insertRolePermissions( paramMap );
                map.put( "code", statusNub );
                map.put( "msg", statusMessage );
                //判断数据权限不为空时
            }
            if (!ds.getDatapermissionId().isEmpty()) {
                paramMap = new HashMap<String, Object>();
                paramMap.put( "roleId", newRole.getRoleId() );
                paramMap.put( "permissionIds", ds.getDatapermissionId() );
                paramMap.put( "webDataPermission", 0 );
                rolePermissionService.insertRolePermissions( paramMap );
                map.put( "code", statusNub );
                map.put( "msg", statusMessage );
            }
        } else {
            fail();
        }
        return map;
    }

    /**
     * 根据roleId查询所有
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/selectAllByPrimaryKey", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectAllByPrimaryKey(Integer roleId) {

        Map<String, Object> maps = new HashMap<>();
        Integer statusNub = 0;
        String statusMessage = "success";
        List<RolePermission> stringObjectMap = rolePermissionService.selectAllByPrimaryKey( roleId );
        maps.put( "code", statusNub );
        maps.put( "msg", statusMessage );
        maps.put( "stringObjectMap", stringObjectMap );

        return maps;

    }


}
