package com.bfdb.service;


import com.bfdb.entity.Permission;
import com.bfdb.entity.RolePermission;

import java.util.List;
import java.util.Map;

public interface RolePermissionService {

    void insertRolePermissions(Map<String, Object> paramMap);

    List<RolePermission> queryPermissionIdsByRoleId(Integer roleId);

    int deleteByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(RolePermission rolePermission);


    List<RolePermission> selectAllByPrimaryKey(Integer roleId);

    List<Permission> selectPermissionByUserId(Integer userId);
}
