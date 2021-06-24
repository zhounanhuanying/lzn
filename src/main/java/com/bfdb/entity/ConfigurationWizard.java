package com.bfdb.entity;

import java.io.Serializable;

public class ConfigurationWizard implements Serializable {

    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private String saveImage;
    private String alarmEquipmentIP;

    @Override
    public String toString() {
        return "ConfigurationWizard{" +
                "jdbcURL='" + jdbcURL + '\'' +
                ", jdbcUsername='" + jdbcUsername + '\'' +
                ", jdbcPassword='" + jdbcPassword + '\'' +
                ", saveImage='" + saveImage + '\'' +
                ", alarmEquipmentIP='" + alarmEquipmentIP + '\'' +
                '}';
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getSaveImage() {
        return saveImage;
    }

    public void setSaveImage(String saveImage) {
        this.saveImage = saveImage;
    }

    public String getAlarmEquipmentIP() {
        return alarmEquipmentIP;
    }

    public void setAlarmEquipmentIP(String alarmEquipmentIP) {
        this.alarmEquipmentIP = alarmEquipmentIP;
    }

    public ConfigurationWizard(String jdbcURL, String jdbcUsername, String jdbcPassword, String saveImage, String alarmEquipmentIP) {

        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
        this.saveImage = saveImage;
        this.alarmEquipmentIP = alarmEquipmentIP;
    }

    public ConfigurationWizard() {

    }
}
