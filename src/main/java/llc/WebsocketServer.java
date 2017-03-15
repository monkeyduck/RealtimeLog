package llc;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import utils.ClientSetting;
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
    private static final Logger logger = Logger.getLogger("DayRollingFile");
    private ClientSetting clientSetting = new ClientSetting();
    private Session session;
    /**
     * 打开连接时触发
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        logger.info("Websocket Start Connecting...");
        SessionUtils.addSimple(this);
    }
    /**
     * 收到客户端消息时触发
     * @param message
     * @return
     */
    @OnMessage
    public String onMessage(String message) {
        JSONObject jsonObject = JSONObject.fromObject(message);
        String id = jsonObject.getString("memberId");
        String module = jsonObject.getString("module");
        if (!id.equals("")) clientSetting.setMember_id(id);
        if (!module.equals("")) clientSetting.setModule(module);

        if (message.contains("complexLog")){
            SessionUtils.removeSimple(this);
            SessionUtils.addComplex(this);
        }
        else if (message.contains("simpleLog")){
            SessionUtils.removeComplex(this);
            SessionUtils.addSimple(this);
        }
        return "Got your message ("+ message +")";
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
    public void onClose(Session session) throws IOException, TimeoutException {
        logger.info("Websocket Close Connection.");
        SessionUtils.remove(this);
    }

    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    public ClientSetting getClientSetting() {
        return this.clientSetting;
    }
}
