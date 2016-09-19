package llc;


import org.apache.log4j.Logger;
import utils.SessionUtils;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 接受log
 * Created by hxx on 5/3/16.
 */
class Receiver {
    static final Logger logger = Logger.getLogger("DayRollingFile");
    static final Logger complexlogger = Logger.getLogger("complex");
    static final Logger simplelogger = Logger.getLogger("simple");
    /**
     * 消费日志
     * @param slog
     */
    public void handleMessage(String slog) {
        try {
            NormalLog log = new NormalLog(slog);
            if (log.isTrans()){
                if (log.belongsToSimple()) {
                    broadcastSimple(log);
                    simplelogger.info(log.toReadableSimpleLog());
                }
                broadcastComplex(slog);
                complexlogger.info(slog);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    public void broadcastSimple(NormalLog log) throws IOException {
        CopyOnWriteArraySet<WebsocketServer> simplewebSocketSet = SessionUtils.getSimpleWebSocketSet();
        for (WebsocketServer websocketServer: simplewebSocketSet){
            String member_id = websocketServer.getClientSetting().getMember_id();
            String module = websocketServer.getClientSetting().getModule();
            boolean flag_mem = true;
            boolean flag_module = true;
            if (!member_id.equals("")){
                if (!log.getMemberId().contains(member_id))
                    flag_mem = false;
            }
            if (!module.equals("") && !module.equals("All")){
                if (!log.getModule().contains(module)){
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
