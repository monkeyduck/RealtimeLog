package llc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SessionUtils;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 接受log
 * Created by hxx on 5/3/16.
 */
class Receiver {
    static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    /**
     * 消费日志
     * @param slog
     */
    public void handleMessage(String slog) {

        logger.info("receive " + ":{}", slog);
        if (SessionUtils.getSimpleWebSocketSet().size() > 0 ||
                SessionUtils.getComplexSocketSet().size() > 0) {
            try {
                MyLog log = new MyLog(slog);
                if (log.belongsToSimple()) {
                    broadcastSimple(log);
                }
                broadcastComplex(slog);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }


    public void broadcastSimple(MyLog log) throws IOException {
        logger.info("Start to broadcast simple log: " + log.toSimpleLog());
        CopyOnWriteArraySet<WebsocketServer> simplewebSocketSet = SessionUtils.getSimpleWebSocketSet();
        logger.info("simple set size:"+simplewebSocketSet.size());
        for (WebsocketServer websocketServer: simplewebSocketSet){
            String member_id = websocketServer.getClientSetting().getMember_id();
            String module = websocketServer.getClientSetting().getModule();
            boolean flag_mem = true;
            boolean flag_module = true;
            if (!member_id.equals("")){
                if (!log.getMember_id().contains(member_id))
                    flag_mem = false;
            }
            if (!module.equals("") && !module.equals("All")){
                if (!log.getModtrans().contains(module)){
                    flag_module = false;
                }
            }
            if (flag_mem && flag_module)
                websocketServer.sendMessage(log.toSimpleLog());
        }
    }

    public void broadcastComplex(String message) throws IOException{
        CopyOnWriteArraySet<WebsocketServer> webSocketSet = SessionUtils.getComplexSocketSet();
        for (WebsocketServer websocketServer: webSocketSet){
            websocketServer.sendMessage(message);
        }
    }
}
