package com.bfdb.entity.websocket;

import java.io.Serializable;

/**
 * 服务器订阅信息
 */
public class SysSubscriptionAlarm implements Serializable {

    private Integer subscriptionalarmId;
    private Integer serverId;
    private String subId;

    public Integer getSubscriptionalarmId() {
        return subscriptionalarmId;
    }

    public void setSubscriptionalarmId(Integer subscriptionalarmId) {
        this.subscriptionalarmId = subscriptionalarmId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public SysSubscriptionAlarm(Integer subscriptionalarmId, Integer serverId, String subId) {

        this.subscriptionalarmId = subscriptionalarmId;
        this.serverId = serverId;
        this.subId = subId;
    }

    public SysSubscriptionAlarm() {

    }
}
