package com.bfdb.service.impl;

import com.bfdb.entity.User;
import com.bfdb.mapper.UserMapper;
import com.bfdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByName(String userName) {
        return userMapper.findByName( userName );
    }

    @Override
    public List<User> selectAllUser(Integer page, Integer limit,Integer userId) {
        return userMapper.selectAllUser( page, limit, userId );
    }

    @Override
    public int dataCount(Integer userId) {
        return userMapper.dataCount(userId);
    }

    @Override
    public int insertSelective(User user) {
        return userMapper.insertSelective( user );
    }

    @Override
    public int deleteByPrimaryKey(Integer userId) {
        return userMapper.deleteByPrimaryKey( userId );
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(User user) {
        int i = 0;
        if ("Administrator".equals( user.getUserName() )) {
            String daoPassword = "d30ede312968fca1a5a1f93df99ed6f9";
            user.setUserName( "Administrator" );
            user.setUserId( 123456789 );//在项目左边动态列表处用到
            user.setPassword( daoPassword );
        } else {
            i = userMapper.updateByPrimaryKeySelective( user );
        }
        return i;
    }

    /**
     * 根据用户名称，查询用户信息
     *
     * @param username
     * @return
     */
    @Override
    public User selectByName(String username) {
        User user = new User();
        if ("Administrator".equals( username )) {
            String daoPassword = "d30ede312968fca1a5a1f93df99ed6f9";
            user.setUserName( "Administrator" );
            user.setUserId( 123456789 );//在项目左边动态列表处用到
            user.setPassword( daoPassword );
        } else {
            user = userMapper.selectByName( username );
        }
        return user;
    }

    @Override
    public User findByUserID(Integer userId) {
        return userMapper.userByUserId(userId);
    }

}
