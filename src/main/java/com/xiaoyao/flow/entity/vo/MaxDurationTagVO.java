package com.xiaoyao.flow.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 逍遥
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaxDurationTagVO {
    /**
     * Tag 标题
     */
    private String title;

    /**
     * 总时长（单位：秒）
     */
    private long totalSeconds;
}
