package com.bfdb.entity;

import java.io.Serializable;

public class RolePermission implements Serializable {

    private int id;
    private int roleId;
    private int  permissionId;
    private int  dataPermissionId;
    private String roleName;
    private String description;
    /**
     * 区分数据权限  和 页面权限  1 页面  0数据
     */
    private Integer webDataPermission;

    public int getDataPermissionId() {
        return dataPermissionId;
    }

    public void setDataPermissionId(int dataPermissionId) {
        this.dataPermissionId = dataPermissionId;
    }

    public RolePermission() {
    }

     public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getWebDataPermission() {
        return webDataPermission;
    }

    public void setWebDataPermission(Integer webDataPermission) {
        this.webDataPermission = webDataPermission;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", permissionId=" + permissionId +
                ", dataPermissionId=" + dataPermissionId +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", webDataPermission=" + webDataPermission +
                '}';
    }
}
