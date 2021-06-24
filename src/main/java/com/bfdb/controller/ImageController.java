package com.bfdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * 通用照片回显公共方法类
 */
@Controller
@RequestMapping(value = "/images")
public class ImageController {

    @RequestMapping("/toFindImg")
    public void picToHtml(@RequestParam("imgUrl") String imgUrl, HttpServletRequest request, HttpServletResponse response){
        FileInputStream in;
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            //图片读取路径
            in=new FileInputStream(imgUrl);
            int i=in.available();
            byte[]data=new byte[i];
            in.read(data);
            in.close();
            //写图片
            OutputStream outputStream=new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
