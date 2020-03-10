package com.wfmyzyz.book.controller.back.book;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookLabel;
import com.wfmyzyz.book.domain.Label;
import com.wfmyzyz.book.service.impl.BookLabelServiceImpl;
import com.wfmyzyz.book.service.impl.BookServiceImpl;
import com.wfmyzyz.book.service.impl.LabelServiceImpl;
import com.wfmyzyz.book.utils.Msg;
import io.swagger.annotations.ApiOperation;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-09-12
 */
@RestController
@RequestMapping("back/admin/book/bookLabel")
public class BookLabelController {

    @Autowired
    private BookLabelServiceImpl bookLabelServiceImpl;
    @Autowired
    private LabelServiceImpl labelServiceImpl;

    /**
     * 获取书籍绑定标签
     * @param id
     * @return
     */
    @ApiOperation(value="获取书籍绑定标签")
    @RequestMapping(value = "getBookLabel/{id}",method = RequestMethod.GET)
    public Msg getBookLabel(@PathVariable("id") Integer id){
        List<BookLabel> bookBindLabelList = bookLabelServiceImpl.list(new QueryWrapper<BookLabel>().eq("book_id", id).eq("tb_status","正常"));
        if (bookBindLabelList.size() == 0){
            return Msg.success().add("data","");
        }
        String labelIds = "";
        for (BookLabel bookBindLabel:bookBindLabelList){
            labelIds += bookBindLabel.getLabelId()+",";
        }
        labelIds = labelIds.substring(0,labelIds.length()-1);
        List<Label> bookLabelList = labelServiceImpl.list(new QueryWrapper<Label>().inSql("label_id", labelIds).eq("tb_status","正常"));
        return Msg.success().add("data",bookLabelList);
    }

    /**
     * 获取书籍非绑定标签
     * @param id
     * @return
     */
    @ApiOperation(value="获取书籍非绑定标签")
    @RequestMapping(value = "getNoBookLabel/{id}",method = RequestMethod.GET)
    public Msg getNoBookBindLabel(@PathVariable("id") Integer id){
        List<BookLabel> bookBindLabelList = bookLabelServiceImpl.list(new QueryWrapper<BookLabel>().eq("book_id", id).eq("tb_status","正常"));
        if (bookBindLabelList.size()==0){
            List<Label> blogArticleLabelList = labelServiceImpl.list(new QueryWrapper<Label>().eq("tb_status","正常"));
            return Msg.success().add("data",blogArticleLabelList);
        }
        String labelIds = "";
        for (BookLabel bookBindLabel:bookBindLabelList){
            labelIds += bookBindLabel.getLabelId()+",";
        }
        labelIds = labelIds.substring(0,labelIds.length()-1);
        List<Label> bookLabelList = labelServiceImpl.list(new QueryWrapper<Label>().notInSql("label_id", labelIds).eq("tb_status","正常"));
        return Msg.success().add("data",bookLabelList);
    }


}

