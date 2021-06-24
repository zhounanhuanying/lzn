package com.bfdb.service;


import com.bfdb.entity.UserRole;

import java.util.List;
import java.util.Map;

public interface UserRoleService {

    int insertUserRole(int userId, int roleId);

    int deleteByPrimaryKey(Integer userId);

    void insertUserRoles(Map<String, Object> paramMap);

    List<UserRole> queryRoleIdsByUserId(Integer userId);

    int updateByPrimaryKeySelective(Map<String, Object> paramMap);

    List<UserRole> selectAllByPrimaryKey(Integer userId);

    List<UserRole> selectByPrimaryKey(Integer roleId);
}
