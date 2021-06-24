package com.bfdb.service.impl;

import com.bfdb.entity.DataPermission;
import com.bfdb.mapper.DataPermissionMapper;
import com.bfdb.service.DataPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataPermissionServiceImpl implements DataPermissionService {

    @Autowired
    DataPermissionMapper dataPermissionMapper;

    /**
     * 查询数据权限
     *
     * @return
     */
    @Override
    public List<DataPermission> selectDataPermissionList() {
        return dataPermissionMapper.selectDataPermissionList();
    }

    /**
     * 根据用户id查询部门信息
     * @param userId
     * @param getpersontableall
     * @return
     */
    @Override
    public  List<DataPermission> selectByPopupWay(Integer userId, String getpersontableall) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userId", userId);
        map.put("visitorUrl", getpersontableall);
        return dataPermissionMapper.selectByPopupWay(map);
    }
}
