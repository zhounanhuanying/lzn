package com.bfdb.mapper;

import com.bfdb.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PermissionMapper {

    List<Permission> selectAllPermission(@Param("page") Integer page, @Param("limit") Integer limit);

    int dataCount();

    int deleteByPrimaryKey(Integer permissionId);

    int insert(Permission permission);

    default int insertSelective(Permission permission) {
        return 0;
    }

    Permission selectByPrimaryKey(Integer permissionId);

    int updateByPrimaryKeySelective(Permission permission);

    int updateByPrimaryKey(Permission permission);

    List<Permission> selectByPermission();

    List<Permission> findPermissionAll();
    //根据标识查询web页面权限
    List<Permission> selectAllPermissionList();
    //根据当前用户id以及请求url查询当前用户是否有接口管理的相关权限
    List<Permission> selectByPermissionRole(Map<String, Object> map);
    //根据用户id、url路径、所在部门查询权限信息
    Permission selectDepartments(Map<String, Object> map);

}
