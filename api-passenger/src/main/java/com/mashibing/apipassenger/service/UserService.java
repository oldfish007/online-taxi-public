package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServicePassengerUserClient;
import com.mashibing.internal.dto.PassengerUser;
import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.dto.TokenResult;
import com.mashibing.internal.request.VerificationCodeDTO;
import com.mashibing.internal.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ServicePassengerUserClient servicePassengerUserClient;
    public ResponseResult getUsers(String accessToken){
        log.info("accessToken"+accessToken);
        //解析token
        TokenResult tokenResult = JwtUtils.checkCode(accessToken);
        log.info("手机号:"+tokenResult.getPhone());
        //远程调用
        ResponseResult<PassengerUser> result = servicePassengerUserClient.findUserByPhone(tokenResult.getPhone());
        return ResponseResult.success(result.getData());

    }
}
