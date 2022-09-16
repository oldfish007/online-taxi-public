package com.mashibing.apipassenger.interceptor;

import com.auth0.jwt.exceptions.*;
import com.mashibing.internal.common.TokenConstants;
import com.mashibing.internal.dto.ResponseResult;
import com.mashibing.internal.dto.TokenResult;
import com.mashibing.internal.util.JwtUtils;
import com.mashibing.internal.util.RedisPrefixUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * stringRedisTemplate 包npe
 * 是在springbean实例化之前初始化的 也就是这个拦截器已经初始化了 bean还没有初始化
 * 需要拦截器执行之前 把这个stringRedisTemplate初始化了
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
        TokenResult tokenResult = null;
        try{
            tokenResult = JwtUtils.parseToken(token);
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
        //从redis中取出token
        if(tokenResult==null){
            resultString="token invalid";
            result=false;
        }else{
            //拼接KEY
            String assessTokenkey = RedisPrefixUtils.generatorTokenKey(tokenResult.getPhone(), tokenResult.getIdentity(), TokenConstants.ACCESS_TOKEN_TYPE);
            String tokenRedis = stringRedisTemplate.opsForValue().get(assessTokenkey);
        //比较我们传入的token和redis中token是否相当
            if ((StringUtils.isBlank(tokenRedis))  || (!token.trim().equals(tokenRedis.trim()))){
                resultString = "access token invalid";
                result = false;
            }
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
