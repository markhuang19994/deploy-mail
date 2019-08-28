package com.iisi.deploymail.handler.resolver.impl

import com.iisi.deploymail.handler.resolver.MailHandlerParamResolver
import com.iisi.deploymail.model.config.CheckinConfig
import com.iisi.deploymail.model.config.CheckoutConfig
import com.iisi.deploymail.model.config.ChecksumConfig
import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.model.prop.mail.ChecksumMailProp
import groovyjarjarcommonscli.MissingArgumentException
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest

@Service
class MailHandlerParamResolverImpl implements MailHandlerParamResolver {


    CheckinMailProp resolveCheckinHandlerParam(HttpServletRequest request) {
        def errorMsgs = []
        def sendToStr = request.getParameter('checkinDefaultSendTo')
        def sendCcStr = request.getParameter('checkinDefaultSendCc')

        if (sendToStr == null) {
            errorMsgs << 'checkinDefaultSendTo'
        }

        if (sendCcStr == null) {
            errorMsgs << 'checkinDefaultSendCc'
        }

        def commonParamMap = resolveCommonParam(request)

        new CheckinMailProp(
                jenkinsJobName: commonParamMap.jenkinsJobName,
                jenkinsBuildNum: commonParamMap.jenkinsBuildNum,
                projectName: commonParamMap.projectName,
                lacrNo: commonParamMap.lacrNo,
                senderName: commonParamMap.senderName,
                to: sendToStr.split(';').collect { it.trim() },
                cc: sendCcStr.split(';').collect { it.trim() }
        )
    }

    CheckoutMailProp resolveCheckoutHandlerParam(HttpServletRequest request) {
        def errorMsgs = []
        def sendToStr = request.getParameter('checkoutDefaultSendTo')
        def sendCcStr = request.getParameter('checkoutDefaultSendCc')

        if (sendToStr == null) {
            errorMsgs << 'checkoutDefaultSendTo'
        }

        if (sendCcStr == null) {
            errorMsgs << 'checkoutDefaultSendCc'
        }

        def commonParamMap = resolveCommonParam(request)

        new CheckoutMailProp(
                jenkinsJobName: commonParamMap.jenkinsJobName,
                jenkinsBuildNum: commonParamMap.jenkinsBuildNum,
                projectName: commonParamMap.projectName,
                lacrNo: commonParamMap.lacrNo,
                senderName: commonParamMap.senderName,
                to: sendToStr.split(';').collect { it.trim() },
                cc: sendCcStr.split(';').collect { it.trim() }
        )
    }

    ChecksumMailProp resolveChecksumHandlerParam(HttpServletRequest request) {
        def errorMsgs = []
        def sendToStr = request.getParameter('checksumDefaultSendTo')
        def sendCcStr = request.getParameter('checksumDefaultSendCc')

        if (sendToStr == null) {
            errorMsgs << 'checksumDefaultSendTo'
        }

        if (sendCcStr == null) {
            errorMsgs << 'checksumDefaultSendCc'
        }

        def commonParamMap = resolveCommonParam(request)

        new ChecksumMailProp(
                jenkinsJobName: commonParamMap.jenkinsJobName,
                jenkinsBuildNum: commonParamMap.jenkinsBuildNum,
                projectName: commonParamMap.projectName,
                lacrNo: commonParamMap.lacrNo,
                senderName: commonParamMap.senderName,
                to: sendToStr.split(';').collect { it.trim() },
                cc: sendCcStr.split(';').collect { it.trim() }
        )
    }

    static Map resolveCommonParam(HttpServletRequest request) {
        def errorMsgs = []
        def jenkinsJobName = request.getParameter('jenkinsJobName')
        def jenkinsBuildNum = request.getParameter('jenkinsBuildNum')
        def projectName = request.getParameter('projectName')
        def lacrNo = request.getParameter('lacrNo')
        def senderName = request.getParameter('senderName')
        if (jenkinsJobName == null) {
            errorMsgs << 'jenkinsJobName'
        }
        if (jenkinsBuildNum == null) {
            errorMsgs << 'jenkinsBuildNum'
        }
        if (projectName == null) {
            errorMsgs << 'projectName'
        }
        if (lacrNo == null) {
            errorMsgs << 'lacrNo'
        }
        if (senderName == null) {
            errorMsgs << 'senderName'
        }
        if (errorMsgs.size() > 0) {
            throw new MissingArgumentException(
                    "${request.getRequestURI()} Miss arg:${errorMsgs.join(', ')}"
            )
        }

        [
                jenkinsJobName : jenkinsJobName,
                jenkinsBuildNum: jenkinsBuildNum,
                projectName    : projectName,
                lacrNo         : lacrNo,
                senderName     : senderName
        ]
    }

    DeployMailUser resolveSaveMailSettingParam(HttpServletRequest request) {
        def checkinSendTo = request.getParameter('checkinDefaultSendTo')?.split(';') ?: ['']
        def checkinSendCc = request.getParameter('checkinDefaultSendCc')?.split(';') ?: ['']
        def checkinConfig = new CheckinConfig(
                defaultSendTo: checkinSendTo,
                defaultSendCC: checkinSendCc
        )

        def checksumSendTo = request.getParameter('checksumDefaultSendTo')?.split(';') ?: ['']
        def checksumSendCc = request.getParameter('checksumDefaultSendCc')?.split(';') ?: ['']
        def checksumConfig = new ChecksumConfig(
                defaultSendTo: checksumSendTo,
                defaultSendCC: checksumSendCc
        )

        def checkoutSendTo = request.getParameter('checkoutDefaultSendTo')?.split(';') ?: ['']
        def checkoutSendCc = request.getParameter('checkoutDefaultSendCc')?.split(';') ?: ['']
        def checkoutConfig = new CheckoutConfig(
                defaultSendTo: checkoutSendTo,
                defaultSendCC: checkoutSendCc
        )


        new DeployMailUser(
                checkinConfig: checkinConfig,
                checksumConfig: checksumConfig,
                checkoutConfig: checkoutConfig
        )
    }
}



