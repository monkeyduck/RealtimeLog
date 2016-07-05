package llc;

import com.rabbitmq.client.*;
import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBHelper;
import utils.DBUtils;
import utils.SessionUtils;
import utils.Utils;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by llc on 16/6/30.
 */
public class LogServer {
    private String QUEUE_NAME = "WebsocketPipe";
    private static final Logger logger = LoggerFactory.getLogger(LogServer.class);
    private static ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    static {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    public void start() throws IOException, TimeoutException{
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Received message:"+message);
                logger.info("simple size:"+SessionUtils.getSimpleWebSocketSet().size());
                logger.info("complex size:"+SessionUtils.getComplexSocketSet().size());
                if (SessionUtils.getSimpleWebSocketSet().size() > 0 ||
                        SessionUtils.getComplexSocketSet().size() > 0){
                    try{
                        MyLog log = new MyLog(message);
                        if (log.belongsToSimple()){
                            System.out.println("#############");
                            broadcastSimple(log);
                        }
                        broadcastComplex(message);
                        logger.info("Received '" + message + "'");
                    }catch (Exception e){
                        logger.error(e.getMessage());
                    }
                }
//                storeIntoDatabase(message);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    public void storeIntoDatabase(String message){
        try{
            MyLog log = new MyLog(message);
            if (log.isModtransUseful()){
                if (!log.getDate().equals(Utils.getExistedDate())){
                    DBUtils.createTable(log.getDate());
                }
                DBUtils.insertLog(log);
                logger.info("Inserted into database successfully!");
            }
        }catch (Exception e){
            logger.info("Cannot parse the log:"+message);
        }
    }

    public void broadcastSimple(MyLog log) throws IOException{
        logger.info("Start to broadcast simple logs...");
        System.out.println(log.toSimpleLog());
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
