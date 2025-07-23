package com.xiaoyao.goal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyao.goal.constant.GoalConstant;
import com.xiaoyao.goal.entity.bo.TimeBO;
import com.xiaoyao.goal.entity.dto.CreateTaskDTO;
import com.xiaoyao.goal.entity.dto.FinishTaskDTO;
import com.xiaoyao.goal.entity.dto.QueryTaskDTO;
import com.xiaoyao.goal.entity.dto.RetrospectiveDTO;
import com.xiaoyao.goal.entity.Task;
import com.xiaoyao.goal.entity.vo.MaxDurationTagVO;
import com.xiaoyao.goal.entity.vo.RetrospectiveRowVO;
import com.xiaoyao.goal.enums.TaskStatus;
import com.xiaoyao.goal.exception.GoalException;
import com.xiaoyao.goal.mapper.TaskMapper;
import com.xiaoyao.goal.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.goal.utils.TimeUtils;
import com.xiaoyao.goal.entity.vo.RetrospectiveVO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        Task task = Task.builder().title(param.getTitle())
                .firstTag(param.getFirstTag())
                .secondTag(param.getSecondTag())
                .startTime(LocalTime.now())
                .startDate(LocalDate.now())
                .status(TaskStatus.IN_PROGRESS.getValue())
                .platform(param.getPlatform())
                .userId(StpUtil.getLoginIdAsInt()).build();
        try{
            save(task);
        }catch (DuplicateKeyException e) {
            throw new GoalException("还有任务未完成！");
        }
        return task.getId();
    }

    @Override
    public IPage<Task> page(QueryTaskDTO param) {
        Page<Task> page = new Page<>(param.getCurrent(), param.getLimit());
        LambdaQueryWrapper<Task> wrapper = Wrappers.lambdaQuery(Task.class)
                .between(Task::getStartDate, param.getStartDate(), param.getFinishDate())
                .eq(Task::getStatus, param.getStatus())
                .eq(Task::getFirstTag, param.getFirstTag())
                .eq(Task::getSecondTag, param.getSecondTag())
                .eq(Task::getUserId, StpUtil.getLoginIdAsInt());
        if (param.getPlatform() != null) wrapper.eq(Task::getPlatform, param.getPlatform());
        return page(page, wrapper);
    }

    @Override
    public TimeBO finishTask(FinishTaskDTO param) {
        Task task = getById(param.getTaskId());
        if (task == null) {
            throw new GoalException("任务不存在！");
        }
        if (task.getStatus() != TaskStatus.IN_PROGRESS.getValue()) {
            throw new GoalException("当前任务已关闭！");
        }
        // 是否展示当前任务
        if (param.isShowTask()) task.setStatus(TaskStatus.COMPLETED.getValue());
        else task.setStatus(TaskStatus.NOT_SHOW.getValue());
        if (StringUtils.hasText(param.getTitle())) task.setTitle(param.getTitle());
        if (StringUtils.hasText(param.getFirstTag())) task.setFirstTag(param.getFirstTag());
        if (StringUtils.hasText(param.getSecondTag())) task.setSecondTag(param.getSecondTag());
        LocalDateTime now = LocalDateTime.now();
        task.setFinishDate(now.toLocalDate());
        task.setFinishTime(now.toLocalTime());

        updateById(task);
        return TimeUtils.calculateDuration(task.getStartDate().atTime(task.getStartTime()), now);
    }

    @Override
    public Task getCurrentTask() {
        return getOne(Wrappers.lambdaQuery(Task.class).eq(Task::getStatus, TaskStatus.IN_PROGRESS.getValue())
                .eq(Task::getUserId, StpUtil.getLoginIdAsInt()));
    }

    @Override
    public RetrospectiveVO retrospective(RetrospectiveDTO param) {
        // 1. 条件查询任务
        LambdaQueryWrapper<Task> wrapper = Wrappers.lambdaQuery(Task.class);
        wrapper.between(Task::getStartDate, param.getStartDate(), param.getFinishDate());
        if (param.getStatus() != null && param.getStatus() == 1) {
            throw new GoalException("参数错误！");
        }
        if (param.getStatus() != null) wrapper.eq(Task::getStatus, param.getStatus());
        else wrapper.eq(Task::getStatus, TaskStatus.COMPLETED.getValue());
        if (param.getPlatform() != null) {
            wrapper.eq(Task::getPlatform, param.getPlatform());
        }
        wrapper.eq(Task::getUserId, StpUtil.getLoginIdAsInt());
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
            String firstTag = Optional.ofNullable(task.getFirstTag()).orElse(GoalConstant.NoTag);
            String secondTag = Optional.ofNullable(task.getSecondTag()).orElse(GoalConstant.NoTag);

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