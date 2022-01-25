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
        def note = request.getParameter('checkinNote')
        def noSends = request.getParameter('noSends')?.split(',')?.toList()

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
                note: note,
                noSends: noSends,
                to: parseMailTextareaVal(sendToStr),
                cc: parseMailTextareaVal(sendCcStr)
        )
    }

    CheckoutMailProp resolveCheckoutHandlerParam(HttpServletRequest request) {
        def errorMsgs = []
        def sendToStr = request.getParameter('checkoutDefaultSendTo')
        def sendCcStr = request.getParameter('checkoutDefaultSendCc')
        def note = request.getParameter('checkoutNote')
        def noSends = request.getParameter('noSends')?.split(',')?.toList()

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
                note: note,
                noSends: noSends,
                to: parseMailTextareaVal(sendToStr),
                cc: parseMailTextareaVal(sendCcStr)
        )
    }

    ChecksumMailProp resolveChecksumHandlerParam(HttpServletRequest request) {
        def errorMsgs = []
        def sendToStr = request.getParameter('checksumDefaultSendTo')
        def sendCcStr = request.getParameter('checksumDefaultSendCc')
        def note = request.getParameter('checksumNote')

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
                note: note,
                to: parseMailTextareaVal(sendToStr),
                cc: parseMailTextareaVal(sendCcStr)
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
        def checkinSendTo = parseMailTextareaVal(request.getParameter('checkinDefaultSendTo'))
        def checkinSendCc = parseMailTextareaVal(request.getParameter('checkinDefaultSendCc'))
        def checkinConfig = new CheckinConfig(
                defaultSendTo: checkinSendTo,
                defaultSendCC: checkinSendCc
        )

        def checksumSendTo = parseMailTextareaVal(request.getParameter('checksumDefaultSendTo'))
        def checksumSendCc = parseMailTextareaVal(request.getParameter('checksumDefaultSendCc'))
        def checksumConfig = new ChecksumConfig(
                defaultSendTo: checksumSendTo,
                defaultSendCC: checksumSendCc
        )

        def checkoutSendTo = parseMailTextareaVal(request.getParameter('checkoutDefaultSendTo'))
        def checkoutSendCc = parseMailTextareaVal(request.getParameter('checkoutDefaultSendCc'))
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

    @Override
    DeployMailUser resolveSaveAdvanceMailSettingParam(HttpServletRequest request) {
        def account = request.getParameter('account') ?: ''
        def accountAlias = request.getParameter('accountAlias') ?: ''
        def pwd = request.getParameter('pwd')
        new DeployMailUser(
                mailAccount: account,
                mailAccountAlias: accountAlias,
                mailPassword: pwd
        )
    }

    private static List<String> parseMailTextareaVal(String text) {
        def result = text?.split('(;|\r?\n)')?.toList()?.findAll { it != null && it != '' }
        result ?: ['']
    }
}



