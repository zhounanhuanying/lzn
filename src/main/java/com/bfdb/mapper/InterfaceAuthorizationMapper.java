package com.bfdb.mapper;

import com.bfdb.entity.InterfaceAuthorization;
import com.bfdb.entity.vo.DataKanDateVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * InterfaceAuthorizationMapper继承基类
 */
@Mapper
public interface InterfaceAuthorizationMapper extends MyBatisBaseDao<InterfaceAuthorization, Integer> {

    List<InterfaceAuthorization> queryInterfaceAuthorizationList(String interfaceExpirationDate);

    List<InterfaceAuthorization> getInterfaceAuthorizationList(Map<String, Object> map);

    int getInterfaceAuthorizationListTotal(Map<String, Object> parmMap);

    List<DataKanDateVo> getCallNumberByCallTime(String callTime);

    Integer getCallNumberByCallTimeAndCallStatus(Map<String, Object> parmMap);

    Integer getCallNumberByCallTimeAndUserId(Map<String, Object> parmMap);

    List<InterfaceAuthorization> getCallNumberAndUserName(String callTime);

    List<InterfaceAuthorization> getCallNumberAndCallStatus(String callTime);

    Integer getAllCallNumber();

    Integer getCRUDAllCallNumber(String interfaceName);

    Integer getAllPersonTableTotal();

    Integer getAllPersonByDataSource(String dataSource);

    Integer getAllPersonByDataSourceLike(String s);
}