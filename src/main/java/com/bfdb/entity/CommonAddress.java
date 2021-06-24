package com.bfdb.entity;

import java.io.Serializable;

/**
 * common_url
 * @author
 */
public class CommonAddress implements Serializable {
    private Integer id;

    /**
     * 公共的地址
     */
    private String commonUrl;

    /**
     * 公共地址名称
     */
    private String urlName;

    private Integer sort;

    private String filter;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommonUrl() {
        return commonUrl;
    }

    public void setCommonUrl(String commonUrl) {
        this.commonUrl = commonUrl;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
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
        CommonAddress other = (CommonAddress) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCommonUrl() == null ? other.getCommonUrl() == null : this.getCommonUrl().equals(other.getCommonUrl()))
                && (this.getUrlName() == null ? other.getUrlName() == null : this.getUrlName().equals(other.getUrlName()))
                && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()))
                && (this.getFilter() == null ? other.getFilter() == null : this.getFilter().equals(other.getFilter()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCommonUrl() == null) ? 0 : getCommonUrl().hashCode());
        result = prime * result + ((getUrlName() == null) ? 0 : getUrlName().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getFilter() == null) ? 0 : getFilter().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", commonUrl=").append(commonUrl);
        sb.append(", urlName=").append(urlName);
        sb.append(", sort=").append(sort);
        sb.append(", filter=").append(filter);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}