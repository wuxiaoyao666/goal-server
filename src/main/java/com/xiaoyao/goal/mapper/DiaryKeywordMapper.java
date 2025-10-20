package com.xiaoyao.goal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.goal.entity.DiaryKeyword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 日记关键词关联表 Mapper 接口
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-08
 */
@Mapper
public interface DiaryKeywordMapper extends BaseMapper<DiaryKeyword> {

    /**
     * 根据关键词查询日记
     * @param words 关键词
     * @return 日记 ID 集合
     */
    Set<Long> selectDistinctDiaryIdsByWords(@Param("words") Set<String> words);
}
