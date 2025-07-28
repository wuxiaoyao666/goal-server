package com.xiaoyao.goal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.entity.dto.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.SearchDiaryDTO;
import com.xiaoyao.goal.entity.vo.DiaryVO;
import com.xiaoyao.goal.entity.vo.RecordDaysVO;
import com.xiaoyao.goal.entity.vo.SearchDiaryVO;
import com.xiaoyao.goal.mapper.DiaryMapper;
import com.xiaoyao.goal.service.IDiaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.goal.utils.BlindGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 日记表 服务实现类
 * </p>
 *
 * @author 逍遥
 * @since 2025-07-27
 */
@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary> implements IDiaryService {

    @Override
    public long sync(SaveDiaryDTO body) {
        Long userId = StpUtil.getLoginIdAsLong();
        LocalDate targetDate = body.getDate();
        // 查询当天日记
        Diary existingDiary = getOne(Wrappers.lambdaQuery(Diary.class)
                .eq(Diary::getUserId, userId)
                .ge(Diary::getCreateTime, targetDate.atStartOfDay())
                .lt(Diary::getCreateTime, targetDate.plusDays(1).atStartOfDay()));
        String searchIndex = BlindGenerator.segment(body.getTitle() + " " + body.getContent());
        Diary diary;
        if (existingDiary != null) {
            // 存在则使用已有日记进行修改
            diary = existingDiary;
            diary.setTitle(body.getTitle());
            diary.setContent(body.getContent());
            diary.setSearchIndex(searchIndex);
        } else {
            // 不存在则创建新日记
            diary = Diary.builder()
                    .userId(userId)
                    .title(body.getTitle())
                    .content(body.getContent())
                    .searchIndex(searchIndex)
                    .createTime(LocalDateTime.of(targetDate, LocalTime.now()))
                    .build();
        }
        saveOrUpdate(diary);
        return diary.getId();
    }

    @Override
    public DiaryVO info(LocalDate body) {
        Diary diary = getOne(Wrappers.lambdaQuery(Diary.class)
                .select(Diary::getId, Diary::getTitle, Diary::getContent)
                .eq(Diary::getUserId, StpUtil.getLoginIdAsLong())
                .ge(Diary::getCreateTime, body.atStartOfDay())
                .lt(Diary::getCreateTime, body.plusDays(1).atStartOfDay()));
        if (diary == null) return null;
        return DiaryVO.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent()).build();
    }

    @Override
    public RecordDaysVO recordDays() {
        List<Diary> diaries = list(Wrappers.lambdaQuery(Diary.class).select(Diary::getCreateTime).eq(Diary::getUserId, StpUtil.getLoginIdAsLong()).orderByDesc(Diary::getCreateTime));
        if (diaries == null || diaries.isEmpty()) {
            return new RecordDaysVO(0, 0);
        }
        List<LocalDateTime> recordDates = diaries.stream().map(Diary::getCreateTime).toList();
        // 计算连续天数
        int continuousDays = 1;
        LocalDate nextDate = recordDates.getFirst().toLocalDate();
        for (int i = 1; i < recordDates.size(); i++) {
            LocalDate currentDate = recordDates.get(i).toLocalDate();
            if (currentDate.plusDays(1).equals(nextDate)) {
                continuousDays++;
            } else {
                // 非连续日期
                break;
            }
            nextDate = currentDate;
        }
        return new RecordDaysVO(recordDates.size(), continuousDays);
    }

    @Override
    public List<SearchDiaryVO> search(SearchDiaryDTO body) {
        String keywordHash = BlindGenerator.hashToken(body.getKeyword());
        List<Diary> diaryList = list(Wrappers.lambdaQuery(Diary.class).select(Diary::getTitle, Diary::getContent, Diary::getCreateTime).eq(Diary::getUserId, StpUtil.getLoginIdAsLong())
                .like(Diary::getSearchIndex, "%" + keywordHash + "%"));
        if (diaryList == null || diaryList.isEmpty()) return new ArrayList<>();
        return diaryList.stream().map((diary) -> new SearchDiaryVO(diary.getTitle(), diary.getContent(), diary.getCreateTime().toLocalDate())).collect(Collectors.toList());
    }
}
