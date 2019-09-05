package com.iisi.deploymail.service.impl

import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.util.DateUtil
import com.iisi.deploymail.util.FreemarkerUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage

@Service('CheckoutMailService')
class CheckoutMailServiceImpl extends AbstractMailServiceImpl<CheckoutMailProp> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutMailServiceImpl.class)

    @Autowired
    CheckoutMailServiceImpl(Environment env, FtlProvider ftlProvider) {
        super(env, ftlProvider)
    }

    @Override
    void sendMail(CheckoutMailProp checkoutMailProp) {
        def checkoutResources = checkoutMailProp.checkoutResources
        def checkinFiles = [checkoutResources.checkoutForm, checkoutResources.checkoutTxt]

        String username = env.getProperty('mail.user.name')
        String password = env.getProperty('mail.user.pwd')
        def session = getMailSession()

        def mailAddress = getMailAddress(checkoutMailProp.to, checkoutMailProp.cc, false)
        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(username))
        message.addRecipients(Message.RecipientType.TO, mailAddress.to)
        if (mailAddress.cc) {
            message.addRecipients(Message.RecipientType.CC, mailAddress.cc)
        }
        message.setSubject(getSubject(checkoutMailProp.projectName, checkoutMailProp.lacrNo))

        Multipart multipart = generateMultipart(checkinFiles as File[])
        MimeBodyPart messageBodyPart = new MimeBodyPart()
        messageBodyPart.setContent(generateMailHtml(checkoutMailProp), HTML_MAIL_CONTENT_TYPE)
        multipart.addBodyPart(messageBodyPart)
        message.setContent(multipart, HTML_MAIL_CONTENT_TYPE)

        Transport transport = session.getTransport("smtp")
        transport.connect(username, password)
        LOGGER.debug("Start send mail to ${mailAddress.toString()}")
        transport.sendMessage(message, message.getAllRecipients())

        LOGGER.debug('Send checkout mail success')
        transport.close()
    }

    private String generateMailHtml(CheckoutMailProp checkoutMailProp) {
        def template = ftlProvider.getFreeMarkerTemplate('/ftl/mail/checkout.ftl')
        def m = [
                projectName: checkoutMailProp.projectName,
                lacrNo     : checkoutMailProp.lacrNo,
                senderName : checkoutMailProp.senderName
        ]
        FreemarkerUtil.processTemplateToString(template, m)
    }

    private static String getSubject(String lacrNo, String projectName) {
        def dateStr = DateUtil.formatNowUtc8Date('MM-dd HH:mm')
        "[COLA-$lacrNo] Checkout [$projectName] [$dateStr]"
    }

}