package com.iisi.deploymail.service.impl

import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.model.prop.mail.MailProp
import com.iisi.deploymail.service.DeployMailService
import org.springframework.core.env.Environment

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.Address
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart

abstract class AbstractMailServiceImpl<T extends MailProp> implements DeployMailService<T> {

    static final HTML_MAIL_CONTENT_TYPE = 'text/html; charset=utf-8'
    final Environment env
    final FtlProvider ftlProvider

    AbstractMailServiceImpl(Environment env, FtlProvider ftlProvider) {
        this.env = env
        this.ftlProvider = ftlProvider
    }

    Session getMailSession() {
        Properties props = [:] as Properties
        props['mail.smtp.auth'] = env.getProperty('mail.smtp.auth')
        props['mail.smtp.starttls.enable'] = env.getProperty('mail.smtp.starttls.enable')
        props['mail.smtp.host'] = env.getProperty('mail.smtp.host')
        props['mail.smtp.port'] = env.getProperty('mail.smtp.port')
        Session.getDefaultInstance(props)
    }

    static Multipart generateMultipart(File... attachFiles) throws MessagingException {
        Multipart multipart = new MimeMultipart()

        for (int i = 0; i < attachFiles.length; i++) {
            File attachFile = attachFiles[i]
            DataSource source = new FileDataSource(attachFile)
            MimeBodyPart messageBodyPart = new MimeBodyPart()
            messageBodyPart.setDataHandler(new DataHandler(source))
            messageBodyPart.setFileName(attachFile.getName())
            multipart.addBodyPart(messageBodyPart, i)
        }

        return multipart
    }

    static Map getMailAddress(List<String> to, List<String> cc, boolean isPreview) {
        def toAddr = to.collect { new InternetAddress(it) } as Address[]
        cc = cc.findAll { it != null && it != '' }

        if (isPreview || cc.size() == 0) {
            return [to: toAddr]
        }

        return [to: toAddr, cc: cc.collect { new InternetAddress(it) } as Address[]]
    }

}