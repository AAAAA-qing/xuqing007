package com.fh.shopapi.argumentReslover;


import com.fh.shopapi.resolver.MemberMethodArgumentReslover;
import com.fh.shopapi.interceptor.IdempotentInterceptor;
import com.fh.shopapi.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
      registry.addInterceptor(idempotentInterceptor()).addPathPatterns("/**");
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        argumentResolvers.add(memberMethodArgumentReslover());
    }


    @Bean
    public MemberMethodArgumentReslover memberMethodArgumentReslover(){
        return new MemberMethodArgumentReslover();
    }

   @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Bean
    public IdempotentInterceptor idempotentInterceptor(){
        return new IdempotentInterceptor();
    }

}
