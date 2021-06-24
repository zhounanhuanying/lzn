package com.bfdb.mapper;

import com.bfdb.entity.AnonymousAddress;
import com.bfdb.entity.CommonAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AnonymousUrlMapper继承基类
 */
@Mapper
public interface AnonymousUrlMapper extends MyBatisBaseDao<AnonymousAddress, Integer> {

    List<AnonymousAddress> selectAnonymousUrlList();

    int insertSelective(AnonymousAddress anonymousAddress);

    AnonymousAddress selectByAnonymousUrl(AnonymousAddress anonymousAddress);

}