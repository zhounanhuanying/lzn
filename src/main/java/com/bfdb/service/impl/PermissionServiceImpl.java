package com.bfdb.service.impl;

import com.bfdb.entity.Permission;
import com.bfdb.mapper.PermissionMapper;
import com.bfdb.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> selectAllPermission(Integer page, Integer limit) {
        return permissionMapper.selectAllPermission(page, limit);
    }

    @Override
    public int dataCount() {
        return permissionMapper.dataCount();
    }

    /**
     * 根据标识查询web页面权限
     * @return
     */
    @Override
    public List<Permission> selectAllPermissionList() {
        return permissionMapper.selectAllPermissionList();
    }

    @Override
    public List<Permission> selectByPermission() {
        return permissionMapper.selectByPermission();
    }


    /**
     * 根据当前用户id以及请求url查询当前用户是否有接口管理的相关权限
     * @param userId
     * @param servletPath
     * @return
     */
    @Override
    public List<Permission> selectByPermissionRole(int userId, String servletPath) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("visitorUrl", servletPath);
        map.put("userId", userId);
        return permissionMapper.selectByPermissionRole(map);
    }

//    /**
//     * 获取人员接口管理
//     * @return
//     */
//    @Override
//    public List<Permission> selectPermissionInterface() {
//        return permissionMapper.selectPermissionInterface();
//    }


}
