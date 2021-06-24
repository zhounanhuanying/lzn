package com.bfdb.mapper;

import com.bfdb.entity.PersonFaceInfomationTable;
import com.bfdb.entity.PersonTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PersonNewFaceInfomationTableMapper继承基类
 */
@Mapper
public interface PersonFaceInfomationTableMapper extends MyBatisBaseDao<PersonFaceInfomationTable, Integer> {

    int insertPersonNewFaceInfomationTable(List<PersonFaceInfomationTable> personNewFaceInfomationTableList);

    int deletePersonTableByUserId(Integer[] personCodes);

    void deletePersontableByPersonId(Integer personId);

    //根据人员信息删除人员照片信息
    int deletePersonTableByImagePersonId(Integer personId);
    //查询人员数据来源
    List<PersonFaceInfomationTable> selectDataSource(String dicCode);
     //查询人脸照片为多少张
    int faceAddresscount(Integer personId);
    //查询人证核验的照片有多少张
    int faceInfoImageCount(Integer personId);
    //  根据人员personId查询相关图片路径
    List<PersonFaceInfomationTable> selectPersonTableByImagePersonId(Integer[] personId);
   //查询人员信息中是否有身份证人脸图
   PersonFaceInfomationTable faceAdddres(Integer personId);

    List<PersonFaceInfomationTable> selectImageList(Integer personId);
}