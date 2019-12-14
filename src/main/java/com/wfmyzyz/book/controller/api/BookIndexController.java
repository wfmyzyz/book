package com.wfmyzyz.book.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.*;
import com.wfmyzyz.book.domain.enums.BookCheckEnum;
import com.wfmyzyz.book.domain.enums.BookEnum;
import com.wfmyzyz.book.domain.enums.BookSerialCheckEnum;
import com.wfmyzyz.book.domain.enums.BookStatusEnum;
import com.wfmyzyz.book.service.*;
import com.wfmyzyz.book.utils.Msg;
import com.wfmyzyz.book.vo.BookAboutVo;
import com.wfmyzyz.book.vo.BookListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@RestController
@RequestMapping("index")
public class BookIndexController {

    @Autowired
    IBookService bookService;
    @Autowired
    ILabelService labelService;
    @Autowired
    IBookLabelService bookLabelService;
    @Autowired
    IBookSerialService bookSerialService;
    @Autowired
    IRotationService rotationService;

    /**
     * 书籍列表
     * @return
     */
    @GetMapping("list")
    public Msg getBookList(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Book> hostQueryWrapper = new QueryWrapper<>();
        hostQueryWrapper.orderByDesc("browse");
        hostQueryWrapper.eq("book_check", BookCheckEnum.上架.toString());
        hostQueryWrapper.eq("tb_status","正常");
        IPage<Book> page = bookService.page(new Page<>(0, 10), hostQueryWrapper);
        List<Book> bookList = page.getRecords();
        List<BookListVo> bookListVoList = new ArrayList<>();
        setBookLabel(bookList, bookListVoList);
        map.put("host",bookListVoList);

        QueryWrapper<Book> newQueryWrapper = new QueryWrapper<>();
        newQueryWrapper.orderByDesc("create_time");
        newQueryWrapper.eq("book_check", BookCheckEnum.上架.toString());
        newQueryWrapper.eq("tb_status","正常");
        IPage<Book> newPage = bookService.page(new Page<>(0, 10), newQueryWrapper);
        List<Book> newBookList = newPage.getRecords();
        List<BookListVo> newBookListVoList = new ArrayList<>();
        setBookLabel(newBookList, newBookListVoList);
        map.put("new",newBookListVoList);

        QueryWrapper<Book> overQueryWrapper = new QueryWrapper<>();
        overQueryWrapper.orderByDesc("update_time");
        overQueryWrapper.eq("book_check", BookCheckEnum.上架.toString());
        overQueryWrapper.eq("tb_status","正常");
        overQueryWrapper.eq("book_status", BookStatusEnum.完结.toString());
        IPage<Book> overPage = bookService.page(new Page<>(0, 10), overQueryWrapper);
        List<Book> overBookList = overPage.getRecords();
        List<BookListVo> overBookListVoList = new ArrayList<>();
        setBookLabel(overBookList, overBookListVoList);
        map.put("over",overBookListVoList);

        QueryWrapper<Book> praiseQueryWrapper = new QueryWrapper<>();
        praiseQueryWrapper.orderByDesc("praise");
        praiseQueryWrapper.eq("book_check", BookCheckEnum.上架.toString());
        praiseQueryWrapper.eq("tb_status","正常");
        IPage<Book> praisePage = bookService.page(new Page<>(0, 10), praiseQueryWrapper);
        List<Book> praiseBookList = praisePage.getRecords();
        List<BookListVo> praiseBookListVoList = new ArrayList<>();
        setBookLabel(praiseBookList, praiseBookListVoList);
        map.put("praise",praiseBookListVoList);
        return Msg.success().add("data",map);
    }

    /**
     * 设置书籍标签
     * @param bookList
     * @param bookListVoList
     */
    private void setBookLabel(List<Book> bookList, List<BookListVo> bookListVoList) {
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
    }

    /**
     * 根据ID获取正本书详情
     * @param id
     * @return
     */
    @GetMapping("getBook/{id}")
    public Msg getBookAboutByBookId(@PathVariable("id") Integer id){
        Book book = bookService.getById(id);
        if (book == null || Objects.equals(book.getBookCheck(),BookCheckEnum.下架.toString()) || Objects.equals(book.getTbStatus(),"删除")){
            return Msg.error().add("error","该书不存在！");
        }
        List<BookLabel> bookLabelList = bookLabelService.list(new QueryWrapper<BookLabel>().eq("book_id", book.getBookId()));
        List<Label> labelList = new ArrayList<>();
        if (bookLabelList != null && bookLabelList.size() > 0){
            List<Integer> labelIdList = bookLabelList.stream().map(BookLabel::getLabelId).collect(Collectors.toList());
            labelList = labelService.list(new QueryWrapper<Label>().in("label_id", labelIdList));
        }
        BookAboutVo bookAboutVo = new BookAboutVo();
        bookAboutVo.setBook(book);
        bookAboutVo.setLabelList(labelList);
        return Msg.success().add("data",bookAboutVo);
    }

    /**
     * 获取书籍章回
     * @param id
     * @return
     */
    @RequestMapping("bookSerialList/{id}")
    public Msg getBookSerial(@PathVariable("id") Integer id, @RequestParam("page") Integer page,
                             @RequestParam("limit") Integer limit){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id",id);
        queryWrapper.eq("tb_status","正常");
        queryWrapper.eq("serial_check", BookSerialCheckEnum.通过.toString());
        queryWrapper.orderByAsc("serial_num");
        IPage<BookSerial> recordList = bookSerialService.page(new Page<>(page,limit),queryWrapper);

        return Msg.success().add("data",recordList);
    }

    /**
     * 获取书籍章回
     * @param bookId
     * @param serialId
     * @return
     */
    @RequestMapping("getBookSerialText/{bookId}/{serialId}")
    public Msg getBookSerialText(@PathVariable("bookId") Integer bookId,@PathVariable("serialId") Integer serialId){
        Book book = bookService.getById(bookId);
        if (book == null || Objects.equals(book.getTbStatus(),"删除") || Objects.equals(book.getBookCheck(),BookCheckEnum.下架.toString())){
            return Msg.error().add("error","没有该书！");
        }
        BookSerial bookSerial = bookSerialService.getById(serialId);
        if (bookSerial == null || Objects.equals(bookSerial.getTbStatus(),"删除") || Objects.equals(bookSerial.getSerialCheck(),BookSerialCheckEnum.未通过.toString())){
            return Msg.error().add("error","没有该章回！");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("book",book);
        map.put("bookSerial",bookSerial);
        return Msg.success().add("data",map);
    }

    /**
     * 获取所有标签
     * @return
     */
    @GetMapping("getAllLabel")
    public Msg getAllLabel(){
        List<Label> list = labelService.list(new QueryWrapper<Label>().eq("tb_status", "正常"));
        return Msg.success().add("data",list);
    }

    /**
     * 获取所有轮播图
     * @return
     */
    @GetMapping("getRotationList")
    public Msg getRotationList(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("tb_status","正常");
        queryWrapper.orderByDesc("num");
        List<Rotation> rotationList = rotationService.list(queryWrapper);
        return Msg.success().add("data",rotationList);
    }
}
