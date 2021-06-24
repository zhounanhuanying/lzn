package com.bfdb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bfdb.entity.DataDictionary;
import com.bfdb.service.DataDictionaryService;
import com.bfdb.service.PersonnelInterfaceService;
import com.bfdb.untils.LayuiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 字典管理
 */
@Controller
@RequestMapping("/dataDictionary")
public class DataDictionaryController {
    @Autowired
    private DataDictionaryService dataDictionaryService;


    @Autowired
    PersonnelInterfaceService personnelInterfaceService;

    /**
     * 查询字典列表
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDataDictionaryList")
    public Map<String,Object> getDataDictionaryList(@RequestParam Map<String, Object> params){
        int totalCount = dataDictionaryService.dataCount(params);
        Map<String, Object> rePutParams = LayuiUtil.rePutParams(params);
        List<DataDictionary> dataDictionaryList = dataDictionaryService.getDataDictionaryList(rePutParams);
        return LayuiUtil.dataList(totalCount, dataDictionaryList);
    }
    @ResponseBody
    @RequestMapping(value = "/getDataDictionaryListNoLogBook")
    public Map<String,Object> getDataDictionaryListNoLogBook(@RequestParam Map<String, Object> params){
        int totalCount = dataDictionaryService.dataCount(params);
        Map<String, Object> rePutParams = LayuiUtil.rePutParams(params);
        List<DataDictionary> dataDictionaryList = dataDictionaryService.getDataDictionaryList(rePutParams);
        return LayuiUtil.dataList(totalCount, dataDictionaryList);
    }

    /**
     * 更新数据字典
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateDataDictionary")
    public Map<String,Object> updateDataDictionary(@RequestParam Map<String, Object> params){
        int i = dataDictionaryService.updateDataDictionary(params);
        return LayuiUtil.dataUpdate(i);
    }

    /**
     * 查询字典信息
     * @param request
     * @return
     */
//    @ResponseBody
//    @RequestMapping("/getDictionaryType")
//    public JSONObject getDictionaryType(HttpServletRequest request){
////        String[] dicTypeList= dataDictionaryService.getAllDicType();
//        //查询字典名称和字典类型code
//        List<DataDictionary> dicTypeList=dataDictionaryService.getAllDicType();
//        JSONObject ServerJson = new JSONObject();
//        //循环遍历信息
//        for (DataDictionary dataDictionary : dicTypeList) {
//            ServerJson.put(dataDictionary.getDicCode(), dataDictionary.getDicName());
//        }
//        return ServerJson;
//    }

    /**
     * 添加数据字典
     * @param dataDictionary
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertDataDictionary")
    public Map<String,Integer> insertDataDictionary(DataDictionary dataDictionary){
        Map<String,Integer> stringObjectMap=new HashMap<>(  );
        int i = dataDictionaryService.insertDataDictionary(dataDictionary);
        stringObjectMap.put( "code", i);
        return stringObjectMap;
    }

    /**
     * 添加基础类型
     * @param dataDictionary
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertDataDictionaryType")
    public Map<String,Object> insertDataDictionaryType(DataDictionary dataDictionary){
        int i = dataDictionaryService.insertDataDictionaryType(dataDictionary);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("code",i);
        returnMap.put("msg", "success");
        return returnMap;
    }

    /**
     *
     * 修改字典基础类型
     * @param dataDictionary
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updataDataDictionaryType",method = RequestMethod.PUT)
    public Map<String,Object> updataDataDictionaryType(DataDictionary dataDictionary){
        return dataDictionaryService.updataDataDictionaryType(dataDictionary);
    }

    /**
     * 删除数据字典
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteDataDictionaryById")
    public Map<String,Object> deleteDataDictionaryById(@RequestParam Map<String, Object> params){
        String dicId = params.get("dicId").toString();
        int i = dataDictionaryService.deleteDataDictionaryById(dicId);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("code",i);
        returnMap.put("msg", "success");
        return returnMap;
    }

    /**
     * 批量删除数据字典
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteDictionaryListBtn")
    public Map<String,Object> deleteDictionaryListBtn(HttpServletRequest request){
        String  dictionaryListString = request.getParameter("data");
        List<DataDictionary> dictionaryList = JSON.parseArray(dictionaryListString, DataDictionary.class);
        int i = dataDictionaryService.deleteDictionaryList(dictionaryList);
        return LayuiUtil.dataDelete(i);
    }

    /**
     * 根据名称查询字典
     * @param name
     * @return
     */
    @RequestMapping(value = "/queryDataDictionaryByName")
    @ResponseBody
    public String queryDataDictionaryByName(String name){
        String dicCode = dataDictionaryService.queryDataDictionaryByName(name);
        return dicCode;
    }

    /**
     * 根据编码查询字典
     * @param dicCode
     * @return
     */
    @RequestMapping(value = "/queryDataDictionaryByCode")
    @ResponseBody
    public String queryDataDictionaryByCode(String dicCode){
        String dicName = dataDictionaryService.queryDataDictionaryByCode(dicCode);
        return dicName;
    }
}
