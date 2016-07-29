package llc.Enums;

import java.util.HashMap;

/**
 *
 * Created by hxx on 4/15/16.
 */
public enum ELevelType {
    ERROR, FATAL,INFO;

    private static final HashMap<String,ELevelType> dic = new HashMap<String, ELevelType>();
    static {
        for (ELevelType type:values()){
            dic.put(type.toString(),type);
        }
    }

    public static ELevelType fromString(String str){
        ELevelType type = dic.get(str.toUpperCase());
        if (type == null){
            return ELevelType.INFO;
        }
        return type;
    }
}

