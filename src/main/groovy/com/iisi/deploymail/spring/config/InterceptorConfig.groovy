package com.iisi.deploymail.spring.config

import com.iisi.deploymail.interceptor.CheckLogInInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    CheckLogInInterceptor checkLogInInterceptor;

    @Override
    void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLogInInterceptor);
    }
}
