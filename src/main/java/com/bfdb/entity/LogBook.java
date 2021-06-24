package com.bfdb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * log_book
 * @author 
 */
public class LogBook implements Serializable {
    private Integer logNum;

    private String logContent;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String userOperation;

    /**
     * 一般操作；程序异常
     */
    private String logType;

    private static final long serialVersionUID = 1L;

    public Integer getLogNum() {
        return logNum;
    }

    public void setLogNum(Integer logNum) {
        this.logNum = logNum;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserOperation() {
        return userOperation;
    }

    public void setUserOperation(String userOperation) {
        this.userOperation = userOperation;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
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
        LogBook other = (LogBook) that;
        return (this.getLogNum() == null ? other.getLogNum() == null : this.getLogNum().equals(other.getLogNum()))
            && (this.getLogContent() == null ? other.getLogContent() == null : this.getLogContent().equals(other.getLogContent()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUserOperation() == null ? other.getUserOperation() == null : this.getUserOperation().equals(other.getUserOperation()))
            && (this.getLogType() == null ? other.getLogType() == null : this.getLogType().equals(other.getLogType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLogNum() == null) ? 0 : getLogNum().hashCode());
        result = prime * result + ((getLogContent() == null) ? 0 : getLogContent().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUserOperation() == null) ? 0 : getUserOperation().hashCode());
        result = prime * result + ((getLogType() == null) ? 0 : getLogType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", logNum=").append(logNum);
        sb.append(", logContent=").append(logContent);
        sb.append(", createTime=").append(createTime);
        sb.append(", userOperation=").append(userOperation);
        sb.append(", logType=").append(logType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}