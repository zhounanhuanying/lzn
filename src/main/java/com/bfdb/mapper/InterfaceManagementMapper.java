package com.bfdb.mapper;

import com.bfdb.entity.DataDictionary;
import com.bfdb.entity.PersonFaceInfomationTable;
import com.bfdb.entity.PersonTable;
import com.bfdb.entity.vo.PersonTableVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InterfaceManagementMapper {

    List<PersonFaceInfomationTable> queryPersonFaceInfomationTableByPersonId(Integer personId);

    DataDictionary queryDicNameByDicTypeAndDicCode(DataDictionary dataDictionary);

    int insertPersonTableList(List<PersonTableVo> personTableList);
     //修改人员基本信息
    int updatePersonTableList(@Param(value = "personTableList") List<PersonTableVo> personTableList);

    PersonTable selectPersonTable(String personCode);
    //查询未删除的人员集合信息
    List<PersonTable> queryPersonTableALLList(Map<String, Object> map);
    //查询未删除的人员条数
    int dataCountALL(PersonTable personTable);
     //教工号或学号或一卡通号查重
    PersonTable selectByIdentityTypeCode(String identityTypeCode);
    PersonTable selectByIdentityNo(String identityNo);

    PersonTable selectPersonTableByIdentityTypeCode(String identityTypeCode);

    int updatePersonTableListByidentityTypeCode(@Param(value = "personTableList") List<PersonTableVo> personTableList);
}
