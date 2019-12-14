package com.wfmyzyz.book.controller.back.book;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookLabel;
import com.wfmyzyz.book.domain.BookSerial;
import com.wfmyzyz.book.domain.Label;
import com.wfmyzyz.book.domain.enums.BookCheckEnum;
import com.wfmyzyz.book.domain.enums.BookEnum;
import com.wfmyzyz.book.service.IBookSerialService;
import com.wfmyzyz.book.service.impl.BookLabelServiceImpl;
import com.wfmyzyz.book.service.impl.BookServiceImpl;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-10-08
 */
@RestController
@RequestMapping("back/admin/book/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookServiceImpl;
    @Autowired
    private BookLabelServiceImpl bookLabelServiceImpl;
    @Autowired
    private IBookSerialService bookSerialService;

    /**
     * 按分页条件获取书籍
     * @param page
     * @param limit
     * @param bookId
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getBookList",method = RequestMethod.GET)
    public LayuiBackData getArticleList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam(value = "bookId",required = false) String bookId,
                                        @RequestParam(value = "title",required = false) String name, @RequestParam(value = "startTime",required = false) String startTime,
                                        @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
        if (bookId != null && !bookId.isEmpty()){
            queryWrapper.eq("book_id",bookId);
        }
        if (name != null && !name.isEmpty()){
            queryWrapper.like("name",name);
        }
        if (startTime !=null && endTime != null &&!startTime.isEmpty()&&!endTime.isEmpty()){
            queryWrapper.between("create_time",startTime,endTime);
        }
        queryWrapper.eq("tb_status","正常");
        IPage<Book> pageList = bookServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }

    /**
     * 添加书籍
     * @param book
     * @param bindingResult
     * @param labelIds
     * @return
     */
    @Transactional
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid Book book, BindingResult bindingResult, String labelIds, HttpServletRequest request){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        //书籍可重名
        /*if (isBook(book.getName()) != null){
            return Msg.error().add("error","书籍名称已存在！");
        }*/
        book.setPubliser("admin");
        if (StringUtils.isBlank(book.getAuthor())){
            book.setAuthor(book.getPubliser());
        }
        if (Objects.equals(book.getBookType(), BookEnum.章回.toString())){
            book.setBookStatus("连载");
        }else {
            book.setBookStatus("完结");
        }
        book.setSerialNum(0);
        boolean flag = bookServiceImpl.save(book);
        if (!flag){
            return Msg.error().add("error","书籍添加错误！");
        }
        List<BookLabel> bookBindLabels = new ArrayList<>();
        if (!labelIds.isEmpty()){
            String[] labelIdArr = labelIds.split(",");
            for (String labelId:labelIdArr){
                BookLabel bookBindLabel = new BookLabel();
                bookBindLabel.setBookId(book.getBookId());
                bookBindLabel.setLabelId(Integer.parseInt(labelId));
                bookBindLabels.add(bookBindLabel);
            }
            bookLabelServiceImpl.saveBatch(bookBindLabels);
        }
        return Msg.success().add("success","书籍添加成功！");
    }

    /**
     * 修改书籍
     * @param book
     * @param bindingResult
     * @param labelIds
     * @return
     */
    @Transactional
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid Book book, BindingResult bindingResult, String labelIds, HttpServletRequest request){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        /*if (isBook(book.getName()) != null){
            Book repeatBook = isBook(book.getName());
            if (!Objects.equals(repeatBook.getBookId(),book.getBookId())){
                return Msg.error().add("error","书籍名称已存在！");
            }
        }*/
        boolean flag = bookServiceImpl.updateById(book);
        if (!flag){
            return Msg.error().add("error","修改失败！请重新修改");
        }
        List<String> labelIdList = Arrays.asList(labelIds.split(","));
        List<BookLabel> addBlogBookBindLabelList = new ArrayList<>();
        List<BookLabel> blogArticleBindLabelList = bookLabelServiceImpl.list(new QueryWrapper<BookLabel>().eq("book_id", book.getBookId()));
        List<Integer> deleteIdList = new ArrayList<>();
        for (BookLabel blogArticleBindLabel:blogArticleBindLabelList){
            deleteIdList.add(blogArticleBindLabel.getBookLabelId());
        }
        if (deleteIdList != null && deleteIdList.size() > 0){
            bookLabelServiceImpl.removeByIds(deleteIdList);
        }
        if (StringUtils.isNotBlank(labelIds)){
            for (String labelId:labelIdList){
                BookLabel blogBookBindLabel = new BookLabel();
                blogBookBindLabel.setBookId(book.getBookId());
                blogBookBindLabel.setLabelId(Integer.parseInt(labelId));
                addBlogBookBindLabelList.add(blogBookBindLabel);
            }
        }
        bookLabelServiceImpl.saveBatch(addBlogBookBindLabelList);
        return Msg.success().add("success","修改成功！");
    }

    /**
     * 根据书籍ID删除书籍
     * @param id
     * @return
     */
    @Transactional
    @RequestMapping(value = "remove/{id}",method = RequestMethod.GET)
    public Msg remove(@PathVariable("id") Integer id){
        Book book = bookServiceImpl.getById(id);
        book.setTbStatus("删除");
        boolean flag = bookServiceImpl.update(new UpdateWrapper<Book>().set("tb_status", "删除").eq("book_id", id));
        if (!flag){
            return Msg.error().add("error","删除失败！请重新删除");
        }
        List<BookLabel> bookBindLabellist = bookLabelServiceImpl.list(new QueryWrapper<BookLabel>().eq("book_id", id).eq("tb_status", "正常"));
        List<Integer> deleteIdList = new ArrayList<>();
        for (BookLabel bookBindLabel:bookBindLabellist){
            deleteIdList.add(bookBindLabel.getBookLabelId());
        }
        if (deleteIdList.size() > 0){
            bookLabelServiceImpl.removeByIds(deleteIdList);
        }
        UpdateWrapper<BookSerial> serialUpdateWrapper = new UpdateWrapper<>();
        serialUpdateWrapper.set("tb_status","删除");
        serialUpdateWrapper.eq("book_id",id);
        bookSerialService.update(serialUpdateWrapper);
        return Msg.success().add("success","删除成功！");
    }

    /**
     * 根据ID批量删除书籍
     * @param bookList
     * @return
     */
    @Transactional
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<Book> bookList){
        List<Integer> bookIdList = new ArrayList<>();
        for (Book book:bookList){
            bookIdList.add(book.getBookId());
        }
        boolean flag = bookServiceImpl.update(new UpdateWrapper<Book>().set("tb_status", "删除").in("book_id", bookIdList).eq("tb_status","正常"));
        if (flag){
            bookLabelServiceImpl.remove(new QueryWrapper<BookLabel>().in("book_id",bookIdList));
            UpdateWrapper<BookSerial> serialUpdateWrapper = new UpdateWrapper<>();
            serialUpdateWrapper.set("tb_status","删除");
            serialUpdateWrapper.in("book_id",bookIdList);
            bookSerialService.update(serialUpdateWrapper);
            return Msg.success().add("success","删除成功！");
        }
        return Msg.error().add("error","删除失败！请重新删除");
    }

    /**
     * 获取一本书籍数据
     * @param id
     * @return
     */
    @RequestMapping(value = "get/{id}",method = RequestMethod.GET)
    public Msg getBook(@PathVariable("id") String id){
        Book book = bookServiceImpl.getById(id);
        if(book == null){
            return Msg.error().add("error","没有该书籍！");
        }
        return Msg.success().add("data",book);
    }

    /**
     * 判断书籍名是否存在
     * @return
     */
    @RequestMapping(value = "getIsBook",method = RequestMethod.GET)
    public Msg getIsBook(@RequestParam("bookName") String bookName){
        if (isBook(bookName) == null){
            return Msg.success();
        }
        return Msg.error().add("error","书籍名已存在！");
    }

    /**
     * 判断书籍名是否已经存在
     * @param bookName
     * @return
     */
    private Book isBook(String bookName) {
        Book book = bookServiceImpl.getOne(new QueryWrapper<Book>().eq("name",bookName).eq("tb_status","正常"));
        return book;
    }

    /**
     * 修改书籍审核
     * @param id
     * @return
     */
    @GetMapping("updateBookCheck/{id}")
    public Msg updateBookCheck(@PathVariable("id") Integer id){
        Book book = bookServiceImpl.getById(id);
        if (Objects.equals(book.getBookCheck(), BookCheckEnum.上架.toString())){
            book.setBookCheck(BookCheckEnum.下架.toString());
        }else {
            book.setBookCheck(BookCheckEnum.上架.toString());
        }
        boolean flag = bookServiceImpl.updateById(book);
        if (!flag){
            return Msg.error().add("error","修改失败！");
        }
        return Msg.success().add("data","修改成功！");
    }

}

