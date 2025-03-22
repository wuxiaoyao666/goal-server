package com.xiaoyao.flow.controller;

import com.xiaoyao.flow.entity.Task;
import com.xiaoyao.flow.service.ITaskService;
import com.xiaoyao.flow.utils.Result;
import com.xiaoyao.flow.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 记录表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Task body){
        return taskService.saveOrUpdate(body) ? Result.success() : Result.fail(ResultCode.SAVE_FAIL);
    }
}
