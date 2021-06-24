package com.bfdb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Permission implements Serializable {
    private int  permissionId;
    private String permissionName;
    private String description;
    private Integer pid;
    private List<Permission> children = new ArrayList<Permission>();
    private boolean open = true;
    private boolean checked = false;
    private String permissionCode;
    private int permissionType;
    private String visitorUrl;
    private String iconAddress;
    private String title;
    private String definition;
    //字段目前用户所在部门code
    private String popupWay;

    public String getPopupWay() {
        return popupWay;
    }

    public void setPopupWay(String popupWay) {
        this.popupWay = popupWay;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Permission() {
    }

    public Permission(int permissionId, String permissionName, String description, Integer pid, List<Permission> children, boolean open, boolean checked, String permissionCode, int permissionType, String visitorUrl,  String iconAddress) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.description = description;
        this.pid = pid;
        this.children = children;
        this.open = open;
        this.checked = checked;
        this.permissionCode = permissionCode;
        this.permissionType = permissionType;
        this.visitorUrl = visitorUrl;
        this.iconAddress = iconAddress;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
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

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
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

}

