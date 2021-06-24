package com.bfdb.service.impl;

import com.bfdb.entity.BaseOrganizition;
import com.bfdb.entity.BasePark;
import com.bfdb.entity.PersonTable;
import com.bfdb.mapper.BaseOrganizitionMapper;
import com.bfdb.mapper.PersonTableMapper;
import com.bfdb.service.BaseOrganizitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BaseOrganizitionServiceImpl implements BaseOrganizitionService {

    @Autowired
    private BaseOrganizitionMapper baseOrganizitionMapper;
    @Autowired
    private PersonTableMapper personTableMapper;

    @Override
    public int dataCount() {
        return baseOrganizitionMapper.dataCount();
    }

    @Override
    public List<BasePark> getBaseOrganizitionList(String parkId) {
        return baseOrganizitionMapper.getBaseOrganizitionList(parkId);
    }

    @Override
    public BaseOrganizition queryBaseOrganizitionByOrgCode(String orgCode) {
        return baseOrganizitionMapper.queryBaseOrganizitionByOrgCode(orgCode);
    }

    @Override
    public void insertBaseOrganizition(BaseOrganizition baseOrganizition) {
        baseOrganizitionMapper.insertBaseOrganizition(baseOrganizition);
    }

    @Override
    public Integer queryBaseOrganizitionById(Integer id) {
        return baseOrganizitionMapper.queryBaseOrganizitionById(id);
    }

    @Override
    public void updateBaseOrganizitionById(BaseOrganizition baseOrganizition) {
        baseOrganizitionMapper.updateBaseOrganizitionById(baseOrganizition);
    }

    @Override
    public int deleteBaseOrganizitionById(Integer id) {
        return baseOrganizitionMapper.deleteBaseOrganizitionById(id);
    }

    @Override
    public String queryOrgNameById(String orgId) {
        return baseOrganizitionMapper.queryOrgNameById(orgId);
    }

    @Override
    public List<BaseOrganizition> getOrganizationTree(String parkId) {

        List<BaseOrganizition> treeList = new ArrayList<>();
        List<BaseOrganizition> baseOrganizition = null;
        /*if(StringUtils.isBlank(parkId)){
            return all;
        }*/
        baseOrganizition = baseOrganizitionMapper.queryBaseOrganizitionAll(parkId);
        Map<Integer, BaseOrganizition> baseOrganizitionMap = new HashMap<>();

        if (!baseOrganizition.isEmpty() && baseOrganizition != null)
            for (BaseOrganizition role : baseOrganizition) {
                baseOrganizitionMap.put(role.getId(), role);
            }

        for (BaseOrganizition role : baseOrganizition) {
            BaseOrganizition child = role;
            if (child.getPid().equals("0")) {
                treeList.add(role);
            } else {
                BaseOrganizition parent = baseOrganizitionMap.get(Integer.parseInt(child.getPid()));
                if (parent!=null){
                    parent.getChildren().add(child);
                }
            }
        }

        return treeList;
    }

    @Override
    public List<PersonTable> getPersonTableByorgId(BaseOrganizition baseOrganizition) {
        return personTableMapper.getPersonTableByorgId(baseOrganizition);
    }

    @Override
    public String queryBaseOrganizitionByPid(String pid) {
        return baseOrganizitionMapper.queryBaseOrganizitionByPid(pid);
    }

    @Override
    public String getOrganizitionById(String organizitionId) {
        return baseOrganizitionMapper.getOrganizitionById(organizitionId);
    }

    @Override
    public String queryPorgNameByName(String orgName) {
        return baseOrganizitionMapper.queryPorgNameByName(orgName);
    }

    @Override
    public int queryBaseOrganizitionByOrgCodeCount(String orgCode) {
        return baseOrganizitionMapper.queryBaseOrganizitionByOrgCodeCount(orgCode);
    }

}
