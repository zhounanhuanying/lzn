package com.bfdb.service;

import com.bfdb.entity.BaseOrganizition;
import com.bfdb.entity.BasePark;
import com.bfdb.entity.PersonTable;

import java.util.List;
import java.util.Map;

public interface BaseOrganizitionService {

    int dataCount();

    List<BasePark> getBaseOrganizitionList(String parkId);

    BaseOrganizition queryBaseOrganizitionByOrgCode(String orgCode);

    void insertBaseOrganizition(BaseOrganizition baseOrganizition);

    Integer queryBaseOrganizitionById(Integer id);

    void updateBaseOrganizitionById(BaseOrganizition baseOrganizition);

    int deleteBaseOrganizitionById(Integer id);

    String queryOrgNameById(String orgId);

    List<BaseOrganizition> getOrganizationTree(String parkId);

    List<PersonTable> getPersonTableByorgId(BaseOrganizition baseOrganizition);

    String queryBaseOrganizitionByPid(String pid);

    String getOrganizitionById(String organizitionId);

    String queryPorgNameByName(String orgName);

    int queryBaseOrganizitionByOrgCodeCount(String orgCode);
}
