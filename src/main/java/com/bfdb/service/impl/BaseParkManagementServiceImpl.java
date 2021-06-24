package com.bfdb.service.impl;

import com.bfdb.entity.BasePark;
import com.bfdb.mapper.BaseParkManagementMapper;
import com.bfdb.service.BaseParkManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BaseParkManagementServiceImpl implements BaseParkManagementService {

    @Autowired
    private BaseParkManagementMapper baseParkManagementMapper;

    @Override
    public int dataCount() {
        return baseParkManagementMapper.dataCount();
    }

    @Override
    public String queryParkNameById(String parkId) {
        return baseParkManagementMapper.queryParkNameById(parkId);
    }

    @Override
    public List<BasePark> fallBasePark() {
        return baseParkManagementMapper.fallBasePark();
    }

    @Override
    public List<BasePark> getBaseParkList(Map<String, Object> rePutParams) {
        List<BasePark> baseParkList = baseParkManagementMapper.getBaseParkList(rePutParams);
        return baseParkList;
    }

    @Override
    public int queryBaseParkByParkCode(String parkCode) {
        int i = baseParkManagementMapper.queryBaseParkByParkCode(parkCode);
        return i;
    }

    @Override
    public void insertBasePark(BasePark basePark) {
        baseParkManagementMapper.insertBasePark(basePark);
    }

    @Override
    public Integer queryBaseParyById(Integer id) {
        return baseParkManagementMapper.queryBaseParyById(id);
    }

    @Override
    public void updateBaseParkById(BasePark basePark) {
        baseParkManagementMapper.updateBaseParkById(basePark);
    }

    @Override
    public int deleteBaseParkById(Integer[] ids) {
        return baseParkManagementMapper.deleteBaseParkById(ids);
    }
}
