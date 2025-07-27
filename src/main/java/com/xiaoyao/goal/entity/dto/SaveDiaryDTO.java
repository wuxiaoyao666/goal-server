package com.xiaoyao.goal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 逍遥
 */
@Data
public class SaveDiaryDTO {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "文章不能为空")
    private String content;
    private LocalDate date = LocalDate.now();
}
