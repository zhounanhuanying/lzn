package com.bfdb.entity;

import java.io.Serializable;

/**
 * person_new_face_infomation_table
 * @author 
 */

public class PersonFaceInfomationTable implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 人员id
     */

    private Integer personId;

    /**
     * 人脸图片地址
     */
    private String faceAddress;

    /**
     * 校园卡正面照地址
     */
    private String campusCardAddress;


    //身份证照片
    private String idcardImage;

    //人证校验图片
    private String faceImage;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 数据来源回显
     */
    private String dataSources;


    /**
     * 照片质量
     */
    private Integer photoLevel;
    /**
     * 标识
     */
    private String identification;

    @Override
    public String toString() {
        return "PersonFaceInfomationTable{" +
                "id=" + id +
                ", personId=" + personId +
                ", faceAddress='" + faceAddress + '\'' +
                ", campusCardAddress='" + campusCardAddress + '\'' +
                ", idcardImage='" + idcardImage + '\'' +
                ", faceImage='" + faceImage + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", dataSources='" + dataSources + '\'' +
                ", photoLevel=" + photoLevel +
                ", identification='" + identification + '\'' +
                '}';
    }

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getFaceAddress() {
        return faceAddress;
    }

    public void setFaceAddress(String faceAddress) {
        this.faceAddress = faceAddress;
    }

    public String getCampusCardAddress() {
        return campusCardAddress;
    }

    public void setCampusCardAddress(String campusCardAddress) {
        this.campusCardAddress = campusCardAddress;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getPhotoLevel() {
        return photoLevel;
    }

    public void setPhotoLevel(Integer photoLevel) {
        this.photoLevel = photoLevel;
    }

    public String getIdcardImage() {
        return idcardImage;
    }

    public void setIdcardImage(String idcardImage) {
        this.idcardImage = idcardImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    private static final long serialVersionUID = 1L;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }


}