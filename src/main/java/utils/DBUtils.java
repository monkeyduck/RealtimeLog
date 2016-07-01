package utils;

import llc.MyLog;

/**
 * Created by llc on 16/7/1.
 */
public class DBUtils {
    public static final DBHelper db;

    static {
        db = new DBHelper();
    }

    public static void createTable(String tableName){
        db.createTable(tableName);
    }

    public static void insertLog(MyLog log){
        db.insertLog(log);
    }
}
