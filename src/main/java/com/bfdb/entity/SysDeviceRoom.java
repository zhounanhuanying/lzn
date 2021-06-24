package com.bfdb.entity;

import java.io.Serializable;

public class SysDeviceRoom implements Serializable{

    private Integer deviceroomId;
    private String deviceId;
    private String roomCode;

    public Integer getDeviceroomId() {
        return deviceroomId;
    }

    public void setDeviceroomId(Integer deviceroomId) {
        this.deviceroomId = deviceroomId;
    }


    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public SysDeviceRoom(Integer deviceroomId, String deviceId, String roomCode) {

        this.deviceroomId = deviceroomId;
        this.deviceId = deviceId;
        this.roomCode = roomCode;
    }

    public SysDeviceRoom() {
    }
}
