package com.xiaoyao.goal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * <p>
 * 记录表
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 当日
     */
    private LocalDate startDate;

    /**
     * 创建时间
     */
    private LocalTime startTime;

    /**
     * 完成日
     */
    private LocalDate finishDate;

    /**
     * 完成时间
     */
    private LocalTime finishTime;


    /**
     * 一级标签
     */
    private String firstTag;

    /**
     * 二级标签
     */
    private String secondTag;

    /**
     * 1: 记录中；2:已完成 3: 不统计
     */
    private Byte status;

    /**
     * 平台: 1: PC 端；2:移动端
     */
    private Byte platform;

    /**
     * 用户 ID
     */
    private Integer userId;
}
