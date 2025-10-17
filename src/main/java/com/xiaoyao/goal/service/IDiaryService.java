package com.xiaoyao.goal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyao.goal.entity.Diary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.goal.entity.dto.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.SearchDiaryDTO;
import com.xiaoyao.goal.entity.vo.DiaryHotTagsVO;
import com.xiaoyao.goal.entity.vo.DiaryVO;

import java.util.List;

/**
 * <p>
 * 日记表 服务类
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-08
 */
public interface IDiaryService extends IService<Diary> {

    /**
     * 同步日记数据
     *
     * @param body 参数
     * @return 数据 ID
     */
    DiaryVO sync(SaveDiaryDTO body);

    /**
     * 日记搜索
     *
     * @param body 搜索参数
     * @return 搜索结果
     */
    IPage<DiaryVO> search(SearchDiaryDTO body);

    /**
     * 删除日记
     * @param id
     * @return
     */
    void delete(Long id);

    /**
     * 查询热门标签
     * @return 热门标签集合
     */
    List<DiaryHotTagsVO> hotTags();
}
