package com.bfdb.mapper;

import com.bfdb.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserRoleMapper {

    int insertUserRole(@Param("userId") int userId, @Param("roleId") int roleId);

    int deleteByPrimaryKey(@Param("userId") Integer userId);

    int insert(UserRole userRole);

    int insertSelective(UserRole userRole);

    List<UserRole> selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(Map<String, Object> paramMap);

    int updateByPrimaryKey(UserRole userRole);

    void deleteRolesByUserId(Map<String, Object> paramMap);

    List<UserRole> queryRoleIdsByUserId(Integer userId);

    void insertUserRoles(Map<String, Object> paramMap);

    List<UserRole> selectAllByPrimaryKey(Integer userId);
}
