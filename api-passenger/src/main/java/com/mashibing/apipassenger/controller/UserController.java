package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.service.UserService;
import com.mashibing.internal.common.CommonStatusEnum;
import com.mashibing.internal.common.TokenConstants;
import com.mashibing.internal.dto.PassengerUser;
import com.mashibing.internal.dto.ResponseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseResult users(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(!bearerToken.startsWith("Bearer")){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        String[] tokens = bearerToken.split(" ");
        if(!(tokens.length==2)){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        String token = tokens[1];
        PassengerUser users = userService.getUsers(token);
        ResponseResult result = new ResponseResult();
        result.setCode(1);
        result.setMessage("success");
        result.setData(users);

        return result;
    }
}
