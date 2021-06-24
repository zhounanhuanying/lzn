package com.bfdb.config;

import com.alibaba.fastjson.JSONObject;
import com.bfdb.entity.websocket.CardInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 前端和后端的交互的websocket
 * 这里@ServerEndpoint(value = "/websocket")的作用相当于端口号
 */
@Component
@ServerEndpoint("/websocket")
public class CustomWebSocket {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的CumWebSocket对象。
     */
    private static CopyOnWriteArraySet<CustomWebSocket> webSocketSet = new CopyOnWriteArraySet<CustomWebSocket>();

    /**
     * 通过session可以给每个WebSocket长连接中的客户端发送数据
     */
    private Session session;
    /**
     * 日志信息
     */
    private Logger log = LoggerFactory.getLogger( CustomWebSocket.class);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("websocket客户端开启连接");
        this.session = session;
        webSocketSet.add(this);
        //添加在线人数
        addOnlineCount();
        log.info("新连接接入,当前在线人数为：" + getOnlineCount());
//        System.out.println(webSocketSet);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("websocket客户端关闭连接");
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        log.info("有连接关闭。当前在线人数为：" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户发送过来的消息为："+message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket出现错误");
        error.printStackTrace();
    }
//    public void sendMessage(CardInfo message) {
//        try {
//            this.session.getBasicRemote().sendText(message);
//            log.info("推送消息成功，消息为：" + message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 减少在线人数
     */
    private void subOnlineCount() {
        CustomWebSocket.onlineCount--;
    }

    /**
     * 添加在线人数
     */
    private void addOnlineCount() {
        CustomWebSocket.onlineCount++;
    }
    /**
     * 当前在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(CardInfo message) throws IOException {
        this.session.getBasicRemote().sendText( JSONObject.toJSONString(message));
    }

    /**
     * 自定义消息
     */
    public static void sendInfo(CardInfo message) throws IOException {
        for (CustomWebSocket productWebSocket : webSocketSet) {
            productWebSocket.sendMessage(message);
        }
    }

}
