package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;


/**
 *导出人员和照片
 */
@Data
@ExcelTarget("PersonUtil")
public class ExcelPersonTable {

    /**
     * 主键
     */
    @Excel(name = "id", width = 30 , needMerge = true,isColumnHidden = true)
    private Integer personId;

    /**
     * 人员唯一标识
     */
    @Excel(name = "personnel_unique_code", width = 30 , needMerge = true,isColumnHidden = true)
    private String personnelUniqueCode;

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
     * 所属园区
     */
    @Excel(name = "所属园区", width = 30)
    private String parkname;
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
     * 人脸图片地址
     */
    @Excel(name = "人脸图片", width = 60)
    private String faceAddress;
//    /**
//     * 校园卡正面照地址
//     */
//    @Excel(name = "校园卡正面照地址", width = 60)
//    private String campusCardAddress;
//    //身份证照片
//    @Excel(name = "身份证照片", width = 60)
//    private String idcardImage;
//    //人证校验图片
//    @Excel(name = "人证校验图片", width = 60)
//    private String faceImage;

    /**
     * 数据来源
     */
    @Excel(name = "数据来源", width = 30,replace={"H5端人脸采集_1","人证核验终端采集_2","web端excel导入_3"})
    private String dataSource;

    /**
     * 班级Code临时存放
     */
    private String orgName;

    /**
     * 学校Code临时存放
     */
    private String departmentss;

    /**
     * 年级Code临时存放
     */
    private String gradeName;
}
