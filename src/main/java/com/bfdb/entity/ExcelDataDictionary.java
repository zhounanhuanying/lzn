package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * @author lsq
 * @version 1.0
 * @description
 * @createTime 2020/8/10 10:38
 */
@ExcelTarget("PersonUtil")
public class ExcelDataDictionary {
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

    //字典名称
    @Excel(name = "字典名称", width = 30 , needMerge = true)
    private String dicName;
    //字典编码
    @Excel(name = "字典编码", width = 30 , needMerge = true)
    private String dicCode;

}
