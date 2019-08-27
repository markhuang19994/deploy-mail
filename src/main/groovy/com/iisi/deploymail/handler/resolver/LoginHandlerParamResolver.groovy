package com.iisi.deploymail.handler.resolver

import com.iisi.deploymail.model.resolve.InitMailResolveResult
import com.iisi.deploymail.model.resolve.LoginResolveResult

import javax.servlet.http.HttpServletRequest

interface LoginHandlerParamResolver {

    LoginResolveResult resolveLoginParam(HttpServletRequest request)

}



