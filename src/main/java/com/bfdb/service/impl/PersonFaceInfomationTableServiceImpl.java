package com.bfdb.service.impl;

import com.bfdb.entity.PersonFaceInfomationTable;
import com.bfdb.entity.PersonTable;
import com.bfdb.mapper.PersonFaceInfomationTableMapper;
import com.bfdb.service.PersonFaceInfomationTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonFaceInfomationTableServiceImpl implements PersonFaceInfomationTableService {


    @Autowired
    private PersonFaceInfomationTableMapper personFaceInfomationTableMapper;

    /**
     * 新人脸照片表
     *
     * @param personNewFaceInfomationTableList
     * @return
     */
    @Override
    public int insertPersonNewFaceInfomationTable(List<PersonFaceInfomationTable> personNewFaceInfomationTableList) {

        return personFaceInfomationTableMapper.insertPersonNewFaceInfomationTable( personNewFaceInfomationTableList );
    }

    @Override
    public void deletePersonTableByPersonId(Integer personId) {
        personFaceInfomationTableMapper.deletePersontableByPersonId( personId );
    }

    /**
     * 人证核验终端照片新增
     *
     * @param person
     */
    @Override
    public int insertSelective(PersonFaceInfomationTable person) {
        return personFaceInfomationTableMapper.insertSelective( person );
    }

    /**
     * 根据人员id删除照片信息
     * @param personId
     * @return
     */
    @Override
    public int deletePersonTableByImagePersonId(Integer personId) {
        return personFaceInfomationTableMapper.deletePersonTableByImagePersonId(personId);
    }

    /**
     * 查询人员数据来源
     * @param dicCode
     * @return
     */
    @Override
    public List<PersonFaceInfomationTable> selectDataSource(String dicCode) {
        return personFaceInfomationTableMapper.selectDataSource(dicCode);
    }

    /**
     * 查询人脸照片为多少张
     * @param personId
     * @return
     */
    @Override
    public int faceAddresscount(Integer personId) {
        return personFaceInfomationTableMapper.faceAddresscount(personId);
    }

    /**
     * 查询人证核验的照片有多少张
     * @param personId
     * @return
     */
    @Override
    public int faceInfoImageCount(Integer personId) {
        return personFaceInfomationTableMapper.faceInfoImageCount(personId);
    }

    /**
     * 查询人员信息中是否有身份证人脸图
     * @param personId
     * @return
     */
    @Override
    public PersonFaceInfomationTable faceAddres(Integer personId) {
        return personFaceInfomationTableMapper.faceAdddres(personId);
    }

    @Override
    public List<PersonFaceInfomationTable> selectImageList(Integer personId) {
        return personFaceInfomationTableMapper.selectImageList(personId);
    }

}
