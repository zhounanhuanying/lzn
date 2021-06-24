package com.bfdb.entity;


import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private int userId;
    private String userName;
    private String password;
    //    private String userType;
//    private int status; // 0 启用 1 禁用
    //token信息
    private String userToken;
    //token过期时间
    private String tokenTime;
    private List<User> roleList;

    public User() {
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(String tokenTime) {
        this.tokenTime = tokenTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<User> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userToken='" + userToken + '\'' +
                ", tokenTime='" + tokenTime + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}
