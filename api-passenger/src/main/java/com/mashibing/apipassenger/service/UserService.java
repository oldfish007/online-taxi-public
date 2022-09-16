package com.mashibing.apipassenger.service;

import com.mashibing.internal.dto.PassengerUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public PassengerUser getUsers(String token){

        //从库里面去取

        PassengerUser passengerUser = new PassengerUser();
        passengerUser.setPassengerName("张三");
        passengerUser.setProfilePhoto("头像");

        return passengerUser;

    }
}
