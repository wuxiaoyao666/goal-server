package com.xiaoyao.goal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.xiaoyao.goal.entity.Tag;
import com.xiaoyao.goal.entity.dto.QueryTagDTO;
import com.xiaoyao.goal.entity.dto.SaveTagDTO;
import com.xiaoyao.goal.service.ITagService;
import com.xiaoyao.goal.utils.Result;
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

    @PostMapping("/page")
    public Result page(@RequestBody QueryTagDTO body) {
        return Result.success(tagService.page(body));
    }

    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody @Validated SaveTagDTO body) {
        Tag tag = Tag.builder().id(body.getId())
                .name(body.getName()).parent(body.getParent())
                .userId(StpUtil.getLoginIdAsInt()).build();
        tagService.saveOrUpdate(tag);
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
