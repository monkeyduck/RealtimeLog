package com.controller;

import com.model.OdpsLog;
import com.service.LogService;
import com.utils.DownloadFileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llc on 16/7/29.
 */
@Controller
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Resource(name="LogService")
    private LogService logService;

    @RequestMapping("/downloadSimple")
    public void downloadSimple(HttpServletResponse response,
                                       @RequestParam("member_id") String member_id,
                                       @RequestParam("date") String date){
        String newDate = date.split("-")[0]+date.split("-")[1]+date.split("-")[2];
        String tableName = "log"+newDate;
        logger.info("tableName: "+tableName);
        List<OdpsLog> logs = logService.queryLog(member_id, tableName);
        List<String> simpleLogs = new ArrayList<String>();
        for (OdpsLog log:logs){
            if (log.belongsToSimple()){
                simpleLogs.add(log.toSimpleLog());
            }
        }
        String targetName = "simple_"+member_id+"_"+date+".txt";
        String basePath = "/root/log4j/hxx/simpleDownload/";
        File file = new File(basePath+targetName);
        if (!file.exists()){
            try {
                FileUtils.writeLines(file, simpleLogs);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        try {
            DownloadFileUtil.pushFile(targetName, basePath + targetName, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("/downloadComplex")
    public ModelAndView downloadComplex(HttpServletResponse response,
                                       @RequestParam("member_id") String member_id,
                                       @RequestParam("date") String date){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("download");
        String newDate = date.split("-")[0]+date.split("-")[1]+date.split("-")[2];
        String tableName = "log"+newDate;
        List<OdpsLog> logs = logService.queryLog(member_id, tableName);
        List<String> complexLogs = new ArrayList<String>();
        for (OdpsLog log:logs){
            if (log.isTrans()){
                complexLogs.add(log.toComplexLog());
            }
        }
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
        return mv;
    }
}
