package com.bfdb.mapper;

import com.bfdb.entity.LogBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogBookMapper {

    //根据级别查询日志信息
    List<LogBook>logSearch(@Param("createTime") String createTime, @Param("logType") String logType, @Param("page") Integer page, @Param("limit") Integer limit);
    //根据时间查询日志信息
    List<LogBook> logSearchByCreatTime(@Param("createTime") String createTime, @Param("page") Integer page, @Param("limit") Integer limit);
     //查询全部日志信息
    List<LogBook> dataAll(@Param("page") Integer page, @Param("limit") Integer limit);
    //根据级别查询日志条数
    int dataCount(@Param("createTime") String createTime, @Param("logType") String logType);
   //根据时间查询日志条数
    int dataCountByCreatTime(@Param("createTime") String createTime);
    //查询全部日志条数
    int dataCount2();

    int insertLogBook(LogBook logBook);
}
