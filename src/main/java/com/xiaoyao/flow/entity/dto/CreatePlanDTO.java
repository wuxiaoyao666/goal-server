package com.xiaoyao.flow.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author 逍遥
 */
@Data
public class CreatePlanDTO {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotNull(message = "标签不能为空")
    private Long tagId;
    /// 类型
    private Byte type = 1;
    /// 结束日期
    private LocalDate endDate;
    /// 结束时间
    private LocalTime endTime;
}
