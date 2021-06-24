package com.bfdb.controller;

import com.alibaba.fastjson.JSON;
import com.bfdb.entity.Datas;
import com.bfdb.entity.WechatPublic;
import com.bfdb.service.WechatPublicService;
import com.bfdb.untils.Base64Utils;
import com.bfdb.untils.FileUtils;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsq
 * @version 1.0
 * @description
 * @createTime 2020/8/17 10:07
 */
@Controller
@RequestMapping("/wechatPublic")
public class WechatPublicController {
    @Autowired
    private WechatPublicService wechatPublicService;
    /**
     * 图片上传
     *
     * @param files
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestParam(value = "file", required = false) MultipartFile files, HttpServletRequest request) throws IOException {
        Map<String, Object> map = new HashMap<>();
        OutputStream out = null;
        String newFileUrl = null;
        //对文件数组进行遍历
//        for (MultipartFile multipartFile : files) {
        //老文件名
        String originalName = files.getOriginalFilename();
        //截取老文件名的后缀
        String prefix = originalName.substring( originalName.lastIndexOf( "." ) + 1 );
        try {
            //获取配置文件中图片的路径
            String dPath = FileUtils.getProperties( "/application.properties", "filePath" );
            //调用图片路径的公共方法
            newFileUrl = Base64Utils.addressPublic( dPath );
            map.put( "code", 0 );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
//        }
        map.put( "msg", newFileUrl );
        return map;

    }

    /**
     * 新增微信公众号配置或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/insertwechatPublic", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object>  insertwechatPublic(@RequestBody Datas datas){
        return wechatPublicService.insertwechatPublic(datas);
    }
//    遍历微信公众号配置
    @RequestMapping(value = "/selectwechatPublic", method = RequestMethod.GET)
    @ResponseBody
    public WechatPublic selectwechatPublic(){
        return wechatPublicService.selectwechatPublic();
    }

}
