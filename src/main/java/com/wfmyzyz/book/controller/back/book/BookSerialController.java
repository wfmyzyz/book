package com.wfmyzyz.book.controller.back.book;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookLabel;
import com.wfmyzyz.book.domain.BookSerial;
import com.wfmyzyz.book.domain.enums.BookSerialCheckEnum;
import com.wfmyzyz.book.service.IBookSerialService;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import com.wfmyzyz.book.vo.BookSerialAboutVo;
import com.wfmyzyz.book.vo.SerialVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-11-20
 */
@RestController
@RequestMapping("back/admin/book/bookSerial")
public class BookSerialController {

    @Autowired
    IBookSerialService bookSerialService;
    @Autowired
    IBookService bookService;

    /**
     * 按分页条件获取章回
     * @param page
     * @param limit
     * @param serialId
     * @param bookId
     * @param title
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getSerialList",method = RequestMethod.GET)
    public LayuiBackData getSerialList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam(value = "serialId",required = false) String serialId,
                                        @RequestParam(value = "bookId") Integer bookId, @RequestParam(value = "title",required = false) String title,
                                        @RequestParam(value = "startTime",required = false) String startTime, @RequestParam(value = "endTime",required = false) String endTime){
        if (bookId == null){
            return LayuiBackData.error();
        }
        Book book = bookService.getById(bookId);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (serialId != null && !serialId.isEmpty()){
            queryWrapper.eq("serial_id",serialId);
        }
        if (title != null && !title.isEmpty()){
            queryWrapper.like("title",title);
        }
        if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()){
            queryWrapper.between("create_time",startTime,endTime);
        }
        queryWrapper.eq("book_id",bookId);
        queryWrapper.eq("tb_status","正常");
        IPage<BookSerial> pageList = bookSerialService.page(new Page<>(page,limit),queryWrapper);
        List<BookSerial> bookSerialList = pageList.getRecords();
        List<SerialVo> serialVoList = new ArrayList<>();
        for (BookSerial bookSerial:bookSerialList){
            SerialVo serialVo = new SerialVo();
            BeanUtils.copyProperties(bookSerial,serialVo);
            serialVo.setBookName(book.getName());
            serialVoList.add(serialVo);
        }
        IPage<SerialVo> serialPageList = new Page();
        serialPageList.setCurrent(pageList.getCurrent());
        serialPageList.setPages(pageList.getPages());
        serialPageList.setSize(pageList.getSize());
        serialPageList.setTotal(pageList.getTotal());
        serialPageList.setRecords(serialVoList);
        return  LayuiBackData.bringData(serialPageList);
    }

    /**
     * 审核章回状态
     * @param id
     * @return
     */
    @RequestMapping("updateSerialCheck/{id}")
    public Msg updateSerialCheck(@PathVariable("id") Integer id){
        BookSerial bookSerial = bookSerialService.getById(id);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("serial_id",id);
        if(Objects.equals(BookSerialCheckEnum.通过.toString(),bookSerial.getSerialCheck())){
            updateWrapper.set("serial_check",BookSerialCheckEnum.未通过.toString());
        }else {
            updateWrapper.set("serial_check",BookSerialCheckEnum.通过.toString());
        }
        boolean update = bookSerialService.update(updateWrapper);
        if (!update){
            return Msg.error().add("error","修改失败！");
        }
        return Msg.success().add("success","修改成功！");
    }

    /**
     * 查询书籍相关信息
     * @param id
     * @return
     */
    @RequestMapping("getAddBookAbout/{id}")
    public Msg getAddBookAbout(@PathVariable("id") Integer id){
        BookSerialAboutVo bookSerialAboutVo = new BookSerialAboutVo();
        Book book = bookService.getById(id);
        bookSerialAboutVo.setName(book.getName());
        bookSerialAboutVo.setSerialNum(book.getSerialNum()+1);
        return Msg.success().add("bookAbout",bookSerialAboutVo);
    }

    /**
     * 根据ID获取章回
     * @param id
     * @return
     */
    @GetMapping("get/{id}")
    public Msg getSerial(@PathVariable("id") Integer id){
        BookSerial serial = bookSerialService.getById(id);
        SerialVo serialVo = new SerialVo();
        if (serial != null){
            Book book = bookService.getById(serial.getBookId());
            BeanUtils.copyProperties(serial,serialVo);
            if (book != null){
                serialVo.setBookName(book.getName());
            }
        }
        return Msg.success().add("data",serialVo);
    }

    /**
     * 添加章回数
     * @param bookSerial
     * @param bindingResult
     * @return
     */
    @Transactional
    @RequestMapping("addSerial")
    public Msg addSerial(@Valid BookSerial bookSerial, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean save = bookSerialService.save(bookSerial);
        if (!save){
            return Msg.error().add("error","章回添加错误！");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id",bookSerial.getBookId());
        queryWrapper.eq("tb_status","正常");
        int count = bookSerialService.count(queryWrapper);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("serial_num",count);
        updateWrapper.eq("book_id",bookSerial.getBookId());
        bookService.update(updateWrapper);
        return Msg.success().add("success","章回添加成功！");
    }

    /**
     * 修改章回
     * @param bookSerial
     * @return
     */
    @PostMapping("update")
    public Msg update(BookSerial bookSerial) {
        if (StringUtils.isBlank(bookSerial.getTitle()) || StringUtils.isBlank(bookSerial.getText())){
            return Msg.error().add("data","章回名与文章不能为空！");
        }
        boolean flag = bookSerialService.updateById(bookSerial);
        if (!flag){
            return Msg.error().add("data","修改失败！");
        }
        return Msg.success().add("data","修改成功！");
    }

    /**
     * 根据ID删除章回
     * @param id
     * @return
     */
    @Transactional
    @GetMapping("remove/{id}")
    public Msg delete(@PathVariable("id") Integer id){
        Integer bookId = bookSerialService.removeByIdBring(id);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("book_id",bookId);
        updateWrapper.setSql("serial_num = serial_num-1");
        bookService.update(updateWrapper);
        return Msg.success().add("data","删除成功！");
    }

    /**
     * 根据ID批量删除书籍
     * @param bookSerialList
     * @return
     */
    @Transactional
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<BookSerial> bookSerialList){
        if (bookSerialList.size() <=0 ){
            return Msg.success().add("success","删除成功！");
        }
        Integer bookId = 0;
        for (BookSerial bookSerial:bookSerialList){
            bookId = bookSerialService.removeByIdBring(bookSerial.getSerialId());
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("book_id",bookId);
        updateWrapper.setSql("serial_num = serial_num-1");
        bookService.update(updateWrapper);
        return Msg.success().add("success","删除成功！");
    }

}

