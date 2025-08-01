package com.xiaoyao.goal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyao.goal.entity.Diary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.goal.entity.dto.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.SearchDiaryDTO;
import com.xiaoyao.goal.entity.vo.DiaryVO;
import com.xiaoyao.goal.entity.vo.RecordDaysVO;

import java.time.LocalDate;

/**
 * <p>
 * 日记表 服务类
 * </p>
 *
 * @author 逍遥
 * @since 2025-07-27
 */
public interface IDiaryService extends IService<Diary> {

    /**
     * 同步数据
     * @param body 参数
     * @return 数据 ID
     */
    long sync(SaveDiaryDTO body);

    /**
     * 查询日记
     * @param body 日期
     * @return 日记详情
     */
    DiaryVO info(LocalDate body);

    /**
     * 获取记录天数
     * @return 总记录天数和连续记录天数
     */
    RecordDaysVO recordDays();

    /**
     * 日记搜索
     * @param body 搜索参数
     * @return 搜索结果
     */
    IPage<Diary> search(SearchDiaryDTO body);
}
