package com.xiaoyao.goal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xiaoyao.goal.entity.dto.task.CreateTaskDTO;
import com.xiaoyao.goal.entity.dto.task.FinishTaskDTO;
import com.xiaoyao.goal.entity.dto.task.QueryTaskDTO;
import com.xiaoyao.goal.entity.dto.task.RetrospectiveDTO;
import com.xiaoyao.goal.service.ITaskService;
import com.xiaoyao.goal.utils.Result;
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

    @PostMapping("/page")
    public Result page(@RequestBody @Validated QueryTaskDTO body) {
        return Result.success(taskService.page(body));
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
    public Result getCurrentTask() {
        return Result.success(taskService.getCurrentTask());
    }

    @PostMapping("removeById")
    public Result removeById(@RequestBody Long id) {
        return Result.success(taskService.removeById(id));
    }
}
