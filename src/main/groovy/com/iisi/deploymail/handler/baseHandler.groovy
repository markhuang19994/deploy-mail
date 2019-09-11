package com.iisi.deploymail.handler

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.service.DeployMailUserService
import com.iisi.deploymail.util.WebUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping(['/base'])
class baseHandler {

    @Autowired
    DeployMailUserService deployMailUserService

    @ResponseBody
    @GetMapping('/invalidateSession')
    String invalidateSession(HttpServletRequest request) {
        def session = request.getSession(false)
        if (session != null) {
            session.invalidate()
        }
        'success'
    }

    @ResponseBody
    @PostMapping('/getUserData')
    ResponseEntity<?> getUserData(HttpServletRequest request) {
        def session = request.getSession(false)
        def userName = String.valueOf(session.getAttribute(Constants.USER_ENG_NAME))
        def deployMailUser = deployMailUserService.getDeployMailUserByEngName(userName)
        ResponseEntity.ok(deployMailUser)
    }


}
