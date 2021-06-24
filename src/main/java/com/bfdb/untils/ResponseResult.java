package com.bfdb.untils;

import java.io.Serializable;

public class ResponseResult implements Serializable{
    private Integer responseCode;
    private String responseDescription;
    private Object datas;
    private Object pageInfo;

    public Object getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Object pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "responseCode=" + responseCode +
                ", responseDescription='" + responseDescription + '\'' +
                ", datas=" + datas +
                ", pageInfo=" + pageInfo +
                '}';
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Object getDatas() {
        return datas;
    }

    public void setDatas(Object datas) {
        this.datas = datas;
    }

    public ResponseResult(Integer responseCode, String responseDescription, Object datas, Object pageInfo) {

        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.datas = datas;
        this.pageInfo = pageInfo;
    }

    public ResponseResult() {

    }
}
