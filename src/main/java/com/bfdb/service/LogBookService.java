package com.bfdb.service;


import com.bfdb.entity.LogBook;

import java.util.List;

public interface LogBookService {
    //根据级别查询日志信息
    List<LogBook> logSearch(String creatTime, String logType, Integer page, Integer limit);
    //根据时间查询日志信息
    List<LogBook> logSearchByCreatTime(String creatTime, Integer page, Integer limit);
    //查询全部日志信息
    List<LogBook> dataAll(Integer page, Integer limit);
    //根据级别查询日志条数
    int dataCount(String creatTime, String logType);
    //根据时间查询日志条数
    int dataCountByCreatTime(String creatTime);
    //查询全部日志条数
    int dataCount2();
    int insertLogBook(LogBook logBook);
}
