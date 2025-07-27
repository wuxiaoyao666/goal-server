package com.xiaoyao.goal.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 逍遥
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDaysVO {
    /**
     * 总天数
     */
    private int totalDays;
    /**
     * 连续记录天数
     */
    private int continuousDays;
}
