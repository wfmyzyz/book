package com.wfmyzyz.book.utils;

import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookSerial;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 */
public class DownloadUtils {

    /**
     * 下载文件
     * @param path
     * @param response
     * @param name
     */
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

    /**
     * 下载txt文件
     * @param response
     * @param book
     * @param bookSerialList
     */
    public static void downloadTxtFile(HttpServletResponse response, Book book, List<BookSerial> bookSerialList) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(book.getName()+".txt","utf-8"));
            PrintWriter writer = response.getWriter();
            writer.print("-------------"+book.getName()+"-------------");
            for (int i=0;i<=5;i++){
                writer.println();
            }
            for (BookSerial bookSerial:bookSerialList){
                writer.print("---------第"+bookSerial.getSerialNum()+"章 "+bookSerial.getTitle()+" ---------");
                writer.println();
                writer.println();
                String text = bookSerial.getText().replaceAll("<br>|<br />", "\r\n");
                String regEx_html = "<[^>]+>";
                Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                Matcher m_html = p_html.matcher(text);
                text = m_html.replaceAll("");
                String regEx_special = "\\&[a-zA-Z]{1,10};";
                Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
                Matcher m_special = p_special.matcher(text);
                text = m_special.replaceAll("");
                writer.print(text);
                writer.println();
                writer.println();
            }
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
