package com.bfdb.entity.vo;

import java.io.Serializable;

/**
 * 字典表公共
 */
public class DictionaryVo implements Serializable {

    private  String dicType;
    private  String dicCode;

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    @Override
    public String toString() {
        return "DictionaryVo{" +
                "dicType='" + dicType + '\'' +
                ", dicCode='" + dicCode + '\'' +
                '}';
    }
}
