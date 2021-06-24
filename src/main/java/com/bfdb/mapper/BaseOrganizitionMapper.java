package com.bfdb.mapper;

import com.bfdb.entity.BaseOrganizition;
import com.bfdb.entity.BasePark;
import com.bfdb.entity.PersonTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseOrganizitionMapper {
    int dataCount();

    List<BasePark> getBaseOrganizitionList(@Param("parkId") String parkId);

    BaseOrganizition queryBaseOrganizitionByOrgCode(String orgCode);

    void insertBaseOrganizition(BaseOrganizition baseOrganizition);

    Integer queryBaseOrganizitionById(Integer id);

    void updateBaseOrganizitionById(BaseOrganizition baseOrganizition);

    int deleteBaseOrganizitionById(@Param("id") Integer id);

    List<BaseOrganizition> queryBaseOrganizition();

    String queryOrgNameById(@Param("orgId") String orgId);

    List<BaseOrganizition> queryBaseOrganizitionAll(@Param("parkId") String parkId);

    String queryBaseOrganizitionByPid(String pid);

    String getOrganizitionById(String organizitionId);

    String queryPorgNameByName(@Param("orgName") String orgName);

    List<BaseOrganizition> queryBaseOrganizitionByOrgType(@Param("orgType") Integer orgType);

    int queryBaseOrganizitionByOrgCodeCount(String orgCode);

    BaseOrganizition queryOrganizitionById(@Param("orgId") String orgId);
}
