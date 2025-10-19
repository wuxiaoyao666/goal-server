package com.xiaoyao.goal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.goal.entity.bo.TimeBO;
import com.xiaoyao.goal.entity.dto.task.CreateTaskDTO;
import com.xiaoyao.goal.entity.dto.task.FinishTaskDTO;
import com.xiaoyao.goal.entity.dto.task.QueryTaskDTO;
import com.xiaoyao.goal.entity.dto.task.RetrospectiveDTO;
import com.xiaoyao.goal.entity.Task;
import com.xiaoyao.goal.entity.vo.RetrospectiveVO;

/**
 * <p>
 * 记录表 服务类
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
public interface ITaskService extends IService<Task> {

    /**
     * 插入任务
     *
     * @param param 参数
     * @return 唯一 ID
     */
    Long createTask(CreateTaskDTO param);

    /**
     * 分页查询 Task
     *
     * @param param 参数
     * @return 分页数据
     */
    IPage<Task> page(QueryTaskDTO param);

    /**
     * 结束任务
     *
     * @param param 参数
     * @return 时长
     */
    TimeBO finishTask(FinishTaskDTO param);

    /**
     * 获取当前任务
     *
     * @return 当前正在执行的
     */
    Task getCurrentTask();

    /**
     * 复盘
     *
     * @param param form 传递的参数
     * @return 复盘记录
     */
    RetrospectiveVO retrospective(RetrospectiveDTO param);
}
