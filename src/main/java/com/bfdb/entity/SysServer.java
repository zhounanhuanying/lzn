package com.bfdb.entity;

import java.io.Serializable;

/**
 * sysserver
 * @author 
 */
public class SysServer implements Serializable {
    /**
     * 主键
     */
    private Integer serverId;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 服务器ip
     */
    private String serverIp;


    /**
     * 订阅ip
     */
    private String clientIp;

    /**
     * 服务器用户名称
     */
    private String serverUsername;

    /**
     * 服务器用户密码
     */
    private String serverPwd;

    /**
     * 人员类型
     */
    private String identicationInfo;
    /**
     * 身份信息回显
     */

    private String identicationInfos;

    /**
     * 所在部门
     */
    private String departments;
    /**
     * 主要用于回显部门或院系信息
     */
    private String departmentss;

    /**
     * 学历
     */
    private String studentLevel;
    /**
     * 主要用于回显学历信息
     */
    private String studentLevels;


    /**
     * 学级
     */
    private String grade;
    /**
     * 主要用于回显年级信息
     */
    private String grades;

    private String subId;
    private Integer deviceNum;

    /**
     * 是否开启人证核验页面  1.开启 0.关闭
     */
    private String verificationStatus;
    /**
     *主要用于回显 是否开启人证核验页面  1.开启 0.关闭
     */
    private String verificationStatuss;


    private static final long serialVersionUID = 1L;

    public String getVerificationStatuss() {
        return verificationStatuss;
    }

    public void setVerificationStatuss(String verificationStatuss) {
        this.verificationStatuss = verificationStatuss;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public void setServerUsername(String serverUsername) {
        this.serverUsername = serverUsername;
    }

    public String getServerPwd() {
        return serverPwd;
    }

    public void setServerPwd(String serverPwd) {
        this.serverPwd = serverPwd;
    }


    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getIdenticationInfo() {
        return identicationInfo;
    }

    public void setIdenticationInfo(String identicationInfo) {
        this.identicationInfo = identicationInfo;
    }

    public String getStudentLevel() {
        return studentLevel;
    }

    public void setStudentLevel(String studentLevel) {
        this.studentLevel = studentLevel;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public Integer getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIdenticationInfos() {
        return identicationInfos;
    }

    public void setIdenticationInfos(String identicationInfos) {
        this.identicationInfos = identicationInfos;
    }

    public String getDepartmentss() {
        return departmentss;
    }

    public void setDepartmentss(String departmentss) {
        this.departmentss = departmentss;
    }

    public String getStudentLevels() {
        return studentLevels;
    }

    public void setStudentLevels(String studentLevels) {
        this.studentLevels = studentLevels;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }


    @Override
    public String toString() {
        return "SysServer{" +
                "serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", serverUsername='" + serverUsername + '\'' +
                ", serverPwd='" + serverPwd + '\'' +
                ", identicationInfo='" + identicationInfo + '\'' +
                ", identicationInfos='" + identicationInfos + '\'' +
                ", departments='" + departments + '\'' +
                ", departmentss='" + departmentss + '\'' +
                ", studentLevel='" + studentLevel + '\'' +
                ", studentLevels='" + studentLevels + '\'' +
                ", grade='" + grade + '\'' +
                ", grades='" + grades + '\'' +
                ", subId='" + subId + '\'' +
                ", deviceNum=" + deviceNum +
                ", verificationStatus='" + verificationStatus + '\'' +
                ", verificationStatuss='" + verificationStatuss + '\'' +
                '}';
    }

    public SysServer() {

    }
}