package utils;

import llc.WebsocketServer;
import org.apache.log4j.Logger;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 功能说明：用来存储业务定义的sessionId和连接的对应关系
 * 利用业务逻辑中组装的sessionId获取有效连接后进行后续操作
 * 作者：linchuan
 * Created by llc on 16/6/30.
 */
public class SessionUtils {

    private static final Logger logger = Logger.getLogger("DayRollingFile");

    private static CopyOnWriteArraySet<WebsocketServer> complexSocketSet = new CopyOnWriteArraySet<WebsocketServer>();

    private static CopyOnWriteArraySet<WebsocketServer> simpleSocketSet = new CopyOnWriteArraySet<WebsocketServer>();

    public static void changeToComplex(WebsocketServer websocketServer){
        removeSimple(websocketServer);
        addComplex(websocketServer);
    }

    public static void addSimple(WebsocketServer websocket){
        simpleSocketSet.add(websocket);
        logger.info("Added a simple client, now there are "+simpleSocketSet.size()+" clients");
    }

    public static void removeSimple(WebsocketServer websocket){
        if (simpleSocketSet.contains(websocket)){
            simpleSocketSet.remove(websocket);
            logger.info("Deleted a simple client, now there are "+simpleSocketSet.size()+" clients");
        }
    }

    public static void addComplex(WebsocketServer websocketServer){
        complexSocketSet.add(websocketServer);
        logger.info("Added a complex client, now there are "+complexSocketSet.size()+" clients");

    }

    public static void removeComplex(WebsocketServer websocketServer){
        if (complexSocketSet.contains(websocketServer)){
            complexSocketSet.remove(websocketServer);
            logger.info("Deleted a complex client, now there are "+complexSocketSet.size()+" clients");
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
