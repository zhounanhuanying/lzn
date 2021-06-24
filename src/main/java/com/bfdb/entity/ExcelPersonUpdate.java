package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * 刘周南
 */
@Data
@ExcelTarget("PersonUtil")
public class ExcelPersonUpdate {

    @Excel(name = "id", width = 30 , needMerge = true,isColumnHidden = true)
    private Integer personId;
    /**
     * 人员姓名
     */
    @Excel(name = "人员姓名", width = 30 , needMerge = true)
    private String personName;
    /**
     * 性别
     */
    @Excel(name = "性别", width = 30 , needMerge = true)
    private Integer gender;

    //身份证号码
    @Excel(name = "身份证号码", width = 30 , needMerge = true)
    private String identityNo;
    //生日
    @Excel(name = "生日", width = 30 , needMerge = true)
    private String birthday;
    //住址
    @Excel(name = "住址", width = 30 , needMerge = true)
    private String residentialAddress;
    //民族
    @Excel(name = "民族", width = 30 , needMerge = true)
    private String ethnicity;
    /**
     * 手机号
     */
    @Excel(name = "手机号", width = 30 , needMerge = true)
    private String telephone;
    /**
     * 身份信息
     */
    //@Excel(name = "人员类型", width = 30 , needMerge = true,replace={"在编教职工_0","离退休教职工_1","学生_2","留学生_3","外聘职员_4","临时工_5","非本校职工或学生_6"})
    @Excel(name = "人员类型", width = 30 , needMerge = true)
    private String identicationInfo;
    /**
     * 学历
     */
    @Excel(name = "学历", width = 30 , needMerge = true)
    private String studentLevel;
    /**
     * 学工号
     */
    @Excel(name = "学工号", width = 30 , needMerge = true)
    private String identityTypeCode;
    /**
     * 备注
     */
    private String description;
    /**
     * 所属园区
     */
    @Excel(name = "所属园区", width = 30)
    private String parkId;

    /**
     * 所属系部
     */
    @Excel(name = "所属系部", width = 30)
    private String departments;

    /**
     * 所属年级
     */
    @Excel(name = "所属年级", width = 30)
    private String grade;

    /**
     * 所属班级
     */
    @Excel(name = "所属班级", width = 30)
    private String schoolclass;
}
