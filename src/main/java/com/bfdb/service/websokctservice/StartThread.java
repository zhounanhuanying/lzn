package com.bfdb.service.websokctservice;


import com.bfdb.entity.websocket.DrivePropertiesBean;
import com.bfdb.mapper.SubscriptionMapper;
import com.bfdb.service.PersonDataOperationTableService;
import com.bfdb.service.PersonFaceInfomationTableService;
import com.bfdb.service.PersonTableService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StartThread extends Thread {

    private DrivePropertiesBean drivePropertiesBean;

    private SubscriptionMapper subscriptionMapper;

    private PersonTableService personTableService;

    private PersonFaceInfomationTableService personFaceInfomationTableService;

    private PersonDataOperationTableService personDataOperationTableService;


    public StartThread(DrivePropertiesBean drivePropertiesBean, SubscriptionMapper subscriptionMapper, PersonTableService personTableService,
                       PersonFaceInfomationTableService personFaceInfomationTableService, PersonDataOperationTableService personDataOperationTableService) {
        this.drivePropertiesBean = drivePropertiesBean;
        this.subscriptionMapper = subscriptionMapper;
        this.personTableService = personTableService;
        this.personFaceInfomationTableService = personFaceInfomationTableService;
        this.personDataOperationTableService = personDataOperationTableService;
    }

    public void run() {
        try {
            System.out.println( "--------------开启守护线程，订阅保活--------------------" );
            //开启守护线程，订阅保活
            ContextLoaderListenerChildren.ThreadSubscriptionRefresh( drivePropertiesBean, subscriptionMapper );
            //开启守护线程，建立socket连接，监听端口号，获取数据
            System.out.println( "--------------开启守护线程，建立socket连接，监听端口号，获取数据--------------------" );
            ServerSocket serverSocket = new ServerSocket( Integer.parseInt( drivePropertiesBean.getServerSocketPort() ) );
            // 轮流等待请求
            while (true) {
                // 等待客户端请求,无请求则闲置;有请求到来时,返回一个对该请求的socket连接
                Socket clientSocket = serverSocket.accept();
                // 创建ThreadAlarm对象,通过socket连接通信
                Thread t = new Thread( new ThreadAlarm(drivePropertiesBean, clientSocket, personTableService,
                        personFaceInfomationTableService ,subscriptionMapper,personDataOperationTableService) );
                t.setDaemon( true );
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
