package com.xiaoyao.flow.entity;

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
 * 计划
 * </p>
 *
 * @author 逍遥
 * @since 2025-04-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan implements Serializable {

    /**
     * 唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 计划名
     */
    private String title;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 标题ID
     */
    private Long tagId;

    /**
     * 状态（1 未完成 2 完成 3 取消）
     */
    private Byte status;

    /**
     * 任务类型（1 收件箱 2 任务 3 循环任务）
     */
    private Byte type;

    /**
     * 规则，只有 type 为 3 才需要。（1 每天一次 2 每周一次）
     */
    private Byte rule;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 是否进行消息通知 1 通知
     */
    private Byte notify;
}
