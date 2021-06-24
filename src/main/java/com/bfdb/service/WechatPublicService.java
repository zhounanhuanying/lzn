package com.bfdb.service;

import com.bfdb.entity.Datas;
import com.bfdb.entity.WechatPublic;

import java.io.IOException;
import java.util.Map;

/**
 * @author lsq
 * @version 1.0
 * @description
 * @createTime 2020/8/17 11:38
 */
public interface WechatPublicService {

    Map<String, Object> insertwechatPublic(Datas datas);

    WechatPublic selectwechatPublic();
}
