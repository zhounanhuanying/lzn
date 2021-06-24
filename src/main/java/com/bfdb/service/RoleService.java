package com.bfdb.service;


import com.bfdb.entity.Role;

import java.util.List;

public interface RoleService {

    boolean insertRole(String roleName, String description);

    List<Role> selectAllRole(Integer page, Integer limit);

    int dataCount();

    Role getRoleByRoleName(String roleName);

    int deleteByPrimaryKey(Integer roleId);

    List<Role> selectAllRole2();
     //修改角色信息
    int updateByPrimaryKeySelective(Role role);

}
