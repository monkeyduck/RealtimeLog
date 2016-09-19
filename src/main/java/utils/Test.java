package utils;

import com.utils.DownloadFileUtil;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by llc on 16/8/8.
 */
public class Test {
    public static void main(String args[]){
//        File readFile = new File("/Users/linchuan/Downloads/854.txt");
//        String member_id = "8548342";
//        int cnt = 0,len = 45;
//        Map<Long, String> sortLogs = new TreeMap<Long, String>(
//                new Comparator<Long>() {
//                    public int compare(Long obj1, Long obj2) {
//                        // 升序排序
//                        return obj1.compareTo(obj2);
//                    }
//                });
//        try {
//            List<String> simple = FileUtils.readLines(readFile);
//            for (String str: simple){
//                if (str.contains(member_id) && str.length()>len){
//                    Pattern pattern = Pattern.compile("(\\d){4}-(\\d){2}-(\\d){2}");
//                    Matcher matcher = pattern.matcher(str);
//                    if (matcher.find()){
//                        int pos = str.indexOf(matcher.group());
//                        String sTime = str.substring(pos,pos+23);
//                        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
//                        //时间解析
//                        DateTime dateTime = DateTime.parse(sTime, format);
//                        long timeStamp = dateTime.getMillis();
//                        if (sortLogs.containsKey(timeStamp)){
//                            timeStamp += 1;
//                        }
//                        sortLogs.put(timeStamp, str);
//                    }
//
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(cnt);
//        System.out.println(sortLogs.size());
    }
}
