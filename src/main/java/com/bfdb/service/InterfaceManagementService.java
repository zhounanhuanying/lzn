package com.bfdb.service;

import com.bfdb.entity.DataDictionary;
import com.bfdb.entity.PersonFaceInfomationTable;
import com.bfdb.entity.PersonTable;
import com.bfdb.entity.User;
import com.bfdb.entity.vo.PageInfoVo;
import com.bfdb.entity.vo.PersonTableVo;

import java.util.List;

public interface InterfaceManagementService {

    int insertPersonTableList(List<PersonTableVo> personTableList, User user);

    int insertOrUpdatePersonTableList(List<PersonTableVo> personTableList, User user);

    List<PersonFaceInfomationTable> queryPersonFaceInfomationTableByPersonId(Integer personId);

    int dataCount(PersonTable personNewTable);
    //根据字典类型和code获取名称
    DataDictionary queryDicNameByDicTypeAndDicCode(DataDictionary dataDictionary);

    int updatePersonTableList(List<PersonTableVo> personTableList, User user);
    //根据personCode查询用户信息
    PersonTable selectPersonTable(String personCode);

    List<PersonTable> queryPersonTableList(PersonTable personTable, PageInfoVo pageInfo);
     //根据人员类型编码查询人员类型
    List<DataDictionary> selectPersonTypeList(String identicationinfo);

    PersonTable selectPersonTableByIdentityTypeCode(String identityTypeCode);
}
