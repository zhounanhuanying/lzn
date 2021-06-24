package com.bfdb.controller;


import com.alibaba.fastjson.JSON;
import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.entity.vo.*;
import com.bfdb.mapper.InterfaceManagementMapper;
import com.bfdb.service.*;
import com.bfdb.untils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 接口管理
 * 提供给第三方系统
 */
@RestController
@RequestMapping("/interfaceManagement")
public class InterfaceManagementController extends AbstractController {

    @Autowired
    private InterfaceManagementService interfaceManagementService;

    @Autowired
    private PersonFaceInfomationTableService personFaceInfomationTableService;

    @Autowired
    PersonDataOperationTableService personDataOperationTableService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private InterfaceManagementMapper interfaceManagementMapper;

    /**
     * 记录接口调用的相关信息
     */
    @Autowired
    private InterfaceAuthorizationService interfaceAuthorizationService;

    @Autowired
    private DataPermissionService dataPermissionService;

    /**
     * 第三方系统调用新增人员接口
     *
     * @param personTableList
     * @return
     */
    @RequestMapping(value = "/insertPersonTable", method = RequestMethod.POST)
    @Transactional
    public ResponseResult insertPersonTable(@RequestBody Datas personTableList) {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();
        int i = 0;
        PersonCodeVo personCodeVo = null;
        PersonDataOperationTable personDataOperationTable = null;
        //创建subject对象,获取用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        InterfaceAuthorization interfaceAuthorization = null;
        try {
            //判断人员信息是否为空
            if (CollectionUtils.isEmpty( personTableList.getPersonTableVoList() )) {
                responseResult.setResponseCode( 111 );
                responseResult.setResponseDescription( "参数不能为空" );
                return responseResult;
            }
            //新增人员基本信息
            i = interfaceManagementService.insertPersonTableList( personTableList.getPersonTableVoList(), user );
            switch (i) {
                case 3:
                    responseResult.setResponseCode( 99 );
                    responseResult.setResponseDescription( "校园卡或一卡通照片最多上传一张" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;

                case 2:
                    responseResult.setResponseCode( 101 );
                    responseResult.setResponseDescription( "姓名不能为空" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case 0:
                    responseResult.setResponseCode( 100 );
                    responseResult.setResponseDescription( "新增人员接口失败" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case 1:
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization.setRequestParameters( JSON.toJSONString( personTableList.getPersonTableVoList() ) );
                    //定义personCode集合
                    List<PersonCodeVo> personCodeVos = new ArrayList<>();
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            //实现逻辑，在公共实现中
                            personCodeVo = personFaceInfomationTableUtils2( user.getUserName(), personTable.getFaceInfomation(), personTable, personTable.getPersonId() );
                            personCodeVos.add( personCodeVo );
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            //实现逻辑，在公共实现中
                            personFaceInfomationTableUtils2( user.getUserName(), personTable.getCardInfomation(), personTable, personTable.getPersonId() );
                        }
                        //添加操作人员更新日志表
                        PersonTable personTable1 = new PersonTable();
                        personTable1.setPersonCode(personTable.getPersonCode());
                        personTable1.setCreateTime(personTable.getCreateTime());
                        personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personTable1 );
                        personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                    }
                    //返回信息值
                    responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
                    responseResult.setDatas( personCodeVos );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                    interfaceAuthorizationService.insertSuccessInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -1:
                    responseResult.setResponseCode( 102 );
                    responseResult.setResponseDescription( "身份证号或教工号或学号或一卡通号必须选择填写一个" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -2:
                    responseResult.setResponseCode( 103 );
                    responseResult.setResponseDescription( "人脸照片至少上传一张且最多上传三张" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -3:
                    responseResult.setResponseCode( 104 );
                    responseResult.setResponseDescription( "身份证号格式或者位数不正确" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -4:
                    responseResult.setResponseCode( 105 );
                    responseResult.setResponseDescription( "手机号格式不正确" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -5:
                    responseResult.setResponseCode( 107 );
                    responseResult.setResponseDescription( "所在部门或院系不能为空" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -6:
                    responseResult.setResponseCode( 108 );
                    responseResult.setResponseDescription( "用户暂无该部门权限" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -7:
                    responseResult.setResponseCode( 110 );
                    responseResult.setResponseDescription( "身份信息不能为空" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -8:
                    responseResult.setResponseCode( 112 );
                    responseResult.setResponseDescription( "身份证号重复" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -9:
                    responseResult.setResponseCode( 113 );
                    responseResult.setResponseDescription( "一卡通号重复" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -10:
                    responseResult.setResponseCode( 114);
                    responseResult.setResponseDescription( "一卡通号不能超过十五位数且必须是数字" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -11:
                    responseResult.setResponseCode( 115);
                    responseResult.setResponseDescription( "人员姓名必须是纯中文" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -12:
                    responseResult.setResponseCode( 116);
                    responseResult.setResponseDescription( "性别不存在" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -13:
                    responseResult.setResponseCode( 117);
                    responseResult.setResponseDescription( "民族不存在" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -14:
                    responseResult.setResponseCode( 118);
                    responseResult.setResponseDescription( "人员类型不存在" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                default:
                    responseResult.setResponseCode( Constant.RESPONSE_FAIL_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_FAIL_DESCRIPTION );
                    responseResult.setDatas( "服务器运行异常" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
            }

        } catch (Exception ex) {
            responseResult.setResponseCode( Constant.RESPONSE_FAIL_CODE );
            responseResult.setResponseDescription( Constant.RESPONSE_FAIL_DESCRIPTION );
            responseResult.setDatas( "服务器运行异常" );
            //循环遍历新增后的人员信息
            for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                //判断图片信息
                if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                    personTable.setFaceInfomation(null);
                }
                if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                    personTable.setCardInfomation(null);
                }
            }
            //保存到接口调用统计表
            interfaceAuthorization = new InterfaceAuthorization();
            interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
            interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
            interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            logger.error( "新增人员接口失败！！" + ex.getMessage() );
        }
        return responseResult;
    }

    /**
     * 第三方系统调用新增人员接口  POST公共
     *
     * @param interfaceAuthorization
     * @param user
     * @param responseResult
     * @return
     */
    private InterfaceAuthorization interfaceAuthorizationCommonPOST(InterfaceAuthorization interfaceAuthorization,
                                                                    User user, ResponseResult responseResult, Datas personTableList) {
        interfaceAuthorization.setInterfaceName( Constant.INSERTPERSONTABLE );
        interfaceAuthorization.setInterfaceAddress( Constant.INTERFACEADDRESS );
        interfaceAuthorization.setInterfaceType( Constant.INTERFACETYPE );
        interfaceAuthorization.setCallTime( new Date() );
        interfaceAuthorization.setCallNumber( Constant.CALLNUMBER );
        interfaceAuthorization.setUserId( user.getUserId() );
        interfaceAuthorization.setRequestParameters( JSON.toJSONString( personTableList ) );
        interfaceAuthorization.setExceptionInformation( JSON.toJSONString( responseResult ) );
        return interfaceAuthorization;
    }

    /**
     * 第三方系统调用修改人员接口
     *
     * @param personTableList
     * @return
     */
    @RequestMapping(value = "/updatePersonTable", method = RequestMethod.PUT)
    @Transactional
    public ResponseResult updatePersonTable(@RequestBody Datas personTableList) {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();
        int i = 0;
        int j = 0;
        //定义personCode集合
        List<PersonCodeVo> personCodeVos = new ArrayList<>();
        PersonCodeVo personCodeVo = null;
        //创建subject对象,获取用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        PersonDataOperationTable personDataOperationTable = null;
        InterfaceAuthorization interfaceAuthorization = null;
        try {
            //判断人员信息是否为空
            if (CollectionUtils.isEmpty( personTableList.getPersonTableVoList() )) {
                responseResult.setResponseCode( 111 );
                responseResult.setResponseDescription( "参数不能为空" );
                return responseResult;
            }
            //修改人员基本信息
            i = interfaceManagementService.updatePersonTableList( personTableList.getPersonTableVoList(), user );
            switch (i) {
                case 3:
                    responseResult.setResponseCode( 99 );
                    responseResult.setResponseDescription( "校园卡或一卡通照片最多上传一张" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case 0:
                    responseResult.setResponseCode( 100 );
                    responseResult.setResponseDescription( "修改人员接口失败" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case 2:
                    responseResult.setResponseCode( 101 );
                    responseResult.setResponseDescription( "姓名不能为空" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case 1:
                    //循环遍历修改的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //根据人员id查询人员信息
                        PersonTable selectPersonTable = interfaceManagementService.selectPersonTable( personTable.getPersonCode() );
                        // 查询对应人员的数据来源
                        List<PersonFaceInfomationTable> personFaceInfomationTable = interfaceManagementService.queryPersonFaceInfomationTableByPersonId( selectPersonTable.getPersonId() );
                        //根据人员id删除人员照片信息
                        j = personFaceInfomationTableService.deletePersonTableByImagePersonId( selectPersonTable.getPersonId() );
                        if (j != 0) {
                            //判断图片信息
                            if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                                //实现逻辑，在公共实现中
                                personCodeVo = personFaceInfomationTableUtils( user.getUserName(), personTable.getFaceInfomation(), personTable,selectPersonTable.getPersonId() );
                                personCodeVos.add( personCodeVo );
                            }
                            if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                                //实现逻辑，在公共实现中
                                personFaceInfomationTableUtils( user.getUserName(), personTable.getCardInfomation(), personTable,selectPersonTable.getPersonId() );
                            }
                            //添加操作人员更新日志表
                            PersonTable personTable1 = new PersonTable();
                            personTable1.setPersonCode(personTable.getPersonCode());
                            personTable1.setCreateTime(personTable.getCreateTime());
                            personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personTable1 );
                            personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                        }
                    }
                    responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
                    responseResult.setDatas( personCodeVos );
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                    interfaceAuthorizationService.insertSuccessInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -1:
                    responseResult.setResponseCode( 102 );
                    responseResult.setResponseDescription( "身份证号或教工号或学号或一卡通号必须选择填写一个" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -2:
                    responseResult.setResponseCode( 103 );
                    responseResult.setResponseDescription( "人脸照片至少上传一张且最多上传三张" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -3:
                    responseResult.setResponseCode( 104 );
                    responseResult.setResponseDescription( "身份证号格式或者位数不正确" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -4:
                    responseResult.setResponseCode( 105 );
                    responseResult.setResponseDescription( "手机号格式不正确" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -5:
                    responseResult.setResponseCode( 107 );
                    responseResult.setResponseDescription( "所在部门或院系不能为空" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -6:
                    responseResult.setResponseCode( 108 );
                    responseResult.setResponseDescription( "用户暂无该部门权限" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -7:
                    responseResult.setResponseCode( 110 );
                    responseResult.setResponseDescription( "身份信息不能为空" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -8:
                    responseResult.setResponseCode( 112);
                    responseResult.setResponseDescription( "身份证号重复" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -9:
                    responseResult.setResponseCode( 113);
                    responseResult.setResponseDescription( "一卡通号重复" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -10:
                    responseResult.setResponseCode( 114);
                    responseResult.setResponseDescription( "一卡通号不能超过十五位数且必须是数字" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -11:
                    responseResult.setResponseCode( 115);
                    responseResult.setResponseDescription( "人员姓名必须是纯中文" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -12:
                    responseResult.setResponseCode( 116);
                    responseResult.setResponseDescription( "性别不存在" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -13:
                    responseResult.setResponseCode( 117);
                    responseResult.setResponseDescription( "民族不存在" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -14:
                    responseResult.setResponseCode( 118);
                    responseResult.setResponseDescription( "人员类型不存在" );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                default:
                    responseResult.setResponseCode( Constant.RESPONSE_FAIL_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_FAIL_DESCRIPTION );
                    responseResult.setDatas( "服务器运行异常" );
                    //保存到接口调用情况
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
            }

        } catch (Exception ex) {
            responseResult.setResponseCode( Constant.RESPONSE_FAIL_CODE );
            responseResult.setResponseDescription( Constant.RESPONSE_FAIL_DESCRIPTION );
            responseResult.setDatas( "服务器运行异常" );
            interfaceAuthorization = new InterfaceAuthorization();
            interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
            interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
            interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            logger.error( "修改人员接口失败！！" + ex.getMessage() );
        }
        return responseResult;
    }
//    获取图片地址
    private String photoPath(String path) throws IOException {
        String s = Base64Utils.baseurlPhotos( path );
        //判断是否base64编码
        boolean base64Encode = isBase64Encode( s );
        if (base64Encode) {
            s= Base64Utils.generateImage( s );
        }else {
            s = Base64Utils.download( s );
        }
        return s;
    }
    /**
     * 判断是否是base64编码
     *
     * @param faceAddress
     * @return
     */
    private boolean isBase64Encode(String faceAddress) {
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
     * 第三方系统调用修改人员接口 PUT公共
     *
     * @param interfaceAuthorization
     * @param user
     * @param responseResult
     * @return
     */
    private InterfaceAuthorization interfaceAuthorizationCommonPUT(InterfaceAuthorization interfaceAuthorization, User user,
                                                                   ResponseResult responseResult, Datas personTableList) {
        interfaceAuthorization.setInterfaceName( Constant.INSERTPERSONTABLE_PUT );
        interfaceAuthorization.setInterfaceAddress( Constant.INTERFACEADDRESS_PUT );
        interfaceAuthorization.setInterfaceType( Constant.INTERFACETYPE_PUT );
        interfaceAuthorization.setCallTime( new Date() );
        interfaceAuthorization.setCallNumber( Constant.CALLNUMBER );
        interfaceAuthorization.setUserId( user.getUserId() );
        interfaceAuthorization.setRequestParameters( JSON.toJSONString( personTableList ) );
        interfaceAuthorization.setExceptionInformation( JSON.toJSONString( responseResult ) );
        return interfaceAuthorization;
    }

    /**
     * 第三方系统 查询人员信息
     *
     * @param pageInfoVo    //分页信息
     * @param personTableVo //查询信息
     * @return
     */
    @RequestMapping(value = "/getPersonTableAll", method = RequestMethod.GET)
    public ResponseResult queryPersonTableList(PageInfoVo pageInfoVo, PersonTable personTableVo) {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();
        List<PersonTable> personTableList = null;
        //返回结果集
        List<PersonTable> personTableList1 = new ArrayList<>();
        InterfaceAuthorization interfaceAuthorization = null;
        //创建subject对象,获取用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //返回分页信息
        PageInfoVo pag = null;
        try {
            //判断查询条件是否为空
            if (personTableVo != null) {
                //如果所在部门为空，默认查询用户有什么权限
                if (StringUtils.isBlank( personTableVo.getDepartments() )) {
                    //根据用户id和当前的接口信息查询部门信息
                    List<DataPermission> permissionList = dataPermissionService.selectByPopupWay( user.getUserId(), Constant.GETPERSONTABLEALL );
                    //判空
                    if (ListUtils.isNotNullAndEmptyList( permissionList )) {
                        //组成一个新的list集合
                        List<String> stringList = new ArrayList<String>();
                        for (DataPermission dataPermission : permissionList) {
                            stringList.add( dataPermission.getPopupWay());
                        }
                        //list集合转数组
                        String[] strs = stringList.toArray( new String[stringList.size()] );
                        //将数组赋值
                        personTableVo.setDepartmentsarr( strs );
                    }
                }
            }
            //查询人员信息
            personTableList = interfaceManagementService.queryPersonTableList( personTableVo, pageInfoVo );
            //图片地址
            String photoURL = Config.getPhotoUrl("photoURL");
            if (ListUtils.isNotNullAndEmptyList( personTableList )) {
                for (PersonTable personTable : personTableList) {
                    //根据人员id查询照片信息
                    List<PersonFaceInfomationTable> list = interfaceManagementService.queryPersonFaceInfomationTableByPersonId( personTable.getPersonId() );
                    for (PersonFaceInfomationTable personFaceInfomationTable:list){
                        if(StringUtils.isNotBlank( personFaceInfomationTable.getFaceAddress() )){
                            personFaceInfomationTable.setFaceAddress(photoURL+personFaceInfomationTable.getFaceAddress() );
                        }
                        if(StringUtils.isNotBlank( personFaceInfomationTable.getCampusCardAddress() )){
                            personFaceInfomationTable.setCampusCardAddress(photoURL+personFaceInfomationTable.getCampusCardAddress() );
                        }
                        if(StringUtils.isNotBlank( personFaceInfomationTable.getIdcardImage() )){
                            personFaceInfomationTable.setIdcardImage(photoURL+personFaceInfomationTable.getIdcardImage() );
                        }
                        if(StringUtils.isNotBlank( personFaceInfomationTable.getFaceImage() )){
                            personFaceInfomationTable.setFaceImage(photoURL+personFaceInfomationTable.getFaceImage() );
                        }
                    }
                    personTable.setPersonFaceInfomationTable( list );
                    personTableList1.add( personTable );
                }
                //查询人员条数
                int totalCount = interfaceManagementService.dataCount( personTableVo );
                pag = new PageInfoVo();
                pag.setRowNum( pageInfoVo.getRowNum() );
                pag.setPageSize( pageInfoVo.getPageSize() );
                pag.setTotalRowNum( totalCount );
                //判空
                if (personTableVo != null) {
                    //判断所在部门的信息是都为空
                    if (personTableVo.getDepartmentsarr() != null) {
                        personTableVo.setDepartmentsarr( null );
                    }

                }
                if (personTableList1.size() > 0) {
                    responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
                    responseResult.setPageInfo( pag );
                    responseResult.setDatas( personTableList1 );
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization.setInterfaceName( "查询人员接口" );
                    interfaceAuthorization.setInterfaceAddress( "/interfaceManagement/getPersonTableAll" );
                    interfaceAuthorization.setInterfaceType( "GET" );
                    interfaceAuthorization.setCallTime( new Date() );
                    interfaceAuthorization.setCallNumber( Constant.CALLNUMBER );
                    interfaceAuthorization.setUserId( user.getUserId() );
                    ResponseResult responseResult1 = new ResponseResult();
                    responseResult1.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                    responseResult1.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
                    interfaceAuthorization.setRequestParameters( JSON.toJSONString( personTableVo ) );
                    interfaceAuthorization.setExceptionInformation( JSON.toJSONString( responseResult1 ) );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );

                }
            }
            //判断用户查询的信息为空的时候
            if (personTableList.size() == 0) {
                responseResult.setResponseCode( 106 );
                responseResult.setResponseDescription( "查询信息为空" );
                pag = new PageInfoVo();
                pag.setRowNum( pageInfoVo.getRowNum() );
                pag.setPageSize( pageInfoVo.getPageSize() );
                responseResult.setPageInfo( pag );
                interfaceAuthorization = new InterfaceAuthorization();
                interfaceAuthorization = commonGetPersonTableAll( interfaceAuthorization, user, responseResult, personTableVo );
                interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            }

        } catch (Exception ex) {
            responseResult.setResponseCode( Constant.RESPONSE_FAIL_CODE );
            responseResult.setResponseDescription( Constant.RESPONSE_FAIL_DESCRIPTION );
            responseResult.setDatas( "服务器运行异常" );
            interfaceAuthorization = new InterfaceAuthorization();
            interfaceAuthorization = commonGetPersonTableAll( interfaceAuthorization, user, responseResult, personTableVo );
            interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
            interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            logger.error( "人员信息查询失败" + ex.getMessage() );
        }
        return responseResult;
    }

    /**
     * 第三方系统调用 查询人员接口  公共
     *
     * @param interfaceAuthorization
     * @param user
     * @param responseResult
     * @param personTableVo
     * @return
     */
    private InterfaceAuthorization commonGetPersonTableAll(InterfaceAuthorization interfaceAuthorization, User user,
                                                           ResponseResult responseResult, PersonTable personTableVo) {
        interfaceAuthorization.setInterfaceName( "查询人员接口" );
        interfaceAuthorization.setInterfaceAddress( "/interfaceManagement/getPersonTableAll" );
        interfaceAuthorization.setInterfaceType( "GET" );
        interfaceAuthorization.setCallTime( new Date() );
        interfaceAuthorization.setCallNumber( Constant.CALLNUMBER );
        interfaceAuthorization.setUserId( user.getUserId() );
        interfaceAuthorization.setRequestParameters( JSON.toJSONString( personTableVo ) );
        interfaceAuthorization.setExceptionInformation( JSON.toJSONString( responseResult ) );
        return interfaceAuthorization;
    }

    /**
     * 第三方系统  查询人员类型接口
     *
     * @param dicType
     * @param dicCode
     * @return
     */
    @RequestMapping(value = "/queryDicName", method = RequestMethod.GET)
    public ResponseResult queryDicName(String dicType, String dicCode) {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();
        List<DataDictionary> dataDictionaryList = null;
        DataDictionary dataDictionary = null;
        InterfaceAuthorization interfaceAuthorization = null;
        //创建subject对象,获取用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (StringUtils.isBlank( dicType ) && StringUtils.isBlank( dicCode )) {
            //根据人员类型编码查询人员类型
            dataDictionaryList = interfaceManagementService.selectPersonTypeList( Constant.IDENTICATIONINFO );
            responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
            responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
            responseResult.setDatas( dataDictionaryList );
            interfaceAuthorization = new InterfaceAuthorization();
            interfaceAuthorization = commonQueryDicName( interfaceAuthorization, user, responseResult );
            interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
        } else {
            dataDictionary = new DataDictionary();
            dataDictionary.setDicType( dicType );
            dataDictionary.setDicCode( dicCode );
            //根据字典类型和code获取名称
            DataDictionary dataDic = interfaceManagementService.queryDicNameByDicTypeAndDicCode( dataDictionary );
            responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
            responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
            interfaceAuthorization = new InterfaceAuthorization();
            interfaceAuthorization = commonQueryDicName( interfaceAuthorization, user, responseResult );
            interfaceAuthorization.setRequestParameters( JSON.toJSONString( dataDictionary ) );
            interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            responseResult.setDatas( dataDic );
        }
        return responseResult;
    }

    /**
     * 第三方系统调用 查询人员类型接口  公共
     *
     * @param interfaceAuthorization
     * @param user
     * @return
     */
    private InterfaceAuthorization commonQueryDicName(InterfaceAuthorization interfaceAuthorization, User user,
                                                      ResponseResult responseResult) {
        interfaceAuthorization.setInterfaceName( "查询人员类型接口" );
        interfaceAuthorization.setInterfaceAddress( "/interfaceManagement/queryDicName" );
        interfaceAuthorization.setInterfaceType( "GET" );
        interfaceAuthorization.setCallTime( new Date() );
        interfaceAuthorization.setCallNumber( Constant.CALLNUMBER );
        interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
        interfaceAuthorization.setUserId( user.getUserId() );
        interfaceAuthorization.setExceptionInformation( JSON.toJSONString( responseResult ) );
        return interfaceAuthorization;
    }

    /**
     * 第三方系统 查询人员更新日志接口
     *
     * @param pageInfoVo
     * @return
     */
    @RequestMapping(value = "/queryPersonDataOperationTable", method = RequestMethod.GET)
    public ResponseResult queryPersonDataOperationTable(PageInfoVo pageInfoVo, PersonDataOperationTableVo personDataOperationTableVo) {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();
        List<PersonDataOperationTable> personDataOperationTableList = null;
        InterfaceAuthorization interfaceAuthorization = null;
        //创建subject对象,获取用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        PageInfoVo pag = null;
        //查询人员条数
        int totalCount = 0;
        try {
            //开始时间
            if (personDataOperationTableVo.getDateTimeStart() == null) {
                personDataOperationTableVo.setDateTimeStart(null);
            }
            //结束时间
            if (personDataOperationTableVo.getDateTimeEnd() == null) {
                personDataOperationTableVo.setDateTimeEnd(null);
            }
            //查询人员数据更新日志表
            personDataOperationTableList = personDataOperationTableService.queryPersonDataOperationTableList( pageInfoVo, personDataOperationTableVo );
            //查询人员条数
            totalCount = personDataOperationTableService.dataCount( personDataOperationTableVo );
            //返回分页信息
            pag = new PageInfoVo();
            //判空
            if (ListUtils.isNotNullAndEmptyList( personDataOperationTableList )) {
                responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
                responseResult.setDatas( personDataOperationTableList );
                pag.setRowNum( pageInfoVo.getRowNum() );
                pag.setPageSize( pageInfoVo.getPageSize() );
                pag.setTotalRowNum( totalCount );
                responseResult.setPageInfo( pag );
                interfaceAuthorization = new InterfaceAuthorization();
                interfaceAuthorization.setInterfaceName( "查询人员更新日志接口" );
                interfaceAuthorization.setInterfaceAddress( "/interfaceManagement/queryPersonDataOperationTable" );
                interfaceAuthorization.setInterfaceType( "GET" );
                interfaceAuthorization.setCallTime( new Date() );
                interfaceAuthorization.setCallNumber( Constant.CALLNUMBER );
                interfaceAuthorization.setUserId( user.getUserId() );
                interfaceAuthorization.setRequestParameters( JSON.toJSONString( personDataOperationTableVo ) );
                ResponseResult responseResult1 = new ResponseResult();
                responseResult1.setPageInfo( pag );
                responseResult1.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                responseResult1.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
                interfaceAuthorization.setExceptionInformation( JSON.toJSONString( responseResult1) );
                interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            }
            if (personDataOperationTableList.size() == 0) {
                responseResult.setResponseCode( 106 );
                responseResult.setResponseDescription( "查询信息为空" );
                interfaceAuthorization = new InterfaceAuthorization();
                interfaceAuthorization = commonQueryPersonDataOperationTable( interfaceAuthorization, user, responseResult, personDataOperationTableVo );
                interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            }
        } catch (Exception ex) {
            logger.error( "查询人员更新日志接口失败" + ex.getMessage() );
        }

        return responseResult;
    }

    /**
     * 第三方系统调用 查询人员更新日志接口  公共
     *
     * @param interfaceAuthorization
     * @param user
     * @param personDataOperationTableVo
     * @return
     */
    private InterfaceAuthorization commonQueryPersonDataOperationTable(InterfaceAuthorization interfaceAuthorization, User user,
                                                                       ResponseResult responseResult, PersonDataOperationTableVo personDataOperationTableVo) {
        interfaceAuthorization.setInterfaceName( "查询人员更新日志接口" );
        interfaceAuthorization.setInterfaceAddress( "/interfaceManagement/queryPersonDataOperationTable" );
        interfaceAuthorization.setInterfaceType( "GET" );
        interfaceAuthorization.setCallTime( new Date() );
        interfaceAuthorization.setCallNumber( Constant.CALLNUMBER );
        interfaceAuthorization.setUserId( user.getUserId() );
        interfaceAuthorization.setRequestParameters( JSON.toJSONString( personDataOperationTableVo ) );
        interfaceAuthorization.setExceptionInformation( JSON.toJSONString( responseResult ) );
        return interfaceAuthorization;
    }

    /**
     * 查询字典信息接口
     *
     * @return
     */
    @RequestMapping(value = "/queryDictionary", method = RequestMethod.GET)
    public ResponseResult queryDictionary(DictionaryVo dictionaryVo) {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();

        List<DataDictionary> allDicType = dataDictionaryService.getAllDicType();
        for(DataDictionary dataDictionary: allDicType){
            List<DataDictionary> dataDictionaryList = null;
            if (StringUtils.isNotBlank( dictionaryVo.getDicType() )) {
                if(dictionaryVo.getDicType().equals(dataDictionary.getDicCode())){
                    dataDictionaryList = interfaceManagementService.selectPersonTypeList( dictionaryVo.getDicType() );
                    responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
                    responseResult.setDatas( dataDictionaryList );
                }else {
                    if (StringUtils.isBlank(responseResult.getResponseDescription())) {
                        responseResult.setResponseCode(116);
                        responseResult.setResponseDescription("未查到该类型的字典信息！请参考正确的请求参数重新查询");
                    }
                }
                //根据人员类型编码查询人员类型
            } else {
                responseResult.setResponseCode( 109 );
                responseResult.setResponseDescription( "dicType不能为空" );
            }
        }
        return responseResult;
    }

    /**
     * 查询全部字典信息接口
     *
     * @return
     */
    @RequestMapping(value = "/queryAllDictionary", method = RequestMethod.GET)
    public ResponseResult queryAllDictionary() {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();

        Map<String,Object> map=new HashMap<>();

        //查询所在部门或院系信息
        List<DataDictionary>  departmentsList=dataDictionaryService.setlectDataDictionaryList( Constant.DEPARTMENT );
        //查询学历
        List<DataDictionary>  studentLevelList=dataDictionaryService.setlectDataDictionaryList( Constant.EDUCATION );
        //查询学级
        List<DataDictionary>  gradeList=dataDictionaryService.setlectDataDictionaryList( Constant.GRADE );
        //查询人员信息
        List<DataDictionary> identicationInfoList = dataDictionaryService.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
        //查询民族
        List<DataDictionary> ethnicityList = dataDictionaryService.setlectDataDictionaryList( Constant.ETHNICITY );
        //查询性别
        List<DataDictionary> sexList = dataDictionaryService.setlectDataDictionaryList( Constant.SEX );
        //查询数据来源
        List<DataDictionary> dataSourceList = dataDictionaryService.setlectDataDictionaryList( Constant.DATASOURCE );
        map.put("所在部门或院系",departmentsList);
        map.put("学历",studentLevelList);
        map.put("年级",gradeList);
        map.put("人员类型",identicationInfoList);
        map.put("民族",ethnicityList);
        map.put("性别",sexList);
        map.put("数据来源",dataSourceList);
            //根据人员类型编码查询人员类型
        responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
        responseResult.setResponseDescription( Constant.RESPONSE_SUCCESS_DESCRIPTION );
        responseResult.setDatas( map );

        return responseResult;
    }


    /**
     * 公共的方法
     *
     * @param personFaceInfomationTable
     * @param personTable
     * @param personId
     */
    private PersonCodeVo personFaceInfomationTableUtils(String dataSource, List<PersonFaceInfomationTable> personFaceInfomationTable, PersonTableVo personTable, Integer personId) {
        PersonCodeVo personCodeVo = null;
        try {
            List<PersonFaceInfomationTable> personFaceInfomationTableList = new ArrayList<>();
            //循环遍历-----组装成新的集合
            for (PersonFaceInfomationTable pnfi : personFaceInfomationTable) {
                String faceAddress = null;
                String campusCardAddress = null;
                //人脸图片地址
                if (StringUtils.isNotBlank( pnfi.getFaceAddress() )) {
                    faceAddress = photoPath( pnfi.getFaceAddress() );
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpeg;base64,","");
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpg;base64,","");

                }
                //校园卡正面照地址
                if (StringUtils.isNotBlank( pnfi.getCampusCardAddress() )) {
                    campusCardAddress = photoPath( pnfi.getCampusCardAddress() );
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpeg;base64,","");
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpg;base64,","");

                }
//                if(StringUtils.isNotBlank(pnfi.getCampusCardAddress())){
//                     campusCardAddress = pnfi.getCampusCardAddress().replace("data:image/jpeg;base64,","");
//                     campusCardAddress = pnfi.getCampusCardAddress().replace("data:image/jpg;base64,","");
//
//                }
                pnfi.setFaceAddress( faceAddress );
                pnfi.setCampusCardAddress( campusCardAddress );
                pnfi.setPersonId( personId );
                pnfi.setDataSource( "接口调用采集-"+dataSource );
                personFaceInfomationTableList.add( pnfi );
            }
            //添加人员相关照片信息
            personFaceInfomationTableService.insertPersonNewFaceInfomationTable( personFaceInfomationTableList );
            personCodeVo = new PersonCodeVo();
            //返回人员编码
            personCodeVo.setPersonCode( personTable.getPersonCode() );
            personCodeVo.setIdentityNo( personTable.getIdentityNo() );
            personCodeVo.setPersonName( personTable.getPersonName() );
        } catch (Exception ex) {
            logger.error( "人员接口失败！！" + ex.getMessage() );
        }
        return personCodeVo;
    }

    private PersonCodeVo personFaceInfomationTableUtils2(String userName, List<PersonFaceInfomationTable> personFaceInfomationTable, PersonTableVo personTable, Integer personId) {
        PersonCodeVo personCodeVo = null;
        try {
            List<PersonFaceInfomationTable> personFaceInfomationTableList = new ArrayList<>();
            //循环遍历-----组装成新的集合
            for (PersonFaceInfomationTable pnfi : personFaceInfomationTable) {
                String faceAddress = null;
                String campusCardAddress = null;
                //人脸图片地址
                if (StringUtils.isNotBlank( pnfi.getFaceAddress() )) {
                    faceAddress = photoPath( pnfi.getFaceAddress() );
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpeg;base64,","");
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpg;base64,","");

                }
                //校园卡正面照地址
                if (StringUtils.isNotBlank( pnfi.getCampusCardAddress() )) {
                    campusCardAddress = photoPath( pnfi.getCampusCardAddress() );
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpeg;base64,","");
//                    faceAddress = pnfi.getFaceAddress().replace("data:image/jpg;base64,","");

                }
//                if(StringUtils.isNotBlank(pnfi.getCampusCardAddress())){
//                     campusCardAddress = pnfi.getCampusCardAddress().replace("data:image/jpeg;base64,","");
//                     campusCardAddress = pnfi.getCampusCardAddress().replace("data:image/jpg;base64,","");
//
//                }
                pnfi.setFaceAddress( faceAddress );
                pnfi.setCampusCardAddress( campusCardAddress );
                pnfi.setPersonId( personId );
//                pnfi.setDataSource( Constant.DATASOURCE_CODE );
                pnfi.setDataSource( "接口调用采集-" + userName );
                personFaceInfomationTableList.add( pnfi );
            }
            //添加人员相关照片信息
            personFaceInfomationTableService.insertPersonNewFaceInfomationTable( personFaceInfomationTableList );
            personCodeVo = new PersonCodeVo();
            //返回人员编码
            personCodeVo.setPersonCode( personTable.getPersonCode() );
            personCodeVo.setIdentityNo( personTable.getIdentityNo() );
            personCodeVo.setPersonName( personTable.getPersonName() );
        } catch (Exception ex) {
            logger.error( "人员接口失败！！" + ex.getMessage() );
        }
        return personCodeVo;
    }

    /**
     * 第三方系统调用新增人员接口
     *
     * @param personTableList
     * @return
     */
    @RequestMapping(value = "/insertOrUpdatePersonTable", method = RequestMethod.POST)
    @Transactional
    public ResponseResult insertOrUpdatePersonTable(@RequestBody Datas personTableList) {
        //返回数据类型
        ResponseResult responseResult = new ResponseResult();
        int i = 0;
        int j = 0;
        //定义personCode集合
        List<PersonCodeVo> personCodeVos = new ArrayList<>();
        PersonCodeVo personCodeVo = null;
        PersonDataOperationTable personDataOperationTable = null;
        //创建subject对象,获取用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        InterfaceAuthorization interfaceAuthorization = null;
        try {
            //判断人员信息是否为空
            if (CollectionUtils.isEmpty( personTableList.getPersonTableVoList() )) {
                responseResult.setResponseCode( 111 );
                responseResult.setResponseDescription( "参数不能为空" );
                return responseResult;
            }
            //新增人员基本信息
            i = interfaceManagementService.insertOrUpdatePersonTableList( personTableList.getPersonTableVoList(), user );
            switch (i) {
                case 3:
                    responseResult.setResponseCode( 99 );
                    responseResult.setResponseDescription( "校园卡或一卡通照片最多上传一张" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;

                case 2:
                    responseResult.setResponseCode( 101 );
                    responseResult.setResponseDescription( "姓名不能为空" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case 0:
                    responseResult.setResponseCode( 100 );
                    responseResult.setResponseDescription( "新增人员接口失败" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case 1:
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization.setRequestParameters( JSON.toJSONString( personTableList.getPersonTableVoList() ) );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            //实现逻辑，在公共实现中
                            personCodeVo = personFaceInfomationTableUtils2( user.getUserName(), personTable.getFaceInfomation(), personTable, personTable.getPersonId() );
                            personCodeVos.add( personCodeVo );
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            //实现逻辑，在公共实现中
                            personFaceInfomationTableUtils2( user.getUserName(), personTable.getCardInfomation(), personTable, personTable.getPersonId() );
                        }
                        //添加操作人员更新日志表
                        PersonTable personTable1 = new PersonTable();
                        personTable1.setPersonCode(personTable.getPersonCode());
                        personTable1.setCreateTime(personTable.getCreateTime());
                        personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personTable1 );
                        personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                    }
                    //返回信息值
                    responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_INSERTSUCCESS_DESCRIPTION );
                    responseResult.setDatas( personCodeVos );
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                    interfaceAuthorizationService.insertSuccessInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -1:
                    responseResult.setResponseCode( 102 );
                    responseResult.setResponseDescription( "教工号或学号或一卡通号为必填项" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -2:
                    responseResult.setResponseCode( 103 );
                    responseResult.setResponseDescription( "人脸照片至少上传一张且最多上传三张" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -3:
                    responseResult.setResponseCode( 104 );
                    responseResult.setResponseDescription( "身份证号格式或者位数不正确" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -4:
                    responseResult.setResponseCode( 105 );
                    responseResult.setResponseDescription( "手机号格式不正确" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -5:
                    responseResult.setResponseCode( 107 );
                    responseResult.setResponseDescription( "所在部门或院系不能为空" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -6:
                    responseResult.setResponseCode( 108 );
                    responseResult.setResponseDescription( "用户暂无该部门权限" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -7:
                    responseResult.setResponseCode( 110 );
                    responseResult.setResponseDescription( "身份信息不能为空" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -8:
                    responseResult.setResponseCode( 112 );
                    responseResult.setResponseDescription( "身份证号重复" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -9:
                    int i1=interfaceManagementMapper.updatePersonTableListByidentityTypeCode(personTableList.getPersonTableVoList());
                    //循环遍历修改的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //根据人员id查询人员信息
                        PersonTable selectPersonTable = interfaceManagementService.selectPersonTableByIdentityTypeCode( personTable.getIdentityTypeCode() );
                        // 查询对应人员的数据来源
                        List<PersonFaceInfomationTable> personFaceInfomationTable = interfaceManagementService.queryPersonFaceInfomationTableByPersonId( selectPersonTable.getPersonId() );
                        String filePath = Config.getPhotoUrl("filePath"); //指定存储地址
                        personFaceInfomationTable.forEach(p->{
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
                        //根据人员id删除人员照片信息
                        j = personFaceInfomationTableService.deletePersonTableByImagePersonId( selectPersonTable.getPersonId() );
                        if (j != 0) {
                            //判断图片信息
                            if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                                //实现逻辑，在公共实现中
                                personCodeVo = personFaceInfomationTableUtils( user.getUserName(), personTable.getFaceInfomation(), personTable,selectPersonTable.getPersonId() );
                                personCodeVo.setPersonCode(selectPersonTable.getPersonCode());
                                personCodeVos.add( personCodeVo );
                            }
                            if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                                //实现逻辑，在公共实现中
                                personFaceInfomationTableUtils( user.getUserName(), personTable.getCardInfomation(), personTable,selectPersonTable.getPersonId() );
                            }
                            //添加操作人员更新日志表
                            PersonTable personTable1 = new PersonTable();
                            personTable1.setPersonCode(selectPersonTable.getPersonCode());
                            personTable1.setCreateTime(personTable.getCreateTime());
                            personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable( personTable1 );
                            personDataOperationTableService.insertPersonDataOperationTable( personDataOperationTable );
                        }
                    }
                    responseResult.setResponseCode( Constant.RESPONSE_SUCCESS_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_UPDATESUCCESS_DESCRIPTION );
                    responseResult.setDatas( personCodeVos );
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPUT( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_1 );
                    interfaceAuthorizationService.insertSuccessInterfaceAuthorization( interfaceAuthorization );
                    break;
               case -10:
                    responseResult.setResponseCode( 114);
                    responseResult.setResponseDescription( "一卡通号不能超过二十位" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                /* case -11:
                    responseResult.setResponseCode( 115);
                    responseResult.setResponseDescription( "人员姓名必须是纯中文" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;*/
                case -12:
                    responseResult.setResponseCode( 116);
                    responseResult.setResponseDescription( "性别不存在" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -13:
                    responseResult.setResponseCode( 117);
                    responseResult.setResponseDescription( "民族不存在" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                case -14:
                    responseResult.setResponseCode( 118);
                    responseResult.setResponseDescription( "人员类型不存在" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
                default:
                    responseResult.setResponseCode( Constant.RESPONSE_FAIL_CODE );
                    responseResult.setResponseDescription( Constant.RESPONSE_FAIL_DESCRIPTION );
                    responseResult.setDatas( "服务器运行异常" );
                    //循环遍历新增后的人员信息
                    for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                        //判断图片信息
                        if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                            personTable.setFaceInfomation(null);
                        }
                        if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                            personTable.setCardInfomation(null);
                        }
                    }
                    //保存到接口调用统计表
                    interfaceAuthorization = new InterfaceAuthorization();
                    interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
                    interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
                    interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
                    break;
            }

        } catch (Exception ex) {
            responseResult.setResponseCode( Constant.RESPONSE_FAIL_CODE );
            responseResult.setResponseDescription( Constant.RESPONSE_FAIL_DESCRIPTION );
            responseResult.setDatas( "服务器运行异常" );
            //循环遍历新增后的人员信息
            for (PersonTableVo personTable : personTableList.getPersonTableVoList()) {
                //判断图片信息
                if (ListUtils.isNotNullAndEmptyList( personTable.getFaceInfomation() )) {
                    personTable.setFaceInfomation(null);
                }
                if (ListUtils.isNotNullAndEmptyList( personTable.getCardInfomation() )) {
                    personTable.setCardInfomation(null);
                }
            }
            //保存到接口调用统计表
            interfaceAuthorization = new InterfaceAuthorization();
            interfaceAuthorization = interfaceAuthorizationCommonPOST( interfaceAuthorization, user, responseResult, personTableList );
            interfaceAuthorization.setCallStatus( Constant.CALLSTATUS_2 );
            interfaceAuthorizationService.insertErrInterfaceAuthorization( interfaceAuthorization );
            logger.error( "新增人员接口失败！！" + ex.getMessage() );
        }
        return responseResult;
    }


}
