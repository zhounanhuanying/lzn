package com.bfdb.service;


import com.bfdb.entity.*;

import java.util.List;
import java.util.Map;

public interface PersonTableService {

    int insertPersonNewTable(PersonTable personNewTable);

    List<PersonTable> queryPersonTableList(PersonTable personNewTable, Integer limit, Integer page);

    int dataCount(PersonTable personNewTable);

    int updateByPrimaryKeySelective(PersonTable personNewTable);

    int deletePersonTableByUserId(Integer[] personId);
    //人员信息导出excel表格
    List<PersonTable> getData(Map<String,Object> map);

    List<PersonFaceInfomationTable> queryPersonFaceInfomationTableByPersonId(Integer personId);

    PersonTable queryPersonTableByIdentityNo(String identityNo);

    void deletePersontableByPersonId(Integer[] personId);

    void updateGradeAndStudentLevlByPersonId(Integer personId);

    /**
     * 根据人员id查询人员信息
     * @param personId
     * @return
     */
    PersonTable selectPersonTableById(Integer personId);

    //根据人员身份证号查询人员信息
    PersonTable selectPersonTableByIdentityNo(String identityNo);
    //根据人员身份证号修改人员信息
    int updateByPrimaryKeyIdentityNo(PersonTable personNewTable);

    PersonTable selectPersonTableByIdentityTypeCode(String identityTypeCode);

    PersonTable selectByPersonId(Integer personId);
    //查询教工号或学号或一卡通号是否已存在
    PersonTable selectByIdentityTypeCode(String identityTypeCode);

    Integer getTotal();

    PersonTable selectPersonTableByopenId(String openid);

    List<PersonTable> selectPersonTableListByopenId(String code);
    //根据字典子节点code码查询人员信息
    List<PersonTable>  selectPersonTableByDepartments(PersonTable dicCode);

    List<PersonFaceInfomationTable> selectDataSource(String dicCode);

    PersonTable SelectDeletePersonId(ExcelPersonDelete excelPersonDelete);

    PersonTable SelectUpdatePersonId(ExcelPersonUpdate excelPersonUpdate);

    PersonTable SelectUpdatePersonIdByIdentityNoOrIdentityTypeCode(ExcelPersonUpdate excelPersonUpdate);
    //根据人员姓名、身份证号、还有一卡通号，去查询
    List<PersonTable> selectByPersonNameIdentityNoIdentity(PersonTable personTable);

    List<PersonTable> queryPersonTableListByIdentification(Map<String, Object> map);

    int dataCountByIdentification(Map<String, Object> map);

    List<BasePark> getPersonByParkId(PersonTable personTable);

    PersonTable queryPersonTableByOrgId(PersonTable personNewTable);

    ExcelPersonTable queryExcelPersonTableByOrgId(ExcelPersonTable excelPersonTable);
}
