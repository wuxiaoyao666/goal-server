package com.xiaoyao.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyao.flow.constant.TimeFlowConstant;
import com.xiaoyao.flow.entity.bo.TimeBO;
import com.xiaoyao.flow.entity.dto.CreateTaskDTO;
import com.xiaoyao.flow.entity.dto.FinishTaskDTO;
import com.xiaoyao.flow.entity.dto.QueryTaskDTO;
import com.xiaoyao.flow.entity.dto.RetrospectiveDTO;
import com.xiaoyao.flow.entity.Task;
import com.xiaoyao.flow.entity.vo.MaxDurationTagVO;
import com.xiaoyao.flow.entity.vo.RetrospectiveRowVO;
import com.xiaoyao.flow.enums.Platform;
import com.xiaoyao.flow.enums.TaskStatus;
import com.xiaoyao.flow.exception.TimeFlowException;
import com.xiaoyao.flow.mapper.TaskMapper;
import com.xiaoyao.flow.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.flow.utils.TimeUtils;
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
    public IPage<Task> page(QueryTaskDTO param) {
        Page page = new Page<>(param.getCurrent(), param.getLimit());
        LambdaQueryWrapper<Task> wrapper = Wrappers.lambdaQuery(Task.class)
                .between(Task::getStartDate, param.getStartDate(), param.getFinishDate())
                .eq(Task::getStatus, param.getStatus())
                .eq(Task::getFirstTag, param.getFirstTag())
                .eq(Task::getSecondTag, param.getSecondTag());
        if (param.getPlatform() != null) wrapper.eq(Task::getPlatform, param.getPlatform());
        return page(page, wrapper);
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
        Map<String, FirstTagData> firstTagMap = new HashMap<>();
        Map<String, Long> globalSecondTagTotalMap = new HashMap<>(); // 全局子标签总时长
        long totalSeconds = 0L;
        for (Task task : tasks) {
            // 计算任务时长
            LocalDateTime start = LocalDateTime.of(task.getStartDate(), task.getStartTime());
            LocalDateTime end = LocalDateTime.of(task.getFinishDate(), task.getFinishTime());
            long durationSeconds = Duration.between(start, end).getSeconds();
            totalSeconds += durationSeconds;

            // 获取主标签和子标签名称（处理空值）
            String firstTag = Optional.ofNullable(task.getFirstTag()).orElse(TimeFlowConstant.NoTag);
            String secondTag = Optional.ofNullable(task.getSecondTag()).orElse(TimeFlowConstant.NoTag);

            // 聚合主标签数据
            FirstTagData firstTagData = firstTagMap.computeIfAbsent(firstTag, k -> new FirstTagData());

            // 合并同名子标签并累加时长
            firstTagData.secondTagMap.merge(
                    secondTag,
                    durationSeconds,
                    Long::sum
            );

            // 更新全局子标签总时长（用于最终的最长子标签统计）
            globalSecondTagTotalMap.merge(secondTag, durationSeconds, Long::sum);
        }

        // 3. 生成扁平化表格数据
        List<RetrospectiveRowVO> flatRows = new ArrayList<>();
        MaxDurationTagVO maxPrimaryTag = null;
        MaxDurationTagVO maxSecondaryTag = null;

        for (Map.Entry<String, FirstTagData> entry : firstTagMap.entrySet()) {
            String firstTag = entry.getKey();
            FirstTagData firstTagData = entry.getValue();
            long firstTotalSeconds = firstTagData.getTotalSeconds();

            // 动态更新最长主标签
            if (maxPrimaryTag == null || firstTotalSeconds > maxPrimaryTag.getTotalSeconds()) {
                maxPrimaryTag = new MaxDurationTagVO(firstTag, firstTotalSeconds);
            }

            // 将子标签 Map 转换为列表（已合并同名项）
            List<Map.Entry<String, Long>> secondTagEntries = new ArrayList<>(
                    firstTagData.secondTagMap.entrySet()
            );

            // 生成合并行数据
            int rowspan = secondTagEntries.size();
            for (int i = 0; i < rowspan; i++) {
                Map.Entry<String, Long> secondTagEntry = secondTagEntries.get(i);
                String secondTag = secondTagEntry.getKey();
                long secondTotalSeconds = secondTagEntry.getValue();

                RetrospectiveRowVO row = new RetrospectiveRowVO();
                row.setFirstTag(firstTag);
                if (i == 0) {
                    row.setRowspan(rowspan);
                    row.setFirstTotalSeconds(firstTotalSeconds);
                }
                row.setSecondTag(secondTag);
                row.setSecondTotalSeconds(secondTotalSeconds);
                flatRows.add(row);

                // 动态更新最长子标签（从全局统计获取）
                long globalSecondTotal = globalSecondTagTotalMap.get(secondTag);
                if (maxSecondaryTag == null || globalSecondTotal > maxSecondaryTag.getTotalSeconds()) {
                    maxSecondaryTag = new MaxDurationTagVO(secondTag, globalSecondTotal);
                }
            }
        }
        // 4. 构建返回结果
        return RetrospectiveVO.builder()
                .tagTime(flatRows)
                .maxDurationPrimary(maxPrimaryTag)
                .maxDurationSecond(maxSecondaryTag)
                .totalSeconds(totalSeconds)
                .build();
    }

    // 主标签聚合数据（内部类）
    private static class FirstTagData {
        // Key: 子标签名称, Value: 总时长
        private final Map<String, Long> secondTagMap = new HashMap<>();

        public long getTotalSeconds() {
            return secondTagMap.values().stream().mapToLong(Long::longValue).sum();
        }
    }
}