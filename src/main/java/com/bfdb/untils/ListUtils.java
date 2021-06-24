package com.bfdb.untils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/2.
 * liuzhounan
 */
public class ListUtils {

    public static boolean isNotNullAndEmptyList(List<?> list) {
        return list != null && list.size() > 0;
    }

    public static boolean isNullOrEmptyList(List<?> list) {
        return list == null || list.size() == 0;
    }

}
