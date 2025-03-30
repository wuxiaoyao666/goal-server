package com.xiaoyao.flow.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.xiaoyao.flow.entity.Tag;
import com.xiaoyao.flow.entity.dto.QueryTagDTO;
import com.xiaoyao.flow.entity.dto.SaveTagDTO;
import com.xiaoyao.flow.service.ITagService;
import com.xiaoyao.flow.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@SaCheckLogin
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public Result list(@RequestBody QueryTagDTO body) {
        return Result.success(tagService.find(body));
    }

    @PostMapping("/save")
    public Result save(@RequestBody @Validated SaveTagDTO body) {
        Tag tag = new Tag();
        tag.setName(body.getName());
        tag.setParent(body.getParent());
        tag.setUserId(StpUtil.getLoginIdAsInt());
        tagService.save(tag);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Long id) {
        tagService.delete(id);
        return Result.success();
    }

    @GetMapping("/tree")
    public Result tree() {
        return Result.success(tagService.tree());
    }
}
