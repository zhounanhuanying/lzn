package com.bfdb.mapper;

import com.bfdb.entity.Permission;
import com.bfdb.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RolePermissionMapper {

    int deleteByPrimaryKey(Integer roleId);

    int insert(RolePermission rolePermission);

    int insertSelective(RolePermission rolePermission);

    RolePermission selectByPrimaryKey(Integer id);

    List<RolePermission> selectAllByPrimaryKey(Integer roleId);

    List<Permission> selectPermissionByUserId(Integer userId);

    int updateByPrimaryKeySelective(RolePermission rolePermission);

    int updateByPrimaryKey(RolePermission rolePermission);
    //新增角色和资源表的关联信息
    void insertRolePermissions(Map<String, Object> paramMap);

    void deletePermissionsByRoleId(Map<String, Object> paramMap);

    List<RolePermission> queryPermissionIdsByRoleId(Integer roleId);
}
