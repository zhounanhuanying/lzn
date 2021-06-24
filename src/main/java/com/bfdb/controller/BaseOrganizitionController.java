package com.bfdb.controller;

import com.alibaba.fastjson.JSON;
import com.bfdb.config.Constant;
import com.bfdb.entity.BaseOrganizition;
import com.bfdb.entity.BasePark;
import com.bfdb.entity.Datas;
import com.bfdb.entity.PersonTable;
import com.bfdb.service.BaseOrganizitionService;
import com.bfdb.untils.LayuiUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 组织架构管理
 * @author zhaojie
 * @createDate 2020/5/28
 */
@RestController
@RequestMapping("/baseOrganizition")
public class BaseOrganizitionController extends AbstractController {

    @Resource
    private BaseOrganizitionService baseOrganizitionService;

    /**
     * 查询组织结构tree
     * @return
     */
    @RequestMapping(value = "/getOrganizationTree", method = RequestMethod.GET)
    @ResponseBody
    public  Map<String, Object> getOrganizationTree(String parkId){
         List<BaseOrganizition> baseOrganizitionList = baseOrganizitionService.getOrganizationTree(parkId);
        return LayuiUtil.dataList(2,baseOrganizitionList);
    }

    /**
     * 查询组织架构信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getBaseOrganizitionAll", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBaseOrganizitionAll(String parkId) {
        int totalCount = baseOrganizitionService.dataCount();
        List<BasePark> baseParkList = baseOrganizitionService.getBaseOrganizitionList(parkId);
        return LayuiUtil.dataList( totalCount, baseParkList );
    }

    /**
     * 根据organizitionId组织结构id来查询人员
     * @param baseOrganizition
     * @return
     */
    @RequestMapping(value = "/getPersonTableByorgId", method = RequestMethod.GET)
    public Map<String, Object> getPersonTableByorgId(BaseOrganizition baseOrganizition) {
        List<PersonTable> list = baseOrganizitionService.getPersonTableByorgId(baseOrganizition);
        return LayuiUtil.dataList(20,list);
    }

    /**
     * 组织架构新增信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertBaseOrganizition", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> insertBaseOrganizition(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        try {
            //获取从前端传送的值
            String datats = request.getParameter( "datats" );
            //转换成json对象
            Datas datasArray = JSON.parseObject( datats, Datas.class );
            BaseOrganizition baseOrganizition = null;
            if(datasArray != null){
                baseOrganizition = datasArray.getBaseOrganizition();//组织机构信息
            }
            if(baseOrganizition.getOrgCode() != null) {
                i = baseOrganizitionService.queryBaseOrganizitionByOrgCodeCount(baseOrganizition.getOrgCode());
                //判断是否查到数据
                if (i == 0) {
                    baseOrganizition.setCreateTime(new Date());
                    baseOrganizition.setOrgType(baseOrganizition.getOrgType());
                    baseOrganizitionService.insertBaseOrganizition(baseOrganizition);
                    resultMap.put("code",2);
                } else {
                    resultMap.put("code", 1);
                }
            }
        } catch (Exception ex) {
            logger.error( "新增组织架构信息失败！！" + ex.getMessage() );
        }
        return resultMap;
    }

    /**
     * 修改组织架构信息
     * @param baseOrganizition
     * @return
     */
    @RequestMapping(value = "/updateBaseOrganizitionById", method = RequestMethod.PUT)
    @Transactional
    public Map<String, Object> updateBaseOrganizitionById(BaseOrganizition baseOrganizition) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (baseOrganizition.getOrgName().substring(0, 2).equals(" ")) {
                map.put("code", -4);
                return map;
            }

            if(baseOrganizition.getOrgCode() != null) {
                BaseOrganizition baseOrganizition1 = baseOrganizitionService.queryBaseOrganizitionByOrgCode(baseOrganizition.getOrgCode());
                //判断是否查到数据
                if (baseOrganizition1 == null || "".equals(baseOrganizition1)) {
                    //修改组织机构
                    baseOrganizition.setUpdateTime(new Date());
                    baseOrganizitionService.updateBaseOrganizitionById(baseOrganizition);
                    map.put("code", 2);
                }else if(baseOrganizition1.getOrgCode().equals(baseOrganizition.getOrgCode())){
                    baseOrganizition.setUpdateTime(new Date());
                    baseOrganizitionService.updateBaseOrganizitionById(baseOrganizition);
                    map.put("code", 2);
                }
            }else {
                map.put("code", -1);
            }
        }catch (Exception ex){
            logger.error( "修改组织架构信息失败！！" + ex.getMessage() );
        }
        return map;
    }

    /**
     * 删除组织架构信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteBaseOrganizitionById", method = RequestMethod.DELETE)
    public Integer deleteBaseOrganizitionById(String id) {
        int i = 0;
        i = baseOrganizitionService.deleteBaseOrganizitionById(Integer.parseInt(id));
        return i;
    }

    /**
     * 根据Id查询组织机构
     * @param orgId
     * @return orgName
     */
    @RequestMapping(value = "/queryOrgNameById", method = RequestMethod.GET)
    @ResponseBody
    public String queryOrgNameById(String orgId){
        String orgName = baseOrganizitionService.queryOrgNameById(orgId);
        return orgName;
    }

    /**
     * 根据名称查询父级组织机构名称
     * @param orgName
     * @return orgName
     */
    @RequestMapping(value = "/queryPorgNameByName", method = RequestMethod.GET)
    @ResponseBody
    public String queryPorgNameByName(String orgName){
        String name = baseOrganizitionService.queryPorgNameByName(orgName);
        return name;
    }
}
