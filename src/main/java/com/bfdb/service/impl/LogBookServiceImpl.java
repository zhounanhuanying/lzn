package com.bfdb.service.impl;

import com.bfdb.entity.LogBook;
import com.bfdb.mapper.LogBookMapper;
import com.bfdb.service.LogBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogBookServiceImpl implements LogBookService {

    @Autowired
    private LogBookMapper logBookMapper;


    /**
     * 根据级别查询日志信息
     * @param createTime
     * @param logType
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<LogBook> logSearch(String createTime, String logType, Integer page, Integer limit) {
        return logBookMapper.logSearch(createTime, logType,page,limit);
    }

    /**
     * 根据时间查询日志信息
     * @param createTime
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<LogBook> logSearchByCreatTime(String createTime, Integer page, Integer limit) {
        return logBookMapper.logSearchByCreatTime(createTime, page, limit);
    }

    /**
     * 查询全部日志信息
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<LogBook> dataAll(Integer page, Integer limit) {
        return logBookMapper.dataAll(page,limit);
    }

    /**
     * 根据级别查询日志条数
     * @param createTime
     * @param logType
     * @return
     */
    @Override
    public int dataCount(String createTime,String logType) {
        return logBookMapper.dataCount(createTime, logType);
    }

    /**
     * 根据时间查询日志条数
     * @param createTime
     * @return
     */
    @Override
    public int dataCountByCreatTime(String createTime) {
        return logBookMapper.dataCountByCreatTime(createTime);
    }

    /**
     * 查询全部日志条数
     * @return
     */
    @Override
    public int dataCount2() {
        return logBookMapper.dataCount2();
    }

    @Override
    public int insertLogBook(LogBook logBook) {
        return logBookMapper.insertLogBook(logBook);
    }
}
