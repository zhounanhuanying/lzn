package com.bfdb.mapper;


import com.bfdb.entity.SysServer;
import com.bfdb.entity.websocket.SysSubscriptionAlarm;

import java.util.List;

public interface SubscriptionMapper {


    List<SysServer> getSysServerList();
   //根据服务器id查询订阅信息
    SysSubscriptionAlarm getSubscription(Integer serverId);

    void updateSubScription(SysSubscriptionAlarm sysSubscriptionAlarm);

    int insertSubscription(SysSubscriptionAlarm sysSubscriptionAlarm);

    int deleteSubscription(Integer subscriptionAlarmId);

    List<SysServer> selectBySysServerList();

}
