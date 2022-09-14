package com.mashibing.apipassenger.service;

import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.response.NumberCodeResponse;
import com.mashibing.apipassenger.remote.ServiceVerificationcodeClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VerificationCodeService {

    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    public String generatorCode(String passenagerPhone){
        //TODO 调用验证服务 ，获取验证码
        //调用远程服务获取6位验证码
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote number code"+numberCode);
        //TODO 存入redis
        System.out.println("存入redis");
        //返回值
        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("message","success");
        return result.toString();


    }

}
