package com.xiaoyao.flow.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
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

}
