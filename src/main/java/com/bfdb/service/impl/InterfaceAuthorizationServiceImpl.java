package com.bfdb.service.impl;

import com.bfdb.entity.InterfaceAuthorization;
import com.bfdb.entity.vo.DataKanDateVo;
import com.bfdb.mapper.InterfaceAuthorizationMapper;
import com.bfdb.service.InterfaceAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InterfaceAuthorizationServiceImpl extends AbstractService implements InterfaceAuthorizationService {

    @Autowired
    private InterfaceAuthorizationMapper interfaceAuthorizationMapper;


    /**
     * 查询接口授权信息
     *
     * @param interfaceExpirationDate
     * @return
     */
    @Override
    public List<InterfaceAuthorization> queryInterfaceAuthorizationList(String interfaceExpirationDate) {
        List<InterfaceAuthorization> interfaceAuthorizationList = null;

        try {
            interfaceAuthorizationList = interfaceAuthorizationMapper.queryInterfaceAuthorizationList(interfaceExpirationDate);
        } catch (Exception ex) {
            logger.error("接口授权查询失败" + ex.getMessage());
        }
        return interfaceAuthorizationList;
    }

    /**
     * 删除授权接口信息
     * @param id
     * @return
     */
    @Override
    public int deleteInterfaceAuthorization(Integer id) {
        return interfaceAuthorizationMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改授权接口信息
     * @param interfaceAuthorization
     * @return
     */
    @Override
    public int updateInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization) {
        return interfaceAuthorizationMapper.updateByPrimaryKeySelective(interfaceAuthorization);
    }

    /**
     * 新增授权接口信息
     * @param interfaceAuthorization
     * @return
     */
    @Override
    public int insertInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization) {
        return interfaceAuthorizationMapper.insertSelective(interfaceAuthorization);
    }

    @Override
    public List<InterfaceAuthorization> getInterfaceAuthorizationList(Map<String, Object> map) {
        return interfaceAuthorizationMapper.getInterfaceAuthorizationList(map);
    }

    @Override
    public int getInterfaceAuthorizationListTotal(Map<String, Object> parmMap) {
        return interfaceAuthorizationMapper.getInterfaceAuthorizationListTotal(parmMap);
    }

    @Override
    public List<DataKanDateVo> getCallNumberByCallTime(String callTime) {
        return interfaceAuthorizationMapper.getCallNumberByCallTime(callTime);
    }

    @Override
    public Integer getCallNumberByCallTimeAndCallStatus(Map<String, Object> parmMap) {
        return interfaceAuthorizationMapper.getCallNumberByCallTimeAndCallStatus(parmMap);
    }

    @Override
    public Integer getCallNumberByCallTimeAndUserId(Map<String, Object> parmMap) {
        return interfaceAuthorizationMapper.getCallNumberByCallTimeAndUserId(parmMap);
    }

    @Override
    public List<InterfaceAuthorization> getCallNumberAndUserName(String callTime) {
        return interfaceAuthorizationMapper.getCallNumberAndUserName(callTime);
    }

    @Override
    public List<InterfaceAuthorization> getCallNumberAndCallStatus(String callTime) {
        return interfaceAuthorizationMapper.getCallNumberAndCallStatus(callTime);
    }

    /**
     * 新增失败接口调用情况
     */
    @Override
    public void insertErrInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization) {
        interfaceAuthorizationMapper.insertSelective( interfaceAuthorization );
    }

    /**
     * 新增成功接口调用情况
     */
    @Override
    public void insertSuccessInterfaceAuthorization(InterfaceAuthorization interfaceAuthorization) {
        interfaceAuthorizationMapper.insertSelective( interfaceAuthorization );
    }

    @Override
    public Integer getAllCallNumber() {
        return interfaceAuthorizationMapper.getAllCallNumber();
    }

    @Override
    public Integer getCRUDAllCallNumber(String interfaceName) {
        return interfaceAuthorizationMapper.getCRUDAllCallNumber(interfaceName);
    }

    @Override
    public Integer getAllPersonTableTotal() {
        return interfaceAuthorizationMapper.getAllPersonTableTotal();
    }

    @Override
    public Integer getAllPersonByDataSource(String dataSource) {
        return interfaceAuthorizationMapper.getAllPersonByDataSource(dataSource);
    }

    @Override
    public Integer getAllPersonByDataSourceLike(String s) {
        return interfaceAuthorizationMapper.getAllPersonByDataSourceLike(s);
    }

}
