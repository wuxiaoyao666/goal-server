package com.xiaoyao.goal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 日记关键词关联表
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("diary_keyword")
// 基于diaryId和word去重
@EqualsAndHashCode(of = {"diaryId", "word"})
public class DiaryKeyword implements Serializable {

    /**
     * 日记ID
     */
    private Long diaryId;

    /**
     * 关键词哈希
     */
    private String word;
}
