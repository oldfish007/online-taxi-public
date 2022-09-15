package com.mashibing.servicepassengeruser.service;

import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.request.VerificationCodeDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    public ResponseResult loginOrRegister(String  passengerPhone){
        System.out.println("usersercice被调用了"+passengerPhone);
        //根据手机号查询用户信息

        //判断用户信息是否存在

        //如果不存在，插入用户

        return ResponseResult.success();
    }
}
