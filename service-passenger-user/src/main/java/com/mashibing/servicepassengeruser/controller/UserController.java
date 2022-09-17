package com.mashibing.servicepassengeruser.controller;

import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.request.VerificationCodeDTO;
import com.mashibing.servicepassengeruser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/user")
    public ResponseResult loginOrRegister(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        return userService.loginOrRegister(passengerPhone);
    }

    @GetMapping("/user/{phone}")
    public ResponseResult findUserByPhone(@PathVariable("phone") String passengerPhone){

        System.out.println("service-passenger-user: phone:"+passengerPhone);
        ResponseResult userByPhone = userService.findUserByPhone(passengerPhone);
        System.out.println(userByPhone);
        return userByPhone;
    }
}
