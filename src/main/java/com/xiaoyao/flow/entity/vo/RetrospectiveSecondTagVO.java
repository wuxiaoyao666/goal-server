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
public class RetrospectiveSecondTagVO {
    /**
     * 任务 ID
     */
    private Long id;
    /**
     * 二级标签
     */
    private String secondTag;
    /**
     * 标题
     */
    private String title;
    /**
     * 总时长
     */
    private long totalSeconds;
}