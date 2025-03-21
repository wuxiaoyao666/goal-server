package com.xiaoyao.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MobileCreateTaskDTO {
    @NotBlank(message = "title 不能为空")
    private String title;
    @NotBlank(message = "主标签不能为空")
    private String firstTag;
    @NotBlank(message = "子标签不能为空")
    private String secondTag;
}
