package com.xiaoyao.goal.entity.vo;

import lombok.Data;

/**
 * @author 逍遥
 */
@Data
public class RetrospectiveRowVO {
    // 主标签名称
    private String firstTag;
    // 主标签总时长
    private Long firstTotalSeconds;
    // 子标签名称
    private String secondTag;
    // 子标签总时长
    private Long secondTotalSeconds;
    // 合并行数（用于前端合并单元格）
    private Integer rowspan;
}
