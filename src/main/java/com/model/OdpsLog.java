package com.model;

import net.sf.json.JSONObject;

/**
 * Created by llc on 16/7/29.
 */
public class OdpsLog {
    private String content;

    private String time;

    private String ip;

    private String log_level;

    private String member_id;

    private String device_id;

    private String modtrans;

    private String log_source;

    private long time_stamp;

    private String log_topic;

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLog_level() {
        return log_level;
    }

    public void setLog_level(String log_level) {
        this.log_level = log_level;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getModtrans() {
        return modtrans;
    }

    public void setModtrans(String modtrans) {
        this.modtrans = modtrans;
    }

    public String getLog_source() {
        return log_source;
    }

    public void setLog_source(String log_source) {
        this.log_source = log_source;
    }

    public String getLog_topic() {
        return log_topic;
    }

    public void setLog_topic(String log_topic) {
        this.log_topic = log_topic;
    }

    public boolean isTrans(){
        return this.modtrans.contains("->");
    }

    public String getContentText(){
        JSONObject jsonObject = JSONObject.fromObject(content);
        if (content.contains("sendContent")){
            return jsonObject.getString("sendContent")+" "+jsonObject.getString("sendType");
        }
        else if (content.contains("replyContent")){
            return jsonObject.getString("replyContent");
        }
        else{
            return "";
        }
    }
    public boolean belongsToSimple(){
        return this.modtrans.contains("user") && !this.getContentText().equals("");
    }

    public String getAudioRecordID(){
        JSONObject jsonObject = JSONObject.fromObject(content);
        if (this.content.contains("audioRecordId"))
            return jsonObject.getString("audioRecordId");
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
    public String getShortMem(){
        if (this.member_id.contains(".")) {
            String[] segs = member_id.split("\\.");
            return segs[1];
        }
        else
            return member_id;
    }

    public String getVersion(){
        JSONObject jsonObject = JSONObject.fromObject(content);
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
        String re;
        if (!getRecordLink().equals("")){
            re = time+"\t"+log_level+"\t"+modtrans
                    +"\t"+getContentText()+"\t"+getRecordLink()
                    +"\t"+getShortMem()+"\t"+getVersion();
        }
        else{
            re=time+"\t"+log_level+"\t"+modtrans
                    +"\t"+getContentText()+"\t"+getShortMem()
                    +"\t"+getVersion();
        }
        return re;
    }

    public String toComplexLog(){
        return "{\"log_source\":\"" + this.getLog_source()+"\",\"log_time\":"+this.getTime_stamp()+",\"log_topic\":\""
                +this.getLog_topic()+"\",\"time\":\""+this.getTime()+"\",\"device_id\":\""+this.getDevice_id()+
                "\",\"ip\":\""+this.getIp()+"\",\"memberId\":\""+this.getMember_id()+"\",\"level\":\""+
                this.getLog_level()+"\",\"module\":\""+this.getModtrans()+"\",\"content\":"+this.getContent()+"}";

    }
}
