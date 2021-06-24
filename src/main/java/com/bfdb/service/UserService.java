package com.bfdb.service;

import com.bfdb.entity.User;


import java.util.List;

public interface UserService {

    User findByName( String userName );

    List<User> selectAllUser(Integer page,Integer limit,Integer userId);

    int dataCount(Integer userId);

    int insertSelective(User user);

    int deleteByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User user);

     //根据用户名称，查询用户信息
    User selectByName(String userName);

    User findByUserID(Integer userId);
}
