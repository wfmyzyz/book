package com.wfmyzyz.book.controller.api;


import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.Collect;
import com.wfmyzyz.book.domain.User;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.service.ICollectService;
import com.wfmyzyz.book.service.IUserService;
import com.wfmyzyz.book.utils.Msg;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/user")
public class UserApiController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICollectService collectService;
    @Autowired
    private IBookService bookService;

    @ApiOperation("用户注册")
    @PostMapping("register")
    public Msg register(@RequestParam("username") String username,@RequestParam("password") String password){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return Msg.error().add("error","参数为空");
        }
        User userByUsername = userService.getUserByUsername(username);
        if (userByUsername != null){
            return Msg.error().add("error","用户已存在");
        }
        userByUsername = new User();
        userByUsername.setUsername(username);
        userByUsername.setPassword(password);
        boolean flag = userService.save(userByUsername);
        if (!flag){
            return Msg.error();
        }
        return Msg.success();
    }

    @ApiOperation("用户登录")
    @PostMapping("login")
    public Msg login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return Msg.error().add("error","参数为空");
        }
        User userByUsername = userService.getUserByUsername(username);
        if (userByUsername == null){
            return Msg.error().add("error","用户名或密码不正确");
        }
        if (!Objects.equals(userByUsername.getPassword(),password)){
            return Msg.error().add("error","用户名或密码不正确");
        }
        HttpSession session = request.getSession();
        session.setAttribute("username",username);
        session.setAttribute("userId",userByUsername.getId());
        return Msg.success();
    }

    @ApiOperation("用户修改密码")
    @PostMapping("updatePassword")
    public Msg updatePassword(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("passwords") String passwords){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(passwords)){
            return Msg.error().add("error","参数不能为空");
        }
        User user = userService.getUserByUsername(username);
        if (user == null){
            return Msg.error().add("error","不存在该用户");
        }
        if (!Objects.equals(user.getPassword(),password)){
            return Msg.error().add("error","旧密码不正确");
        }
        user.setPassword(passwords);
        boolean flag = userService.updateById(user);
        if (!flag){
            return Msg.error().add("error","修改失败");
        }
        return Msg.success().add("data","修改成功");
    }

    @ApiOperation("获取用户收藏书籍列表")
    @PostMapping("collectBookList")
    public Msg collectBookList(@RequestParam("username") String username){
        if (StringUtils.isBlank(username)){
            return Msg.error().add("error","参数不能为空");
        }
        User user = userService.getUserByUsername(username);
        if (user == null){
            return Msg.error().add("error","不存在该用户");
        }
        List<Collect> collectList = collectService.getCollectByUserId(user.getId());
        List<Integer> bookIdList = collectList.stream().map(Collect::getBookId).collect(Collectors.toList());
        if (bookIdList == null || bookIdList.size() <= 0){
            return Msg.success().add("data",new ArrayList<>());
        }
        Collection<Book> books = bookService.listByIds(bookIdList);
        return Msg.success().add("data",books);
    }

}

