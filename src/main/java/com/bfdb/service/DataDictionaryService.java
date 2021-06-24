package com.bfdb.service;


import com.bfdb.entity.DataDictionary;

import java.util.List;
import java.util.Map;

public interface DataDictionaryService {

    List<DataDictionary> getDataDictionaryList(Map<String, Object> psrams);

    int dataCount(Map<String, Object> params);

    int updateDataDictionary(Map<String, Object> psrams);

    int insertDataDictionary(DataDictionary dataDictionary);

    int insertDataDictionaryType(DataDictionary dataDictionary);

    int deleteDataDictionaryById(String dicId);

    List<DataDictionary> getAllDicType();

    int deleteDictionaryList(List<DataDictionary> dictionaryList);

    List<DataDictionary> setlectDataDictionaryList(String dicType);

    List<DataDictionary> setlectDataDictionaryListTest();
    //查询未知的字典信息
    DataDictionary setlectDataDictionaryByweizhi(String dicType);
    //根据办公地点名称查询办公信息
    DataDictionary selectDataDictionaryBadname(String dicType, String departments);

    DataDictionary selectDataDictionaryByDicTypeAndDicName(DataDictionary dataDictionary);

    Map<String, Object> updataDataDictionaryType(DataDictionary dataDictionary);

    String queryDataDictionaryByName(String name);

    String queryDataDictionaryByCode(String dicCode);
}
