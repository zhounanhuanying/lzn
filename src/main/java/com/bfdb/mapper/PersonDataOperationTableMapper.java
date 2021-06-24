package com.bfdb.mapper;


import com.bfdb.entity.PersonDataOperationTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * PersonDataOperationTableMapper继承基类
 */
@Mapper
public interface PersonDataOperationTableMapper extends MyBatisBaseDao<PersonDataOperationTable, Integer> {
    //查询人员数据更新日志表
    List<PersonDataOperationTable> queryPersonDataOperationTableList(Map<String, Object> map);
    //查询人员数据总条数
    int dataCount(Map<String, Object> map);
}