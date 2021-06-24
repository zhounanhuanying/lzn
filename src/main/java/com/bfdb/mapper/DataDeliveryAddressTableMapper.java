package com.bfdb.mapper;

import com.bfdb.entity.DataDeliveryAddressTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * DataDeliveryAddressTableMapper继承基类
 */
@Mapper
public interface DataDeliveryAddressTableMapper extends MyBatisBaseDao<DataDeliveryAddressTable, Integer> {

    List<DataDeliveryAddressTable> queryDataDeliveryAddressList(Map<String, Object> deliveryTime);

    int selectCount(String deliveryTime);

}