package com.bfdb.entity.websocket;

import java.io.Serializable;

public class CardInfo implements Serializable {
    //身份证号码
    private String identityNo;
    //姓名
    private String name;
    //年月日
    private String birthday;
    //住址
    private String residentialAddress;
    //签发地址
    private String issuingAuthority;
    //起止时间
    private String validDateStart;
    //结束时间
    private String validDateEnd;
    //性别
    private Integer gender;
    //民族
    private Integer ethnicity;
    //身份证图片
    private String smallImage;
    //获取的当前图片全景图
    private String faceInfoImage;
    //获取的当前图片小图
    private String faceImage;
    /**
     * 服务器ip
     */
    private String serverIp;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }
    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getValidDateStart() {
        return validDateStart;
    }

    public void setValidDateStart(String validDateStart) {
        this.validDateStart = validDateStart;
    }

    public String getValidDateEnd() {
        return validDateEnd;
    }

    public void setValidDateEnd(String validDateEnd) {
        this.validDateEnd = validDateEnd;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Integer ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getFaceInfoImage() {
        return faceInfoImage;
    }

    public void setFaceInfoImage(String faceInfoImage) {
        this.faceInfoImage = faceInfoImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "identityNo='" + identityNo + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", validDateStart='" + validDateStart + '\'' +
                ", validDateEnd='" + validDateEnd + '\'' +
                ", gender=" + gender +
                ", ethnicity=" + ethnicity +
                ", smallImage='" + smallImage + '\'' +
                ", faceInfoImage='" + faceInfoImage + '\'' +
                ", faceImage='" + faceImage + '\'' +
                ", serverIp='" + serverIp + '\'' +
                '}';
    }
}
