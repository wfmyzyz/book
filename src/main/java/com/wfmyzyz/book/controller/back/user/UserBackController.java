package com.wfmyzyz.book.controller.back.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.User;
import com.wfmyzyz.book.service.IUserService;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
@RestController
@RequestMapping("back/user")
public class UserBackController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value="分页获取用户列表")
    @RequestMapping("/getUserList")
    public LayuiBackData getUserList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,@RequestParam(value = "username",required = false) String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.eq("username",username);
        }
        IPage<User> pageList = userService.page(new Page<>(page, limit), queryWrapper);
        return LayuiBackData.bringData(pageList);
    }

    @ApiOperation(value="新增用户")
    @RequestMapping("/add")
    public Msg add(@RequestParam("username") String username,@RequestParam("password") String password){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return Msg.error().add("error","参数为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        queryWrapper.last("limit 1");
        User one = userService.getOne(queryWrapper);
        if (one != null){
            return Msg.error().add("error","用户已存在");
        }
        one = new User();
        one.setUsername(username);
        one.setPassword(password);
        boolean save = userService.save(one);
        if (!save){
            return Msg.error().add("error","添加失败");
        }
        return Msg.success().add("data","添加成功");
    }

    @ApiOperation(value="修改密码")
    @RequestMapping("/updatePassword")
    public Msg updatePassword(@RequestParam("userId") String userId,@RequestParam("password") String password){
        User user = userService.getById(userId);
        if (user == null){
            return Msg.error().add("error","用户不存在");
        }
        user.setPassword(password);
        boolean flag = userService.updateById(user);
        if (!flag){
            return Msg.error().add("error","修改失败");
        }
        return Msg.success().add("data","修改成功");
    }

    @ApiOperation(value="获取用户信息")
    @RequestMapping("/get/{id}")
    public Msg get(@PathVariable("id") String id){
        User user = userService.getById(id);
        if (user == null){
            return Msg.error().add("error","用户不存在");
        }
        return Msg.success().add("data",user);
    }

    @ApiOperation(value="删除用户")
    @RequestMapping("/remove")
    public Msg remove(@RequestBody List<Integer> ids){
        if (ids.size() <= 0){
            return Msg.success();
        }
        boolean flag = userService.removeByIds(ids);
        if (!flag){
            return Msg.error();
        }
        return Msg.success();
    }

}

