package com.bfdb.entity.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@PropertySource({"classpath:verificationTerminal.properties"})
public class ConfigurationFileInject implements Serializable {
    //http
    @Value("${allUrl.urlPre}")
    private String subscriptionUrlPre;
    //门禁记录推送的订阅接口
    @Value("${alarm.subscriptionUrlAft}")
    private String subscriptionUrlAft;
    //订阅刷新时输入的有效时间
    @Value("${alarm.subscriptionRefreshTime}")
    private String subscriptionRefreshTime;
    //订阅间隔时间
    @Value("${alarm.updateSubscriptionTime}")
    private String updateSubscriptionTime;
    //接收服务端口号
    @Value("${alarm.serverSocket.port}")
    private String serverSocketPort;
    //数据接收推送的服务器地址类型
    @Value("${alarm.equipment.addressType}")
    private String equipmentAddressType;
    //订阅类型
    @Value("${alarm.subscription.type}")
    private String subscriptionType;
    //初始化订阅时间
    @Value("${alarm.subscriptionInitTime}")
    private String subscriptionInitTime;
    //订阅的库id数目，此为最大
    @Value("${alarm.subscribePersonCondition.libIDNum}")
    private String subscribePersonConditionLibIDNum;
    //订阅传入的id
    @Value("${alarm.equipment.ip}")
    private String equipmentIp;
    //订阅传入的端口号
    @Value("${alarm.equipment.port}")
    private String port;

    //结果码（可控制开门）0:不开门， 1:开门
    @Value("${allUrl.resultCode}")
    private String resultCode;
    //经过时刻：范围[0, 18] 终端上报过人记录中的时间
    @Value("${allUrl.passTime}")
    private String passTime;
    //人机显示信息行数最大为2，当前最多显示两行
    @Value("${allUrl.showInfoNum}")
    private String showInfoNum;
    //请求路径
    @Value("${allUrl.gUIShowInfoUrl}")
    private String gUIShowInfoUrl;
    //设备的ip及端口号
    @Value("${allUrl.showInfoUrl}")
    private String showInfoUrl;

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSubscriptionUrlPre() {
        return subscriptionUrlPre;
    }

    public String getSubscriptionInitTime() {
        return subscriptionInitTime;
    }

    public void setSubscriptionInitTime(String subscriptionInitTime) {
        this.subscriptionInitTime = subscriptionInitTime;
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

    public String getSubscriptionRefreshTime() {
        return subscriptionRefreshTime;
    }

    public void setSubscriptionRefreshTime(String subscriptionRefreshTime) {
        this.subscriptionRefreshTime = subscriptionRefreshTime;
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

    public String getEquipmentAddressType() {
        return equipmentAddressType;
    }

    public void setEquipmentAddressType(String equipmentAddressType) {
        this.equipmentAddressType = equipmentAddressType;
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

    public String getEquipmentIp() {
        return equipmentIp;
    }

    public void setEquipmentIp(String equipmentIp) {
        this.equipmentIp = equipmentIp;
    }

    public String getShowInfoUrl() {
        return showInfoUrl;
    }

    public void setShowInfoUrl(String showInfoUrl) {
        this.showInfoUrl = showInfoUrl;
    }

    @Override
    public String toString() {
        return "ConfigurationFileInject{" +
                "subscriptionUrlPre='" + subscriptionUrlPre + '\'' +
                ", subscriptionUrlAft='" + subscriptionUrlAft + '\'' +
                ", subscriptionRefreshTime='" + subscriptionRefreshTime + '\'' +
                ", updateSubscriptionTime='" + updateSubscriptionTime + '\'' +
                ", serverSocketPort='" + serverSocketPort + '\'' +
                ", equipmentAddressType='" + equipmentAddressType + '\'' +
                ", subscriptionType='" + subscriptionType + '\'' +
                ", subscriptionInitTime='" + subscriptionInitTime + '\'' +
                ", subscribePersonConditionLibIDNum='" + subscribePersonConditionLibIDNum + '\'' +
                ", equipmentIp='" + equipmentIp + '\'' +
                ", port='" + port + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", passTime='" + passTime + '\'' +
                ", showInfoNum='" + showInfoNum + '\'' +
                ", gUIShowInfoUrl='" + gUIShowInfoUrl + '\'' +
                ", showInfoUrl='" + showInfoUrl + '\'' +
                '}';
    }
}
