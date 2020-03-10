package com.wfmyzyz.book.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wfmyzyz.book.config.Config;
import com.wfmyzyz.book.domain.Admin;
import com.wfmyzyz.book.service.IAdminService;
import com.wfmyzyz.book.service.impl.AdminServiceImpl;
import com.wfmyzyz.book.utils.JsonResult;
import com.wfmyzyz.book.utils.Md5Utils;
import com.wfmyzyz.book.utils.RedisUtils;
import com.wfmyzyz.book.utils.RequestTools;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("back")
public class AdminBackLoginController {

    @Autowired
    JsonResult jsonResult;

    @Autowired
    IAdminService adminServiceImpl;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RequestTools requestTools;
    @Autowired
    Md5Utils md5Utils;
    @Autowired
    RedisUtils redisUtils;

    /**
     * 后台用户验证登录
     * @param adminname
     * @param password
     * @param rememberMe
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value="用户登录", notes="用户登录" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="adminname",value="adminname",required=true,paramType="String"),
            @ApiImplicitParam(name="password",value="password",required=true,paramType="String"),
            @ApiImplicitParam(name="vrifyCode",value="vrifyCode",required=true,paramType="String"),
            @ApiImplicitParam(name="rememberMe",value="rememberMe",required=true,paramType="Boolean")
    })
    @RequestMapping(value = "adminLogin",method = RequestMethod.POST)
    public String adminLogin(@RequestParam("adminname") String adminname, @RequestParam("password") String password, @RequestParam("vrifyCode") String vrifyCode, @RequestParam("rememberMe") boolean rememberMe,
                             HttpServletRequest request, HttpServletResponse response){
        if (adminname.isEmpty()||password.isEmpty()){
            return jsonResult.error(202,"必填字段不能为空！");
        }
        if (request.getSession().getAttribute("vrifyCode") == null){
            return jsonResult.error(202,"验证码已过期，请重新获取！");
        }
        if (!vrifyCode.equals(request.getSession().getAttribute("vrifyCode").toString())){
            return jsonResult.error(202,"验证码不正确！");
        }
        Admin blogAdmin = adminServiceImpl.getOne(new QueryWrapper<Admin>().eq("adminname",adminname));
        if (blogAdmin != null){
            String adminPassword = password+blogAdmin.getSalt();
            String md5Password = DigestUtils.md5DigestAsHex(adminPassword.getBytes());
            if (md5Password.equals(blogAdmin.getPassword())){
                String adminNameMd5 = md5Utils.encrypt16(adminname);
                if (rememberMe){
                    Cookie cookie=new Cookie("adminnameCookie",adminNameMd5);
                    cookie.setMaxAge(604800);
                    cookie.setPath("admin/**");
                    response.addCookie(cookie);
                }else{
                    request.getSession().setAttribute("adminnameSession",adminNameMd5);
                }

                redisUtils.addToRedis(Config.BACK_ADMIN+adminNameMd5,requestTools.getIpAddress(request));

                return jsonResult.ok("登录成功！","登录成功！");
            }
        }
        return jsonResult.error(204,"用户名或密码不正确！");
    }

}
