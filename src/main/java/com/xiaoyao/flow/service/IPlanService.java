package com.xiaoyao.flow.service;

import com.xiaoyao.flow.entity.Plan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.flow.entity.dto.CreatePlanDTO;

/**
 * <p>
 * 计划 服务类
 * </p>
 *
 * @author 逍遥
 * @since 2025-04-05
 */
public interface IPlanService extends IService<Plan> {

    /**
     * 创建计划
     * @param param 计划参数
     * @return 计划 ID
     */
    Long create(CreatePlanDTO param);
}
