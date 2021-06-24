package com.bfdb.service;

import com.bfdb.entity.PersonDataOperationTable;
import com.bfdb.entity.vo.PageInfoVo;
import com.bfdb.entity.vo.PersonDataOperationTableVo;

import java.util.List;

public interface PersonDataOperationTableService {
    //添加人员更新日志
    void insertPersonDataOperationTable(PersonDataOperationTable personDataOperationTable);
    //查询人员数据更新日志表
    List<PersonDataOperationTable> queryPersonDataOperationTableList(PageInfoVo pageInfoVo, PersonDataOperationTableVo personDataOperationTableVo);
   //查询条数
    int dataCount(PersonDataOperationTableVo personDataOperationTableVo);

}
