package com.iisi.deploymail.handler.page

import com.iisi.deploymail.service.IndexService
import com.iisi.deploymail.tool.HtmlResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class IndexHandler {

    @Autowired
    HtmlResource htmlResource

    @Autowired
    IndexService indexService

    @ResponseBody
    @GetMapping('/')
    String indexPage() {
        htmlResource.get('index')
    }

    @ResponseBody
    @GetMapping('/deployMailUsers')
    ResponseEntity<?> getAllDeployUsers() {
        def users = indexService.getAllDeployUsers()
        ResponseEntity.ok(users)
    }


}
