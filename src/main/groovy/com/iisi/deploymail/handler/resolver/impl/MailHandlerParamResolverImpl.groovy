package com.iisi.deploymail.handler.resolver.impl

import com.iisi.deploymail.handler.resolver.MailHandlerParamResolver
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.model.resolve.InitMailResolveResult
import groovyjarjarcommonscli.MissingArgumentException
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest

@Service
class MailHandlerParamResolverImpl implements MailHandlerParamResolver {
    @Override
    InitMailResolveResult resolveInitMailParam(HttpServletRequest request) {
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

        new InitMailResolveResult(
                checkinMailProp: new CheckinMailProp(
                        jenkinsJobName: jenkinsJobName,
                        jenkinsBuildNum: jenkinsBuildNum,
                        projectName: projectName,
                        lacrNo: lacrNo,
                        senderName: senderName
                ),
                checkoutMailProp: new CheckoutMailProp(
                        jenkinsJobName: jenkinsJobName,
                        jenkinsBuildNum: jenkinsBuildNum,
                        projectName: projectName,
                        lacrNo: lacrNo,
                        senderName: senderName
                )
        )
    }
}



