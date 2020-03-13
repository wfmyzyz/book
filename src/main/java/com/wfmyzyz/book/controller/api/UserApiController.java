package com.wfmyzyz.book.controller.api;


import com.wfmyzyz.book.domain.User;
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
import java.util.Objects;

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
        return Msg.success();
    }

}

