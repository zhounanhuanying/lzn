package com.bfdb.controller;

import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.mapper.BaseOrganizitionMapper;
import com.bfdb.mapper.BaseParkManagementMapper;
import com.bfdb.service.DataDictionaryService;
import com.bfdb.service.PersonTableService;
import com.bfdb.service.SysServerService;
import com.bfdb.untils.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 页面操作记录
 */
@Controller
public class BookAOPController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private SysServerService sysServerService;

    @Autowired
    private PersonTableService personTableService;

    @Autowired
    private BaseParkManagementMapper baseParkManagementMapper;

    @Autowired
    private BaseOrganizitionMapper baseOrganizitionMapper;

    //用户角色页面
    @Operation(name = "角色管理")
    @RequestMapping("/role")
    public ModelAndView role() {
        ModelAndView view = new ModelAndView();
        view.setViewName( "role/role" );
        return view;
    }

    //用户页面
    @Operation(name = "用户管理")
    @RequestMapping("/userRole")
    public ModelAndView user() {
        ModelAndView view = new ModelAndView();
        //字典类
        view= dataDictionaryCommon();
        view.setViewName( "role/user" );
        return view;
    }

    /**
     * 系统配置页面
     *
     * @return
     */
    @Operation(name = "系统配置")
    @RequestMapping("/systemConfiguration")
    public ModelAndView systemConfiguration() {
        ModelAndView modelAnd=new ModelAndView();
        //字典类
        modelAnd= dataDictionaryCommon();
        modelAnd.setViewName("systemConfiguration/systemConfiguration");
        return modelAnd;
    }

    /**
     * 日志页面
     *
     * @return
     */
    @Operation(name = "日志管理")
    @RequestMapping("/logBook")
    public String logBook() {
        return "logBook/logBook";
    }

    /**
     * 接口调用统计
     * @return
     */
    @Operation(name = "接口调用统计")
    @RequestMapping("/interfaceAuthorizationPage")
    public String interfaceAuthorizationPage() {
        return "logBook/interfaceAuthorization";
    }

    /**
     * 字典管理页面
     *
     * @return
     */
    @Operation(name = "字典管理")
    @RequestMapping("/dataDictionary")
    public ModelAndView dataDictionary() {
        ModelAndView view = new ModelAndView();
        //查询字典名称和字典类型code
        List<DataDictionary> dicTypeList=dataDictionaryService.getAllDicType();
//      JSONObject ServerJson = new JSONObject();
//        //循环遍历信息
//        for (DataDictionary dataDictionary : dicTypeList) {
//            ServerJson.put(dataDictionary.getDicCode(), dataDictionary.getDicName());
//        }
        view.addObject("ServerJson",dicTypeList);
        view.setViewName( "dataDictionary/dataDictionary");
        return view;
    }





    /**
     * 设备管理页面
     *
     * @return
     */
    @Operation(name = "设备管理")
    @RequestMapping("/deviceManager")
    public ModelAndView deviceManager() {
        ModelAndView view = new ModelAndView();
        //字典类
        view= dataDictionaryCommon();
        view.setViewName( "deviceManager/deviceManager" );
        return  view;
    }


    /*
     *人员信息页面
     *
     * */
    @Operation(name = "人员管理")
    @RequestMapping(value = "/personManager", method = RequestMethod.GET)
    public ModelAndView personManager1() {
        ModelAndView modelAnd=new ModelAndView();
        //字典类
        modelAnd= dataDictionaryCommon();
        modelAnd.setViewName("personManager/personManager");
        return modelAnd;
    }


    /**
     * 人脸信息采集管理
     *
     * @return
     */
    /*@RequestMapping("/WxpersonCollection")
    public ModelAndView dataCollection1() {
        ModelAndView view = new ModelAndView();
        //字典类
        view= dataDictionaryCommon();
        view.setViewName( "dataCollection/WxpersonCollection" );
        return view;
    }*/

    /**
     * 人证核验终端页面
     * @return
     */
    @Operation(name = "人证核验终端管理")
    @RequestMapping(value = "/personverificationTerminal", method = RequestMethod.GET)
    public ModelAndView personverificationTerminal() {
        ModelAndView modelAnd=new ModelAndView();
        //字典类
        modelAnd= dataDictionaryCommon();
        modelAnd.setViewName("dataCollection/personverificationTerminal");
        return modelAnd;
    }
    /**
     * 采集设备管理
     * @return
     */
    @Operation(name = "采集设备管理")
    @RequestMapping(value = "/serverManager", method = RequestMethod.GET)
    public ModelAndView serverManager() {
        ModelAndView modelAnd=new ModelAndView();
        //字典类
        modelAnd= dataDictionaryCommon();
        modelAnd.setViewName("serverManager/serverManager");
        return modelAnd;
    }

    /**
     * 数据看板
     * @return
     */
    @Operation(name = "数据看板")
    @RequestMapping(value = "/dataKanban", method = RequestMethod.GET)
    public ModelAndView dataKanban() {
        ModelAndView modelAnd=new ModelAndView();
        //字典类
        modelAnd= dataDictionaryCommon();
        modelAnd.setViewName("dataKanban/dataKanban");
        return modelAnd;
    }

    /**
     * 微信公众号
     * @return
     */
    @Operation(name = "微信公众号")
    @RequestMapping(value = "/wechatpublic", method = RequestMethod.GET)
    public ModelAndView wechatpublic() {
        ModelAndView modelAnd=new ModelAndView();
        //字典类
        modelAnd= dataDictionaryCommon();
        modelAnd.setViewName("dataCollection/Wechatpublic");
        return modelAnd;
    }
    /**
     * 通用方法
     * @return
     */
    public ModelAndView dataDictionaryCommon() {
        ModelAndView view = new ModelAndView();
        //查询人员信息
        List<DataDictionary> identicationInfoList = dataDictionaryService.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
        view.addObject( "identicationInfoList", identicationInfoList );
        //查询园区信息
        List<BasePark> parkList = baseParkManagementMapper.queryBasePark();
        view.addObject( "parkList", parkList );
        //查询组织机构信息
        List<BaseOrganizition> baseOrganizitionList = baseOrganizitionMapper.queryBaseOrganizition();
        view.addObject( "baseOrganizitionList", baseOrganizitionList );

        //查询所在部门或院系信息
        List<DataDictionary> departmentsList = dataDictionaryService.setlectDataDictionaryList( Constant.DEPARTMENT );
        view.addObject( "departmentsList", departmentsList );
       /* List<DataDictionary> departmentsList = dataDictionaryService.setlectDataDictionaryListTest();
        view.addObject( "departmentsList", departmentsList );*/
        //查询学历
        List<DataDictionary> studentLevelList = dataDictionaryService.setlectDataDictionaryList( Constant.EDUCATION );
        view.addObject( "studentLevelList", studentLevelList );
        //查询学级信息
        List<DataDictionary> gradeList = dataDictionaryService.setlectDataDictionaryList( Constant.GRADE );
        view.addObject( "gradeList", gradeList );
        //查询民族
        List<DataDictionary> ethnicityList = dataDictionaryService.setlectDataDictionaryList( Constant.ETHNICITY );
        view.addObject( "ethnicityList", ethnicityList );
        //获取端口号
//        String serverPort = FileUtils.getProperties( "/application.properties", "server.port" );
//        view.addObject( "serverPort", serverPort );
        //机构类型
        List<DataDictionary> dataDictionaryList = dataDictionaryService.setlectDataDictionaryList( Constant.MECHANISM );
        view.addObject( "dataDictionaryList", dataDictionaryList );
        //性别
        List<DataDictionary> sexList = dataDictionaryService.setlectDataDictionaryList( Constant.SEX );
        view.addObject( "sexList", sexList );
        //查询是否开启页面显示的人证核验终端的集合
        List<SysServer> sysServerList = sysServerService.getSysServerByVerificationStatusList();
        view.addObject( "sysServerList", sysServerList );
        //查询是否开启页面显示的人证核验终端的集合
        List<SysServer> sysServerListSuccess = sysServerService.getSysServerListSuccess();
        view.addObject( "sysServerListSuccess", sysServerListSuccess );
        return view;
    }
}
