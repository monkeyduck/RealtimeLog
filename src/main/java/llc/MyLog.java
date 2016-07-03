package llc;

import com.aliyun.openservices.log.common.LogContent;
import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.LogItem;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by llc on 16/6/20.
 */
public class MyLog {
    private static Logger logger = LoggerFactory.getLogger(MyLog.class);

    private String log_source;
    private int log_time;
    private String log_topic;
    private String time;
    private String device_id;
    private String ip;
    private String member_id;
    private String log_level;
    private String modtrans;
    private String content;

    MyLog(String message) throws Exception{
        JSONObject jsonObject = JSONObject.fromObject(message);
        this.log_level = jsonObject.getString("log_level");
        this.log_source = jsonObject.getString("log_source");
        this.log_time = jsonObject.getInt("log_time");
        this.log_topic = jsonObject.getString("log_topic");
        this.time = jsonObject.getString("time");
        this.device_id = jsonObject.getString("device_id");
        this.member_id = jsonObject.getString("member_id");
        this.modtrans = jsonObject.getString("modtrans");
        this.content = jsonObject.getString("content");
        this.ip = jsonObject.getString("ip");
    }

    MyLog(LogItem logItem, LogGroupData logGroupData){
        this.log_source = logGroupData.GetSource();
        this.log_time = logItem.GetTime();
        this.log_topic = logGroupData.GetTopic();
        for (LogContent content: logItem.GetLogContents()){
            if (content.GetKey().equals("log_level"))
                this.log_level = content.GetValue();
            else if (content.GetKey().equals("content"))
                this.content = content.GetValue();
            else if (content.GetKey().equals("modtrans"))
                this.modtrans = content.GetValue();
            else if (content.GetKey().equals("time"))
                this.time = content.GetValue();
            else if (content.GetKey().equals("device_id")){
                if (!content.GetValue().equals("")){
                    this.device_id = content.GetValue();
                }else{
                    int startIndex = this.content.indexOf("deviceId");
                    if (startIndex > 0){
                        this.device_id = this.content.substring(startIndex+11, startIndex+28);
                    }else{
                        logger.error("Log doesn't have device_id field!");
                    }
                }
            }
            else if (content.GetKey().equals("ip"))
                this.ip = content.GetValue();
            else if (content.GetKey().equals("member_id"))
                this.member_id = content.GetValue();
            else
                logger.error("Lack some fields when create MyLog object. LogItem:" + logItem.toString());
        }
    }

    public String getLog_source() {
        return log_source;
    }

    public int getLog_time() {
        return log_time;
    }

    public String getLog_topic() {
        return log_topic;
    }

    public String getTime() {
        return time;
    }

    public String getDevice_id() { return device_id; }

    public String getIp() {
        return ip;
    }

    public String getMember_id() {
        return member_id;
    }

    public String getLog_level() {
        return log_level;
    }

    public String getModtrans() {
        return modtrans;
    }

    public String getContent() {
        return content;
    }

    public String getDate(){
        return this.time.split(" ")[0];
    }

    public boolean isModtransUseful(){
        if (this.modtrans.contains("->"))
            return true;
        else
            return false;
    }

    public boolean belongsToSimple(){
        if (this.modtrans.contains("user"))
            return true;
        else
            return false;
    }

    public String getContentText(){
        JSONObject jsonObject = JSONObject.fromObject(this.content);
        if (content.contains("sendContent")){
            return jsonObject.getString("sendContent");
        }
        else if (content.contains("replyContent")){
            return jsonObject.getString("replyContent");
        }
        else{
            return this.content;
        }
    }

    public String getShortMem(){
        if (this.member_id.contains("."))
            return member_id.split(".")[1];
        else
            return member_id;
    }

    public String getVersion(){
        JSONObject jsonObject = JSONObject.fromObject(this.content);
        if (content.contains("version")){
            return jsonObject.getString("version");
        }
        else if (content.contains("softwareVersion")){
            return jsonObject.getString("softwareVersion");
        }
        else{
            return "";
        }
    }

    public String toSimpleLog(){
        return this.time+" "+this.log_level+" "+this.modtrans+" "+this.getContentText()+
                " "+this.getShortMem()+" "+this.getVersion();
    }

    @Override
    public String toString(){
        return "{\"log_source\":\"" + this.getLog_source()+"\",\"log_time\":"+this.getLog_time()+",\"log_topic\":\""
                +this.getLog_topic()+"\",\"time\":\""+this.getTime()+"\",\"device_id\":\""+this.getDevice_id()+
                "\",\"ip\":\""+this.getIp()+"\",\"member_id\":\""+this.getMember_id()+"\",\"log_level\":\""+
                this.getLog_level()+"\",\"modtrans\":\""+this.getModtrans()+"\",\"content\":"+this.getContent()+"}";
    }
}
