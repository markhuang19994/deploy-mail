package com.iisi.deploymail.handler


import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping(['/base'])
class baseHandler {


    @ResponseBody
    @GetMapping('/invalidateSession')
    String getAllDeployUsers(HttpServletRequest request) {
        def session = request.getSession(false)
        if (session != null) {
            session.invalidate()
        }
        'success'
    }


}
