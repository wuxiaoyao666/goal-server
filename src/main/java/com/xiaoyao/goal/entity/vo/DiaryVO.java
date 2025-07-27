package com.xiaoyao.goal.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 逍遥
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryVO {
    private Long id;
    private String title;
    private String content;
}
