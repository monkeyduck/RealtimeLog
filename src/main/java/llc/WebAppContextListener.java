package llc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by llc on 16/7/28.
 */
public class WebAppContextListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(WebAppContextListener.class);
    private static MQServer server = null;

    public void contextInitialized(ServletContextEvent event) {
        logger.info("MQ Server 启动..........");
        server = new MQServer();
        try{
            server.start();
        }catch (InterruptedException e){
            logger.error(e.getMessage());
            logger.error("Delete que: "+MQServer.queueName);
            server.stop();
        }

    }

    public void contextDestroyed(ServletContextEvent event) {
        server.stop();
    }


}
