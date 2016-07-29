package com.utils;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by hxx on 5/27/16.
 */
public class DownloadFileUtil {

    public static void pushFile(String saveName, String path, HttpServletResponse response) throws IOException {
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(saveName, "UTF-8"));
        OutputStream out = response.getOutputStream();
        File file = new File(path);
        FileUtils.copyFile(file, out);
        out.close();
    }
}
