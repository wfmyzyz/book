package com.wfmyzyz.book.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookLabel;
import com.wfmyzyz.book.domain.Label;
import com.wfmyzyz.book.domain.enums.BookCheckEnum;
import com.wfmyzyz.book.service.IBookLabelService;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.service.ILabelService;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import com.wfmyzyz.book.vo.BookListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiong
 */
@RestController
@RequestMapping("api/book")
public class BookApiController {

    @Autowired
    private IBookLabelService bookLabelService;
    @Autowired
    private ILabelService labelService;
    @Autowired
    private IBookService bookService;

    /**
     * 根据标签获取书籍列表
     * @param labels
     * @return
     */
    @RequestMapping("getBookListByLabelList")
    public LayuiBackData getBookListByLabelList(@RequestParam("labels") List<String> labels, @RequestParam("page") Integer page,@RequestParam("limit") Integer limit){
        IPage<BookListVo> pages = new Page<>();
        if (labels != null && labels.size() > 0){
            QueryWrapper labelQueryWrapper = new QueryWrapper();
            labelQueryWrapper.in("name",labels);
            labelQueryWrapper.eq("tb_status","正常");
            List<Label> labelList = labelService.list(labelQueryWrapper);
            if (labelList != null && labelList.size() > 0){
                QueryWrapper<BookLabel> bookLabelQueryWrapper = new QueryWrapper<>();
                bookLabelQueryWrapper.select("  book_id,count( book_id ) count ");
                bookLabelQueryWrapper.in("label_id",labelList.stream().map(e->e.getLabelId()).collect(Collectors.toList()));
                bookLabelQueryWrapper.groupBy("book_id");
                bookLabelQueryWrapper.having("count >= "+labelList.size(),"");
                List<BookLabel> bookLabelList = bookLabelService.list(bookLabelQueryWrapper);
                if (bookLabelList != null && bookLabelList.size() > 0){
                    QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
                    bookQueryWrapper.eq("tb_status","正常");
                    bookQueryWrapper.eq("book_check", BookCheckEnum.上架.toString());
                    bookQueryWrapper.in("book_id",bookLabelList.stream().map(e->e.getBookId()).collect(Collectors.toList()));
                    IPage<Book> bookIPage = bookService.page(new Page<>(page, limit), bookQueryWrapper);
                    List<BookListVo> bookListVoList = new ArrayList<>();
                    for (Book book:bookIPage.getRecords()){
                        BookListVo bookListVo = new BookListVo();
                        BeanUtils.copyProperties(book,bookListVo);
                        QueryWrapper<BookLabel> bookLabelQueryWrapper2 = new QueryWrapper<>();
                        bookLabelQueryWrapper2.eq("tb_status","正常");
                        bookLabelQueryWrapper2.eq("book_id", book.getBookId());
                        List<BookLabel> bookLabelList2 = bookLabelService.list(bookLabelQueryWrapper2);
                        if (bookLabelList2 != null && bookLabelList2.size() > 0){
                            List<Integer> labelIdList = bookLabelList2.stream().map(BookLabel::getLabelId).collect(Collectors.toList());
                            QueryWrapper<Label> labelQueryWrapper2 = new QueryWrapper<>();
                            labelQueryWrapper2.eq("tb_status","正常");
                            labelQueryWrapper2.in("label_id",labelIdList);
                            List<Label> labelList2 = labelService.list(labelQueryWrapper2);
                            bookListVo.setLabelList(labelList2);
                        }
                        bookListVoList.add(bookListVo);
                    }
                    pages.setRecords(bookListVoList);
                    pages.setPages(bookIPage.getPages());
                    pages.setSize(bookIPage.getSize());
                    pages.setTotal(bookIPage.getTotal());
                    pages.setCurrent(bookIPage.getCurrent());
                    return LayuiBackData.bringData(pages);
                }
            }
        }else {
            QueryWrapper<Book> hostQueryWrapper = new QueryWrapper<>();
            hostQueryWrapper.orderByDesc("browse");
            hostQueryWrapper.eq("book_check", BookCheckEnum.上架.toString());
            hostQueryWrapper.eq("tb_status","正常");
            IPage<Book> page3 = bookService.page(new Page<>(page, limit), hostQueryWrapper);
            List<Book> bookList = page3.getRecords();
            if (bookList != null && bookList.size() > 0){
                List<BookListVo> bookListVoList = new ArrayList<>();
                for (Book book:bookList){
                    BookListVo bookListVo = new BookListVo();
                    BeanUtils.copyProperties(book,bookListVo);
                    QueryWrapper<BookLabel> bookLabelQueryWrapper = new QueryWrapper<>();
                    bookLabelQueryWrapper.eq("tb_status","正常");
                    bookLabelQueryWrapper.eq("book_id", book.getBookId());
                    List<BookLabel> bookLabelList = bookLabelService.list(bookLabelQueryWrapper);
                    if (bookLabelList != null && bookLabelList.size() > 0){
                        List<Integer> labelIdList = bookLabelList.stream().map(BookLabel::getLabelId).collect(Collectors.toList());
                        QueryWrapper<Label> labelQueryWrapper = new QueryWrapper<>();
                        labelQueryWrapper.eq("tb_status","正常");
                        labelQueryWrapper.in("label_id",labelIdList);
                        List<Label> labelList = labelService.list(labelQueryWrapper);
                        bookListVo.setLabelList(labelList);
                    }
                    bookListVoList.add(bookListVo);
                }
                pages.setRecords(bookListVoList);
                pages.setPages(page3.getPages());
                pages.setSize(page3.getSize());
                pages.setTotal(page3.getTotal());
                pages.setCurrent(page3.getCurrent());
                return LayuiBackData.bringData(pages);
            }
        }
        return LayuiBackData.error();
    }
}
