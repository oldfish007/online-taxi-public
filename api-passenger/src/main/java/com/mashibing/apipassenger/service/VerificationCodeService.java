package com.mashibing.apipassenger.service;

import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.response.NumberCodeResponse;
import com.mashibing.apipassenger.remote.ServiceVerificationcodeClient;
import com.mashibing.internal.response.TokenResponse;
import net.sf.json.JSONObject;
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

    public ResponseResult generatorCode(String passenagerPhone){
        //TODO 调用验证服务 ，获取验证码
        //调用远程服务获取6位验证码
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote number code"+numberCode);
        //TODO 存入redis
        String key = verificationCodePrefix+passenagerPhone;
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

        // 根据key获取value

        // 校验验证码

        // 判断原来是否有用户，并进行对应的处理【如果没有用户就插入，如果有就直接返回】

        //颁发令牌

        // 响应
        TokenResponse tokenResponse = new TokenResponse();
       //设置accessToken
        //设置refreshToken
        return ResponseResult.success(tokenResponse);



    }
}
