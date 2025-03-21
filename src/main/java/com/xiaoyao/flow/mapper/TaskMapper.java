package com.xiaoyao.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.flow.entity.Task;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * 记录表 Mapper 接口
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}

