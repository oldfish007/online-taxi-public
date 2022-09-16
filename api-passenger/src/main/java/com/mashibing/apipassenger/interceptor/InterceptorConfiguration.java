package com.mashibing.apipassenger.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
//这样初始化的时候 bean就已经提前有了
//拦截器里面的bean就会一个一个注入进去
    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/noauthTest")
                .excludePathPatterns(Arrays.asList("/verification-code","/verification-code-check","/token-refresh"))
            ;

    }
}
