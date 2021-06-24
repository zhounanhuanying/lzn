package com.bfdb.mapper;

import com.bfdb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface UserMapper {
    User userByUserId(@Param("userId") Integer userId);

    List<User> selectAllUser(@Param("page") Integer page, @Param("limit")Integer limit, @Param("userId") Integer userId);

    User findByName(@Param("userName") String userName );

    int dataCount(@Param("userId")Integer userId);

    int deleteByPrimaryKey(@Param("userId")Integer userId);

    int insert(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(Integer userId);
    //修改用户信息
    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);

    User selectByName(String userName);

}
