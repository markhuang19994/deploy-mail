package com.iisi.deploymail.handler.page

import com.iisi.deploymail.service.DeployMailUserService
import com.iisi.deploymail.tool.HtmlResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(['/', '/indexHandler'])
class IndexHandler {

    @Autowired
    HtmlResource htmlResource

    @Autowired
    DeployMailUserService deployMailUserService

    @ResponseBody
    @GetMapping('/index')
    String indexPage() {
        htmlResource.get('index')
    }

    @ResponseBody
    @GetMapping('/deployMailUsers')
    ResponseEntity<?> getAllDeployUsers() {
        def users = deployMailUserService.getAllDeployMailUserNames()
        ResponseEntity.ok(users)
    }


}
