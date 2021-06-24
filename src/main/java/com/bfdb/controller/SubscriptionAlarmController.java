package com.bfdb.controller;

import com.bfdb.entity.SysServer;
import com.bfdb.service.SubscriptionService;
import com.bfdb.untils.HttpResponseResult;
import com.bfdb.untils.LayuiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 订阅管理
 */
@RestController
@RequestMapping("/subscription")
public class SubscriptionAlarmController {

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * 订阅告警终端信息
     * @param sysServer
     * @return
     */
    @RequestMapping(value = "/subscriptionAlarm")
    public Map<String,Object> subscriptionAlarm(SysServer sysServer){
        HttpResponseResult httpResponseResult = subscriptionService.subscriptionAlarm(sysServer);
        if(httpResponseResult!=null && !"".equals(sysServer)){
            return LayuiUtil.subscriptionResponse(httpResponseResult.getResponseCode());
        }
        return LayuiUtil.subscriptionResponse(-1);
    }

    /**
     * 取消告警订阅
     * @param sysServer
     * @return
     */
    @RequestMapping(value = "/cancelSubscriptionAlarm")
    public Map<String,Object> cancelSubscriptionAlarm(SysServer sysServer){
        HttpResponseResult httpResponseResult = subscriptionService.cancelSubscriptionAlarm(sysServer);
        return LayuiUtil.subscriptionResponse(httpResponseResult.getResponseCode());
    }
}
