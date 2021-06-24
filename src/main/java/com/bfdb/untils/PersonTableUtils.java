package com.bfdb.untils;

import com.bfdb.entity.PersonFaceInfomationTable;
import com.bfdb.entity.PersonTable;
import com.bfdb.service.PersonFaceInfomationTableService;
import com.bfdb.service.PersonTableService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员管理中用到的公共方法
 */
public class PersonTableUtils{


    /**
     * 查询人员信息  ---  公共方法
     *
     * @param personTableList
     * @param personTableService
     * @return
     */
    public static List<PersonTable> personTableList(List<PersonTable> personTableList, PersonTableService personTableService) {
        //返回结果集
        List<PersonTable> personTableList1 = new ArrayList<>();
        if (ListUtils.isNotNullAndEmptyList( personTableList )) {
            for (PersonTable personNewTable1 : personTableList) {
                List<PersonFaceInfomationTable> list = personTableService.queryPersonFaceInfomationTableByPersonId( personNewTable1.getPersonId() );
                personNewTable1.setPersonFaceInfomationTable( list );
                personTableList1.add( personNewTable1 );
            }
        }
        return personTableList;
    }

    /**
     *新增人员相关照片 的公共类
     * @param person
     * @param personTable
     * @param personFaceInfomationTableService
     */
    public static void personTableImage(PersonFaceInfomationTable person, PersonTable personTable, PersonFaceInfomationTableService personFaceInfomationTableService) {
        try {
            if (personTable.getPersonFaceInfomationTableBean() != null) {
                String faceAddress = null;
                String campusCardAddress = null;
                String faceImage = null;
                //判断人证核验图片是否为空
                if (StringUtils.isNotBlank( personTable.getPersonFaceInfomationTableBean().getFaceImage() )) {
                    faceImage = Base64Utils.generateImage( personTable.getPersonFaceInfomationTableBean().getFaceImage() );
                    person = new PersonFaceInfomationTable();
                    person.setFaceImage( faceImage );
                    person.setDataSource( "2" );
                    person.setPersonId( personTable.getPersonId() );
                    personFaceInfomationTableService.insertSelective( person );
                }
                //判断身份人脸是否为空
                if (StringUtils.isNotBlank( personTable.getPersonFaceInfomationTableBean().getFaceAddress() )) {
//                            imageSplit = personTable.getPersonFaceInfomationTableBean().getFaceAddress().split( "," );
                    faceAddress = Base64Utils.generateImage( personTable.getPersonFaceInfomationTableBean().getFaceAddress() );
                    person = new PersonFaceInfomationTable();
                    person.setFaceAddress( faceAddress );
                    person.setDataSource( "2" );
                    person.setIdentification( "1" );
                    person.setPersonId( personTable.getPersonId() );
                    personFaceInfomationTableService.insertSelective( person );
                }
                //判断校园卡图片是否为空
                if (StringUtils.isNotBlank( personTable.getPersonFaceInfomationTableBean().getCampusCardAddress() )) {
//                            imageSplit = personTable.getPersonFaceInfomationTableBean().getCampusCardAddress().split( "," );
                    campusCardAddress = personTable.getPersonFaceInfomationTableBean().getCampusCardAddress().replace( "data:image/jpeg;base64,", "" );
                    campusCardAddress = Base64Utils.generateImage( campusCardAddress );
                    person = new PersonFaceInfomationTable();
                    person.setCampusCardAddress( campusCardAddress );
                    person.setDataSource( "2" );
                    person.setPersonId( personTable.getPersonId() );
                    personFaceInfomationTableService.insertSelective( person );
                }
            }
        } catch (Exception e) {
            System.out.println("人证核验终端获取的数据失败！！" + e.getMessage());
        }

    }

    /**
     * 人员管理 --判断比较身份证号是否重复
     * @param personNewTable
     * @param thisPersonTable
     * @param personTableService
     * @return
     */
    public static PersonTable identityNo(PersonTable personNewTable, PersonTable thisPersonTable, PersonTableService personTableService) {
        PersonTable IdentityNo = null;
        // 判断身份证号是否已存在
        if (StringUtils.isNotBlank(personNewTable.getIdentityNo())) {
            if (thisPersonTable.getIdentityNo() != null) {
                // 判断当前修改的身份证号跟之前的身份证号是否一样
                if (!thisPersonTable.getIdentityNo().equals( personNewTable.getIdentityNo() )) {
                    IdentityNo = personTableService.selectPersonTableByIdentityNo( personNewTable.getIdentityNo() );
                }
            } else {
                IdentityNo = personTableService.selectPersonTableByIdentityNo( personNewTable.getIdentityNo() );
            }
        }
        return IdentityNo;
    }

}
