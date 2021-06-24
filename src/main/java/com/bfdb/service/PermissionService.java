package com.bfdb.service;


import com.bfdb.entity.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> selectAllPermission(Integer page, Integer limit);

    int dataCount();
    //根据标识查询web页面权限
    List<Permission> selectAllPermissionList();


    List<Permission> selectByPermission();

     // 根据当前用户id以及请求url查询当前用户是否有接口管理的相关权限
    List<Permission> selectByPermissionRole(int userId, String servletPath);
    //获取人员接口管理
//    List<Permission> selectPermissionInterface();

}
