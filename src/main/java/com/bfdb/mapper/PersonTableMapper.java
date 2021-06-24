package com.bfdb.mapper;

import com.bfdb.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * PersonNewTableMapper继承基类
 */
@Mapper
public interface PersonTableMapper extends MyBatisBaseDao<PersonTable, Integer> {

    //查询人员信息
    List<PersonTable> queryPersonTableList(Map<String, Object> map);
    //查询人员条数
    int dataCount(PersonTable personNewTable);
    //删除人员信息
    int deletePersonTableByUserId(Integer[] personCodes);
    //人员信息导出excel表格
    List<PersonTable> getData(Map<String,Object> map);

    List<PersonFaceInfomationTable> queryPersonFaceInfomationTableByPersonId(Integer personId);

    PersonTable queryPersonTableByIdentityNo(String identityNo);

    void deletePersontableByPersonId(Integer[] personId);

    void updateGradeAndStudentLevlByPersonId(Integer personId);
    //根据人员id查询人员信息
    PersonTable selectPersonTableBypersonId(Integer personid);

    //根据人员身份证号查询人员信息
    PersonTable selectPersonTableByIdentityNo(String identityNo);
    //根据人员身份证号修改人员信息
    int updateByPrimaryKeyIdentityNo(PersonTable personNewTable);

    PersonTable selectPersonTableByIdentityTypeCode(String identityTypeCode);

    PersonTable selectByPersonId(Integer personId);
   //查询教工号或学号或一卡通号是否已存在
    PersonTable selectByIdentityTypeCode(String identityTypeCode);

    Integer getTotal();

    PersonTable selectPersonTableByopenId(String openId);

    List<PersonTable> selectPersonTableListByopenId(String code);
    //根据字典表中子节点的code码查询人员信息
    List<PersonTable>  selectPersonTableByDepartments(PersonTable dicCode);

    PersonTable SelectDeletePersonId(ExcelPersonDelete excelPersonDelete);

    PersonTable SelectUpdatePersonId(ExcelPersonUpdate excelPersonUpdate);

    PersonTable SelectUpdatePersonIdByIdentityNoOrIdentityTypeCode(ExcelPersonUpdate excelPersonUpdate);
    //根据人员姓名、身份证号、还有一卡通号，去查询
    List<PersonTable> selectByPersonNameIdentityNoIdentity(PersonTable personTable);
    //根据当前personCode去查询该人员信息
    PersonTable selectByPersonCode(String personCode);

    List<PersonTable> queryPersonTableListByIdentification(Map<String, Object> map);

    int dataCountByIdentification(Map<String, Object> map);

    List<BasePark> getPersonByParkId(PersonTable personTable);

    List<PersonTable> getPersonTableByorgId(BaseOrganizition personTable);

    int updateByPrimaryKeySelective (PersonTable personTable);

}