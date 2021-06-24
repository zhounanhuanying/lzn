package com.bfdb.service.impl;

import com.bfdb.entity.PersonDataOperationTable;
import com.bfdb.entity.vo.PageInfoVo;
import com.bfdb.entity.vo.PersonDataOperationTableVo;
import com.bfdb.mapper.PersonDataOperationTableMapper;
import com.bfdb.service.PersonDataOperationTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonDataOperationTableServiceImpl implements PersonDataOperationTableService {

    @Autowired
    PersonDataOperationTableMapper personDataOperationTableMapper;

    /**
     * 添加人员更新日志
     *
     * @param personDataOperationTable
     */
    @Override
    public void insertPersonDataOperationTable(PersonDataOperationTable personDataOperationTable) {
        personDataOperationTableMapper.insertSelective( personDataOperationTable );
    }

    /**
     * 查询人员数据更新日志表
     *
     * @param pageInfoVo
     * @param personDataOperationTableVo
     * @return
     */
    @Override
    public List<PersonDataOperationTable> queryPersonDataOperationTableList(PageInfoVo pageInfoVo, PersonDataOperationTableVo personDataOperationTableVo) {
        List<PersonDataOperationTable> personDataOperationTableList = null;
        //定义分页信息
        int rowNum = pageInfoVo.getRowNum();
        int pageSize = pageInfoVo.getPageSize();
        int pageNos = (rowNum - 1) * pageSize;
        Map<String, Object> map = new LinkedHashMap<>();
        //主键id不为空时，根据主键id默认查询后面200条数据
        if( personDataOperationTableVo.getId()!=null){
            map.put( "limit", 200 );
            map.put( "page", 0);
        }else {
            map.put( "limit", pageSize );
            map.put( "page", pageNos);
        }
        map.put( "personCode", personDataOperationTableVo.getPersoncode() );
        map.put("dateTimeStart", personDataOperationTableVo.getDateTimeStart());
        map.put("dateTimeEnd", personDataOperationTableVo.getDateTimeEnd());
        map.put("id", personDataOperationTableVo.getId());
        personDataOperationTableList = personDataOperationTableMapper.queryPersonDataOperationTableList( map );
        //查询人员集合信息
        return personDataOperationTableList;
    }

    /**
     * 查询条数
     *
     * @param personDataOperationTableVo
     * @return
     */
    @Override
    public int dataCount(PersonDataOperationTableVo personDataOperationTableVo) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put( "personCode", personDataOperationTableVo.getPersoncode() );
        map.put("dateTimeStart", personDataOperationTableVo.getDateTimeStart());
        map.put("dateTimeEnd", personDataOperationTableVo.getDateTimeEnd());
        map.put("id", personDataOperationTableVo.getId());
        return personDataOperationTableMapper.dataCount( map );
    }
}
