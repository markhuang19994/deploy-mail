package com.iisi.deploymail.handler

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.handler.resolver.MailHandlerParamResolver
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.service.DeployMailService
import com.iisi.deploymail.service.DeployResourcesService
import com.iisi.deploymail.tool.HtmlResource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping('mailHandler')
class MailHandler {

    Logger l = LoggerFactory.getLogger(MailHandler.class)

    @Autowired
    HtmlResource htmlResource

    @Autowired
    MailHandlerParamResolver paramResolver

    @Autowired
    DeployResourcesService deployResourcesService

    @Autowired
    DeployMailService<CheckinMailProp> checkinMailService

    @Autowired
    DeployMailService<CheckoutMailProp> checkoutMailService

    @ResponseBody
    @PostMapping(path = '/checkin')
    String sendCheckin(HttpServletRequest request) {
        def session = request.getSession()
        def checkinMailProp = paramResolver.resolveCheckinHandlerParam(request)
        checkinMailProp.checkinResources = deployResourcesService.downloadCheckinResources(checkinMailProp)
        checkinMailService.sendMail(checkinMailProp)
        return 'Checkin mail send success'
    }

    @ResponseBody
    @PostMapping(path = '/checkout')
    String sendCheckout(HttpServletRequest request) {
        def session = request.getSession()
        def checkoutMailProp = paramResolver.resolveCheckoutHandlerParam(request)
        checkoutMailProp.checkoutResources = deployResourcesService.downloadCheckoutResources(checkoutMailProp)
        checkoutMailService.sendMail(checkoutMailProp)
        return 'Checkout mail send success'
    }
}



