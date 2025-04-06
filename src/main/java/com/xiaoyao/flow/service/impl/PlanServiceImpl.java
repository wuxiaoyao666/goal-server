package com.xiaoyao.flow.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.xiaoyao.flow.entity.Plan;
import com.xiaoyao.flow.entity.dto.CreatePlanDTO;
import com.xiaoyao.flow.enums.PlanType;
import com.xiaoyao.flow.mapper.PlanMapper;
import com.xiaoyao.flow.service.IPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 计划 服务实现类
 * </p>
 *
 * @author 逍遥
 * @since 2025-04-05
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements IPlanService {

    @Override
    public Long create(CreatePlanDTO param) {
        Plan.PlanBuilder planBuilder = Plan.builder().title(param.getTitle())
                .tagId(param.getTagId())
                .type(param.getType())
                .endDate(param.getEndDate())
                .endTime(param.getEndTime())
                .userId(StpUtil.getLoginIdAsInt());
        if (Objects.equals(param.getType(), PlanType.Task.getValue())) {
            LocalDateTime now = LocalDateTime.now();
            planBuilder.startDate(now.toLocalDate())
                    .startTime(now.toLocalTime());
        }
        Plan plan = planBuilder.build();
        save(plan);
        return plan.getId();
    }
}
