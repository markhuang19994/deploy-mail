package com.iisi.deploymail.handler.resolver

import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.model.prop.mail.ChecksumMailProp

import javax.servlet.http.HttpServletRequest

interface MailHandlerParamResolver {

    CheckinMailProp resolveCheckinHandlerParam(HttpServletRequest request)

    CheckoutMailProp resolveCheckoutHandlerParam(HttpServletRequest request)

    ChecksumMailProp resolveChecksumHandlerParam(HttpServletRequest request)

    DeployMailUser resolveSaveMailSettingParam(HttpServletRequest request)
}



