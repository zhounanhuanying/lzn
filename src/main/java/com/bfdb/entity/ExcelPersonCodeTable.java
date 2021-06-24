package com.bfdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("PersonUtil")
public class ExcelPersonCodeTable {

    /**
     * 主键
     */
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
     * 年级
     */
    @Excel(name = "年级", width = 30 , needMerge = true)
    private String grade;
    /**
     * 所在部门或院系
     */
    /*@Excel(name = "所在部门或院系", width = 30 , needMerge = true,replace={"01-党政办公室_1","02-党委组织部、党校_2","03-党委宣传部、新闻中心_3","04-党委统战部_4",
            "05-纪委办公室、监察处_5","06-党委教师工作部、人事部_6","07-政策法规研究室_7","08-教务处、卓越办_8","09-科学技术研究院_9","10-大学科技园_10","11-学生处、学生工作部、武装部_11",
    "12-研究生院、研究生工作部、学位办_12","13-计划财务处_13","14-审计处_14","15-对外联络与合作部_15","16-国际合作处、港澳台办公室_16","17-资产管理处_17","18-基建处、校园规划办公室_18",
    "19-后勤管理处、后勤服务集团_19","20-保卫处、保卫部_20","21-学科建设办公室、双一流建设办公室_21","22-人才办、博后办_22","23-网络与信息化办公室_23","24-教育基金工作办公室_24",
    "25-离退休工作办公室_25","26-校工会_26","27-校团委、艺教中心_27","28-电气与电子工程学院_28","29-能源动力与机械工程学院_29","30-控制与计算机工程学院_30","31-经济与管理学院_31",
    "32-可再生能源学院_31","33-核科学与工程学院_33","34-环境科学与工程学院_34","35-数理学院_35","36-人文与社会科学学院_36","37-外国语学院_37","38-马克思主义学院_38","39-国际教育学院_39",
    "40-继续教育学院_40","41-体育教学部_41","42-新能源电力系统国家重点实验室_42","43-生物质发电成套设备国家工程实验室_43","44-国家火力发电工程技术研究中心_44","45-现代电力研究院_45",
    "46-苏州研究院_46","47-工程实践中心_47","48-金工实践中心_48","49-高等教育研究所_49","50-图书馆_50","51-档案馆_51","52-期刊出版社_52","53-校医院_53","54-资产经营有限公司_54","55-其他_55","56-教工家属（教工的一卡通需要上传）_56"})
   */

    @Excel(name = "办公地点", width = 30 , needMerge = true)
    private String departments;
    /**
     * 教工号或学号或一卡通号
     */
    /*@Excel(name = "教工号或学号或一卡通号", width = 30 , needMerge = true)*/
    @Excel(name = "工牌号", width = 30 , needMerge = true)
    private String identityTypeCode;
    /**
     * 备注
     */
    private String description;
    /**
     * 人脸图片地址
     */
    @Excel(name = "人脸图片地址", width = 60)
    private String faceAddress;
    /**
     * 校园卡正面照地址
     */
    @Excel(name = "校园卡正面照地址", width = 60)
    private String campusCardAddress;
    //身份证照片
    @Excel(name = "身份证照片", width = 60)
    private String idcardImage;
    //人证校验图片
    @Excel(name = "人证校验图片", width = 60)
    private String faceImage;

    /**
     * 数据来源
     */
    @Excel(name = "数据来源", width = 30)
    private String dataSource;
    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdenticationInfo() {
        return identicationInfo;
    }

    public void setIdenticationInfo(String identicationInfo) {
        this.identicationInfo = identicationInfo;
    }

    public String getStudentLevel() {
        return studentLevel;
    }

    public void setStudentLevel(String studentLevel) {
        this.studentLevel = studentLevel;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getIdentityTypeCode() {
        return identityTypeCode;
    }

    public void setIdentityTypeCode(String identityTypeCode) {
        this.identityTypeCode = identityTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFaceAddress() {
        return faceAddress;
    }

    public void setFaceAddress(String faceAddress) {
        this.faceAddress = faceAddress;
    }

    public String getCampusCardAddress() {
        return campusCardAddress;
    }

    public void setCampusCardAddress(String campusCardAddress) {
        this.campusCardAddress = campusCardAddress;
    }

    public String getIdcardImage() {
        return idcardImage;
    }

    public void setIdcardImage(String idcardImage) {
        this.idcardImage = idcardImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    @Override
    public String toString() {
        return "ExcelPersonTable{" +
                "personId=" + personId +
                ", personName='" + personName + '\'' +
                ", gender=" + gender +
                ", identityNo='" + identityNo + '\'' +
                ", birthday='" + birthday + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                ", ethnicity='" + ethnicity + '\'' +
                ", telephone='" + telephone + '\'' +
                ", identicationInfo='" + identicationInfo + '\'' +
                ", studentLevel='" + studentLevel + '\'' +
                ", grade='" + grade + '\'' +
                ", departments='" + departments + '\'' +
                ", identityTypeCode='" + identityTypeCode + '\'' +
                ", description='" + description + '\'' +
                ", faceAddress='" + faceAddress + '\'' +
                ", campusCardAddress='" + campusCardAddress + '\'' +
                ", idcardImage='" + idcardImage + '\'' +
                ", faceImage='" + faceImage + '\'' +
                ", dataSource='" + dataSource + '\'' +
                '}';
    }
}
