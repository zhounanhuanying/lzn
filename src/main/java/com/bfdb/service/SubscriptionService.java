package com.bfdb.service;


import com.bfdb.entity.SysServer;
import com.bfdb.untils.HttpResponseResult;

public interface SubscriptionService {

    HttpResponseResult subscriptionAlarm(SysServer sysServer);

    HttpResponseResult cancelSubscriptionAlarm(SysServer sysServer);

}
