package utils;

import llc.WebsocketServer;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 功能说明：用来存储业务定义的sessionId和连接的对应关系
 * 利用业务逻辑中组装的sessionId获取有效连接后进行后续操作
 * 作者：linchuan
 * Created by llc on 16/6/30.
 */
public class SessionUtils {

    private static CopyOnWriteArraySet<WebsocketServer> webSocketSet = new CopyOnWriteArraySet<WebsocketServer>();

    public static void add(WebsocketServer websocket){
        webSocketSet.add(websocket);
    }

    public static void remove(WebsocketServer websocket){
        webSocketSet.remove(websocket);
    }

    public static CopyOnWriteArraySet<WebsocketServer> getWebSocketSet(){
        return webSocketSet;
    }
}
