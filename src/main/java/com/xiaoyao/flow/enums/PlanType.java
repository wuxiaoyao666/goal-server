package com.xiaoyao.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanType {
    InBox((byte) 1),
    Task((byte) 2),
    Schedule((byte) 3);
    private final Byte value;
}
