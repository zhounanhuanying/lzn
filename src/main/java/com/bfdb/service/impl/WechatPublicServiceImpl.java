package com.bfdb.service.impl;

import com.bfdb.entity.Datas;
import com.bfdb.entity.WechatPublic;
import com.bfdb.mapper.WechatPublicMapper;
import com.bfdb.service.WechatPublicService;
import com.bfdb.untils.Base64Utils;
import com.bfdb.untils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsq
 * @version 1.0
 * @description
 * @createTime 2020/8/17 11:40
 */
@Service
public class WechatPublicServiceImpl implements WechatPublicService {
    @Autowired
    private WechatPublicMapper wechatPublicMapper;

    @Override
    public Map<String, Object> insertwechatPublic(Datas datas){
        try {
            WechatPublic wechatPublic = datas.getWechatPublic();
//            查询微信公众号配置是否存在
            WechatPublic wechatPublic1 = wechatPublicMapper.selectwechatPublic();
            Map<String, Object> map = new HashMap<>();
            String filePath = FileUtils.getProperties("/application.properties", "filePath");
            //            获取删除的图片数组
            datas.getImgs().forEach(p->{
                    File file = new File(filePath+p);
                    // 路径不为空则进行删除     
                    if (file.exists()) {
                        file.delete();
                    }
            });
//            将base64转为正常图片路径

                if (StringUtils.isNotBlank(wechatPublic.getQrCodePath())) {
                    String s = Base64Utils.baseurlPhotos(wechatPublic.getQrCodePath());
                    if (isBase64Encode(s)) {
                        wechatPublic.setQrCodePath(Base64Utils.generateImage(s));
                    }
                }
                if (StringUtils.isNotBlank(wechatPublic.getCollectionEntryPath())) {
                    String s = Base64Utils.baseurlPhotos(wechatPublic.getCollectionEntryPath());
                    if (isBase64Encode(Base64Utils.baseurlPhotos(s))) {
                        wechatPublic.setCollectionEntryPath(Base64Utils.generateImage(s));
                    }
                }
                if (StringUtils.isNotBlank(wechatPublic.getFaceCollectionPagePath())) {
                    String s = Base64Utils.baseurlPhotos(wechatPublic.getFaceCollectionPagePath());
                    if (isBase64Encode(Base64Utils.baseurlPhotos(s))) {
                        wechatPublic.setFaceCollectionPagePath(Base64Utils.generateImage(s));
                    }
                }
//微信配置不存在，调用新增 否则调用修改
            if (wechatPublic1 == null) {
                map.put("code",wechatPublicMapper.insertSelective(wechatPublic));
            } else {
                wechatPublic.setId(wechatPublic1.getId());
                map.put("code",wechatPublicMapper.updateByPrimaryKey(wechatPublic));
            }
            return map;
        }catch (IOException i){
            return null;
        }

    }
//遍历微信公众号配置
    @Override
    public WechatPublic selectwechatPublic() {
        return wechatPublicMapper.selectwechatPublic();
    }

    /**
     * 判断是否是base64编码
     *
     * @param faceAddress
     * @return
     */
    private boolean isBase64Encode(String faceAddress) {
        if (faceAddress == null || faceAddress.length() == 0) {
            return false;
        }
        if (faceAddress.length() % 4 != 0) {
            return false;
        }
        char[] chrs = faceAddress.toCharArray();
        for (char chr : chrs) {
            if ((chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9') ||
                    chr == '+' || chr == '/' || chr == '=') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
