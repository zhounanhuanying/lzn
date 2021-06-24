package com.bfdb.mapper;


import com.bfdb.entity.SysServer;

import java.util.List;
import java.util.Map;

public interface SysServerMapper {

    int insertSysServer(SysServer sysServer);

    //查询服务器信息
    List<SysServer> getSysServerList(Map<String, Object> params);

    //删除服务器
    int deleteSysServerById(Integer serverId);

    //修改服务器信息
    int updateSysServer(SysServer sysServer);

    //查询服务器条数
    int dataCount(Map<String, Object> params);

    SysServer getSysServerByServerIp(String serverIP);
    //查询是否开启页面显示的人证核验终端的集合
    List<SysServer> getSysServerByVerificationStatusList();

    List<SysServer> getSysServerListSuccess();
}
