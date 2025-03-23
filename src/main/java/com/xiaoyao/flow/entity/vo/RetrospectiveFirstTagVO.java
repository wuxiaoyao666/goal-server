package com.xiaoyao.flow.entity.vo;

import com.xiaoyao.flow.entity.bo.TimeBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 逍遥
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrospectiveFirstTagVO {

    /**
     * 主 Tag 标题
     */
    private String title;

    /**
     * 子 Tag 集合
     */
    private List<RetrospectiveSecondTagVO> secondTags;

    /**
     * 总时长（单位：秒）
     */
    private long totalSeconds;
}
