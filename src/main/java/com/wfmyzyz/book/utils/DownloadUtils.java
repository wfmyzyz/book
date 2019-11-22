package com.wfmyzyz.book.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author admin
 */
public class DownloadUtils {

    public static void DownloadFile(String path,HttpServletResponse response,String name){
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;
        File pdfFile = new File(path);
        try {
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(name,"utf-8"));
            bufferedInputStream = new BufferedInputStream(new FileInputStream(pdfFile));
            byte[] buffer = new byte[1024];
            int read;
            outputStream = response.getOutputStream();
            while ((read = bufferedInputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedInputStream != null){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
