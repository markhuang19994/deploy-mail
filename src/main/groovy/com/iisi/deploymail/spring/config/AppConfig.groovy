package com.iisi.deploymail.spring.config


import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableAsync
@EnableScheduling
@PropertySource(
        value = ['classpath:config.properties', 'file:////usr/app/config/config.properties'],
        ignoreResourceNotFound = true
)
class AppConfig {

}
