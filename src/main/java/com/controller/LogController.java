package com.controller;

import com.model.OdpsLog;
import com.service.LogService;
import com.utils.DownloadFileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by llc on 16/7/29.
 */
@Controller
public class LogController {
    private static final Logger logger = Logger.getLogger("DayRollingFile");

    @Resource(name="LogService")
    private LogService logService;

    @RequestMapping("/downloadSimple")
    public void downloadSimple(HttpServletResponse response,
                                       @RequestParam("member_id") String member_id,
                                       @RequestParam("date") String date){
        DateTime dt = new DateTime();
        String todayDate = dt.toString("yyyy-MM-dd");
        Map<Long, String> sortLogs = new TreeMap<Long, String>(
                new Comparator<Long>() {
                    public int compare(Long obj1, Long obj2) {
                        // 升序排序
                        return obj1.compareTo(obj2);
                    }
                });
        // long member_id to short member_id
        if (member_id.contains(".")){
            member_id = member_id.split("\\.")[1];
        }
        /**
         * 查询今日日志
         */
        String mqlog;
        String path = "/root/log4j/hxx/webSocket/";
        if (date.equals(todayDate)) {
            mqlog = path + "simplelog";
        }
        else {
            String newDate = date.split("-")[0] + date.split("-")[1] + date.split("-")[2];
            String tableName = "simplelog" + newDate + ".log";
            mqlog = path + tableName;
        }
        File readFile = new File(mqlog);
        int len = 45;
        try {
            List<String> simple = FileUtils.readLines(readFile);
            Pattern pattern = Pattern.compile("(\\d){4}-(\\d){2}-(\\d){2}");
            Matcher matcher = null;
            for (String str: simple){
                if (str.contains(member_id) && str.length()>len){
                    matcher = pattern.matcher(str);
                    if (matcher.find()) {
                        int pos = str.indexOf(matcher.group());
                        String sTime = str.substring(pos, pos + 23);

                        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
                        //时间解析
                        DateTime dateTime = DateTime.parse(sTime, format);
                        long timeStamp = dateTime.getMillis();
                        if (sortLogs.containsKey(timeStamp)) {
                            timeStamp += 1;
                        }
                        sortLogs.put(timeStamp, str);
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        String targetName = "simple_"+member_id+"_"+date+".txt";
        String basePath = "/root/log4j/hxx/simpleDownload/";

        File file = new File(basePath+targetName);
        try {
            FileUtils.writeLines(file, sortLogs.values());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        try {
            DownloadFileUtil.pushFile(targetName, basePath + targetName, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        file.delete();
    }

    @RequestMapping("/downloadComplex")
    public void downloadComplex(HttpServletResponse response,
                                       @RequestParam("member_id") String member_id,
                                       @RequestParam("date") String date){
        DateTime dt = new DateTime();
        String today = dt.toString("yyyy-MM-dd");
        List<String> complexLogs = new ArrayList<String>();
        /**
         * 查询今日复杂日志
         */
        String mqlog;
        String path = "/root/log4j/hxx/webSocket/";
        if (date.equals(today)) {
            mqlog = path + "complexlog";
        }
        else {
            String newDate = date.split("-")[0] + date.split("-")[1] + date.split("-")[2];
            String tableName = "complexlog" + newDate + ".log";
            mqlog = path + tableName;
        }
        File readFile = new File(mqlog);
        try{
            List<String> complex = FileUtils.readLines(readFile);
            for (String str: complex){
                if (str.contains(member_id)){
                    complexLogs.add(str);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        /**
//         * 查询昨日及以前
//         */
//        else{
//            String newDate = date.split("-")[0]+date.split("-")[1]+date.split("-")[2];
//            String tableName = "log"+newDate;
//            if (member_id.length()<13){ //短id查询
//                member_id = "%"+member_id;
//            }
//            List<OdpsLog> logs = logService.queryLog(member_id, tableName);
//            for (OdpsLog log:logs){
//                if (log.isTrans()){
//                    complexLogs.add(log.toComplexLog());
//                }
//            }
//        }

        String targetName = "complex_"+member_id+"_"+date+".txt";
        String basePath = "/root/log4j/hxx/complexDownload/";
        File file = new File(basePath+targetName);
        if (!file.exists()){
            try {
                FileUtils.writeLines(file, complexLogs);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        try {
            DownloadFileUtil.pushFile(targetName, basePath + targetName, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        file.delete();
    }


}
