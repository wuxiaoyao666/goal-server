package com.xiaoyao.goal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyao.goal.entity.Diary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.goal.entity.dto.diary.ExportDiaryDTO;
import com.xiaoyao.goal.entity.dto.diary.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.diary.SearchDiaryDTO;
import com.xiaoyao.goal.entity.vo.DiaryHotTagsVO;
import com.xiaoyao.goal.entity.vo.DiaryVO;
import jakarta.servlet.http.HttpServletResponse;

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
     *
     * @param id 唯一标识
     */
    void delete(Long id);

    /**
     * 查询热门标签
     *
     * @param count 统计数量
     * @return 热门标签集合
     */
    List<DiaryHotTagsVO> hotTags(Integer count);

    /**
     * 导出日记
     *
     * @param body     日记导出参数
     * @param response 响应
     */
    void export(ExportDiaryDTO body, HttpServletResponse response);
}
