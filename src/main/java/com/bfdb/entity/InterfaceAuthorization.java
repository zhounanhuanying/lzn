package com.bfdb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * interface_authorization
 * @author 
 */
public class InterfaceAuthorization implements Serializable {
    private Integer id;

    private String interfaceName;

    private String interfaceAddress;

    private String interfaceType;

    /**
     * 接口有效期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date interfaceExpirationDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date callTime;

    private Integer callNumber;

    private Integer callStatus;


    public String getCallStatuss() {
        return callStatuss;
    }

    public void setCallStatuss(String callStatuss) {
        this.callStatuss = callStatuss;
    }

    private String callStatuss;


    private Integer userId;


    /**
     * 请求参数
     */
    private String requestParameters;

    /**
     * 异常信息
     */
    private String exceptionInformation;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // 回显用户姓名
    private String userName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceAddress() {
        return interfaceAddress;
    }

    public void setInterfaceAddress(String interfaceAddress) {
        this.interfaceAddress = interfaceAddress;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public Date getInterfaceExpirationDate() {
        return interfaceExpirationDate;
    }

    public void setInterfaceExpirationDate(Date interfaceExpirationDate) {
        this.interfaceExpirationDate = interfaceExpirationDate;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public Integer getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(Integer callNumber) {
        this.callNumber = callNumber;
    }

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        InterfaceAuthorization other = (InterfaceAuthorization) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInterfaceName() == null ? other.getInterfaceName() == null : this.getInterfaceName().equals(other.getInterfaceName()))
            && (this.getInterfaceAddress() == null ? other.getInterfaceAddress() == null : this.getInterfaceAddress().equals(other.getInterfaceAddress()))
            && (this.getInterfaceType() == null ? other.getInterfaceType() == null : this.getInterfaceType().equals(other.getInterfaceType()))
            && (this.getInterfaceExpirationDate() == null ? other.getInterfaceExpirationDate() == null : this.getInterfaceExpirationDate().equals(other.getInterfaceExpirationDate()))
            && (this.getCallTime() == null ? other.getCallTime() == null : this.getCallTime().equals(other.getCallTime()))
            && (this.getCallNumber() == null ? other.getCallNumber() == null : this.getCallNumber().equals(other.getCallNumber()))
            && (this.getCallStatus() == null ? other.getCallStatus() == null : this.getCallStatus().equals(other.getCallStatus()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInterfaceName() == null) ? 0 : getInterfaceName().hashCode());
        result = prime * result + ((getInterfaceAddress() == null) ? 0 : getInterfaceAddress().hashCode());
        result = prime * result + ((getInterfaceType() == null) ? 0 : getInterfaceType().hashCode());
        result = prime * result + ((getInterfaceExpirationDate() == null) ? 0 : getInterfaceExpirationDate().hashCode());
        result = prime * result + ((getCallTime() == null) ? 0 : getCallTime().hashCode());
        result = prime * result + ((getCallNumber() == null) ? 0 : getCallNumber().hashCode());
        result = prime * result + ((getCallStatus() == null) ? 0 : getCallStatus().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        return result;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getExceptionInformation() {
        return exceptionInformation;
    }

    public void setExceptionInformation(String exceptionInformation) {
        this.exceptionInformation = exceptionInformation;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "InterfaceAuthorization{" +
                "id=" + id +
                ", interfaceName='" + interfaceName + '\'' +
                ", interfaceAddress='" + interfaceAddress + '\'' +
                ", interfaceType='" + interfaceType + '\'' +
                ", interfaceExpirationDate=" + interfaceExpirationDate +
                ", callTime=" + callTime +
                ", callNumber=" + callNumber +
                ", callStatus=" + callStatus +
                ", callStatuss='" + callStatuss + '\'' +
                ", userId=" + userId +
                ", requestParameters='" + requestParameters + '\'' +
                ", exceptionInformation='" + exceptionInformation + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}