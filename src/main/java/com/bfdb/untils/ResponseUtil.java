package com.bfdb.untils;
import com.bfdb.config.Constant;

import com.alibaba.fastjson.JSON;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {

    /**
     * 登录响应
     * @param response
     */
    public static void responseLoginWithJson(HttpServletResponse response,
                                       Object responseObject) {
        //将实体对象转换为JSON Object转换
        response.setHeader(Constant.HEADER_AUTHORIZATION,(String)responseObject);
        try (PrintWriter writer = getWriter(response)) {
            if (writer != null) {
                writer.close();
            }
        }
    }
    /**
     * 接口响应
     * @param response
     */
    public static void responseOutWithJson(HttpServletResponse response,
                                           Object responseObject) throws IOException {
        //让浏览器用utf8来解析返回的数据
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        //告诉servlet用UTF-8转码，而不是用默认的ISO8859
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.append(JSON.toJSONString(responseObject));
        if (writer != null) {
            writer.close();
        }
    }
    private static PrintWriter getWriter(HttpServletResponse response){
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter writer =null;
        try {
            writer = httpServletResponse.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.reset();
        return writer;
    }
}
