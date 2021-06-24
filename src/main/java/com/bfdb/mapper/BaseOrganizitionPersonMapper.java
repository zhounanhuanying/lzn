package com.bfdb.mapper;

import com.bfdb.entity.BaseOrganizitionPerson;
import com.bfdb.entity.BasePark;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseOrganizitionPersonMapper {
    int dataCount();

    List<BasePark> getBaseOrganizitionPersonList(Map<String, Object> hashmap);

    void insertBaseOrganizitionPerson(BaseOrganizitionPerson baseOrganizitionPerson);

    Integer queryBaseOrganizitionPersonById(Integer id);

    void updateBaseOrganizitionPersonById(BaseOrganizitionPerson baseOrganizitionPerson);

    void deleteBaseOrganizitionPersonByPersonId(Integer[] id);

    int deleteBaseOrganizitionPersonById(Integer id);

    BaseOrganizitionPerson queryBaseOrganizitionPersonByPersonId(Integer personId);
}
