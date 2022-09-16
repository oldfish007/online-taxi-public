package com.mashibing.internal.common;

import lombok.Getter;

public enum CommonStatusEnum {
    /**
     * Token类提示：1100-1199
     */
    TOKEN_ERROR(1199,"token错误"),

    /**
     * 验证码错误提示:1000-1099
     */
    VERIFICATION_CODE_ERROR(1099,"验证码不正确"),
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
