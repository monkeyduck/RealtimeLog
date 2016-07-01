package utils;

import llc.MyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.UserTransaction;
import java.sql.*;


/**
 * Created by llc on 16/6/20.
 */
public class DBHelper {
    private static final String url;
    private static final String user;
    private static final String password;
    private static final Logger logger;
    private static final String name;

    private UserTransaction userTx = null;

    static {
        url = "jdbc:mysql://123.56.237.250:3306/aibasis_log?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
        user = "aibasis";
        password = "Wenjin1411";
        name = "com.mysql.jdbc.Driver";
        logger = LoggerFactory.getLogger(DBHelper.class);
    }

    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;

    public ResultSet getRs() {
        return rs;
    }

    public DBHelper() {
        try {
            Class.forName(name);
            // 获得 Transaction 管理对象
//            InitialContext ctx = new InitialContext();
//            userTx = (UserTransaction)ctx.lookup("java:comp/UserTransaction");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            closeAll();
            conn = null;
        }
    }

    public Connection getConnection(){
        if (conn == null){
            try{
                conn = DriverManager.getConnection(url, user, password);
            }catch (Exception e){
                logger.error(e.getMessage());
                System.out.println(e.getMessage());
                closeAll();
                conn = null;
            }
        }
        return conn;
    }


    public void executeSQL(String sql) {
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            //userTx.begin();
            stmt.execute(sql);
            //userTx.commit();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            closeAll();
            conn = null;
        }
    }

    public void createTable(String tableName){
        Utils.setExistedDate(tableName);
        tableName = "RealTimeLog"+String.join("", tableName.split("-"));
        String drop = "DROP TABLE IF EXISTS`" + tableName + "`";
        executeSQL(drop);
        String sql = "CREATE TABLE `" + tableName + "` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `log_source` varchar(20) DEFAULT NULL,\n" +
                "  `log_time` bigint(20) DEFAULT NULL,\n" +
                "  `log_topic` varchar(50) DEFAULT NULL,\n" +
                "  `time` datetime DEFAULT NULL,\n" +
                "  `device_id` varchar(50) DEFAULT NULL,\n" +
                "  `ip` varchar(20) DEFAULT NULL,\n" +
                "  `member_id` varchar(50) DEFAULT NULL,\n" +
                "  `log_level` varchar(10) DEFAULT NULL,\n" +
                "  `modtrans` varchar(50) DEFAULT NULL,\n" +
                "  `content` text,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        executeSQL(sql);
        String createIndex = "create index memberIndex on "+tableName+" (member_id)";
        executeSQL(createIndex);
        createIndex = "create index deviceIndex on "+tableName+" (device_id)";
        executeSQL(createIndex);
    }

    public void insertLog(MyLog log){
        String str[] = log.getDate().split("-");
        String dbTableName = "RealTimeLog" + String.join("", str);
        String sql = String.format("insert into %s(log_source,log_time,log_topic,time,device_id,ip,member_id," +
                "log_level,modtrans,content) values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                dbTableName, log.getLog_source(), String.valueOf(log.getLog_time()), log.getLog_topic(), log.getTime(),
                log.getDevice_id(), log.getIp(), log.getMember_id(), log.getLog_level(), log.getModtrans(),
                log.getContent());

        executeSQL(sql);
    }

    public void closeAll() {
        // 关闭结果集对象
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                System.out.println(e.getMessage());
            }
        }

        // 关闭PreparedStatement对象
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                System.out.println(e.getMessage());
            }
        }

        // 关闭stmt 对象
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                System.out.println(e.getMessage());
            }
        }

        // 关闭Connection 对象
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

    private static String replaceNull(String str) {
        if (str != null) {
            String ret = str.replaceAll("'", "''");
            return ret == null ? "" : ret;
        } else {
            return "";
        }
    }
}
