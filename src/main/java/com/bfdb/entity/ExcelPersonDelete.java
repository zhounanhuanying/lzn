package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * 刘周南
 */
@Data
@ExcelTarget("PersonUtil")
public class ExcelPersonDelete {

    /**
     * 人员姓名
     */
    @Excel(name = "人员姓名", width = 30 , needMerge = true)
    private String personName;

    //身份证号码
    @Excel(name = "身份证号码", width = 30 , needMerge = true)
    private String identityNo;

    /**
     * 学工号
     */
    @Excel(name = "学工号", width = 30 , needMerge = true)
    private String identityTypeCode;


}
