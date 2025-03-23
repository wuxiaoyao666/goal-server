package com.xiaoyao.flow.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xiaoyao.flow.service.ITagService;
import com.xiaoyao.flow.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("tree")
    public Result tree(){
        return Result.success(tagService.tree());
    }
}
