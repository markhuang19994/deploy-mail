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
class CheckoutDeployMailService extends AbstractDeployMailService<CheckoutMailProp> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutDeployMailService.class)

    @Autowired
    CheckoutDeployMailService(Environment env, FtlProvider ftlProvider) {
        super(env, ftlProvider)
    }

    @Override
    Map<String, Object> sendMail(CheckoutMailProp checkoutMailProp) {
        def checkoutResources = checkoutMailProp.checkoutResources
        def checkoutFiles = []

        if (!checkoutMailProp.noSends?.contains('checkoutForm')) {
            checkoutFiles << checkoutResources.checkoutForm
        }
        checkoutFiles << checkoutResources.checkoutTxt

        if (checkoutResources.otherFiles) {
            checkoutFiles += checkoutResources.otherFiles
        }

        String mailAccount = checkoutMailProp.mailAccount ?: env.getProperty('mail.user.name')
        String mailAccountAlias = checkoutMailProp.mailAccountAlias ?: mailAccount
        String password = checkoutMailProp.mailPassword ?: env.getProperty('mail.user.pwd')
        def session = getMailSession()

        def mailAddress = getMailAddress(checkoutMailProp.to, checkoutMailProp.cc, false)
        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(mailAccountAlias))
        message.addRecipients(Message.RecipientType.TO, mailAddress.to)
        if (mailAddress.cc) {
            message.addRecipients(Message.RecipientType.CC, mailAddress.cc)
        }
        message.setSubject(getSubject(checkoutMailProp.lacrNo, checkoutMailProp.projectName))

        def mailHtml = generateMailHtml(checkoutMailProp)
        def note = checkoutMailProp.note != null ? checkoutMailProp.note : '';
        mailHtml.replace('${note}', note)

        Multipart multipart = generateMultipart(checkoutFiles as File[])
        MimeBodyPart messageBodyPart = new MimeBodyPart()
        messageBodyPart.setContent(mailHtml, HTML_MAIL_CONTENT_TYPE)
        multipart.addBodyPart(messageBodyPart)
        message.setContent(multipart, HTML_MAIL_CONTENT_TYPE)

        Transport transport = session.getTransport("smtp")
        transport.connect(mailAccount, password)
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
                senderName : checkoutMailProp.senderName,
                note       : checkoutMailProp.note
        ]
        FreemarkerUtil.processTemplateToString(template, m)
    }

    private static String getSubject(String lacrNo, String projectName) {
        def dateStr = DateUtil.formatNowUtc8Date('MM-dd HH:mm')
        "[COLA-$lacrNo] Checkout [$projectName] [$dateStr]"
    }

}
