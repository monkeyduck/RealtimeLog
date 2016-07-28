package llc;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by hxx on 5/4/16.
 */
enum Config {
    INSTANCE;
    private static PropertiesConfiguration configuration;

    static {
        try {
            configuration = new PropertiesConfiguration("amqpconf.properties");
        } catch (ConfigurationException e) {
            Receiver.logger.error(e.getMessage());
        }
    }

    public String getServerIP() {
        return configuration.getString("serverIP");
    }

    public String getUserName() {
        return configuration.getString("userName");
    }

    public String getPassWord(){
        return configuration.getString("passWord");
    }

    public String getVirtualHost(){
        return configuration.getString("virtualHost");
    }

    public String getQueueName(){
        return configuration.getString("queueName");
    }

    public String getExchangeName(){
        return configuration.getString("exchangeName");
    }

    public String getRoutineKey(){
        return configuration.getString("routineKey");
    }
}

