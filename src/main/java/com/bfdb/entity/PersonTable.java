package com.bfdb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * persontable
 * @author 
 */
@Data
public class PersonTable implements Serializable {

     private static final long serialVersionUID = 1L;


    /**
     * 主键
     */

    private Integer personId;

    //人员唯一编码
    private String personnelUniqueCode;

    /**
     * 人员姓名
     */

    private String personName;

    /**
     * 性别
     */

    private Integer gender;

    //身份证号码
    private String identityNo;
    //生日
    private String birthday;
    //住址
    private String residentialAddress;
    //民族
    private String ethnicity;
    //民族回显
    private String ethnicitys;

    /**
     * 手机号
     */


    private String telephone;

    /**
     * 身份信息
     */

    private String identicationInfo;
    /**
     * 身份信息回显
     */

    private String identicationInfos;

    /**
     * 学历
     */

    private String studentLevel;
    /**
     * 主要用于回显学历信息
     */
    private String studentLevels;

    /**
     * 年级
     */

    private String grade;
    /**
     * 主要用于回显年级信息
     */
    private String grades;

    /**
     * 所在部门或院系
     */

    private String departments;
    /**
     * 主要用于回显部门或院系信息
     */
    private String departmentss;

    /**
     * 教工号或学号或一卡通号
     */

    private String identityTypeCode;

    /**
     * 非本校人员信息
     */

    private String outPersonInfomation;

    /**
     * 人员编码
     */

    private String personCode;

    //园区id
    private String parkId;

    //组织机构id
    private String organizitionId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */

    private Date modifyTime;


    //是否删除
    private Integer isDeleted;

    /**
     * 备注
     */

    private String description;
    /**
     * 主要用于回显性别信息
     */
    private String genders;

    /*
    用于查询图片
     */
    private List<PersonFaceInfomationTable> personFaceInfomationTable;
    /**
     * 主要用于接收照片地址信息
     */
    private PersonFaceInfomationTable personFaceInfomationTableBean;

    private String schoolClass;

    private String gradeName;

    private String orgName;

    @Override
    public String toString() {
        return "PersonTable{" +
                "personId=" + personId +
                ", personnelUniqueCode='" + personnelUniqueCode + '\'' +
                ", personName='" + personName + '\'' +
                ", gender=" + gender +
                ", identityNo='" + identityNo + '\'' +
                ", birthday='" + birthday + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                ", ethnicity='" + ethnicity + '\'' +
                ", ethnicitys='" + ethnicitys + '\'' +
                ", telephone='" + telephone + '\'' +
                ", identicationInfo='" + identicationInfo + '\'' +
                ", identicationInfos='" + identicationInfos + '\'' +
                ", studentLevel='" + studentLevel + '\'' +
                ", studentLevels='" + studentLevels + '\'' +
                ", grade='" + grade + '\'' +
                ", grades='" + grades + '\'' +
                ", departments='" + departments + '\'' +
                ", departmentss='" + departmentss + '\'' +
                ", identityTypeCode='" + identityTypeCode + '\'' +
                ", outPersonInfomation='" + outPersonInfomation + '\'' +
                ", personCode='" + personCode + '\'' +
                ", parkId='" + parkId + '\'' +
                ", organizitionId='" + organizitionId + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", isDeleted=" + isDeleted +
                ", description='" + description + '\'' +
                ", genders='" + genders + '\'' +
                ", personFaceInfomationTable=" + personFaceInfomationTable +
                ", personFaceInfomationTableBean=" + personFaceInfomationTableBean +
                ", schoolClass='" + schoolClass + '\'' +
                ", gradeName='" + gradeName + '\'' +
                ", orgName='" + orgName + '\'' +
                ", openId='" + openId + '\'' +
                ", departmentsarr=" + Arrays.toString(departmentsarr) +
                '}';
    }


    public String getPersonnelUniqueCode() {
        return personnelUniqueCode;
    }

    public void setPersonnelUniqueCode(String personnelUniqueCode) {
        this.personnelUniqueCode = personnelUniqueCode;
    }

    /*
     * 微信公众号openid
     * */
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getEthnicitys() {
        return ethnicitys;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    /**
     * 主要用于接收根据用户查询部门的code码，拼接组成的所在部门数组
     */
    private String[]   departmentsarr;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String[] getDepartmentsarr() {
        return departmentsarr;
    }

    public void setDepartmentsarr(String[] departmentsarr) {
        this.departmentsarr = departmentsarr;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getOrganizitionId() {
        return organizitionId;
    }

    public void setOrganizitionId(String organizitionId) {
        this.organizitionId = organizitionId;
    }

    public void setEthnicitys(String ethnicitys) {
        this.ethnicitys = ethnicitys;
    }

    public String getIdenticationInfos() {
        return identicationInfos;
    }

    public void setIdenticationInfos(String identicationInfos) {
        this.identicationInfos = identicationInfos;
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

    public String getStudentLevels() {
        return studentLevels;
    }

    public void setStudentLevels(String studentLevels) {
        this.studentLevels = studentLevels;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getDepartmentss() {
        return departmentss;
    }

    public void setDepartmentss(String departmentss) {
        this.departmentss = departmentss;
    }

    public String getIdentityTypeCode() {
        return identityTypeCode;
    }

    public void setIdentityTypeCode(String identityTypeCode) {
        this.identityTypeCode = identityTypeCode;
    }

    public String getOutPersonInfomation() {
        return outPersonInfomation;
    }

    public void setOutPersonInfomation(String outPersonInfomation) {
        this.outPersonInfomation = outPersonInfomation;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenders() {
        return genders;
    }

    public void setGenders(String genders) {
        this.genders = genders;
    }

    public List<PersonFaceInfomationTable> getPersonFaceInfomationTable() {
        return personFaceInfomationTable;
    }

    public void setPersonFaceInfomationTable(List<PersonFaceInfomationTable> personFaceInfomationTable) {
        this.personFaceInfomationTable = personFaceInfomationTable;
    }

    public PersonFaceInfomationTable getPersonFaceInfomationTableBean() {
        return personFaceInfomationTableBean;
    }

    public void setPersonFaceInfomationTableBean(PersonFaceInfomationTable personFaceInfomationTableBean) {
        this.personFaceInfomationTableBean = personFaceInfomationTableBean;
    }

}