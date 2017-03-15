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
    private String member_id;
    private String device_id;
    private String modtrans;
    private String ip;
    private EnvironmentType envType;
    private String timeStamp;
    private ELevelType level;
    private JSONObject jsonObject;
    private String time;




    public NormalLog(String log) throws Exception {
        JSONObject json = JSONObject.fromObject(log);
        level = ELevelType.fromString(json.getString("level"));
        member_id = replaceNull(json.getString("memberId"));
        modtrans = replaceNull(json.getString("module"));
        ip = replaceNull(json.getString("ip"));
        device_id = replaceNull(json.getString("deviceId"));
        String environment = replaceNull(json.getString("environment"));
        timeStamp = replaceNull(json.getString("timeStamp"));
        envType = EnvironmentType.fromString(environment);
        //检查日志是否合法
        content = processContent(member_id, timeStamp, modtrans,(json.getString("content")));
        jsonObject = JSONObject.fromObject(content);
        time = new DateTime(Long.parseLong(timeStamp)).toString("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public String getContent() {
        return content;
    }

    public String getModtrans() {
        return modtrans;
    }

    public void setModtrans(String modtrans) {
        this.modtrans = modtrans;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    static private String processContent(String member_id, String timeStamp, String modtrans, String content)
            throws Exception{
        if (modtrans.equals("") || member_id.equals("") || timeStamp.equals("")) {
            String message = "modtrans, member_id and timeStamp should have value";
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
        if (this.member_id.contains(".")) {
            String[] segs = member_id.split("\\.");
            return segs[1];
        }
        else
            return member_id;
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
            return "<a href=\"http://record-resource.oss-cn-beijing.aliyuncs.com/"+this.member_id+"/"+record_id+
                    "\">录音链接</a>";
        }
        else
            return "";

    }

    public String toSimpleLog(){
        String re;
        if (!getRecordLink().equals("")){
            re = time+"&nbsp;&nbsp;&nbsp;&nbsp;"+level+"&nbsp;&nbsp;&nbsp;&nbsp;"+modtrans.replaceAll(">","&gt;")
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"+getContentText()+"&nbsp;&nbsp;&nbsp;&nbsp;"+getRecordLink()
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"+getShortMem()+"&nbsp;&nbsp;&nbsp;&nbsp;"+getVersion();
        }
        else{
            re=time+"&nbsp;&nbsp;&nbsp;&nbsp;"+level+"&nbsp;&nbsp;&nbsp;&nbsp;"+modtrans.replaceAll(">","&gt;")
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"+getContentText()+"&nbsp;&nbsp;&nbsp;&nbsp;"+getShortMem()
                    +"&nbsp;&nbsp;&nbsp;&nbsp;"
                    +getVersion();
        }
        return re;
    }

    public String toReadableSimpleLog(){
        String re;
        if (!getRecordLink().equals("")){
            re = time+"\t"+level+"\t"+modtrans+"\t"+getContentText()+"\t"+getRecordLink()
                    +"\t"+getShortMem()+"\t"+getVersion();
        }
        else{
            re=time+"\t"+level+"\t"+modtrans+"\t"+getContentText()+"\t"+getShortMem()
                    +"\t"+getVersion();
        }
        return re;
    }

    public boolean isTrans() {
        return modtrans.contains("->");
    }

//    @Override
//    public String toString(){
//        return "{\"log_source\":\"" + this.getLog_source()+"\",\"log_time\":"+this.getLog_time()+",\"log_topic\":\""
//                +this.getLog_topic()+"\",\"time\":\""+this.getTime()+"\",\"device_id\":\""+this.getDevice_id()+
//                "\",\"ip\":\""+this.getIp()+"\",\"member_id\":\""+this.getMember_id()+"\",\"level\":\""+
//                this.getLog_level()+"\",\"modtrans\":\""+this.getModtrans()+"\",\"content\":"+this.getContent()+"}";
//    }
}
