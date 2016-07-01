package llc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SessionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by llc on 16/6/30.
 */
@ServerEndpoint("/websocket")
public class WebsocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketServer.class);
    private Session session;
    private static LogServer logServer;
    static {
        logServer = new LogServer();
            Thread th = new Thread(new Runnable() {
                public void run() {
                    try {
                        logServer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException te){
                        te.printStackTrace();
                    }
                }
            });
        th.start();

    }
    /**
     * 打开连接时触发
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        logger.info("Websocket Start Connecting...");
        SessionUtils.add(this);
    }
    /**
     * 收到客户端消息时触发
     * @param message
     * @return
     */
    @OnMessage
    public String onMessage(String message) {
        return "Got your message ("+ message +").Thanks !";
    }

    /**
     * 异常时触发
     * @param session
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        logger.error("Websocket Connection Exception");
        logger.error(throwable.getMessage(), throwable);
        SessionUtils.remove(this);
    }

    /**
     * 关闭连接时触发
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        logger.info("Websocket Close Connection.");
        SessionUtils.remove(this);
    }

    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

}
