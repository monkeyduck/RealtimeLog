package llc;

/**
 * Created by llc on 17/1/22.
 */

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;
import utils.SessionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

public class KafkaProcessor implements Runnable{
    private final ConsumerConnector consumer;
    // 连接的Zookeeper接口, 不同业务模块使用的数据是相同的
    private final static String zookeeper = "10.172.217.86:2181,10.44.143.42:2181,10.252.0.171:2181";
    // Kafka消费者订阅的主题,不同模块使用是相同的
    private final static String topic = "aibasisStats";
    // Kafka
    private final static String groupId = "webSocketConsumer2";
    // logger
//    static final Logger logger = LoggerFactory.getLogger(KafkaProcessor.class);
    static final Logger logger = org.apache.log4j.Logger.getLogger("DayRollingFile");
    static final Logger complexlogger = org.apache.log4j.Logger.getLogger("complex");
    static final Logger simplelogger = org.apache.log4j.Logger.getLogger("simple");

    public KafkaProcessor() {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
    }
    public void process() {
        Map<String, Integer> topicCount = new HashMap<String, Integer>();
        topicCount.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                String message = new String(it.next().message());
                logger.info("Message from Single Topic: " + message);
                handleMessage(message);
            }
        }
        if (consumer != null) {
            consumer.shutdown();
        }
    }

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

    public static void main(String[] args) {
        KafkaProcessor simpleHLConsumer = new KafkaProcessor();
        simpleHLConsumer.process();
    }

    public void run() {
        process();
    }
}
