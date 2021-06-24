package com.bfdb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * data_delivery_address_table
 * @author 
 */
public class DataDeliveryAddressTable implements Serializable {
    private Integer id;

    private String deliveryName;

    private String deliveryAddres;

    /**
     * 推送时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date deliveryTime;

    private String deliveryStatus;

    private Integer deliveryCount;

    private String deliveryContent;

    private Integer deliveryType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryAddres() {
        return deliveryAddres;
    }

    public void setDeliveryAddres(String deliveryAddres) {
        this.deliveryAddres = deliveryAddres;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(Integer deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public String getDeliveryContent() {
        return deliveryContent;
    }

    public void setDeliveryContent(String deliveryContent) {
        this.deliveryContent = deliveryContent;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
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
        DataDeliveryAddressTable other = (DataDeliveryAddressTable) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeliveryName() == null ? other.getDeliveryName() == null : this.getDeliveryName().equals(other.getDeliveryName()))
            && (this.getDeliveryAddres() == null ? other.getDeliveryAddres() == null : this.getDeliveryAddres().equals(other.getDeliveryAddres()))
            && (this.getDeliveryTime() == null ? other.getDeliveryTime() == null : this.getDeliveryTime().equals(other.getDeliveryTime()))
            && (this.getDeliveryStatus() == null ? other.getDeliveryStatus() == null : this.getDeliveryStatus().equals(other.getDeliveryStatus()))
            && (this.getDeliveryCount() == null ? other.getDeliveryCount() == null : this.getDeliveryCount().equals(other.getDeliveryCount()))
            && (this.getDeliveryContent() == null ? other.getDeliveryContent() == null : this.getDeliveryContent().equals(other.getDeliveryContent()))
            && (this.getDeliveryType() == null ? other.getDeliveryType() == null : this.getDeliveryType().equals(other.getDeliveryType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeliveryName() == null) ? 0 : getDeliveryName().hashCode());
        result = prime * result + ((getDeliveryAddres() == null) ? 0 : getDeliveryAddres().hashCode());
        result = prime * result + ((getDeliveryTime() == null) ? 0 : getDeliveryTime().hashCode());
        result = prime * result + ((getDeliveryStatus() == null) ? 0 : getDeliveryStatus().hashCode());
        result = prime * result + ((getDeliveryCount() == null) ? 0 : getDeliveryCount().hashCode());
        result = prime * result + ((getDeliveryContent() == null) ? 0 : getDeliveryContent().hashCode());
        result = prime * result + ((getDeliveryType() == null) ? 0 : getDeliveryType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deliveryName=").append(deliveryName);
        sb.append(", deliveryAddres=").append(deliveryAddres);
        sb.append(", deliveryTime=").append(deliveryTime);
        sb.append(", deliveryStatus=").append(deliveryStatus);
        sb.append(", deliveryCount=").append(deliveryCount);
        sb.append(", deliveryContent=").append(deliveryContent);
        sb.append(", deliveryType=").append(deliveryType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}