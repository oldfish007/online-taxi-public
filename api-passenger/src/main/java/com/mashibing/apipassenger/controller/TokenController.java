package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.service.TokenService;
import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.dto.TokenResult;
import com.mashibing.internal.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/token-refresh")
    public ResponseResult tokenRefresh(@RequestBody TokenResponse tokenResponse){

        return tokenService.refreshToken(tokenResponse.getRefreshToken());
    }
}
