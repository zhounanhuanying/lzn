package com.bfdb.controller;


import com.bfdb.config.Constant;
import com.bfdb.entity.DataDeliveryAddressTable;
import com.bfdb.entity.PageInfo;
import com.bfdb.service.DataDeliveryAddressTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据推送地址
 */
@RestController
public class DataDeliveryAddressTableController extends AbstractController {

    @Autowired
    private DataDeliveryAddressTableService dataDeliveryAddressTableService;

    /**
     * 新增数据推送地址
     *
     * @param dataDeliveryAddressTable
     * @return
     */
    @RequestMapping(value = "/dataDeliveryAddress", method = RequestMethod.POST)
    public Map<String, Object> insertDataDeliveryAddressTable(@RequestBody DataDeliveryAddressTable dataDeliveryAddressTable) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);

        try {

            int i = dataDeliveryAddressTableService.insertDataDeliveryAddressTable(dataDeliveryAddressTable);
            if (i != 0) {
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
            }

        } catch (Exception ex) {
            logger.error("新增数据推送地址失败" + ex.getMessage());
        }
        return resultMap;
    }


    /**
     * 查询数据推送信息
     *
     * @param deliveryTime //推送时间
     * @return
     */
    @RequestMapping(value = "/dataDeliveryAddress/{deliveryTime}", method = RequestMethod.GET)
    public Map<String, Object> queryDataDeliveryAddressList(HttpServletResponse response,
                                                            @PathVariable("deliveryTime") String deliveryTime,
                                                            PageInfo pageInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);
        //返回结果集
        List<DataDeliveryAddressTable> dataDeliveryAddressTableList = null;

        try {
            dataDeliveryAddressTableList = dataDeliveryAddressTableService.queryDataDeliveryAddressList(deliveryTime, pageInfo);
            //根据推送时间查询推送信息总条数
            int totalCount = dataDeliveryAddressTableService.selectCount(deliveryTime);

            //判断结果集和条数的是否为空
            if (dataDeliveryAddressTableList.size() > 0 && totalCount > 0) {
                resultMap.put("dataDeliveryAddressTableList", dataDeliveryAddressTableList);
                resultMap.put("totalCount", totalCount);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
//                ResponseUtil.responseOutWithJson(response,new ResponseResult(Constant.RESPONSE_SUCCESS_CODE, Constant.RESPONSE_SUCCESS_DESCRIPTION, dataDeliveryAddressTableList));
            }

        } catch (Exception ex) {
            logger.error("查询数据推送信息失败" + ex.getMessage());
//            ResponseUtil.responseOutWithJson(response,new ResponseResult(Constant.RESPONSE_FAIL_CODE, Constant.RESPONSE_FAIL_DESCRIPTION,null));
        }
        return resultMap;
    }

    /**
     * 删除数据推送信息
     *
     * @param id //数据推送id
     * @return
     */
    @RequestMapping(value = "/dataDeliveryAddress/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteDataDeliveryAddress(HttpServletResponse response, @PathVariable("id") Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);

        try {

            int i = dataDeliveryAddressTableService.deleteDataDeliveryAddress(id);

            if (i != 0) {
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
            }

        } catch (Exception ex) {
            logger.error("删除数据推送信息失败" + ex.getMessage());
        }
        return resultMap;
    }

    /**
     * 修改数据推送地址
     *
     * @param dataDeliveryAddressTable
     * @return
     */
    @RequestMapping(value = "/dataDeliveryAddress", method = RequestMethod.PUT)
    public Map<String, Object> updateDataDeliveryAddressTable(@RequestBody DataDeliveryAddressTable dataDeliveryAddressTable) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);

        try {

            int i = dataDeliveryAddressTableService.updateDataDeliveryAddressTable(dataDeliveryAddressTable);
            if (i != 0) {
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
            }

        } catch (Exception ex) {
            logger.error("修改数据推送地址失败" + ex.getMessage());
        }
        return resultMap;
    }

}
