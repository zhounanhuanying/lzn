package com.bfdb.controller;

import com.bfdb.config.Constant;
import com.bfdb.entity.BaseOrganizitionPerson;
import com.bfdb.entity.BasePark;
import com.bfdb.service.BaseOrganizitionPersonService;
import com.bfdb.untils.LayuiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 组织机构和人员关系管理
 * @author zhaojie
 * @createDate 2020/5/28
 */
@RestController
@RequestMapping("/BaseOrganizitionPerson")
public class BaseOrganizitionPersonController extends AbstractController{

    @Autowired
    private BaseOrganizitionPersonService baseOrganizitionPersonService;

    /**
     * 查询组织机构和人员关系信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getBaseOrganizitionPersonAll", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBaseOrganizitionPersonAll(HttpServletRequest request) {
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
        int totalCount = baseOrganizitionPersonService.dataCount();
        List<BasePark> baseParkList = baseOrganizitionPersonService.getBaseOrganizitionPersonList( hashmap );
        return LayuiUtil.dataList( totalCount, baseParkList );
    }

    /**
     * 组织机构和人员关系新增信息
     * @param baseOrganizitionPerson
     * @return
     */
    @RequestMapping(value = "/insertBaseOrganizitionPerson", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> insertBaseOrganizitionPerson(BaseOrganizitionPerson baseOrganizitionPerson) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        try {
            baseOrganizitionPerson.setCreateTime(new Date());
            baseOrganizitionPersonService.insertBaseOrganizitionPerson(baseOrganizitionPerson);
            resultMap.put("code",2);
        } catch (Exception ex) {
            logger.error( "新增组织机构和人员关系信息失败！！" + ex.getMessage() );
        }
        return resultMap;
    }

    /**
     * 修改组织机构和人员关系信息
     * @param baseOrganizitionPerson
     * @return
     */
    @RequestMapping(value = "/updateBaseOrganizitionPersonById", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> updateBaseOrganizitionPersonById(BaseOrganizitionPerson baseOrganizitionPerson) {
        Map<String, Object> map = new HashMap<>();
        try {
            //添加园区
            baseOrganizitionPerson.setUpdateTime(new Date());
            baseOrganizitionPersonService.updateBaseOrganizitionPersonById(baseOrganizitionPerson);
            map.put("code",2);
        }catch (Exception ex){
            logger.error( "修改组织机构和人员关系信息失败！！" + ex.getMessage() );
        }
        return map;
    }

    /**
     * 删除组织机构和人员关系信息
     * @param baseOrganizitionPerson
     * @return
     */
    @RequestMapping(value = "/deleteBaseOrganizitionPersonById", method = RequestMethod.DELETE)
    public Map<String, Object> deleteBaseOrganizitionPersonById(BaseOrganizitionPerson baseOrganizitionPerson) {
        int i = 0;
        i = baseOrganizitionPersonService.deleteBaseOrganizitionPersonById( baseOrganizitionPerson.getId() );
        return LayuiUtil.dataDelete( i );
    }

}
