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

/**
 * Created by llc on 16/8/8.
 */
public class Test {
    public static void main(String args[]){
        File readFile = new File("/Users/linchuan/Downloads/simplelog20160807.log");
        String member_id = "73019606";
        int cnt = 0;
        Map<Long, String> sortLogs = new TreeMap<Long, String>(
                new Comparator<Long>() {
                    public int compare(Long obj1, Long obj2) {
                        // 升序排序
                        return obj1.compareTo(obj2);
                    }
                });
        try {
            List<String> simple = FileUtils.readLines(readFile);
            for (String str: simple){
                if (str.contains(member_id)){
                    cnt++;
                    String s = str.split("\\t")[0];
                    String sTime = s.substring(s.length()-23);
                    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    //时间解析
                    DateTime dateTime = DateTime.parse(sTime, format);
                    long timeStamp = dateTime.getMillis();
                    if (sortLogs.containsKey(timeStamp)){
                        System.out.println("Repeat "+timeStamp);
                        timeStamp += 1;
                    }
                    sortLogs.put(timeStamp, str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(cnt);
        System.out.println(sortLogs.size());
    }
}
