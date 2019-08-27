package com.iisi.deploymail.handler

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.handler.resolver.MailHandlerParamResolver
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.service.DeployMailService
import com.iisi.deploymail.service.DeployResourcesService
import com.iisi.deploymail.tool.HtmlResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

@Controller
class MailHandler {

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
    @GetMapping(path = '/initMail')
    String initMail(HttpServletRequest request) {
        def initMailParam = paramResolver.resolveInitMailParam(request)
        def session = request.getSession()
        session.setAttribute(Constants.CHECKIN_MAIL_PROP, initMailParam.checkinMailProp)
        session.setAttribute(Constants.CHECKOUT_MAIL_PROP, initMailParam.checkoutMailProp)
        return 'Initialized mail success'
    }

    @ResponseBody
    @GetMapping(path = '/checkin')
    String sendCheckin(HttpServletRequest request) {
        def session = request.getSession()
        def checkinMailProp = session.getAttribute(Constants.CHECKIN_MAIL_PROP) as CheckinMailProp
        checkinMailProp.checkinResources = deployResourcesService.downloadCheckinResources(commonMailProp)
        checkinMailService.sendMail(checkinMailProp)
        return 'Checkin mail send success'
    }

    @ResponseBody
    @GetMapping(path = '/checkout')
    String sendCheckout(HttpServletRequest request) {
        def session = request.getSession()
        def checkoutMailProp = session.getAttribute(Constants.CHECKOUT_MAIL_PROP) as CheckoutMailProp
        checkoutMailProp.checkoutResources = deployResourcesService.downloadCheckoutResources(commonMailProp)
        checkoutMailService.sendMail(checkoutMailProp)
        return 'Checkout mail send success'
    }
}



