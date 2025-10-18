package com.xiaoyao.goal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 逍遥
 */
@Getter
@AllArgsConstructor
public enum DiaryFileType {
    SG(".sg"),
    HTML(".html"),
    MD(".md");

    private final String value;
}
