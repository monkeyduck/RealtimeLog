package llc;

import com.rabbitmq.client.*;
import utils.SessionUtils;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeoutException;

/**
 * Created by llc on 16/6/30.
 */
public class LogServer {
    private String QUEUE_NAME = "WebsocketPipe";
    private static ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    static {
        factory = new ConnectionFactory();
        factory.setHost("123.56.237.250");
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
                CopyOnWriteArraySet<WebsocketServer> webSocketSet = SessionUtils.getWebSocketSet();
                for (WebsocketServer websocketServer: webSocketSet){
                    websocketServer.sendMessage(message);
                }
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    public void broadcastLog() throws IOException {
        while(true){
            CopyOnWriteArraySet<WebsocketServer> webSocketSet = SessionUtils.getWebSocketSet();
            for (int i=1;i<=10;i++){
                for (WebsocketServer websocketServer: webSocketSet){
                    websocketServer.sendMessage("This is the "+i+"message");
                }
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
