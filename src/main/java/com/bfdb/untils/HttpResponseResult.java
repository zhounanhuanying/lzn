package com.bfdb.untils;

import java.io.Serializable;

public class HttpResponseResult implements Serializable {
    private String responseURL;
    private Integer responseCode;
    private String responseString;
    private Integer statusCode;
    private String data;

    @Override
    public String toString() {
        return "HttpResponseResult{" +
                "responseURL='" + responseURL + '\'' +
                ", responseCode=" + responseCode +
                ", responseString='" + responseString + '\'' +
                ", statusCode=" + statusCode +
                ", data=" + data +
                '}';
    }

    public String getResponseURL() {
        return responseURL;
    }

    public void setResponseURL(String responseURL) {
        this.responseURL = responseURL;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HttpResponseResult(String responseURL, Integer responseCode, String responseString, Integer statusCode, String data) {

        this.responseURL = responseURL;
        this.responseCode = responseCode;
        this.responseString = responseString;
        this.statusCode = statusCode;
        this.data = data;
    }

    public HttpResponseResult() {

    }
}
