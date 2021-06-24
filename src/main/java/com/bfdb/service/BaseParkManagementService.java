package com.bfdb.service;

import com.bfdb.entity.BasePark;

import java.util.List;
import java.util.Map;

public interface BaseParkManagementService {

    List<BasePark> getBaseParkList(Map<String, Object> rePutParams);

    int queryBaseParkByParkCode(String parkCode);

    void insertBasePark(BasePark basePark);

    Integer queryBaseParyById(Integer id);

    void updateBaseParkById(BasePark basePark);

    int deleteBaseParkById(Integer[] ids);

    int dataCount();

    String queryParkNameById(String parkId);

    List<BasePark> fallBasePark();
}
