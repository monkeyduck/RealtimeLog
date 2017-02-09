package llc;


import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by llc on 16/7/28.
 */
public class WebAppContextListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger("DayRollingFile");
    private static MQServer server = null;
    private static KafkaProcessor kafka = null;

//    public void contextInitialized(ServletContextEvent event) {
//        logger.info("MQ Server 启动..........");
//        server = new MQServer();
//        try{
//            server.start();
//        }catch (InterruptedException e){
//            logger.error(e.getMessage());
//            logger.error("Delete que: "+MQServer.queueName);
//            server.stop();
//        }
//
//    }
//
//    public void contextDestroyed(ServletContextEvent event) {
//        logger.info("Websocket start to shutdown...");
//        server.stop();
//    }

    public void contextInitialized(ServletContextEvent event) {
        logger.info("Kafka Server 启动..........");
        kafka = new KafkaProcessor();
        Thread thread = new Thread(kafka);
        thread.start();
//        kafka.process();
    }

    public void contextDestroyed(ServletContextEvent event) {
        logger.info("Websocket start to shutdown...");
    }

}
