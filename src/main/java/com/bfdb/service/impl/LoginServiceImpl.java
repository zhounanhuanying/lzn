package com.bfdb.service.impl;

import com.bfdb.entity.Permission;
import com.bfdb.entity.User;
import com.bfdb.mapper.LoginMapper;
import com.bfdb.mapper.PermissionMapper;
import com.bfdb.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User getUserByName(String userName) {
        //从数据库根据用户名查询得到用户信息
        User user = loginMapper.getUserByName(userName);
        return user;
    }

    @Override
    public List<Permission> findPermissionByUserId(int userId) {
        List<Permission> permission=loginMapper.findPermissionByUserId(userId);
        return permission;
    }

    @Override
    public List<Permission> findPermissionAll() {
        List<Permission> permission=permissionMapper.findPermissionAll();
        return permission;
    }
}
