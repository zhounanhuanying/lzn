package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 *导出人员
 */
@Data
public class ExcelPersonTableZi implements Serializable {
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
    @Excel(name = "性别", width = 30 , needMerge = true,replace={"女_0","男_1"})
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
     * 数据来源
     */
    @Excel(name = "数据来源", width = 30,replace={"H5端人脸采集_1","人证核验终端采集_2","web端excel导入_3"})
    private String dataSource;

    /**
     * 所属园区
     */
    @Excel(name = "所属园区", width = 30)
    private String parkname;


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
