package com.bfdb.service;

import com.bfdb.entity.BaseOrganizitionPerson;
import com.bfdb.entity.BasePark;

import java.util.List;
import java.util.Map;

public interface BaseOrganizitionPersonService {
    int dataCount();

    List<BasePark> getBaseOrganizitionPersonList(Map<String, Object> hashmap);

    void insertBaseOrganizitionPerson(BaseOrganizitionPerson baseOrganizitionPerson);

    Integer queryBaseOrganizitionPersonById(Integer id);

    void updateBaseOrganizitionPersonById(BaseOrganizitionPerson baseOrganizitionPerson);

    int deleteBaseOrganizitionPersonById(Integer id);
}
