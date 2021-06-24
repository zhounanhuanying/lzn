package com.bfdb.untils;

/**
 *从身份证号码中获取出生年月日   工具类
 */
public class BirthdayUtils {

    /**
     * 省份证的正则表达式^(\d{15}|\d{17}[\dx])$
     * @param id    省份证号
     * @return    生日（yyyy-MM-dd）
     */
    public static String extractYearMonthDayOfIdCard(String id) {
        String year = null;
        String month = null;
        String day = null;
        //正则匹配身份证号是否是正确的，15位或者17位数字+数字/x/X
        if (id.matches("^\\d{15}|\\d{17}[\\dxX]$")) {
            year = id.substring(6, 10);
            month = id.substring(10,12);
            day = id.substring(12,14);
        }else {
            System.out.println("身份证号码不匹配！");
            return null;
        }
        return year + "-" + month + "-" + day;
    }

}