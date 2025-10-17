package com.xiaoyao.goal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.entity.vo.DiaryHotTagsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 日记表 Mapper 接口
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-08
 */
@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {

    /**
     * 查询热门标签
     *
     * @param userId 用户 ID
     * @param count  数量
     * @return 该用户热门标签
     */
    List<DiaryHotTagsVO> selectHotTags(@Param("userId") Long userId, @Param("count") Integer count);
}
