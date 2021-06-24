package com.bfdb.mapper;


import com.bfdb.entity.PersonnelInterface;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * PersonnelInterfaceMapper继承基类
 */
@Mapper
public interface PersonnelInterfaceMapper extends MyBatisBaseDao<PersonnelInterface, Integer> {
    //查询接口管理全部
    List<PersonnelInterface> selectPersonnelInterfaceAll();

}