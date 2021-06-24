package com.bfdb.mapper;

import com.bfdb.entity.CommonAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CommonUrlMapper继承基类
 */
@Mapper
public interface CommonUrlMapper extends MyBatisBaseDao<CommonAddress, Integer> {

    List<CommonAddress> selectByList();
    int insertSelective(CommonAddress commonAddress);

    int selectMaxSort();

    CommonAddress selectByCommonUrl(CommonAddress commonAddress);
}