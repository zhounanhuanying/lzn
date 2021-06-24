package com.bfdb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BasePark implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键id
    private Integer id;

    //园区类型
    private Integer parkType;

    private String dicName;

    //园区名称
    private String parkName;

    //园区编码
    private String parkCode;

    //logo图片
    private String parkLogoImage;

    //是否删除 1未删除 2已删除
    private Integer isDelete;

    //排序字段
    private Integer sort;

    //备注说明
    private String content;

    private String imgs;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParkType() {
        return parkType;
    }

    public void setParkType(Integer parkType) {
        this.parkType = parkType;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getParkLogoImage() {
        return parkLogoImage;
    }

    public void setParkLogoImage(String parkLogoImage) {
        this.parkLogoImage = parkLogoImage;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BasePark{" +
                "id=" + id +
                ", parkType=" + parkType +
                ", dicName='" + dicName + '\'' +
                ", parkName='" + parkName + '\'' +
                ", parkCode='" + parkCode + '\'' +
                ", parkLogoImage='" + parkLogoImage + '\'' +
                ", isDelete=" + isDelete +
                ", sort=" + sort +
                ", content='" + content + '\'' +
                ", imgs='" + imgs + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
