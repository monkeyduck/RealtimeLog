package utils;

import llc.WebsocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 功能说明：用来存储业务定义的sessionId和连接的对应关系
 * 利用业务逻辑中组装的sessionId获取有效连接后进行后续操作
 * 作者：linchuan
 * Created by llc on 16/6/30.
 */
public class SessionUtils {

    private static final Logger logger = LoggerFactory.getLogger(SessionUtils.class);

    private static CopyOnWriteArraySet<WebsocketServer> complexSocketSet = new CopyOnWriteArraySet<WebsocketServer>();

    private static CopyOnWriteArraySet<WebsocketServer> simpleSocketSet = new CopyOnWriteArraySet<WebsocketServer>();

    public static void changeToComplex(WebsocketServer websocketServer){
        removeSimple(websocketServer);
        addComplex(websocketServer);
    }

    public static void addSimple(WebsocketServer websocket){
        simpleSocketSet.add(websocket);
    }

    public static void removeSimple(WebsocketServer websocket){
        if (simpleSocketSet.contains(websocket))
            simpleSocketSet.remove(websocket);
        else
            logger.error("No websocketServer registered in simpleSocketSet!Please add it before remove");
    }

    public static void addComplex(WebsocketServer websocketServer){
        complexSocketSet.add(websocketServer);
    }

    public static void removeComplex(WebsocketServer websocketServer){
        if (complexSocketSet.contains(websocketServer)){
            complexSocketSet.remove(websocketServer);
        }
        else{
            logger.error("No websocketServer registered in complexSocketSet!Please add it before remove");
        }
    }

    public static CopyOnWriteArraySet<WebsocketServer> getSimpleWebSocketSet(){
        return simpleSocketSet;
    }

    public static CopyOnWriteArraySet<WebsocketServer> getComplexSocketSet(){
        return complexSocketSet;
    }

    public static void add(WebsocketServer websocketServer){
        addSimple(websocketServer);
    }

    public static void remove(WebsocketServer websocketServer){
        removeSimple(websocketServer);
        removeComplex(websocketServer);
    }
}
