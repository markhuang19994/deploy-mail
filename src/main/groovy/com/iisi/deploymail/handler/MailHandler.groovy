package com.iisi.deploymail.handler

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.handler.resolver.MailHandlerParamResolver
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.model.prop.mail.ChecksumMailProp
import com.iisi.deploymail.service.DeployMailService
import com.iisi.deploymail.service.DeployMailUserService
import com.iisi.deploymail.service.DeployResourcesService
import com.iisi.deploymail.service.impl.AbstractMailServiceImpl
import com.iisi.deploymail.tool.HtmlResource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
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
    DeployMailUserService deployMailUserService

    @Autowired
    DeployMailService<CheckinMailProp> checkinMailService

    @Autowired
    DeployMailService<CheckoutMailProp> checkoutMailService

    @Autowired
    DeployMailService<ChecksumMailProp> checksumMailService

    @ResponseBody
    @PostMapping(path = '/checkin')
    String sendCheckin(HttpServletRequest request) {
        def session = request.getSession()
        def checkinMailProp = paramResolver.resolveCheckinHandlerParam(request)
        checkinMailProp.checkinResources = deployResourcesService.downloadCheckinResources(checkinMailProp)
        def userEngName = session.getAttribute(Constants.USER_ENG_NAME).toString()
        AbstractMailServiceImpl.fillAdvanceSetting(checkinMailProp, userEngName, deployMailUserService)
        checkinMailService.sendMail(checkinMailProp)
        return 'Checkin mail send success'
    }

    @ResponseBody
    @PostMapping(path = '/checkout')
    String sendCheckout(HttpServletRequest request) {
        def session = request.getSession()
        def checkoutMailProp = paramResolver.resolveCheckoutHandlerParam(request)
        checkoutMailProp.checkoutResources = deployResourcesService.downloadCheckoutResources(checkoutMailProp)
        def userEngName = session.getAttribute(Constants.USER_ENG_NAME).toString()
        AbstractMailServiceImpl.fillAdvanceSetting(checkoutMailProp, userEngName, deployMailUserService)
        checkoutMailService.sendMail(checkoutMailProp)
        return 'Checkout mail send success'
    }

    @ResponseBody
    @PostMapping(path = '/checksum')
    String sendChecksum(HttpServletRequest request) {
        def session = request.getSession()
        def checksumMailProp = paramResolver.resolveChecksumHandlerParam(request)
        checksumMailProp.checksumResources = deployResourcesService.downloadChecksumResources(checksumMailProp)
        def userEngName = session.getAttribute(Constants.USER_ENG_NAME).toString()
        AbstractMailServiceImpl.fillAdvanceSetting(checksumMailProp, userEngName, deployMailUserService)
        checksumMailService.sendMail(checksumMailProp)
        return 'Checksum mail send success'
    }

    @ResponseBody
    @PostMapping(path = '/saveMailSetting')
    String saveMailSetting(HttpServletRequest request) {
        def deployMailUser = paramResolver.resolveSaveMailSettingParam(request)

        def session = request.getSession()
        def engName = session.getAttribute(Constants.USER_ENG_NAME)
        deployMailUser.engName = engName

        def updateCount = deployMailUserService.updateDeployMailUser(deployMailUser)
        return "資料儲存${updateCount > 0 ? '成功' : '失敗'}"
    }

    @ResponseBody
    @PostMapping(path = '/saveAdvanceMailSetting')
    String saveAdvanceMailSetting(HttpServletRequest request) {
        def deployMailUser = paramResolver.resolveSaveAdvanceMailSettingParam(request)
        def session = request.getSession()
        deployMailUser.engName = session.getAttribute(Constants.USER_ENG_NAME)
        def updateCount = deployMailUserService.updateDeployMailUserSetting(deployMailUser)
        return "資料儲存${updateCount > 0 ? '成功' : '失敗'}"
    }
}



