package com.wfmyzyz.book.controller.back.administrators.role;


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
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("back/admin/administrators/role")
public class AdminRoleController {

    @Autowired
    private IAdminRoleService adminRoleServiceImpl;
    @Autowired
    IAdminService adminServiceImpl;

    /**
     * 查询管理员角色
     * @param page
     * @param limit
     * @param adminRoleId
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getAdminRoleList")
    public LayuiBackData getAdminRoleList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam(value = "adminRoleId",required = false) String adminRoleId, @RequestParam(value = "name",required = false) String name,
                                          @RequestParam(value = "startTime",required = false) String startTime, @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
        if (adminRoleId != null && !adminRoleId.isEmpty()){
            queryWrapper.eq("admin_role_id",adminRoleId);
        }
        if (name != null && !name.isEmpty()){
            queryWrapper.like("name",name);
        }
        if (startTime !=null && endTime != null &&!startTime.isEmpty()&&!endTime.isEmpty()){
            queryWrapper.between("create_time",startTime,endTime);
        }
        queryWrapper.eq("tb_status","正常");
        IPage<AdminRole> pageList = adminRoleServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }

    /**
     * 查询管理员角色是否存在
     * @param roleName
     * @return
     */
    @RequestMapping(value = "getIsAdminRole")
    public Msg getIsAdminRole(@RequestParam("roleName") String roleName){
        boolean flag = isAdminRole(roleName);
        if (flag){
            return Msg.success().add("is","true");
        }
        return Msg.success().add("is","false");
    }

    /**
     * 添加管理员角色
     * @param adminRole
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid AdminRole adminRole, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        if (isAdminRole(adminRole.getName())){
            return Msg.error().add("error","该角色名已存在！");
        }
        boolean flag = adminRoleServiceImpl.save(adminRole);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
     * 修改管理员角色
     * @param adminRole
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid AdminRole adminRole, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        if (isAdminRole(adminRole.getName())){
            return Msg.error().add("error","该角色名已存在！");
        }
        AdminRole oldAdminRole = adminRoleServiceImpl.getById(adminRole.getAdminRoleId());
        if (isAdmin(oldAdminRole.getName())){
            return Msg.error().add("error","修改失败！该角色下存在绑定的管理员");
        }
        boolean flag = adminRoleServiceImpl.updateById(adminRole);
        if (!flag){
            return Msg.error().add("error","修改失败！请重新修改");
        }
        return Msg.success().add("data","修改成功！");
    }

    /**
     * 删除单个管理员角色
     * @param id
     * @return
     */
    @Transactional
    @RequestMapping(value = "remove/{id}",method = RequestMethod.GET)
    public Msg remove(@PathVariable("id") String id){
        AdminRole adminRole = adminRoleServiceImpl.getById(id);
        if (isAdmin(adminRole.getName())){
            return Msg.error().add("error","删除失败！该角色下存在绑定的管理员");
        }
        adminRole.setTbStatus("删除");
        boolean flag = adminRoleServiceImpl.updateById(adminRole);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
     * 批量删除角色
     * @param adminRoleList
     * @return
     */
    @Transactional
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<AdminRole> adminRoleList){
        List<AdminRole> newAdminRole = new ArrayList<>();
        boolean isAdmin = false;
        for (AdminRole adminRole:adminRoleList){
            if (isAdmin(adminRole.getName())){
                isAdmin = true;
            }else {
                adminRole.setTbStatus("删除");
                newAdminRole.add(adminRole);
            }
        }
        boolean flag = adminRoleServiceImpl.updateBatchById(newAdminRole);
        if (flag){
            if (isAdmin){
                return Msg.success().add("data","删除成功！删除列表中存在绑定管理员，绑定管理员的不可删除");
            }
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
        AdminRole adminRole = adminRoleServiceImpl.getById(id);
        if(adminRole == null){
            return Msg.error().add("error","没有该角色！");
        }
        return Msg.success().add("data",adminRole);
    }

    /**
     * 判断角色是否已经存在
     * @param roleName
     * @return
     */
    private boolean isAdminRole(String roleName) {
        QueryWrapper<AdminRole> condition = new QueryWrapper<>();
        condition.eq("name", roleName);
        condition.eq("tb_status","正常");
        AdminRole adminRole = adminRoleServiceImpl.getOne(condition);
        if (adminRole != null){
            return true;
        }
        return false;
    }

    /**
     * 判断角色是否绑定管理员
     * @param roleName
     * @return
     */
    private boolean isAdmin(String roleName){
        List<Admin> adminList = adminServiceImpl.list(new QueryWrapper<Admin>().eq("role",roleName));
        if (adminList.size() > 0){
            return true;
        }
        return false;
    }

}

