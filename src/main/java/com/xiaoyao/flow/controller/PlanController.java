package com.xiaoyao.flow.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xiaoyao.flow.entity.dto.CreatePlanDTO;
import com.xiaoyao.flow.service.IPlanService;
import com.xiaoyao.flow.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 计划 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-04-05
 */
@SaCheckLogin
@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private IPlanService planService;

    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreatePlanDTO body) {
        return Result.success(planService.create(body));
    }
}
