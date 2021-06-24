package com.bfdb.entity.vo;

import java.io.Serializable;

public class PageInfoVo implements Serializable {
    /**
     * 实际返回的条目数
     */
    private Integer RowNum=1;//当前页码;
    /**
     * 每页显示数量
     */
    private Integer pageSize =200;//每页记录数;
    /**
     * 总条数
     */
    private Integer TotalRowNum;

    public Integer getRowNum() {
        return RowNum;
    }

    public void setRowNum(Integer rowNum) {
        RowNum = rowNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRowNum() {
        return TotalRowNum;
    }

    public void setTotalRowNum(Integer totalRowNum) {
        TotalRowNum = totalRowNum;
    }

    @Override
    public String toString() {
        return "PageInfoVo{" +
                "RowNum='" + RowNum + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", TotalRowNum='" + TotalRowNum + '\'' +
                '}';
    }
}
