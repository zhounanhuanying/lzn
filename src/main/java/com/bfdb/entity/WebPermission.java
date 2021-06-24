package com.bfdb.entity;

import java.io.Serializable;

public class WebPermission implements Serializable {
    private Integer permissionId;
    private String permissionName;
    private String description;
    private Integer pid;
    private String permissionCode;
    private Integer permissionType;
    private String visitorUrl;
    private String iconAddress;

    public WebPermission() {
    }


    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public String getVisitorUrl() {
        return visitorUrl;
    }

    public void setVisitorUrl(String visitorUrl) {
        this.visitorUrl = visitorUrl;
    }

    public String getIconAddress() {
        return iconAddress;
    }

    public void setIconAddress(String iconAddress) {
        this.iconAddress = iconAddress;
    }

    @Override
    public String toString() {
        return "WebPermission{" +
                "permissionId=" + permissionId +
                ", permissionName='" + permissionName + '\'' +
                ", description='" + description + '\'' +
                ", pid=" + pid +
                ", permissionCode='" + permissionCode + '\'' +
                ", permissionType=" + permissionType +
                ", visitorUrl='" + visitorUrl + '\'' +
                ", iconAddress='" + iconAddress + '\'' +
                '}';
    }
}
