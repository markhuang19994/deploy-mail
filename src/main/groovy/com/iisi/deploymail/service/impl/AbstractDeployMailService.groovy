package com.iisi.deploymail.service.impl


import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.model.prop.mail.MailProp
import com.iisi.deploymail.service.DeployMailService
import com.iisi.deploymail.service.DeployMailUserService
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

abstract class AbstractDeployMailService<T extends MailProp> implements DeployMailService<T> {

    static final HTML_MAIL_CONTENT_TYPE = 'text/html; charset=utf-8'
    final Environment env
    final FtlProvider ftlProvider

    AbstractDeployMailService(Environment env, FtlProvider ftlProvider) {
        this.env = env
        this.ftlProvider = ftlProvider
    }

    Session getMailSession() {
        MailHelper.getMailSession(env)
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

    static fillAdvanceSetting(MailProp mailProp, String userEngName, DeployMailUserService deployMailUserService) {
        def mailUser = deployMailUserService.getDeployMailUserByEngName(userEngName)

        def account = mailUser.mailAccount
        def accountAlias = mailUser.mailAccountAlias
        String pwd = MailHelper.decodeMailUserPwd(mailUser.mailPassword)

        mailProp.mailAccount = account
        mailProp.mailAccountAlias = accountAlias
        mailProp.mailPassword = pwd
    }
}
