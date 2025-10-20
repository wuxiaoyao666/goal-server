package com.xiaoyao.goal.entity.dto.diary;

import com.xiaoyao.goal.constant.GoalConstant;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 逍遥
 */
@Data
public class SearchDiaryDTO {
    /**
     * 关键词
     */
    private String keyword;

    /**
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 当前页
     */
    private int current = 1;

    /**
     * 每页展示条数
     */
    private int limit = GoalConstant.DefaultPageLimit;
}
