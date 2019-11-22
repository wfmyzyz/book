package com.wfmyzyz.book.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookSerial;
import com.wfmyzyz.book.domain.enums.BookEnum;
import com.wfmyzyz.book.domain.enums.BookSerialCheckEnum;
import com.wfmyzyz.book.service.IBookSerialService;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.utils.DownloadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 */
@RequestMapping("download")
@Controller
public class DownloadController {

    @Autowired
    IBookService bookService;
    @Autowired
    IBookSerialService bookSerialService;
    @Value("${myuploadurl.url}")
    String path;

    /**
     * 根据ID下载书籍
     * @return
     */
    @GetMapping("book/{id}")
    public HttpServletResponse downloadBook(@PathVariable("id") Integer id, HttpServletResponse response){
        Book book = bookService.getById(id);
        if (Objects.equals(book.getBookType(), BookEnum.pdf.toString())){
            String pathName = book.getUploadUrl().substring(book.getUploadUrl().lastIndexOf("/")+1);
            DownloadUtils.DownloadFile(path+"/admin/book/pdf/"+pathName,response,book.getName()+".pdf");
        }else {
            QueryWrapper<BookSerial> queryWrapper = new QueryWrapper();
            queryWrapper.eq("book_id",book.getBookId());
            queryWrapper.eq("serial_check", BookSerialCheckEnum.通过.toString());
            queryWrapper.eq("tb_status","正常");
            queryWrapper.orderByAsc("serial_num");
            List<BookSerial> bookSerialList = bookSerialService.list(queryWrapper);
            response.setContentType("application/octet-stream; charset=utf-8");
            try {
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
                    text = m_special.replaceAll(""); // 过滤特殊标签

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
        return null;
    }


}
