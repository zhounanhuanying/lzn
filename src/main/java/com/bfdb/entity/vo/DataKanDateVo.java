package com.bfdb.entity.vo;

import java.util.Date;

public class DataKanDateVo {
    private String date;
    private Integer callNumber;

    @Override
    public String toString() {
        return "DataKanDateVo{" +
                "date=" + date +
                ", callNumber=" + callNumber +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(Integer callNumber) {
        this.callNumber = callNumber;
    }
}
