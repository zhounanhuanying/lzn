package com.bfdb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * data_permission
 *
 * @author
 */
public class DataPermission implements Serializable {
    private Integer permissionId;

    private String permissionName;

    private String description;

    private Integer pid;

    private String permissionCode;

    /**
     * 页面权限=0；方法权限=1；数据权限=2
     */
    private Integer permissionType;

    private String visitorUrl;

    private String popupWay;

    private String iconAddress;

    private String definition;

    private List<DataPermission> children = new ArrayList<DataPermission>();
    private boolean open = true;
    private boolean checked = false;

    private static final long serialVersionUID = 1L;

    public DataPermission() {

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

    public String getPopupWay() {
        return popupWay;
    }

    public void setPopupWay(String popupWay) {
        this.popupWay = popupWay;
    }

    public String getIconAddress() {
        return iconAddress;
    }

    public void setIconAddress(String iconAddress) {
        this.iconAddress = iconAddress;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<DataPermission> getChildren() {
        return children;
    }

    public void setChildren(List<DataPermission> children) {
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

    public DataPermission(int permissionId, String permissionName, String description, Integer pid, List<DataPermission> children, boolean open, boolean checked, String permissionCode, int permissionType, String visitorUrl,  String iconAddress) {
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
    @Override
    public String toString() {
        return "DataPermission{" +
                "permissionId=" + permissionId +
                ", permissionName='" + permissionName + '\'' +
                ", description='" + description + '\'' +
                ", pid=" + pid +
                ", permissionCode='" + permissionCode + '\'' +
                ", permissionType=" + permissionType +
                ", visitorUrl='" + visitorUrl + '\'' +
                ", popupWay='" + popupWay + '\'' +
                ", iconAddress='" + iconAddress + '\'' +
                ", definition='" + definition + '\'' +
                ", children=" + children +
                ", open=" + open +
                ", checked=" + checked +
                '}';
    }
}