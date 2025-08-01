package com.xiaoyao.goal.entity.dto;

import com.xiaoyao.goal.constant.GoalConstant;
import lombok.Data;

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
     * 当前页
     */
    private int current = 1;

    /**
     * 每页展示条数
     */
    private int limit = GoalConstant.DefaultPageLimit;
}
