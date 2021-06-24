package com.bfdb.service;

import com.bfdb.entity.DataPermission;

import java.util.List;

public interface DataPermissionService {
    //查询数据权限
    List<DataPermission> selectDataPermissionList();

    //根据当前用户id以及请求url查询当前用户是否有接口管理的相关权限
    List<DataPermission> selectByPopupWay(Integer userId, String getpersontableall);

}
