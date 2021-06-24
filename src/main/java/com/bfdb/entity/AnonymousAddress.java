package com.bfdb.entity;

import java.io.Serializable;

/**
 * @author
 */
public class AnonymousAddress implements Serializable {
    private Integer id;

    /**
     * 对外公开的地址
     */
    private String anonymousUrl;

    /**
     * 公开的地址名字
     */
    private String anonymousName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnonymousUrl() {
        return anonymousUrl;
    }

    public void setAnonymousUrl(String anonymousUrl) {
        this.anonymousUrl = anonymousUrl;
    }

    public String getAnonymousName() {
        return anonymousName;
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
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
        AnonymousAddress other = (AnonymousAddress) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAnonymousUrl() == null ? other.getAnonymousUrl() == null : this.getAnonymousUrl().equals(other.getAnonymousUrl()))
            && (this.getAnonymousName() == null ? other.getAnonymousName() == null : this.getAnonymousName().equals(other.getAnonymousName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAnonymousUrl() == null) ? 0 : getAnonymousUrl().hashCode());
        result = prime * result + ((getAnonymousName() == null) ? 0 : getAnonymousName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", anonymousUrl=").append(anonymousUrl);
        sb.append(", anonymousName=").append(anonymousName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}