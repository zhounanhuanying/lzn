package com.bfdb.entity.websocket;

import java.io.Serializable;
import java.util.List;

public class SubscribePersonCondition implements Serializable {

    //订阅的库ID数目
    private Integer libIDNum;
    //订阅的库ID列表
    private List<LibID> libIDList;

    private Integer fileType;

    @Override
    public String toString() {
        return "SubscribePersonCondition{" +
                "libIDNum=" + libIDNum +
                ", libIDList=" + libIDList +
                ", fileType=" + fileType +
                '}';
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

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public SubscribePersonCondition(Integer libIDNum, List<LibID> libIDList, Integer fileType) {

        this.libIDNum = libIDNum;
        this.libIDList = libIDList;
        this.fileType = fileType;
    }

    public SubscribePersonCondition() {

    }
}
