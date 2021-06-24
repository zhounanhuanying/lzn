package com.bfdb.mapper;

import com.bfdb.entity.BasePark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseParkManagementMapper {

    int dataCount();

    List<BasePark> getBaseParkList(Map<String, Object> rePutParams);

    Integer queryBaseParkByParkCode(String parkCode);

    void insertBasePark(BasePark basePark);

    Integer queryBaseParyById(Integer id);

    void updateBaseParkById(BasePark basePark);

    int deleteBaseParkById(Integer[] ids);

    List<BasePark> queryBasePark();

    String queryParkNameById(String parkId);

    List<BasePark> fallBasePark();
}
