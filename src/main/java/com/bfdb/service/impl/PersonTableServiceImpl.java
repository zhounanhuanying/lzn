package com.bfdb.service.impl;

import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.mapper.*;
import com.bfdb.service.PersonFaceInfomationTableService;
import com.bfdb.service.PersonTableService;
import com.bfdb.untils.Config;
import com.bfdb.untils.ListUtils;
import com.bfdb.untils.PersonDataOperationTableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;


@Service
public class PersonTableServiceImpl implements PersonTableService {
    @Autowired
    private PersonTableMapper personTableMapper;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;
    @Autowired
    private PersonDataOperationTableMapper   personDataOperationTableMapper;
    @Autowired
    private PersonFaceInfomationTableService personFaceInfomationTableService;
    @Autowired
    private BaseOrganizitionMapper baseOrganizitionMapper;

    @Resource
    private PersonFaceInfomationTableMapper personFaceInfomationTableMapper;
    /**
     * 新增人脸信息  和 人证核验终端共用
     * @param personNewTable
     * @return
     */
    @Override
    public int insertPersonNewTable(PersonTable personNewTable) {
        //注意replaceAll前面的是正则表达式
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        //创建时间
        if(personNewTable.getCreateTime()==null){
            personNewTable.setCreateTime( new Date(  ) );
        }
         personNewTable.setPersonCode( uuid);
        personNewTable.setIsDeleted(1);//是否删除(1:未删除，2:假删除)
        if(StringUtils.isBlank( personNewTable.getDepartments() )){
            personNewTable.setDepartments( "weizhi" );
        }
        return personTableMapper.insertSelective( personNewTable );
    }

    @Override
    public List<PersonTable> queryPersonTableListByIdentification(Map<String, Object> map) {
        //查询人员集合信息
        List<PersonTable>  personTableList = personTableMapper.queryPersonTableListByIdentification(map);
        //查询年级集合信息
        List<BaseOrganizition> baseOrganizitionPNameList = baseOrganizitionMapper.queryBaseOrganizitionByOrgType(2);
        //查询班级集合信息
        List<BaseOrganizition> baseOrganizitionNameList = baseOrganizitionMapper.queryBaseOrganizitionByOrgType(3);
        //查询所在部门或院系信息
        List<DataDictionary>  departmentsList = dataDictionaryMapper.setlectDataDictionaryList( Constant.DEPARTMENT );
        //查询学历
        List<DataDictionary>  studentLevelList = dataDictionaryMapper.setlectDataDictionaryList( Constant.EDUCATION );
        //查询学级
        List<DataDictionary>  gradeList = dataDictionaryMapper.setlectDataDictionaryList( Constant.GRADE );
        //查询人员信息
        List<DataDictionary> identicationInfoList = dataDictionaryMapper.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
        //查询民族
        List<DataDictionary> ethnicityList = dataDictionaryMapper.setlectDataDictionaryList( Constant.ETHNICITY );
        //查询性别
        List<DataDictionary> sexList = dataDictionaryMapper.setlectDataDictionaryList( Constant.SEX );

        /**
         *判断是否为空
         * 以下代码，是为了从字典表中查出相关数据，进行对比显现
         */
        if(ListUtils.isNotNullAndEmptyList(personTableList)){
            for(PersonTable  personTable1:personTableList){
                //判空 学历
                if(StringUtils.isNotBlank(personTable1.getStudentLevel())){
                    if((ListUtils.isNotNullAndEmptyList(studentLevelList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:studentLevelList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getStudentLevel())){
                            for(DataDictionary dataDictionary:studentLevelList){
                                if(dataDictionary.getDicCode().equals(personTable1.getStudentLevel()  )){
                                    personTable1.setStudentLevel( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setStudentLevel("");
                        }
                    }
                }
                //对比班级
                if(StringUtils.isNotBlank(personTable1.getOrganizitionId())){
                    if((ListUtils.isNotNullAndEmptyList(baseOrganizitionNameList))){
                        List<String> list=new ArrayList<>();
                        for(BaseOrganizition baseOrganizition:baseOrganizitionNameList){
                            list.add(String.valueOf(baseOrganizition.getId()));
                        }
                        if(list.contains(personTable1.getOrganizitionId())){
                            for(BaseOrganizition baseOrganizition:baseOrganizitionNameList){
                                if(baseOrganizition.getId() == Integer.valueOf(personTable1.getOrganizitionId())){
                                    personTable1.setOrgName( baseOrganizition.getOrgName() );
                                }
                            }
                        }else {
                            personTable1.setOrgName("");
                        }
                    }
                }
                //对比年级
                if(StringUtils.isNotBlank(personTable1.getGrade())){
                    if((ListUtils.isNotNullAndEmptyList(baseOrganizitionPNameList))){
                        List<String> list=new ArrayList<>();
                        for(BaseOrganizition baseOrganizition:baseOrganizitionPNameList){
                            list.add(String.valueOf(baseOrganizition.getId()));
                        }
                        if(list.contains(personTable1.getGrade())){
                            for(BaseOrganizition baseOrganizition:baseOrganizitionPNameList){
                                if(baseOrganizition.getId() == Integer.valueOf(personTable1.getGrade())){
                                    personTable1.setGradeName( baseOrganizition.getOrgName() );
                                }
                            }
                        }else {
                            personTable1.setGradeName("");
                        }
                    }
                }

                //判空 学级
                if(StringUtils.isNotBlank(personTable1.getGrade())){
                    if((ListUtils.isNotNullAndEmptyList(gradeList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:gradeList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getGrade())){
                            for(DataDictionary dataDictionary:gradeList){
                                if(dataDictionary.getDicCode().equals(personTable1.getGrade()  )){
                                    personTable1.setGrade( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setGrade("");
                        }
                    }

                }
                //判空 所在部门或院系
                if(StringUtils.isNotBlank(personTable1.getDepartments())){
                    if((ListUtils.isNotNullAndEmptyList(departmentsList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:departmentsList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getDepartments())){
                            for(DataDictionary dataDictionary:departmentsList){
                                if(dataDictionary.getDicCode().equals(personTable1.getDepartments()  )){
                                    personTable1.setDepartments( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setDepartments("");
                        }
                    }
                }
                //判空 人员类型
                if(StringUtils.isNotBlank(personTable1.getIdenticationInfo())){
                    if((ListUtils.isNotNullAndEmptyList(identicationInfoList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:identicationInfoList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getIdenticationInfo())){
                            for(DataDictionary dataDictionary:identicationInfoList){
                                if(dataDictionary.getDicCode().equals(personTable1.getIdenticationInfo()  )){
                                    personTable1.setIdenticationInfo( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setIdenticationInfo("");
                        }
                    }
                }
                //判空 民族
                if(StringUtils.isNotBlank(personTable1.getEthnicity())){
                    if((ListUtils.isNotNullAndEmptyList(ethnicityList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:ethnicityList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getEthnicity())){
                            for(DataDictionary dataDictionary:ethnicityList){
                                if(dataDictionary.getDicCode().equals(personTable1.getEthnicity()  )){
                                    personTable1.setEthnicity( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setEthnicity("");
                        }
                    }
                }
                //判空 性别
                if(personTable1.getGender()!=null){
                    if((ListUtils.isNotNullAndEmptyList(sexList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:sexList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getGender().toString())){
                            for(DataDictionary dataDictionary:sexList){
                                if(dataDictionary.getDicCode().equals(personTable1.getGender().toString())){
                                    personTable1.setGenders( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setGenders("");
                        }
                    }
                }
            }
        }
        return personTableList;
    }

    @Override
    public int dataCountByIdentification(Map<String, Object> map) {
        return personTableMapper.dataCountByIdentification( map );
    }

    @Override
    public List<BasePark> getPersonByParkId(PersonTable personTable) {
        return personTableMapper.getPersonByParkId(personTable);
    }

    @Override
    public PersonTable queryPersonTableByOrgId(PersonTable personNewTable) {
        //查询选择的年级信息
        BaseOrganizition baseOrganizitionYName = baseOrganizitionMapper.queryOrganizitionById(personNewTable.getDepartments());
        //查询选择的年级信息
        BaseOrganizition baseOrganizitionEName = baseOrganizitionMapper.queryOrganizitionById(personNewTable.getGrade());
        //查询选择的班级信息
        BaseOrganizition baseOrganizitionSName = baseOrganizitionMapper.queryOrganizitionById(personNewTable.getOrganizitionId());

        /**
         *判断是否为空
         * 以下代码，是为了从组织机构表中查出相关数据，进行对比显现
         */
        if(personNewTable != null || !"".equals(personNewTable)) {
            //对比学校
            if (StringUtils.isNotBlank(personNewTable.getDepartments())) {
                if ((baseOrganizitionYName != null)) {
                    if (baseOrganizitionYName.getId() == Integer.valueOf(personNewTable.getDepartments())) {
                        personNewTable.setDepartmentss(baseOrganizitionYName.getOrgCode());
                    }
                } else {
                    personNewTable.setDepartmentss("");
                }
            }

            //对比班级
            if (StringUtils.isNotBlank(personNewTable.getOrganizitionId())) {
                if ((baseOrganizitionSName != null)) {
                    if (baseOrganizitionSName.getId() == Integer.valueOf(personNewTable.getOrganizitionId())) {
                        personNewTable.setOrgName(baseOrganizitionSName.getOrgCode());
                    }
                } else {
                    personNewTable.setOrgName("");
                }
            }

            //对比年级
            if (StringUtils.isNotBlank(personNewTable.getGrade())) {
                if ((baseOrganizitionEName != null)) {
                    if (baseOrganizitionEName.getId() == Integer.valueOf(personNewTable.getGrade())) {
                        personNewTable.setGradeName(baseOrganizitionEName.getOrgCode());
                    }
                } else {
                    personNewTable.setGradeName("");
                }
            }
        }
        return personNewTable;
    }

    @Override
    public ExcelPersonTable queryExcelPersonTableByOrgId(ExcelPersonTable excelPersonTable) {
        //查询选择的年级信息
        BaseOrganizition baseOrganizitionYName = baseOrganizitionMapper.queryOrganizitionById(excelPersonTable.getDepartments());
        //查询选择的年级信息
        BaseOrganizition baseOrganizitionEName = baseOrganizitionMapper.queryOrganizitionById(excelPersonTable.getGrade());
        //查询选择的班级信息
        BaseOrganizition baseOrganizitionSName = baseOrganizitionMapper.queryOrganizitionById(excelPersonTable.getSchoolclass());

        /**
         *判断是否为空
         * 以下代码，是为了从组织机构表中查出相关数据，进行对比显现
         */
        if(excelPersonTable != null || !"".equals(excelPersonTable)) {
            //对比学校
            if (StringUtils.isNotBlank(excelPersonTable.getDepartments())) {
                if ((baseOrganizitionYName != null)) {
                    if (baseOrganizitionYName.getId() == Integer.valueOf(excelPersonTable.getDepartments())) {
                        excelPersonTable.setDepartmentss(baseOrganizitionYName.getOrgCode());
                    }
                } else {
                    excelPersonTable.setDepartmentss("");
                }
            }

            //对比班级
            if (StringUtils.isNotBlank(excelPersonTable.getSchoolclass())) {
                if ((baseOrganizitionSName != null)) {
                    if (baseOrganizitionSName.getId() == Integer.valueOf(excelPersonTable.getSchoolclass())) {
                        excelPersonTable.setOrgName(baseOrganizitionSName.getOrgCode());
                    }
                } else {
                    excelPersonTable.setOrgName("");
                }
            }

            //对比年级
            if (StringUtils.isNotBlank(excelPersonTable.getGrade())) {
                if ((baseOrganizitionEName != null)) {
                    if (baseOrganizitionEName.getId() == Integer.valueOf(excelPersonTable.getGrade())) {
                        excelPersonTable.setGradeName(baseOrganizitionEName.getOrgCode());
                    }
                } else {
                    excelPersonTable.setGradeName("");
                }
            }
        }
        return excelPersonTable;
    }

    /**
     * 查询人员信息
     *
     * @param personTable
     * @param limit
     * @param page
     * @return
     */
    @Override
    public List<PersonTable> queryPersonTableList(PersonTable personTable, Integer limit, Integer page) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("limit", limit);
        map.put("page", page);
        map.put("personName", personTable.getPersonName());
        map.put("identityTypeCode", personTable.getIdentityTypeCode());
        map.put("identicationInfo", personTable.getIdenticationInfo());
        map.put("departments", personTable.getDepartments());
        map.put("isDeleted", personTable.getIsDeleted());
        //查询人员集合信息
        List<PersonTable>  personTableList=personTableMapper.queryPersonTableList(map);
        //查询所在部门或院系信息
        List<DataDictionary>  departmentsList=dataDictionaryMapper.setlectDataDictionaryList( Constant.DEPARTMENT );
       //查询学历
        List<DataDictionary>  studentLevelList=dataDictionaryMapper.setlectDataDictionaryList( Constant.EDUCATION );
        //查询学级
        List<DataDictionary>  gradeList=dataDictionaryMapper.setlectDataDictionaryList( Constant.GRADE );
        //查询人员信息
        List<DataDictionary> identicationInfoList = dataDictionaryMapper.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
        //查询民族
        List<DataDictionary> ethnicityList = dataDictionaryMapper.setlectDataDictionaryList( Constant.ETHNICITY );
        //查询性别
        List<DataDictionary> sexList = dataDictionaryMapper.setlectDataDictionaryList( Constant.SEX );

        /**
         *判断是否为空
         * 以下代码，是为了从字典表中查出相关数据，进行对比显现
         */
        if(ListUtils.isNotNullAndEmptyList(personTableList)){
            for(PersonTable  personTable1:personTableList){
                 //判空 学历
                if(StringUtils.isNotBlank(personTable1.getStudentLevel())){
                     if((ListUtils.isNotNullAndEmptyList(studentLevelList))){
                         List<String> list=new ArrayList<>();
                         for(DataDictionary dataDictionary:studentLevelList){
                             list.add(dataDictionary.getDicCode());
                         }
                         if(list.contains(personTable1.getStudentLevel())){
                             for(DataDictionary dataDictionary:studentLevelList){
                                 if(dataDictionary.getDicCode().equals(personTable1.getStudentLevel()  )){
                                     personTable1.setStudentLevel( dataDictionary.getDicName() );
                                 }
                             }
                         }else {
                             personTable1.setStudentLevel("");
                         }
                     }

                }
                 //判空 学级
                if(StringUtils.isNotBlank(personTable1.getGrade())){
                     if((ListUtils.isNotNullAndEmptyList(gradeList))){
                         List<String> list=new ArrayList<>();
                         for(DataDictionary dataDictionary:gradeList){
                             list.add(dataDictionary.getDicCode());
                         }
                         if(list.contains(personTable1.getGrade())){
                             for(DataDictionary dataDictionary:gradeList){
                                 if(dataDictionary.getDicCode().equals(personTable1.getGrade()  )){
                                     personTable1.setGrade( dataDictionary.getDicName() );
                                 }
                             }
                         }else {
                             personTable1.setGrade("");
                         }
                     }

                }
                 //判空 所在部门或院系
                if(StringUtils.isNotBlank(personTable1.getDepartments())){
                     if((ListUtils.isNotNullAndEmptyList(departmentsList))){
                         List<String> list=new ArrayList<>();
                         for(DataDictionary dataDictionary:departmentsList){
                             list.add(dataDictionary.getDicCode());
                         }
                         if(list.contains(personTable1.getDepartments())){
                             for(DataDictionary dataDictionary:departmentsList){
                                 if(dataDictionary.getDicCode().equals(personTable1.getDepartments()  )){
                                     personTable1.setDepartments( dataDictionary.getDicName() );
                                 }
                             }
                         }else {
                             personTable1.setDepartments("");
                         }
                     }

                }
                 //判空 人员类型
                if(StringUtils.isNotBlank(personTable1.getIdenticationInfo())){
                     if((ListUtils.isNotNullAndEmptyList(identicationInfoList))){
                         List<String> list=new ArrayList<>();
                         for(DataDictionary dataDictionary:identicationInfoList){
                             list.add(dataDictionary.getDicCode());
                         }
                         if(list.contains(personTable1.getIdenticationInfo())){
                             for(DataDictionary dataDictionary:identicationInfoList){
                                 if(dataDictionary.getDicCode().equals(personTable1.getIdenticationInfo()  )){
                                     personTable1.setIdenticationInfo( dataDictionary.getDicName() );
                                 }
                             }
                         }else {
                             personTable1.setIdenticationInfo("");
                         }
                     }

                }
                //判空 民族
                if(StringUtils.isNotBlank(personTable1.getEthnicity())){
                    if((ListUtils.isNotNullAndEmptyList(ethnicityList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:ethnicityList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getEthnicity())){
                            for(DataDictionary dataDictionary:ethnicityList){
                                if(dataDictionary.getDicCode().equals(personTable1.getEthnicity()  )){
                                    personTable1.setEthnicity( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setEthnicity("");
                        }
                    }

                }
                //判空 性别
                if(personTable1.getGender()!=null){
                    if((ListUtils.isNotNullAndEmptyList(sexList))){
                        List<String> list=new ArrayList<>();
                        for(DataDictionary dataDictionary:sexList){
                            list.add(dataDictionary.getDicCode());
                        }
                        if(list.contains(personTable1.getGender().toString())){
                            for(DataDictionary dataDictionary:sexList){
                                if(dataDictionary.getDicCode().equals(personTable1.getGender().toString())){
                                    personTable1.setGenders( dataDictionary.getDicName() );
                                }
                            }
                        }else {
                            personTable1.setGenders("");
                        }
                    }

                }
            }
        }
        return personTableList;
    }

    /**
     * 查询人员条数
     * @param
     * @return
     */
    @Override
    public int dataCount(PersonTable personTable) {
        return personTableMapper.dataCount(personTable);
    }

    /**
     * 修改人员信息
     * @param personNewTable
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(PersonTable personNewTable) {
        personNewTable.setModifyTime( new Date(  ) );
        if(StringUtils.isBlank( personNewTable.getDepartments() )){
            personNewTable.setDepartments( "weizhi" );
        }
        return personTableMapper.updateByPrimaryKeySelective( personNewTable );
    }

    /**
     * 删除人员信息
     * @param personId
     * @return
     */
    @Override
    public int deletePersonTableByUserId(Integer[] personId) {
        PersonTable personTable=null;
        List<PersonFaceInfomationTable> personFaceInfomationTables = personFaceInfomationTableMapper.selectPersonTableByImagePersonId(personId);
        //添加人员更新日志
        PersonDataOperationTable  personDataOperationTable=null;
        for(Integer personid:personId){
            personTable= personTableMapper.selectPersonTableBypersonId(personid);
            //添加人员更新日志
              personDataOperationTable=PersonDataOperationTableUtils.deletePersonDataOperationTable(personTable);
            personDataOperationTableMapper.insertSelective(personDataOperationTable);
        }
        // 删除图片物理地址
        String filePath = Config.getPhotoUrl("filePath"); //指定存储地址
        personFaceInfomationTables.forEach(p ->{
            if (p.getCampusCardAddress()!=null){
                File file = new File(filePath+p.getCampusCardAddress());
                // 路径不为空则进行删除     
                if (file.exists()) {
                    file.delete();
                }
            }
            if (p.getFaceAddress()!=null){
                File file = new File(filePath+p.getFaceAddress());
                if (file.exists()) {
                    file.delete();
                }
            }
            if (p.getIdcardImage()!=null){
                File file = new File(filePath+p.getIdcardImage());
                if (file.exists()) {
                    file.delete();
                }
            }
        });

        return personTableMapper.deletePersonTableByUserId(personId);
    }

    /**
     * 人员信息导出excel表格
     * @param map
     * @return
     */
    @Override
    public List<PersonTable> getData(Map<String,Object> map) {
        return personTableMapper.getData(map);
    }

    /**
     * 根据人员personId查询图片集合
     * @param personId
     * @return
     */
    @Override
    public List<PersonFaceInfomationTable> queryPersonFaceInfomationTableByPersonId(Integer personId) {
        List<PersonFaceInfomationTable> personFaceInfomationTables=personTableMapper.queryPersonFaceInfomationTableByPersonId(personId);
        //查询数据来源
        List<DataDictionary>  dataSourceList=dataDictionaryMapper.setlectDataDictionaryList( Constant.DATASOURCE );
        if(ListUtils.isNotNullAndEmptyList(personFaceInfomationTables)){
            for(PersonFaceInfomationTable  personFaceInfomationTable:personFaceInfomationTables){
                //判空 学历
                if(StringUtils.isNotBlank(personFaceInfomationTable.getDataSource())){
                    if((ListUtils.isNotNullAndEmptyList(dataSourceList))){
                        for(DataDictionary dataDictionary:dataSourceList){
                            if(dataDictionary.getDicCode().equals(personFaceInfomationTable.getDataSource()  )){
                                personFaceInfomationTable.setDataSource( dataDictionary.getDicName() );
                            }else {
                                personFaceInfomationTable.setDataSource(personFaceInfomationTable.getDataSource());
                            }
                        }
                    }
                }
            }
        }
        return personFaceInfomationTables;
    }

    /**
     * 根据身份证号查询人员信息
     * @param identityNo
     * @return
     */
    @Override
    public PersonTable queryPersonTableByIdentityNo(String identityNo) {
        return personTableMapper.queryPersonTableByIdentityNo(identityNo);
    }

    /**
     * 根据多个personId删除人员基本信息
     * @param personId
     */
    @Override
    public void deletePersontableByPersonId(Integer[] personId) {
        personTableMapper.deletePersontableByPersonId(personId);
    }

    /**
     * 修改学级信息和年级
     * @param personId
     */
    @Override
    public void updateGradeAndStudentLevlByPersonId(Integer personId) {
        personTableMapper.updateGradeAndStudentLevlByPersonId(personId);
    }

    /**
     * 根据人员id查询人员信息
     * @param personId
     * @return
     */
    @Override
    public PersonTable selectPersonTableById(Integer personId) {
        return personTableMapper.selectPersonTableBypersonId(personId);
    }

    /**
     * 根据人员身份证号查询人员信息
     * @param identityNo
     * @return
     */
    @Override
    public PersonTable selectPersonTableByIdentityNo(String identityNo) {
        return personTableMapper.selectPersonTableByIdentityNo(identityNo);
    }

    /**
     * 根据人员身份证号修改人员信息
     * @param personNewTable
     * @return
     */
    @Override
    public int updateByPrimaryKeyIdentityNo(PersonTable personNewTable) {
        personNewTable.setModifyTime( new Date(  ) );
        return personTableMapper.updateByPrimaryKeyIdentityNo(personNewTable);
    }

    /**
     * 根据人员教工号查询人员信息
     * @param identityTypeCode
     * @return
     */

    @Override
    public PersonTable selectPersonTableByIdentityTypeCode(String identityTypeCode) {
        return personTableMapper.selectPersonTableByIdentityTypeCode(identityTypeCode);
    }

    /**
     * 根据人员personId查询人员信息
     * @param personId
     * @return
     */
    @Override
    public PersonTable selectByPersonId(Integer personId) {
        return personTableMapper.selectByPersonId(personId);
    }

    /**
     * 查询教工号或学号或一卡通号是否已存在
     * @param identityTypeCode
     * @return
     */
    @Override
    public PersonTable selectByIdentityTypeCode(String identityTypeCode) {
        return personTableMapper.selectByIdentityTypeCode(identityTypeCode);
    }

    /**
     * 查询人员信息总条数
     * @return
     */
    @Override
    public Integer getTotal() {
        return personTableMapper.getTotal();
    }

    /**
     * 微信公众号根据openId查询人员基本信息
     * @param openId
     * @return
     */
    @Override
    public PersonTable selectPersonTableByopenId(String openId) {
        return personTableMapper.selectPersonTableByopenId(openId);
    }

    /**
     * 微信公众号根据code查询全部人员信息
     * @param code
     * @return
     */
    @Override
    public List<PersonTable> selectPersonTableListByopenId(String code) {
        return personTableMapper.selectPersonTableListByopenId(code);
    }

    /**
     * 根据字典表中子节点的code码查询人员信息
     * @param dicCode
     * @return
     */
    @Override
    public   List<PersonTable>  selectPersonTableByDepartments(PersonTable dicCode) {
        return personTableMapper.selectPersonTableByDepartments(dicCode);
    }

    /**
     * 查询人员数据来源
     * @param dicCode
     * @return
     */
    @Override
    public List<PersonFaceInfomationTable> selectDataSource(String dicCode) {
        return personFaceInfomationTableService.selectDataSource(dicCode);
    }

    /**
     * 根据 姓名 身份证 教工号查询该人员信息
     * @param excelPersonDelete
     * @return
     */
    @Override
    public PersonTable SelectDeletePersonId(ExcelPersonDelete excelPersonDelete) {
        return personTableMapper.SelectDeletePersonId(excelPersonDelete);
    }

    /**
     * 根据修改的人员personId查询人员信息
     * @param excelPersonUpdate
     * @return
     */
    @Override
    public PersonTable SelectUpdatePersonId(ExcelPersonUpdate excelPersonUpdate) {
        return personTableMapper.SelectUpdatePersonId(excelPersonUpdate);
    }

    /**
     * 根据修改的personId或身份证号或教工号查询人员信息
     * @param excelPersonUpdate
     * @return
     */
    @Override
    public PersonTable SelectUpdatePersonIdByIdentityNoOrIdentityTypeCode(ExcelPersonUpdate excelPersonUpdate) {
        return personTableMapper.SelectUpdatePersonIdByIdentityNoOrIdentityTypeCode(excelPersonUpdate);
    }

    /**
     * 根据人员姓名、身份证号、还有一卡通号，去查询
     * @param personTable
     * @return
     */
    @Override
    public List<PersonTable> selectByPersonNameIdentityNoIdentity(PersonTable personTable) {
        return personTableMapper.selectByPersonNameIdentityNoIdentity(personTable);
    }

}
