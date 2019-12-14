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

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

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
            DownloadUtils.downloadTxtFile(response, book, bookSerialList);

        }
        return null;
    }


}
