package com.bfdb.service.impl;

import com.bfdb.entity.Permission;
import com.bfdb.entity.RolePermission;
import com.bfdb.mapper.RolePermissionMapper;
import com.bfdb.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 根据角色id删除角色信息
     * 新增角色和资源表的关联信息
     * @param paramMap
     */
    @Override
    public void insertRolePermissions(Map<String, Object> paramMap) {
        //根据角色id删除角色信息
//        rolePermissionMapper.deletePermissionsByRoleId(paramMap);
        //新增角色和资源表的关联信息
        rolePermissionMapper.insertRolePermissions(paramMap);
    }

    @Override
    public List<RolePermission> queryPermissionIdsByRoleId(Integer roleId) {
        return rolePermissionMapper.queryPermissionIdsByRoleId(roleId);
    }

    @Override
    public int deleteByPrimaryKey(Integer roleId) {
        return rolePermissionMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int updateByPrimaryKeySelective(RolePermission rolePermission) {
        return rolePermissionMapper.updateByPrimaryKeySelective(rolePermission);
    }

    @Override
    public List<RolePermission> selectAllByPrimaryKey(Integer roleId) {
        return rolePermissionMapper.selectAllByPrimaryKey(roleId);
    }

    @Override
    public List<Permission> selectPermissionByUserId(Integer userId) {
        return rolePermissionMapper.selectPermissionByUserId(userId);
    }

}
