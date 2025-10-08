package com.xiaoyao.goal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class DiaryKeyword implements Serializable {

    /**
     * 日记关联表主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 日记ID
     */
    private Long diaryId;

    /**
     * 关键词哈希
     */
    private String keywordHash;
}
