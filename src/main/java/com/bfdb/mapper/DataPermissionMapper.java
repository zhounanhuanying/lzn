package com.bfdb.mapper;

import com.bfdb.entity.DataPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * DataPermissionMapper继承基类
 */
@Mapper
public interface DataPermissionMapper extends MyBatisBaseDao<DataPermission, Integer> {
    //查询数据权限
    List<DataPermission> selectDataPermissionList();

    //根据用户id查询部门信息
    List<DataPermission> selectByPopupWay(Map<String, Object> map);
     //删除数据权限
    int deleteBydataPermission(String dicCode);
}