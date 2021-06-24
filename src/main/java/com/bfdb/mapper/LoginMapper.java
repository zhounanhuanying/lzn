package com.bfdb.mapper;

import com.bfdb.entity.Permission;
import com.bfdb.entity.User;

import java.util.List;

public interface LoginMapper {
    User getUserByName(String userName);

    List<Permission> findPermissionByUserId(int userId);

}
