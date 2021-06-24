package com.bfdb.untils;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @description: 时间工具类
 * @author: liuzhounan
 * @date: Created in 2019/10/10 11:08
 * @version:
 * @modified By:
 */
public class DateUtils {
    public static final String DATE_FORMART_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param currentDate 创建时间
     * @return
     * @throws ParseException
     */
    public static Date plusDay(int num, Date currentDate) {
        LocalDateTime localDateTime = dateConvertLocalDateTime(currentDate);
        localDateTime = localDateTime.plusDays(num);
        return localDateTimeConvertDate(localDateTime);
    }

    /**
     * @description 根据时间字符串转换成
     * @return
     * @author kejie.huang
     * @date 2019/10/10 11:19
     */
    public static Date dateStrConvertDate(String currentDate, String formateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formateStr);
        LocalDateTime localDateTime = LocalDateTime.parse(currentDate, dateTimeFormatter);
        return localDateTimeConvertDate(localDateTime);
    }

    /**
     * @return
     * @description 根据date转换成localDateTime
     * @author kejie.huang
     * @date 2019/10/14 14:31
     */
    private static LocalDateTime dateConvertLocalDateTime(Date date) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime;
    }

    /**
     * @return
     * @description 方法描述 根据localDateTime转换成date
     * @author kejie.huang
     * @date 2019/10/14 14:31
     */
    public static Date localDateTimeConvertDate(LocalDateTime localDateTime) {
        return Date.from(getZonedDateTimeByLocalDateTime(localDateTime).toInstant());
    }
    /**
     * @description 根据时间转换成时间字符串
     * @return
     * @author kejie.huang
     * @date 2019/10/10 11:19
     */
    public static String formatDateToParse(Date currentDate, String formateStr) {
        LocalDateTime localDateTime = dateConvertLocalDateTime(currentDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formateStr);
        return dateTimeFormatter.format(localDateTime);
    }
    /**
     * @description 根据开始时间，结束时间进行对比
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     * @author kejie.huang
     * @date 2019/10/10 11:16
     */
    public static boolean compareDay(Date startDate, Date endDate) {
        return startDate.before(endDate);
    }

    /**
     * @title DateUtils
     * @Description获取本月第一天
     * @author kejie.huang
     * @Date 2019/10/14 10:52
     * @Copyright 2019-2020
     */
    public static Date getMonthFirstDay() {
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime firstday = date.with(TemporalAdjusters.firstDayOfMonth());
        return localDateTimeConvertDate(firstday);
    }

    /**
     * @title DateUtils
     * @Description获取本月最后一天
     * @author kejie.huang
     * @Date 2019/10/14 10:52
     * @Copyright 2019-2020
     */
    public static Date getMonthLastDay() {
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime lastday = date.with(TemporalAdjusters.lastDayOfMonth());
        return localDateTimeConvertDate(lastday);
    }

    /**
     * @title DateUtils
     * @Description 根据localDateTime转换成ZonedDateTime对象，用于把localDatTime转成Date
     * @author kejie.huang
     * @Date 2019/10/14 11:03
     * @Copyright 2019-2020
     */
    public static ZonedDateTime getZonedDateTimeByLocalDateTime(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime;
    }

    /**
     * @return
     * @description 获得某天最大时间 2019-10-14 23:59:59
     * @author kejie.huang
     * @date 2019/10/14 10:56
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        ;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeConvertDate(endOfDay);
    }

    /**
     * @return
     * @description 获得某天最小时间 2019-10-14 00:00:00
     * @author kejie.huang
     * @date 2019/10/14 10:57
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeConvertDate(startOfDay);
    }
}