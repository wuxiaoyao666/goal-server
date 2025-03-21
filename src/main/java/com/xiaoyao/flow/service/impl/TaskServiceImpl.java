package com.xiaoyao.flow.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.flow.dto.MobileCreateTaskDTO;
import com.xiaoyao.flow.dto.MobileFinishTaskDTO;
import com.xiaoyao.flow.entity.Task;
import com.xiaoyao.flow.enums.Platform;
import com.xiaoyao.flow.enums.TaskStatus;
import com.xiaoyao.flow.exception.TimeFlowException;
import com.xiaoyao.flow.mapper.TaskMapper;
import com.xiaoyao.flow.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.flow.utils.TimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        // 1. 校验是否有任务还未完成
        long unFinishTaskCount = count(Wrappers.lambdaQuery(Task.class).eq(Task::getStatus, TaskStatus.IN_PROGRESS.getValue())
                .eq(Task::getPlatform, Platform.APP.getValue()));
        if (unFinishTaskCount > 0) {
            throw new TimeFlowException("还有任务未完成！");
        }
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
    public String MobileFinishTask(MobileFinishTaskDTO param) {
        Task task = getOne(Wrappers.lambdaQuery(Task.class)
                .eq(Task::getId, param.getTaskId()));
        if (task == null) {
            throw new TimeFlowException("任务不存在！");
        }
        if (task.getStatus() != TaskStatus.IN_PROGRESS.getValue()) {
            throw new TimeFlowException("当前任务已关闭！");
        }
        LocalDateTime now = LocalDateTime.now();
        task.setEndTime(now.toLocalTime());
        task.setStatus(TaskStatus.COMPLETED.getValue());
        updateById(task);
        return String.format("耗时：%s", TimeUtils.calculateDuration(task.getToday().atTime(task.getStartTime()), now));
    }

    @Override
    public List<Task> getCurrentTaskId() {
        return list(Wrappers.lambdaQuery(Task.class).eq(Task::getStatus, TaskStatus.IN_PROGRESS.getValue())
                .eq(Task::getPlatform, Platform.APP.getValue()));
    }
}