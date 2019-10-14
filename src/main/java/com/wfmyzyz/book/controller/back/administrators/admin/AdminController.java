package com.wfmyzyz.book.controller.back.administrators.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.Admin;
import com.wfmyzyz.book.domain.AdminRole;
import com.wfmyzyz.book.service.IAdminRoleService;
import com.wfmyzyz.book.service.IAdminService;
import com.wfmyzyz.book.service.impl.AdminRoleServiceImpl;
import com.wfmyzyz.book.service.impl.AdminServiceImpl;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xiong
 * @since 2019-07-09
 */
@RestController
@RequestMapping("back/admin/administrators/admin")
public class AdminController {

    @Autowired
    IAdminService adminServiceImpl;
    @Autowired
    IAdminRoleService adminRoleServiceImpl;

    /**
     * 查询管理员
     * @param page
     * @param limit
     * @param adminId
     * @param adminname
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getAdminList")
    public LayuiBackData getAdminList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam(value = "adminId",required = false) String adminId, @RequestParam(value = "adminname",required = false) String adminname,
                                      @RequestParam(value = "startTime",required = false) String startTime, @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
        if (adminId != null && !adminId.isEmpty()){
            queryWrapper.eq("admin_id",adminId);
        }
        if (adminname != null && !adminname.isEmpty()){
            queryWrapper.like("adminname",adminname);
        }
        if (startTime !=null && endTime != null &&!startTime.isEmpty()&&!endTime.isEmpty()){
            queryWrapper.between("create_time",startTime,endTime);
        }
        queryWrapper.eq("tb_status","正常");
        IPage<AdminRole> pageList = adminServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }

    /**
     * 查询管理员是否存在
     * @param adminname
     * @return
     */
    @RequestMapping(value = "getIsAdmin")
    public Msg getIsAdmin(@RequestParam("adminname") String adminname){
        boolean flag = isAdminLife(adminname);
        if (flag){
            return Msg.success().add("is","true");
        }
        return Msg.success().add("is","false");
    }

    /**
     * 添加管理员
     * @param admin
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid Admin admin, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        if (isAdminLife(admin.getAdminname())){
            return Msg.error().add("error","该管理员已存在！");
        }
        String password = admin.getPassword();
        String salt = String.valueOf(System.currentTimeMillis());
        String adminPassword = password+salt;
        admin.setSalt(salt);
        String md5Password = DigestUtils.md5DigestAsHex(adminPassword.getBytes());
        admin.setPassword(md5Password);
        boolean flag = adminServiceImpl.save(admin);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
     * 修改管理员
     * @param admin
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid Admin admin, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        Admin oldAdmin = adminServiceImpl.getById(admin.getAdminId());
        if (!oldAdmin.getAdminname().equals(admin.getAdminname())){
            return Msg.error().add("error","管理员账号不允许修改！请重新修改");
        }
        if (!oldAdmin.getPassword().equals(admin.getPassword())){
            String salt = String.valueOf(System.currentTimeMillis());
            String adminPassword = admin.getPassword()+salt;
            admin.setSalt(salt);
            String md5Password = DigestUtils.md5DigestAsHex(adminPassword.getBytes());
            admin.setPassword(md5Password);
        }
        boolean flag = adminServiceImpl.updateById(admin);
        if (!flag){
            return Msg.error().add("error","修改失败！请重新修改");
        }
        return Msg.success().add("data","修改成功！");
    }

    /**
     * 删除单个管理员
     * @param id
     * @return
     */
    @RequestMapping(value = "remove/{id}",method = RequestMethod.GET)
    public Msg remove(@PathVariable("id") String id){
        Admin admin = adminServiceImpl.getById(id);
        admin.setTbStatus("删除");
        boolean flag = adminServiceImpl.updateById(admin);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
     * 批量删除管理员
     * @param adminList
     * @return
     */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<Admin> adminList){
        List<Admin> newAdmin = new ArrayList<>();
        for (Admin admin:adminList){
            admin.setTbStatus("删除");
            newAdmin.add(admin);
        }
        boolean flag = adminServiceImpl.updateBatchById(newAdmin);
        if (flag){
            return Msg.success().add("data","删除成功！");
        }
        return Msg.error().add("error","删除失败！");
    }

    /**
     * 获取一条管理员角色数据
     * @param id
     * @return
     */
    @RequestMapping(value = "get/{id}",method = RequestMethod.GET)
    public Msg getOne(@PathVariable("id") String id){
        Admin admin = adminServiceImpl.getById(id);
        if(admin == null){
            return Msg.error().add("error","没有该角色！");
        }
        return Msg.success().add("data",admin);
    }

    /**
     * 判断管理员是否已经存在
     * @param adminName
     * @return
     */
    private boolean isAdminLife(String adminName) {
        QueryWrapper<Admin> condition = new QueryWrapper<>();
        condition.eq("adminname", adminName);
        condition.eq("tb_status","正常");
        Admin admin = adminServiceImpl.getOne(condition);
        if (admin != null){
            return true;
        }
        return false;
    }


}

