package com.xiaoyao.flow.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.xiaoyao.flow.constant.TimeFlowConstant;
import com.xiaoyao.flow.entity.dto.RegisterDTO;
import com.xiaoyao.flow.service.IUserService;
import com.xiaoyao.flow.utils.Result;
import com.xiaoyao.flow.entity.dto.LoginDTO;
import com.xiaoyao.flow.entity.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody @Validated LoginDTO body) {
        UserVo login = userService.login(body);
        StpUtil.login(login.getId(), new SaLoginParameter().setExtra(TimeFlowConstant.JwtUserInfo, login));
        return Result.success(StpUtil.getTokenValue());
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegisterDTO body){
        userService.register(body);
        return Result.success();
    }

    @SaCheckLogin
    @GetMapping("/info")
    public Result info() {
        return Result.success(StpUtil.getExtra(TimeFlowConstant.JwtUserInfo));
    }
}
