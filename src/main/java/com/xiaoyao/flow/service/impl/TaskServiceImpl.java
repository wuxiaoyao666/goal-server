package com.xiaoyao.flow.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.flow.dto.MobileCreateTaskDTO;
import com.xiaoyao.flow.dto.MobileFinishTaskDTO;
import com.xiaoyao.flow.entity.Task;
import com.xiaoyao.flow.enums.Platform;
import com.xiaoyao.flow.enums.TaskStatus;
import com.xiaoyao.flow.mapper.TaskMapper;
import com.xiaoyao.flow.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 * 记录表 服务实现类
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Override
    public Long MobileSaveTask(MobileCreateTaskDTO param) {
        // TODO 校验密钥
        Task task = Task.builder().title(param.getTitle())
                .firstTag(param.getFirstTag())
                .secondTag(param.getSecondTag())
                .startTime(LocalTime.now())
                .today(LocalDate.now())
                .status(TaskStatus.IN_PROGRESS.getValue())
                .platform(Platform.APP.getValue()).build();
        save(task);
        return task.getId();
    }

    @Override
    public void MobileFinishTask(MobileFinishTaskDTO param) {
        // TODO 校验密钥
        Task task = getOne(Wrappers.lambdaQuery(Task.class)
                .eq(Task::getId, param.getTaskId()));
        if(task.getStatus() != TaskStatus.IN_PROGRESS.getValue()) {
            throw new RuntimeException("当前任务已关闭！");
        }
        task.setEndTime(LocalTime.now());
        task.setStatus(TaskStatus.COMPLETED.getValue());
        updateById(task);
    }
}
