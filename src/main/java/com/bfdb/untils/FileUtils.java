package com.bfdb.untils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtils {

    /**
     * 获取配置文件的内容
     *
     * @param filePath 文件的地址
     * @param keyWord  key值
     */
    public static String getProperties(String filePath, String keyWord) {
        Properties prop = new Properties();
        String value = null;
        try {
            InputStream inputStream = FileUtils.class.getResourceAsStream(filePath);
            prop.load(inputStream);
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
    /**
     * 获取配置文件中的配置
     *
     * @return
     */
    public static Map<String, String> findProperties() {
        Map<String, String> map = new HashMap<String, String>();
        Properties prop = new Properties();
        try {
            InputStream inStream = FileUtils.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(inStream); /// 加载属性列表
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                map.put(key, prop.getProperty(key));
            }
            inStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return map;
    }

    /**
     * 验证目录是否存在，如果不存在，则创建对应目录。
     *
     * @param filePathName
     */
    public static String makePathFile(String filePathName, String year) {
        File pathFile = new File(filePathName);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        File childFile = new File(pathFile + "\\" + year);
        if (!childFile.exists()) {
            childFile.mkdir();
        }
        return childFile + "\\";
    }


    /*
    * 验证文件是否存在，如果不存在，则创建对应文件夹。
    *
    * */
  public static void mdkirFile(){
      Date date = new Date();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
      String format = simpleDateFormat.format( date );
      String[] split = format.split( "/" );
      String urle= "";
      String dPath = Config.getPhotoUrl("filePath");
      for (int i = 0; i < split.length; i++) {
          urle += split[i] + "/";
          File file = new File( dPath + urle );
          //判断文件夹是否存在
          if (!file.exists() && !file.isDirectory()) {
              //不存在的话,创建文件夹
              file.mkdirs();
          }
      }

  }


}
