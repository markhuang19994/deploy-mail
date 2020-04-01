package com.iisi.deploymail.service.impl

import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.model.prop.mail.ChecksumMailProp
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

@Service('ChecksumMailService')
class ChecksumMailServiceImpl extends AbstractMailServiceImpl<ChecksumMailProp> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksumMailServiceImpl.class)

    @Autowired
    ChecksumMailServiceImpl(Environment env, FtlProvider ftlProvider) {
        super(env, ftlProvider)
    }

    @Override
    void sendMail(ChecksumMailProp checksumMailProp) {
        def checksumResources = checksumMailProp.checksumResources

        def checksumFiles = [checksumResources.checksum]
        if (checksumResources.otherFiles) {
            checksumFiles += checksumResources.otherFiles
        }

        String mailAccount = checksumMailProp.mailAccount ?: env.getProperty('mail.user.name')
        String mailAccountAlias = checksumMailProp.mailAccountAlias ?: mailAccount
        String password = checksumMailProp.mailPassword ?: env.getProperty('mail.user.pwd')
        def session = getMailSession()

        def mailAddress = getMailAddress(checksumMailProp.to, checksumMailProp.cc, false)
        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(mailAccountAlias))
        message.addRecipients(Message.RecipientType.TO, mailAddress.to)
        if (mailAddress.cc) {
            message.addRecipients(Message.RecipientType.CC, mailAddress.cc)
        }
        message.setSubject(getSubject(checksumMailProp.lacrNo, checksumMailProp.projectName))

        def mailHtml = generateMailHtml(checksumMailProp)
        def note = checksumMailProp.note != null ? checksumMailProp.note : '';
        mailHtml.replace('${note}', note)

        Multipart multipart = generateMultipart(checksumFiles as File[])
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

    private String generateMailHtml(ChecksumMailProp checksumMailProp) {
        def template = ftlProvider.getFreeMarkerTemplate('/ftl/mail/checksum.ftl')
        def m = [
                projectName: checksumMailProp.projectName,
                lacrNo     : checksumMailProp.lacrNo,
                senderName : checksumMailProp.senderName,
                note       : checksumMailProp.note
        ]
        FreemarkerUtil.processTemplateToString(template, m)
    }

    private static String getSubject(String lacrNo, String projectName) {
        def dateStr = DateUtil.formatNowUtc8Date('MM-dd HH:mm')
        "[COLA-$lacrNo] Checksum [$projectName] [$dateStr]"
    }

}
