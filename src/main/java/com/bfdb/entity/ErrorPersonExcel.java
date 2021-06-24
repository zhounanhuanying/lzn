package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 刘周南
 */
@Data
public class ErrorPersonExcel {
    /**
     * 主键
     */
    @Excel(name = "id", width = 30 , needMerge = true,isColumnHidden = true)
    private Integer personId;
    /*
         异常信息
         */
    @Excel(name = "异常信息", width = 50 , needMerge = true)
    private String errorMsg;
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
     * 人脸图片地址
     */
    @Excel(name = "人脸图片", width = 60)
    private String faceAddress;
    /**
     * 校园卡正面照地址
     */
    private String campusCardAddress;
    //身份证照片
    private String idcardImage;
    //人证校验图片
    private String faceImage;

    /**
     * 数据来源
     */
    private String dataSource;

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
