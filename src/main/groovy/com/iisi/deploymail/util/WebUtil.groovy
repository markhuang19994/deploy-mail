package com.iisi.deploymail.util

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

final class WebUtil {
    private WebUtil() {
        throw new AssertionError()
    }

    static HttpSession createNewSession(HttpServletRequest request) {
        def session = request.getSession(false)
        if (session != null) {
            session.invalidate()
        }
        request.getSession(true)
    }


}
