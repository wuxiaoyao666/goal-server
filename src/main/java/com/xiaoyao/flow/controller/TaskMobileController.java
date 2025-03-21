package com.xiaoyao.flow.controller;

import com.xiaoyao.flow.dto.MobileCreateTaskDTO;
import com.xiaoyao.flow.dto.MobileFinishTaskDTO;
import com.xiaoyao.flow.service.ITaskService;
import com.xiaoyao.flow.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task/mobile")
public class TaskMobileController {

    @Autowired
    private ITaskService taskService;

    @PostMapping("/createTask")
    public Result<Long> createTask(@Validated @RequestBody MobileCreateTaskDTO body){
        return Result.success(taskService.MobileSaveTask(body));
    }

    @PostMapping("/finishTask")
    public Result finishTask(@Validated @RequestBody MobileFinishTaskDTO body){
        taskService.MobileFinishTask(body);
        return Result.success();
    }
}
