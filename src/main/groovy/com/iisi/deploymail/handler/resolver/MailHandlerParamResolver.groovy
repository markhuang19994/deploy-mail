package com.iisi.deploymail.handler.resolver

import com.iisi.deploymail.model.resolve.InitMailResolveResult

import javax.servlet.http.HttpServletRequest

interface MailHandlerParamResolver {

    InitMailResolveResult resolveInitMailParam(HttpServletRequest request)

}



