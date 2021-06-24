package com.bfdb.controller;


import com.alibaba.fastjson.JSON;
import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.mapper.BaseOrganizitionMapper;
import com.bfdb.mapper.BaseOrganizitionPersonMapper;
import com.bfdb.service.BaseOrganizitionService;
import com.bfdb.service.PersonDataOperationTableService;
import com.bfdb.service.PersonFaceInfomationTableService;
import com.bfdb.service.PersonTableService;
import com.bfdb.untils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * 人员信息管理
 */
@RestController
@RequestMapping("/personTable")
public class PersonTableController extends AbstractController {

    @Autowired
    private PersonTableService personTableService;
    @Autowired
    private PersonFaceInfomationTableService personFaceInfomationTableService;

    @Autowired
    PersonDataOperationTableService personDataOperationTableService;

    @Autowired
    private BaseOrganizitionPersonMapper baseOrganizitionPersonMapper;

    @Autowired
    private BaseOrganizitionService baseOrganizitionService;
    /**
     *
     * @param request
     * @param personName
     * @param identityTypeCode
     * @param identicationInfo
     * @param departments
     * @param isDeleted
     * @param identification
     * @param parkId
     * @return
     */
    @RequestMapping(value = "/getPersonTableAll", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryPersonTableList(HttpServletRequest request,String personName,String identityTypeCode,
                                                    String identicationInfo, String departments, Integer isDeleted,
                                                    String identification,String parkId) {
        Map<String, Object> resultMap = new HashMap<>();
        //获取分页条数信息
        Integer limit = Integer.parseInt( request.getParameter( "limit" ) );
        //获取页数
        Integer page = (Integer.parseInt( request.getParameter( "page" ) ) - 1) * limit;
        Integer statusNub = 0;
        String statusMessage = "success";
        List<PersonTable> personTableList = null;
        //返回结果集
        List<PersonTable> personTableList1 =null;
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("limit", limit);
            map.put("page", page);
            map.put("personName", personName);
            map.put("identityTypeCode", identityTypeCode);
            //map.put("departments", departments);
            map.put("identicationInfo",identicationInfo);
            map.put("isDeleted", 1);
            map.put("identification", identification);
            map.put("parkId",parkId);
            personTableList = personTableService.queryPersonTableListByIdentification( map);
            //查询人员条数
            int totalCount = personTableService.dataCountByIdentification( map );
            Constant.personTableList = personTableList1;
//            resultMap.put( "data", personTableList1 );
            resultMap.put( "data", personTableList );
            resultMap.put( "count", totalCount );
        } catch (Exception ex) {
            logger.error( "人员信息查询失败" + ex.getMessage() );
        }
        resultMap.put( "code", statusNub );
        resultMap.put( "msg", statusMessage );
        return resultMap;
    }

    /**
     * 根据园区ID查询人员及组织关系
     * @param personTable
     * @return
     */
    @RequestMapping(value = "/getPersonByParkId", method = RequestMethod.GET)
    @ResponseBody
    public List<BasePark> getPersonByParkId(PersonTable personTable){
        personTable.setIsDeleted(2);
        return personTableService.getPersonByParkId(personTable);
    }

    /**
     * 身份证正则验证
     * @param id
     * @return
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
     * H5 ： 新增人员基础信息
     *
     * @return
     */
    @RequestMapping(value = "/insertPersonNewTable", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> insertPersonNewTable(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        PersonDataOperationTable personDataOperationTable = null;
        try {
            //获取从前端传送的值
            String datats = request.getParameter( "datats" );
            //转换成json对象
            Datas datasArray = JSON.parseObject( datats, Datas.class );
            //人脸基础信息
            PersonTable personNewTable = null;
            //人员与组织机构关系
            BaseOrganizitionPerson baseOrganizitionPerson = null;
            //人脸照片信息
            List<PersonFaceInfomationTable> faceAddressList = null;
            //判断是否为空
            if (datasArray != null) {
                personNewTable = datasArray.getPersonNewTable();//人脸基础信息
                faceAddressList = datasArray.getFaceAddressList();//人脸图片地址 校园卡图片地址
            }
            /*//判断身份信息是否是学生
            if (!("2".equals( personNewTable.getIdenticationInfo() )) && !("3".equals( personNewTable.getIdenticationInfo() ))) {
                personNewTable.setStudentLevel( "" );
                personNewTable.setGrade( "" );
            }*/
            PersonTable IdentityNo = null;
            // 判断身份证号是否已存在
            if (personNewTable.getIdentityNo() != null && !"".equals( personNewTable.getIdentityNo() )) {
                IdentityNo = personTableService.selectPersonTableByIdentityNo( personNewTable.getIdentityNo() );
//                从身份证获取生日
                personNewTable.setBirthday( extractYearMonthDayOfIdCard( personNewTable.getIdentityNo() ) );
            }
            PersonTable IdentityTypeCode = null;
            //判断教师教工号是否重复
            if("7".equals(personNewTable.getIdenticationInfo())){
                // 判断教工号或一卡通号是否已存在
                if(personNewTable.getIdentityTypeCode()!=null && !"".equals(personNewTable.getIdentityTypeCode())){
                    IdentityTypeCode=personTableService.selectPersonTableByIdentityTypeCode(personNewTable.getIdentityTypeCode());
                }
            }
            if (IdentityNo == null && IdentityTypeCode==null) {
                if(personNewTable.getSchoolClass() != null && !"".equals(personNewTable.getSchoolClass())) {
                    personNewTable.setOrganizitionId(personNewTable.getSchoolClass());
                    personNewTable.setSchoolClass(personNewTable.getSchoolClass());
                    String gradeId = baseOrganizitionService.queryBaseOrganizitionByPid(personNewTable.getSchoolClass());
                    personNewTable.setGrade(gradeId);
                    String departmentssId = baseOrganizitionService.queryBaseOrganizitionByPid(gradeId);
                    personNewTable.setDepartments(departmentssId);
                    //判断新增人员身份  如果是学生进行人员唯一标识拼接
                    if("8".equals(personNewTable.getIdenticationInfo())){
                        //查询所选 学校/年级/班级 信息进行拼接 学校编码_年级编码_班级编码_学工号 = 人员唯一标识
                        PersonTable personNewTable1 = personTableService.queryPersonTableByOrgId(personNewTable);
                        if(personNewTable1 != null){
                            personNewTable.setPersonnelUniqueCode(personNewTable1.getOrgName()+"_"+personNewTable1.getIdentityTypeCode());
                        }
                    }
                    i = personTableService.insertPersonNewTable(personNewTable);
                    //判断是都为空
                    if (i != 0) {
                        //新增组织机构与人员关系表
                        baseOrganizitionPerson =new BaseOrganizitionPerson();
                        baseOrganizitionPerson.setOrganizitionId(personNewTable.getSchoolClass());
                        baseOrganizitionPerson.setPersonId(personNewTable.getPersonId());
                        baseOrganizitionPerson.setCreateTime(new Date());
                        baseOrganizitionPersonMapper.insertBaseOrganizitionPerson(baseOrganizitionPerson);
                        //重新组装成list集合
                        List<PersonFaceInfomationTable> personNewFaceInfomationTableList = new ArrayList<>();
                        //判断集合是否为空
                        if (ListUtils.isNotNullAndEmptyList(faceAddressList)) {
                            //循环遍历-----组装成新的集合
                            for (PersonFaceInfomationTable pnfi : faceAddressList) {
                                PersonFaceInfomationTable person = new PersonFaceInfomationTable();
                                String faceAddress = null;
                                person.setIdentification("2");
                                String campusCardAddress = null;
                                //人脸图片地址
                                if (StringUtils.isNotBlank(pnfi.getFaceAddress())) {
                                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpeg;base64,", "");
//                                faceAddress = Base64Utils.generateImage( faceAddress );
                                    boolean base64Encode = isBase64Encode(faceAddress);
                                    if (base64Encode) {
                                        faceAddress = Base64Utils.generateImage(faceAddress);
                                    }
                                }
                                //校园卡正面照地址
                                if (StringUtils.isNotBlank(pnfi.getCampusCardAddress())) {
                                    //campusCardAddress = pnfi.getCampusCardAddress().replace( "data:image/jpeg;base64,", "" );
                                    campusCardAddress = Base64Utils.baseurlPhotos(pnfi.getCampusCardAddress());
                                    campusCardAddress = Base64Utils.generateImage(campusCardAddress);
                                }
                                person.setCampusCardAddress(campusCardAddress);
                                person.setFaceAddress(faceAddress);
                                person.setDataSource("1");
                                person.setPersonId(personNewTable.getPersonId());
                                personNewFaceInfomationTableList.add(person);
                            }
                            //添加人员图片和一卡通照片
                            personFaceInfomationTableService.insertPersonNewFaceInfomationTable(personNewFaceInfomationTableList);
                            resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
                        }
                        //添加人员更新日志
                        personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable(personNewTable);
                        personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
//                resultMap.put( "description", Constant.RESPONSE_SUCCESS_DESCRIPTION );
                    }
                }else {
                    resultMap.put( "code", 3 );
                }
            } else {
                if(IdentityNo!=null){
                    resultMap.put( "code", 1 );
                }
                if(IdentityTypeCode!=null){
                    resultMap.put( "code", 2 );
                }

            }
        } catch (Exception ex) {
            logger.error( "新增人员基础信息失败！！" + ex.getMessage() );
        }
        return resultMap;
    }

    /**
     * 人脸特征值提取
     * @return
     */

    @RequestMapping(value = "/faceJianCe",method = RequestMethod.GET)
    public Map<String, Object> faceJianCe(@RequestParam("img") String img){

        HttpUtil.post("",img);

        return null;
    }
    /**
     * 人员管理页面： 新增人员基础信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/personInsertPersonNewTable", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> personInsertPersonNewTable(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        PersonDataOperationTable personDataOperationTable = null;
        try {
            //获取从前端传送的值
            String datats = request.getParameter( "datats" );
            //转换成json对象
            Datas datasArray = JSON.parseObject( datats, Datas.class );
            //人脸基础信息
            PersonTable personNewTable = null;
            //人员与组织机构关系
            BaseOrganizitionPerson baseOrganizitionPerson = null;
            //人脸照片信息
            List<PersonFaceInfomationTable> faceAddressList = null;
            //判断是否为空
            if (datasArray != null) {
                personNewTable = datasArray.getPersonNewTable();//人脸基础信息
                faceAddressList = datasArray.getFaceAddressList();//人脸图片地址 校园卡图片地址
            }
            //调用修改方法，更新数据库中的头像名
            //判断身份信息是否是学生
            if (!("2".equals( personNewTable.getIdenticationInfo() )) && !("3".equals( personNewTable.getIdenticationInfo() ))) {
                personNewTable.setStudentLevel( "" );
                personNewTable.setGrade( "" );
            }
            PersonTable IdentityNo = null;
//            PersonTable IdentityTypeCode=null;
            // 判断身份证号是否已存在
            if (personNewTable.getIdentityNo() != null && !"".equals( personNewTable.getIdentityNo() )) {
                IdentityNo = personTableService.selectPersonTableByIdentityNo( personNewTable.getIdentityNo() );
            }
            // 判断教工号或一卡通号是否已存在
            PersonTable IdentityTypeCode = null;
            if(personNewTable.getIdentityTypeCode()!=null && !"".equals(personNewTable.getIdentityTypeCode())){
                IdentityTypeCode=personTableService.selectPersonTableByIdentityTypeCode(personNewTable.getIdentityTypeCode());
            }
            if(IdentityNo==null && IdentityTypeCode==null){
//            if (IdentityNo == null) {
                //判断组织机构id有没有传过来
                if(personNewTable.getSchoolClass() != null && !"".equals(personNewTable.getSchoolClass())) {
                    personNewTable.setOrganizitionId(personNewTable.getSchoolClass());
                    personNewTable.setSchoolClass(personNewTable.getSchoolClass());
                    String gradeId = baseOrganizitionService.queryBaseOrganizitionByPid(personNewTable.getSchoolClass());
                    personNewTable.setGrade(gradeId);
                    String departmentssId = baseOrganizitionService.queryBaseOrganizitionByPid(gradeId);
                    personNewTable.setDepartments(departmentssId);
                    //判断新增人员身份  如果是学生进行人员唯一标识拼接
                    if("8".equals(personNewTable.getIdenticationInfo())){
                        //查询所选 学校/年级/班级 信息进行拼接 学校编码_年级编码_班级编码_学工号 = 人员唯一标识
                        PersonTable personNewTable1 = personTableService.queryPersonTableByOrgId(personNewTable);
                        if(personNewTable1 != null){
                            personNewTable.setPersonnelUniqueCode(personNewTable1.getOrgName()+"_"+personNewTable1.getIdentityTypeCode());
                        }
                    }
                    i = personTableService.insertPersonNewTable( personNewTable );
                    //判断是都为空
                    if (i != 0) {
                        //新增组织机构与人员关系表
                        baseOrganizitionPerson =new BaseOrganizitionPerson();
                        baseOrganizitionPerson.setOrganizitionId(personNewTable.getSchoolClass());
                        baseOrganizitionPerson.setPersonId(personNewTable.getPersonId());
                        baseOrganizitionPerson.setCreateTime(new Date());
                        baseOrganizitionPersonMapper.insertBaseOrganizitionPerson(baseOrganizitionPerson);
                        //重新组装成list集合
                        List<PersonFaceInfomationTable> personNewFaceInfomationTableList = new ArrayList<>();
                        //判断集合是否为空
                        if (ListUtils.isNotNullAndEmptyList( faceAddressList )) {
                            //循环遍历-----组装成新的集合
                            for (PersonFaceInfomationTable pnfi : faceAddressList) {
                                PersonFaceInfomationTable person = new PersonFaceInfomationTable();
                                person.setIdentification("2");
                                String faceAddress = null;
                                String campusCardAddress = null;
//                            String idcardImage = null;
                                String faceImage = null;
                                //人脸图片地址
                                if (StringUtils.isNotBlank( pnfi.getFaceAddress() )) {
                                    //faceAddress = pnfi.getFaceAddress().replace( "data:image/jpeg;base64,", "" );
                                    faceAddress=Base64Utils.baseurlPhotos(pnfi.getFaceAddress());
                                    faceAddress = Base64Utils.generateImage( faceAddress );
                                }
                                //校园卡正面照地址
                                if (StringUtils.isNotBlank( pnfi.getCampusCardAddress() )) {
                                    //campusCardAddress = pnfi.getCampusCardAddress().replace( "data:image/jpeg;base64,", "" );
                                    campusCardAddress=Base64Utils.baseurlPhotos(pnfi.getCampusCardAddress());
                                    campusCardAddress = Base64Utils.generateImage( campusCardAddress );
                                }
//                            if (StringUtils.isNotBlank( pnfi.getIdcardImage() )) {
//                                idcardImage = pnfi.getIdcardImage().replace( "data:image/jpeg;base64,", "" );
//                                idcardImage = Base64Utils.generateImage( idcardImage );
//                            }
                                //人证校验图片
                                if (StringUtils.isNotBlank( pnfi.getFaceImage() )) {
                                    //faceImage = pnfi.getFaceImage().replace( "data:image/jpeg;base64,", "" );
                                    faceImage=Base64Utils.baseurlPhotos(pnfi.getFaceImage());
                                    faceImage = Base64Utils.generateImage( faceImage );
                                }
                                person.setCampusCardAddress( campusCardAddress );
                                person.setFaceAddress( faceAddress );
//                            person.setIdcardImage( idcardImage );
                                person.setFaceImage( faceImage );
                                person.setDataSource( "3" );
                                person.setPersonId( personNewTable.getPersonId() );
                                personNewFaceInfomationTableList.add( person );
                            }
                            //添加人员图片和一卡通照片
                            personFaceInfomationTableService.insertPersonNewFaceInfomationTable( personNewFaceInfomationTableList );
                            resultMap.put( "code", Constant.RESPONSE_SUCCESS_CODE );
                        }
                        //添加人员更新日志
                        personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personNewTable );
                        personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                    }
                }else {
                    resultMap.put( "code", 3 );
                }
            } else {
                if (IdentityNo != null)
                    resultMap.put( "code", 1 );
                if (IdentityTypeCode != null)
                    resultMap.put( "code", 2 );
            }
        } catch (Exception ex) {
            logger.error( "新增人员基础信息失败！！" + ex.getMessage() );
        }
        return resultMap;
    }

    /**
     * 人证核验终端获取的数据
     *
     * @return
     */
    @RequestMapping(value = "/insertPersonTableTerminal", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> insertPersonTableTerminal(HttpServletRequest request, PersonTable personTable) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        PersonFaceInfomationTable person = null;
//        String[] imageSplit = null;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        PersonDataOperationTable personDataOperationTable = null;
        try {
            //根据人员身份证号查询人员信息
            PersonTable personTableByIdentityNo = personTableService.selectPersonTableByIdentityNo( personTable.getIdentityNo() );
            if (personTableByIdentityNo == null) {
                //新增人证核验基本信息
                i = personTableService.insertPersonNewTable( personTable );
                //判断是否为0
                if (i != 0) {
                    //人员图片新增调用公共类
                    PersonTableUtils.personTableImage(person,personTable,personFaceInfomationTableService);
                    //添加人员更新日志
                    personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personTable );
                    personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                    resultMap.put( "code", Constant.RESPONSE_SUCCESS_CODE );
                    resultMap.put( "description", Constant.RESPONSE_SUCCESS_DESCRIPTION );
                }
            } else {
                i = personTableService.updateByPrimaryKeyIdentityNo( personTable );
                if (i != 0) {
                    //根据人员id查询人员信息
                    PersonTable selectPersonTable = personTableService.selectPersonTableByIdentityNo( personTable.getIdentityNo() );
                    //根据人员id删除人员照片信息
                    int j = personFaceInfomationTableService.deletePersonTableByImagePersonId( selectPersonTable.getPersonId() );
                    if (j != 0) {
                        //人员图片新增调用公共类
                        PersonTableUtils.personTableImage(person,personTable,personFaceInfomationTableService);
                    }
                    //添加人员更新日志
                    personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personTable );
                    personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                    resultMap.put( "code", Constant.RESPONSE_SUCCESS_CODE );
                    resultMap.put( "description", Constant.RESPONSE_SUCCESS_DESCRIPTION );
                }
            }


        } catch (Exception ex) {
            logger.error( "人证核验终端获取的数据失败！！" + ex.getMessage() );
        }
        return resultMap;
    }

    /*
    上传照片删除
     */
    @RequestMapping(value = "/deleteImg", method = RequestMethod.POST)
    public Map<String, Object> delFile(HttpServletRequest request) {
        OutputStream out = null;
        boolean flag = false;
        Map<String, Object> map = new HashMap<>();
        try {
            String img = request.getParameter( "imgurl" );
            System.out.println( img );
            // 将多出来的双引号去掉
            String str = img.replace( "\"", "" );
            File file = new File( str );
            // 判断是否存在此文件
            if (file.isFile() && file.exists()) {
                if (file.getAbsoluteFile().delete()) {
                    map.put( "code", 0 );
                } else {
                    map.put( "code", 1 );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return map;
    }

    /**
     * 编辑人员信息
     *
     * @param personNewTable
     * @return
     */
    @RequestMapping(value = "/updateByPrimaryKeySelective", method = RequestMethod.PUT)
    public Map<String, Object> updateByPrimaryKeySelective(PersonTable personNewTable) {
        Map<String, Object> map = new HashMap<>();
        BaseOrganizitionPerson baseOrganizitionPerson = new BaseOrganizitionPerson();
        PersonDataOperationTable personDataOperationTable = null;
        if (personNewTable.getPersonName().substring( 0, 1 ).equals( " " )) {
            map.put( "code", -4 );
            return map;
        }
        PersonTable IdentityNo = null;
        // 现根据当前id去查询该人员信息
        PersonTable thisPersonTable = personTableService.selectByPersonId( personNewTable.getPersonId() );
        //判断比较身份证号是否重复公共类
        IdentityNo=PersonTableUtils.identityNo(personNewTable,thisPersonTable,personTableService);

        // 判断教工号或一卡通号是否已存在
        PersonTable IdentityTypeCode = null;
        if (personNewTable.getIdentityTypeCode() != null) {
            if (thisPersonTable.getIdentityTypeCode() != null) {
                // 判断当前修改的身份证号跟之前的身份证号是否一样
                if (!thisPersonTable.getIdentityTypeCode().equals( personNewTable.getIdentityTypeCode() )) {
                    IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode( personNewTable.getIdentityTypeCode() );
                }
            } else {
                IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode( personNewTable.getIdentityTypeCode() );
            }
        }
        // 身份证号码和教工号都不重复
        if (IdentityNo == null && IdentityTypeCode == null) {
            personNewTable.setSchoolClass(personNewTable.getOrganizitionId());
            String pid = baseOrganizitionService.getOrganizitionById(personNewTable.getOrganizitionId());
            personNewTable.setGrade(pid);
            String departmentsId = baseOrganizitionService.getOrganizitionById(pid);
            personNewTable.setDepartments(departmentsId);
            personNewTable.setModifyTime(new Date());
            //判断新增人员身份  如果是学生进行人员唯一标识拼接
            if("8".equals(personNewTable.getIdenticationInfo())){
                //查询所选 学校/年级/班级 信息进行拼接 学校编码_年级编码_班级编码_学工号 = 人员唯一标识
                PersonTable personNewTable1 = personTableService.queryPersonTableByOrgId(personNewTable);
                if(personNewTable1 != null){
                    personNewTable.setPersonnelUniqueCode(personNewTable1.getOrgName()+"_"+personNewTable1.getIdentityTypeCode());
                }
            }
            int i = personTableService.updateByPrimaryKeySelective( personNewTable );
            if(i != 0){
                BaseOrganizitionPerson baseOrganizitionPerson1 = baseOrganizitionPersonMapper.queryBaseOrganizitionPersonByPersonId(personNewTable.getPersonId());
                baseOrganizitionPerson.setId(baseOrganizitionPerson1.getId());
                baseOrganizitionPerson.setPersonId(personNewTable.getPersonId());
                baseOrganizitionPerson.setOrganizitionId(personNewTable.getOrganizitionId());
                baseOrganizitionPerson.setUpdateTime(new Date());
                baseOrganizitionPersonMapper.updateBaseOrganizitionPersonById(baseOrganizitionPerson);
            }
            /*if (!("2".equals( personNewTable.getIdenticationInfo() )) && !("3".equals( personNewTable.getIdenticationInfo() ))) {
                personTableService.updateGradeAndStudentLevlByPersonId( personNewTable.getPersonId() );
            }*/
            //根据人员id查询人员信息
            PersonTable personTable = personTableService.selectPersonTableById( personNewTable.getPersonId() );
            //添加人员更新日志
            personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable( personTable );
            personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
            return LayuiUtil.dataUpdate( i );
        } else {
            if (IdentityNo !=null)
                map.put( "code", 5 );
            if (IdentityTypeCode !=null)
                map.put( "code", 6 );
        }
        return map;
    }

    /**
     * 编辑人员照片信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatePersonImg", method = RequestMethod.POST)
    public Map<String, Object> updatePersonImg(HttpServletRequest request) {
        String imgPath = Config.getPhotoUrl("photoURL");
        Map<String, Object> map = new HashMap<>();
        try {
            //获取从前端传送的值
            String datats = request.getParameter( "datats" );
            //转换成json对象
            Datas datasArray = JSON.parseObject( datats, Datas.class );
            String filePath = Config.getPhotoUrl("filePath");
            //            获取删除的图片数组
            datasArray.getImgs().forEach(p->{
                String[] split = p.split("/imageEcho/");
                if (split!=null){
                    File file = new File(filePath+split[1]);
                    // 路径不为空则进行删除     
                    if (file.exists()) {
                        file.delete();
                    }
                }
            });
            //人脸基础信息
            PersonTable personNewTable = null;
            //人脸照片信息
            List<PersonFaceInfomationTable> faceAddressList = null;
            //判断是否为空
            if (datasArray != null) {
                personNewTable = datasArray.getPersonNewTable();//人脸基础信息
                faceAddressList = datasArray.getFaceAddressList();//人脸图片地址 校园卡图片地址

                faceAddressList.forEach(img->{
                    if(img.getFaceAddress() !=null && img.getFaceAddress().startsWith(imgPath)){
                        img.setFaceAddress(img.getFaceAddress().split(imgPath)[1]);
                    }
                });
            }
            List<PersonFaceInfomationTable> personFaceInfomationTableList = personTableService.queryPersonFaceInfomationTableByPersonId( personNewTable.getPersonId() );
            String dataSource = null;
            for (PersonFaceInfomationTable personFaceInfomationTable : personFaceInfomationTableList) {
                if (personFaceInfomationTable.getDataSources() != null && personFaceInfomationTable.getDataSources() != "") {
                    dataSource = personFaceInfomationTable.getDataSources();
                }
            }

            //先删除该人员的照片信息
            personFaceInfomationTableService.deletePersonTableByPersonId( personNewTable.getPersonId() );
            //重新组装成list集合
            List<PersonFaceInfomationTable> personNewFaceInfomationTableList = new ArrayList<>();
            //判断集合是否为空
            if (ListUtils.isNotNullAndEmptyList( faceAddressList )) {
                //循环遍历-----组装成新的集合
                for (PersonFaceInfomationTable pnfi : faceAddressList) {
                    PersonFaceInfomationTable person = new PersonFaceInfomationTable();
                    String faceAddress = null;
                    String campusCardAddress = null;
                    String faceImage = null;
                    //人脸图片地址
                    if (StringUtils.isNotBlank( pnfi.getFaceAddress() )) {
                        // faceAddress = pnfi.getFaceAddress().replace( "data:image/jpeg;base64,", "" );
                        faceAddress=Base64Utils.baseurlPhotos(pnfi.getFaceAddress());
                        //判断是否base64编码
                        boolean base64Encode = isBase64Encode( faceAddress );
                        if (base64Encode) {
                            faceAddress = Base64Utils.generateImage( faceAddress );
                        }
                    }
                    //校园卡正面照地址
                    if (StringUtils.isNotBlank( pnfi.getCampusCardAddress() )) {
                        //campusCardAddress = pnfi.getCampusCardAddress().replace( "data:image/jpeg;base64,", "" );
                        campusCardAddress=Base64Utils.baseurlPhotos( pnfi.getCampusCardAddress() );
                        //判断是否base64编码
                        boolean base64Encode = isBase64Encode( campusCardAddress );
                        if (base64Encode) {
                            campusCardAddress = Base64Utils.generateImage( campusCardAddress );
                        }else {
                            campusCardAddress = campusCardAddress.split(imgPath)[1];
                        }
                    }
                    //人证校验图片
                    if (StringUtils.isNotBlank( pnfi.getFaceImage() )) {
                        //faceImage = pnfi.getFaceImage().replace( "data:image/jpeg;base64,", "" );
                        faceImage=Base64Utils.baseurlPhotos(pnfi.getFaceImage());
                        //判断是否base64编码
                        boolean base64Encode = isBase64Encode( faceImage );
                        if (base64Encode) {
                            faceImage = Base64Utils.generateImage( faceImage );
                        }else {
                            faceImage = faceImage.split(imgPath)[1];
                        }


                    }
                    person.setCampusCardAddress( campusCardAddress );
                    person.setFaceAddress( faceAddress );
                    person.setFaceImage( faceImage );
                    person.setDataSource( dataSource );
                    person.setIdentification( "2" );
                    //person.setIdentification( pnfi.getIdentification() );
                    person.setPersonId( personNewTable.getPersonId() );
                    personNewFaceInfomationTableList.add( person );
                }
                //添加人员图片和一卡通照片
                personFaceInfomationTableService.insertPersonNewFaceInfomationTable( personNewFaceInfomationTableList );
            }
//                resultMap.put( "description", Constant.RESPONSE_SUCCESS_DESCRIPTION );
        } catch (Exception ex) {
            logger.error( "新增人员基础信息失败！！" + ex.getMessage() );
        }
        return map;
    }

    /**
     * 判断是否是base64编码
     *
     * @param faceAddress
     * @return
     */
    public boolean isBase64Encode(String faceAddress) {
        if (faceAddress == null || faceAddress.length() == 0) {
            return false;
        }
        if (faceAddress.length() % 4 != 0) {
            return false;
        }
        char[] chrs = faceAddress.toCharArray();
        for (char chr : chrs) {
            if ((chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9') ||
                    chr == '+' || chr == '/' || chr == '=') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除人员信息
     *
     * @param personId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deletePersonTableByUserId", method = RequestMethod.DELETE)
    public Map<String, Object> deletePersonTableByUserId(@RequestBody Integer[] personId, HttpServletRequest request) throws Exception {
//        String url = request.getSession().getServletContext().getRealPath("/uploadfile/facepicture/"); //服务器地址
        int i = 0;
        i = personTableService.deletePersonTableByUserId( personId );
        //int j = personFaceInfomationTableService.deletePersonTableByUserId( personId );
        baseOrganizitionPersonMapper.deleteBaseOrganizitionPersonByPersonId(personId);
        return LayuiUtil.dataDelete( i );
    }

    /**
     * 图片上传
     *
     * @param files
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestParam(value = "file", required = false) MultipartFile files, HttpServletRequest request) throws IOException {
        Map<String, Object> map = new HashMap<>();
        OutputStream out = null;
        String newFileUrl = null;
        //对文件数组进行遍历
//        for (MultipartFile multipartFile : files) {
        //老文件名
        String originalName = files.getOriginalFilename();
        //截取老文件名的后缀
        String prefix = originalName.substring( originalName.lastIndexOf( "." ) + 1 );
        try {
            //获取配置文件中图片的路径
            String dPath = Config.getPhotoUrl("filePath");
            //调用图片路径的公共方法
            newFileUrl = Base64Utils.addressPublic( dPath );
            map.put( "code", 0 );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
//        }
        map.put( "msg", newFileUrl );
        return map;

    }

    /**
     * 查询教工号或学号或一卡通号是否已存在
     *
     * @return
     */
    @RequestMapping(value = "/selectByIdentityTypeCode", method = RequestMethod.GET)
    public Map<String, Object> selectByIdentityTypeCode(HttpServletRequest request, String identityTypeCode) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        try {
            if (StringUtils.isNotBlank( identityTypeCode )) {
                PersonTable personTable = personTableService.selectByIdentityTypeCode( identityTypeCode );
                if (personTable != null) {
                    resultMap.put( "code", Constant.RESPONSE_SUCCESS_CODE );
                }
            }
        } catch (Exception ex) {
            logger.error( "查询教工号或学号或一卡通号是否已存在失败！！" + ex.getMessage() );
        }
        return resultMap;
    }
    /**
     * 查询身份证是否已存在
     *
     * @return
     */
    @RequestMapping(value = "/selectByIdentityNo", method = RequestMethod.GET)
    public Map<String, Object> selectByIdentityNo(HttpServletRequest request, String identityNo) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if (StringUtils.isNotBlank( identityNo )) {
                PersonTable personTable = personTableService.queryPersonTableByIdentityNo( identityNo );
                if (personTable != null) {
                    resultMap.put( "code", Constant.RESPONSE_SUCCESS_CODE );
                }
            }
        } catch (Exception ex) {
            resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
            logger.error( "查询身份证是否已存在失败！！" + ex.getMessage() );
        }
        return resultMap;
    }

    /*
     * 根据openid查询人员信息
     * */
    @RequestMapping(value = "/getPersonTableByopenId", method = RequestMethod.GET)
    public Map<String, Object> getPersonTableByopenId(@RequestParam String openid) {
        Map<String, Object> map = new HashMap<>();
        List<PersonTable> list = personTableService.selectPersonTableListByopenId( openid );
        map.put( "list", list );
        return map;
    }


    /**
     * 获取微信config配置里的参数
     */
    @RequestMapping(value = "getWxConfig", method = RequestMethod.POST)
    public Map<String, Object> getWxConfig(HttpServletRequest request, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
//        String AppId = "wx8036690eff388a39";
        String AppId = FileUtils.getProperties("/application.properties", "appId");
        String url = request.getParameter( "url" );
        //1、获取AccessToken
        String accessToken = getAccessToken();
        session.setAttribute( "access_token", accessToken );
        //2、获取Ticket
        String jsapi_ticket = getTicket( accessToken );
        //3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace( "-", "" ).substring( 0, 16 );//随机字符串
        String timestamps = String.valueOf( System.currentTimeMillis() / 1000 );//时间戳
        System.out.println( "accessToken:" + accessToken + "\njsapi_ticket:" + jsapi_ticket + "\n时间戳：" + timestamps + "\n随机字符串：" + noncestr );
        ;

        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamps + "&url=" + url;
        //6、将字符串进行sha1加密
        String signature = SHA1( str );
        System.out.println( "参数：" + str + "\n签名：" + signature );
        map.put( "timestamps", timestamps );
        map.put( "noncestr", noncestr );
        map.put( "signatures", signature );
        map.put( "AppId", AppId );
        return map;
    }


    //获取ticket
    public static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL( url );
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod( "GET" ); // 必须是get方式请求
            http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
            http.setDoOutput( true );
            http.setDoInput( true );
            System.setProperty( "sun.net.client.defaultConnectTimeout", "30000" );// 连接超时30秒
            System.setProperty( "sun.net.client.defaultReadTimeout", "30000" ); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read( jsonBytes );
            String message = new String( jsonBytes, "UTF-8" );
            JSONObject demoJson = JSONObject.fromObject( message );
            ticket = demoJson.getString( "ticket" );
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    //获取accesstoken
    public static String getAccessToken() {
        String access_token = "";
        String grant_type = "client_credential";//获取access_token填写client_credential
//        String AppId = "wx8036690eff388a39";//第三方用户唯一凭证
        String AppId = FileUtils.getProperties("/application.properties", "appId");//第三方用户唯一凭证
//        String secret = "498e85672b054f22052961ac89971d0b";//第三方用户唯一凭证密钥，即appsecret
        String secret = FileUtils.getProperties("/application.properties", "secret");//第三方用户唯一凭证密钥，即appsecret
        //这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" + AppId + "&secret=" + secret;

        try {
            URL urlGet = new URL( url );
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod( "GET" ); // 必须是get方式请求
            http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
            http.setDoOutput( true );
            http.setDoInput( true );
            System.setProperty( "sun.net.client.defaultConnectTimeout", "30000" );// 连接超时30秒
            System.setProperty( "sun.net.client.defaultReadTimeout", "30000" ); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read( jsonBytes );
            String message = new String( jsonBytes, "UTF-8" );
            JSONObject demoJson = JSONObject.fromObject( message );
            System.out.println( "获取tocken的JSON字符串：" + demoJson );
            access_token = demoJson.getString( "access_token" );
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }


    // SHA1算法
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance( "SHA-1" );
            digest.update( decript.getBytes() );
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString( messageDigest[i] & 0xFF );
                if (shaHex.length() < 2) {
                    hexString.append( 0 );
                }
                hexString.append( shaHex );
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    @RequestMapping(value = "/downloadMedia", method = RequestMethod.POST)
    private Map<String, Object> getInputStream(HttpServletRequest request, HttpSession session) {

//        String dPath = FileUtils.getProperties("/application.properties", "filePath");
        Map<String, Object> map = new HashMap<>();
        String accessToken = (String) session.getAttribute( "access_token" );
        String mediaId = request.getParameter( "serverId" );
        String download = "";
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id=" + mediaId;
        System.out.println( "--------url------" + url );
        try {
            download = Base64Utils.download( url );
          /*  String picUrl="/images/toFindImg?imgUrl="+dPath+download;
            map.put("picUrl",picUrl);*/
            map.put( "download", download );
            System.out.println( "--------download------" + download );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

    /*
     * 微信公众号修改人员信息
     * */
    @RequestMapping(value = "/WxUpdatePersonTable", method = RequestMethod.POST)
    @Transactional
    public Map<String, Object> WxUpdatePersonTable(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        resultMap.put( "code", Constant.RESPONSE_FAIL_CODE );
        PersonDataOperationTable personDataOperationTable = null;
        try {
            //获取从前端传送的值
            String datats = request.getParameter( "datats" );
            //转换成json对象
            Datas datasArray = JSON.parseObject( datats, Datas.class );
            //人脸基础信息
            PersonTable personNewTable = null;
            //人脸照片信息
            List<PersonFaceInfomationTable> faceAddressList = null;
            //判断是否为空
            if (datasArray != null) {
                personNewTable = datasArray.getPersonNewTable();//人脸基础信息
                faceAddressList = datasArray.getFaceAddressList();//人脸图片地址 校园卡图片地址
            }
            //判断身份信息是否是学生
            if (!("2".equals( personNewTable.getIdenticationInfo() )) && !("3".equals( personNewTable.getIdenticationInfo() ))) {
                personNewTable.setStudentLevel( "" );
                personNewTable.setGrade( "" );
            }
            PersonTable IdentityNo = null;
            // 现根据当前id去查询该人员信息
            PersonTable thisPersonTable = personTableService.selectByPersonId( personNewTable.getPersonId() );
            //调用公共类》》》》判断比较身份证号是否重复
            IdentityNo=PersonTableUtils.identityNo( personNewTable,thisPersonTable,personTableService );


            // 判断教工号或一卡通号是否已存在
            PersonTable IdentityTypeCode = null;
            if (personNewTable.getIdentityTypeCode() != null) {

                //从身份证获取生日
                personNewTable.setBirthday( extractYearMonthDayOfIdCard( personNewTable.getIdentityNo() ) );
                if (thisPersonTable.getIdentityTypeCode() != null) {
                    // 判断当前修改的身份证号跟之前的身份证号是否一样
                    if (!thisPersonTable.getIdentityTypeCode().equals( personNewTable.getIdentityTypeCode() )) {
                        IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode( personNewTable.getIdentityTypeCode() );
                    }
                } else {
                    IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode( personNewTable.getIdentityTypeCode() );
                }
            }

            if (IdentityNo == null && IdentityTypeCode== null)  {
                i = personTableService.updateByPrimaryKeySelective( personNewTable );
                if (!("2".equals( personNewTable.getIdenticationInfo() )) && !("3".equals( personNewTable.getIdenticationInfo() ))) {
                    personTableService.updateGradeAndStudentLevlByPersonId( personNewTable.getPersonId() );
                }
                //根据人员id查询人员信息
                PersonTable personTable = personTableService.selectPersonTableById( personNewTable.getPersonId() );
                //添加人员更新日志
                personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable( personTable );
                personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );

                List<PersonFaceInfomationTable> personFaceInfomationTableList = personTableService.queryPersonFaceInfomationTableByPersonId( personNewTable.getPersonId() );
                String dataSource = null;
                for (PersonFaceInfomationTable personFaceInfomationTable : personFaceInfomationTableList) {
                    if (personFaceInfomationTable.getDataSources() != null && personFaceInfomationTable.getDataSources() != "") {
                        dataSource = personFaceInfomationTable.getDataSources();
                    }
                }

                //先删除该人员的照片信息
                personFaceInfomationTableService.deletePersonTableByPersonId( personNewTable.getPersonId() );
                //重新组装成list集合
                List<PersonFaceInfomationTable> personNewFaceInfomationTableList = new ArrayList<>();
                //判断集合是否为空
                if (ListUtils.isNotNullAndEmptyList( faceAddressList )) {
                    //循环遍历-----组装成新的集合
                    /*for (PersonFaceInfomationTable pnfi : faceAddressList) {
                        PersonFaceInfomationTable person = new PersonFaceInfomationTable();
                        person.setCampusCardAddress( pnfi.getCampusCardAddress() );
                        person.setFaceAddress( pnfi.getFaceAddress() );
                        person.setIdcardImage( pnfi.getIdcardImage() );
                        person.setFaceImage( pnfi.getFaceImage() );
                        person.setIdentification( pnfi.getIdentification());
                        person.setPersonId( personNewTable.getPersonId() );
                        personNewFaceInfomationTableList.add( person );
                    }*/
                    for (PersonFaceInfomationTable pnfi : faceAddressList) {
                        PersonFaceInfomationTable person = new PersonFaceInfomationTable();
                        String faceAddress = null;
                        String campusCardAddress = null;
                        String faceImage = null;
                        //人脸图片地址
                        if (StringUtils.isNotBlank( pnfi.getFaceAddress() )) {
                            faceAddress = pnfi.getFaceAddress().replace( "data:image/jpeg;base64,", "" );
                            //判断是否base64编码
                            boolean base64Encode = isBase64Encode( faceAddress );
                            if(base64Encode){
                                faceAddress = Base64Utils.generateImage( faceAddress );
                            }
                        }
                        //校园卡正面照地址
                        if (StringUtils.isNotBlank( pnfi.getCampusCardAddress() )) {
                            campusCardAddress = pnfi.getCampusCardAddress().replace( "data:image/jpeg;base64,", "" );
                            //判断是否base64编码
                            boolean base64Encode = isBase64Encode( campusCardAddress );
                            if(base64Encode){
                                campusCardAddress = Base64Utils.generateImage( campusCardAddress );
                            }
                        }
                        //人证校验图片
                        if (StringUtils.isNotBlank( pnfi.getFaceImage() )) {
                            faceImage = pnfi.getFaceImage().replace( "data:image/jpeg;base64,", "" );
                            //判断是否base64编码
                            boolean base64Encode = isBase64Encode( faceImage );
                            if(base64Encode){
                                faceImage = Base64Utils.generateImage( faceImage );
                            }


                        }
                        person.setCampusCardAddress( campusCardAddress);
                        person.setFaceAddress(faceAddress);
                        person.setFaceImage(faceImage);
                        person.setDataSource( dataSource );
                        person.setIdentification( pnfi.getIdentification() );
                        person.setPersonId( personNewTable.getPersonId() );
                        personNewFaceInfomationTableList.add( person );
                    }

                    //添加人员图片和一卡通照片
                    personFaceInfomationTableService.insertPersonNewFaceInfomationTable( personNewFaceInfomationTableList );
                }
                resultMap.put( "code", Constant.RESPONSE_SUCCESS_CODE );
            } else {
                if(IdentityNo!=null){
                    resultMap.put( "code", 1 );
                }
                if(IdentityTypeCode!=null){
                    resultMap.put( "code", 2 );
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

}
