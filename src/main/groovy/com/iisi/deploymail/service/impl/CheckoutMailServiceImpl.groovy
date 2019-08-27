package com.iisi.deploymail.service.impl

import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.model.prop.mail.CommonMailProp
import com.iisi.deploymail.util.FileUtil
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
import java.text.SimpleDateFormat

@Service('CheckoutMailService')
class CheckoutMailServiceImpl extends AbstractMailServiceImpl<CheckoutMailProp> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutMailServiceImpl.class)

    @Autowired
    CheckoutMailServiceImpl(Environment env, FtlProvider ftlProvider) {
        super(env, ftlProvider)
    }

    @Override
    void sendMail(CommonMailProp commonMailProp, CheckoutMailProp checkoutMailProp) {
        def checkoutResources = checkoutMailProp.checkoutResources
        def checkinFiles = [checkoutResources.checkoutForm, checkoutResources.checkoutTxt]

        String username = env.getProperty('mail.user.name')
        String password = env.getProperty('mail.user.pwd')
        def session = getMailSession()

        def mailAddress = getMailAddress(['mark.huang@iisigroup.com'], ['mark.huang@iisigroup.com'], false)
        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(username))
        message.addRecipients(Message.RecipientType.TO, mailAddress.to)
        message.addRecipients(Message.RecipientType.CC, mailAddress.cc)
        message.setSubject(getSubject(commonMailProp.projectName, commonMailProp.lacrNo))

        Multipart multipart = generateMultipart(checkinFiles as File[])
        MimeBodyPart messageBodyPart = new MimeBodyPart()
        messageBodyPart.setContent(generateMailHtml(commonMailProp, checkoutMailProp), HTML_MAIL_CONTENT_TYPE)
        multipart.addBodyPart(messageBodyPart)
        message.setContent(multipart, HTML_MAIL_CONTENT_TYPE)

        Transport transport = session.getTransport("smtp")
        transport.connect(username, password)
        LOGGER.debug("Start send mail to ${mailAddress.toString()}")
        transport.sendMessage(message, message.getAllRecipients())

        LOGGER.debug('Send checkout mail success')
        transport.close()
    }

    private String generateMailHtml(CommonMailProp commonMailProp, CheckoutMailProp checkoutMailProp) {
        def template = ftlProvider.getFreeMarkerTemplate('/ftl/mail/checkout.ftl')
        def m = [
                projectName: commonMailProp.projectName,
                lacrNo     : commonMailProp.lacrNo
        ]
        FreemarkerUtil.processTemplateToString(template, m)
    }

    private static String getSubject(String lacrNo, String projectName) {
        def dateStr = new SimpleDateFormat('MM-dd HH:mm').format(new Date())
        "[COLA-$lacrNo] Checkout [$projectName] [$dateStr]"
    }

}