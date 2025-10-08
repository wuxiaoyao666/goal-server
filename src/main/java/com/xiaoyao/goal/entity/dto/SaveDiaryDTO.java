package com.xiaoyao.goal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 逍遥
 */
@Data
public class SaveDiaryDTO {
    private Long id;
    @NotBlank(message = "标题不能为空")
    private String title;
    private String content;
    /**
     * 标签
     */
    private List<String> tags;
}
