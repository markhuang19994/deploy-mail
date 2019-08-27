package com.iisi.deploymail.handler.resolver.impl

import com.iisi.deploymail.handler.resolver.LoginHandlerParamResolver
import com.iisi.deploymail.model.resolve.LoginResolveResult
import groovyjarjarcommonscli.MissingArgumentException
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest

@Service
class LoginHandlerParamResolverImpl implements LoginHandlerParamResolver {
    @Override
    LoginResolveResult resolveLoginParam(HttpServletRequest request) {
        def errorMsgs = []
        def engName = request.getParameter('engName')

        if (engName == null) {
            errorMsgs << 'engName'
        }

        if (errorMsgs.size() > 0) {
            throw new MissingArgumentException(
                    "${request.getRequestURI()} Miss arg:${errorMsgs.join(', ')}"
            )
        }

        new LoginResolveResult(engName: engName)
    }

}



