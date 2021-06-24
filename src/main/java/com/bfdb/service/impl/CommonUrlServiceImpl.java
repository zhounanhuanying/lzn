package com.bfdb.service.impl;

import com.bfdb.entity.CommonAddress;
import com.bfdb.mapper.CommonUrlMapper;
import com.bfdb.service.CommonUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonUrlServiceImpl implements CommonUrlService {

    @Autowired
    CommonUrlMapper commonUrlMapper;

    @Override
    public List<CommonAddress> selectByList() {
        return commonUrlMapper.selectByList();
    }

    @Override
    public int insertSelective(CommonAddress commonAddress) {
        return commonUrlMapper.insertSelective(commonAddress);
    }

    @Override
    public int selectMaxSort() {
        return commonUrlMapper.selectMaxSort();
    }

    @Override
    public CommonAddress selectByCommonUrl(CommonAddress commonAddress) {
        return commonUrlMapper.selectByCommonUrl(commonAddress);
    }

    @Override
    public int updateByPrimaryKey(CommonAddress commonAddress1) {
        return commonUrlMapper.updateByPrimaryKeySelective(commonAddress1);
    }
}
