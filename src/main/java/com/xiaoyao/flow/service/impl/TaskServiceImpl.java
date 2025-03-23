package com.xiaoyao.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.flow.constant.TimeFlowConstant;
import com.xiaoyao.flow.entity.bo.TimeBO;
import com.xiaoyao.flow.entity.dto.CreateTaskDTO;
import com.xiaoyao.flow.entity.dto.FinishTaskDTO;
import com.xiaoyao.flow.entity.dto.RetrospectiveDTO;
import com.xiaoyao.flow.entity.Task;
import com.xiaoyao.flow.entity.vo.RetrospectiveFirstTagVO;
import com.xiaoyao.flow.enums.Platform;
import com.xiaoyao.flow.enums.TaskStatus;
import com.xiaoyao.flow.exception.TimeFlowException;
import com.xiaoyao.flow.mapper.TaskMapper;
import com.xiaoyao.flow.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.flow.utils.TimeUtils;
import com.xiaoyao.flow.entity.vo.RetrospectiveSecondTagVO;
import com.xiaoyao.flow.entity.vo.RetrospectiveVO;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
    public Long createTask(CreateTaskDTO param) {
        // 1. 校验是否有任务还未完成
        long unFinishTaskCount = count(Wrappers.lambdaQuery(Task.class).eq(Task::getStatus, TaskStatus.IN_PROGRESS.getValue()));
        if (unFinishTaskCount > 0) {
            throw new TimeFlowException("还有任务未完成！");
        }
        Task task = Task.builder().title(param.getTitle())
                .firstTag(param.getFirstTag())
                .secondTag(param.getSecondTag())
                .startTime(LocalTime.now())
                .startDate(LocalDate.now())
                .status(TaskStatus.IN_PROGRESS.getValue())
                .platform(param.getPlatform()).build();
        save(task);
        return task.getId();
    }

    @Override
    public TimeBO finishTask(FinishTaskDTO param) {
        Task task = getOne(Wrappers.lambdaQuery(Task.class)
                .eq(Task::getId, param.getTaskId()));
        if (task == null) {
            throw new TimeFlowException("任务不存在！");
        }
        if (task.getStatus() != TaskStatus.IN_PROGRESS.getValue()) {
            throw new TimeFlowException("当前任务已关闭！");
        }
        // 是否展示当前任务
        if (param.isShowTask()) task.setStatus(TaskStatus.COMPLETED.getValue());
        else task.setStatus(TaskStatus.NOT_SHOW.getValue());
        LocalDateTime now = LocalDateTime.now();
        task.setFinishDate(now.toLocalDate());
        task.setFinishTime(now.toLocalTime());

        updateById(task);
        return TimeUtils.calculateDuration(task.getStartDate().atTime(task.getStartTime()), now);
    }

    @Override
    public List<Task> getCurrentTaskId() {
        return list(Wrappers.lambdaQuery(Task.class).eq(Task::getStatus, TaskStatus.IN_PROGRESS.getValue())
                .eq(Task::getPlatform, Platform.APP.getValue()));
    }

    @Override
    public RetrospectiveVO retrospective(RetrospectiveDTO param) {
        // 1. 条件查询任务
        LambdaQueryWrapper<Task> wrapper = Wrappers.lambdaQuery(Task.class);
        wrapper.between(Task::getStartDate, param.getStartDate(), param.getFinishDate());
        if (param.getStatus() != null && param.getStatus() == 1) {
            throw new TimeFlowException("参数错误！");
        }
        if (param.getStatus() != null) wrapper.eq(Task::getStatus, param.getStatus());
        else wrapper.eq(Task::getStatus, TaskStatus.COMPLETED.getValue());
        if (param.getPlatform() != null) {
            wrapper.eq(Task::getPlatform, param.getPlatform());
        }
        List<Task> tasks = list(wrapper);
        // 2. 统计二级标签时长
        Map<String, FirstTagData> firstTagMap = new HashMap<>();
        long totalSeconds = 0L;
        RetrospectiveSecondTagVO maxSecondaryTag = null;
        for (Task task : tasks) {
            // 计算耗时（秒）
            LocalDateTime start = LocalDateTime.of(task.getStartDate(), task.getStartTime());
            LocalDateTime end = LocalDateTime.of(task.getFinishDate(), task.getFinishTime());
            long durationSeconds = Duration.between(start, end).getSeconds();
            totalSeconds += durationSeconds;

            // 实时更新最长二级标签（无需后续遍历）
            if (maxSecondaryTag == null || durationSeconds > maxSecondaryTag.getTotalSeconds()) {
                maxSecondaryTag = new RetrospectiveSecondTagVO(
                        task.getId(),
                        Optional.ofNullable(task.getSecondTag()).orElse(TimeFlowConstant.NoTag),
                        task.getTitle(),
                        durationSeconds
                );
            }

            // 聚合一级标签数据
            String firstTag = Optional.ofNullable(task.getFirstTag()).orElse(TimeFlowConstant.NoTag);
            firstTagMap.computeIfAbsent(firstTag, k -> new FirstTagData())
                    .addData(maxSecondaryTag, durationSeconds);  // 直接复用已创建的VO
        }
        // 3. 生成完整复盘
        List<RetrospectiveFirstTagVO> tagTime = new ArrayList<>();
        RetrospectiveFirstTagVO maxPrimaryTag = null;
        for (Map.Entry<String, FirstTagData> entry : firstTagMap.entrySet()) {
            String firstTagTitle = entry.getKey();
            FirstTagData data = entry.getValue();

            // 直接使用秒数构建VO（无需TimeBO转换）
            RetrospectiveFirstTagVO primaryVO = new RetrospectiveFirstTagVO(
                    firstTagTitle,
                    data.secondTagList,
                    data.totalSeconds
            );
            tagTime.add(primaryVO);

            // 动态比较最长一级标签
            if (maxPrimaryTag == null || data.totalSeconds > maxPrimaryTag.getTotalSeconds()) {
                maxPrimaryTag = new RetrospectiveFirstTagVO(
                        firstTagTitle,
                        null,
                        data.totalSeconds
                );
            }
        }
        // 4. 构建返回结果
        return RetrospectiveVO.builder()
                .tagTime(tagTime)
                .maxDurationPrimary(maxPrimaryTag)
                .maxDurationSecond(maxSecondaryTag)
                .totalSeconds(totalSeconds)
                .build();
    }

    private static class FirstTagData {
        private long totalSeconds;
        private final List<RetrospectiveSecondTagVO> secondTagList = new ArrayList<>();

        public void addData(RetrospectiveSecondTagVO vo, long durationSeconds) {
            this.totalSeconds += durationSeconds;
            this.secondTagList.add(vo);
        }
    }
}