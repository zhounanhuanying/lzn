package com.bfdb.controller;

import com.bfdb.entity.LogBook;
import com.bfdb.service.LogBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统日志
 */
@RestController
@RequestMapping("logBook")
public class LogBookController {

    @Autowired
   private LogBookService logBookService;
    /**
     * 查询日志信息
     * */
    @RequestMapping(value = "/dataAll", method = RequestMethod.GET)
    public Map<String,Object> dataAll(HttpServletRequest request){
        Map<String,Object> maps =new HashMap<>();
        Integer statusNub=0;
        String statusMessage="success";
        //获取查询条件信息
        String createTime = request.getParameter("createTime");
        String logType = request.getParameter("logType");
        //获取页码信息
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        Integer page = (Integer.parseInt(request.getParameter("page"))-1)*limit;
          //判空
        if((createTime!=null&&createTime!="") || (logType!=null&&logType!="")){
            List<LogBook> logBooksByCondtion =null;
            int totalCount = 0;
            if(logType!=null&&logType!=""){
                //根据级别查询日志条数
               totalCount = logBookService.dataCount(createTime,logType);
               //根据级别查询日志信息
               logBooksByCondtion = logBookService.logSearch(createTime, logType,page,limit);
            }else{
                //根据时间查询日志条数
                totalCount = logBookService.dataCountByCreatTime(createTime);
                //根据时间查询日志信息
                logBooksByCondtion = logBookService.logSearchByCreatTime(createTime,page,limit);
            }
            maps.put("code",statusNub);
            maps.put("msg",statusMessage);
            maps.put("count",totalCount);
            maps.put("data",logBooksByCondtion);
        }else if((createTime==null||createTime=="") && (logType==null||logType=="")){
            //查询全部日志条数
            int totalCount = logBookService.dataCount2();
            //查询全部日志信息
            List<LogBook> logBooks = logBookService.dataAll(page,limit);
            maps.put("code",statusNub);
            maps.put("msg",statusMessage);
            maps.put("count",totalCount);
            maps.put("data",logBooks);
        }

       return maps;
    }

}
