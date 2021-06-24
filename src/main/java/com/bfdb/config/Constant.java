package com.bfdb.config;

import com.bfdb.entity.PersonTable;

import java.util.List;
import java.util.UUID;

public class Constant {

    /**
     * 失效token,code
     */
    public static final String OVERDUE_TOKEN_CODE = "{\"code\":1001,\"msg\":\"token失效,请重新登录\"}";
    /**
     * token不一致
     */
    public static final String INCONSISTENT_TOKEN_CODE = "{\"code\":1004,\"msg\":\"token不一致,请重新登录\"}";

    /**
     * token数据错误
     */
    public static final String ERROR_TOKEN = "Token数据错误";
    /**
     * token数据错误
     */
    public static final String ERROR_TOKEN_CODE = "{\"code\":1002,\"msg\":\"token错误,请重新登录\"}";

    /**
     * token为null
     */
    public static final String NONE_TOKEN = "Token为空";


    public static final String NONE_TOKEN_CODE ="{\"code\":1003,\"msg\":\"暂无权限，请联系管理员\"}";

    /**
     * 头部token的key
     */
    public static final String HEADER_AUTHORIZATION = "access_token";

    /**
     * token唯一识别码
     */
    public static final String JWT_ID = UUID.randomUUID().toString();

    /**
     * 加密密文，必须妥善保管，禁止外泄
     */
    public static final String JWT_SECRET = "encryptedCiphertext";

    /**
     * 请求成功返回code值
     */
    public static final Integer RESPONSE_SUCCESS_CODE  = 200;

    /**
     * 请求成功返回描述
     */
    public static final String RESPONSE_SUCCESS_DESCRIPTION  = "success";
    /**
     * 请求成功返回描述
     */
    public static final String RESPONSE_UPDATESUCCESS_DESCRIPTION  = "修改成功";
    /**
     * 请求成功返回描述
     */
    public static final String RESPONSE_INSERTSUCCESS_DESCRIPTION  = "添加成功";
    /**
     * 请求失败返回code值
     */
    public static final Integer RESPONSE_FAIL_CODE  = 0;

    /**
     * 请求成功返回描述
     */
    public static final String RESPONSE_FAIL_DESCRIPTION  = "error";
    /**
     * 机构类型
     */
    public static final String  MECHANISM  = "mechanism";
    /**
     * 机构类型
     */
    public static final String  SEX  = "sex";
    //所在部门或者院系班级
    public static final String  DEPARTMENT  = "department";
    //学历
    public static final String  EDUCATION  = "Education";
    //学级
    public static final String  GRADE  = "grade";
    //人员类型
    public static final String  IDENTICATIONINFO = "personType";
    //民族信息
    public static final String  ETHNICITY = "ethnicity";
    //园区类型信息
    public static final String  PARKTYPE = "parkType";
    //数据来源信息
    public static final String  DATASOURCE = "dataSource";
    //接口名称
    public static final String INSERTPERSONTABLE = "新增人员接口";
    //接口
    public static final String INTERFACEADDRESS = "/interfaceManagement/insertPersonTable";
    //请求类型
    public static final String INTERFACETYPE = "POST";
    //接口名称
    public static final String INSERTPERSONTABLE_PUT = "更新人员接口";
    //接口
    public static final String INTERFACEADDRESS_PUT  = "/interfaceManagement/updatePersonTable";
    //请求类型
    public static final String INTERFACETYPE_PUT  = "PUT";
    //调用次数
    public static final Integer CALLNUMBER = 1;
    //调用状态
    //失败
    public static final Integer CALLSTATUS_2 = 2;
    //成功
    public static final Integer CALLSTATUS_1= 1;
    //查询
    public static final String GETPERSONTABLEALL = "/interfaceManagement/getPersonTableAll";
    //查询人员类型接口
    public static final String QUERYDICNAME = "/interfaceManagement/queryDicName";

    // 导出当前页数据 用于存放当前页数据
    public static List<PersonTable> personTableList;

    /**
     * 请求成功返回code值
     */
    public static final String DATASOURCE_CODE  = "4";

    /**
     * 学生
     */
    public static final String IDENTICATIONINFO_2 = "2";
    /**
     * 留学生
     */
    public static final String IDENTICATIONINFO_3 = "3";




}
