package com.bfdb.service.impl;

import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.entity.vo.PageInfoVo;
import com.bfdb.entity.vo.PersonTableVo;
import com.bfdb.mapper.DataDictionaryMapper;
import com.bfdb.mapper.InterfaceManagementMapper;
import com.bfdb.mapper.PermissionMapper;
import com.bfdb.mapper.PersonTableMapper;
import com.bfdb.service.DataDictionaryService;
import com.bfdb.service.InterfaceManagementService;
import com.bfdb.untils.IDCardUtil;
import com.bfdb.untils.ListUtils;
import com.bfdb.untils.PhoneFormatCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterfaceManagementServiceImpl implements InterfaceManagementService {

    @Autowired
    private InterfaceManagementMapper interfaceManagementMapper;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PersonTableMapper personTableMapper;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 第三方系统调用新增人员信息
     *
     * @param personTableList
     * @param user
     * @return
     */
    @Override
    @Transactional
    public int insertPersonTableList(List<PersonTableVo> personTableList, User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        int i = 1;
        int count = 0;
        //遍历判断
        for (PersonTableVo personTable : personTableList) {
            //注意replaceAll前面的是正则表达式
            String uuid = UUID.randomUUID().toString().replaceAll( "-", "" );
            //创建时间
            if (personTable.getCreateTime() == null) {
                personTable.setCreateTime( new Date() );
            }
            //人员姓名不能为空
            if (StringUtils.isBlank( personTable.getPersonName() )) {
                return i = 2;

            }
            //人员类型不能为空
            DataDictionary dataDictionary = new DataDictionary();
            dataDictionary.setDicType( Constant.IDENTICATIONINFO );
            dataDictionary.setDicCode( personTable.getIdenticationInfo() );
            if (StringUtils.isBlank( personTable.getIdenticationInfo() )) {
                return i = -7;

            } else if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
//                人员类型在字典表中不存在
                return i = -14;

            }
            // 人员姓名必须为中文
            if (!personTable.getPersonName().matches( "[\\u4e00-\\u9fa5]+" )) {
                return i = -11;
            }
//            性别是否在字典表中存在
            if (personTable.getGender() != null) {
                dataDictionary.setDicType( Constant.SEX );
                dataDictionary.setDicCode( personTable.getGender().toString() );
                if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
                    return i = -12;
                }
            }
//            民族是否在字典表中存在
            if (StringUtils.isNotBlank( personTable.getEthnicity() )) {
                dataDictionary.setDicType( Constant.ETHNICITY );
                dataDictionary.setDicCode( personTable.getEthnicity() );
                if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
                    return i = -13;
                }
            }
            //当前用户是否和所在部门一致
            if (StringUtils.isBlank( personTable.getDepartments() )) {
                return i = -5;

            } else {
                //根据用户id、url路径、所在部门查询权限信息
                if(123456789!=user.getUserId()){
                    map.put( "userId", user.getUserId() );
                    map.put( "visitorUrl", Constant.INTERFACEADDRESS );
                    map.put( "popupWay", personTable.getDepartments() );
                    Permission selectDepartments = permissionMapper.selectDepartments( map );
                    //判空
                    if (selectDepartments == null) {
                        return i = -6;
                    }
                }
            }
            //验证手机号是否正确
            if (StringUtils.isNotBlank( personTable.getTelephone() )) {
                boolean phoneLegal = IDCardUtil.isPhone( personTable.getTelephone() );
                if (phoneLegal == false) {
                    return i = -4;
                }
            }
            //身份证号或者教工号不能为空
            if (StringUtils.isBlank( personTable.getIdentityNo() )) {
                if (StringUtils.isBlank( personTable.getIdentityTypeCode() )) {
                    return i = -1;
                } else {
                    //验证一卡通号是否符合要求
                    boolean identityTypeCode = IDCardUtil.isIdentityTypeCodeMapper( personTable.getIdentityTypeCode() );
                    if (!identityTypeCode) {
                        return i = -10;
                    }
                    // 教工号或学号或一卡通号查重
                    PersonTable personTable1 = interfaceManagementMapper.selectByIdentityTypeCode( personTable.getIdentityTypeCode() );
                    if (personTable1 != null) {
                        return i = -9;
                    }
                }
            } else {
                //验证身份证号
                boolean idCard = IDCardUtil.isIDCard( personTable.getIdentityNo() );
                if (idCard == false) {
                    return i = -3;
                }
                //身份证查重
                PersonTable identityNo = interfaceManagementMapper.selectByIdentityNo( personTable.getIdentityNo() );
                if (identityNo != null) {
                    return i = -8;
                }
                // 教工号或学号或一卡通号查重
                PersonTable identityTypeCode = interfaceManagementMapper.selectByIdentityTypeCode( personTable.getIdentityTypeCode() );
                if (identityTypeCode != null) {
                    return i = -9;
                }
            }
            personTable.setPersonCode( uuid );
            personTable.setIsDeleted( 1 );//是否删除(1:未删除，2:假删除)
            //从身份证号获取出生年月日
            if (!StringUtils.isBlank(personTable.getIdentityNo())){
                personTable.setBirthday( extractYearMonthDayOfIdCard( personTable.getIdentityNo() ) );
            }
            //判断图片不能为空
            if (personTable.getFaceInfomation() == null || personTable.getFaceInfomation().size() <= 0) {
                return i = -2;
            } else {

               /* List<PersonFaceInfomationTable> personFaceInfomationTable1 = personTable.getPersonFaceInfomationTable();
                personFaceInfomationTable1.stream().filter(personFaceInfomationTable ->
                    (personFaceInfomationTable.getFaceAddress()!=null && personFaceInfomationTable.getFaceAddress().trim().length()>0)|| (personFaceInfomationTable.getCampusCardAddress()!=null && personFaceInfomationTable.getCampusCardAddress().trim().length()>0)


                ).collect(Collectors.toList());*/
                //循环判断人脸图片至少是一张
                List<PersonFaceInfomationTable> personFaceInfomationTable1 = new ArrayList<>();
                List<PersonFaceInfomationTable> personCampusCardAddressInfomationTable = new ArrayList<>();
//                if (ListUtils.isNotNullAndEmptyList(personTable.getFaceInfomation())){
                for (PersonFaceInfomationTable personFaceInfomationTable : personTable.getFaceInfomation()) {
                    if (StringUtils.isNotBlank( personFaceInfomationTable.getFaceAddress())) {
                        personFaceInfomationTable1.add(personFaceInfomationTable);
                        count++;
                    }
                }
                personTable.setFaceInfomation(personFaceInfomationTable1);
//                }

                //循环判断一卡通图片至多是一张
                if (ListUtils.isNotNullAndEmptyList(personTable.getCardInfomation())){
                    for (PersonFaceInfomationTable personCardAddressInfomationTable : personTable.getCardInfomation()) {
                        if (StringUtils.isNotBlank( personCardAddressInfomationTable.getCampusCardAddress())) {
                            personCampusCardAddressInfomationTable.add(personCardAddressInfomationTable);
                        }
                    }
                    personTable.setCardInfomation(personCampusCardAddressInfomationTable);
                }
                if (personCampusCardAddressInfomationTable.size() > 1){
//          一卡通照片超过一张
                    return 3;
                }
            }
        }
        //判断照片是否符合要求
        if (count <= 0 || count>3) {
            return i = -2;
        }

        //添加入库
        i = interfaceManagementMapper.insertPersonTableList( personTableList );
        return i;
    }



    /**
     * 第三方系统调用新增或修改人员信息
     *
     * @param personTableList
     * @param user
     * @return
     */
    @Override
    @Transactional
    public int insertOrUpdatePersonTableList(List<PersonTableVo> personTableList, User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        int i = 1;
        int count = 0;
        //遍历判断
        for (PersonTableVo personTable : personTableList) {

            //判断图片不能为空
            if (personTable.getFaceInfomation() == null || personTable.getFaceInfomation().size() <= 0) {
                return i = -2;
            } else {

               /* List<PersonFaceInfomationTable> personFaceInfomationTable1 = personTable.getPersonFaceInfomationTable();
                personFaceInfomationTable1.stream().filter(personFaceInfomationTable ->
                    (personFaceInfomationTable.getFaceAddress()!=null && personFaceInfomationTable.getFaceAddress().trim().length()>0)|| (personFaceInfomationTable.getCampusCardAddress()!=null && personFaceInfomationTable.getCampusCardAddress().trim().length()>0)


                ).collect(Collectors.toList());*/
                //循环判断人脸图片至少是一张
                List<PersonFaceInfomationTable> personFaceInfomationTable1 = new ArrayList<>();
                List<PersonFaceInfomationTable> personCampusCardAddressInfomationTable = new ArrayList<>();
//                if (ListUtils.isNotNullAndEmptyList(personTable.getFaceInfomation())){
                for (PersonFaceInfomationTable personFaceInfomationTable : personTable.getFaceInfomation()) {
                    if (StringUtils.isNotBlank( personFaceInfomationTable.getFaceAddress())) {
                        personFaceInfomationTable1.add(personFaceInfomationTable);
                        count++;
                    }
                }
                personTable.setFaceInfomation(personFaceInfomationTable1);
//                }

                //循环判断一卡通图片至多是一张
                if (ListUtils.isNotNullAndEmptyList(personTable.getCardInfomation())){
                    for (PersonFaceInfomationTable personCardAddressInfomationTable : personTable.getCardInfomation()) {
                        if (StringUtils.isNotBlank( personCardAddressInfomationTable.getCampusCardAddress())) {
                            personCampusCardAddressInfomationTable.add(personCardAddressInfomationTable);
                        }
                    }
                    personTable.setCardInfomation(personCampusCardAddressInfomationTable);
                }
                if (personCampusCardAddressInfomationTable.size() > 1){
//          一卡通照片超过一张
                    return 3;
                }
            }

            //判断照片是否符合要求
            if (count <= 0 || count>3) {
                return i = -2;
            }

            //注意replaceAll前面的是正则表达式
            String uuid = UUID.randomUUID().toString().replaceAll( "-", "" );
            //创建时间
            if (personTable.getCreateTime() == null) {
                personTable.setCreateTime( new Date() );
            }
            //人员姓名不能为空
            if (StringUtils.isBlank( personTable.getPersonName() )) {
                return i = 2;

            }
            //人员类型不能为空
            DataDictionary dataDictionary = new DataDictionary();
            dataDictionary.setDicType( Constant.IDENTICATIONINFO );
            dataDictionary.setDicCode( personTable.getIdenticationInfo() );
            if (StringUtils.isBlank( personTable.getIdenticationInfo() )) {
                return i = -7;

            } else if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
//                人员类型在字典表中不存在
                return i = -14;

            }
            // 人员姓名必须为中文
//            if (!personTable.getPersonName().matches( "[\\u4e00-\\u9fa5]+" )) {
//                return i = -11;
//            }
//            性别是否在字典表中存在
            if (personTable.getGender() != null) {
                dataDictionary.setDicType( Constant.SEX );
                dataDictionary.setDicCode( personTable.getGender().toString() );
                if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
                    return i = -12;
                }
            }
//            民族是否在字典表中存在
            if (StringUtils.isNotBlank( personTable.getEthnicity() )) {
                dataDictionary.setDicType( Constant.ETHNICITY );
                dataDictionary.setDicCode( personTable.getEthnicity() );
                if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
                    return i = -13;
                }
            }
            //当前用户是否和所在部门一致
            if (StringUtils.isBlank( personTable.getDepartments() )) {
                return i = -5;

            } else {
                //根据用户id、url路径、所在部门查询权限信息
                if(123456789!=user.getUserId()){
                map.put( "userId", user.getUserId() );
                map.put( "visitorUrl", Constant.INTERFACEADDRESS );
                map.put( "popupWay", personTable.getDepartments() );
                Permission selectDepartments = permissionMapper.selectDepartments( map );
                //判空
                if (selectDepartments == null) {
                    return i = -6;
                }
                }
            }
            //验证手机号是否正确
            if (StringUtils.isNotBlank( personTable.getTelephone() )) {
                boolean phoneLegal = IDCardUtil.isPhone( personTable.getTelephone() );
                if (phoneLegal == false) {
                    return i = -4;
                }
            }
            personTable.setPersonCode( uuid );
            personTable.setIsDeleted( 1 );//是否删除(1:未删除，2:假删除)
            //从身份证号获取出生年月日
            if (!StringUtils.isBlank(personTable.getIdentityNo())){
                personTable.setBirthday( extractYearMonthDayOfIdCard( personTable.getIdentityNo() ) );
            }

            //身份证号判断
            if (StringUtils.isNotBlank( personTable.getIdentityNo() )) {
                //验证身份证号
                boolean idCard = IDCardUtil.isIDCard( personTable.getIdentityNo() );
                if (idCard == false) {
                    return i = -3;
                }
                //身份证查重
                PersonTable identityNo = interfaceManagementMapper.selectByIdentityNo( personTable.getIdentityNo() );
                if (identityNo != null) {
                    return i = -8;
                }
            }

            //教工号不能为空
            if (StringUtils.isBlank( personTable.getIdentityTypeCode() )) {
                return i = -1;
            } else {
                //验证一卡通号是否符合要求
//                boolean identityTypeCode = IDCardUtil.isIdentityTypeCodeMapper( personTable.getIdentityTypeCode() );
                if (personTable.getIdentityTypeCode().length()>20) {
                    return i = -10;
                }
                // 教工号或学号或一卡通号查重
                PersonTable personTable1 = interfaceManagementMapper.selectByIdentityTypeCode( personTable.getIdentityTypeCode() );
                if (personTable1 != null) {
                    return i = -9;
                }
            }
        }
        //添加入库
        i = interfaceManagementMapper.insertPersonTableList( personTableList );
        return i;
    }

    /**
     * 第三方系统调用修改人员基本信息
     *
     * @param personTableList
     * @param user
     * @return
     */
    @Override
    @Transactional
    public int updatePersonTableList(List<PersonTableVo> personTableList, User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        int i = 1;
        int count = 0;
        for (PersonTableVo personTable : personTableList) {
            //人员姓名不能为空
            if (StringUtils.isBlank( personTable.getPersonName() )) {
                return i = 2;
            }
            //人员类型不能为空
            DataDictionary dataDictionary = new DataDictionary();
            dataDictionary.setDicType( Constant.IDENTICATIONINFO );
            dataDictionary.setDicCode( personTable.getIdenticationInfo() );
            if (StringUtils.isBlank( personTable.getIdenticationInfo() )) {
                return i = -7;

            } else if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
//                人员类型在字典表中不存在
                return i = -14;

            }
            // 人员姓名必须为中文
            if (!personTable.getPersonName().matches( "[\\u4e00-\\u9fa5]+" )) {
                return i = -11;
            }
            //            性别是否在字典表中存在
            if (personTable.getGender() != null) {
                dataDictionary.setDicType( Constant.SEX );
                dataDictionary.setDicCode( personTable.getGender().toString() );
                if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
                    return i = -12;
                }
            }
//            民族是否在字典表中存在
            if (StringUtils.isNotBlank( personTable.getEthnicity() )) {
                dataDictionary.setDicType( Constant.ETHNICITY );
                dataDictionary.setDicCode( personTable.getEthnicity() );
                if (dataDictionaryService.selectDataDictionaryByDicTypeAndDicName( dataDictionary ) == null) {
                    return i = -13;
                }
            }            //当前用户是否和所在部门一致
            if (StringUtils.isBlank( personTable.getDepartments() )) {
                return i = -5;

            } else {
                //根据用户id、url路径、所在部门查询权限信息
                if(123456789!=user.getUserId()){
                    map.put( "userId", user.getUserId() );
                    map.put( "visitorUrl", Constant.INTERFACEADDRESS );
                    map.put( "popupWay", personTable.getDepartments() );
                    Permission selectDepartments = permissionMapper.selectDepartments( map );
                    //判空
                    if (selectDepartments == null) {
                        return i = -6;
                    }
                }
            }
            //验证手机号是否正确
            if (StringUtils.isNotBlank( personTable.getTelephone() )) {
                boolean phoneLegal = IDCardUtil.isPhone( personTable.getTelephone() );
                if (phoneLegal == false) {
                    return i = -4;
                }
            }
            //身份证号或者教工号不能为空
            if (StringUtils.isBlank( personTable.getIdentityNo() )) {
                if (StringUtils.isBlank( personTable.getIdentityTypeCode() )) {
                    return i = -1;
                } else {
                    //验证一卡通号是否符合要求
                    boolean identityTypeCode = IDCardUtil.isIdentityTypeCodeMapper( personTable.getIdentityTypeCode() );
                    if (!identityTypeCode) {
                        return i = -10;
                    }
                    // 教工号或学号或一卡通号查重
                    PersonTable personTable1 = interfaceManagementMapper.selectByIdentityTypeCode( personTable.getIdentityTypeCode() );
                    if (personTable1 != null && !personTable1.getPersonCode().equals(personTable.getPersonCode())) {
                        return i = -9;
                    }
                }
            } else {
                //验证身份证号
                boolean idCard = IDCardUtil.isIDCard( personTable.getIdentityNo() );
                if (idCard == false) {
                    return i = -3;
                }
                // 根据当前personCode去查询该人员信息
                PersonTable thisPersonTable = personTableMapper.selectByPersonCode( personTable.getPersonCode() );
                PersonTable identityNo = null;
                if (StringUtils.isNotBlank( thisPersonTable.getIdentityNo() )) {
                    // 判断当前修改的身份证号跟之前的身份证号是否一样
                    if (!thisPersonTable.getIdentityNo().equals( personTable.getIdentityNo() )) {
                        identityNo = interfaceManagementMapper.selectByIdentityNo( personTable.getIdentityNo() );
                    }
                } else {
                    identityNo = interfaceManagementMapper.selectByIdentityNo( personTable.getIdentityNo() );
                }
                if (identityNo != null) {
                    return i = -8;
                }
                // 教工号或学号或一卡通号查重
                PersonTable personTable1 = interfaceManagementMapper.selectByIdentityTypeCode( personTable.getIdentityTypeCode() );
                if (personTable1 != null && !personTable1.getPersonCode().equals(personTable.getPersonCode())) {
                    return i = -9;
                }
            }
            personTable.setIsDeleted( 1 );//是否删除(1:未删除，2:假删除)
            //从身份证号获取出生年月日
            if (!StringUtils.isBlank(personTable.getIdentityNo())){
                personTable.setBirthday( extractYearMonthDayOfIdCard( personTable.getIdentityNo() ) );
            }
            //判断图片不能为空
            if (personTable.getFaceInfomation() == null || personTable.getFaceInfomation().size() <= 0) {
                return i = -2;
            } else {
                //循环判断人脸图片或者校园卡必须为一张
                //循环判断人脸图片至少是一张
                List<PersonFaceInfomationTable> personFaceInfomationTable1 = new ArrayList<>();
                List<PersonFaceInfomationTable> personCampusCardAddressInfomationTable = new ArrayList<>();
//                if (ListUtils.isNotNullAndEmptyList(personTable.getFaceInfomation())){
                for (PersonFaceInfomationTable personFaceInfomationTable : personTable.getFaceInfomation()) {
                    if (StringUtils.isNotBlank( personFaceInfomationTable.getFaceAddress())) {
                        personFaceInfomationTable1.add(personFaceInfomationTable);
                        count++;
                    }
                }
                personTable.setFaceInfomation(personFaceInfomationTable1);
//                }

                //循环判断一卡通图片至多是一张
                if (ListUtils.isNotNullAndEmptyList(personTable.getCardInfomation())) {
                    for (PersonFaceInfomationTable personCardAddressInfomationTable : personTable.getCardInfomation()) {
                        if (StringUtils.isNotBlank(personCardAddressInfomationTable.getCampusCardAddress())) {
                            personCampusCardAddressInfomationTable.add(personCardAddressInfomationTable);
                        }
                    }
                    personTable.setCardInfomation(personCampusCardAddressInfomationTable);
                }
                if (personCampusCardAddressInfomationTable.size() > 1){
//          一卡通照片超过一张
                    return 3;
                }
            }
            //修改时间
            personTable.setModifyTime( new Date() );

        }
        //判断照片是否符合要求
        if (count < 0 || count>3) {
            return i = -2;
        }
        //修改人员信息
        i = interfaceManagementMapper.updatePersonTableList( personTableList );
        return i;
    }

    /**
     * 根据personCode查询用户信息
     *
     * @param personCode
     * @return
     */
    @Override
    public PersonTable selectPersonTable(String personCode) {
        return interfaceManagementMapper.selectPersonTable( personCode );
    }

    /**
     * 省份证的正则表达式^(\d{15}|\d{17}[\dx])$
     *
     * @param id 省份证号
     * @return 生日（yyyy-MM-dd）
     */
    public String extractYearMonthDayOfIdCard(String id) {
        String year = null;
        String month = null;
        String day = null;
        //正则匹配身份证号是否是正确的，15位或者17位数字+数字/x/X
        if (id.matches( "^\\d{15}|\\d{17}[\\dxX]$" )) {
            year = id.substring( 6, 10 );
            month = id.substring( 10, 12 );
            day = id.substring( 12, 14 );
        } else {
            System.out.println( "身份证号码不匹配！" );
            return null;
        }
        return year + "-" + month + "-" + day;
    }

    /**
     * 查询人员信息
     *
     * @param personTable
     * @return
     */
    @Override
    public List<PersonTable> queryPersonTableList(PersonTable personTable, PageInfoVo pageInfoVo) {
        //定义分页信息
        int rowNum = pageInfoVo.getRowNum();
        int pageSize = pageInfoVo.getPageSize();
        int pageNos = (rowNum - 1) * pageSize;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put( "limit", pageSize );
        map.put( "page", pageNos );
        map.put( "personName", personTable.getPersonName() );
        map.put( "identicationInfo", personTable.getIdenticationInfo() );
        map.put( "departments", personTable.getDepartments() );
        map.put( "departmentsarr", personTable.getDepartmentsarr() );
        map.put( "personCode", personTable.getPersonCode() );
        //查询未删除的人员集合信息
        List<PersonTable> personTableList = interfaceManagementMapper.queryPersonTableALLList( map );
        //查询所在部门或院系信息
        List<DataDictionary> departmentsList = dataDictionaryMapper.setlectDataDictionaryList( Constant.DEPARTMENT );
        //查询学历
        List<DataDictionary> studentLevelList = dataDictionaryMapper.setlectDataDictionaryList( Constant.EDUCATION );
        //查询学级
        List<DataDictionary> gradeList = dataDictionaryMapper.setlectDataDictionaryList( Constant.GRADE );
        //查询人员信息
        List<DataDictionary> identicationInfoList = dataDictionaryMapper.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
        //查询民族
        List<DataDictionary> ethnicityList = dataDictionaryMapper.setlectDataDictionaryList( Constant.ETHNICITY );

        /**
         *判断是否为空
         * 以下代码，是为了从字典表中查出相关数据，进行对比显现
         */
        if (ListUtils.isNotNullAndEmptyList( personTableList )) {
            for (PersonTable personTable1 : personTableList) {
                //判空 学历
                if (StringUtils.isNotBlank( personTable1.getStudentLevel() )) {
                    if ((ListUtils.isNotNullAndEmptyList( studentLevelList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : studentLevelList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( personTable1.getStudentLevel() )) {
                            for (DataDictionary dataDictionary : studentLevelList) {
                                if (dataDictionary.getDicCode().equals( personTable1.getStudentLevel() )) {
                                    personTable1.setStudentLevel( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            personTable1.setStudentLevel( "" );
                        }
                    }

                }
                //判空 学级
                if (StringUtils.isNotBlank( personTable1.getGrade() )) {
                    if ((ListUtils.isNotNullAndEmptyList( gradeList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : gradeList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( personTable1.getGrade() )) {
                            for (DataDictionary dataDictionary : gradeList) {
                                if (dataDictionary.getDicCode().equals( personTable1.getGrade() )) {
                                    personTable1.setGrade( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            personTable1.setGrade( "" );
                        }
                    }

                }
                //判空 所在部门或院系
                if (StringUtils.isNotBlank( personTable1.getDepartments() )) {
                    if ((ListUtils.isNotNullAndEmptyList( departmentsList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : departmentsList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( personTable1.getDepartments() )) {
                            for (DataDictionary dataDictionary : departmentsList) {
                                if (dataDictionary.getDicCode().equals( personTable1.getDepartments() )) {
                                    personTable1.setDepartments( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            personTable1.setDepartments( "" );
                        }
                    }

                }
                //判空 人员类型
                if (StringUtils.isNotBlank( personTable1.getIdenticationInfo() )) {
                    if ((ListUtils.isNotNullAndEmptyList( identicationInfoList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : identicationInfoList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( personTable1.getIdenticationInfo() )) {
                            for (DataDictionary dataDictionary : identicationInfoList) {
                                if (dataDictionary.getDicCode().equals( personTable1.getIdenticationInfo() )) {
                                    personTable1.setIdenticationInfo( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            personTable1.setIdenticationInfo( "" );
                        }
                    }

                }
                //判空 民族
                if (StringUtils.isNotBlank( personTable1.getEthnicity() )) {
                    if ((ListUtils.isNotNullAndEmptyList( ethnicityList ))) {
                        List<String> list = new ArrayList<>();
                        for (DataDictionary dataDictionary : ethnicityList) {
                            list.add( dataDictionary.getDicCode() );
                        }
                        if (list.contains( personTable1.getEthnicity() )) {
                            for (DataDictionary dataDictionary : ethnicityList) {
                                if (dataDictionary.getDicCode().equals( personTable1.getEthnicity() )) {
                                    personTable1.setEthnicity( dataDictionary.getDicName() );
                                }
                            }
                        } else {
                            personTable1.setEthnicity( "" );
                        }
                    }

                }
            }
        }
        return personTableList;
    }

    /**
     * 根据人员类型编码查询人员类型
     *
     * @param identicationinfo
     * @return
     */
    @Override
    public List<DataDictionary> selectPersonTypeList(String identicationinfo) {
        return dataDictionaryMapper.setlectDataDictionaryList( identicationinfo );

    }

    @Override
    public PersonTable selectPersonTableByIdentityTypeCode(String identityTypeCode) {
        return interfaceManagementMapper.selectPersonTableByIdentityTypeCode(identityTypeCode);
    }

    @Override
    public List<PersonFaceInfomationTable> queryPersonFaceInfomationTableByPersonId(Integer personId) {
        return interfaceManagementMapper.queryPersonFaceInfomationTableByPersonId( personId );
    }

    /**
     * 查询未删除的人员条数
     *
     * @param personTable
     * @return
     */
    @Override
    public int dataCount(PersonTable personTable) {
        return interfaceManagementMapper.dataCountALL( personTable );
    }

    /**
     * 根据字典类型和code获取名称
     *
     * @param dataDictionary
     * @return
     */
    @Override
    public DataDictionary queryDicNameByDicTypeAndDicCode(DataDictionary dataDictionary) {
        return interfaceManagementMapper.queryDicNameByDicTypeAndDicCode( dataDictionary );
    }

}
