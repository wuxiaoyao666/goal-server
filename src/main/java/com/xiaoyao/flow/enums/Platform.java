package com.xiaoyao.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    PC((byte) 1),
    APP((byte) 2);

    private final byte value;
}
