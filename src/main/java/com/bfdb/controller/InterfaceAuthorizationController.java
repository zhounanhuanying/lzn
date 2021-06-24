package com.bfdb.controller;


import com.alibaba.fastjson.JSON;
import com.bfdb.config.Constant;
import com.bfdb.entity.DataDictionary;
import com.bfdb.entity.InterfaceAuthorization;
import com.bfdb.entity.User;
import com.bfdb.entity.vo.DataKanDateVo;
import com.bfdb.entity.vo.DataKanbanVo;
import com.bfdb.service.InterfaceAuthorizationService;
import com.bfdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 接口授权管理
 */
@RestController
@RequestMapping("/InterfaceAuthorization")
public class InterfaceAuthorizationController extends AbstractController {

    @Autowired
    private InterfaceAuthorizationService interfaceAuthorizationService;

    @Autowired
    private UserService userService;


    /**
     * 查询接口授权信息
     *
     * @param interfaceExpirationDate //授权时间
     * @return
     */
    @RequestMapping(value = "/interfaceAuthorization/{interfaceExpirationDate}", method = RequestMethod.GET)
    public Map<String, Object> queryInterfaceAuthorizationList(@PathVariable("interfaceExpirationDate") String interfaceExpirationDate) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);
        //返回结果集
        List<InterfaceAuthorization> interfaceAuthorizationList = null;

        try {
            interfaceAuthorizationList = interfaceAuthorizationService.queryInterfaceAuthorizationList(interfaceExpirationDate);

            if (interfaceAuthorizationList.size() > 0) {
                resultMap.put("interfaceAuthorizationList", interfaceAuthorizationList);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
            }

        } catch (Exception ex) {
            logger.error("接口授权查询失败" + ex.getMessage());
        }
        return resultMap;
    }

    /*
     查看接口调用信息 按时间查询 、授权接口、status
     */
    @RequestMapping(value = "/interfaceAuthorizationList", method = RequestMethod.GET)
    public Map<String, Object> GetinterfaceAuthorizationList(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Integer statusNub=0;
        String statusMessage="success";

        String callTime = request.getParameter("callTime");
        String interfaceName = request.getParameter("interfaceName");
        Integer status=0;
        if(request.getParameter("status")!="" && request.getParameter("status")!=null){
           status=Integer.parseInt(request.getParameter("status"));
        }
        //获取页码信息
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        Integer page = (Integer.parseInt(request.getParameter("page"))-1)*limit;

        Map<String, Object> parmMap = new LinkedHashMap<>();
        parmMap.put("limit", limit);
        parmMap.put("page", page);
        parmMap.put("callTime", callTime);
        parmMap.put("callStatus",status);
        parmMap.put("interfaceName",interfaceName);

        List<InterfaceAuthorization> interfaceAuthorizationList = interfaceAuthorizationService.getInterfaceAuthorizationList(parmMap);
        int totalCount=interfaceAuthorizationService.getInterfaceAuthorizationListTotal(parmMap);
        if (interfaceAuthorizationList.size() > 0) {
            for(InterfaceAuthorization interfaceAuthorization:interfaceAuthorizationList){
                //根据userid查询对应的姓名
                User user=userService.findByUserID(interfaceAuthorization.getUserId());
                if(user!=null){
                    interfaceAuthorization.setUserName(user.getUserName());
                }
                // 回显调用状态
                if(interfaceAuthorization.getCallStatus()==2){
                    interfaceAuthorization.setCallStatuss("异常");
                }else if(interfaceAuthorization.getCallStatus()==1){
                    interfaceAuthorization.setCallStatuss("成功");
                }
            }
            map.put( "data", interfaceAuthorizationList );
            map.put( "count", totalCount );
        }
        map.put( "code", statusNub );
        map.put( "msg", statusMessage );
        return map;
    }



    /**
     * 删除接口授权信息
     *
     * @param id //授权id
     * @return
     */
    @RequestMapping(value = "/interfaceAuthorization/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteInterfaceAuthorization(@PathVariable("id") Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);
        try {
            int i = interfaceAuthorizationService.deleteInterfaceAuthorization(id);
            if (i != 0) {
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
            }
        } catch (Exception ex) {
            logger.error("删除接口授权信息失败" + ex.getMessage());
        }
        return resultMap;
    }

    /**
     * 修改接口授权信息
     *
     * @param interfaceAuthorization
     * @return
     */
    @RequestMapping(value = "/interfaceAuthorization", method = RequestMethod.PUT)
    public Map<String, Object> updateInterfaceAuthorization(@RequestBody InterfaceAuthorization interfaceAuthorization) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);

        try {
            int i = interfaceAuthorizationService.updateInterfaceAuthorization(interfaceAuthorization);

            if (i != 0) {
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
            }
        } catch (Exception ex) {
            logger.error("修改接口授权信息失败" + ex.getMessage());
        }
        return resultMap;
    }

    /**
     * 新增接口授权信息
     *
     * @param authorization
     * @return
     */
    @RequestMapping(value = "/interfaceAuthorization/", method = RequestMethod.POST)
    public Map<String, Object> insertInterfaceAuthorization(@RequestBody InterfaceAuthorization authorization) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Constant.RESPONSE_FAIL_CODE);
//        //获取当前登录人的信息
//        Subject subject = SecurityUtils.getSubject();
//        User user = (User) subject.getPrincipal();

        try {
           int   i = interfaceAuthorizationService.insertInterfaceAuthorization(authorization);
            if (i != 0) {
                resultMap.put("code", Constant.RESPONSE_SUCCESS_CODE);
                resultMap.put("description", Constant.RESPONSE_SUCCESS_DESCRIPTION);
            }
        } catch (Exception ex) {
            logger.error("新增接口授权信息失败" + ex.getMessage());
        }
        return resultMap;
    }

    /*
    * 根据指定日期 查询该日期前一周的接口调用次数
    */
    @RequestMapping(value = "/getCallNumberByCallTime",method = RequestMethod.GET)
    public Map<String,Object> getCallNumberByCallTime(){
        Map<String,Object> map=new HashMap<>();
            // 通过调用日期查询
            List<DataKanDateVo> list=interfaceAuthorizationService.getCallNumberByCallTime(getDate());
            map.put("code","success");
            map.put("list",list);
        return map;
    }

    /*
     * 根据指定日期和接口调用状态 查询该日期前一周的接口调用次数
     */
    @RequestMapping(value = "/getCallNumberByCallStatus",method = RequestMethod.GET)
    public Map<String,Object> getCallNumberByCallTimeAndCallStatus(@RequestParam("callStatus") Integer callStatus){
        Map<String,Object> map=new HashMap<>();
        // 判断日期格式
            // 通过调用日期和调用状态查询
            Map<String,Object> parmMap=new HashMap<>();
            parmMap.put("callTime",getDate());
            parmMap.put("callStatus",callStatus);
            Integer callNumber=interfaceAuthorizationService.getCallNumberByCallTimeAndCallStatus(parmMap);
            map.put("code","success");
            map.put("callNumber",callNumber);
        return map;
    }
    /*
     * 根据指定日期和用户姓名 查询该日期前一周的接口调用次数
     */
    @RequestMapping(value = "/getCallNumberByUserName",method = RequestMethod.GET)
    public Map<String,Object> getCallNumberByCallTimeAndUserName(@RequestParam("userName") String userName){
        Map<String,Object> map=new HashMap<>();
        // 通过用户名称查询用户id
        User user = userService.findByName(userName);
        if(user!=null) {
            // 判断日期格式
                Map<String, Object> parmMap = new HashMap<>();
                parmMap.put("callTime", getDate());
                parmMap.put("userId", user.getUserId());
                // 通过调用日期和用户id查询
                Integer callNumber = interfaceAuthorizationService.getCallNumberByCallTimeAndUserId(parmMap);
                map.put("code", "success");
                map.put("callNumber", callNumber);
            } else {
                map.put("code", "请检查日期格式！");
            }
        return map;
    }

    /*
    *  根据调用日期查询该日期前一周的接口调用次数 并对用户的调用次数分组排序
    * */
    @RequestMapping(value = "/getCallNumberAndUserName",method = RequestMethod.GET)
    public Map<String,Object> getCallNumberAndUserName() {
        Map<String, Object> map = new HashMap<>();
            // 通过调用日期查询并按照用户分组
            List<InterfaceAuthorization> list= interfaceAuthorizationService.getCallNumberAndUserName(getDate());
            Integer callNumber =interfaceAuthorizationService.getAllCallNumber();
            map.put("allNumber",callNumber);
            // 定义一个存储存在的用户的接口调用次数集合
            List<InterfaceAuthorization> interfaceAuthorizationList=new ArrayList<>();
            if(list.size()>0){
                for(InterfaceAuthorization interfaceAuthorization:list){
                    // 根据用户id查询用户信息
                    User user=userService.findByUserID(interfaceAuthorization.getUserId());
                    // 判断该用户是否存在
                    if(user!=null){
                        interfaceAuthorization.setUserName(user.getUserName());
                        interfaceAuthorizationList.add(interfaceAuthorization);
                    }
                }
                if(interfaceAuthorizationList.size()<=5){
                    map.put("list", interfaceAuthorizationList);
                }else {
                    map.put("list", interfaceAuthorizationList.subList(0,5));
                }
            }
        return map;
    }

    /*
     *  根据调用日期查询该日期前一周的接口调用次数 并根据调用状态分组排序
     * */
    @RequestMapping(value = "/getCallNumberAndCallStatus",method = RequestMethod.GET)
    public Map<String,Object> getCallNumberAndCallStatus() {
        Map<String, Object> map = new HashMap<>();
            // 通过调用日期查询并按照调用状态分组
            List<InterfaceAuthorization> list= interfaceAuthorizationService.getCallNumberAndCallStatus(getDate());
            if(list.size()>0){
                for(InterfaceAuthorization interfaceAuthorization:list){
                    if(interfaceAuthorization.getCallStatus()==1){
                        interfaceAuthorization.setCallStatuss("成功");
                    }else if(interfaceAuthorization.getCallStatus()==2){
                        interfaceAuthorization.setCallStatuss("异常");
                    }
                }
                map.put("code", "success");
                map.put("list", list);
            }
        return map;
    }

    /*
    * 接口调用总次数*/
    @RequestMapping(value = "/getAllCallNumber",method = RequestMethod.GET)
    public Map<String,Object> getAllCallNumber(){
        Map<String, Object> map = new HashMap<>();
        Integer callNumber =interfaceAuthorizationService.getAllCallNumber();
        if(callNumber!=null){
            char arr[]=(callNumber).toString().toCharArray();
            for(int i = 0;i<arr.length/2;i++){
                char temp;
                temp = arr[i];
                arr[i] = arr[arr.length-i-1] ;
                arr[arr.length-i-1] = temp;
            }
            map.put("callNumber",arr);
            map.put("time",getDate());
        }else {
            map.put("callNumber",0);
        }
        return map;
    }

    /*
    * 增、删、改、查总调用次数*/
    @RequestMapping(value = "/getCRUDAllCallNumber",method = RequestMethod.GET)
    public Map<String,Object> getCRUDAllCallNumber(){
        Map<String, Object> map = new HashMap<>();
        // 增
        Integer insertCallNumber=0;
        // 删
        Integer deleteCallNumber=0;
        // 改
        Integer updateCallNumber=0;
        // 查
        Integer selectCallNumber=0;
        // 总次数
        Integer callNumber =interfaceAuthorizationService.getAllCallNumber();

        Integer insert =interfaceAuthorizationService.getCRUDAllCallNumber("新增");
        Integer delete =interfaceAuthorizationService.getCRUDAllCallNumber("删除");
        Integer update =interfaceAuthorizationService.getCRUDAllCallNumber("更新人员");
        Integer select =interfaceAuthorizationService.getCRUDAllCallNumber("查询");
        if(insert!=null){
            insertCallNumber=insert;
        }
        if(delete!=null){
            deleteCallNumber=delete;
        }
        if(update!=null){
            updateCallNumber=update;
        }
        if(select!=null){
            selectCallNumber=select;
        }
        map.put("callNumber",callNumber);
        map.put("insertCallNumber",insertCallNumber);
        map.put("deleteCallNumber",deleteCallNumber);
        map.put("updateCallNumber",updateCallNumber);
        map.put("selectCallNumber",selectCallNumber);
        return map;
    }

    /*
    人员采集总人数
    */
    @RequestMapping(value = "/getAllPersonTableTotal",method = RequestMethod.GET)
    public Map<String,Object> getAllPersonTableTotal(){
        Map<String, Object> map = new HashMap<>();
        // 查询人员条数及人员采集总个数
        Integer allPerson=interfaceAuthorizationService.getAllPersonTableTotal();
        if(allPerson<999999){
            char arr[]=(allPerson).toString().toCharArray();
            for(int i = 0;i<arr.length/2;i++){
                char temp;
                temp = arr[i];
                arr[i] = arr[arr.length-i-1] ;
                arr[arr.length-i-1] = temp;
            }
            map.put("allPerson",arr);
        }else {
            Integer lage=999999;
            char brr[]=(allPerson).toString().toCharArray();
            map.put("allPerson",brr);
        }
            map.put("time",getDate());
        return map;

    }
    /*
    * 人员采集分类 根据数据来源分类统计
    */
    @RequestMapping(value = "/getAllPersonByDataSource",method = RequestMethod.GET)
    public Map<String,Object> getAllPersonByDataSource(){
        Map<String, Object> map = new HashMap<>();
        List<DataKanbanVo> list=new ArrayList<>();
            // H5人脸采集
            Integer H5=interfaceAuthorizationService.getAllPersonByDataSource("1");
            DataKanbanVo dataKanbanVo1=new DataKanbanVo();
            dataKanbanVo1.setName("H5");
            dataKanbanVo1.setValue(H5);
            list.add(dataKanbanVo1);
            // 认证核验终端采集
            Integer Terminal=interfaceAuthorizationService.getAllPersonByDataSource("2");
            DataKanbanVo dataKanbanVo2=new DataKanbanVo();
            dataKanbanVo2.setName("采集终端");
            dataKanbanVo2.setValue(Terminal);
            list.add(dataKanbanVo2);
            // web端
            Integer web=interfaceAuthorizationService.getAllPersonByDataSource("3");
            DataKanbanVo dataKanbanVo3=new DataKanbanVo();
            dataKanbanVo3.setName("Web");
            dataKanbanVo3.setValue(web);
            list.add(dataKanbanVo3);
            // 接口调用
            Integer other=interfaceAuthorizationService.getAllPersonByDataSourceLike("接口调用采集");
            DataKanbanVo dataKanbanVo4=new DataKanbanVo();
            dataKanbanVo4.setName("接口调用采集");
            dataKanbanVo4.setValue(other);
            list.add(dataKanbanVo4);
            map.put("list", list);
            Integer allPerson=interfaceAuthorizationService.getAllPersonTableTotal();
            map.put("allPerson",allPerson);
        return map;
    }


    // 获取系统当前时间
    public String getDate(){
        Date date =new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


}
