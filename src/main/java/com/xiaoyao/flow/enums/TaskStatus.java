package com.xiaoyao.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    IN_PROGRESS((byte) 1),
    COMPLETED((byte) 2),
    NOT_SHOW((byte) 3);

    private final byte value;
}
