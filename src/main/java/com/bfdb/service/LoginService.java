package com.bfdb.service;

import com.bfdb.entity.Permission;
import com.bfdb.entity.User;

import java.util.List;

public interface LoginService {

    User getUserByName(String userName);

    List<Permission> findPermissionByUserId(int userId);

    List<Permission> findPermissionAll();
}
