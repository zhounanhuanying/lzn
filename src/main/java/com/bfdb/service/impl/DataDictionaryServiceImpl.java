package com.bfdb.service.impl;

import com.bfdb.entity.*;
import com.bfdb.mapper.DataDictionaryMapper;
import com.bfdb.mapper.DataPermissionMapper;
import com.bfdb.mapper.PersonnelInterfaceMapper;
import com.bfdb.service.DataDictionaryService;
import com.bfdb.service.PersonTableService;
import com.bfdb.untils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    @Autowired
    PersonnelInterfaceMapper personnelInterfaceMapper;

    //数据权限
    @Autowired
    DataPermissionMapper dataPermissionMapper;

    @Autowired
    private PersonTableService personTableService;

    @Override
    public List<DataDictionary> getDataDictionaryList(Map<String, Object> params) {
        //查询字典表
        List<DataDictionary> dataDictionaryList = dataDictionaryMapper.getDataDictionaryList( params );
        // 查询字典类型
        List<DataDictionary> dataDictionaryByDicCodeList = dataDictionaryMapper.getDataDictionaryByDicCodeList();
        //循环判断字典类型
        for (DataDictionary dataDictionary : dataDictionaryList) {
            for (DataDictionary dictionary : dataDictionaryByDicCodeList) {
                if (dictionary.getDicCode().equals( dataDictionary.getDicType() )) {
                    dataDictionary.setDicTypes( dataDictionary.getDicType() );
                    dataDictionary.setDicType( dictionary.getDicName() );
                }

            }
        }

        return dataDictionaryList;
    }

    @Override
    public int dataCount(Map<String, Object> params) {
        return dataDictionaryMapper.dataCount( params );
    }

    @Override
    public int updateDataDictionary(Map<String, Object> params) {
        return dataDictionaryMapper.updateDataDictionary( params );
    }

    /**
     * 新增数据字典
     *
     * @param dataDictionary
     * @return
     */
    @Override
    public int insertDataDictionary(DataDictionary dataDictionary) {
        DataPermission dataPermission = null;
        int i = 0;
        //此代码为了验证数据是否有重复的code码
        String[] allDicCode = dataDictionaryMapper.getAllDicCode( dataDictionary.getDicType() );
        String dicCodePar = dataDictionary.getDicCode();
        if (dataDictionary.getDicType() == null || "".equals( dataDictionary.getDicType() )) {
            return -4;
        }
        for (String dicCode : allDicCode) {
            if (dicCode.equals( dicCodePar )) {
                return -1;
            }
        }
        //新增数据字典信息
        i = dataDictionaryMapper.insertDataDictionary( dataDictionary );

        //判断  只有所在部门或者院系才有接口数据权限
        if ("department".equals( dataDictionary.getDicType() )) {
            //查询接口信息
            List<PersonnelInterface> personnelInterfaces = personnelInterfaceMapper.selectPersonnelInterfaceAll();
            dataPermission = new DataPermission();
            dataPermission.setPermissionName( dataDictionary.getDicName() );
            dataPermission.setPid( 0 );
            //随机生成一个数
            dataPermission.setPermissionCode( String.format( "%06d", getRandomRange( 1000000, 1000 ) ) + ":root" );
            dataPermission.setIconAddress( "iconfont icon-icon-test" );
            dataPermission.setPopupWay( dataDictionary.getDicCode() );
            //新增一个数据权限的父级
            int di = dataPermissionMapper.insertSelective( dataPermission );
            if (di > 0) {
                dataPermission.setPermissionType( dataPermission.getPermissionId() );
                dataPermission.setVisitorUrl( "#" + dataPermission.getPermissionId() );
                //修改父级节点的相关字段
                int fi = dataPermissionMapper.updateByPrimaryKeySelective( dataPermission );
                if (fi > 0) {
                    int count = 1;
                    //循环添加接口信息
                    for (PersonnelInterface personnelInterface : personnelInterfaces) {
                        DataPermission dataPermission1 = new DataPermission();
                        dataPermission1.setPermissionName( personnelInterface.getInterfaceName() );
                        dataPermission1.setPid( dataPermission.getPermissionId() );
                        dataPermission1.setPermissionType( dataPermission.getPermissionId() + count );
                        dataPermission1.setVisitorUrl( personnelInterface.getInterfaceUrl() );
                        dataPermission1.setPermissionCode( String.format( "%03d", getRandomRange( 1000000, 1000 ) ) + ":page" );
                        dataPermission1.setPopupWay( dataDictionary.getDicCode() );
//                        dataPermission1.setDefinition( "2" );
                        i = dataPermissionMapper.insertSelective( dataPermission1 );
                        count++;
                    }
                }
            }
        }
        return i;
    }

    /**
     * 随机数
     *
     * @param max
     * @param min
     * @return
     */
    public static int getRandomRange(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public int insertDataDictionaryType(DataDictionary dataDictionary) {
        dataDictionary.setDicType( "基础类型" );
        String[] allDicCode = dataDictionaryMapper.getAllDicCode( dataDictionary.getDicType() );
        //判断基础类型名称是否有重复的
        String[] allDicNmae = dataDictionaryMapper.getAllDicName( dataDictionary.getDicName() );
        String dicCodePar = dataDictionary.getDicCode();
        for (String dicCode : allDicCode) {
            if (dicCode.equals( dicCodePar )) {
                return -1;
            }
        }
        /**
         * 判断字典名称是否重复
         */
        String dicCodeName = dataDictionary.getDicName();
        for (String dicName : allDicNmae) {
            if (dicName.equals( dicCodeName )) {
                return -2;
            }
        }
        int i = dataDictionaryMapper.insertDataDictionary( dataDictionary );
        if (i > 0) {
            i = 0;
        }
        return i;
    }

    /**
     * 单个删除字典表的信息
     *
     * @param dicId
     * @return
     */
    @Override
    public int deleteDataDictionaryById(String dicId) {
        int i = 0;
        //查询weizhi信息
        DataDictionary dataDictionary = dataDictionaryMapper.setlectByUnknown( Integer.valueOf( dicId ) );
        //如果为空就正常删除
        if (dataDictionary == null) {
            i = dataDictionaryMapper.deleteDataDictionaryById( dicId );
        } else {
            return i = -1;
        }
        return i;
    }

    @Override
    public List<DataDictionary> getAllDicType() {
        return dataDictionaryMapper.getAllDicType();
    }

    /**
     * 批量删除
     *
     * @param dataDictionaryList
     * @return
     */
    @Override
    public int deleteDictionaryList(List<DataDictionary> dataDictionaryList) {
        int i = 0;
        List<DataDictionary> deleteDataList = new ArrayList<>();
        for (DataDictionary dic : dataDictionaryList) {
            if ("基础类型".equals( dic.getDicType() )) {
                deleteDataList.add( dic );
            } else {
                //查询weizhi信息   未知无法删除
                DataDictionary dataDictionary = dataDictionaryMapper.setlectByUnknown( dic.getDicId() );
                PersonTable personTableVo = new PersonTable();
                if (dataDictionary == null) {
                    if ("department".equals( dic.getDicTypes() )) {
                        //根据所在部门的code码查询人员信息
                        personTableVo.setDepartments( dic.getDicCode() );
                        return commonPersonTable( personTableVo, dic );
                    } else if ("Education".equals( dic.getDicTypes() )) {
                        //根据学历code码查询人员信息
                        personTableVo.setStudentLevel( dic.getDicCode() );
                        return commonPersonTable( personTableVo, dic );
                    } else if ("grade".equals( dic.getDicTypes() )) {
                        personTableVo.setGrade( dic.getDicCode() );
                        //学级code码查询人员信息
                        return commonPersonTable( personTableVo, dic );
                    } else if ("ethnicity".equals( dic.getDicTypes() )) {
                        personTableVo.setEthnicity( dic.getDicCode() );
                        //民族code码查询人员信息
                        return commonPersonTable( personTableVo, dic );
                    } else if ("personType".equals( dic.getDicTypes() )) {
                        personTableVo.setIdenticationInfo( dic.getDicCode() );
                        //人员code码查询人员信息
                        return commonPersonTable( personTableVo, dic );
                    } else if ("sex".equals( dic.getDicTypes() )) {
                        personTableVo.setGender( Integer.valueOf( dic.getDicCode() ) );
                        //性别code码查询人员信息
                        return commonPersonTable( personTableVo, dic );
                    } else if ("dataSource".equals( dic.getDicTypes() )) {
                        //数据来源code码查询人员信息
                        List<PersonFaceInfomationTable> personFaceInfomationTableList = personTableService.selectDataSource( dic.getDicCode() );
                        //判断所在部门下有没有人员信息
                        if (ListUtils.isNotNullAndEmptyList( personFaceInfomationTableList )) {
                            return i = -2;
                        } else {
                            //删除所在部门除外的部门信息
                            dataDictionaryMapper.deleteDataDictionaryById( dic.getDicId().toString() );
                            //删除数据权限相关信息
                            dataPermissionMapper.deleteBydataPermission( dic.getDicCode() );
                            i = 1;
                        }
                    } else {
                        //判断所在部门下有没有人员信息
                        //删除所在部门除外的部门信息
                        dataDictionaryMapper.deleteDataDictionaryById( dic.getDicId().toString() );
                        //删除数据权限相关信息
                        dataPermissionMapper.deleteBydataPermission( dic.getDicCode() );
                        i = 1;
                    }


                } else {
                    return i = -1;
                }
                if (!(i > 0)) {
                    return 0;
                }
            }
        }
        //判断字典类型不能删除
        if (ListUtils.isNotNullAndEmptyList( deleteDataList )) {
            return i = -3;
        }
        return 1;
    }

    /**
     * 字典表中  批量删除  公共方法
     *
     * @param personTableVo
     * @param dic
     * @return
     */
    private int commonPersonTable(PersonTable personTableVo, DataDictionary dic) {
        int i = 0;
        List<PersonTable> personTableList = personTableService.selectPersonTableByDepartments( personTableVo );
        //判断人员信息
        if (ListUtils.isNotNullAndEmptyList( personTableList )) {
            return i = -2;
        } else {
            dataDictionaryMapper.deleteDataDictionaryById( dic.getDicId().toString() );
            dataPermissionMapper.deleteBydataPermission( dic.getDicCode() );
            i = 1;
        }
        return i;

    }

    /**
     * 根据字典类型查询字典信息
     *
     * @param dicType
     * @return
     */
    @Override
    public List<DataDictionary> setlectDataDictionaryList(String dicType) {
        return dataDictionaryMapper.setlectDataDictionaryList( dicType );
    }

    @Override
    public List<DataDictionary> setlectDataDictionaryListTest() {
        return dataDictionaryMapper.setlectDataDictionaryListTest();
    }

    /**
     * 查询未知的字典信息
     * @return
     * @param dicType
     */
    @Override
    public DataDictionary setlectDataDictionaryByweizhi(String dicType) {
        return dataDictionaryMapper.setlectDataDictionaryByweizhi(dicType);
    }

    /**
     * 根据办公地点名称查询办公信息
     *
     * @param dicType
     * @param departments
     * @return
     */
    @Override
    public DataDictionary selectDataDictionaryBadname(String dicType, String departments) {
        return dataDictionaryMapper.selectDataDictionaryBadname(dicType,departments);
    }

    @Override
    public DataDictionary selectDataDictionaryByDicTypeAndDicName(DataDictionary dataDictionary) {
        return dataDictionaryMapper.selectDataDictionaryByDicTypeAndDicName(dataDictionary);
    }

    @Override
    public Map<String, Object> updataDataDictionaryType(DataDictionary dataDictionary) {
        Map<String,Object> map = new HashMap<>();
        map.put("code",dataDictionaryMapper.updataDataDictionaryType(dataDictionary));
        return map;
    }

    @Override
    public String queryDataDictionaryByName(String name) {
        return dataDictionaryMapper.queryDataDictionaryByName(name);
    }

    @Override
    public String queryDataDictionaryByCode(String dicCode) {
        return dataDictionaryMapper.queryDataDictionaryByCode(dicCode);
    }
}
