package com.bfdb.controller;

import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.mapper.BaseParkManagementMapper;
import com.bfdb.service.BaseOrganizitionService;
import com.bfdb.service.DataDictionaryService;
import com.bfdb.service.PersonTableService;
import com.bfdb.service.SysServerService;
import com.bfdb.untils.Config;
import com.bfdb.untils.FileUtils;
import com.bfdb.untils.HttpGetUtil;
import com.bfdb.untils.Operation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面访问controller
 */
@Controller
public class PageController {


    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private SysServerService sysServerService;

    @Autowired
    private PersonTableService personTableService;

    @Autowired
    private BaseParkManagementMapper baseParkManagementMapper;

    private Map<String, String> map = new HashMap<>();
    /**
     * @return 登录页面
     */
    @RequestMapping("/")
    public String login() {
        return "login";
    }
    //登录
    @RequestMapping("/login")
    public String login1() {
        return "login";
    }
    //H5登录
    @RequestMapping("/H5login")
    public String H5login() {
        return "dataCollection/H5login";
    }
    //H5首页i
    @RequestMapping("/H5index")
    public String H5index() {
        return "dataCollection/H5index";
    }
    //微信公众号首页
    /*@RequestMapping("/WxIndex")
    public String WxIndex(HttpServletRequest request) {
        request.getRequestURL();
        return "dataCollection/WxIndex";
    }*/
    //以图搜图
    @RequestMapping("/searchByImage")
    public String searchByImage() {
        return "AiAnalysis/SearchByImage";
    }
    //人脸比对
    @RequestMapping("/faceComparison")
    public String faceComparison() {
        return "AiAnalysis/FaceComparison";
    }
    //AI配置管理
    @RequestMapping("/aiConfig")
    public String aiConfig() {
        return "AiAnalysis/AiConfig";
    }
    /**
     * @return 未授权页面
     */
    @RequestMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized/unauthorized";
    }

    /**
     * 欢迎页面
     * @return
     */
    @RequestMapping("/welcomePage")
    public String welcomePage() {
        return "welcomePage/welcomePage";
    }

    //主页面
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("home/index");
        return view;
    }

    @Operation(name = "微信人脸信息采集")
    @RequestMapping("/WxIndex")
    public ModelAndView WxIndex(HttpServletRequest request,HttpSession session) {
        ModelAndView view = new ModelAndView();
        // 获取地址中的code
        String code = request.getParameter("code");
        String codes=map.get("code");
        if(StringUtils.isBlank(codes)){
            map.put("code",code);
            Map params = new HashMap();
            params.put( "secret", FileUtils.getProperties("/application.properties", "secret") );
            params.put( "appid", FileUtils.getProperties("/application.properties", "appId") );
            params.put( "grant_type", "authorization_code" );
            params.put( "code", code );
            String result = HttpGetUtil.httpRequestToString(
                    "https://api.weixin.qq.com/sns/oauth2/access_token", params );
            JSONObject jsonObject = JSONObject.fromObject( result );
            String openid = jsonObject.get( "openid" ).toString();
            session.setAttribute("openid",openid);
            // 根据openid查询是否已经录入过信息
            PersonTable personTable = personTableService.selectPersonTableByopenId( openid );
            if (personTable != null) {

                view= dataDictionaryCommon();
                view.addObject("openid",openid);
                view.setViewName( "dataCollection/WxpersonCollection" );
            } else {
                view.addObject("openid",openid);
                view.setViewName( "dataCollection/WxIndex" );
            }
        }else {
            if(codes.equals(code)){
                String openid=(String) session.getAttribute("openid");
                PersonTable personTable = personTableService.selectPersonTableByopenId( openid );
                if (personTable != null) {
                    view= dataDictionaryCommon();
                    view.addObject("openid",openid);
                    view.setViewName( "dataCollection/WxpersonCollection" );
                } else {
                    view.addObject("openid",openid);
                    view.setViewName( "dataCollection/WxIndex" );
                }
            }else {
                map.put("code",code);
                Map params = new HashMap();
                params.put( "secret", FileUtils.getProperties("/application.properties", "secret") );
                params.put( "appid", FileUtils.getProperties("/application.properties", "appId") );
                params.put( "grant_type", "authorization_code" );
                params.put( "code", code );
                String result = HttpGetUtil.httpRequestToString(
                        "https://api.weixin.qq.com/sns/oauth2/access_token", params );
                JSONObject jsonObject = JSONObject.fromObject( result );
                String openid = jsonObject.get( "openid" ).toString();
                session.setAttribute("openid",openid);
                // 根据openid查询是否已经录入过信息
                PersonTable personTable = personTableService.selectPersonTableByopenId( openid );
                if (personTable != null) {

                    view= dataDictionaryCommon();
                    view.addObject("openid",openid);
                    view.setViewName( "dataCollection/WxpersonCollection" );
                } else {
                    view.addObject("openid",openid);
                    view.setViewName( "dataCollection/WxIndex" );
                }
            }
        }

        return view;
    }

    /**
     * 人脸信息采集管理
     *
     * @return
     */
    @Operation(name = "人脸信息采集管理")
    @RequestMapping("/personCollection")
    public ModelAndView dataCollection() {
        ModelAndView view = new ModelAndView();
        //字典类
        view= dataDictionaryCommon();
        view.setViewName( "dataCollection/personCollection" );
        return view;
    }

    /**
     * 园区信息管理
     *
     * @return
     */
    @Operation(name = "园区信息管理")
    @RequestMapping("/basePark")
    public ModelAndView basePark() {
        ModelAndView view = new ModelAndView();
        //通用方法
        view= dataDictionaryCommon();
        view.setViewName( "base/basePark" );
        return view;
    }

    /**
     * 组织机构管理
     *
     * @return
     */
    @Operation(name = "组织机构管理")
    @RequestMapping("/baseOrganizition")
    public ModelAndView baseOrganizition() {
        ModelAndView view = new ModelAndView();
        //通用方法
        view= dataDictionaryCommon();
        view.setViewName( "base/baseOrganizition" );
        return view;
    }
    /**
     * H5人脸信息采集管理
     *
     * @return
     */
    @Operation(name = "H5人脸信息采集管理")
    @RequestMapping("/H5personCollection")
    public ModelAndView H5dataCollection() {
        ModelAndView view = new ModelAndView();
        //字典类
        view= dataDictionaryCommon();
        view.setViewName( "dataCollection/H5personCollection" );
        return view;
    }

    // 获取发布时间
    @RequestMapping("/getFabuTime")
    @ResponseBody
    public String getFabuTime() {
        return FileUtils.getProperties( "/application.properties", "fabuTime" ).toString();
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
        List<DataDictionary> parkTypeList = dataDictionaryService.setlectDataDictionaryList(Constant.PARKTYPE);
        view.addObject( "parkTypeList", parkTypeList );
        //查询园区信息
        List<BasePark> parkList = baseParkManagementMapper.queryBasePark();
        view.addObject( "parkList", parkList );
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

    /**
     * 微信授权
     * @param wxConfig
     * @return 微信授权码
     * @throws FileNotFoundException
     */
    @ResponseBody
    @RequestMapping(value = "/{wxConfig}")
    public String as(@PathVariable String wxConfig) throws FileNotFoundException {
        if (StringUtils.isNotBlank(Config.getPhotoUrl("wxConfig")) && Config.getPhotoUrl("wxConfig").equals(wxConfig)){
            File file = new File(ResourceUtils.getURL("classpath:static").getPath().replace("%20", " ").replace('/', '\\') + "/" + wxConfig);
            BufferedReader reader = null;
            StringBuffer sbf = new StringBuffer();
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempStr;
                while ((tempStr = reader.readLine()) != null) {
                    sbf.append(tempStr);
                }
                reader.close();
                return sbf.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return sbf.toString();
        }
        return null;
    }
}
