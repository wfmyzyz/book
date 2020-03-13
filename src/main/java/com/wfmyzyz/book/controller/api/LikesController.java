package com.wfmyzyz.book.controller.api;


import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.Likes;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.service.ILikesService;
import com.wfmyzyz.book.service.IUserService;
import com.wfmyzyz.book.utils.Msg;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/likes")
public class LikesController {

    @Autowired
    private ILikesService likesService;
    @Autowired
    private IBookService bookService;

    @ApiOperation(value="用户点赞")
    @PostMapping("book")
    public Msg updateLike(@RequestParam("bookId") Integer bookId, HttpServletRequest request){
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
        Likes likesByUserIdAndBookId = likesService.getLikesByUserIdAndBookId(userId, bookId);
        if (likesByUserIdAndBookId == null){
            likesByUserIdAndBookId = new Likes();
            book.setPraise((Integer.parseInt(book.getPraise())+1)+"");
            likesByUserIdAndBookId.setBookId(bookId);
            likesByUserIdAndBookId.setUserId(userId);
        }else {
            likesByUserIdAndBookId.setTbStatus("删除");
            book.setPraise((Integer.parseInt(book.getPraise())-1)+"");
        }
        bookService.updateById(book);
        likesService.saveOrUpdate(likesByUserIdAndBookId);
        return Msg.success();
    }

}

