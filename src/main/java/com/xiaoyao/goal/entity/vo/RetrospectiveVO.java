package com.xiaoyao.goal.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author 逍遥
 */
@Data
@Builder
public class RetrospectiveVO {

    /**
     * 占用时间最长的一集标题
     */
    private MaxDurationTagVO maxDurationPrimary;

    /**
     * 占用时间最长的二级标题
     */
    private MaxDurationTagVO maxDurationSecond;

    /**
     * 复盘
     */
    private List<RetrospectiveRowVO> tagTime;

    /**
     * 总时长（单位：秒）
     */
    private long totalSeconds;
}
