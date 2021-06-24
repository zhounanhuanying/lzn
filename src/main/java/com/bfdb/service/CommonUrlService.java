package com.bfdb.service;

import com.bfdb.entity.CommonAddress;

import java.util.List;

public interface CommonUrlService {
    List<CommonAddress> selectByList();
    int insertSelective(CommonAddress commonAddress);
    int selectMaxSort();

    CommonAddress selectByCommonUrl(CommonAddress commonAddress);

    int updateByPrimaryKey(CommonAddress commonAddress1);
}
