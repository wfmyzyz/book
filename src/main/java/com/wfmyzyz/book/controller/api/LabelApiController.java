package com.wfmyzyz.book.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wfmyzyz.book.domain.Label;
import com.wfmyzyz.book.service.ILabelService;
import com.wfmyzyz.book.utils.Msg;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xiong
 */
@RestController
@RequestMapping("api/label")
public class LabelApiController {
    @Autowired
    ILabelService labelService;

    /**
     * 获取所有标签列表
     * @return
     */
    @ApiOperation(value="获取所有标签", notes="获取所有标签" ,httpMethod="GET")
    @GetMapping("getAllLabel")
    public Msg getAllLabel(){
        List<Label> list = labelService.list(new QueryWrapper<Label>().eq("tb_status", "正常"));
        return Msg.success().add("data",list);
    }
}
