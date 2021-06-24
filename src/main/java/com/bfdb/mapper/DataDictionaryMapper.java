package com.bfdb.mapper;


import com.bfdb.entity.DataDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DataDictionaryMapper {

    List<DataDictionary> getDataDictionaryList(Map<String, Object> psrams);
    //查询所有数据字典条数
    int dataCount(Map<String, Object> params);

    int updateDataDictionary(Map<String, Object> psrams);

    int insertDataDictionary(DataDictionary dataDictionary);

    int deleteDataDictionaryById(String dicId);

    String[] getAllDicCode(String dicType);

    List<DataDictionary> getAllDicType();

    int deleteDictionaryList(Integer[] ids);

     DataDictionary getDicListByDicName(String name, String dicName);

    List<DataDictionary> setlectDataDictionaryList(String dicType);
    // <!--查询字典类型-->
    List<DataDictionary> getDataDictionaryByDicCodeList();
     //根据id查询字典类型
    DataDictionary setlectByUnknown(Integer dicId);
    List<DataDictionary> setlectDataDictionaryListTest();

    String[] getAllDicName(String dicName);
    //查询未知的字典信息
    DataDictionary setlectDataDictionaryByweizhi(String dicType);
    //根据办公地点名称查询办公信息
    DataDictionary selectDataDictionaryBadname(@Param("dicType") String dicType, @Param("dicCode") String dicCode);

    DataDictionary selectDataDictionaryByDicTypeAndDicName(DataDictionary dataDictionary);
//修改字典基础类型
    int updataDataDictionaryType(DataDictionary dataDictionary);

    String queryDataDictionaryByName(@Param("name") String name);

    String queryDataDictionaryByCode(@Param("dicCode") String dicCode);
}
