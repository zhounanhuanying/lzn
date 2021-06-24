package com.bfdb.entity.websocket;

import java.io.Serializable;
import java.util.List;

public class Subscription implements Serializable {

    //IP地址类型
    private Integer addressType;
    //IPv4地址。
    private String iPAddress;
    //端口
    private Integer port;
    //订阅周期，单位为s，范围为[30, 3600]
    private Integer duration;
    private Integer type;
    //订阅的库ID数目
    private Integer libIDNum;
    //订阅的库ID列表
    private List<LibID> libIDList;

    public String getiPAddress() {
        return iPAddress;
    }

    public void setiPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public Integer getLibIDNum() {
        return libIDNum;
    }

    public void setLibIDNum(Integer libIDNum) {
        this.libIDNum = libIDNum;
    }

    public List<LibID> getLibIDList() {
        return libIDList;
    }

    public void setLibIDList(List<LibID> libIDList) {
        this.libIDList = libIDList;
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Subscription() {

    }

    @Override
    public String toString() {
        return "Subscription{" +
                "addressType=" + addressType +
                ", iPAddress='" + iPAddress + '\'' +
                ", port=" + port +
                ", duration=" + duration +
                ", type=" + type +
                ", libIDNum=" + libIDNum +
                ", libIDList=" + libIDList +
                '}';
    }
}
