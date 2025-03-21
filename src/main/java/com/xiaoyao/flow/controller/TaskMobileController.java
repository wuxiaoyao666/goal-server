package com.xiaoyao.flow.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xiaoyao.flow.dto.MobileCreateTaskDTO;
import com.xiaoyao.flow.dto.MobileFinishTaskDTO;
import com.xiaoyao.flow.service.ITaskService;
import com.xiaoyao.flow.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SaCheckLogin
@RestController
@RequestMapping("/task/mobile")
public class TaskMobileController {

    @Autowired
    private ITaskService taskService;

    @PostMapping("/createTask")
    public Result createTask(@Validated @RequestBody MobileCreateTaskDTO body){
        return Result.success(taskService.MobileSaveTask(body));
    }

    @PostMapping("/finishTask")
    public Result finishTask(@Validated @RequestBody MobileFinishTaskDTO body){
        return Result.success(taskService.MobileFinishTask(body));
    }

    @GetMapping("/getCurrentTaskId")
    public Result getCurrentTaskId(){
        return Result.success(taskService.getCurrentTaskId());
    }
}