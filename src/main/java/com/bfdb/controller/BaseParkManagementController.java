package com.bfdb.controller;

import com.alibaba.fastjson.JSON;
import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.service.BaseParkManagementService;
import com.bfdb.untils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 园区管理
 * @author zhaojie
 * @createDate 2021/5/27
 */
@RestController
@RequestMapping("/basePark")
public class BaseParkManagementController extends AbstractController{

    @Autowired
    private BaseParkManagementService baseParkManagementService;

    /**
     * 查询园区信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getBaseParkAll", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBaseParkAll(HttpServletRequest request,String parkName) {
        Integer limit = 0;
        Integer page = 0;
        if(request.getParameter("limit") != null && request.getParameter("page") != null) {
            limit = Integer.parseInt(request.getParameter("limit"));
            //获取页数
            page = (Integer.parseInt(request.getParameter("page")) - 1) * limit;
        }
        Map<String, Object> hashmap = new LinkedHashMap<>();
        hashmap.put("limit", limit);
        hashmap.put("page", page);
        hashmap.put("isDelete",1);
        hashmap.put("parkName",parkName);
        int totalCount = baseParkManagementService.dataCount();
        List<BasePark> baseParkList = baseParkManagementService.getBaseParkList( hashmap );
        return LayuiUtil.dataList( totalCount, baseParkList );
    }

    /**
     * 园区新增信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertBasePark", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> insertBasePark(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        try {
            //获取从前端传送的值
            String datats = request.getParameter( "datats" );
            //转换成json对象
            Datas datasArray = JSON.parseObject( datats, Datas.class );
            String logo = null;
            BasePark basePark = null;
            if (datasArray != null) {
                basePark = datasArray.getBasePark();//园区基础信息
                logo = datasArray.getBaseParkImg();
            }
            if(basePark.getParkCode() != null) {
                i = baseParkManagementService.queryBaseParkByParkCode(basePark.getParkCode());
                //判断是否查到数据
                if (i == 0) {
                    if (StringUtils.isNotBlank( logo )) {
                        logo = Base64Utils.baseurlPhotos(logo);
                        logo = Base64Utils.generateImage( logo );
                    }
                    basePark.setParkLogoImage(logo);
                    Date date = new Date(System.currentTimeMillis());
                    basePark.setCreateTime(date);
                    basePark.setIsDelete(1);
                    baseParkManagementService.insertBasePark(basePark);
                    resultMap.put("code",2);
                } else {
                    resultMap.put("code", 1);
                }
            }
        } catch (Exception ex) {
            logger.error( "新增园区信息失败！！" + ex.getMessage() );
        }
        return resultMap;
    }

    /**
     * 修改园区信息
     * @param basePark
     * @return
     */
    @RequestMapping(value = "/updateBaseParkById", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> updateBaseParkById(BasePark basePark) {
        Map<String, Object> map = new HashMap<>();
        String logo = null;
        try {
            //获取图片的路径 删除原来的照片
            String filePath = Config.getPhotoUrl("filePath");
            if(StringUtils.isNotBlank(basePark.getImgs())){
                String[] split = basePark.getImgs().split("/imageEcho/");
                if (split!=null){
                    File file = new File(filePath+split[1]);
                    // 路径不为空则进行删除     
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            // 现根据当前id去查询该园区的信息
            if (StringUtils.isNotBlank(basePark.getParkLogoImage())) {
                logo = Base64Utils.baseurlPhotos(basePark.getParkLogoImage());
                //判断是否base64编码
                boolean base64Encode = isBase64Encode( logo );
                if(base64Encode){
                    logo = Base64Utils.generateImage( logo );
                }
            }
            basePark.setParkLogoImage(logo);
            //添加园区
            Date date = new Date(System.currentTimeMillis());
            basePark.setUpdateTime(date);
            baseParkManagementService.updateBaseParkById(basePark);
            map.put("code",2);
        }catch (Exception ex){
            logger.error( "修改园区信息失败！！" + ex.getMessage() );
        }
        return map;
    }
    /**
     * 判断是否是base64编码
     *
     * @param faceAddress
     * @return
     */
    public boolean isBase64Encode(String faceAddress) {
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
    /**
     * 删除园区信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBaseParkById", method = RequestMethod.DELETE)
    public Map<String, Object> deleteBaseParkById(@RequestBody(required = false) Integer[] ids) {
        int i = 0;
        i = baseParkManagementService.deleteBaseParkById( ids );
        return LayuiUtil.dataDelete( i );
    }

    /**
     * 根据园区Id查询园区名称
     * @param parkId
     * @return parkName
     */
    @RequestMapping(value = "/queryParkNameById", method = RequestMethod.GET)
    @ResponseBody
    public String queryParkNameById(String parkId){
        return baseParkManagementService.queryParkNameById(parkId);
    }
}
