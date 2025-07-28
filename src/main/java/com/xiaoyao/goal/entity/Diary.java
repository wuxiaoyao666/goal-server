package com.xiaoyao.goal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoyao.goal.utils.AesEncryptTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 日记表
 * </p>
 *
 * @author 逍遥
 * @since 2025-07-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(autoResultMap = true)
public class Diary implements Serializable {

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 日记标题
     */
    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String title;

    /**
     * 日记正文
     */
    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String content;

    /**
     * 盲索引
     */
    private String searchIndex;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
