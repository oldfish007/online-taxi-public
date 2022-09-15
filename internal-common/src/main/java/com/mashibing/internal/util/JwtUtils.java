package com.mashibing.internal.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mashibing.internal.dto.TokenResult;
import lombok.val;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtUtils {

    //salt
    private static final String SIGN = "CPFmsb!@#$$";
    private static final String JWT_KEY_PHONE="phone";
    //1.乘客 2 司机
    private static final String JWT_KEY_IDENTITY="identity";

    //手机号类型 司机和 乘客区分

    //生成token
    public static String generatorToken(String passengerPhone,String identity){
        Map<String,String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE,passengerPhone);
        map.put(JWT_KEY_IDENTITY,identity);
        //token过期时间
        Calendar calendar = Calendar.getInstance();
        //当表当月中的哪一天
        calendar.add(Calendar.DATE,1);// +1 天
        Date date = calendar.getTime();

        JWTCreator.Builder builder = JWT.create();
        //整合map
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        //整合过期时间
        builder.withExpiresAt(date);
        //生成token
        String sign = builder.sign(Algorithm.HMAC256(SIGN));
        return sign;
    }
    //解析token
    public static TokenResult parseToken(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = decodedJWT.getClaim(JWT_KEY_PHONE).toString();
        String identity = decodedJWT.getClaim(JWT_KEY_IDENTITY).toString();
        TokenResult tokenResult  = new TokenResult();
        tokenResult.setIdentity(identity);
        tokenResult.setPhone(phone);
        return tokenResult;
    }

    public static void main(String[] args) {

        String token = generatorToken("13438040105","1");

        System.out.println("生成jwtToken"+token);
        System.out.println("解析-----------------");
        TokenResult tokenResult = parseToken(token);
        System.out.println(tokenResult.getIdentity()+":"+tokenResult.getPhone());
    }


  /*  public String createJwtToken(UserDetails userDetails, long timeToExpire, Key key){
        val now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("mooc")
                .claim("authorities",userDetails.getAuthorities().stream()
                        //.map(authority->authority.getAuthority())
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))//变换处理 然后在收集成一个流
                //Sets the JWT Claims sub (subject) value. A null value will remove the property from the Claims. This is a convenience method. It will first ensure a Claims instance exists as the JWT body and then set the Claims subject field w
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now)) //签发时间
                .setExpiration(new Date(now+timeToExpire)) //过期时间
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();//压缩*/


}
