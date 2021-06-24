package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 刘周南
 */
@Data
public class ErrorExcelPersonDelete {

    @Excel(name = "异常信息", width = 50 , needMerge = true)
    private String errorMsg;
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
    /*@Excel(name = "教工号或学号或一卡通号", width = 30 , needMerge = true)*/
    @Excel(name = "学工号", width = 30 , needMerge = true)
    private String identityTypeCode;

}