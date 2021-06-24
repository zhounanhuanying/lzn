package com.bfdb.service;

import com.bfdb.entity.AnonymousAddress;
import com.bfdb.entity.CommonAddress;

public interface AnonymousUrlService {
    int insertSelective(AnonymousAddress anonymousAddress);

    AnonymousAddress selectByAnonymousUrl(AnonymousAddress anonymousAddress);

    int updateByPrimaryKey(AnonymousAddress anonymousAddress);
}
