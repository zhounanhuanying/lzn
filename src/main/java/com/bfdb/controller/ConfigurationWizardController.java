package com.bfdb.controller;

import com.bfdb.entity.ConfigurationWizard;
import com.bfdb.entity.websocket.DrivePropertiesBean;
import com.bfdb.untils.Config;
import com.bfdb.untils.FileUtils;
import com.bfdb.untils.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;


/**
 * 系统配置
 */
@Controller
@RequestMapping("/configuration")
public class ConfigurationWizardController {
    @ResponseBody
    @RequestMapping(value = "/initConfigurationWhetherPath")
    public Map<String, Object> initConfigurationWhetherPath(HttpServletRequest request) throws IOException {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> kmap = new HashMap<String, Object>();

        //获取配置文件的内容
        File file1  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
        Config jdbcCg = Config.getInstance( file1.getPath() );
        String jdbcURL;
        String jdbcUsername;
        String jdbcPassword;
        if (jdbcCg.getValue("spring.datasource.url") == null|| jdbcCg.getValue("spring.datasource.url") == ""){
            jdbcURL =FileUtils.getProperties( "/application.properties", "spring.datasource.url" );
        }else {
            jdbcURL = jdbcCg.getValue("spring.datasource.url").toString();
        }
        if (jdbcCg.getValue("spring.datasource.username") == null|| jdbcCg.getValue("spring.datasource.username") == ""){
            jdbcUsername = FileUtils.getProperties( "/application.properties", "spring.datasource.username" );
        }else {
            jdbcUsername = jdbcCg.getValue("spring.datasource.username").toString();
        }
        if (jdbcCg.getValue("spring.datasource.password") == null|| jdbcCg.getValue("spring.datasource.password") == ""){
            jdbcPassword =FileUtils.getProperties( "/application.properties", "spring.datasource.password" );
        }else {
            jdbcPassword = jdbcCg.getValue("spring.datasource.password").toString();
        }
        String substringUrl = "";
        if (jdbcURL != null && !"".equals( jdbcURL )) {
            substringUrl = jdbcURL.substring( jdbcURL.indexOf( "//" ) + 2, jdbcURL.indexOf( "?" ) );
        }
        kmap.put( "jdbcURL", substringUrl );
        kmap.put( "jdbcUsername", jdbcUsername );
        kmap.put( "jdbcPassword", jdbcPassword );
        returnMap.put( "data", kmap );
        returnMap.put( "code", 0 );
        return returnMap;
    }


    /*
    * 从配置文件中获取图片回显的ip
    * */
    @ResponseBody
    @RequestMapping(value = "/selectPhotoUrlConfig")
    public Map<String, Object> selectPhotoUrlConfig() {
        Map<String,Object> map=new HashMap<>();
        String photoURL = Config.getPhotoUrl("photoURL");
        if(StringUtils.isNotBlank(photoURL)){
            map.put("code",1);
            map.put("ip",photoURL.substring( photoURL.indexOf( "//" ) + 2, photoURL.lastIndexOf( ":" ) ));
        }
        return map;
    }

    /*
     * 从配置文件中获取订阅的配置
     * */
    @ResponseBody
    @RequestMapping(value = "/selectDingYueConfig")
    public Map<String, Object> selectDingYueConfig() {
        Map<String,Object> map=new HashMap<>();
        String equipmentIp = Config.getPhotoUrl("alarm.equipment.ip");
        String port = Config.getPhotoUrl("alarm.equipment.port");
        if(StringUtils.isNotBlank(equipmentIp) && StringUtils.isNotBlank(port)){
            map.put("code",1);
            map.put("equipmentIp",equipmentIp);
            map.put("port",port);
        }
        return map;
    }

    /*
     * 从配置文件中获取导出excel表格条数的配置
     * */
    @ResponseBody
    @RequestMapping(value = "/selectExcelNUmber")
    public Map<String, Object> selectExcelNUmber() {
        Map<String,Object> map=new HashMap<>();
        String excelNUmber = Config.getPhotoUrl("excelNUmber");
        if(StringUtils.isNotBlank(excelNUmber)){
            map.put("code",1);
            map.put("excelNUmber",excelNUmber);
        }
        return map;
    }

    /*
     * 配置导出excel表格条数
     * */
    @ResponseBody
    @RequestMapping(value = "/setExcelNUmber")
    public Map<String, Object> setExcelNUmber(@RequestParam("excelNUmber") String excelNUmber) {
        Map<String,Object> map=new HashMap<>();
        File file  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
        Config photoUrl = Config.getInstance( file.getPath() );
        photoUrl.setProperty( "excelNUmber", excelNUmber );
        map.put("code",1);
        return map;
    }



    /*
     * 从配置文件中获取图片存储路径
     * */
    @ResponseBody
    @RequestMapping(value = "/selectImgUrl")
    public Map<String, Object> selectImgUrl() {
        Map<String,Object> map=new HashMap<>();
        String filePath = Config.getPhotoUrl("filePath");
        if(StringUtils.isNotBlank(filePath)){
            map.put("code",1);
            map.put("filePath",filePath);
        }
        return map;
    }

    /*
     * 配置图片存储路径
     * */
    @ResponseBody
    @RequestMapping(value = "/setImgUrl")
    public Map<String, Object> setImgUrl(@RequestParam("filePath") String filePath) {
        Map<String,Object> map=new HashMap<>();
        File file  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
        Config photoUrl = Config.getInstance( file.getPath() );
        photoUrl.setProperty( "filePath", filePath );
        try {
            String os = System.getProperty( "os.name" );
            if (!os.toLowerCase().startsWith( "win" )) {
                restartTomcat();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("code",1);
        return map;
    }

    /*
     * 配置订阅的ip和端口号
     * */
    @ResponseBody
    @RequestMapping(value = "/setDYconfig",method = RequestMethod.PUT)
    public Map<String, Object> setDYconfig(DrivePropertiesBean drivePropertiesBean) {
        Map<String,Object> map=new HashMap<>();
        if(HttpUtil.isIP(drivePropertiesBean.getEquipmentIp())){
            File file  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
            Config photoUrl = Config.getInstance( file.getPath() );
            photoUrl.setProperty( "alarm.equipment.ip", drivePropertiesBean.getEquipmentIp() );
            photoUrl.setProperty( "alarm.equipment.port", drivePropertiesBean.getPort() );
            photoUrl.setProperty( "alarm.serverSocket.port", drivePropertiesBean.getPort() );
            map.put("code",1);
            try {
                String os = System.getProperty( "os.name" );
                if (!os.toLowerCase().startsWith( "win" )) {
                    restartTomcat();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            map.put("code",0);
        }
        return map;
    }

    /*
     * 配置图片回显的ip
     * */
    @ResponseBody
    @RequestMapping(value = "/setPhotoUrlIp")
    public Map<String, Object> setPhotoUrlIp(@RequestParam("ip") String ip) {
        Map<String,Object> map=new HashMap<>();
        if(HttpUtil.isIP(ip)){
            File file  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
            Config photoUrl = Config.getInstance( file.getPath() );
            String url="http://"+ip+":12667/imageEcho/";
            photoUrl.setProperty( "photoURL", url );
            map.put("code",1);
        }else {
            map.put("code",0);
        }
        return map;
    }




    /**
     * 系统初始化配置
     *
     * @param request
     * @param configurationWizard
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/initConfiguration")
    public Map<String, Object> initConfiguration(HttpServletRequest request, ConfigurationWizard configurationWizard) throws IOException {

        Map<String, Object> returnMap = new HashMap<>();

        String jdbcURL1 = configurationWizard.getJdbcURL();
        String jdbcURL2 = "jdbc:mysql://" + jdbcURL1 + "?allowMultiQueries=true&characterEncoding=utf-8";
        String jdbcUsername = configurationWizard.getJdbcUsername();
        String jdbcPassword = configurationWizard.getJdbcPassword();
        File file  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection( jdbcURL2, jdbcUsername, jdbcPassword );
            returnMap.put( "code", 0 );
        } catch (Exception e) {
            returnMap.put( "code", -1 );
            return returnMap;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                System.out.println( "Close Connection error." );
            }
        }
        //修改初始化是否配置
        Config jdbcCg = Config.getInstance( file.getPath() );
        jdbcCg.setProperty( "spring.datasource.url", jdbcURL2 );
        jdbcCg.setProperty( "spring.datasource.username", jdbcUsername );
        jdbcCg.setProperty( "spring.datasource.password", jdbcPassword );
        try {
            String os = System.getProperty( "os.name" );
            if (!os.toLowerCase().startsWith( "win" )) {

                restartTomcat();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;

    }

    /**
     * 测试数据库连接
     *
     * @param request
     * @param configurationWizard
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/testConnectionJDBC")
    public Map<String, Object> testConnectionJDBC(HttpServletRequest request, ConfigurationWizard configurationWizard) throws IOException {
//        ClassPathResource classPathResource = new ClassPathResource("/application.properties");
        Map<String, Object> returnMap = new HashMap<>();

        String jdbcURL1 = configurationWizard.getJdbcURL();
        String jdbcURL2 = "jdbc:mysql://" + jdbcURL1 + "?allowMultiQueries=true";
        String jdbcUsername = configurationWizard.getJdbcUsername();
        String jdbcPassword = configurationWizard.getJdbcPassword();

        Connection conn = null;
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection( jdbcURL2, jdbcUsername, jdbcPassword );
            returnMap.put( "code", 0 );
        } catch (Exception e) {
            returnMap.put( "code", -1 );
            return returnMap;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                System.out.println( "Close Connection error." );
            }
        }
        return returnMap;
    }
    private void restartTomcat() throws IOException {

        //当前目录文件夹
        File directory = new File( "" );
        String binPath = directory.getAbsolutePath();

        //restart.sh脚本文件
        String scriptString = "#!/bin/sh\n" +
                "echo \"**********************环境检测 开始**********************\"\n" +
                "echo\n" +
                "service_path=`ls -lt | find  -name \"faceinfo.jar\"`\n" +
                "echo \">>> 服务路径地址: $service_path\"\n" +
                "service_name=${service_path##*/}\n" +
                "echo \">>> 服务JAR名称: $service_name\"\n" +
                "echo \"**********************环境检测 结束**********************\"\n" +
                "echo\n" +
                " \n" +
                "JAR_NAME=$service_name\n" +
                " \n" +
                " \n" +
                "#使用说明，用来提示输入参数\n" +
                "usage() {\n" +
                "    echo \"Usage: sh 执行脚本.sh [start|stop|restart|status]\"\n" +
                "    exit 1\n" +
                "}\n" +
                " \n" +
                "#检查程序是否在运行\n" +
                "is_exist(){\n" +
                "  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `\n" +
                "  #如果不存在返回1，存在返回0     \n" +
                "  if [ -z \"${pid}\" ]; then\n" +
                "   \techo \">>> 服务 $JAR_NAME 未运行\"\n" +
                "   \treturn 1\n" +
                "  else \n" +
                "  \techo \">>> 服务 $JAR_NAME 运行中\"\n" +
                "    return 0\n" +
                "  \n" +
                "  fi\n" +
                "}\n" +
                " \n" +
                "#启动方法\n" +
                "start(){\n" +
                "  echo \"**********************启动服务 开始**********************\"\n" +
                "  is_exist\n" +
                "  if [ $? -eq \"0\" ]; then \n" +
                "    echo \">>> 服务 ${JAR_NAME} 已经在运行中， PID=${pid} <<<\" \n" +
                "  else\n" +
                "    #nohup java -jar $service_path >/dev/null 2>&1 &\n" +
                "\tnohup java -jar $JAR_NAME > faceinfo.out &\n" +
                "    getPID\n" +
                "\t    if [ $? -eq \"0\" ]; then\n" +
                "\t    \techo \">>> 服务 $JAR_NAME 启动失败 <<<\"  \n" +
                "\t    else\n" +
                "\t    \techo \">>> 服务 $JAR_NAME 启动成功 PID=$! <<<\"\n" +
                "\t    fi\n" +
                "   fi\n" +
                "   echo \"**********************启动服务 完成**********************\"\n" +
                "  }\n" +
                " \n" +
                "#停止方法\n" +
                "stop(){\n" +
                "\techo \"**********************关闭服务 开始**********************\"\n" +
                "  is_exist  \n" +
                "\tif [ $? -eq \"1\" ]; then\n" +
                "\t  echo \">>> 无服务运行中\"\n" +
                "\telse\n" +
                "\t  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `\n" +
                "\t  echo \">>> 服务运行中，PID=$pid . 开始关闭服务.\"\n" +
                "\t  kill -9 $pid\n" +
                "\t  echo \">>> 服务已停止.\"\n" +
                "\tfi\n" +
                "\techo \"**********************关闭服务 完成**********************\"\n" +
                "}\n" +
                " \n" +
                "#输出运行状态\n" +
                "status(){\n" +
                "\techo \"**********************查询服务状态 开始**********************\"\n" +
                "  is_exist\n" +
                "  if [ $? -eq \"0\" ]; then\n" +
                "    echo \">>> 服务 ${JAR_NAME} is running PID is ${pid} <<<\"\n" +
                "  else\n" +
                "    echo \">>> 服务 ${JAR_NAME} is not running <<<\"\n" +
                "  fi\n" +
                "  echo \"**********************查询服务状态 完成**********************\"\n" +
                "}\n" +
                " \n" +
                "#重启\n" +
                "restart(){\n" +
                "\techo \"**********************重启服务 开始**********************\"\n" +
                "  stop\n" +
                "  echo\n" +
                "  start\n" +
                "  echo \"**********************重启服务 完成**********************\"\n" +
                "}\n" +
                "\n" +
                " \n" +
                "getPID() {\n" +
                "\tpid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `\n" +
                "\tif [ -z \"${pid}\" ]; then\n" +
                "   \techo \">>> 服务 $JAR_NAME 未运行\"\n" +
                "   \treturn 1\n" +
                "  else \n" +
                "  \techo \">>> 服务 $JAR_NAME 运行中\"\n" +
                "    return ${pid}\n" +
                "  \n" +
                "  fi\n" +
                "}\n" +
                " \n" +
                "#根据输入参数，选择执行对应方法，不输入则执行使用说明\n" +
                "case \"$1\" in\n" +
                "  \"start\")\n" +
                "    start\n" +
                "    ;;\n" +
                "  \"stop\")\n" +
                "    stop\n" +
                "    ;;\n" +
                "  \"status\")\n" +
                "    status\n" +
                "    ;;\n" +
                "  \"restart\")\n" +
                "    restart\n" +
                "    ;;\n" +
                "  *)\n" +
                "    usage\n" +
                "    ;;\n" +
                "esac\n" +
                "exit 0\n";
        //将restart.sh写到linux文件夹下
        File restart = new File( binPath + "/restart.sh" );
        FileOutputStream stream = new FileOutputStream( restart );
        stream.write( scriptString.getBytes() );
        stream.flush();
        stream.close();
        //修改执行脚本权限
        String urla = File.separator + "usr" + File.separator + "local" + File.separator  + "faceinfo"+File.separator+"restart.sh"+" " +"restart";
        String[] commands = new String[]{"/system/bin/sh", "-c",
                "chmod 755 " + urla};
        Process process = null;
        Process proc = null;
        DataOutputStream dataOutputStream = null;
        restart.setExecutable(true);
        try {

            process = Runtime.getRuntime().exec( "su" );
            dataOutputStream = new DataOutputStream( process.getOutputStream() );
            int length = commands.length;
            for (int i = 0; i < length; i++) {
                dataOutputStream.writeBytes( commands[i] + "\n" );
            }
            dataOutputStream.writeBytes( "exit\n" );
            dataOutputStream.flush();
            dataOutputStream.close();
            process.waitFor();
            //执行脚本，重启服务器
            proc = Runtime.getRuntime().exec( urla);
            proc.waitFor();

        } catch (Exception e) {
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                process.destroy();
                proc.destroy();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 保存系统配置
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/initConfigurationBtn")
    public Map<String, Object> initConfigurationBtn(HttpServletRequest request) {

        Map<String, Object> returnMap = new HashMap<>();

        Map<String, Object> kmap = new HashMap<String, Object>();
        //获取配置文件的内容
        String jdbcURL = FileUtils.getProperties( "/application.properties", "spring.datasource.url" );
        String jdbcUsername = FileUtils.getProperties( "/application.properties", "spring.datasource.username" );
        String jdbcPassword = FileUtils.getProperties( "/application.properties", "spring.datasource.password" );

        String substringUrl = "";
        if (jdbcURL != null && !"".equals( jdbcURL )) {
            substringUrl = jdbcURL.toString().substring( jdbcURL.toString().indexOf( "//" ) + 2, jdbcURL.toString().indexOf( "?" ) );
        }
        kmap.put( "jdbcURL", substringUrl );
        kmap.put( "jdbcUsername", jdbcUsername );
        kmap.put( "jdbcPassword", jdbcPassword );
        returnMap.put( "data", kmap );
        returnMap.put( "code", 0 );
        return returnMap;
    }

}
