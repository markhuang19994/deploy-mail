package com.iisi.deploymail.spring.config


import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(['classpath:/other/docker/app/config/config.properties', '/usr/app/config/config.properties'])
class AppConfig {

}