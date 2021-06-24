package com.bfdb.entity;

import java.io.Serializable;

/**
 * dataDictionary
 * @author 
 */
public class DataDictionary implements Serializable {

    //序号
    private Integer dicId;
    //字典名称
    private String dicName;
    //字典编码
    private String dicCode;
    //字典类型
    private String dicType;
    //字典类型
    private String dicTypes;
    //排序
    private Integer sort;

    public Integer getDicId() {
        return dicId;
    }

    public String getDicTypes() {
        return dicTypes;
    }

    public void setDicTypes(String dicTypes) {
        this.dicTypes = dicTypes;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public DataDictionary(Integer dicId, String dicName, String dicCode, String dicType, Integer sort) {

        this.dicId = dicId;
        this.dicName = dicName;
        this.dicCode = dicCode;
        this.dicType = dicType;
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "DataDictionary{" +
                "dicId=" + dicId +
                ", dicName='" + dicName + '\'' +
                ", dicCode='" + dicCode + '\'' +
                ", dicType='" + dicType + '\'' +
                ", dicTypes='" + dicTypes + '\'' +
                ", sort=" + sort +
                '}';
    }

    public DataDictionary() {

    }
}