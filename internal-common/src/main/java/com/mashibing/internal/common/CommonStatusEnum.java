package com.mashibing.internal.common;

import lombok.Getter;

public enum CommonStatusEnum {
    SUCCESS(1,"SUCCESS"),
    FAIL(0,"FAIL");
    @Getter
    private int code;
    @Getter
    private String value;

    CommonStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }


}
