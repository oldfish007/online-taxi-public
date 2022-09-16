package com.mashibing.apipassenger.service;

import com.mashibing.internal.common.CommonStatusEnum;
import com.mashibing.internal.common.TokenConstants;
import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.dto.TokenResult;
import com.mashibing.internal.response.TokenResponse;
import com.mashibing.internal.util.JwtUtils;
import com.mashibing.internal.util.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
 @Autowired
 private StringRedisTemplate stringRedisTemplate;
    public ResponseResult refreshToken(String refreshTokenSrc){
         //首先解析
        TokenResult tokenResult = JwtUtils.parseToken(refreshTokenSrc);
        //客户端传过来refreshToke 和redis中取出来的比较
        if(tokenResult==null){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        String phone = tokenResult.getPhone();
        String identity = tokenResult.getIdentity();
        //解析就是校验 通过以后  首先生成refreshKEY 从redis拿出原来的refreshKey进行比较
        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(phone, identity, TokenConstants.REFRESH_TOKEN_TYPE);
        String refreshTokenRedis = stringRedisTemplate.opsForValue().get(refreshTokenKey);
        if ((StringUtils.isBlank(refreshTokenRedis))  || (!refreshTokenSrc.trim().equals(refreshTokenRedis.trim()))){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        //首先重新生成新的accessToken refreshToken  之后按照放入生成的KEY 放入redis
        String assessTokenKey = RedisPrefixUtils.generatorTokenKey(tokenResult.getPhone(), tokenResult.getIdentity(), TokenConstants.ACCESS_TOKEN_TYPE);
        String generatorAccessToken = JwtUtils.generatorToken(phone, identity, TokenConstants.ACCESS_TOKEN_TYPE);
        String generatorRefreshToken = JwtUtils.generatorToken(phone, identity, TokenConstants.REFRESH_TOKEN_TYPE);
        //最新的 token 存入redis中
        stringRedisTemplate.opsForValue().set(assessTokenKey,generatorAccessToken,15,TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey,generatorRefreshToken,50,TimeUnit.SECONDS);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAssessToken(generatorAccessToken);
        tokenResponse.setRefreshToken(generatorRefreshToken);
        return ResponseResult.success(tokenResponse);
    }
}
