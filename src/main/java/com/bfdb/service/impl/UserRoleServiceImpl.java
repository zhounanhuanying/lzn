package com.bfdb.service.impl;

import com.bfdb.entity.UserRole;
import com.bfdb.mapper.UserRoleMapper;
import com.bfdb.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public int insertUserRole(int userId, int roleId) {
        return userRoleMapper.insertUserRole(userId, roleId);
    }

    @Override
    public int deleteByPrimaryKey(Integer userId) {
        return userRoleMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public void insertUserRoles(Map<String, Object> paramMap) {
        userRoleMapper.deleteRolesByUserId(paramMap);
        userRoleMapper.insertUserRoles(paramMap);
    }

    @Override
    public List<UserRole> queryRoleIdsByUserId(Integer userId) {
        return userRoleMapper.queryRoleIdsByUserId(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(Map<String, Object> paramMap) {
        return userRoleMapper.updateByPrimaryKeySelective(paramMap);
    }

    @Override
    public List<UserRole> selectAllByPrimaryKey(Integer userId) {
        return userRoleMapper.selectAllByPrimaryKey(userId);
    }

    @Override
    public List<UserRole> selectByPrimaryKey(Integer roleId) {
        return userRoleMapper.selectByPrimaryKey(roleId);
    }
}
