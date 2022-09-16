package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServicePassengerUserClient;
import com.mashibing.internal.common.CommonStatusEnum;
import com.mashibing.internal.common.IdentityConstants;
import com.mashibing.internal.common.TokenConstants;
import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.request.VerificationCodeDTO;
import com.mashibing.internal.response.NumberCodeResponse;
import com.mashibing.apipassenger.remote.ServiceVerificationcodeClient;
import com.mashibing.internal.response.TokenResponse;
import com.mashibing.internal.util.JwtUtils;
import com.mashibing.internal.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class VerificationCodeService {

    private String verificationCodePrefix="passenger-verification-code-";
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ServicePassengerUserClient servicePassengerUserClient;

    public ResponseResult generatorCode(String passenagerPhone){
        //调用远程服务获取6位验证码
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote number code"+numberCode);
        //String key = generatorKey(passenagerPhone);
        String key =RedisPrefixUtils.generatorKeyByPhone(passenagerPhone,IdentityConstants.PASSENGER_IDENTITY);
        stringRedisTemplate.opsForValue().set(key,numberCode+"",2, TimeUnit.MINUTES);
        //todo 对接短信服务运营商
        return ResponseResult.success();


    }

    /**
     *
     * @param passengerPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String passengerPhone, String verificationCode) {
        // 根据手机号，去redis读取验证码
        // 生成key
        //String key = generatorKey(passengerPhone);
        String key = RedisPrefixUtils.generatorKeyByPhone(passengerPhone,IdentityConstants.PASSENGER_IDENTITY);
        // 根据key获取value
        String codeForRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("取出来的验证码"+codeForRedis);
        // 校验验证码
        //1.用户生成的验证码在redis中已经过期了，得提示过期了重新生成
          if(StringUtils.isEmpty(codeForRedis)){
              return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
          }
          if(!verificationCode.trim().equals(codeForRedis.trim())){
              return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
          }

        // todo 判断原来是否有用户，并进行对应的处理【如果没有用户就插入，如果有就直接返回】
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
          verificationCodeDTO.setPassengerPhone(passengerPhone);
        servicePassengerUserClient.loginOrRegister(verificationCodeDTO);
        //todo 验证码校验完成以后就要马上把验证过的验证吗删除掉 主要是看产品设计

        //todo 颁发令牌
        String generatorToken = JwtUtils.generatorToken(passengerPhone, IdentityConstants.PASSENGER_IDENTITY);
        //todo 把token存一个月到服务端的redis,一个月后客户端在传过来一个token反解析一下发现服务端没有了说明他不应该登录
        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(passengerPhone, IdentityConstants.PASSENGER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(accessTokenKey,generatorToken,30,TimeUnit.DAYS);
        // 响应
        TokenResponse tokenResponse = new TokenResponse();
       //设置accessToken
        tokenResponse.setAssessToken(generatorToken);
        tokenResponse.setRefreshToken("refreshToken value");
        //设置refreshToken
        return ResponseResult.success(tokenResponse);



    }

    private String generatorKey(String passengerPhone) {
        return verificationCodePrefix + passengerPhone;
    }
}
