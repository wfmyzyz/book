package com.wfmyzyz.book.controller.api;


import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.Collect;
import com.wfmyzyz.book.domain.Likes;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.service.ICollectService;
import com.wfmyzyz.book.utils.Msg;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private IBookService bookService;
    @Autowired
    private ICollectService collectService;

    @ApiOperation(value="用户收藏")
    @PostMapping("book")
    public Msg updateCollect(@RequestParam("bookId") Integer bookId, HttpServletRequest request){
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null){
            return Msg.error().add("error","请登录！");
        }
        Integer userId = Integer.parseInt(userIdObj.toString());
        if (bookId == null || userId == null){
            return Msg.error().add("error","参数为空");
        }
        Book book = bookService.getById(bookId);
        if (book == null){
            return Msg.error().add("error","书籍不存在");
        }
        Map<String,Integer> map = new HashMap<>();
        Collect collect = collectService.getCollectByUserIdAndBookId(bookId, userId);
        if (collect == null){
            collect = new Collect();
            collect.setBookId(bookId);
            collect.setUserId(userId);
            map.put("type",1);
        }else {
            collect.setTbStatus("删除");
            map.put("type",0);
        }
        collectService.saveOrUpdate(collect);
        return Msg.success().add("data",map);
    }

}

