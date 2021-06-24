package com.bfdb.service.impl;

import com.bfdb.entity.BaseOrganizitionPerson;
import com.bfdb.entity.BasePark;
import com.bfdb.mapper.BaseOrganizitionPersonMapper;
import com.bfdb.service.BaseOrganizitionPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BaseOrganizitionPersonServiceImpl implements BaseOrganizitionPersonService {

    @Autowired
    private BaseOrganizitionPersonMapper baseOrganizitionPersonMapper;

    @Override
    public int dataCount() {
        return baseOrganizitionPersonMapper.dataCount();
    }

    @Override
    public List<BasePark> getBaseOrganizitionPersonList(Map<String, Object> hashmap) {
        return baseOrganizitionPersonMapper.getBaseOrganizitionPersonList(hashmap);
    }

    @Override
    public void insertBaseOrganizitionPerson(BaseOrganizitionPerson baseOrganizitionPerson) {
        baseOrganizitionPersonMapper.insertBaseOrganizitionPerson(baseOrganizitionPerson);
    }

    @Override
    public Integer queryBaseOrganizitionPersonById(Integer id) {
        return baseOrganizitionPersonMapper.queryBaseOrganizitionPersonById(id);
    }

    @Override
    public void updateBaseOrganizitionPersonById(BaseOrganizitionPerson baseOrganizitionPerson) {
        baseOrganizitionPersonMapper.updateBaseOrganizitionPersonById(baseOrganizitionPerson);
    }

    @Override
    public int deleteBaseOrganizitionPersonById(Integer id) {
        return baseOrganizitionPersonMapper.deleteBaseOrganizitionPersonById(id);
    }
}
