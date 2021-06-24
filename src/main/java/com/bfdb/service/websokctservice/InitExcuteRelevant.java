package com.bfdb.service.websokctservice;

import com.bfdb.entity.websocket.ConfigurationFileInject;
import com.bfdb.entity.websocket.DrivePropertiesBean;
import com.bfdb.mapper.SubscriptionMapper;
import com.bfdb.service.PersonDataOperationTableService;
import com.bfdb.service.PersonFaceInfomationTableService;
import com.bfdb.service.PersonTableService;
import com.bfdb.untils.Config;
import com.bfdb.untils.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 初始化保活任务
 */
@Component
public class InitExcuteRelevant implements InitializingBean {

    @Autowired
    private ConfigurationFileInject configurationFileInject;
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private PersonTableService personTableService;

    @Autowired
    private PersonFaceInfomationTableService personFaceInfomationTableService;

    @Autowired
    private PersonDataOperationTableService personDataOperationTableService;


    /**
     * InitializingBean 接口中的afterPropertiesSet方法用来在设置完所有bean属性后调用
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println( "-----------------开始线程-------------------------" );

        File file  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
        file.setExecutable(true);
        if (!file.exists()){
            file.createNewFile();
        }
//        File file1  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
        Config jdbcCg = Config.getInstance( file.getPath() );
        if (jdbcCg.getValue("alarm.subscriptionInitTime") == null|| jdbcCg.getValue("alarm.subscriptionInitTime") == ""){
            jdbcCg.setProperty("alarm.subscriptionInitTime", FileUtils.getProperties( "/verificationTerminal.properties", "alarm.subscriptionInitTime" ));
        }
        if (jdbcCg.getValue("alarm.subscriptionRefreshTime") == null|| jdbcCg.getValue("alarm.subscriptionRefreshTime") == "") {
            jdbcCg.setProperty("alarm.subscriptionRefreshTime", FileUtils.getProperties("/verificationTerminal.properties", "alarm.subscriptionRefreshTime"));
        }
        if (jdbcCg.getValue("alarm.updateSubscriptionTime") == null|| jdbcCg.getValue("alarm.updateSubscriptionTime") == "") {
            jdbcCg.setProperty("alarm.updateSubscriptionTime", FileUtils.getProperties("/verificationTerminal.properties", "alarm.updateSubscriptionTime"));
        }
        if (jdbcCg.getValue("alarm.equipment.addressType") == null|| jdbcCg.getValue("alarm.equipment.addressType") == "") {
            jdbcCg.setProperty("alarm.equipment.addressType", FileUtils.getProperties("/verificationTerminal.properties", "alarm.equipment.addressType"));
        }
        if (jdbcCg.getValue("alarm.equipment.ip") == null|| jdbcCg.getValue("alarm.equipment.ip") == "") {
            jdbcCg.setProperty("alarm.equipment.ip", FileUtils.getProperties("/verificationTerminal.properties", "alarm.equipment.ip"));
        }
        if (jdbcCg.getValue("alarm.equipment.port") == null|| jdbcCg.getValue("alarm.equipment.port") == "") {
            jdbcCg.setProperty("alarm.equipment.port", FileUtils.getProperties("/verificationTerminal.properties", "alarm.equipment.port"));
        }
        if (jdbcCg.getValue("alarm.subscription.type") == null|| jdbcCg.getValue("alarm.subscription.type") == "") {
            jdbcCg.setProperty("alarm.subscription.type", FileUtils.getProperties("/verificationTerminal.properties", "alarm.subscription.type"));
        }
        if (jdbcCg.getValue("alarm.subscribePersonCondition.libIDNum") == null|| jdbcCg.getValue("alarm.subscribePersonCondition.libIDNum") == "") {
            jdbcCg.setProperty("alarm.subscribePersonCondition.libIDNum", FileUtils.getProperties("/verificationTerminal.properties", "alarm.subscribePersonCondition.libIDNum"));
        }
        if (jdbcCg.getValue("alarm.serverSocket.port") == null|| jdbcCg.getValue("alarm.serverSocket.port") == "") {
            jdbcCg.setProperty("alarm.serverSocket.port", FileUtils.getProperties("/verificationTerminal.properties", "alarm.serverSocket.port"));
        }
        if (jdbcCg.getValue("allUrl.urlPre") == null|| jdbcCg.getValue("allUrl.urlPre") == "") {
            jdbcCg.setProperty("allUrl.urlPre", FileUtils.getProperties("/verificationTerminal.properties", "allUrl.urlPre"));
        }
        if (jdbcCg.getValue("alarm.subscriptionUrlAft") == null|| jdbcCg.getValue("alarm.subscriptionUrlAft") == "") {
            jdbcCg.setProperty("alarm.subscriptionUrlAft", FileUtils.getProperties("/verificationTerminal.properties", "alarm.subscriptionUrlAft"));
        }
        if (jdbcCg.getValue("allUrl.resultCode") == null|| jdbcCg.getValue("allUrl.resultCode") == "") {
            jdbcCg.setProperty("allUrl.resultCode", FileUtils.getProperties("/verificationTerminal.properties", "allUrl.resultCode"));
        }
        if (jdbcCg.getValue("allUrl.passTime") == null|| jdbcCg.getValue("allUrl.passTime") == "") {
            jdbcCg.setProperty("allUrl.passTime", FileUtils.getProperties("/verificationTerminal.properties", "allUrl.passTime"));
        }
        if (jdbcCg.getValue("allUrl.showInfoNum") == null|| jdbcCg.getValue("allUrl.showInfoNum") == "") {
            jdbcCg.setProperty("allUrl.showInfoNum", FileUtils.getProperties("/verificationTerminal.properties", "allUrl.showInfoNum"));
        }
        if (jdbcCg.getValue("allUrl.gUIShowInfoUrl") == null|| jdbcCg.getValue("allUrl.gUIShowInfoUrl") == "") {
            jdbcCg.setProperty("allUrl.gUIShowInfoUrl", FileUtils.getProperties("/verificationTerminal.properties", "allUrl.gUIShowInfoUrl"));
        }
        if (jdbcCg.getValue("allUrl.showInfoUrl") == null|| jdbcCg.getValue("allUrl.showInfoUrl") == "") {
            jdbcCg.setProperty("allUrl.showInfoUrl", FileUtils.getProperties("/verificationTerminal.properties", "allUrl.showInfoUrl"));
        }
        if (jdbcCg.getValue("filePath") == null|| jdbcCg.getValue("filePath") == "") {
            jdbcCg.setProperty("filePath", FileUtils.getProperties("/application.properties", "filePath"));
        }
        if (jdbcCg.getValue("photoURL") == null|| jdbcCg.getValue("photoURL") == "") {
            jdbcCg.setProperty("photoURL", FileUtils.getProperties("/application.properties", "photoURL"));
        }
        if (jdbcCg.getValue("excelNUmber") == null|| jdbcCg.getValue("excelNUmber") == "") {
            jdbcCg.setProperty("excelNUmber", FileUtils.getProperties("/application.properties", "excelNUmber"));
        }


        DrivePropertiesBean drivePropertiesBean = new DrivePropertiesBean();
        //http
        drivePropertiesBean.setSubscriptionUrlPre(Config.getPhotoUrl("allUrl.urlPre"));
        //门禁记录推送的订阅接口
        drivePropertiesBean.setSubscriptionUrlAft( Config.getPhotoUrl("alarm.subscriptionUrlAft") );
        //订阅刷新时输入的有效时间
        drivePropertiesBean.setSubscriptionRefreshTime( Config.getPhotoUrl("alarm.subscriptionRefreshTime") );
        //订阅间隔时间
        drivePropertiesBean.setUpdateSubscriptionTime( Config.getPhotoUrl("alarm.updateSubscriptionTime") );
        //接收服务端口号
        drivePropertiesBean.setServerSocketPort( Config.getPhotoUrl("alarm.serverSocket.port") );
        //数据接收推送的服务器地址类型
        drivePropertiesBean.setAddressType( Config.getPhotoUrl("alarm.equipment.addressType") );
        //订阅类型
        drivePropertiesBean.setSubscriptionType( Config.getPhotoUrl("alarm.subscription.type") );
        //初始化订阅时间
        drivePropertiesBean.setSubscriptionInitTime( Config.getPhotoUrl("alarm.subscriptionInitTime") );
        //订阅的库id数目，此为最大
        drivePropertiesBean.setSubscribePersonConditionLibIDNum( Config.getPhotoUrl("alarm.subscribePersonCondition.libIDNum") );
        //订阅传入的端口号
        drivePropertiesBean.setPort( Config.getPhotoUrl("alarm.equipment.port") );
        //订阅传入的id
        drivePropertiesBean.setEquipmentIp( Config.getPhotoUrl("alarm.equipment.ip") );
        //结果码（可控制开门）0:不开门， 1:开门
        drivePropertiesBean.setResultCode( Config.getPhotoUrl("allUrl.resultCode") );
        //经过时刻：范围[0, 18] 终端上报过人记录中的时间
        drivePropertiesBean.setPassTime( Config.getPhotoUrl("allUrl.passTime") );
        //人机显示信息行数最大为2，当前最多显示两行
        drivePropertiesBean.setShowInfoNum( Config.getPhotoUrl("allUrl.showInfoNum") );
        //请求路径
        drivePropertiesBean.setgUIShowInfoUrl( Config.getPhotoUrl("allUrl.gUIShowInfoUrl") );
        //设备ip及端口号
        drivePropertiesBean.setShowInfoUrl(Config.getPhotoUrl("allUrl.showInfoUrl"));
        StartThread s = new StartThread( drivePropertiesBean, subscriptionMapper, personTableService,
                personFaceInfomationTableService,personDataOperationTableService);
        s.setDaemon( true );// 设置线程为后台线程，tomcat不会被hold,启动后依然一直监听。
        s.start();
    }

}
