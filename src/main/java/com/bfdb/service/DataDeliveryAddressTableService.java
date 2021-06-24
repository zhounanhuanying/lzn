package com.bfdb.service;

import com.bfdb.entity.DataDeliveryAddressTable;
import com.bfdb.entity.PageInfo;

import java.util.List;

public interface DataDeliveryAddressTableService {

    int insertDataDeliveryAddressTable(DataDeliveryAddressTable dataDeliveryAddressTable);

    List<DataDeliveryAddressTable> queryDataDeliveryAddressList(String deliveryTime, PageInfo pageInfo);

    int deleteDataDeliveryAddress(Integer id);

    int updateDataDeliveryAddressTable(DataDeliveryAddressTable dataDeliveryAddressTable);

    int selectCount(String deliveryTime);

}
