package utils;

/**
 * Created by llc on 16/6/20.
 */
public class Utils {

    private static String existedDate = "2016-06-21";

    public static final String endpoint = "cn-beijing.sls.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的

    public static final String accessKeyId = "Kywj58hCJKOSQTSk"; // 使用你的阿里云访问秘钥AccessKeyId

    public static final String accessKeySecret = "tnQmCz77jZdxr5OBBqKb4lWx4fRziC"; // 使用你的阿里云访问秘钥AccessKeySecret

    public static final String project = "xiaole-log"; // 上面步骤创建的项目名称

    public static final String logstore = "logstore"; // 上面步骤创建的日志库名称

    public static void setExistedDate(String strDate){
        existedDate = strDate;
    }

    public static String getExistedDate(){
        return existedDate;
    }
}
