package com.bfdb.mapper;

import com.bfdb.entity.WechatPublic;
import org.apache.ibatis.annotations.Mapper;

/**
 * WechatPublicMapper继承基类
 */
@Mapper
public interface WechatPublicMapper extends MyBatisBaseDao<WechatPublic, Integer> {
//    新增微信配置图片
    int insertSelective(WechatPublic wechatPublic);
//遍历微信公众号配置
    WechatPublic selectwechatPublic();
}