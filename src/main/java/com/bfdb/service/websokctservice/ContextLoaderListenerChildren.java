package com.bfdb.service.websokctservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bfdb.entity.SysServer;
import com.bfdb.entity.websocket.*;
import com.bfdb.mapper.SubscriptionMapper;
import com.bfdb.untils.HttpResponseResult;
import com.bfdb.untils.HttpUtil;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务会默认创建线程池，项目关闭并不会关闭此线程池，
 * 所以继承ContextLoaderListener类来关闭线程池
 */
public class ContextLoaderListenerChildren extends ContextLoaderListener {
    private static ScheduledExecutorService executorService;

    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized( event );
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //试图停止当前正执行的task，并返回尚未执行的task的list
        executorService.shutdownNow();
        super.contextDestroyed( event );
    }


    public static void ThreadSubscriptionRefresh(DrivePropertiesBean drivePropertiesBean, SubscriptionMapper subscriptionMapper) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        //创建定时任务，定时刷新订阅
        executorService.scheduleAtFixedRate( new Runnable() {
            @Override
            public void run() {
                try {
                    List<LibID> libIDList=null;
                    LibID libID=null;
                    //获取服务器信息
                    List<SysServer> sysServerList = subscriptionMapper.getSysServerList();
                    //判断服务器信息
                    if (!"".equals(sysServerList) && sysServerList != null) {
                        for (SysServer sysServer : sysServerList) {
                            //判断服务器是否可连接
                            boolean hostReachable = HttpUtil.isHostReachable(sysServer.getServerIp(), 3);
                            Integer serverId = sysServer.getServerId();
                            //从数据库获取订阅信息
                            SysSubscriptionAlarm subscriptionAlarm = subscriptionMapper.getSubscription(serverId);
                            //如果能连接
                            if (hostReachable) {
                                String serverIP = sysServer.getServerIp();
                                String serverUsername = sysServer.getServerUsername();
                                String serverPwd = sysServer.getServerPwd();
                                if (subscriptionAlarm != null && !"".equals(subscriptionAlarm)) {
                                    //订阅信息实例化
                                    String subId = subscriptionAlarm.getSubId();
                                    //订阅刷新的有效时间
                                    Duration duration = new Duration();
                                    duration.setDuration( Integer.parseInt( drivePropertiesBean.getSubscriptionRefreshTime() ) );
                                    String durationString = JSON.toJSONString( duration );
                                    // 调用服务器刷新订阅接口
                                    HttpResponseResult httpResponseResult = HttpUtil.doPut( drivePropertiesBean.getSubscriptionUrlPre() +serverIP + drivePropertiesBean.getSubscriptionUrlAft()+subId, durationString, serverUsername, serverPwd );
                                    if (httpResponseResult != null) {
                                        //如刷新失败，则调用接口重新订阅
                                        if (httpResponseResult.getResponseCode() != 0) {
                                            //定义参数
                                            Subscription subscription = new Subscription();
                                            subscription.setAddressType(Integer.parseInt(drivePropertiesBean.getAddressType()));
                                            subscription.setIPAddress(drivePropertiesBean.getEquipmentIp());
                                            subscription.setPort(Integer.parseInt(drivePropertiesBean.getPort()));
                                            subscription.setType(Integer.parseInt(drivePropertiesBean.getSubscriptionType()));
                                            subscription.setDuration(Integer.parseInt( drivePropertiesBean.getSubscriptionInitTime() ) );
                                            subscription.setLibIDNum( Integer.parseInt( drivePropertiesBean.getSubscribePersonConditionLibIDNum() ) );
                                            //定义订阅的库 ID 列表
                                            libIDList=new ArrayList<>();
                                            //LibIDNum 为 0 时，此字段可选
                                            libID=new  LibID();
                                            libID.setId(3);
                                            libIDList.add(libID);
                                            subscription.setLibIDList(libIDList );
                                            //调用服务器订阅接口
                                            httpResponseResult = HttpUtil.doPost(drivePropertiesBean.getSubscriptionUrlPre() + serverIP + drivePropertiesBean.getSubscriptionUrlAft(),
                                                    JSONObject.toJSONString(subscription), serverUsername, serverPwd);
                                            if (httpResponseResult.getResponseCode() == 0) {
                                                //将订阅信息保存至数据库
                                                //获取返回订阅成功后返回的数据
                                                String subscriptionJson = httpResponseResult.getData();
                                                //进行json转换
                                                JSONObject jsonObject = JSONObject.parseObject( subscriptionJson );
                                                //从json字符串中获取返回的ID信息
                                                String jsonObjectString = jsonObject.getString( "ID" );
                                                //组成新的对象 报错到数据库中
                                                SysSubscriptionAlarm sysSubscriptionAlarm = new SysSubscriptionAlarm();
                                                //服务器id值
                                                sysSubscriptionAlarm.setSubscriptionalarmId(subscriptionAlarm.getSubscriptionalarmId());
                                                //订阅成功返回的订阅ID值
                                                sysSubscriptionAlarm.setSubId(jsonObjectString);
                                                //修改操作
                                                subscriptionMapper.updateSubScription(sysSubscriptionAlarm);
                                            }
                                        }
                                        System.out.println("---httpResponseResult----"+httpResponseResult);
                                    }
                                }

                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, Integer.parseInt( drivePropertiesBean.getUpdateSubscriptionTime() ) * 1000, TimeUnit.MILLISECONDS );
        /**
         *   command：执行线程
         initialDelay：初始化延时
         period：两次开始执行最小间隔时间
         unit：计时单位 (5000)：5秒
         */
    }
}
