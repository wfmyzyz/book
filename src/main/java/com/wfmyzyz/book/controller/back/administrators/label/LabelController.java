package com.wfmyzyz.book.controller.back.administrators.label;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.book.domain.AdminRole;
import com.wfmyzyz.book.domain.BookLabel;
import com.wfmyzyz.book.domain.Label;
import com.wfmyzyz.book.service.IBookLabelService;
import com.wfmyzyz.book.service.ILabelService;
import com.wfmyzyz.book.utils.LayuiBackData;
import com.wfmyzyz.book.utils.Msg;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Miss.Mo
 * @since 2019-09-12
 */
@RestController
@RequestMapping("/back/admin/administrators/label")
public class LabelController {

    @Autowired
    ILabelService labelService;
    @Autowired
    IBookLabelService bookLabelService;

    /**
     * 按分页条件获取所有标签数据
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getList",method = RequestMethod.GET)
    public LayuiBackData getList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam(value = "labelId",required = false) String labelId,
                                 @RequestParam(value = "name",required = false) String name, @RequestParam(value = "startTime",required = false) String startTime,
                                 @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
        if (labelId != null && !labelId.isEmpty()){
            queryWrapper.eq("label_id",labelId);
        }
        if (name != null && !name.isEmpty()){
            queryWrapper.like("name",name);
        }
        if (startTime !=null && endTime != null &&!startTime.isEmpty()&&!endTime.isEmpty()){
            queryWrapper.between("create_time",startTime,endTime);
        }
        queryWrapper.eq("tb_status","正常");
        IPage<Label> pageList = labelService.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }

    /**
     * 获取所有的标签
     * @return
     */
    @RequestMapping(value = "getAllLabel",method = RequestMethod.GET)
    public Msg getAllLabel(){
        List<Label> labelList = labelService.list(new QueryWrapper<Label>().eq("tb_status","正常"));
        if (labelList != null){
            return Msg.success().add("data",labelList);
        }
        return Msg.error().add("error","没有标签数据！");
    }

    /**
     * 添加标签
     * @param label
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg layuiBackData(@Valid Label label, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        if (isLabel(label.getName()) != null){
            return Msg.error().add("error","该标签已存在！");
        }
        boolean flag = labelService.save(label);
        if (!flag){
            return Msg.error().add("data","添加失败！请重新添加");
        }
        return  Msg.success();
    }

    /**
     * 修改标签
     * @param label
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg updateLabel(@Valid Label label, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        if (isBookLabel(label.getLabelId()) != null && isBookLabel(label.getLabelId()).size() > 0){
            return Msg.error().add("error","该标签绑定书籍，不可修改！");
        }
        Label oldLabel = isLabel(label.getName());
        if (oldLabel != null && !oldLabel.getLabelId().equals(label.getLabelId())){
            return Msg.error().add("error","该标签名已存在！");
        }
        boolean flag = labelService.updateById(label);
        if (!flag){
            return Msg.error().add("error","修改失败！请重新修改");
        }
        return Msg.success();
    }

    /**
     * 获取一条标签数据
     * @param id
     * @return
     */
    @RequestMapping(value = "get/{id}",method = RequestMethod.GET)
    public Msg getLabel(@PathVariable("id") String id){
        Label label = labelService.getById(id);
        if(label == null){
            return Msg.error().add("error","没有该标签！");
        }
        return Msg.success().add("data",label);
    }

    /**
     * 删除单个标签
     * @param id
     * @return
     */
    @Transactional
    @RequestMapping(value = "remove/{id}",method = RequestMethod.GET)
    public Msg removeLabel(@PathVariable("id") String id){
        if (isBookLabel(Integer.valueOf(id)) != null && isBookLabel(Integer.valueOf(id)).size() > 0){
            return Msg.error().add("error","该标签绑定书籍，不可修改！");
        }
        Label label = labelService.getById(id);
        label.setTbStatus("删除");
        boolean flag = labelService.updateById(label);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
     * 批量删除标签
     * @param labelIds
     * @return
     */
    @Transactional
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatchLabel(@RequestBody List<Label> labelIds){
        List<Label> delLabelList = new ArrayList<>();
        List<Label> noDelLabelList = new ArrayList<>();
        labelIds.forEach(label -> {
            if (isBookLabel(label.getLabelId()) != null && isBookLabel(label.getLabelId()).size() > 0){
                noDelLabelList.add(label);
            }else {
                delLabelList.add(label);
            }
        });
        delLabelList.forEach(e->{
            e.setTbStatus("删除");
        });
        boolean flag = false;
        if (delLabelList.size() > 0){
            flag = labelService.updateBatchById(delLabelList);
        }
        if (!flag){
            return Msg.error().add("error","删除失败！请重新删除");
        }
        if (noDelLabelList.size() > 0){
            List<Integer> noDelId = new ArrayList<>();
            noDelLabelList.forEach(e->{
                noDelId.add(e.getLabelId());
            });
            return Msg.error().add("error","标签ID为:["+ StringUtils.join(noDelId,",") +"]的标签下绑定书籍，该标签不可删除");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
     * 查询书籍下是否绑定标签
     * @return
     */
    private List<BookLabel> isBookLabel(Integer labelId){
        QueryWrapper condition = new QueryWrapper();
        condition.eq("tb_status","正常");
        condition.eq("label_id",labelId);
        List list = bookLabelService.list(condition);
        return list;
    }

    /**
     * 判断标签名是否已经存在
     * @param labelName
     * @return
     */
    private Label isLabel(String labelName) {
        QueryWrapper<Label> condition = new QueryWrapper<>();
        condition.eq("name", labelName);
        condition.eq("tb_status","正常");
        Label label = labelService.getOne(condition);
        return label;
    }

    /**
     * 查询标签名是否存在
     * @param labelName
     * @return
     */
    @RequestMapping(value = "getIsLabel")
    public Msg getIsLabel(@RequestParam("labelName") String labelName){
        Label label = isLabel(labelName);
        if (label != null){
            return Msg.success().add("is","true");
        }
        return Msg.success().add("is","false");
    }
}

