package utils;

import llc.NormalLog;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by llc on 16/8/8.
 */
public class Test {
    public static void main(String args[]){
        File file = new File("/Users/linchuan/Downloads/a.txt");
        try {
            List<String> logs = FileUtils.readLines(file);
            for (String slog : logs){
                slog = slog.substring(slog.indexOf("{"));
                NormalLog log = new NormalLog(slog);
                if (log.isTrans()){
                    if (log.belongsToSimple()) {
                        System.out.println((log.toReadableSimpleLog()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
