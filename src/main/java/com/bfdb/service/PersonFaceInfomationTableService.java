package com.bfdb.service;


import com.bfdb.entity.PersonFaceInfomationTable;
import com.bfdb.entity.PersonTable;

import java.util.List;

public interface PersonFaceInfomationTableService {

    int insertPersonNewFaceInfomationTable(  List<PersonFaceInfomationTable>  personNewFaceInfomationTableList);

    void deletePersonTableByPersonId(Integer personId);

    int insertSelective(PersonFaceInfomationTable person);

    //根据人员id删除人员照片信息
    int deletePersonTableByImagePersonId(Integer personId);
   //查询人员数据来源
    List<PersonFaceInfomationTable> selectDataSource(String dicCode);
    //查询人脸照片为多少张
    int faceAddresscount(Integer personId);
    //查询人证核验的照片有多少张
    int faceInfoImageCount(Integer personId);
     //查询人员信息中是否有身份证人脸图
     PersonFaceInfomationTable faceAddres(Integer personId);

    List<PersonFaceInfomationTable> selectImageList(Integer personId);
}
