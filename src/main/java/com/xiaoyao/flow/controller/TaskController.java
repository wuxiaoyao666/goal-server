package com.xiaoyao.flow.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xiaoyao.flow.entity.dto.CreateTaskDTO;
import com.xiaoyao.flow.entity.dto.FinishTaskDTO;
import com.xiaoyao.flow.entity.dto.RetrospectiveDTO;
import com.xiaoyao.flow.entity.Task;
import com.xiaoyao.flow.service.ITaskService;
import com.xiaoyao.flow.utils.Result;
import com.xiaoyao.flow.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 记录表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@SaCheckLogin
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Task body) {
        return taskService.saveOrUpdate(body) ? Result.success() : Result.fail(ResultCode.SAVE_FAIL);
    }

    @PostMapping("/retrospective")
    public Result retrospective(@RequestBody @Validated RetrospectiveDTO body) {
        return Result.success(taskService.retrospective(body));
    }

    @PostMapping("/createTask")
    public Result createTask(@Validated @RequestBody CreateTaskDTO body) {
        return Result.success(taskService.createTask(body));
    }

    @PostMapping("/finishTask")
    public Result finishTask(@Validated @RequestBody FinishTaskDTO body) {
        return Result.success(taskService.finishTask(body));
    }

    @GetMapping("/getCurrentTask")
    public Result getCurrentTaskId() {
        return Result.success(taskService.getCurrentTaskId());
    }
}
