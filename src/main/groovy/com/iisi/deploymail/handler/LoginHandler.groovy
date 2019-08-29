package com.iisi.deploymail.handler

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.handler.resolver.LoginHandlerParamResolver
import com.iisi.deploymail.service.LoginService
import com.iisi.deploymail.util.WebUtil
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

    @ResponseBody
    @GetMapping('/login')
    ResponseEntity<?> login(HttpServletRequest request) {
        def result = loginHandlerParamResolver.resolveLoginParam(request)
        def deployMailUser =  loginService.login(result.engName)
        def session = WebUtil.createNewSession(request)
        session.setAttribute(Constants.USER_ENG_NAME, result.engName)
        session.setAttribute(Constants.Flag.IS_LOGIN, Boolean.TRUE)
        ResponseEntity.ok(deployMailUser)
    }

}



