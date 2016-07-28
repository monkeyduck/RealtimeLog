package llc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;


/**
 * Created by llc on 16/7/12.
 */
public class MQServer {
    private final static String serverIP = Config.INSTANCE.getServerIP();
    private final static String userName = Config.INSTANCE.getUserName();
    private final static String passWord = Config.INSTANCE.getPassWord();
    private final static String virtualHost = Config.INSTANCE.getVirtualHost();
    final static String queueName = Config.INSTANCE.getQueueName();
    private final static String exchangeName = Config.INSTANCE.getExchangeName();
    private final static String routineKey = Config.INSTANCE.getRoutineKey();
    static final Logger logger = LoggerFactory.getLogger(MQServer.class);
    private CachingConnectionFactory cf;
    private RabbitAdmin admin;

    public MQServer() {
        cf = new CachingConnectionFactory(serverIP);
        cf.setUsername(userName);
        cf.setPassword(passWord);
        cf.setVirtualHost(virtualHost);
        admin = new RabbitAdmin(cf);
    }

    public void start() throws InterruptedException{
        Queue queue = new Queue(queueName);
        admin.declareQueue(queue);
        Exchange exchange = new FanoutExchange(exchangeName);
        admin.declareExchange(exchange);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routineKey).noargs());

        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(cf);
        Receiver listener = new Receiver();

        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
        container.setMessageListener(adapter);
        container.setQueueNames(queueName);
        logger.info("Start mq with queueName: "+queueName);
        container.start();
    }

    public void stop(){
        admin.deleteQueue(queueName);
    }

}
