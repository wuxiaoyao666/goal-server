package com.xiaoyao.flow.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.xiaoyao.flow.service.IUserService;
import com.xiaoyao.flow.utils.Result;
import com.xiaoyao.flow.vo.UserVo;
import dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Result<UserVo> login(@RequestBody LoginDTO param){
        UserVo login = userService.login(param);
        StpUtil.login(login.getId());
        return Result.success(login);
    }
}
