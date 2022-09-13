package com.mashibing.apipassenger.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.sf.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Service;


@Service
public class VerificationCodeService {

    public String generatorCode(String passenagerPhone){
        //TODO 调用验证服务 ，获取验证码

        //TODO 存入redis
        System.out.println("存入redis");
        //返回值
        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("message","success");
        return result.toString();


    }

}
