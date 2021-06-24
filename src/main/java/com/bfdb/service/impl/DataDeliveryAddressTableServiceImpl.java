package com.bfdb.service.impl;

import com.bfdb.entity.DataDeliveryAddressTable;
import com.bfdb.entity.PageInfo;
import com.bfdb.mapper.DataDeliveryAddressTableMapper;
import com.bfdb.service.DataDeliveryAddressTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class DataDeliveryAddressTableServiceImpl extends AbstractService implements DataDeliveryAddressTableService {

    @Autowired
    private DataDeliveryAddressTableMapper dataDeliveryAddressTableMapper;


    /**
     * 新增推送数据地址
     *
     * @param dataDeliveryAddressTable
     * @return
     */
    @Override
    public int insertDataDeliveryAddressTable(DataDeliveryAddressTable dataDeliveryAddressTable) {
        return dataDeliveryAddressTableMapper.insert(dataDeliveryAddressTable);
    }

    /**
     * 查询数据推送信息
     *
     * @param deliveryTime
     * @param pageInfo
     * @return
     */
    @Override
    public List<DataDeliveryAddressTable> queryDataDeliveryAddressList(String deliveryTime, PageInfo pageInfo) {
        List<DataDeliveryAddressTable> dataDeliveryAddressTableList = null;
        //定义分页信息
        int pageNo = pageInfo.getPage();
        int pageSize = pageInfo.getEvePageRecordCnt();
        int pageNos = (pageNo - 1) * pageSize;
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            map.put("pageSize", pageSize);
            map.put("pageNo", pageNos);
            map.put("deliveryTime", deliveryTime);

            dataDeliveryAddressTableList = dataDeliveryAddressTableMapper.queryDataDeliveryAddressList(map);
        } catch (Exception ex) {
            logger.error("查询数据推送信息失败" + ex.getMessage());
        }
        return dataDeliveryAddressTableList;
    }

    /**
     * 删除数据推送信息
     *
     * @param id
     * @return
     */
    @Override
    public int deleteDataDeliveryAddress(Integer id) {
        return dataDeliveryAddressTableMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改数据推送信息
     *
     * @param dataDeliveryAddressTable
     * @return
     */
    @Override
    public int updateDataDeliveryAddressTable(DataDeliveryAddressTable dataDeliveryAddressTable) {
        return dataDeliveryAddressTableMapper.updateByPrimaryKeySelective(dataDeliveryAddressTable);
    }

    /**
     * 根据推送时间查询数据推送条数
     *
     * @param deliveryTime
     * @return
     */
    @Override
    public int selectCount(String deliveryTime) {
        return dataDeliveryAddressTableMapper.selectCount(deliveryTime);
    }
}
