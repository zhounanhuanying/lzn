package com.bfdb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bfdb.entity.SysServer;
import com.bfdb.entity.websocket.ConfigurationFileInject;
import com.bfdb.entity.websocket.LibID;
import com.bfdb.entity.websocket.Subscription;
import com.bfdb.entity.websocket.SysSubscriptionAlarm;
import com.bfdb.mapper.SubscriptionMapper;
import com.bfdb.service.SubscriptionService;
import com.bfdb.untils.Config;
import com.bfdb.untils.HttpResponseResult;
import com.bfdb.untils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private ConfigurationFileInject configurationFileInject;

    private static final int RESPONSEFAIL = 599;
    private static final int RESPONSESUCCESS = 0;

    /**
     * 订阅告警信息
     *
     * @param sysServer
     * @return
     */
    @Override
    public HttpResponseResult subscriptionAlarm(SysServer sysServer) {
        HttpResponseResult httpResponseResult = new HttpResponseResult();

        if (sysServer != null && !"".equals( sysServer )) {
            List<LibID> libIDList = null;
            LibID libID = null;
            //判断ip地址能否连接
            boolean hostReachable = HttpUtil.isHostReachable( sysServer.getServerIp(), 3000 );
            //判断
            if (hostReachable) {
                Integer serverId = sysServer.getServerId();
                String serverIP = sysServer.getServerIp();
                String clientIP = sysServer.getClientIp();
                String serverUsername = sysServer.getServerUsername();
                String serverPwd = sysServer.getServerPwd();
                //从数据查询查询服务器订阅消息
                SysSubscriptionAlarm subscription1 = subscriptionMapper.getSubscription( serverId );
                //判断订阅信息表是否为空,可以订阅
                if (subscription1 == null) {
                    Subscription subscription = new Subscription();
                    subscription.setAddressType( Integer.parseInt( Config.getPhotoUrl("alarm.equipment.addressType") ) );
                    subscription.setIPAddress( clientIP );
                    subscription.setPort( Integer.parseInt(Config.getPhotoUrl("alarm.equipment.port")) );
                    subscription.setType( Integer.parseInt( Config.getPhotoUrl("alarm.subscription.type") ) );
                    subscription.setDuration( Integer.parseInt( Config.getPhotoUrl("alarm.subscriptionInitTime") ) );
                    subscription.setLibIDNum( Integer.parseInt( Config.getPhotoUrl("alarm.subscribePersonCondition.libIDNum") ) );
                    //定义订阅的库 ID 列表
                    libIDList = new ArrayList<>();
                    //LibIDNum 为 0 时，此字段可选
                    libID = new LibID();
                    libID.setId( 3 );
                    libIDList.add( libID );
                    subscription.setLibIDList( libIDList );
//                System.out.println(JSONObject.toJSON(subscription  ));
                    try {
                        //调用订阅创建接口
                        httpResponseResult = HttpUtil.doPost( Config.getPhotoUrl("allUrl.urlPre") + serverIP + Config.getPhotoUrl("alarm.subscriptionUrlAft"), JSONObject.toJSONString( subscription ), serverUsername, serverPwd );
                        if (httpResponseResult.getResponseCode() == 0) {
                            //订阅成功后返回的数据
                            String subscriptionJson = httpResponseResult.getData();
                            //转成json
                            JSONObject jsonObject = JSONObject.parseObject( subscriptionJson );
                            //从json中获取到订阅的ID
                            String jsonObjectString = jsonObject.getString( "ID" );
                            //组成新的对象 新增到数据库中
                            SysSubscriptionAlarm sysSubscriptionAlarm = new SysSubscriptionAlarm();
                            //服务器id
                            sysSubscriptionAlarm.setServerId( serverId );
                            //订阅成功后返回的id值
                            sysSubscriptionAlarm.setSubId( jsonObjectString );
                            //进行新增操作
                            int insertSubscriptionInt = subscriptionMapper.insertSubscription( sysSubscriptionAlarm );
                            httpResponseResult.setResponseCode( insertSubscriptionInt );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    httpResponseResult.setResponseCode( -1 );
                }
            } else {
                httpResponseResult.setResponseCode( -1 );
            }
        }
        return httpResponseResult;
    }

    /**
     * 取消告警订阅
     *
     * @param sysServer
     * @return
     */
    @Override
    public HttpResponseResult cancelSubscriptionAlarm(SysServer sysServer) {
        HttpResponseResult httpResponseResult = new HttpResponseResult();
        if (sysServer != null && !"".equals( sysServer )) {
            //判断服务器是否可连接
            boolean hostReachable = HttpUtil.isHostReachable( sysServer.getServerIp(), 3000);
            if (hostReachable) {
                Integer serverId = sysServer.getServerId();
                String serverIP = sysServer.getServerIp();
                //String clientIp = sysServer.getClientIp();
                String serverUsername = sysServer.getServerUsername();
                String serverPwd = sysServer.getServerPwd();
                //根据服务器id查询订阅信息表
                SysSubscriptionAlarm subscription = subscriptionMapper.getSubscription( serverId );
                Integer subscriptionAlarmId = subscription.getSubscriptionalarmId();
                String subId = subscription.getSubId();
                try {
                    //调用删除订阅接口
                    httpResponseResult = HttpUtil.doDelete( Config.getPhotoUrl("allUrl.urlPre") + serverIP + Config.getPhotoUrl("alarm.subscriptionUrlAft") + subId, serverUsername, serverPwd );
                    if (httpResponseResult.getResponseCode() == RESPONSESUCCESS || httpResponseResult.getResponseCode() == RESPONSEFAIL) {
                        int deleteSubscriptionInt = subscriptionMapper.deleteSubscription( subscriptionAlarmId );
                        httpResponseResult.setResponseCode( deleteSubscriptionInt );
                        return httpResponseResult;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                httpResponseResult.setResponseCode( -2 );
                return httpResponseResult;
            }
        }
        return httpResponseResult;
    }
}
