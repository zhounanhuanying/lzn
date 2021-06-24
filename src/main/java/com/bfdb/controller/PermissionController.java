package com.bfdb.controller;

import com.alibaba.fastjson.JSONObject;
import com.bfdb.entity.DataPermission;
import com.bfdb.entity.Permission;
import com.bfdb.entity.RolePermission;
import com.bfdb.service.DataPermissionService;
import com.bfdb.service.PermissionService;
import com.bfdb.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限树
 */
@Controller
@RequestMapping("permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;

    //数据权限
    @Autowired
    private DataPermissionService dataPermissionService;

    /**
     * 查询所有权限--树
     */
    @RequestMapping(value = "/selectAllPermissionTree", method = RequestMethod.POST)
    @ResponseBody
    public Object selectAllPermissionTree() {
        JSONObject jsonObject = new JSONObject();
        List<Permission> roots = new ArrayList<Permission>();
        //数据权限
        List<DataPermission> dataPermissionRoots = new ArrayList<DataPermission>();
        //根据标识查询web页面权限
        List<Permission> webpermissionsList = permissionService.selectAllPermissionList();
        Map<Integer, Permission> webpermissionMap = new HashMap<Integer, Permission>();
        for (Permission permission : webpermissionsList) {
            webpermissionMap.put( permission.getPermissionId(), permission );
        }
        for (Permission permission : webpermissionsList) {
            Permission child = permission;
            if (child.getPid() == 0) {
                roots.add( permission );
            } else {
                Permission parent = webpermissionMap.get( child.getPid() );
                if (parent != null) {
                    parent.getChildren().add( child );
                }
            }
        }
//        List<Permission> interfacePermissions = permissionService.selectInterfacePermissionsList();
        //查询数据权限
        List<DataPermission> dataPermissionList = dataPermissionService.selectDataPermissionList();
        Map<Integer, DataPermission> interfacePermissionMap = new HashMap<Integer, DataPermission>();
        for (DataPermission permission : dataPermissionList) {
            interfacePermissionMap.put( permission.getPermissionId(), permission );
        }
        for (DataPermission dataPermission : dataPermissionList) {
            DataPermission child = dataPermission;
            if (child.getPid() == 0) {
                dataPermissionRoots.add( dataPermission );
            } else {
                DataPermission parent = interfacePermissionMap.get( child.getPid() );
                if (parent != null) {
                    parent.getChildren().add( child );

                }
            }
        }
        jsonObject.put( "roots", roots );
        jsonObject.put( "interfaceRoots", dataPermissionRoots );
        return jsonObject;
    }

    /**
     * 查询当前角色所拥有的权限
     */
    @RequestMapping(value = "/selectAllPermissionTreeByRoleId", method = RequestMethod.POST)
    @ResponseBody
    public Object selectAllPermissionTreeByRoleId(Integer roleId) {
        JSONObject jsonObject = new JSONObject();
        List<Permission> roots = new ArrayList<Permission>();
        //数据权限
        List<DataPermission> dataPermissionRoots = new ArrayList<DataPermission>();
        // 根据标识查询web页面权限
        List<Permission> webpermissionsList = permissionService.selectAllPermissionList();
        // 查询当前角色已经分配的许可信息
        List<RolePermission> rolePermissionList = rolePermissionService.queryPermissionIdsByRoleId( roleId );
        //页面权限
        Map<Integer, Permission> permissionMap = new HashMap<>();
        //数据权限
        Map<Integer, DataPermission> dataPermissionHashMap = new HashMap<Integer, DataPermission>();
        //查询数据权限
        List<DataPermission> dataPermissionList = dataPermissionService.selectDataPermissionList();
        for (RolePermission rolePermission : rolePermissionList) {
            //web页面权限
            for (Permission permission : webpermissionsList) {
                //判断是页面权限
                if (rolePermission.getWebDataPermission() == 1) {
                    if (rolePermission.getPermissionId() == (permission.getPermissionId())) {
                        permission.setChecked( true );
                        permissionMap.put( permission.getPermissionId(), permission );
                    }
                }
            }
            // 数据权限
            for (DataPermission dataPermission : dataPermissionList) {
                //判断是数据权限
                if (rolePermission.getWebDataPermission() ==0){
                    if (rolePermission.getPermissionId() == dataPermission.getPermissionId()) {
                        dataPermission.setChecked( true );
                        dataPermissionHashMap.put( dataPermission.getPermissionId(), dataPermission );
                    }
                }
            }
        }
        for (Permission permission : webpermissionsList) {
            if (permission.getPid() == 0) {
                roots.add( permission );
            } else {
                Permission parent = permissionMap.get( permission.getPid() );
//                System.out.println("permission.getPid():"+ JSONObject.toJSON( permission.getPid() ) );
                for (Permission permi : webpermissionsList) {
                    permissionMap.put( permi.getPermissionId(), permi );
                }
                if (parent != null) {
                    parent.getChildren().add( permission );
//                    System.out.println("permission:"+ JSONObject.toJSON( permission ) );

                }
            }
        }
        //数据权限
        for (DataPermission deptEmtity : dataPermissionList) {
            if (deptEmtity.getPid() == 0) {
                dataPermissionRoots.add( deptEmtity );
            } else {
                DataPermission parent = dataPermissionHashMap.get( deptEmtity.getPid() );
                for (DataPermission dataPermission : dataPermissionList) {
                    dataPermissionHashMap.put( dataPermission.getPermissionId(), dataPermission );
                }
                if (parent != null) {
                    parent.getChildren().add( deptEmtity );
                }
            }
        }

        jsonObject.put( "roots", roots );
        jsonObject.put( "interfaceRoots", dataPermissionRoots );
        return jsonObject;
    }

}
