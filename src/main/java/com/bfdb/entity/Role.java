package com.bfdb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Role implements Serializable {

    private int roleId;
    private String roleName;
    private String description;
    private Integer pid;
    private List<Role> children = new ArrayList<Role>();
    private boolean open = true;
    private boolean checked = false;
    public Role() {
    }

    public Role(int roleId, String roleName, String description, Integer pid, List<Role> children, boolean open, boolean checked) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.pid = pid;
        this.children = children;
        this.open = open;
        this.checked = checked;
    }

    public List<Role> getChildren() {
        return children;
    }

    public void setChildren(List<Role> children) {
        this.children = children;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", pid=" + pid +
                ", children=" + children +
                ", open=" + open +
                ", checked=" + checked +
                '}';
    }
}
