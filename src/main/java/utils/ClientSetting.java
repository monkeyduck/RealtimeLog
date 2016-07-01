package utils;

/**
 * Created by llc on 16/7/1.
 */
public class ClientSetting {
    private String member_id;
    private String module;

    public ClientSetting(){
        member_id = "";
        module = "";
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
