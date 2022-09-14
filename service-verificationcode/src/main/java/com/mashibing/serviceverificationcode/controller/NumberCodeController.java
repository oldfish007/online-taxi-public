package com.mashibing.serviceverificationcode.controller;

import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.response.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberCodeController {

    @GetMapping("/numberCode/{size}")
    public ResponseResult numberCode(@PathVariable("size") int  size){
        double random = (Math.random()*9+1)*(Math.pow(10,size-1));
        int generateCode = (int)random;
        System.out.println("generator src code:"+generateCode);
        /*JSONObject result = new JSONObject();
        result.put("code","1");
        result.put("message","success");
        JSONObject data = new JSONObject();
        data.put("numberCode",generateCode);
        result.put("data",data);*/

        NumberCodeResponse numberCodeResponse =  new NumberCodeResponse();
        numberCodeResponse.setNumberCode(generateCode);


        return ResponseResult.success(numberCodeResponse);
    }

    /*public static void main(String[] args) {
        double random = (Math.random()*9+1)*Math.pow(10,6);
        System.out.println(random);
        int code = (int)random;
        System.out.println(code);
    }*/
}
