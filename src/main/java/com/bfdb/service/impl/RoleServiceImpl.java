package com.bfdb.service.impl;

import com.bfdb.entity.Role;
import com.bfdb.mapper.RoleMapper;
import com.bfdb.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean insertRole(String roleName, String description) {
        int i = roleMapper.insertRole(roleName, description);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public List<Role> selectAllRole(Integer page, Integer limit) {
        return roleMapper.selectAllRole(page, limit);
    }

    @Override
    public int dataCount() {
        return roleMapper.dataCount();
    }

    @Override
    public Role getRoleByRoleName(String roleName) {
        return roleMapper.getRoleByRoleName(roleName);
    }

    @Override
    public int deleteByPrimaryKey(Integer roleId) {
        return roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public List<Role> selectAllRole2() {
        return roleMapper.selectAllRole2();
    }

    /**
     * 修改角色信息
     * @param role
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }
}
