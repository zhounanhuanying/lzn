package com.bfdb.entity.websocket;

import java.io.Serializable;

public class DrivePropertiesBean implements Serializable{

    private String equipmentIp;
    private String addressType;
    private String port;
    private String subscriptionType;
    private String subscribePersonConditionLibIDNum;
    private String subscriptionRefreshTime;
    private String SubscriptionInitTime;
    private String updateSubscriptionTime;
    private String serverSocketPort;
    private String subscriptionUrlPre;
    private String subscriptionUrlAft;

    //结果码（可控制开门）0:不开门， 1:开门
    private String resultCode;
    //经过时刻：范围[0, 18] 终端上报过人记录中的时间
    private String passTime;
    //人机显示信息行数最大为2，当前最多显示两行
    private String showInfoNum;
    //请求路径
    private String gUIShowInfoUrl;
    private String showInfoUrl;

    public String getShowInfoUrl() {
        return showInfoUrl;
    }

    public void setShowInfoUrl(String showInfoUrl) {
        this.showInfoUrl = showInfoUrl;
    }


    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }


    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getShowInfoNum() {
        return showInfoNum;
    }

    public void setShowInfoNum(String showInfoNum) {
        this.showInfoNum = showInfoNum;
    }

    public String getgUIShowInfoUrl() {
        return gUIShowInfoUrl;
    }

    public void setgUIShowInfoUrl(String gUIShowInfoUrl) {
        this.gUIShowInfoUrl = gUIShowInfoUrl;
    }

    public String getEquipmentIp() {
        return equipmentIp;
    }

    public void setEquipmentIp(String equipmentIp) {
        this.equipmentIp = equipmentIp;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    public String getSubscribePersonConditionLibIDNum() {
        return subscribePersonConditionLibIDNum;
    }

    public void setSubscribePersonConditionLibIDNum(String subscribePersonConditionLibIDNum) {
        this.subscribePersonConditionLibIDNum = subscribePersonConditionLibIDNum;
    }

    public String getSubscriptionRefreshTime() {
        return subscriptionRefreshTime;
    }

    public void setSubscriptionRefreshTime(String subscriptionRefreshTime) {
        this.subscriptionRefreshTime = subscriptionRefreshTime;
    }

    public String getSubscriptionInitTime() {
        return SubscriptionInitTime;
    }

    public void setSubscriptionInitTime(String subscriptionInitTime) {
        SubscriptionInitTime = subscriptionInitTime;
    }

    public String getUpdateSubscriptionTime() {
        return updateSubscriptionTime;
    }

    public void setUpdateSubscriptionTime(String updateSubscriptionTime) {
        this.updateSubscriptionTime = updateSubscriptionTime;
    }

    public String getServerSocketPort() {
        return serverSocketPort;
    }

    public void setServerSocketPort(String serverSocketPort) {
        this.serverSocketPort = serverSocketPort;
    }

    public String getSubscriptionUrlPre() {
        return subscriptionUrlPre;
    }

    public void setSubscriptionUrlPre(String subscriptionUrlPre) {
        this.subscriptionUrlPre = subscriptionUrlPre;
    }

    public String getSubscriptionUrlAft() {
        return subscriptionUrlAft;
    }

    public void setSubscriptionUrlAft(String subscriptionUrlAft) {
        this.subscriptionUrlAft = subscriptionUrlAft;
    }

    @Override
    public String toString() {
        return "DrivePropertiesBean{" +
                "equipmentIp='" + equipmentIp + '\'' +
                ", addressType='" + addressType + '\'' +
                ", port='" + port + '\'' +
                ", subscriptionType='" + subscriptionType + '\'' +
                ", subscribePersonConditionLibIDNum='" + subscribePersonConditionLibIDNum + '\'' +
                ", subscriptionRefreshTime='" + subscriptionRefreshTime + '\'' +
                ", SubscriptionInitTime='" + SubscriptionInitTime + '\'' +
                ", updateSubscriptionTime='" + updateSubscriptionTime + '\'' +
                ", serverSocketPort='" + serverSocketPort + '\'' +
                ", subscriptionUrlPre='" + subscriptionUrlPre + '\'' +
                ", subscriptionUrlAft='" + subscriptionUrlAft + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", passTime='" + passTime + '\'' +
                ", showInfoNum='" + showInfoNum + '\'' +
                ", gUIShowInfoUrl='" + gUIShowInfoUrl + '\'' +
                ", showInfoUrl='" + showInfoUrl + '\'' +
                '}';
    }
}
