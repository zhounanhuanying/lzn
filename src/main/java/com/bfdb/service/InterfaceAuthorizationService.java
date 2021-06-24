package com.bfdb.service;

import com.bfdb.entity.InterfaceAuthorization;
import com.bfdb.entity.vo.DataKanDateVo;

import java.util.List;
import java.util.Map;

public interface InterfaceAuthorizationService {

    List<InterfaceAuthorization> queryInterfaceAuthorizationList(String interfaceExpirationDate);


    int deleteInterfaceAuthorization(Integer id);

    int updateInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization);

    int insertInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization);

    List<InterfaceAuthorization> getInterfaceAuthorizationList(Map<String, Object> map);

    int getInterfaceAuthorizationListTotal(Map<String, Object> parmMap);

    List<DataKanDateVo> getCallNumberByCallTime(String callTime);

    Integer getCallNumberByCallTimeAndCallStatus(Map<String, Object> parmMap);

    Integer getCallNumberByCallTimeAndUserId(Map<String, Object> parmMap);

    List<InterfaceAuthorization> getCallNumberAndUserName(String callTime);

    List<InterfaceAuthorization> getCallNumberAndCallStatus(String callTime);

    void insertErrInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization);

    void insertSuccessInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization);

    Integer getAllCallNumber();

    Integer getCRUDAllCallNumber(String interfaceName);

    Integer getAllPersonTableTotal();

    Integer getAllPersonByDataSource(String dataSource);

    Integer getAllPersonByDataSourceLike(String s);
}
