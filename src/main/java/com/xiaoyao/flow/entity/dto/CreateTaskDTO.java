package com.xiaoyao.flow.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskDTO {
    @NotBlank(message = "title 不能为空")
    private String title;
    @NotBlank(message = "主标签不能为空")
    private String firstTag;
    @NotBlank(message = "子标签不能为空")
    private String secondTag;
    @NotNull(message = "请选择平台")
    private Byte platform;
}
