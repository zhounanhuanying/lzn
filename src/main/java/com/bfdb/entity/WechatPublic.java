package com.bfdb.entity;

import java.io.Serializable;

/**
 * wechat_public
 * @author 
 */
public class WechatPublic implements Serializable {
    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 鼎立公众号二维码
     */
    private String qrCodePath;

    /**
     * 公众号采集页面入口
     */
    private String collectionEntryPath;

    /**
     * 人脸采集页面
     */
    private String faceCollectionPagePath;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }

    public String getCollectionEntryPath() {
        return collectionEntryPath;
    }

    public void setCollectionEntryPath(String collectionEntryPath) {
        this.collectionEntryPath = collectionEntryPath;
    }

    public String getFaceCollectionPagePath() {
        return faceCollectionPagePath;
    }

    public void setFaceCollectionPagePath(String faceCollectionPagePath) {
        this.faceCollectionPagePath = faceCollectionPagePath;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WechatPublic other = (WechatPublic) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getQrCodePath() == null ? other.getQrCodePath() == null : this.getQrCodePath().equals(other.getQrCodePath()))
            && (this.getCollectionEntryPath() == null ? other.getCollectionEntryPath() == null : this.getCollectionEntryPath().equals(other.getCollectionEntryPath()))
            && (this.getFaceCollectionPagePath() == null ? other.getFaceCollectionPagePath() == null : this.getFaceCollectionPagePath().equals(other.getFaceCollectionPagePath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getQrCodePath() == null) ? 0 : getQrCodePath().hashCode());
        result = prime * result + ((getCollectionEntryPath() == null) ? 0 : getCollectionEntryPath().hashCode());
        result = prime * result + ((getFaceCollectionPagePath() == null) ? 0 : getFaceCollectionPagePath().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", qrCodePath=").append(qrCodePath);
        sb.append(", collectionEntryPath=").append(collectionEntryPath);
        sb.append(", faceCollectionPagePath=").append(faceCollectionPagePath);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}