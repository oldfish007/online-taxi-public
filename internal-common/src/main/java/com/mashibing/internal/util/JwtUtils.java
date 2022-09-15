package com.mashibing.internal.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
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

    //生成token
    public static String generatorToken(String passengerPhone){
        Map<String,String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE,passengerPhone);
        //token过期时间
        Calendar calendar = Calendar.getInstance();
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
    public static String parseToken(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        Claim claim = decodedJWT.getClaim(JWT_KEY_PHONE);
        return claim.asString();
    }

    public static void main(String[] args) {

        String token = generatorToken("13438040105");
        System.out.println("生成jwtToken"+token);
        System.out.println("解析后的"+parseToken(token));
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
