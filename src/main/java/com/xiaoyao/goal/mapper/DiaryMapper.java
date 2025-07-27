package com.xiaoyao.goal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.goal.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日记表 Mapper 接口
 * </p>
 *
 * @author 逍遥
 * @since 2025-07-27
 */
@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {

}
