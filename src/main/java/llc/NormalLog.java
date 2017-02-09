package llc;

import llc.Enums.ELevelType;
import llc.Enums.EnvironmentType;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;


/**
 * 日志服务
 * Created by hxx on 3/15/16.
 */
public class NormalLog {

    private static final Logger logger = Logger.getLogger("DayRollingFile");
    private String content;
    private String memberId;
    private String deviceId;
    private String module;
    private String ip;
    private EnvironmentType envType;
    private String timeStamp;
    private ELevelType level;
    private JSONObject jsonObject;
    private String time;




    public NormalLog(String log) throws Exception {
        JSONObject json = JSONObject.fromObject(log);
        level = ELevelType.fromString(json.getString("level"));
        memberId = replaceNull(json.getString("memberId"));
        module = replaceNull(json.getString("module"));
        ip = replaceNull(json.getString("ip"));
        deviceId = replaceNull(json.getString("deviceId"));
        String environment = replaceNull(json.getString("environment"));
        timeStamp = replaceNull(json.getString("timeStamp"));
        envType = EnvironmentType.fromString(environment);
        //检查日志是否合法
        content = processContent(memberId, timeStamp, module,(json.getString("content")));
        jsonObject = JSONObject.fromObject(content);
        time = new DateTime(Long.parseLong(timeStamp)).toString("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public String getContent() {
        return content;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getModule() {
        return module;
    }

    public String getIp() {
        return ip;
    }

    public EnvironmentType getEnvType() {
        return envType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public ELevelType getLevel() {
        return level;
    }

    private static String replaceNull(String st) {
        return st == null ? "" : st;
    }

    static private String processContent(String memberId, String timeStamp, String module, String content)
            throws Exception{
        if (module.equals("") || memberId.equals("") || timeStamp.equals("")) {
            String message = "module, memberId and timeStamp should have value";
            throw new Exception(message);
        }
        return content;
    }

    public boolean belongsToSimple(){
        if (!this.getContentText().equals(""))
            return true;
        else
            return false;
    }

    public String getContentText(){
        if (content.contains("nonFirstStart")){
            return "nonFirstStart  "+this.jsonObject.getString("sendModule");
        }
        else if (content.contains("sendContent")){
            return this.jsonObject.getString("sendContent")+" "+jsonObject.getString("sendType");
        }
        else if (content.contains("replyContent")){
            return this.jsonObject.getString("replyContent") + " " + jsonObject.getString("replyType");
        }
        else return "";
    }

    public String getShortMem(){
        if (this.memberId.contains(".")) {
            String[] segs = memberId.split("\\.");
            return segs[1];
        }
        else
            return memberId;
    }

    public String getVersion(){
        if (content.contains("version")){
            return this.jsonObject.getString("version");
        }
        else if (content.contains("softwareVersion")){
            return this.jsonObject.getString("softwareVersion");
        }
        else{
            return "";
        }
    }

    public String getAudioRecordID(){
        if (this.content.contains("audioRecordId"))
            return this.jsonObject.getString("audioRecordId");
        else
            return "";
    }

    public String getRecordLink(){
        String record_id = getAudioRecordID();
        if (!record_id.equals("")){
            return "<a href=\"http://record-resource.oss-cn-beijing.aliyuncs.com/"+this.memberId+"/"+record_id+
                    "\">录音链接</a>";
        }
        else
            return "";

    }

    public String toSimpleLog(){
        String re;
        if (!getRecordLink().equals("")){
            re = time+"&nbsp;&nbsp;&nbsp;&nbsp;"+level+"&nbsp;&nbsp;&nbsp;&nbsp;"+module.replaceAll(">","&gt;")
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"+getContentText()+"&nbsp;&nbsp;&nbsp;&nbsp;"+getRecordLink()
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"+getShortMem()+"&nbsp;&nbsp;&nbsp;&nbsp;"+getVersion();
        }
        else{
            re=time+"&nbsp;&nbsp;&nbsp;&nbsp;"+level+"&nbsp;&nbsp;&nbsp;&nbsp;"+module.replaceAll(">","&gt;")
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"+getContentText()+"&nbsp;&nbsp;&nbsp;&nbsp;"+getShortMem()
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"
                    +getVersion();
        }
        return re;
    }

    public String toReadableSimpleLog(){
        String re;
        if (!getRecordLink().equals("")){
            re = time+"\t"+level+"\t"+module+"\t"+getContentText()+"\t"+getRecordLink()
                    +"\t"+getShortMem()+"\t"+getVersion();
        }
        else{
            re=time+"\t"+level+"\t"+module+"\t"+getContentText()+"\t"+getShortMem()
                    +"\t"+getVersion();
        }
        return re;
    }

    public boolean isTrans() {
        return module.contains("->");
    }

//    @Override
//    public String toString(){
//        return "{\"log_source\":\"" + this.getLog_source()+"\",\"log_time\":"+this.getLog_time()+",\"log_topic\":\""
//                +this.getLog_topic()+"\",\"time\":\""+this.getTime()+"\",\"device_id\":\""+this.getDevice_id()+
//                "\",\"ip\":\""+this.getIp()+"\",\"memberId\":\""+this.getMember_id()+"\",\"level\":\""+
//                this.getLog_level()+"\",\"module\":\""+this.getModtrans()+"\",\"content\":"+this.getContent()+"}";
//    }
}
