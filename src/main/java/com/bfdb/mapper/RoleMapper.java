package com.bfdb.mapper;

import com.bfdb.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {

    int insertRole(@Param("roleName") String roleName, @Param("description") String description);

    int roleByRoleId(@Param("roleId") Integer roleId);

    Role getRoleByRoleName(@Param("roleName") String roleName);

    List<Role> selectAllRole(@Param("page") Integer page, @Param("limit") Integer limit);

    int dataCount();

    int deleteByPrimaryKey(Integer roleId);

    int insert(Role role);

    int insertSelective(Role role);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(Role role);

    int updateByPrimaryKey(Role role);

    List<Role> selectAllRole2();
}
