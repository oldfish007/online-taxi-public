package com.mashibing.apipassenger.interceptor;

import com.auth0.jwt.exceptions.*;
import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.dto.TokenResult;
import com.mashibing.internal.util.JwtUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor extends HandlerInterceptorAdapter {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String resultString="";
        boolean result=true;
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.isEmpty(bearerToken)){
            result=false;
            return result;
        }
        if(!StringUtils.startsWith("Bearer","Bearer")){
            result=false;
            return result;
        }
        String[] tokens = bearerToken.split(" ");
        String token = tokens[1];
        try{
             JwtUtils.parseToken(token);
        }catch (SignatureVerificationException e){
            resultString="token sign error";
            result=false;
        }catch(TokenExpiredException e){
            resultString="token time out";
            result=false;
        }catch(AlgorithmMismatchException e){
            resultString="token AlgorithmMismatchException";
            result=false;
        }catch(InvalidClaimException e){
            resultString="token InvalidClaimException";
            result=false;
        }catch(JWTDecodeException e){
            resultString="token JWTDecodeException";
            result=false;
        }
        if(!result){
            PrintWriter out = response.getWriter();
            out.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
