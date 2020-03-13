package com.wfmyzyz.book.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookLabel;
import com.wfmyzyz.book.domain.Collect;
import com.wfmyzyz.book.domain.Label;
import com.wfmyzyz.book.domain.enums.BookCheckEnum;
import com.wfmyzyz.book.domain.enums.BookStatusEnum;
import com.wfmyzyz.book.service.IBookLabelService;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.service.ICollectService;
import com.wfmyzyz.book.service.ILabelService;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import com.wfmyzyz.book.vo.BookListVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;
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
    @Autowired
    private ICollectService collectService;

    /**
     * 根据标签获取书籍列表
     * @param labels
     * @return
     */
    @ApiOperation(value="根据标签获取书籍列表", notes="根据标签获取书籍列表" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="labels",value="labels",required=false,paramType="List<String>"),
            @ApiImplicitParam(name="page",value="page",required=true,paramType="Integer"),
            @ApiImplicitParam(name="limit",value="limit",required=true,paramType="Integer"),
            @ApiImplicitParam(name="bookName",value="bookName",required=false,paramType="query")
    })
    @RequestMapping("getBookListByLabelList")
    public LayuiBackData getBookListByLabelList(@RequestParam(value = "labels",required = false) List<String> labels, @RequestParam("page") Integer page,
                                                @RequestParam("limit") Integer limit,@RequestParam(value = "bookName",required = false) String bookName){
        IPage<BookListVo> pages = new Page<>();
        if (StringUtils.isNotBlank(bookName)){
            QueryWrapper<Book> hostQueryWrapper = new QueryWrapper<>();
            hostQueryWrapper.orderByDesc("browse");
            hostQueryWrapper.like("name",bookName);
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
            return LayuiBackData.error();
        }
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

    @ApiOperation("通过标签获取书籍排行")
    @RequestMapping("ranking")
    public Msg getBookRankByLabel(@RequestParam("label") String name){
        int max = 10;
        Map<String,Object> map = new HashMap<>();
        map.put("newBook",new ArrayList<>());
        map.put("readBook",new ArrayList<>());
        map.put("collectBook",new ArrayList<>());
        QueryWrapper<Book> timeQuery = new QueryWrapper<>();
        QueryWrapper<Book> readQuery = new QueryWrapper<>();
        QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<>();
        collectQueryWrapper.select("book_id,count(book_id) sum");
        if (!"全部".equals(name)){
            Label label = labelService.getOne(new QueryWrapper<Label>().eq("name", name).eq("tb_status", "正常"));
            QueryWrapper<BookLabel> queryWrapper = new QueryWrapper<BookLabel>().eq("label_id", label.getLabelId()).eq("tb_status", "正常");
            List<BookLabel> bookLabelList = bookLabelService.list(queryWrapper);
            List<Integer> bookIdList = bookLabelList.stream().map(BookLabel::getBookId).collect(Collectors.toList());
            if (bookIdList.size() > 0) {
                timeQuery.in("book_id", bookIdList);
                readQuery.in("book_id", bookIdList);
                collectQueryWrapper.in("book_id", bookIdList);
            }
        }
        timeQuery.eq("tb_status", "正常").eq("book_check", BookCheckEnum.上架.toString()).orderByDesc("create_time").last("limit " + max);
        List<Book> list = bookService.list(timeQuery);
        map.put("newBook", list);
        readQuery.eq("tb_status", "正常").eq("book_check", BookCheckEnum.上架.toString()).orderByAsc("- browse").last("limit " + max);
        List<Book> readList = bookService.list(readQuery);
        map.put("readBook", readList);
        collectQueryWrapper.groupBy("book_id").orderByDesc("sum").last("limit " + max);
        List<Collect> collectList = collectService.list(collectQueryWrapper);
        List<Integer> collect = collectList.stream().map(Collect::getBookId).collect(Collectors.toList());
        if (collect.size() > 0){
            Collection<Book> books = bookService.listByIds(collect);
            map.put("collectBook", books);
        }
        return Msg.success().add("data",map);
    }
}
