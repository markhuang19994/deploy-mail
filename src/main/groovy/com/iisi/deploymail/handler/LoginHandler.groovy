package com.iisi.deploymail.handler

import com.iisi.deploymail.handler.resolver.LoginHandlerParamResolver
import com.iisi.deploymail.service.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

@Controller
class LoginHandler {

    @Autowired
    LoginHandlerParamResolver loginHandlerParamResolver

    @Autowired
    LoginService loginService

    LoginHandler(){
        println 213
    }

    @ResponseBody
    @GetMapping('/login')
    ResponseEntity<?> login(HttpServletRequest request) {
        def result = loginHandlerParamResolver.resolveLoginParam(request)
        def deployMailUser =  loginService.login(result.engName)
        ResponseEntity.ok(deployMailUser)
    }

}



