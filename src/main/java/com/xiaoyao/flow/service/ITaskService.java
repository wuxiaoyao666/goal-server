package com.xiaoyao.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.flow.dto.MobileCreateTaskDTO;
import com.xiaoyao.flow.dto.MobileFinishTaskDTO;
import com.xiaoyao.flow.entity.Task;

import java.util.List;

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
     * 移动端插入任务
     * @param param 参数
     * @return 唯一 ID
     */
    Long MobileSaveTask(MobileCreateTaskDTO param);

    /**
     * 移动端结束任务
     * @param param 参数
     * @return 时长
     */
    String MobileFinishTask(MobileFinishTaskDTO param);

    /**
     * 获取当前任务 ID
     * @return 当前正在执行的
     */
    List<Task> getCurrentTaskId();
}
