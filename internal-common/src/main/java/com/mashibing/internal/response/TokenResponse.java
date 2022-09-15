package com.mashibing.internal.response;

import lombok.Data;

@Data
public class TokenResponse {

    private String assessToken;
    private String refreshToken;
}
