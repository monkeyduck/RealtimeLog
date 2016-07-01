package llc;

import com.rabbitmq.client.*;
import jdk.nashorn.internal.runtime.ECMAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBHelper;
import utils.SessionUtils;
import utils.Utils;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeoutException;

/**
 * Created by llc on 16/6/30.
 */
public class LogServer {
    private String QUEUE_NAME = "WebsocketPipe";
    private static final Logger logger = LoggerFactory.getLogger(LogServer.class);
    private DBHelper dbHelper = new DBHelper();
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

                CopyOnWriteArraySet<WebsocketServer> webSocketSet = SessionUtils.getSimpleWebSocketSet();
                for (WebsocketServer websocketServer: webSocketSet){
                    websocketServer.sendMessage(message);
                }
                logger.info("Received '" + message + "'");
                storeIntoDatabase(message);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    public void storeIntoDatabase(String message){
        try{
            MyLog log = new MyLog(message);
            if (log.isModtransUseful()){
                if (!log.getDate().equals(Utils.getExistedDate())){
                    dbHelper.createTable(log.getDate());
                }
                dbHelper.insertLog(log);
                logger.info("Inserted into database successfully!");
            }
        }catch (Exception e){
            logger.info("Cannot parse the log:"+message);
        }
    }

}
