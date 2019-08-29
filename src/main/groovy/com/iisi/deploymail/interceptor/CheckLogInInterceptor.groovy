package com.iisi.deploymail.interceptor

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.handler.page.IndexHandler
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CheckLogInInterceptor implements HandlerInterceptor {
    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            def handlerMethod = handler as HandlerMethod
            def methodName = handlerMethod.getMethod().getName()
            if (methodName in ['login', 'invalidateSession']
                    || handlerMethod.getBeanType() == IndexHandler.class) {
                return true;
            }
            def session = request.getSession(false)
            if (session == null) {
                response.sendError(401, 'user not login')
                return false
            }

            def isLogin = Boolean.TRUE == session.getAttribute(Constants.Flag.IS_LOGIN)
            if (isLogin) {
                return true
            }
            response.sendError(401, 'user not login')
            return false
        }


        return true;
    }

    @Override
    void postHandle(HttpServletRequest request, HttpServletResponse response,
                    Object handler, ModelAndView modelAndView) {

    }

    @Override
    void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                         Object handler, Exception exception) {

    }
}

