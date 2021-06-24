package com.bfdb.service.impl;

import com.bfdb.entity.AnonymousAddress;
import com.bfdb.entity.CommonAddress;
import com.bfdb.mapper.AnonymousUrlMapper;
import com.bfdb.service.AnonymousUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnonymousUrlServiceImpl implements AnonymousUrlService {
    @Autowired
    private AnonymousUrlMapper anonymousUrlMapper;

    @Override
    public int insertSelective(AnonymousAddress anonymousAddress) {
        return anonymousUrlMapper.insertSelective(anonymousAddress);
    }

    @Override
    public AnonymousAddress selectByAnonymousUrl(AnonymousAddress anonymousAddress) {
        return anonymousUrlMapper.selectByAnonymousUrl(anonymousAddress);
    }

    @Override
    public int updateByPrimaryKey(AnonymousAddress anonymousAddress) {
        return anonymousUrlMapper.updateByPrimaryKey(anonymousAddress);
    }

}
