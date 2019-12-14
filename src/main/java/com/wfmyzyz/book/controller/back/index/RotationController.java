package com.wfmyzyz.book.controller.back.index;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.AdminRole;
import com.wfmyzyz.book.domain.Rotation;
import com.wfmyzyz.book.service.IRotationService;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/back/index/rotation")
public class RotationController {

    @Autowired
    IRotationService rotationService;

    /**
     * 分页查询轮播图
     * @param page
     * @param limit
     * @param title
     * @param url
     * @return
     */
    @RequestMapping("/getRotationList")
    public LayuiBackData getRotationList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,@RequestParam(value = "title",required = false) String title,
                                         @RequestParam(value = "url",required = false) String url){
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(title)){
            queryWrapper.like("title",title);
        }
        if (StringUtils.isNotBlank(url)){
            queryWrapper.like("url",url);
        }
        queryWrapper.eq("tb_status","正常");
        IPage pageList = rotationService.page(new Page<>(page, limit), queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }

    /**
     * 添加轮播图
     * @param rotation
     * @return
     */
    @RequestMapping("add")
    public Msg add(@Valid Rotation rotation, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean save = rotationService.save(rotation);
        if (!save){
            return Msg.error();
        }
        return Msg.success().add("data","添加成功");
    }

    /**
     * 修改轮播图
     * @param rotation
     * @param bindingResult
     * @return
     */
    @RequestMapping("update")
    public Msg update(@Valid Rotation rotation,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = rotationService.updateById(rotation);
        if (!flag){
            return Msg.error();
        }
        return Msg.success().add("data","修改成功");
    }

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    @RequestMapping("get/{id}")
    public Msg get(@PathVariable("id") Integer id){
        Rotation rotation = rotationService.getById(id);
        return Msg.success().add("data",rotation);
    }

    /**
     * 删除单个轮播图
     * @param id
     * @return
     */
    @GetMapping(value = "remove/{id}")
    public Msg remove(@PathVariable("id") String id){
        Rotation rotation = rotationService.getById(id);
        rotation.setTbStatus("删除");
        boolean flag = rotationService.updateById(rotation);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
     * 批量轮播图
     * @param rotationList
     * @return
     */
    @PostMapping(value = "remove")
    public Msg removeBatch(@RequestBody List<Rotation> rotationList){
        rotationList.forEach(e->e.setTbStatus("删除"));
        boolean flag = rotationService.updateBatchById(rotationList);
        if (flag){
            return Msg.success().add("data","删除成功！");
        }
        return Msg.error().add("error","删除失败！");
    }
}

